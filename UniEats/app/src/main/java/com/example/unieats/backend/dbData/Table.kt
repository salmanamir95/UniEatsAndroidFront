package com.example.unieats.backend.dbData

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Table(
    val id: String = "",
    val number: Int = 0,
    val seats: Int = 4,
    val reservedBy: String? = null
) {
    val isReserved: Boolean
        get() = !reservedBy.isNullOrEmpty()

    fun reserve(studentId: String) = copy(reservedBy = studentId)
    fun cancelReservation() = copy(reservedBy = null)
}
