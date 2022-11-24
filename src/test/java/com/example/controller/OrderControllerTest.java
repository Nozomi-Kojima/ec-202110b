package com.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest
class OrderControllerTest {

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
	@DisplayName("注文確認画面表示")
	void testtoOrderConfirm() throws Exception {
		mockMvc.perform(get("/order/toOrder"))
		.andExpect(view().name("redirect:/user/toLogin"));

	}
	@Test
	void testToOrderConfirm02() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/register/insert")
                .param("name", "田中佑奈")
                .param("email", "tanaka@example.com")
                .param("password", "mogumogu")
                .param("zipcode", "111-1111")
                .param("address", "東京都新宿区")
                .param("telephone", "080-0000-0000")
        ).andExpect(view().name("order/order_confirm"))
        .andReturn();
	}
	
	
	

}
