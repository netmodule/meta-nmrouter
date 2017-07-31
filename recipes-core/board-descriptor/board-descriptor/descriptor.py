
def _array_to_int(data):
    # This one works for python2.7 and python3
    import codecs
    return int(codecs.encode(data, "hex"), 16)


def _array_to_string(data):
    return data.decode("utf-8")


def _array_to_hash(data):
    return bytearray(data)


def _array_to_mac(data):
    return ':'.join('%02x' % b for b in data)


def _array_to_partition(data):
    import codecs
    part = {
        "flags": data[0],
        "fstype": data[1],
        "offset": int(codecs.encode(data[2:6], "hex"), 16),
        "size": int(codecs.encode(data[6:10], "hex"), 16),
        "name": data[10:].decode("utf-8")
    }

    return part


def _array_to_partition64(data):
    import codecs
    part = {
        "flags": data[0],
        "fstype": data[1],
        "fsoptions": data[3],
        "offset": int(codecs.encode(data[8:16], "hex"), 16),
        "size": int(codecs.encode(data[16:24], "hex"), 16),
        "name": data[24:].decode("utf-8")
    }

    return part


_string_to_type = {
    "String": _array_to_string,
    "Date": _array_to_string,
    "UInt8": _array_to_int,
    "UInt16": _array_to_int,
    "UInt32": _array_to_int,
    "UInt64": _array_to_int,
    "MAC": _array_to_mac,
    "IPV4": _array_to_string,
    "Partition": _array_to_partition,
    "Partition64": _array_to_partition64,
    "Hash": _array_to_hash
}


def _string_to_array(data):
    return bytearray(data, "utf8")


def _uint_to_array(data, format):
    value = int(data)
    import struct
    return struct.pack(format, value)


def _uint8_to_array(data):
    return _uint_to_array(data, "B")


def _uint16_to_array(data):
    return _uint_to_array(data, "H")


def _uint32_to_array(data):
    return _uint_to_array(data, "L")


def _uint64_to_array(data):
    return _uint_to_array(data, "Q")


def _dict_to_partition(data):
    bd_data = bytearray()
    bd_data.extend(_uint8_to_array(data["flags"]))
    bd_data.extend(_uint8_to_array(data["fstype"]))
    bd_data.extend(_uint32_to_array(data["offset"]))
    bd_data.extend(_uint32_to_array(data["size"]))
    bd_data.extend(_string_to_array(data["name"]))
    return bd_data


def _dict_to_partition64(data):
    bd_data = bytearray()
    bd_data.extend(_uint8_to_array(data["flags"]))
    bd_data.extend(_uint8_to_array(data["fstype"]))
    bd_data.extend(_uint8_to_array(data["fsoptions"]))
    bd_data.extend([0]*5)
    bd_data.extend(_uint64_to_array(data["offset"]))
    bd_data.extend(_uint64_to_array(data["size"]))
    bd_data.extend(_string_to_array(data["name"]))
    return bd_data


def _not_supported_yet(data):
    del data
    raise NotImplementedError("Setting this type is not supported yet")

_type_to_value = {
    "String": _string_to_array,
    "Date": _string_to_array,
    "UInt8": _uint8_to_array,
    "UInt16": _uint16_to_array,
    "UInt32": _uint32_to_array,
    "UInt64": _uint64_to_array,
    "MAC": _not_supported_yet,
    "IPV4": _string_to_array,
    "Partition": _dict_to_partition,
    "Partition64": _dict_to_partition64,
    "Hash": _not_supported_yet
}


class _Tlv:
    def __init__(self, tag, length, value, pos):
        self.tag = tag
        self.length = length
        self.value = value
        self.pos = pos


class _BdRaw:
    def _verify_checksum(self, data, should_sum):
        is_sum = sum(data) & 0xffff
        if is_sum != should_sum:
            raise ValueError("Checksum does not match (should: {0}, is: {1}"
                             .format(should_sum, is_sum))

    def _get_hw(self, data):
        return (data[0] << 8) + data[1]

    def _read_binary(self, data):
        i = 0
        self.id = data[i:i + 4]
        i += 4
        self.buffer_len = self._get_hw(data[i:])
        i += 2
        self.checksum = self._get_hw(data[i:])
        i += 2
        j = 0
        self.tlvs = []
        while i < self.buffer_len + 8:
            # Zero means end of bd
            tag = self._get_hw(data[i:i + 2])
            i += 2
            if tag == 0:
                i -= 2
                break
            length = self._get_hw(data[i:i + 2])
            i += 2
            value = data[i: i + length]
            self.tlvs.append(_Tlv(tag, length, value, i))
            i += length
            j += 1

        return i

    def __init__(self, data):
        self.is_writable = False

        data_length = self._read_binary(data)

        if self.checksum != 0:
            self._verify_checksum(data[8:data_length], self.checksum)
        else:
            self.is_writable = True


