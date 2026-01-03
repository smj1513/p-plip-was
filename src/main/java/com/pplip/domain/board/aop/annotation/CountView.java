package com.pplip.domain.board.aop.annotation;

import com.pplip.domain.board.aop.enums.BoardType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CountView {
	BoardType value();
}
