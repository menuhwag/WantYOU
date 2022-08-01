package com.menu.wantyou.controller;

import com.menu.wantyou.domain.Profile;
import com.menu.wantyou.dto.UserDTO;
import com.menu.wantyou.service.ProfileServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@AutoConfigureMockMvc
class ProfileControllerTest {
    @MockBean
    private ProfileServiceImpl profileService;

    @Autowired
    private MockMvc mockMvc;

    private final String NAME = "홍길동";
    private final String BIRTHYEAR = "2000";
    private final String BIRTHDAY = "1223";

    @Disabled
    @WithMockUser(username = "test01", authorities = {"USER"})
    @Test
    public void get_My_Profile() throws Exception{
        Profile profile = new Profile(UserDTO.SignUp.CreateProfile.builder()
                                            .name(NAME)
                                            .birthYear(BIRTHYEAR)
                                            .birthDay(BIRTHDAY)
                                            .build());

        given(profileService.findByUsername(any(String.class))).willReturn(profile);
        mockMvc.perform(get("/profiles/me"))
                .andExpect(status().isOk());
    }
}