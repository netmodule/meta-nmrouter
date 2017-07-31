#!/usr/bin/python

from optparse import OptionParser
import json
import sys


def read_config(config_file):
    fd = open(config_file, "r")
    config = fd.read()
    fd.close()
    return json.loads(config)


def read_all(descs, args=None):
    for desc in descs:
        for key, value in desc.get_list().items():
            print ("{0}: {1}".format(key, value))


def read_key(descs, args):
    key = args[0]
    value = None
    for desc in descs:
        value = desc.get(key)
        if value is not None:
            break
    print(value)


def write_key(descs, args):
    value_updated = False
    if len(args) == 0:
        raise ValueError("Invalid argument to write")

    key_value = args[0]
    [key, value] = key_value.split("=")
    for desc in descs:
        if desc.set(key, value):
            value_updated = True
            break

    if not value_updated:
        raise ValueError("Key not found or invalid value")

cmd_table = {
    "read": read_key,
    "write": write_key,
    "read-all": read_all
}


def read_descriptors(config):
    import descriptor

    # Read the tag -> name -> type list
    fd = open(config["tag_list"], "r")
    config_table = fd.read()

    descs = []
    for eeprom in config["eeprom"]:
        for valid_bd in eeprom["valid_bds"]:
            desc = descriptor.Descriptor(eeprom["path"],
                                         valid_bd["start"],
                                         valid_bd["size"],
                                         json.loads(config_table))
            desc.read()
            descs.append(desc)
    return descs


def main():
    usage = "usage: %prog [options] read/read-all/write\n\n"
    usage = usage + "Commands:\n"
    usage = usage + "  read <key>: reads the value of a descriptor entry\n"
    usage = usage + "  write <key>=<value>: write the value to a descriptor,\n"
    usage = usage + "                       key can be partition64.flags\n"
    usage = usage + "  read-all: read all values from the descriptor"
    parser = OptionParser(usage)
    parser.add_option("-c", "--config", action="store", type="string",
                      dest="config",
                      help="Configuration file to load",
                      default="/etc/bd/config.json")

    (options, args) = parser.parse_args()

    try:
        config = read_config(options.config)

        if len(sys.argv) < 2:
            parser.print_help()

        cmd = sys.argv[1]
        if cmd not in cmd_table:
            parser.print_help()

        descs = read_descriptors(config)

        for cmd_name, fun in cmd_table.items():
            if cmd_name == cmd:
                fun(descs, sys.argv[2:])
                break
    except ValueError as ex:
        print("")
        print("Invalid syntax:")
        print(ex.message)
        print("")
        parser.print_help()
    except:
        print("")
        print("Boarddescriptor operation failed with:")
        exc_type, exc_obj, exc_traceback = sys.exc_info()
        print((exc_type, exc_obj))
        traceback_details = {
            'filename': exc_traceback.tb_frame.f_code.co_filename,
            'lineno': exc_traceback.tb_lineno,
            'name': exc_traceback.tb_frame.f_code.co_name,
            'type': exc_type.__name__,
            }
        print(traceback_details)


if __name__ == "__main__":
    main()
