package com

import com.infrastructure.wisesaying.entity.WiseSayingEntity

fun main() {
    println("Hello World!")
    val test = WiseSayingEntity(1, "테스트" , "작성자");
    val copy = test.copy(id = 2)

    println(test.toJson())
}