package com.example.youverifyassessment.domain.model

import com.example.youverifyassessment.utils.UtilsAndExtensions.maskCardPan

data class PaymentCard(
    val cardType: String,
    val cardTitle: String,
    val cardNumber: String,
    val cardCVV: String,
    val cardExpiryMonth: Int,
    val cardExpiryYear: Int,
    val selected: Boolean = false,
    val cardHolderName: String = ""
) {
    val cardMaskedPan = cardNumber.maskCardPan()
}

fun PaymentCard.mapToPaymentCardEntity(): PaymentCardEntity {
    return PaymentCardEntity(
        cardType = this.cardType,
        cardTitle = this.cardTitle,
        cardNumber = this.cardNumber,
        cardCVV = this.cardCVV,
        cardExpiryMonth = this.cardExpiryMonth,
        cardExpiryYear = this.cardExpiryYear
    )
}
