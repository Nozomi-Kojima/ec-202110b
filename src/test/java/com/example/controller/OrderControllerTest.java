package com.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.example.util.CsvDataSetLoader;
import com.example.util.SessionUtil;
import com.example.util.Tanaka_SessionUtil;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, // このテストクラスでDIを使えるように指定
		TransactionDbUnitTestExecutionListener.class
		//
})
@Transactional
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@SpringBootTest
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
		MvcResult mvcResult = mockMvc.perform(post("/order/toOrder").session(userSession))
				.andExpect(view().name("order/order_confirm"))// ここがnullなので1のCSV作ってｄｂsetuoする
				.andReturn();
	}

	// 配達時間
	@Test
	@DisplayName("注文確定時に配達時刻エラーチェックが正常に機能するかのテスト")
	void testOrderConfurm3() throws Exception {
		MockHttpSession userSession = Tanaka_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(post("/order/confirm")
				.param("destinationName", "田中佑奈")
				.param("destinationEmail", "tanaka@example.com")
				.param("zipcode", "111-1111")
				.param("address", "東京都新宿区")// 配達時刻を33時とか-1時
				.param("destinationTel", "080-0000-0000")
				.param("deliveryDate", "2022-11-28")
				.param("deliveryTime", "33")
				.param("paymentMethod", "1")
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
				.param("destinationName", "田中佑奈")
				.param("destinationEmail", "tanaka@example.com")
				.param("zipcode", "111-1111")
				.param("address", "東京都新宿区")
				.param("destinationTel", "080-0000-0000")
				.param("deliveryDate", "2022-11-30")
				.param("deliveryTime", "11")
				.param("paymentMethod", "1")
				.param("token", "hiden")
				.session(userSession))
				.andExpect(view().name("order/order_confirm"))// ここがnullなので1のCSV作ってｄｂsetuoする
				.andReturn();
	}
	
	@Test
	@DisplayName("注文正常完了")
	@DatabaseSetup("/User/user")
	void testOrderConfurm6() throws Exception {
		MockHttpSession userSession = Tanaka_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(post("/order/confirm")
				.param("destinationName", "田中佑奈")
				.param("destinationEmail", "tanaka@example.com")
				.param("zipcode", "111-1111")
				.param("address", "東京都新宿区")
				.param("destinationTel", "080-0000-0000")
				.param("deliveryDate", "2022-11-30")
				.param("deliveryTime", "11")
				.param("paymentMethod", "2")
				.param("token", "hiden")
				.session(userSession))
				.andExpect(view().name("redirect:/order/toOrderFinished"))// ここがnullなので1のCSV作ってｄｂsetuoする
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
