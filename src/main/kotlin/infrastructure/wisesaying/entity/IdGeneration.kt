package com.infrastructure.wisesaying.entity

class IdGeneration(private var id: Long) {
    fun generationId(): Long = ++id

    fun resetId() {
        id = 0L
    }
}