#!/usr/bin/python

from optparse import OptionParser
import json
import sys


def read_config(config_file):
    fd = open(config_file, "r")
    config = fd.read()
    fd.close()
    return json.loads(config)


def read_all(bd):
    for key, value in bd.items():
        print ("{0}: {1}".format(key, value))


def read_key(bd, key):
    print(bd[key])


def main():
    import descriptor

    usage = "usage: %prog [options] arg"
    parser = OptionParser(usage)
    parser.add_option("-c", "--config", action="store", type="string",
                      dest="config",
                      help="Configuration file to load",
                      default="/etc/bd/config.json")

    parser.add_option("-a", "--read-all", action="store_true", dest="read_all",
                      help="Read everything from the boarddescriptors",
                      default=False)

    parser.add_option("-r", "--read-key", action="store", dest="read_key",
                      type="string",
                      help="Read key from the boarddescritpors",
                      default=None)

    parser.add_option("-w", "--write-key", action="store", dest="write_key",
                      type="string",
                      help="Try to write key to boarddescriptor",
                      default=None)

    parser.add_option("-v", "--write-value", action="store", dest="write_val",
                      type="string",
                      help="Value to write if -w (must be specified)",
                      default=None)

    (options, args) = parser.parse_args()

    config = read_config(options.config)

    # Read the tag -> name -> type list
    fd = open(config["tag_list"], "r")
    config_table = fd.read()

    bd = {}
    descs = []
    for eeprom in config["eeprom"]:
        for valid_bd in eeprom["valid_bds"]:
            desc = descriptor.Descriptor(eeprom["path"], valid_bd["start"],
                                         valid_bd["size"], config_table)
            desc.read()
            bd.update(desc.get_list())
            descs.append(desc)

    if options.read_all:
        read_all(bd)

    if options.read_key is not None:
        read_key(bd, options.read_key)

    if options.write_key is not None:
        if options.write_val is None:
            print("No value write specified, please set -v")
            sys.exit(10)
        for desc in descs:
            desc.set(options.write_key, options.write_val)


if __name__ == "__main__":
    main()
