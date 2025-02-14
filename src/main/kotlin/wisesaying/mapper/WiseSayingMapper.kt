package com.wisesaying.mapper

import com.infrastructure.wisesaying.entity.WiseSayingEntity
import java.util.*
import java.util.stream.Collectors
import java.wisesaying.domain.WiseSaying
import kotlin.collections.LinkedHashMap

object WiseSayingMapper {
    fun toEntity(wiseSaying: WiseSaying): WiseSayingEntity {
        return WiseSayingEntity(
            wiseSaying.id,
            wiseSaying.wiseSaying,
            wiseSaying.writer
        )
    }

    fun toEntity(json: String): WiseSayingEntity {
        val map = json.trim()
            .replace("[{}\"]".toRegex(), "")
            .split(",")
            .map { it.split(":") }
            .associate { (key, value) -> key.trim() to value.trim() }

        return WiseSayingEntity(
            id = map["id"]?.toLongOrNull() ?: throw RuntimeException("Invalid ID"),
            wiseSaying = map["wiseSaying"] ?: throw RuntimeException("Missing wiseSaying"),
            writer = map["writer"] ?: throw RuntimeException("Missing writer")
        )
    }

    fun toJson(wiseSayingEntity: WiseSayingEntity): String {
        return """
        {
            "id": ${wiseSayingEntity.id},
            "wiseSaying": "${wiseSayingEntity.wiseSaying}",
            "writer": "${wiseSayingEntity.writer}"
        }
        """.trimIndent()
    }

    fun toJson(linkedHashMap: LinkedHashMap<Long, WiseSayingEntity>): String {
        val sb = StringBuilder()
        sb.append("[")

        linkedHashMap.values.forEach{
            wiseSayingEntity -> sb.append(WiseSayingMapper.toJson(wiseSayingEntity))
        }

        sb.deleteCharAt(sb.length - 1)
        sb.append("]")

        return sb.toString()
    }

    fun toLinkedHashMap(json: String?): LinkedHashMap<Long, WiseSayingEntity> {
        if (json.isNullOrEmpty()) return LinkedHashMap()

        val substring = json.substring(1, json.length - 1)
        val jsonArray = substring.split("},{")

        return jsonArray
            .map { WiseSayingMapper.toEntity(it) }
            .associateByTo(LinkedHashMap(), WiseSayingEntity::id)
    }

    fun toDomain(wiseSayingEntity: WiseSayingEntity): WiseSaying {
        return WiseSaying(
            wiseSayingEntity.id,
            wiseSayingEntity.wiseSaying,
            wiseSayingEntity.writer
        )
    }

}