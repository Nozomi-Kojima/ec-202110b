package com.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
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
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

@SpringBootTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
      DependencyInjectionTestExecutionListener.class,
      TransactionDbUnitTestExecutionListener.class 
})

class OrderHistoryControllerTest {
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

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("ログインしていなかった場合、ログイン画面へ遷移")
	void showOrderHistoryTest1() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		userSession.clearAttributes();
		MvcResult mvcResult = mockMvc.perform(get("/orderHistory").session(userSession))
				.andExpect(view().name("forward:/user/toLogin")).andReturn();
	}
	
	@Test
	@DisplayName("注文がない場合")
	void showOrderHistoryTest2() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(get("/orderHistory").session(userSession))
				.andExpect(view().name("orderHistory/order_no_history")).andReturn();
	}
	@Test
	@DisplayName("注文がある場合")
	@DatabaseSetup("/order")
	void showOrderHistoryTest3() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(get("/orderHistory").session(userSession))
				.andExpect(view().name("orderHistory/order_history")).andReturn();
	}

}
