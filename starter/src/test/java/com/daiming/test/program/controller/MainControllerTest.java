package com.daiming.test.program.controller;

import com.alibaba.fastjson.JSONObject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class MainControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
            .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void userCreate() throws Exception {
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("first", "firstName");
        jsonContent.put("last", "lastName");
        jsonContent.put("email", "emailAddress");
        jsonContent.put("password", "password");

        this.mockMvc.perform(
            post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent.toJSONString().getBytes()))
            .andExpect(status().isOk())
            .andDo(document("userCreate"));
    }

    @Test
    void userGet() throws Exception {
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("token", "tokenString");

        this.mockMvc.perform(
            get("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent.toJSONString().getBytes()))
            .andExpect(status().isOk())
            .andDo(document("userGet"));
    }

    @Test
    void userLogin() throws Exception {
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("password", "passwordString");
        jsonContent.put("email", "email");

        this.mockMvc.perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent.toJSONString().getBytes()))
            .andExpect(status().isOk())
            .andDo(document("userLogin"));
    }

    @Test
    void userLogout() throws Exception {
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("token", "tokenString");

        this.mockMvc.perform(
            post("/user/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent.toJSONString().getBytes()))
            .andExpect(status().isOk())
            .andDo(document("userLogout"));
    }

    @Test
    void heartbeat() throws Exception {
        this.mockMvc.perform(
            get("/heartbeat"))
            .andExpect(status().isOk())
            .andDo(document("heartbeat"));
    }
}