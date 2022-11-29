package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.example.domain.Item;
import com.example.util.CsvDataSetLoader;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;


@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, // このテストクラスでDIを使えるように指定
	TransactionDbUnitTestExecutionListener.class
	//
})
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@SpringBootTest
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
	@DisplayName("総合ランキング表示")
	void testshowTotalRanking() throws Exception {
		 MvcResult mvcResult = (MvcResult) mockMvc.perform(get("/orderRanking/showTotalRanking"))
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
		    System.out.println(yearList);
		    System.out.println(monthList);
		    Item topItem = topTenItems.get(0);
		    Item topItem = topTenItems.get(9);
		    assertEquals("一位の商品",topItem.getName(), "一位の商品があってない");
		    
		    
		    	
	}
	
	@Test
	@DisplayName("月間ランキング表示")
	void testshowmonthRanking() throws Exception {
		 MvcResult mvcResult = (MvcResult) mockMvc.perform((RequestBuilder) ((ResultActions) post("/orderRanking/showTotalRankingByMonth")
				 .param("orderYear", "1")
				 .param("orderYear","2")
				 .param("orderYear", "1")
				 .param("orderYear", "12")
				 .param("orderMonth", "2011")
				 .param("orderMonth", "2019")
				 .param("orderMonth", "2022"))
				 .andExpect(view().name("/orderRanking/month_order_ranking"))
                 .andReturn();
	 
    	
	}}
