package com.seogineer.javaspringbarogojointest.enums;

public enum ErrorMessage {
    ID_ALREADY_EXISTS("이미 존재하는 ID입니다."),
    REGISTRATION_SUCCESS("회원 가입 성공"),
    INVALID_USERNAME_OR_PASSWORD("ID나 비밀번호가 유효하지 않습니다."),
    USER_NOT_FOUND("사용자가 존재하지 않습니다."),
    MAX_PERIOD_EXCEEDED("조회 가능한 최대 기간은 3일입니다."),
    DELIVERY_NOT_FOUND("배달 정보가 존재하지 않습니다."),
    USER_INFO_MISMATCH("사용자 정보가 일치하지 않습니다."),
    CANNOT_MODIFY_COMPLETED_ORDER("배달 완료된 주문은 수정할 수 없습니다."),
    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
