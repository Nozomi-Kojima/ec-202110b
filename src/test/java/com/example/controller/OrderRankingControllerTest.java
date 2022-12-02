package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import javax.activation.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.example.domain.Item;
import com.example.util.CsvDataSetLoader;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;


@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, // このテストクラスでDIを使えるように指定
	TransactionDbUnitTestExecutionListener.class
	//
})
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@SpringBootTest
@Transactional
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
	void tearDown(@Autowired javax.sql.DataSource datasource) throws Exception {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("/sql_shirai/auto_incriment.sql"));
		populator.execute(datasource);
	}

	@Test
	@DisplayName("総合ランキング表示")
	@DatabaseSetup("/popularSearch")
	void testshowTotalRanking() throws Exception {
		MvcResult mvcResult = (MvcResult) mockMvc.perform(get("/orderRanking/showTotalRanking"))
				.andExpect(view().name("/orderRanking/total_order_ranking")).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();
		@SuppressWarnings(value = "unchecked") // 下のキャストのワーニングを出さないようにする
		List<Item> topTenItems = (List<Item>) mav.getModel().get("topTenItems");
		@SuppressWarnings("unchecked")
		List<Integer> yearList = (List<Integer>) mav.getModel().get("yearList");
		@SuppressWarnings("unchecked")
		List<Integer> monthList = (List<Integer>) mav.getModel().get("monthList");
		
		System.out.println(topTenItems);
		System.out.println(yearList);
		System.out.println(monthList);
		
		Item topItem1 = topTenItems.get(0);
		Item topItem10 = topTenItems.get(9);
		assertEquals("とんこつラーメン", topItem1.getName(), "一位の商品があってない");
		assertEquals("追い鰹チャーシュー", topItem10.getName(), "10位の商品があってない");
		
	}
	
	@Test
	@DisplayName("月間ランキング表示")
	@DatabaseSetup("/popularSearch")
	void testshowmonthRanking() throws Exception {
		MvcResult mvcResult = mockMvc.perform(
				post("/orderRanking/showTotalRankingByMonth").param("orderYear", "2020").param("orderMonth", "12"))
				.andExpect(view().name("/orderRanking/month_order_ranking")).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		List<Item> topTenItemsByMonth = (List<Item>)mav.getModel().get("topTenItemsByMonth");
		Item item1 = topTenItemsByMonth.get(0);
		Item item10 = topTenItemsByMonth.get(9);
		
		assertEquals("とんこつラーメン", item1.getName(), "一位の商品があってない");
		assertEquals("追い鰹チャーシュー",item10.getName(), "10位の商品があってない");
	}
	@Test
	@DisplayName("月間ランキング表示　12月以外")
	@DatabaseSetup("/popularSearch")
	void testshowmonthRanking2() throws Exception {
		MvcResult mvcResult = mockMvc.perform(
				post("/orderRanking/showTotalRankingByMonth").param("orderYear", "2020").param("orderMonth", "10"))
				.andExpect(view().name("/orderRanking/month_order_ranking")).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		List<Item> topTenItemsByMonth = (List<Item>)mav.getModel().get("topTenItemsByMonth");
		Item item1 = topTenItemsByMonth.get(0);
		
		assertEquals("追い鰹チャーシュー", item1.getName(), "一位の商品があってない");
	}
	
}
