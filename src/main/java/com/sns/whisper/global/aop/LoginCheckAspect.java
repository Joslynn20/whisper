package com.sns.whisper.global.aop;

import com.sns.whisper.domain.user.application.LoginService;
import com.sns.whisper.exception.post.NotAuthorizedUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class LoginCheckAspect {

    private final LoginService loginService;


    @Before("@annotation(com.sns.whisper.global.aop.LoginCheck)")
    public void checkLogin(JoinPoint pointcut) {
        log.info("AOP - Login Check Started: {}", pointcut.getSignature()
                                                          .getName());

        String userId = loginService.getCurrentUserId();

        if (userId == null) {
            throw new NotAuthorizedUserException();
        }

    }
}
