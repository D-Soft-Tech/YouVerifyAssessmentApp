package com.example.youverifyassessment.domain.encryption

import com.google.android.gms.common.util.Hex
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class DataEncryptionUseCase(
    private val secretKey: String,
    private val iv: String,
    private val encryptionAlgType: String,
    private val encryptionPaddingType: String
) : DataEncryptionContract {

    private fun getCipher(cipherMode: Int): Cipher =
        Cipher.getInstance(encryptionPaddingType).apply {
            init(
                cipherMode,
                SecretKeySpec(
                    secretKey.toByteArray(StandardCharsets.UTF_8),
                    encryptionAlgType,
                ),
                IvParameterSpec(this@DataEncryptionUseCase.iv.toByteArray(StandardCharsets.UTF_8)),
            )
        }

    override fun encrypt(data: String): String =
        Hex.bytesToStringLowercase(
            getCipher(Cipher.ENCRYPT_MODE).doFinal(data.toByteArray(StandardCharsets.UTF_8)),
        )

    override fun decrypt(encryptedData: String): String =
        String(
            getCipher(Cipher.DECRYPT_MODE).doFinal(
                Hex.stringToBytes(encryptedData),
            ),
            StandardCharsets.UTF_8,
        )
}