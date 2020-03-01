package com.omise.tamboon.extensions

fun String.isValidCreditCardNumber(): Boolean{
    var sum = 0
    var alternate = false
    for (i in this.length - 1 downTo 0) {
        var n: Int = this.substring(i, i + 1).toInt()
        if (alternate) {
            n *= 2
            if (n > 9) {
                n = n % 10 + 1
            }
        }
        sum += n
        alternate = !alternate
    }
    return sum % 10 == 0
}