package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.example.domain.Item;
import com.example.domain.OrderItem;
import com.example.util.CsvDataSetLoader;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@SpringBootTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class
})

/*
 * コントローラー実行後のセッション情報の取り出しは以下でいけそうです
 * （あくまでサンプルです。（ログイン成功後、セッションに「userId」という名前でidを保存できているかをテストする場合））
 * 
 * MockHttpSession mockSession = (MockHttpSession)mvcResult.getRequest().getSession(); 
 * Integer userId = (Integer)mockSession.getAttribute("userId");
 */

@Transactional
class CartControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	@AfterEach
	void tearDown(@Autowired javax.sql.DataSource datasource) throws Exception {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("/sql_shirai/auto_incriment.sql"));
		populator.execute(datasource);
	}

	@Test
	@DisplayName("カート追加　未ログイン　カートなし")
	void insertToCart() throws Exception{
		MvcResult mvcResult = mockMvc.perform(post("/cart/insert")
				.param("itemId", "1").param("size","M").param("quantity", "1")
				.param("orderToppingList", "1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1"))
				.andExpect(view().name("redirect:/cart/showCart")).andReturn();
		MockHttpSession mockSession = (MockHttpSession)mvcResult.getRequest().getSession(); 
		
		@SuppressWarnings("unchecked")
		List<OrderItem> orderItemList = (List<OrderItem>)mockSession.getAttribute("orderItemList");
		
		Item i = new Item(1, "とんこつラーメン", "創業当時から今に引き継ぐとんこつラーメンの本流であり、原点の味。18時間の調理と、丸1日の熟成を経て、とんこつの旨味を極限まで抽出した豊かで香り高いシルキーなスープに、博多らしい細麺がマッチします。"
				, 700, 800, "1.jpg",false, null);
		
		OrderItem item = new OrderItem(null, 1, null, 1, 'M', i, null);
		List<OrderItem> result = new LinkedList<>(List.of(item));
		
		assertEquals(result.toString(), orderItemList.toString());
	}
	@Test
	@DisplayName("カート追加　未ログイン　カートあり")
	void insertToCart1() throws Exception{
		MockHttpSession cartSession = com.example.util.SessionUtilShirai.createCartSession();
		MvcResult mvcResult = mockMvc.perform(post("/cart/insert")
				.session(cartSession)
				.param("itemId", "1").param("size","M").param("quantity", "1")
				.param("orderToppingList", ""))
				.andExpect(view().name("redirect:/cart/showCart")).andReturn();
		MockHttpSession mockSession = (MockHttpSession)mvcResult.getRequest().getSession(); 
		
		List<OrderItem> orderItemList = (List<OrderItem>)mockSession.getAttribute("orderItemList");
		int i1 = orderItemList.get(0).getSubTotal();
		int i2 = orderItemList.get(1).getSubTotal();
		int sum = i1 + i2 ;
		assertEquals(1400, sum);
	}
	@Test
	@DisplayName("カート追加　ログイン")
	@ExpectedDatabase(value = "/userInsertToCart", assertionMode = DatabaseAssertionMode.NON_STRICT)
	void insertToCart2() throws Exception{
		MockHttpSession userSession = com.example.util.SessionUtilShirai.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(post("/cart/insert")
				.session(userSession)
				.param("itemId", "1").param("size","M").param("quantity", "1")
				.param("orderToppingList", "1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0"))
				.andExpect(view().name("redirect:/cart/showCart")).andReturn();
	}
	
	@Test
	@DisplayName("カートから削除")
	void deleteFromCart() throws Exception{
		 MockHttpSession cartSession = com.example.util.SessionUtilShirai.createCartSession();
		 MvcResult mvcResult = mockMvc.perform(post("/cart/delete")
                 .session(cartSession)
                 .param("index", "0").param("orderItemId",""))
				 .andExpect(view().name("forward:/cart/showCart"))
				 .andReturn();
		 MockHttpSession mockSession = (MockHttpSession)mvcResult.getRequest().getSession(); 
		 assertNull(mockSession.getAttribute("orderItemList"));
	}
	
	@Test
	@DisplayName("カートを見る　未ログイン　カート追加なし")
	@DatabaseSetup("/popularSearch")
	void showCart0() throws Exception{
		 MvcResult mvcResult = mockMvc.perform(post("/cart/showCart"))
				 .andExpect(view().name("/cart/cart_list_noItem"))
				 .andReturn();
		 
//		 assertTrue("/cart/cart_list_noItem" == mvcResult.getModelAndView().getViewName());
	}
	@Test
	@DisplayName("カートを見る　未ログイン　既にカートリストあり　カート追加なし")
	@DatabaseSetup("/popularSearch")
	void showCart1() throws Exception{
		MockHttpSession session = com.example.util.SessionUtilShirai.createCartNonLogin();
		MvcResult mvcResult = mockMvc.perform(post("/cart/showCart").session(session))
				.andExpect(view().name("/cart/cart_list_noItem"))
				.andReturn();
		
//		 assertTrue("/cart/cart_list_noItem" == mvcResult.getModelAndView().getViewName());
	}
	@Test
	@DisplayName("カートを見る　未ログイン　カート追加あり")
	@DatabaseSetup("/loginInsertCart")
	void showCart2() throws Exception{
		MockHttpSession cartSession = com.example.util.SessionUtilShirai.createCartSession();
		MvcResult mvcResult = mockMvc.perform(post("/cart/showCart")
				.session(cartSession))
				.andExpect(view().name("/cart/cart_list"))
				.andReturn();
		MockHttpSession mockSession = (MockHttpSession)mvcResult.getRequest().getSession();
		assertEquals(70, mockSession.getAttribute("tax"));
		assertEquals(770, mockSession.getAttribute("totalPrice"));
	}
	@Test
	@DisplayName("カートを見る　ログイン　カート追加なし")
//	@DatabaseSetup("/popularSearch")
	void showCart3() throws Exception{
		MockHttpSession userSession = com.example.util.SessionUtilShirai.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(post("/cart/showCart")
				.session(userSession))
				.andExpect(view().name("/cart/cart_list_noItem"))
				.andReturn();
	}
	@Test
	@DisplayName("カートを見る　ログイン　カート追加あり")
	@DatabaseSetup("/loginInsertCart")
	void showCart4() throws Exception{
		MockHttpSession session = com.example.util.SessionUtilShirai.createUserCartSession();
		MvcResult mvcResult = mockMvc.perform(post("/cart/showCart")
				.session(session))
				.andExpect(view().name("/cart/cart_list"))
				.andReturn();
		MockHttpSession mockSession = (MockHttpSession)mvcResult.getRequest().getSession();
		assertEquals(70, mockSession.getAttribute("tax"));
		assertEquals(770, mockSession.getAttribute("totalPrice"));
	}
	
	@Test
	@DisplayName("カートを統合")
	@DatabaseSetup("/loginInsertCart")
	void combineCart() throws Exception{
		 MockHttpSession session = com.example.util.SessionUtilShirai.createCombineSession();
		 MvcResult mvcResult = mockMvc.perform(post("/cart/combineCart")
                 .session(session))
				 .andExpect(view().name("order/order_confirm"))
				 .andReturn();
		 
		 MockHttpSession mockSession = (MockHttpSession)mvcResult.getRequest().getSession(); 
		 assertEquals(140, mockSession.getAttribute("tax"));
		 assertEquals(1540, mockSession.getAttribute("totalPrice"));
	}

}
