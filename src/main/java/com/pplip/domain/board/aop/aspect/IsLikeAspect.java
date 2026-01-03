package com.pplip.domain.board.aop.aspect;

import com.pplip.domain.board.freeboard.persistence.dao.UserLikeBoardDao;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Aspect
@Component
@Log4j2
public class IsLikeAspect {
	private final UserLikeBoardDao userLikeBoardDao;
}
