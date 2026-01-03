package com.pplip.global.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequest {
    private int pageNum = 1;
    private int pageSize = 20;

    // MyBatis가 #{offset}을 만났을 때 이 메서드를 호출합니다.
    public int getOffset() {
        // pageNum이 0이나 음수로 들어올 경우를 대비해 최소 1로 보정해주는 것이 안전합니다.
        int page = Math.max(1, pageNum);
        return (page - 1) * pageSize;
    }
}
