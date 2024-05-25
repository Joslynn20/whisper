package com.sns.whisper.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.whisper.domain.user.application.GeneralUserService;
import com.sns.whisper.domain.user.presentation.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({UserController.class})
@ActiveProfiles("test")
public class ControllerTest {

    @MockBean
    protected GeneralUserService generalUserService;

    @Autowired
    protected MockMvc mockMvc;
    
    @Autowired
    protected ObjectMapper objectMapper;

}
