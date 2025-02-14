package com.infrastructure.wisesaying.repository

import java.util.*
import java.wisesaying.domain.WiseSaying

interface WiseSayingRepository {
    fun save(wiseSaying: WiseSaying): Long
    fun findById(id: Long): Optional<WiseSaying>
    fun delete(id: Long): Long
    fun update(wiseSaying: WiseSaying)
    fun findAll(): LinkedList<WiseSaying>
    fun build(): Boolean
}