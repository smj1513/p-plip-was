package com.pplip.domain.trip.attraction.usecase.impl;

import com.pplip.domain.auth.persistence.entity.Account;
import com.pplip.domain.trip.attraction.api.request.SearchHistoryRequest;
import com.pplip.domain.trip.attraction.api.response.SearchHistoryResponse;
import com.pplip.domain.trip.attraction.persistence.entity.SearchHistory;
import com.pplip.domain.trip.attraction.usecase.SearchHistoryService;
import com.pplip.domain.user.persistence.dao.UserDao;
import com.pplip.global.page.Page;
import com.pplip.global.page.PageRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class SearchHistoryServiceImplTest {

}