package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.example.domain.Item;
import com.example.domain.OrderItem;
import com.example.form.OrderItemForm;
import com.example.util.CsvDataSetLoader;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	TransactionalTestExecutionListener.class
})

/*
 * コントローラー実行後のセッション情報の取り出しは以下でいけそうです
 * （あくまでサンプルです。（ログイン成功後、セッションに「userId」という名前でidを保存できているかをテストする場合））
 * 
 * MockHttpSession mockSession = (MockHttpSession)
 * mvcResult.getRequest().getSession(); Integer userId = (Integer)
 * mockSession.getAttribute("userId");
 */

class CartControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
//	@Autowired
//	private HttpSession session;
	
	CartController cart = new CartController();
	
	/*
	 * @BeforeEach void each()throws Exception { MvcResult mvcResult1 =
	 * mockMvc.perform(post("/item/item_list_noodle").param("id", "1"))
	 * .andExpect(view().name("/item/item_detail")).andReturn(); }
	 */

	@Test
	@DisplayName("カート追加　未ログイン")
	void insertToCart() throws Exception{
		MvcResult mvcResult = mockMvc.perform(get("http://localhost:8080/ec-202110b/item/showDetail?id=1")
				.param("size","M").param("quantity", "1"))
				.andExpect(view().name("/cart/insert")).andReturn();
		MockHttpSession mockSession = (MockHttpSession)mvcResult.getRequest().getSession(); 
		
		@SuppressWarnings("unchecked")
		List<OrderItem> orderItemList = (List<OrderItem>)mockSession.getAttribute("orderItemList");
		
		OrderItemForm form = new OrderItemForm("1", "1", "M", null);
		cart.insertToCart(form, 1);
		
		Item i = new Item(1, "とんこつラーメン", "創業当時から今に引き継ぐとんこつラーメンの本流であり、原点の味。18時間の調理と、丸1日の熟成を経て、とんこつの旨味を極限まで抽出した豊かで香り高いシルキーなスープに、博多らしい細麺がマッチします。"
				, 700, 800, "1.jpg",false, null);
		OrderItem item = new OrderItem(1, 1, 1, 1, 'M', i, null);
		List<OrderItem> result = new ArrayList<>(List.of(item));
		assertEquals(result, orderItemList);
	}

}
