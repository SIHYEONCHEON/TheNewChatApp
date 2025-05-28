package com.example.thenewchatapp

/** 채팅 메시지 데이터 모델 */
data class ChatMessage(
    var message: String,
    val type: MessageType,
    val isResult: Boolean = false
)

/** 메시지 타입 구분 */
enum class MessageType {
    SENT,
    RECEIVED,
    LOADING,
    LOADING_RESULT
}
//📌 이게 뭔가?
//채팅 기록 저장용 데이터 모델 (채팅 내용 그릇)
//
//📍 어디서 쓰이나?
//(지금은 아직 미사용일 가능성 높음 → 예전에 채팅 기능 계획하면서 만든 것으로 보임)
//
//사용자가 입력한 메시지와 AI가 응답한 메시지를 구분하기 위해 사용
//
//📦 정리
//message : 채팅 내용
//
//isUser : true는 사용자, false는 AI
//
//📌 왜 필요해?
//→ 추후 AI 챗봇이나 EasyCommand 명령어 수행 후 답변 등을 채팅형태로 보여줄 때 사용