package com.example.youverifyassessment.domain.model

data class PaymentCardEntity(
    val id: Long = 0,
    val cardType: String,
    val cardTitle: String,
    val cardNumber: String,
    val cardCVV: String,
    val cardExpiryMonth: Int,
    val cardExpiryYear: Int,
    var selected: Boolean = false,
    val cardHolderName: String = ""
) {
    companion object {
        val paymentCardEntities = listOf<PaymentCardEntity>(
            PaymentCardEntity(
                1,
                PaymentCardOptions.MASTER_CARD.cardScheme,
                "First Bank",
                "9090983373310900",
                "101",
                1,
                2024,
                true,
                "John Doe Mark"
            ),
            PaymentCardEntity(
                2,
                PaymentCardOptions.VISA_CARD.cardScheme,
                "GT Bank",
                "3763092000922738",
                "337",
                9,
                2023,
                false,
                "Anthony Oluwafemi Joshua"
            ),
            PaymentCardEntity(
                2,
                PaymentCardOptions.VERVE_CARD.cardScheme,
                "Access Bank",
                "5060666666666666666",
                "123",
                12,
                2025,
                false,
                "Chukwuebuka Haruna Kunle"
            )
        )
    }
}

fun PaymentCardEntity.mapToPaymentCard(): PaymentCard {
    return PaymentCard(
        cardType = this.cardType,
        cardTitle = this.cardTitle,
        cardNumber = this.cardNumber,
        cardCVV = this.cardCVV,
        cardExpiryMonth = this.cardExpiryMonth,
        cardExpiryYear = this.cardExpiryYear,
        selected = this.selected,
        cardHolderName = cardHolderName
    )
}