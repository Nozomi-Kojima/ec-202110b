package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.example.domain.Item;
import com.example.form.OrderItemForm;
import com.example.util.CsvDataSetLoader;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	TransactionalTestExecutionListener.class
})
class CartControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	CartController cart = new CartController();

	@Test
	void insertToCart() throws Exception{
		MvcResult mvcResult = mockMvc.perform(get("/item")).andExpect(view().name("/item/item_detail")).andReturn();
		ModelAndView mav = mvcResult.getModelAndView();
		
		OrderItemForm form = new OrderItemForm("1", "1", "M", null);
		cart.insertToCart(form, 1);
		
		@SuppressWarnings("unchecked")
		List<Item> itemList = (List<Item>)mav.getModel().get("itemList");
	}

}
