package com.example.youverifyassessment.domain.encryption

interface DataEncryptionContract {
    /**
     * Encrypts the plain text, [data], provided.
     *
     * @param [data] The string that is to be encrypted
     * @return [String] The encrypted data resulting from the encryption process
     * */
    fun encrypt(data: String): String

    /**
     * Decrypts the encrypted string, [encryptedData]
     *
     * @param [encryptedData] the String to be decrypted.
     * @return [String] The decrypted string.
     * */
    fun decrypt(encryptedData: String): String
}