SOC_FAMILY = "armada"
require conf/machine/include/soc-family.inc

DEFAULTTUNE ?= "cortexa9thf-neon"
require conf/machine/include/tune-cortexa9.inc

# Default providers, may need to override for specific machines
PREFERRED_PROVIDER_virtual/bootloader = "u-boot"
PREFERRED_PROVIDER_u-boot = "u-boot"

KERNEL_IMAGETYPE = "zImage"

UBOOT_ARCH = "arm"
UBOOT_MACHINE = "mvebu"

UBOOT_ENTRYPOINT = "0x80008000"
UBOOT_LOADADDRESS = "0x80008000"

# List common SoC features, may need to add touchscreen for specific machines
MACHINE_FEATURES = "kernel26 usbgadget usbhost vfat ext2 ext4 ethernet"
