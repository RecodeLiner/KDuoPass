package com.rcl.kduopass.utils


private const val BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
private val BASE32_DECODE_MAP = IntArray(128) { -1 }.apply {
    for (i in BASE32_ALPHABET.indices) {
        this[BASE32_ALPHABET[i].code] = i
    }
}

/**
 * Перевод ByteArray в Base32
 */
fun ByteArray.toBase32(): String {
    val result = StringBuilder()
    var bits = 0
    var value = 0
    var index = 0

    for (byte in this) {
        value = (value shl 8) or (byte.toInt() and 0xFF)
        bits += 8

        while (bits >= 5) {
            bits -= 5
            result.append(BASE32_ALPHABET[value shr bits and 0x1F])
            index++
        }
    }

    if (bits > 0) {
        result.append(BASE32_ALPHABET[value shl (5 - bits) and 0x1F])
        index++
    }

    // Дополняем строку символами '=' до длины, кратной 8
    while (index % 8 != 0) {
        result.append('=')
        index++
    }

    return result.toString()
}

/**
 * Перевод Base32 в ByteArray
 */
fun String.fromBase32(): ByteArray {
    val base32 = this.uppercase().replace("=", "")
    val output = ByteArray((base32.length * 5 / 8))
    var bits = 0
    var value = 0
    var index = 0

    for (char in base32) {
        val decoded = BASE32_DECODE_MAP[char.code]
        if (decoded == -1) {
            throw IllegalArgumentException("Invalid Base32 character: $char")
        }

        value = (value shl 5) or decoded
        bits += 5

        if (bits >= 8) {
            bits -= 8
            output[index++] = (value shr bits and 0xFF).toByte()
        }
    }

    return output.copyOf(index)
}