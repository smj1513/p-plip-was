package com.pplip.domain.trip.attraction.persistence.dao;

import com.pplip.domain.trip.attraction.persistence.entity.Tag;
import com.pplip.global.page.PageRequest;
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
class TagDaoTest {

    @Autowired
    TagDao dao;

    @Test
    @DisplayName("성공: 모든 태그 조회 (페이지네이션)")
    void findAll_Success() {
        // given
        PageRequest pageRequest = new PageRequest(1, 20);
        
        // when
        List<Tag> all = dao.findAll(pageRequest);
        
        // then
        assertThat(all).isNotNull();
    }

    @Test
    @DisplayName("성공: 특정 명소의 태그 조회")
    void findByNo_Success() {
        // given
        // Assuming attraction with ID 125266 exists and has tags
        long attractionId = 56658;
        
        // when
        List<Tag> allByAttractionNo = dao.findAllByAttractionNo(attractionId);
        
        // then
        assertThat(allByAttractionNo).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("실패: 존재하지 않는 명소의 태그 조회 시 빈 리스트 반환")
    void findByNo_Fail_WhenNotFound() {
        // given
        long nonExistentAttractionId = -1L;
        
        // when
        List<Tag> allByAttractionNo = dao.findAllByAttractionNo(nonExistentAttractionId);
        
        // then
        assertThat(allByAttractionNo).isNotNull().isEmpty();
    }
}