class _BdParsed:
    def __init__(self, str_type, value):
        self.value = self._value_type(str_type, value)

    def _value_type(self, str_type, value):
        for key, fun in _string_to_type.items():
            if key == str_type:
                return fun(value)


class Descriptor:
    def __init__(self, file, offset, max_size, config_table):
        self.file = file
        self.offset = offset
        self.max_size = max_size
        self.config_table = config_table
        self.bd_raw = None
        self.bd_parsed = None
        self.tlv_by_name = None

    def _read_raw_bd(self):
        fd = open(self.file, "rb")
        fd.seek(self.offset)
        data = fd.read(self.max_size)
        fd.close()

        # python2.7 reads string instead of bytearray
        if type(data) is str:
            data = bytearray(data)

        return _BdRaw(data)

    def _get_name_value(self, tlv):
        for key, value in self.config_table.items():
            if tlv.tag == value["id"]:
                parsed = _BdParsed(value["type"], tlv.value)
                return key, parsed.value
        return "unknown_" + str(tlv.tag), tlv.value

    def _parse_data(self, bdraw):
        bdparsed = {}
        tlv_by_name = {}
        for tlv in bdraw.tlvs:
            name, value = self._get_name_value(tlv)
            key = name
            i = 1
            while key in bdparsed:
                key = name + "_" + str(i)
                i += 1
            bdparsed[key] = value
            tlv_by_name[key] = tlv

        return bdparsed, tlv_by_name

    def read(self):
        self.bd_raw = self._read_raw_bd()
        self.bd_parsed, self.tlv_by_name = self._parse_data(self.bd_raw)

    def get_list(self):
        return self.bd_parsed

    def get(self, name):
        name = name.split(".")
        element = self.bd_parsed
        # Search the final element to print
        # This allows names in the form partition64.flags
        for key in name:
            if key not in element:
                return None
            element = element[key]
        return element

    def _write_bd(self, pos, data, maxlen):
        fd = open(self.file, "r+b")
        fd.seek(self.offset + pos, 0)
        fd.write(data[0:maxlen])
        fd.close()

    def _update_dict(self, dictionary, element_to_change, value):
        # We are at the end of all elements, so change value
        if len(element_to_change) == 1:
            dictionary[element_to_change[0]] = value
        else:
            # Create new dicitionary with changed elememts
            dictionary[element_to_change[0]] = self._update_dict(
                dictionary[element_to_change[0]], element_to_change[1:], value)
        return dictionary

    def _do_set(self, name, value, tlv):
        tlv_type = None

        for key, config_item in self.config_table.items():
            if tlv.tag == config_item["id"]:
                tlv_type = config_item["type"]
                break

        if tlv_type is None:
            raise ValueError("Could not find tag type")

        for key, fun in _type_to_value.items():
            if key == tlv_type:
                if not type(name) is list:
                    self._write_bd(tlv.pos, fun(value), tlv.length)
                else:
                    element = self.bd_parsed[name[0]]
                    # if we have changed the element it will be our value again
                    value = self._update_dict(element, name[1:], value)
                    self._write_bd(tlv.pos, fun(value), tlv.length)
                self.bd_parsed[name[0]] = value

    def set(self, name, value):
        if self.tlv_by_name is None:
            raise ImportError("Descriptor not read yet, run read_all first")

        name = name.split(".")

        tlv = self.tlv_by_name.get(name[0])
        if tlv is None:
            return False

        if not self.bd_raw.is_writable:
            raise IOError("This operation is not permitted on "
                          "this descriptor (ro)")

        # if it's not a name in the form partition.flags then
        # we don't use name as array
        if len(name) == 1:
            name = name[0]

        self._do_set(name, value, tlv)
        return True
