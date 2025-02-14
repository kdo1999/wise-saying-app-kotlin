package com.infrastructure.wisesaying.entity

import java.wisesaying.domain.WiseSaying

data class WiseSayingEntity(
    val id: Long,
    val wiseSaying: String,
    val writer: String
) {
    //TODO WiseSaying 수정할 것
    fun toModel(): WiseSaying = WiseSaying(id, wiseSaying, writer)

    fun from(wiseSaying: WiseSaying): WiseSayingEntity {
        return WiseSayingEntity(
            wiseSaying.id,
            wiseSaying.wiseSaying,
            wiseSaying.writer
        )
    }

    fun toJson(): String =
        """
        { 
            "id": $id,
            "wiseSaying": "$wiseSaying",
            "writer": "$writer"
        }
        """.trimIndent()

    fun fromJson(json: String): WiseSayingEntity {
        val map = json.trim()
            .replace("[{}\"]".toRegex(), "")
            .split(",")
            .map { it.split(":") }
            .associate { (key, value) -> key.trim() to value.trim() }

        return WiseSayingEntity(
            id = map["id"]?.toLongOrNull() ?: throw RuntimeException(),
            wiseSaying = map["wiseSaying"] ?: throw RuntimeException(),
            writer = map["writer"] ?: throw RuntimeException()
        )
    }
}

