package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class
})
@Transactional
/*
 * コントローラー実行後のセッション情報の取り出しは以下でいけそうです
 * （あくまでサンプルです。（ログイン成功後、セッションに「userId」という名前でidを保存できているかをテストする場合））
 * 
 * MockHttpSession mockSession = (MockHttpSession)mvcResult.getRequest().getSession(); 
 * Integer userId = (Integer)mockSession.getAttribute("userId");
 */

class CartControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
//	@Autowired
//	private CartController cart;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	
	/*
	 * @BeforeEach void each()throws Exception { MvcResult mvcResult1 =
	 * mockMvc.perform(post("/item/item_list_noodle").param("id", "1"))
	 * .andExpect(view().name("/item/item_detail")).andReturn(); }
	 */

	@Test
	@DisplayName("カート追加　未ログイン")
	void insertToCart() throws Exception{
//		List<Integer> topping = new ArrayList<>(List.of(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0));
		
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
		
		assertEquals(result.get(0).getItem().getName(), orderItemList.get(0).getItem().getName());
	}
	
	@Test
	@DisplayName("カートから削除")
	void deleteFromCart() throws Exception{
		 MockHttpSession cartSession = com.example.util.SessionUtil.createCartSession();
		 MvcResult mvcResult = mockMvc.perform(post("/cart/delete")
                 .session(cartSession)
                 .param("index", "0"))
				 .andExpect(view().name("/cart/showCart"))
				 .andReturn();
		 MockHttpSession mockSession = (MockHttpSession)mvcResult.getRequest().getSession(); 
		 assertNull(mockSession);
	}
	
	@Test
	@DisplayName("カートを見る")
	@DatabaseSetup("/popularSearch")
	void showCart() throws Exception{
		
	}
	
	@Test
	@DisplayName("カートを統合")
	void combineCart() throws Exception{
		 MockHttpSession cartSession = com.example.util.SessionUtil.createCartSession();
		 MockHttpSession userSession = com.example.util.SessionUtil.createUserIdAndUserSession();
		 MvcResult mvcResult = mockMvc.perform(post("/cart/combineCart")
                 .session(cartSession).session(userSession))
				 .andExpect(view().name("/order/order_confirm"))
				 .andReturn();
		
		 MockHttpSession mockSession = (MockHttpSession)mvcResult.getRequest().getSession(); 
		 
		 assertEquals(770, mockSession.getAttribute("totalPrice"));
	}

}
