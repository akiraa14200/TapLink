package app.taplink.android.nfc

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import java.nio.charset.StandardCharsets

/**
 * Proof-of-concept NFC Forum Type 4 Tag NDEF endpoint.
 * The receiving device must run a compatible NFC reader during this prototype.
 */
class TapLinkHceService : HostApduService() {
    private var selectedFile: ByteArray? = null

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        if (commandApdu == null || commandApdu.size < 4) return SW_WRONG_LENGTH

        // SELECT the NFC Forum Type 4 Tag application.
        if (commandApdu.startsWith(SELECT_NDEF_APPLICATION)) {
            selectedFile = null
            return SW_OK
        }

        // SELECT Capability Container (E103) or NDEF file (E104).
        if (commandApdu.size >= 7 &&
            commandApdu[0] == 0x00.toByte() &&
            commandApdu[1] == 0xA4.toByte() &&
            commandApdu[2] == 0x00.toByte()
        ) {
            selectedFile = when {
                commandApdu[5] == 0xE1.toByte() && commandApdu[6] == 0x03.toByte() -> CAPABILITY_CONTAINER
                commandApdu[5] == 0xE1.toByte() && commandApdu[6] == 0x04.toByte() -> buildNdefFile()
                else -> null
            }
            return if (selectedFile != null) SW_OK else SW_FILE_NOT_FOUND
        }

        // READ BINARY.
        if (commandApdu[0] == 0x00.toByte() && commandApdu[1] == 0xB0.toByte()) {
            val file = selectedFile ?: return SW_FILE_NOT_FOUND
            val offset = ((commandApdu[2].toInt() and 0xFF) shl 8) or
                (commandApdu[3].toInt() and 0xFF)
            if (offset >= file.size) return SW_WRONG_OFFSET
            val requested = if (commandApdu.size >= 5) commandApdu[4].toInt() and 0xFF else 0
            val length = if (requested == 0) 256 else requested
            val end = minOf(file.size, offset + length)
            return file.copyOfRange(offset, end) + SW_OK
        }

        return SW_INS_NOT_SUPPORTED
    }

    override fun onDeactivated(reason: Int) {
        selectedFile = null
    }

    private fun buildNdefFile(): ByteArray {
        val username = getSharedPreferences("taplink_nfc", MODE_PRIVATE)
            .getString("username", "")
            .orEmpty()
        val safeUsername = username.filter { it.isLetterOrDigit() || it == '.' || it == '_' }
        val url = "https://taplink.app/@$safeUsername"
        val urlBytes = url.removePrefix("https://").toByteArray(StandardCharsets.UTF_8)
        val payload = byteArrayOf(0x04) + urlBytes // URI prefix code for https://
        val record = byteArrayOf(
            0xD1.toByte(), 0x01, payload.size.toByte(), 0x55
        ) + payload
        val ndefMessage = record
        val length = ndefMessage.size
        return byteArrayOf((length shr 8).toByte(), length.toByte()) + ndefMessage
    }

    private fun ByteArray.startsWith(prefix: ByteArray): Boolean =
        size >= prefix.size && prefix.indices.all { this[it] == prefix[it] }

    companion object {
        private val SELECT_NDEF_APPLICATION = byteArrayOf(
            0x00, 0xA4.toByte(), 0x04, 0x00, 0x07,
            0xD2.toByte(), 0x76, 0x00, 0x00, 0x85.toByte(), 0x01, 0x01
        )
        private val CAPABILITY_CONTAINER = byteArrayOf(
            0x00, 0x0F, 0x20, 0x00, 0x3B, 0x00, 0x34,
            0x04, 0x06, 0xE1.toByte(), 0x04, 0x00, 0xFF.toByte(), 0x00, 0xFF.toByte()
        )
        private val SW_OK = byteArrayOf(0x90.toByte(), 0x00)
        private val SW_WRONG_LENGTH = byteArrayOf(0x67, 0x00)
        private val SW_FILE_NOT_FOUND = byteArrayOf(0x6A, 0x82.toByte())
        private val SW_WRONG_OFFSET = byteArrayOf(0x6B, 0x00)
        private val SW_INS_NOT_SUPPORTED = byteArrayOf(0x6D, 0x00)
    }
}
