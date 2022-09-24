package com.menu.wantyou.controller;

import com.menu.wantyou.domain.Profile;
import com.menu.wantyou.domain.User;
import com.menu.wantyou.domain.UserDetailsImpl;
import com.menu.wantyou.dto.profile.ProfileReqDTO;
import com.menu.wantyou.service.ProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProfileControllerTest {
    @MockBean
    ProfileServiceImpl profileService;

    @Autowired
    private MockMvc mockMvc;

    private final String NAME = "홍길동";
    private final String BIRTHYEAR = "2000";
    private final String BIRTHDAY = "1223";
    private final UserDetails userDetails = new UserDetailsImpl(User.builder().username("user01").build());

    @Test
    public void getMyProfile() throws Exception{
        ProfileReqDTO profileReqDTO = ProfileReqDTO.builder()
                                                    .name(NAME)
                                                    .birthYear(BIRTHYEAR)
                                                    .birthDay(BIRTHDAY)
                                                    .build();
        Profile profile = profileReqDTO.toEntity();

        given(profileService.findByUsername(any(String.class))).willReturn(profile);
        mockMvc.perform(get("/profiles/me").with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.birthYear").exists())
                .andExpect(jsonPath("$.birthMonth").exists())
                .andExpect(jsonPath("$.birthDay").exists());
    }

    @Test
    public void updateMyProfile() throws Exception {
        String newName = "김첨지";
        String newYear = "2002";
        String newDay = "0505";

        ProfileReqDTO updateDTO = ProfileReqDTO.builder()
                                                .name(newName)
                                                .birthYear(newYear)
                                                .birthDay(newDay)
                                                .build();

        Profile updateProfile = updateDTO.toEntity();

        given(profileService.update(any(String.class), any(updateDTO.getClass()))).willReturn(updateProfile);

        mockMvc.perform(
                patch("/profiles/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content("{" +
                                "\"name\" : \"" + newName + "\"," +
                                "\"birthYear\" : \"" + newYear + "\"," +
                                "\"birthDay\" : \"" + newDay + "\"" +
                                "}")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.birthYear").exists())
                .andExpect(jsonPath("$.birthMonth").exists())
                .andExpect(jsonPath("$.birthDay").exists());
    }
}