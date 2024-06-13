package com.example.backoffice.domain.product.model

enum class Status {
    ON_DISCOUNT,
    ON_SALE,
    SOLD_OUT;

    companion object {
        fun calc(stock: Int?, discountRate: Int?) : Status {
            return if(stock!! < 1)  SOLD_OUT
            else if(discountRate!!>0) ON_SALE
            else ON_DISCOUNT
        }
    }
}