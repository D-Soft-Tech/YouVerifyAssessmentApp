package com.example.youverifyassessment.domain.model

enum class PaymentCardOptions(val cardScheme: String) {
    MASTER_CARD("MasterCard"),
    VISA_CARD("VISA"),
    VERVE_CARD("VerVe")
}