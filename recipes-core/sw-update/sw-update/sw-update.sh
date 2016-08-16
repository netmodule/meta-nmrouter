#!/bin/sh
# Software update script

# Local variables
IMAGE_LOCATION=""
IMAGE_FW_VERSION=""
IMAGE_HW_VERSION=""
IMAGE_PROD_COMPATIBILITY=""
IMAGE_MD5=""
TMP_RFS_MOUNT="/tmp/new_rfs"

# Load platform specific configuration
source /etc/default/sw_update_config

#-------------------------------------------------------------------------------
# Print log with level $1 and message $2
log() {
  local level=${1?}
  shift
  local code=
  local line="[$(date '+%F %T')] $level: $*"
  if [ -t 2 ]
  then
    case "$level" in
      INFO) code=36 ;;
      DEBUG) code=32 ;;
      WARN) code=33 ;;
      ERROR) code=31 ;;
      *) code=37 ;;
    esac
    echo -e "\e[${code}m${line}\e[0m"
  else
    echo "$line"
  fi >&2
}

#-------------------------------------------------------------------------------
# Extract image header information and update local variables
extract_header_infos()
{
  log INFO "Extracting firmware header information ... "

  IMAGE_HEADER=$(head ${IMAGE_LOCATION} -n1 | sed 's/;bz2-image=.*//g')
  log INFO "$IMAGE_HEADER"
  oIFS=$IFS
  IFS=";"
  for field in "${FIELDS[@]}"
  do
    case "${field}" in
      attocube-fw=*)
        IMAGE_FW_VERSION=$(echo $field | cut -d= -f2)
        ;;
      hwver=*)
        IMAGE_HW_VERSION=$(echo $field | cut -d= -f2)
        ;;
      prod_compatibility=*)
        IMAGE_PROD_COMPATIBILITY=$(echo $field | cut -d= -f2)
        ;;
      md5sum=*)
        IMAGE_MD5=$(echo $field | cut -d= -f2)
        ;;
      *)
        log WARNING "Unknown header field ${field}"
        ;;
    esac
  done
  IFS=$oIFS
}

#-------------------------------------------------------------------------------
# Check if the given image is compatible with the hardware
check_image_compatibility()
{
  log INFO "Checking firmware compatibility ... "

  # Prod variant
  if [ "${PLATFORM_PROD_COMPATIBILITY}" != "${IMAGE_PROD_COMPATIBILITY}" ]
  then
    log ERROR "Incompatible product variant ${PLATFORM_PROD_COMPATIBILITY} != ${IMAGE_PROD_COMPATIBILITY}"
    exit -1
  fi

  # HW version, the image can be for more than one HW
  found=0
  oIFS=$IFS
  IFS='|'
  for hw_vesion in $IMAGE_HW_VERSION
  do
    if [ "${hw_vesion}" = "${PLATFORM_HW_VERSION}" ]
    then
      found=1
      break
    fi
  done
  IFS=$oIFS

  if [ "${found}" = "0" ]
  then
    log ERROR "Incompatible hardware version: ${PLATFORM_HW_VERSION} not in ${IMAGE_HW_VERSION}"
    exit -1
  fi
}

#-------------------------------------------------------------------------------
# Extract the image from the binary
extract_image()
{
  EXTRACTED_IMAGE=$IMAGE_LOCATION.tmp
  cat $IMAGE_LOCATION | sed '1{/.*bz2-image=/d}' > $EXTRACTED_IMAGE
}

#-------------------------------------------------------------------------------
# Check the md5sum of the firmware image
check_image_md5()
{
  log INFO "Checking image MD5 ..."

  COMPUTED_MD5=$(md5sum $EXTRACTED_IMAGE | awk '{print $1}')
  if [ "${COMPUTED_MD5}" != "${IMAGE_MD5}" ]
  then
    log ERROR "MD5 mismatch ${COMPUTED_MD5} != ${IMAGE_MD5}"
    exit -1
  fi
}

