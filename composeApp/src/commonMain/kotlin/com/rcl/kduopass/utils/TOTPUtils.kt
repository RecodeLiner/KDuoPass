package com.rcl.kduopass.utils

import okio.ByteString
import kotlin.experimental.xor
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object TOTPUtils {
    private const val TIME_STEP: Long = 30L
    private const val DIGITS: Int = 6

    @OptIn(ExperimentalTime::class)
    fun generateTOTP(secret: String): String {
        val currentTime = Clock.System.now()
        val time = currentTime.toEpochMilliseconds() / 1000L
        val timeSteps = time / TIME_STEP
        val key = secret.fromBase32()

        val timeBytes = ByteArray(8) { i ->
            ((timeSteps shr ((7 - i) * 8)) and 0xFF).toByte()
        }

        val hmac = hmacSHA1(key, timeBytes)

        val offset = hmac[hmac.size - 1].toInt() and 0x0F
        val truncatedHash = hmac.copyOfRange(offset, offset + 4)

        val code = truncatedHash.fold(0) { acc, byte -> (acc shl 8) or (byte.toInt() and 0xFF) }
        val otp = code and 0x7FFFFFFF

        return otp.toString().takeLast(DIGITS)
    }

    private fun hmacSHA1(key: ByteArray, message: ByteArray): ByteArray {
        val blockSize = 64
        val actualKey = if (key.size > blockSize) {
            ByteString.of(*key).sha1().toByteArray()
        } else {
            key
        }

        val keyPadded = ByteArray(blockSize)
        actualKey.copyInto(keyPadded)

        val oKeyPad = ByteArray(blockSize) { keyPadded[it] xor 0x5c }
        val iKeyPad = ByteArray(blockSize) { keyPadded[it] xor 0x36 }

        val innerDigest = ByteString.of(*iKeyPad, *message).sha1()
        return ByteString.of(*oKeyPad, *innerDigest.toByteArray()).sha1().toByteArray()
    }
}
