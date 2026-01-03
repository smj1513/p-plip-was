package com.pplip.domain.trip.attraction.usecase.impl;

import com.pplip.domain.trip.attraction.api.response.AttractionResponse;
import com.pplip.domain.trip.attraction.persistence.dao.SidoGugunsDao;
import com.pplip.domain.trip.attraction.persistence.entity.SpecialSidos;
import com.pplip.domain.trip.attraction.usecase.SidoGugunsService;
import com.pplip.global.api.code.ErrorCode;
import com.pplip.global.exception.BoardLogicException;
import com.pplip.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SidoGugunsServiceImpl implements SidoGugunsService {

    private final SidoGugunsDao dao;

    @Override
    public void validSidoGuguns(Integer sidoCode, Integer gugunCode) {
        for (SpecialSidos sido : SpecialSidos.values()) {
            if (sido.getCode() != sidoCode) {
                continue;
            }
            if (gugunCode != null) {
                throw new BusinessLogicException(ErrorCode.SIDO_GUGUN_INVALID_VALUE, "특별, 광역, 자치 시도 코드에는 구군 코드가 포함될 수 없습니다.");
            }
            return;
        }

        List<AttractionResponse.Gugun> allGugun = dao.findAllGugunInSido(sidoCode);
        if (allGugun.isEmpty()) {
            throw new BusinessLogicException(ErrorCode.SIDO_GUGUN_NOT_FOUND_ERROR, "존재하지 않는 시도코드입니다.");
        }
        allGugun.stream().filter(data -> data.getGugunCode().equals(gugunCode))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.SIDO_GUGUN_NOT_FOUND_ERROR, "시도코드와 구군코드가 맵핑되지 않습니다."));
    }


    @Override
    public List<AttractionResponse.Region> findAllRegion() {
        return dao.findAllRegion();
    }
}