#-------------------------------------------------------------------------------
# Flash the firmware on the inactive partition
flash_firmware()
{
  if [ "${PLATFORM_ACTIVE_PARTITION}" == "1" ]
  then
    # the active partition is 1 so we have to flash 0
    DEST_PARTITION=${PLATFORM_FIRST_PARTITION}
  else
    DEST_PARTITION=${PLATFORM_SECOND_PARTITION}
  fi

  log INFO "Formatting partition ${DEST_PARTITION} ..."
  mkfs.ext4 ${DEST_PARTITION}
  if [ "$?" != "0" ]
  then
    log ERROR "Formating parition ${DEST_PARTITION} failed"
    exit -1
  fi

  log INFO "Extracting firmware ..."
  mkdir -p ${TMP_RFS_MOUNT}
  if [ ! -d ${TMP_RFS_MOUNT} ]
  then
    log ERROR "Couldn't create folder ${TMP_RFS_MOUNT}"
    exit -1
  fi

  mount ${DEST_PARTITION} ${TMP_RFS_MOUNT}
  if [ "$?" != "0" ]
  then
    log ERROR "Couldn't mount ${DEST_PARTITION} to ${TMP_RFS_MOUNT}"
    exit -1
  fi

  tar -xzf ${EXTRACTED_IMAGE} -C ${TMP_RFS_MOUNT}
  if [ "$?" != "0" ]
  then
    log ERROR "Error extracting new firmware"
    exit -1
  fi

  umount ${TMP_RFS_MOUNT}
  if [ "${PLATFORM_ACTIVE_PARTITION}" == "1" ]
  then
    set_active_partition 0
  else
    set_active_partition 1
  fi
}

cleanup()
{
  log INFO "cleanup..."
  rm $EXTRACTED_IMAGE
}

check_file()
{
  if [ ! -e "$1" ]
  then
    log ERROR "Please provide a valid update image"
    exit -1
  else
    IMAGE_LOCATION=$1
  fi
}

get_bin()
{
  echo $(dd if=$IMAGE_LOCATION bs=1 count=$2 skip=$1 2>/dev/null | hexdump -ve '1/1 "%.2x"')
}

update_linux()
{
  log INFO "Starting linux update ..."
  check_file $1

  head=$(get_bin 0 2)
  if [ "a$head" == "a1f8b" ]; then
    log INFO "Found valid gzip archive"
    # gzip header found, normal image
    # Don't use the header stuff for now, move the file instead
    EXTRACTED_IMAGE=$IMAGE_LOCATION
  else
    log INFO "Search gzip archive in image"
    for i in $(seq 0 1024); do
      head=$(get_bin $i 2)
      if [ "a$head" == "a1f8b" ]; then
        log INFO "Found gzip archive in image, extracting now"
        EXTRACTED_IMAGE=$(mktemp)
        dd if=$IMAGE_LOCATION bs=$i skip=1 of=$EXTRACTED_IMAGE 2>/dev/null
        break
      fi
    done
    test $i -eq 1024 && echo "Could not find a vaild gzip archive" && exit -1
  fi

  # This is not the original sw-update, we use plain tar.gz
  # extract_header_infos
  # check_image_compatibility
  # extract_image
  # Also don't check the md5 sum
  # check_image_md5
  log INFO "Installing linux update ..."

  flash_firmware
  cleanup

  log INFO "Linux update succeed!"
}

update_uboot()
{
  log INFO "Starting u-boot update ..."
  check_file $1

  head_should=$(echo -e "\x27\x05\x19\x56")
  head_is=$(dd if=$1 bs=1 count=4 2>/dev/null)
  if [ "$head_is" != "$head_should" ]; then
    log ERROR "Header of the input image seems invalid, abort update"
    return
  fi

  dd if=$1 of=/dev/mmcblk0 bs=512 seek=768 &> /dev/null
  if [ $? -ne 0 ]; then
    log ERROR "Failed to write u-boot to mmcblk0"
    return
  fi
  log INFO "u-boot update succeed!"
}

update_spl()
{
  log INFO "Starting spl update ..."
  check_file $1

  head_should=$(echo -e "\x40\x00\x00\x00")
  head_is=$(dd if=$1 bs=1 count=4 2>/dev/null)
  if [ "$head_is" != "$head_should" ]; then
    log ERROR "Header of the input image seems invalid, abort update"
    return
  fi

  dd if=$1 of=/dev/mmcblk0 bs=512 seek=256 &> /dev/null
  if [ $? -ne 0 ]; then
    log ERROR "Failed to write spl to mmcblk0"
    return
  fi
  log INFO "spl update succeed!"
}

############################ Start of script ###################################

while getopts ":l:u:s:h" opt; do
  case $opt in
    l)
      update_linux $OPTARG
      ;;
    u)
      update_uboot $OPTARG
      ;;
    s)
      update_spl $OPTARG
      ;;
    h)
      echo "Invalid option: -$OPTARG" >&2
      ;;
  esac
done

exit 0
