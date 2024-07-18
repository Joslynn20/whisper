package com.sns.whisper.global.resolver;

import com.sns.whisper.domain.user.application.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final LoginService loginService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authenticated.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String userId = loginService.getCurrentUserId();

        if (userId == null) {
            return new GuestUser();
        }
        
        return new LoginUser(userId);
    }
}
