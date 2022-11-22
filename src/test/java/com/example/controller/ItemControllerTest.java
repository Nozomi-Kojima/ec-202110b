package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.example.domain.Item;
import com.example.form.ReviewForm;
import com.example.util.CsvDataSetLoader;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

@SpringBootTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
		DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class
})
class ItemControllerTest {
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;

	ItemController itemController = new ItemController();
	
	@BeforeEach
	void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	
	@Test
	void showlist() throws Exception{
		//コントローラー呼び出し//
		MvcResult mvcResult = mockMvc.perform(get("/item")).andExpect(view().name("/item/item_list_noodle")).andReturn();
		
		//スコープのデータ取り出し//
		ModelAndView mav = mvcResult.getModelAndView();
		List<Item> itemList = (List<Item>)mav.getModel().get("itemList");
		
		Item item1 = itemList.get(0);
		assertEquals("追い鰹チャーシュー", item1.getName());
		assertEquals(1050, item1.getPriceM());
		assertEquals(1100, item1.getPriceL());
	}
	
	@Test
	void setUpReviewForm() throws Exception{
		ReviewForm reviewForm = itemController.setUpReviewForm();
		assertTrue(reviewForm instanceof ReviewForm);
	}
	
	@Test
	void searchItem() throws Exception{
		
	}

}
