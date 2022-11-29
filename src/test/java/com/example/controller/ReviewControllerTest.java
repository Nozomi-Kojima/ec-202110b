package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.util.CsvDataSetLoader;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@SpringBootTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class
})
@Transactional
class ReviewControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	@BeforeEach
	void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	@DisplayName("入力値エラーチェック")
	void insert0() throws Exception {
		String itemId = "1";
		MockHttpSession userSession = com.example.util.SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(post("/review/insert")
				 .session(userSession)
				 .param("name", "").param("review", "テスト").param("itemId", itemId))
				 .andExpect(redirectedUrl("forward:/item/showDetail?id=1"))
				 .andExpect(flash().attribute("message", "名前を入力してください"))
				 .andReturn();
	}
	
	@Test
	@DisplayName("未ログイン")
	void insert1() throws Exception {
		String itemId = "1";
		MvcResult mvcResult = mockMvc.perform(post("/review/insert")
				 .param("name", "テストユーザ").param("review", "テスト").param("itemId", itemId))
				 .andExpect(redirectedUrl("/item/showDetail?id=1&message=%3F%3F%3F%3F%3F%3F%3F%3F%3F%3F%3F%3F%3F%3F%3F%3F%3F%3F%3F%3F%3F%3F%3F"))
				 .andExpect(flash().attribute("message", "レビューの投稿はログインしていないと出来ません"))
				 .andReturn();
	}
	
	@Test
	@DisplayName("ログイン")
	@DatabaseSetup("/user")
	@ExpectedDatabase(value = "/review", assertionMode = DatabaseAssertionMode.NON_STRICT)
	void insert2() throws Exception {
		MockHttpSession userSession = com.example.util.SessionUtil.createUserIdAndUserSession();
		String itemId = "1";
		MvcResult mvcResult = mockMvc.perform(post("/review/insert")
				.session(userSession)
				.param("name", "テストユーザ").param("review", "テスト").param("itemId", itemId))
				.andExpect(view().name("redirect:/item/showDetail?id=" + itemId))
				.andReturn();
		
	}

}
