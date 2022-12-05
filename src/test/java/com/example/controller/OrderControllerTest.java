package com.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.web.servlet.ModelAndView;

import com.example.util.CsvDataSetLoader;
import com.example.util.Tanaka_SessionUtil;
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
@Transactional
class OrderControllerTest {

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

	// ログインしていない場合ログイン画面に遷移し、ログイン後、DBに注文情報をインサートする
	@Test
	void testtoOrderConfirm() throws Exception {
		mockMvc.perform(get("/order/toOrder")).andExpect(view().name("redirect:/user/toLogin"));

	}
	
	//セッションスコープにorders.csvをセット。＠ExpectedDatabaseで格納されているか確認。
	@Test
	@DisplayName("セッションスコープが時間切れの時。セッションスコープにアイテムリストを格納しなおす。")
	@DatabaseSetup("/User/user")
	@ExpectedDatabase(value = "/User/user", assertionMode = DatabaseAssertionMode.NON_STRICT)
	void testOrderConfurm2() throws Exception {
		MockHttpSession userSession = Tanaka_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(get("/order/toOrder").session(userSession))
				.andExpect(view().name("order/order_confirm"))
				.andReturn();
	}

	// 配達時間
	@Test
	@DisplayName("注文確定時に配達時刻エラーチェックが正常に機能するかのテスト")
	void testOrderConfurm3() throws Exception {
		MockHttpSession userSession = Tanaka_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(post("/order/confirm")
				.param("deliveryDate", "2022-12-01")
				.param("deliveryTime", "12")
				.session(userSession))
				.andExpect(view().name("order/order_confirm"))// ここがnullなので1のCSV作ってｄｂsetuoする
				.andReturn();
	}
	
	@Test
	@DisplayName("「今から3時間後以降の日時をご入力ください」と表示されるかのテスト")
	void testOrderConfurm4() throws Exception {
		MockHttpSession userSession = Tanaka_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(post("/order/confirm")
				.param("destinationName", "田中佑奈")
				.param("destinationEmail", "tanaka@example.com")
				.param("zipcode", "111-1111")
				.param("address", "東京都新宿区")
				.param("destinationTel", "080-0000-0000")
				.param("deliveryDate", "2022-11-22")
				.param("deliveryTime", "11")
				.param("paymentMethod", "1")
				.session(userSession))
				.andExpect(view().name("order/order_confirm"))// ここがnullなので1のCSV作ってｄｂsetuoする
				.andReturn();
	}
	
	@Test
	@DisplayName("代引選択時にカード情報入力されていたらエラー表示をするテスト")
	void testOrderConfurm5() throws Exception {
		MockHttpSession userSession = Tanaka_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(post("/order/confirm")
				.param("paymentMethod", "1")
				.param("token", "hiden")
				.session(userSession))
				.andExpect(view().name("order/order_confirm"))// ここがnullなので1のCSV作ってｄｂsetuoする
				.andReturn();
	}
	@Test
	@DisplayName("代引き注文正常完了")
	@DatabaseSetup("/User/user2")
	void testOrderConfurm6() throws Exception {
		MockHttpSession userSession = Tanaka_SessionUtil.Kojima_OrderSession();
		MvcResult mvcResult2 = mockMvc.perform(post("/order/confirm")
				.param("userId", "2")
				.param("destinationName", "テストユーザー")
				.param("destinationEmail", "test@test.co.jp")
				.param("zipcode", "222-2222")
				.param("address", "東京都中野区")
				.param("destinationTel", "070-0000-0000")
				.param("deliveryDate", "2022-12-10")
				.param("deliveryTime", "18")
				.param("price", "850")
				.param("paymentMethod", "1")
				.param("token", "")
				.session(userSession))
				.andExpect(view().name("redirect:/order/toOrderFinished"))// ここがnullなので1のCSV作ってｄｂsetuoする
				.andReturn();
	}
	
	
	@Test
	@DisplayName("クレジットカード注文正常完了")
	@DatabaseSetup("/User/user2")
	void testOrderConfurm7() throws Exception {
		MockHttpSession userSession = Tanaka_SessionUtil.Kojima_OrderSession();
		MvcResult mvcResult = mockMvc.perform(post("/order/confirm")
				.param("userId", "1")
				.param("destinationName", "田中佑奈")
				.param("destinationEmail", "tanaka@example.com")
				.param("zipcode", "111-1111")
				.param("address", "東京都新宿区")
				.param("destinationTel", "080-0000-0000")
				.param("deliveryDate", "2022-12-10")
				.param("price", "750")
				.param("deliveryTime", "18")
				.param("paymentMethod", "2")
				.param("token", "hiden")
				.session(userSession))
				.andExpect(view().name("redirect:/order/toOrderFinished"))// ここがnullなので1のCSV作ってｄｂsetuoする
				.andReturn();
	}

