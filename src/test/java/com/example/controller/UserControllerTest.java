package com.example.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.example.util.CsvDataSetLoader;
import com.example.util.SessionUtil;
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

//	@Test
//    @DisplayName("ユーザー登録　正常系")
//	@ExpectedDatabase(value = "/User/insert_01/expected", assertionMode = DatabaseAssertionMode.NON_STRICT)
//    void insert1() throws Exception {
//		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
//        MvcResult mvcResult = mockMvc.perform(post("/user/registerConfirm")
//                        .param("name", "飛田宣彦")
//                        .param("email", "nobuhiko.tobita@gmail.com")
//                        .param("password", "Abcd1234")
//                        .param("passwordConfirm", "Abcd1234")
//                        .param("zipcode", "111-1111")
//                        .param("address", "東京都新宿区")
//                        .param("telephone", "03-1111-1111")
//                        .session(userSession))
//                .andExpect(view().name("/user/register_confirm"))
//                .andReturn();
//}
	
	@Test
	@DisplayName("メールアドレス重複")
	@DatabaseSetup("/User/insert_01/expected")
	void ConfirmTest1() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResultDuplication = mockMvc.perform(post("/user/registerConfirm")
				.param("email","ramen.test@example.com")
				.session(userSession))
				.andExpect(view().name("/user/register_user")).andReturn();
	}
	@Test
	@DisplayName("アドレスが一致していない")
	@DatabaseSetup("/User/insert_01/expected")
	void ConfirmTest2() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResultDuplication = mockMvc.perform(post("/user/registerConfirm")
				.param("password","morimori")
				.param("passwordConfirm", "morimo")
				.session(userSession))
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
	@DisplayName("メールアドレスだけ重複")
	@DatabaseSetup("/User/insert_01/expected")
	void ConfirmTest4() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResultDuplication = mockMvc.perform(post("/user/registerConfirm")
				.param("name", "飛田宣彦")
                .param("email", "ramen.test@example.com")
                .param("password", "Abcd1234")
                .param("passwordConfirm", "Abcd1234")
                .param("zipcode", "111-1111")
                .param("address", "東京都新宿区")
                .param("telephone", "03-1111-1111")
				.session(userSession))
				.andExpect(view().name("/user/register_user")).andReturn();
	}
	@Test
	@DisplayName("アドレスが一致していない")
	@DatabaseSetup("/User/insert_01/expected")
	void ConfirmTest5() throws Exception {
		MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResultDuplication = mockMvc.perform(post("/user/registerConfirm")
				.param("name", "飛田宣彦")
                .param("email", "nobuhiko.tobita@gmail.com")
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
                .param("email", "nobuhiko.tobita@gmail.com")
                .param("password", "Abcd1234")
                .param("passwordConfirm", "Abcd1234")
				.session(userSession))
				.andExpect(view().name("/user/register_confirm")).andReturn();
}
	    @Test
	    @DisplayName("ユーザー登録")
        @ExpectedDatabase(value = "/User/insert_01/expected", assertionMode = DatabaseAssertionMode.NON_STRICT)
	    void insert() throws Exception {
		 MockHttpSession userSession = SessionUtil.createUserIdAndUserSession();
	        MvcResult mvcResult = mockMvc.perform(get("/user/register")
                  .session(userSession)
	                ).andExpect(view().name("redirect:/user/toFinish"))
	                .andReturn();

	    }
	   
	
	
}
