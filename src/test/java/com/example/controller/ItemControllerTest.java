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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.example.domain.Item;
import com.example.form.ItemForm;
import com.example.form.OrderItemForm;
import com.example.form.ReviewForm;
import com.example.util.CsvDataSetLoader;
import com.github.springtestdbunit.annotation.DatabaseSetup;
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
		@SuppressWarnings("unchecked")
		List<Item> itemList = (List<Item>)mav.getModel().get("itemList");
		
		Item item1 = itemList.get(0);
		assertEquals("かさね味Special", item1.getName());
		assertEquals(900, item1.getPriceM());
		assertEquals(1000, item1.getPriceL());
	}
	
	@Test
	void setUpReviewForm() throws Exception{
		ReviewForm reviewForm = itemController.setUpReviewForm();
		assertTrue(reviewForm instanceof ReviewForm);
	}
	
	@Test
	@DisplayName("値段が高い順")
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
		assertEquals("台湾まぜそば", item.getName());
	}
	@Test
	@DisplayName("値段が低い順")
	void searchItem2() throws Exception{
		MvcResult mvcResult = mockMvc.perform(post("/item/search")
				.param("sortType","priceLow"))
				.andExpect(view().name("/item/item_list_noodle"))
				.andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		@SuppressWarnings("unchecked")
		List<Item> itemList = (List<Item>)mav.getModel().get("itemList");
		
		Item item = itemList.get(0);
		assertEquals("旨辛味噌麺", item.getName());
	}
	@Test
	@DisplayName("人気順")
	@DatabaseSetup("/popularSearch") // テスト実行前に初期データを投入
	void searchItem3() throws Exception{
		MvcResult mvcResult = mockMvc.perform(post("/item/search")
				.param("sortType","popular"))
				.andExpect(view().name("/item/item_list_noodle"))
				.andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		@SuppressWarnings("unchecked")
		List<Item> itemList = (List<Item>)mav.getModel().get("itemList");
		
		Item item = itemList.get(0);
		assertEquals("赤ラーメン", item.getName());
		Item item2 = itemList.get(1);
		assertEquals("追い鰹チャーシュー", item2.getName());
	}
	@Test
	@DisplayName("名前検索　人気順")
	void searchItem4() throws Exception{
		MvcResult mvcResult = mockMvc.perform(post("/item/search")
				.param("name","百").param("sortType", "popular"))
				.andExpect(view().name("/item/item_list_noodle"))
				.andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		@SuppressWarnings("unchecked")
		List<Item> itemList = (List<Item>)mav.getModel().get("itemList");
		
		Item item = itemList.get(0);
		assertEquals("百福元味", item.getName());
		}
	@Test
	@DisplayName("名前検索　該当なし")
	void searchItem5() throws Exception{
		MvcResult mvcResult = mockMvc.perform(post("/item/search")
				.param("name","v").param("sortType", "popular"))
				.andExpect(view().name("/item/item_list_noodle"))
				.andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		String message = (String)mav.getModel().get("message");
		
		assertEquals("該当の商品が見つかりません", message);
	}
	@Test
	void setUpOrderItemForm() throws Exception{
		OrderItemForm form = itemController.setUpOrderItemForm();
		assertTrue(form instanceof OrderItemForm);
	}
	
	@Test
	void showDetail() throws Exception{
		MvcResult mvcResult = mockMvc.perform(post("/item/showDetail")
				.param("id","4"))
				.andExpect(view().name("/item/item_detail"))
				.andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		Item item = (Item)mav.getModel().get("item");
		
		assertEquals("かさね味Special", item.getName());
		
	}

}
