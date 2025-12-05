package com.pplip.domain.trip.attraction.persistence.dao;

import com.pplip.domain.trip.attraction.api.response.AttractionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AttractionDaoTest {

    @Autowired
    AttractionDao dao;

    @Test
    @DisplayName("성공: ID로 관광지 정보를 조회할 수 있다")
    void findByNo_Success_WhenAttractionExists() {
        // given
        long attractionId = 57874L;

        // when
        Optional<AttractionResponse.Details> byNo = dao.findByNo(attractionId);

        // then
        assertThat(byNo).isPresent();
        assertThat(byNo.get().getTitle()).isEqualTo("정족산");
    }

    @Test
    @DisplayName("실패: 존재하지 않는 ID로 조회 시 빈 Optional을 반환한다")
    void findByNo_Fail_WhenAttractionDoesNotExist() {
        // given
        long nonExistentId = -1L;

        // when
        Optional<AttractionResponse.Details> byNo = dao.findByNo(nonExistentId);

        // then
        assertThat(byNo).isNotPresent();
    }
}