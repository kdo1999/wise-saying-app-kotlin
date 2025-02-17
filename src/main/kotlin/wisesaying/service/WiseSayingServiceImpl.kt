package com.wisesaying.service

import com.infrastructure.wisesaying.repository.WiseSayingRepository
import java.util.*
import java.util.stream.Collectors
import java.wisesaying.domain.WiseSaying
import java.wisesaying.service.response.WiseSayingPageResponse
import java.wisesaying.util.WiseSayingCondition

class WiseSayingServiceImpl(
    private val wiseSayingRepository: WiseSayingRepository
) : WiseSayingService {

    override fun add(wiseSaying: String, writer: String): Long {
        val saveWiseSaying = WiseSaying(wiseSaying, writer)
        return wiseSayingRepository.save(saveWiseSaying)
    }

    override fun findById(id: Long): WiseSaying {
        return wiseSayingRepository.findById(id)
            .orElseThrow { RuntimeException("${id}번 명언이 존재하지 않습니다.") }
    }

    override fun update(wiseSaying: WiseSaying, updateWiseSaying: String, updateWriter: String) {
        try {
            wiseSaying.fetch(updateWiseSaying, updateWriter)

            wiseSayingRepository.update(wiseSaying);
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
    }

    override fun delete(targetId: Long): Long {
        return try {
            wiseSayingRepository.delete(targetId) // 삭제 실행
            targetId // 삭제 성공 시 targetId 반환
        } catch (e: RuntimeException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    override fun findAll(wiseSayingCondition: WiseSayingCondition): WiseSayingPageResponse {
        val findAll = wiseSayingRepository.findAll()

        if (findAll.isEmpty()) {
            return WiseSayingPageResponse.createEmptyPageResponse()
        }

        val totalPage = (findAll.size % 5).toLong()

        val toList = findAll
            .filter { wiseSaying ->
                (wiseSayingCondition.wiseSaying == null || wiseSaying.wiseSaying == wiseSayingCondition.wiseSaying) &&
                        (wiseSayingCondition.writer == null || wiseSaying.writer == wiseSayingCondition.writer)
            }
            .drop(((wiseSayingCondition.pageNum()?.minus(1) ?: 0) * 5).toInt())
            .take(5)
            .toCollection(LinkedList())

        return WiseSayingPageResponse(wiseSayingCondition.pageNum, totalPage, toList);
    }

    override fun build(): Boolean {
       return wiseSayingRepository.build()
    }
}