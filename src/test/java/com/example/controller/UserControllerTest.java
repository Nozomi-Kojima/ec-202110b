package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

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

import com.example.domain.User;
import com.example.form.LoginForm;
import com.example.util.CsvDataSetLoader;
import com.example.util.SessionUtil;
import com.example.util.SessionUtilShirai;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;


@SpringBootTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
      DependencyInjectionTestExecutionListener.class, // このテストクラスでDIを使えるように指定
      TransactionDbUnitTestExecutionListener.class // @DatabaseSetupや@ExpectedDatabaseなどを使えるように指定
})
@Transactional
class UserControllerTest {
	
	
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
	
	@Test
	@DisplayName("メールアドレス重複")
	@DatabaseSetup("/User/insert_01/expected")
	void ConfirmTest1() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResultDuplication = mockMvc.perform(post("/user/registerConfirm")
				.param("email","ramen.test@gmail.com")
//				.session(userSession)
				)
				.andExpect(view().name("/user/register_user")).andReturn();
	}
	@Test
	@DisplayName("passが一致していない")
	@DatabaseSetup("/User/insert_01/expected")
	void ConfirmTest2() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResultDuplication = mockMvc.perform(post("/user/registerConfirm")
				.param("password","Abcd1234")
				.param("passwordConfirm", "Abcd12")
//				.session(userSession)
				)
				.andExpect(view().name("/user/register_user")).andReturn();
	}
	@Test
	@DisplayName("上記２つ以外の場合")
	@DatabaseSetup("/User/insert_01/expected")
	void ConfirmTest3() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResultDuplication = mockMvc.perform(post("/user/registerConfirm")
				.param("email","@gmail.com")
				.param("password","Abcd12345")
				.param("passwordConfirm", "Abcd12345")
				.session(userSession))
				.andExpect(view().name("/user/register_user")).andReturn();
	}
	@Test
	@DisplayName("メールアドレスの重複")
	@DatabaseSetup("/User/insert_01/expected")
	void ConfirmTest4() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResultDuplication = mockMvc.perform(post("/user/registerConfirm")
				.param("name", "鈴木太郎")
                .param("email", "ramen.test@gmail.com")
                .param("password", "Abcd1234")
                .param("passwordConfirm", "Abcd1234")
                .param("zipcode", "111-1111")
                .param("address", "東京都新宿区")
                .param("telephone", "03-1111-1111")
				.session(userSession))
				.andExpect(view().name("/user/register_user")).andReturn();
	}
	@Test
	@DisplayName("passが一致していない")
	@DatabaseSetup("/User/insert_01/expected")
	void ConfirmTest5() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResultDuplication = mockMvc.perform(post("/user/registerConfirm")
				.param("name", "鈴木太郎")
                .param("email", "suzuki@example.com")
                .param("password", "Abcd1234")
                .param("passwordConfirm", "Abcd123")
                .param("zipcode", "111-1111")
                .param("address", "東京都新宿区")
                .param("telephone", "03-1111-1111")
				.session(userSession))
				.andExpect(view().name("/user/register_user")).andReturn();
	}
	
	
	@Test
	@DisplayName("パスワード●表示")
	@DatabaseSetup("/User/insert_01/expected")
	void ConfirmTest6() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResultDuplication = mockMvc.perform(post("/user/registerConfirm")
                .param("email", "test@gmail.com")
                .param("password", "Abcd1234")
                .param("passwordConfirm", "Abcd1234")
				.session(userSession))
				.andExpect(view().name("/user/register_confirm")).andReturn();
}
	    @Test
	    @DisplayName("ユーザー登録")
        @ExpectedDatabase(value = "/Insert/insert-01/expected", assertionMode = DatabaseAssertionMode.NON_STRICT)
	    void insert() throws Exception {
		 MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
	        MvcResult mvcResult = mockMvc.perform(get("/user/register")
                  .session(userSession)
	                ).andExpect(view().name("redirect:/user/toFinish"))
	                .andReturn();

	    }
	    @Test
	    @DisplayName("ログイン画面への遷移")
	    void login_page() throws Exception {
	        MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
	        MvcResult mvcResult = mockMvc.perform(get("/user/toLogin")
	                        .session(userSession))
	                .andExpect(view().name("/user/login"))
	                .andReturn();
	    }
	    
	    @Test
	    @DisplayName("登録完了画面への遷移")
	    void finish_page() throws Exception {
	        MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
	        MvcResult mvcResult = mockMvc.perform(get("/user/toFinish")
	                        .session(userSession))
	                .andExpect(view().name("/user/register_finish"))
	                .andReturn();
	    }
	    
	    @Test
	    @DisplayName("ログアウト画面への遷移")
	    void logout_page() throws Exception {
	       // MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
	        MvcResult mvcResult = mockMvc.perform(get("/user/logout")
	                      //  .session(userSession)
	        		)
	                .andExpect(view().name("redirect:/item"))
	                .andReturn();
	    }
	    @Test
	    @DisplayName("ログイン(異常系)")
	    void login0() throws Exception {
	        MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
	        MvcResult mvcResult = mockMvc.perform(get("/user/login")
	                        .session(userSession))
	                .andExpect(view().name("/user/login"))
	                .andReturn();
	    }
	    @Test
	    @DisplayName("ログイン(異常系2)")
	    @DatabaseSetup("/User/user_login")
	    void login1() throws Exception {
	    	MvcResult mvcResult = mockMvc.perform(post("/user/login")
	    			.param("email", "test@test.co.jp")
	    			.param("password", "moromoro"))
	    			.andExpect(view().name("/user/login"))
	    			.andReturn();
	    }
	    @Test
	    @DatabaseSetup("/User/user_login")
	    @DisplayName("ログインしてカートへ")
	    void login2() throws Exception {
	    	MockHttpSession cartSession = SessionUtilShirai.createCartSession();
	    	MvcResult mvcResult = mockMvc.perform(post("/user/login")
	    			.session(cartSession)
	    			.param("email", "test@test.co.jp")
	    			.param("password", "morimori"))
	    			.andExpect(redirectedUrl("/cart/combineCart"))
	    			.andReturn();
	    	MockHttpSession mockSession = (MockHttpSession)mvcResult.getRequest().getSession(); 
	    	List<User> userList = (List<User>)mockSession.getAttribute("user");
	    	User user = userList.get(0);
	    	assertEquals("テストユーザ", user.getName());
	    }
	    @Test
	    @DatabaseSetup("/User/user_login")
	    @DisplayName("ログインして注文履歴へ")
	    void login3() throws Exception {
	    	MockHttpSession cartSession = SessionUtilShirai.fromOrderHistoryLogin();
	    	MvcResult mvcResult = mockMvc.perform(post("/user/login")
	    			.session(cartSession)
	    			.param("email", "test@test.co.jp")
	    			.param("password", "morimori"))
	    			.andExpect(view().name("forward:/orderHistory/"))
	    			.andReturn();
	    
	    }
	    @Test
	    @DatabaseSetup("/User/user_login")
	    @DisplayName("ログインして商品一覧へ")
	    void login4() throws Exception {
	    	MvcResult mvcResult = mockMvc.perform(post("/user/login")
	    			.param("email", "test@test.co.jp")
	    			.param("password", "morimori"))
	    			.andExpect(redirectedUrl("/item"))
	    			.andReturn();
	    	
	    }
	   
	
	
}
