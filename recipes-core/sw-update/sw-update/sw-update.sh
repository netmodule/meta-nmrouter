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
  local code= line="[$(date '+%F %T')] $level: $*"
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

############################ Start of script ###################################
if [ ! -e "$1" ]
then
  log ERROR "Please provide a valid update image"
  exit -1
else
  IMAGE_LOCATION=$1
fi

log INFO "Starting update ..."

#extract_header_infos
#check_image_compatibility
# extract_image
# Don't use the header stuff for now, move the file instead
EXTRACTED_IMAGE=$IMAGE_LOCATION
# Also don't check the md5 sum
# check_image_md5
flash_firmware
cleanup

log INFO "Software update succeed!"

exit 0
