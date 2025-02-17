package com.wisesaying.service

import java.util.Optional
import java.wisesaying.domain.WiseSaying
import java.wisesaying.service.response.WiseSayingPageResponse
import java.wisesaying.util.WiseSayingCondition

interface WiseSayingService {
    fun add(wiseSaying: String, writer: String): Long
    fun findById(id: Long): WiseSaying
    fun update(wiseSaying: WiseSaying, updateWiseSaying: String, updateWriter: String);
    fun delete(targetId: Long): Long
    fun findAll(wiseSayingCondition: WiseSayingCondition):WiseSayingPageResponse
    fun build(): Boolean
}