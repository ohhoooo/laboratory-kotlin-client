package com.irlab.crawlingapp.network

data class ItemModel(
    val title: String, // 제목
    val communityTitle: String, // 커뮤니티 이름
    val time: String, // 시간
    val classification: String, // 분류
    val like: String, // 좋아요 수
    val comments: String, // 댓글 수
    val itemImageView: String, // 아이템 이미지
    val url: String // 해당 사이트 주소
)
