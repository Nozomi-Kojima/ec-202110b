package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.example.domain.Item;
import com.example.form.ItemForm;
import com.example.form.OrderItemForm;
import com.example.form.ReviewForm;
import com.example.util.CsvDataSetLoader;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

@SpringBootTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
		DependencyInjectionTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,
//		TransactionalTestExecutionListener.class
})
@Transactional
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
	@DisplayName("?????????????????????????????????")
	void showlist() throws Exception{
		//?????????????????????????????????//
		MvcResult mvcResult = mockMvc.perform(get("/item")).andExpect(view().name("/item/item_list_noodle")).andReturn();
		
		//????????????????????????????????????//
		ModelAndView mav = mvcResult.getModelAndView();
		@SuppressWarnings("unchecked")
		List<Item> itemList = (List<Item>)mav.getModel().get("itemList");
		
		Item item1 = itemList.get(0);
		assertEquals("????????????Special", item1.getName());
		assertEquals(900, item1.getPriceM());
		assertEquals(1000, item1.getPriceL());
	}
	
	@Test
	void setUpReviewForm() throws Exception{
		ReviewForm reviewForm = itemController.setUpReviewForm();
		assertTrue(reviewForm instanceof ReviewForm);
	}
	
	@Test
	@DisplayName("??????????????????")
	void searchItem1() throws Exception{
		MvcResult mvcResult = mockMvc.perform(post("/item/search")
				.param("sortType","priceHigh"))
				.andExpect(view().name("/item/item_list_noodle"))
				.andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
//		ItemForm itemForm = new ItemForm("","priceHigh");
		
		@SuppressWarnings("unchecked")
		List<Item> itemList = (List<Item>)mav.getModel().get("itemList");
		
		Item item = itemList.get(0);
		assertEquals("??????????????????", item.getName());
	}
	@Test
	@DisplayName("??????????????????")
	void searchItem2() throws Exception{
		MvcResult mvcResult = mockMvc.perform(post("/item/search")
				.param("sortType","priceLow"))
				.andExpect(view().name("/item/item_list_noodle"))
				.andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		@SuppressWarnings("unchecked")
		List<Item> itemList = (List<Item>)mav.getModel().get("itemList");
		
		Item item = itemList.get(0);
		assertEquals("???????????????", item.getName());
	}
	
	@Test
	@DisplayName("?????????")
	@DatabaseSetup("/pSearch") // ?????????????????????????????????????????????
	void searchItem3() throws Exception{
		MvcResult mvcResult = mockMvc.perform(post("/item/search")
				.param("sortType","popular"))
				.andExpect(view().name("/item/item_list_noodle"))
				.andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		@SuppressWarnings("unchecked")
		List<Item> itemList = (List<Item>)mav.getModel().get("itemList");
		
		Item item = itemList.get(0);
		assertEquals("???????????????", item.getName());
		Item item2 = itemList.get(1);
		assertEquals("????????????????????????", item2.getName());
	}
	
	@Test
	@DisplayName("????????????????????????")
	void searchItem4() throws Exception{
		MvcResult mvcResult = mockMvc.perform(post("/item/search")
				.param("name","???").param("sortType", "popular"))
				.andExpect(view().name("/item/item_list_noodle"))
				.andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		@SuppressWarnings("unchecked")
		List<Item> itemList = (List<Item>)mav.getModel().get("itemList");
		
		Item item = itemList.get(0);
		assertEquals("????????????", item.getName());
		}
	@Test
	@DisplayName("???????????????????????????")
	void searchItem5() throws Exception{
		MvcResult mvcResult = mockMvc.perform(post("/item/search")
				.param("name","v").param("sortType", "aaa"))
				.andExpect(view().name("/item/item_list_noodle"))
				.andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		String message = (String)mav.getModel().get("message");
		
		assertEquals("???????????????????????????????????????", message);
	}
	@Test
	void setUpOrderItemForm() throws Exception{
		OrderItemForm form = itemController.setUpOrderItemForm();
		assertTrue(form instanceof OrderItemForm);
	}
	@Test
	@DisplayName("????????????????????????????????????")
	@DatabaseSetup("/emptySearch") 
	void searchItem6() throws Exception{
		MvcResult mvcResult = mockMvc.perform(post("/item/search")
				.param("sortType", "popular"))
				.andExpect(view().name("/item/item_list_noodle"))
				.andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		String message = (String)mav.getModel().get("message");
		
		assertEquals("???????????????????????????????????????", message);
	}
	
	@Test
	@DisplayName("???????????????????????????")
	void showDetail() throws Exception{
		MvcResult mvcResult = mockMvc.perform(post("/item/showDetail")
				.param("id","4"))
				.andExpect(view().name("/item/item_detail"))
				.andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		Item item = (Item)mav.getModel().get("item");
		
		assertEquals("????????????Special", item.getName());
		
	}

}
