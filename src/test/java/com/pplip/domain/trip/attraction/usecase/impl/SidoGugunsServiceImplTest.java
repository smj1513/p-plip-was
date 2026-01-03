package com.pplip.domain.trip.attraction.usecase.impl;

import com.pplip.domain.trip.attraction.persistence.entity.SpecialSidos;
import com.pplip.global.exception.BusinessLogicException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SidoGugunsServiceImplTest {

    @Autowired
    SidoGugunsServiceImpl service;

    @Test
    public void falseTest() throws Exception {
        Assertions.assertThatThrownBy(() -> service.validSidoGuguns(SpecialSidos.SEOUL.getCode(), 1))
                .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    public void booleanTest() throws Exception {
        service.validSidoGuguns(35, 23);
        Assertions.assertThatThrownBy(() -> service.validSidoGuguns(35, 100))
                .isInstanceOf(BusinessLogicException.class);
        Assertions.assertThatThrownBy(() -> service.validSidoGuguns(100, 100))
                .isInstanceOf(BusinessLogicException.class)
                .hasMessage("존재하지 않는 시도코드입니다.");


    }
}