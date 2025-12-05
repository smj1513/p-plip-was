package com.pplip.domain.user.cache;

/**
 * 이메일 유효성 검사를 위한 계약을 정의하는 인터페이스
 */
public interface EmailValidator {
    /**
     * 주어진 이메일 주소의 유효성을 검사합니다.
     *
     * @param email 검사할 이메일 주소
     * @return 이메일이 유효하면 true, 그렇지 않으면 false
     */
    boolean valid (String email);

    /**
     * 주어진 이메일과 유효 정보를 저장합니다.
     *
     * @param email 이메일
     * @param info  이메일 유효 정보
     */
    void putValidInfo(String email, EmailValidationInfo info);

    /**
     * 이메일의 인증번호가 일치하는지 확인합니다.
     *
     * @param email 확인할 이메일
     * @param code 확인할 코드
     * @return 일치한다면 true, 그렇지 않다면 false
     */
    boolean isMatching(String email, String code);

    /**
     * 이메일 인증 정보를 삭제합니다.
     * => isMatching이 true일 때 삭제.
     * => 호출해줘야 함.
     * @param email
     */
    void deleteInfo(String email);
}
