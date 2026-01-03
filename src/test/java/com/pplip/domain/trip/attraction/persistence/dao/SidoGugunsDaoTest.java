package com.pplip.domain.trip.attraction.persistence.dao;

import com.pplip.domain.trip.attraction.api.response.AttractionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("SidoGugunDao 테스트")
class SidoGugunsDaoTest {

    @Autowired
    private SidoGugunsDao sidoGugunsDao;

    @Test
    @DisplayName("성공: 모든 지역 정보 조회")
    void findAllRegion_Success() {
        // Given

        // When
        List<AttractionResponse.Region> regions = sidoGugunsDao.findAllRegion();

        // Then
        assertThat(regions).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("성공: 모든 시도 정보 조회")
    void findAllSido_Success() {
        // Given

        // When
        List<AttractionResponse.Sido> sidos = sidoGugunsDao.findAllSido();

        // Then
        assertThat(sidos).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("성공: 모든 구군 정보 조회")
    void findAllGugun_Success() {
        // Given

        // When
        List<AttractionResponse.Gugun> guguns = sidoGugunsDao.findAllGugun();

        // Then
        assertThat(guguns).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("성공: 특정 시도의 구군 정보 조회")
    void findAllGugunInSido_Success() {
        // Given
        Integer sidoCode = 1; // Assuming 1 is a valid sido_code (e.g., 서울)

        // When
        List<AttractionResponse.Gugun> guguns = sidoGugunsDao.findAllGugunInSido(sidoCode);

        // Then
        assertThat(guguns).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("실패: 존재하지 않는 시도 코드로 조회 시 빈 리스트 반환")
    void findAllGugunInSido_Fail_WhenSidoCodeNotFound() {
        // Given
        Integer nonExistentSidoCode = 9999;

        // When
        List<AttractionResponse.Gugun> guguns = sidoGugunsDao.findAllGugunInSido(nonExistentSidoCode);

        // Then
        assertThat(guguns).isNotNull().isEmpty();
    }
}
