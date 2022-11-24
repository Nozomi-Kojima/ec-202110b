package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

@SpringBootTest
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
	@DisplayName("注文履歴表示")
	void testshowOrderHistory() throws Exception {
			mockMvc.perform(get("orderHistory/order_history"))
			.andExpect(view().name("forward:/user/toLogin"));
	}
	void testshowOrderHistory2() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/orderHistory"))
                .andExpect(view().name("orderHistory/order_history"))
                .andReturn();

		ModelAndView mav = mvcResult.getModelAndView();
        @SuppressWarnings(value = "unchecked")// 下のキャストのワーニングを出さないようにする
        List<OrderItem> resultOrderitemList = (List<OrderList>) mav.getModel().get("orderList");
		

}}
