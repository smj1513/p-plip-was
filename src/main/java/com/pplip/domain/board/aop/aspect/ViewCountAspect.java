package com.pplip.domain.board.aop.aspect;

import com.pplip.domain.board.aop.annotation.CountView;
import com.pplip.domain.board.freeboard.persistence.dao.FreeBoardDao;
import com.pplip.domain.board.notice.persistence.dao.NoticeDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@RequiredArgsConstructor
@Log4j2
public class ViewCountAspect {
	private final FreeBoardDao freeBoardDao;
	private final NoticeDao noticeBoardDao;

	@AfterReturning(pointcut = "@annotation(annotation)", returning = "result")
	@Transactional(propagation = Propagation.REQUIRES_NEW)//조회 로직 read only에서 분리
	public void increaseViewCount(JoinPoint joinPoint, CountView annotation, Object result) {
		// 메서드의 첫 번째 인자(게시글 ID)를 가져옴.
		Object[] args = joinPoint.getArgs();
		if (args.length == 0 || !(args[0] instanceof Long)) {
			return; // ID가 없으면 무시
		}
		Long id = (Long) args[0];
		switch (annotation.value()) {
			case FREE_BOARD -> freeBoardDao.updateViewCount(id);
			case NOTICE -> noticeBoardDao.updateViewCount(id);
			// 게시판이 추가되면 여기에 case 한 줄만 추가하면 됨
			default -> log.warn("지원하지 않는 게시판 타입입니다.");
		}
	}
}
