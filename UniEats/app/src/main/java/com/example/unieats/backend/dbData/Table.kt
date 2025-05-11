package com.example.unieats.backend.dbData

data class Table(
    val id: String = "",
    val number: Int = 0,
    val seats: Int = 4,
    var reservedBy: String? = null
) {
    val isReserved: Boolean
        get() = !reservedBy.isNullOrEmpty()
}