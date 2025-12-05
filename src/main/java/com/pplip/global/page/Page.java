package com.pplip.global.page;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Page<T> {

    // 1. 실제 데이터
    private List<T> list;       // 조회된 데이터 목록 (SELECT 결과)

    // 2. 페이징 기본 정보 (요청값 + DB 조회값)
    private int pageNo;         // 현재 페이지 번호 (요청)
    private int pageSize;       // 페이지당 출력할 데이터 개수 (요청, 예: 10개)
    private int totalCount;     // 전체 데이터 개수 (DB에서 조회, SELECT count(*))

    // 3. 계산된 페이징 정보 (응답값 - 프론트엔드 렌더링용)
    private int totalPage;      // 전체 페이지 수 (예: 데이터 105개, 10개씩 -> 11페이지)
    private int startPage;      // 화면 하단 시작 페이지 번호 (예: [1] ... [10] 에서 1)
    private int endPage;        // 화면 하단 끝 페이지 번호 (예: [1] ... [10] 에서 10)
    private boolean prev;       // '이전' 버튼 활성화 여부
    private boolean next;       // '다음' 버튼 활성화 여부

    // 하단에 보여줄 네비게이션 버튼 개수 (보통 10개씩 끊어서 보여줌: 1~10, 11~20)
    private static final int NAV_SIZE = 10;

    // 생성자: 여기서 페이징 계산 로직을 수행합니다.
    public Page(List<T> list, int pageNo, int pageSize, int totalCount) {
        this.list = list;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;

        calculatePagination();
    }

    private void calculatePagination() {
        // 1. 전체 페이지 수 계산
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);

        // 2. 현재 페이지가 속한 네비게이션 블록의 끝 페이지 계산
        // 예: 현재 3페이지, NAV_SIZE가 10이면 -> endPage는 10
        this.endPage = (int) (Math.ceil((double) pageNo / NAV_SIZE)) * NAV_SIZE;

        // 3. 시작 페이지 계산
        this.startPage = this.endPage - NAV_SIZE + 1;

        // 4. 실제 마지막 페이지가 계산된 endPage보다 작다면 조정
        if (this.endPage > this.totalPage) {
            this.endPage = this.totalPage;
        }

        // 5. 이전, 다음 버튼 활성화 여부
        this.prev = this.startPage > 1;
        this.next = this.endPage < this.totalPage;
    }
}