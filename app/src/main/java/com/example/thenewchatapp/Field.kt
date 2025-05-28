package com.example.thenewchatapp

data class Field(
    var title: String,
    var content: String = ""
)

//📌 이게 뭔가?
//필드 화면의 데이터 모델 (데이터 저장용 그릇)
//
//📍 어디서 쓰이나?
//필드 1, 필드 2, 필드 3... 이런 필드들의 제목과 내용을 저장하는 용도
//
//예를 들어 "필드3", 내용은 "여기에 글쓰기" 같은 데이터가 저장됨
//
//📦 정리
//title : 필드의 이름 (ex. 필드1)
//
//content : 필드의 본문 내용 (글쓰기 내용)
//
//📌 왜 필요해?
//필드 목록을 만들고, 클릭했을 때 해당 내용을 보여주기 위해서야.