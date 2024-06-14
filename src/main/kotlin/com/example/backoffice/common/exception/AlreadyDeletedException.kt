package com.example.backoffice.common.exception

class AlreadyDeletedException(val modelName: String, val id: Long) : RuntimeException(
    "$modelName with id $id is already deleted."
)