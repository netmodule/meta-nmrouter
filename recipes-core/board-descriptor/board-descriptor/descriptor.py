import json


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


_string_to_type = {
    "String": _array_to_string,
    "Date": _array_to_string,
    "UInt8": _array_to_int,
    "UInt16": _array_to_int,
    "UInt32": _array_to_int,
    "UInt64": _array_to_int,
    "MAC": _array_to_mac,
    "IPV4": _array_to_string,
    "Parition": _array_to_string,
    "Parition64": _array_to_string,
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


def _not_supported_yet(data):
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
    "Parition": _string_to_array,
    "Parition64": _string_to_array,
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
    def __init__(self, strtype, value):
        self.value = self._value_type(strtype, value)

    def _value_type(self, strtype, value):
        for key, fun in _string_to_type.items():
            if key == strtype:
                return fun(value)


class Descriptor:
    def __init__(self, file, offset, max_size, config_table):
        self.file = file
        self.offset = offset
        self.max_size = max_size
        self.config_table = json.loads(config_table)

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

    def _parse_data(self, bdraw):
        bdparsed = {}
        tlv_by_name = {}
        for tlv in bdraw.tlvs:
            name, value = self._get_name_value(tlv)
            bdparsed[name] = value
            tlv_by_name[name] = tlv

        return bdparsed, tlv_by_name

    def read(self):
        self.bdraw = self._read_raw_bd()
        self.bdparsed, self.tlv_by_name = self._parse_data(self.bdraw)

    def get_list(self):
        return self.bdparsed

    def get(self, name):
        return self.bdparsed[name]

    def _write_bd(self, pos, data, maxlen):
        fd = open(self.file, "r+b")
        fd.seek(self.offset + pos, 0)
        fd.write(data[0:maxlen])
        fd.close()

    def _do_set(self, name, value, tlv):
        for key, config_item in self.config_table.items():
            if tlv.tag == config_item["id"]:
                type = config_item["type"]
                break

        if type is None:
            raise ValueError("Could not found tag type")

        for key, fun in _type_to_value.items():
            if key == type:
                self._write_bd(tlv.pos, fun(value), tlv.length)

    def set(self, name, value):
        if self.tlv_by_name is None:
            raise ImportError("Descriptor not read yet, run read_all first")

        tlv = self.tlv_by_name.get(name)
        if tlv is None:
            return False

        if not self.bd.is_writable:
            raise IOError("This operation is not permitted on \
                          this descriptor (ro)")
        self._do_set(name, value, tlv)
        return True