	@Test
	@DisplayName("クレジットカード情報未入力")
	@DatabaseSetup("/User/user2")
	void testOrderConfurm8() throws Exception {
		MockHttpSession userSession = Tanaka_SessionUtil.Kojima_OrderSession();
		MvcResult mvcResult = mockMvc.perform(post("/order/confirm")
				.param("userId", "1")
				.param("destinationName", "田中佑奈")
				.param("destinationEmail", "tanaka@example.com")
				.param("zipcode", "111-1111")
				.param("address", "東京都新宿区")
				.param("destinationTel", "080-0000-0000")
				.param("deliveryDate", "2022-12-10")
				.param("price", "750")
				.param("deliveryTime", "18")
				.param("paymentMethod", "2")
				.param("token", "")
				.session(userSession))
				.andExpect(view().name("order/order_confirm"))// ここがnullなので1のCSV作ってｄｂsetuoする
				.andReturn();
	}
	@Test
	@DisplayName("配達時間エラー")
	@DatabaseSetup("/User/user2")
	void testOrderConfurm9() throws Exception {
		MockHttpSession userSession = Tanaka_SessionUtil.Kojima_OrderSession();
		MvcResult mvcResult = mockMvc.perform(post("/order/confirm")
				.param("userId", "1")
				.param("destinationName", "田中佑奈")
				.param("destinationEmail", "tanaka@example.com")
				.param("zipcode", "111-1111")
				.param("address", "東京都新宿区")
				.param("destinationTel", "080-0000-0000")
				.param("deliveryDate", "2022-12-01")
				.param("price", "750")
				.param("deliveryTime", "18")
				.param("paymentMethod", "2")
				.param("token", "")
				.session(userSession))
				.andExpect(view().name("order/order_confirm"))// ここがnullなので1のCSV作ってｄｂsetuoする
				.andReturn();
	}
	@Test
	@DisplayName("フォーマットエラー")
	@DatabaseSetup("/User/user2")
	void testOrderConfurm10() throws Exception {
		MockHttpSession userSession = Tanaka_SessionUtil.Kojima_OrderSession();
		MvcResult mvcResult = mockMvc.perform(post("/order/confirm")
				.param("deliveryDate", "99999129-349")
				.param("deliveryTime", "24")
				.session(userSession))
				.andExpect(status().is5xxServerError())
				.andReturn();
	}
	@Test
	@DisplayName("配達時間のみエラー")
	@DatabaseSetup("/User/user2")
	void testOrderConfurm11() throws Exception {
		MockHttpSession userSession = Tanaka_SessionUtil.Kojima_OrderSession();
		MvcResult mvcResult = mockMvc.perform(post("/order/confirm")
				.param("userId", "1")
				.param("destinationName", "田中佑奈")
				.param("destinationEmail", "tanaka@example.com")
				.param("zipcode", "111-1111")
				.param("address", "東京都新宿区")
				.param("destinationTel", "080-0000-0000")
				.param("price", "750")
				.param("paymentMethod", "2")
				.param("token", "hiden")
				.param("deliveryDate", "9999129-349")
				.param("deliveryTime", "0")
				.session(userSession))
				.andExpect(status().is5xxServerError())
				.andReturn();
	}
	
	
	@Test
	@DisplayName("注文完了画面に遷移するか確認するテスト")
	void testOrderConfirm() throws Exception { 
        MockHttpSession userIdSession = Tanaka_SessionUtil.createUserIdAndUserSession();
        MvcResult mvcResult = mockMvc.perform(post("/order/toOrderFinished")
                        .session(userIdSession))//sessionセットしてから呼ぶとテストできる
                .andExpect(view().name("order/order_finished"))
                .andReturn();
    }

	
}
