package com.georgebindragon.android.base.media

import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class WavWriter {
    fun writePcm16(
        outputStream: OutputStream,
        samples: ShortArray,
        sampleRateHz: Int,
        channelCount: Int,
    ) {
        require(sampleRateHz > 0) { "sampleRateHz must be positive." }
        require(channelCount > 0) { "channelCount must be positive." }

        val bytesPerSample = Short.SIZE_BYTES
        val dataSize = samples.size * bytesPerSample
        val byteRate = sampleRateHz * channelCount * bytesPerSample
        val blockAlign = channelCount * bytesPerSample

        outputStream.writeAscii("RIFF")
        outputStream.writeIntLe(36 + dataSize)
        outputStream.writeAscii("WAVE")
        outputStream.writeAscii("fmt ")
        outputStream.writeIntLe(16)
        outputStream.writeShortLe(1)
        outputStream.writeShortLe(channelCount)
        outputStream.writeIntLe(sampleRateHz)
        outputStream.writeIntLe(byteRate)
        outputStream.writeShortLe(blockAlign)
        outputStream.writeShortLe(16)
        outputStream.writeAscii("data")
        outputStream.writeIntLe(dataSize)

        val buffer = ByteBuffer.allocate(dataSize).order(ByteOrder.LITTLE_ENDIAN)
        samples.forEach(buffer::putShort)
        outputStream.write(buffer.array())
    }

    private fun OutputStream.writeAscii(value: String) {
        write(value.toByteArray(Charsets.US_ASCII))
    }

    private fun OutputStream.writeIntLe(value: Int) {
        write(
            byteArrayOf(
                value.toByte(),
                (value shr 8).toByte(),
                (value shr 16).toByte(),
                (value shr 24).toByte(),
            ),
        )
    }

    private fun OutputStream.writeShortLe(value: Int) {
        write(
            byteArrayOf(
                value.toByte(),
                (value shr 8).toByte(),
            ),
        )
    }
}
