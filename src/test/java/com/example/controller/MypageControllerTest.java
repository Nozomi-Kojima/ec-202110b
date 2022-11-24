package com.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.validation.constraints.Email;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.util.CsvDataSetLoader;
import com.example.util.SessionUtil;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;


@SpringBootTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
      DependencyInjectionTestExecutionListener.class,
      TransactionDbUnitTestExecutionListener.class 
})

class MypageControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		  mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	@DisplayName("マイページ画面へ遷移")
	void toMypageTest() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(get("/mypage/toMypage").session(userSession))
				.andExpect(view().name("/mypage/mypage")).andReturn();
	}
	@Test
	@DisplayName("ログインできなかったらログイン画面へ遷移")
	void toMypageTest2() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		userSession.clearAttributes();
		MvcResult mvcResult = mockMvc.perform(get("/mypage/toMypage").session(userSession))
				.andExpect(view().name("forward:/user/toLogin")).andReturn();
	}
	@Test
	@DisplayName("ユーザー更新画面へ遷移")
	void toUpdateUserTest() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(get("/mypage/toUpdateUser").session(userSession))
				.andExpect(view().name("/mypage/updateUser")).andReturn();
		userSession.clearAttributes();
		MvcResult mvcResultfailure = mockMvc.perform(get("/mypage/toUpdateUser").session(userSession))
				.andExpect(view().name("forward:/user/toLogin")).andReturn();
		
	}
	@Test
	@DisplayName("ユーザー更新完了画面へ遷移")
	void toUpdateUserFinishedTest() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(get("/mypage/toUpdateUserFinished").session(userSession))
				.andExpect(view().name("/mypage/updateUserFinished")).andReturn();
		userSession.clearAttributes();
		MvcResult mvcResultfailure = mockMvc.perform(get("/mypage/toUpdateUserFinished").session(userSession))
				.andExpect(view().name("forward:/user/toLogin")).andReturn();
		
	}
	@Test
	@DisplayName("ユーザー情報削除確認画面へ遷移")
	void toDeleteUserConfirmTest() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(get("/mypage/toDeleteUserConfirm").session(userSession))
				.andExpect(view().name("/mypage/deleteUserConfirm")).andReturn();
		userSession.clearAttributes();
		MvcResult mvcResultfailure = mockMvc.perform(get("/mypage/toDeleteUserConfirm").session(userSession))
				.andExpect(view().name("forward:/user/toLogin")).andReturn();
		
	}
	@Test
	@DisplayName("もしユーザー更新結果がエラーだったら")
	void updateUserConfirmTest() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();		
		userSession.setNew(false);
		MvcResult mvcResultError = mockMvc.perform(get("/mypage/updateUserConfirm").session(userSession))
				.andExpect(view().name("/mypage/updateUser")).andReturn();
	}
	@Test
	@DisplayName("もしメールアドレスが重複していたら")
	void updateUserConfirmTest2() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResultDuplication = mockMvc.perform(get("/mypage/updateUserConfirm").session(userSession))
				.andExpect(view().name("/mypage/updateUser")).andReturn();
	}
	

}
