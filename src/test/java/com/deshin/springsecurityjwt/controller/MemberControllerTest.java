package com.deshin.springsecurityjwt.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.deshin.springsecurityjwt.dto.SignupMemberRequest;
import com.deshin.springsecurityjwt.security.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  final String URI = "/member";

  private final String userId = "userid01";

  @BeforeEach
  void signUp()throws Exception {
    SignupMemberRequest request = getSignupMemberRequest();

    mvc.perform(post(URI + "/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andDo(print());

  }

  @Test
  @DisplayName("회원정보 확인 성공")
  @WithMockCustomUser(userId = userId)
  void memberInfo() throws Exception {

    mvc.perform(get(URI + "/user/me")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

  }

  @Test
  @DisplayName("회원정보 확인 실패")
  @WithMockCustomUser(userId = userId)
  void memberInfoFail() throws Exception {

    mvc.perform(get(URI + "/admin/me")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());

  }

  private SignupMemberRequest getSignupMemberRequest() {
    return new SignupMemberRequest(userId, "123456", "김유저");
  }

}