package com.infrastructure.wisesaying.repository

import com.infrastructure.wisesaying.entity.IdGeneration
import com.infrastructure.wisesaying.entity.WiseSayingEntity
import com.wisesaying.mapper.WiseSayingMapper
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors
import java.wisesaying.domain.WiseSaying
import kotlin.collections.LinkedHashMap
import kotlin.io.path.Path

class WiseSayingRepositoryImpl : WiseSayingRepository {
    private val idGeneration: IdGeneration
    private val wiseSayingEntityLinkedHashMap: LinkedHashMap<Long, WiseSayingEntity>

    companion object {
        private const val FILE_PATH = "db/wiseSaying/"
        private const val LAST_ID_PATH = "${FILE_PATH}lastId.txt"
    }

    init {
        val lastIdPath = Paths.get(LAST_ID_PATH)
        val wiseSayingPath = Paths.get("${FILE_PATH}data.json")

        try {
            idGeneration = if (Files.exists(lastIdPath)) {
                val id = Files.readString(lastIdPath).toLong()
                IdGeneration(id)
            } else {
                IdGeneration(0L)
            }

            wiseSayingEntityLinkedHashMap = if (Files.exists(wiseSayingPath)) {
                val json = Files.readString(wiseSayingPath)
                WiseSayingMapper.toLinkedHashMap(json)

            } else {
                LinkedHashMap()
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun save(wiseSaying: WiseSaying): Long {
        val id = idGeneration.generationId();
        val saveWiseSayingEntity = WiseSayingEntity(id, wiseSaying.wiseSaying, wiseSaying.writer)

        val wiseSayingPath = getWiseSayingPath(saveWiseSayingEntity.id)

        val lastIdPath = Paths.get(LAST_ID_PATH);

        if (!Files.exists(wiseSayingPath)) {
            Files.createDirectories(wiseSayingPath.parent)
            Files.createFile(wiseSayingPath)
        }

        if (!Files.exists(lastIdPath)) Files.createFile(wiseSayingPath)

        val saveWiseSayingEntityJson = WiseSayingMapper.toJson(saveWiseSayingEntity)

        try {
            Files.write(wiseSayingPath, saveWiseSayingEntityJson.toByteArray())
            Files.write(lastIdPath, saveWiseSayingEntity.id.toString().toByteArray())

            wiseSayingEntityLinkedHashMap[saveWiseSayingEntity.id] = saveWiseSayingEntity
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        return saveWiseSayingEntity.id;
    }

    override fun findById(id: Long): Optional<WiseSaying> {
        val findWiseSaying = wiseSayingEntityLinkedHashMap[id] ?: return Optional.empty();

        return Optional.of(
            WiseSaying(findWiseSaying.id, findWiseSaying.wiseSaying, findWiseSaying.writer)
        )
    }

    override fun delete(id: Long): Long {
        val wiseSayingPath = getWiseSayingPath(id)
        val remove = wiseSayingEntityLinkedHashMap.remove(id) ?: throw RuntimeException("예외")

        Files.delete(wiseSayingPath)

        return remove.id
    }

    override fun update(wiseSaying: WiseSaying) {
        val updateWiseSayingEntity =
            WiseSayingEntity(wiseSaying.id, wiseSaying.wiseSaying, wiseSaying.writer)

        val wiseSayingPath = getWiseSayingPath(updateWiseSayingEntity.id)

        try {
            Files.write(wiseSayingPath, updateWiseSayingEntity.toJson().toByteArray())
            wiseSayingEntityLinkedHashMap[updateWiseSayingEntity.id] = updateWiseSayingEntity
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun findAll(): LinkedList<WiseSaying> {
        if (wiseSayingEntityLinkedHashMap.isEmpty()) return LinkedList();

        return wiseSayingEntityLinkedHashMap.values
            .map(WiseSayingMapper::toDomain).toCollection(LinkedList())
    }

    override fun build(): Boolean {
        val buildPath = Paths.get("${FILE_PATH}data.jaon")

        if (wiseSayingEntityLinkedHashMap.isEmpty()) return false

        if (!Files.exists(buildPath)) {
            Files.createDirectories(buildPath.parent)
            Files.createFile(buildPath)
        }

        Files.write(buildPath, WiseSayingMapper.toJson(wiseSayingEntityLinkedHashMap).toByteArray())

        return true
    }

    private fun getWiseSayingPath(id: Long): Path = Paths.get("${FILE_PATH}${id}.json")
}