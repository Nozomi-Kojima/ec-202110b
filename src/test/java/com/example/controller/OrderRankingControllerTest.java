package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.example.domain.Item;

class OrderRankingControllerTest {
	
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
	@DisplayName("ランキング表示")
	void testshowTotalRanking() throws Exception {
		 MvcResult mvcResult = (MvcResult) mockMvc.perform(get("/showTotalRanking"));
		 .andExpect(view().name("/orderRanking/total_order_ranking"))
         .andReturn();
		 
		 ModelAndView mav = mvcResult.getModelAndView();
		 @SuppressWarnings(value = "unchecked")// 下のキャストのワーニングを出さないようにする
	        List<Item> topTenItems = (List<Item>) mav.getModel().get("topTenItems");
		    @SuppressWarnings("unchecked")
			List<Integer> yearList = (List<Integer>) mav.getModel().get("yearList");
		    @SuppressWarnings("unchecked")
			List<Integer> monthList = (List<Integer>) mav.getModel().get("monthList");
		    
		    System.out.println(topTenItems);
		    
		    
		    
		    
		 
		
	}
	

}
