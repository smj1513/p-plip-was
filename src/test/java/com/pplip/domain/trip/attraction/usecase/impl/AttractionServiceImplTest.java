package com.pplip.domain.trip.attraction.usecase.impl;

import com.pplip.domain.trip.attraction.api.request.AttractionRequest;
import com.pplip.domain.trip.attraction.api.response.AttractionResponse;
import com.pplip.domain.trip.attraction.persistence.entity.ContentType;
import com.pplip.domain.trip.attraction.usecase.AttractionService;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AttractionServiceImplTest {

    @Autowired
    AttractionService service;

    @Test
    public void searchTest() throws Exception {
        // given
        AttractionRequest.Search search = AttractionRequest.Search.builder()
                .lat(new BigDecimal(37.5663))
                .lng(new BigDecimal(126.9779))
                .m(2000)
                .contentTypes(List.of(ContentType.ATTRACTION))
                .query("유심")
                .build();

        // when
        Page<AttractionResponse.Summary> page = service.findAllBySearch(search, new PageRequest(0, 20));
        page.getList().stream().forEach(System.out::println);
        Assertions.assertThat(page.getTotalCount()).isEqualTo(1);
        // then
    }


    @Test
    public void findByNo() throws Exception {
        // given
        AttractionResponse.Details data = service.findByNo(57250L);
        // when
        System.out.println(data);
        // then
    }
}