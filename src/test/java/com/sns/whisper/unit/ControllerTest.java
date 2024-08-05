package com.sns.whisper.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.whisper.config.InfrastructureTestConfiguration;
import com.sns.whisper.config.LoginTestConfiguration;
import com.sns.whisper.domain.post.application.PostService;
import com.sns.whisper.domain.post.presentation.PostController;
import com.sns.whisper.domain.user.application.LoginService;
import com.sns.whisper.domain.user.application.UserService;
import com.sns.whisper.domain.user.presentation.UserController;
import com.sns.whisper.global.aop.LoginCheckAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = {UserController.class, PostController.class})
@Import({AopAutoConfiguration.class, LoginCheckAspect.class,
        InfrastructureTestConfiguration.class, LoginTestConfiguration.class})
@ActiveProfiles("test")
public class ControllerTest {

    @MockBean
    protected UserService userService;

    @SpyBean
    protected LoginService loginService;

    @MockBean
    protected PostService postService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

}
