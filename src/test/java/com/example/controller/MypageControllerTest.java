package com.example.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.AfterAll;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.example.domain.User;
import com.example.util.CsvDataSetLoader;
import com.example.util.Kojima_SessionUtil;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;


@SpringBootTest
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class)
@TestExecutionListeners({
      DependencyInjectionTestExecutionListener.class,
      TransactionDbUnitTestExecutionListener.class 
})
class MypageControllerTest {
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

	@Test
	@DisplayName("??????????????????????????????")
	void toMypageTest() throws Exception {
		MockHttpSession userSession = Kojima_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(get("/mypage/toMypage").session(userSession))
				.andExpect(view().name("/mypage/mypage")).andReturn();		
	    ModelAndView mav = mvcResult.getModelAndView();
        assertEquals("/mypage/mypage", mav.getViewName());
	}
	@Test
	@DisplayName("????????????????????????????????????????????????????????????")
	void toMypageTest2() throws Exception {
		MockHttpSession userSession = Kojima_SessionUtil.createUserIdAndUserSession();
		userSession.clearAttributes();
		MvcResult mvcResult = mockMvc.perform(get("/mypage/toMypage").session(userSession))
				.andExpect(view().name("forward:/user/toLogin")).andReturn();
	    ModelAndView mav = mvcResult.getModelAndView();
        assertEquals("forward:/user/toLogin", mav.getViewName());
	}
	@Test
	@DisplayName("?????????????????????????????????")
	void toUpdateUserTest() throws Exception {
		MockHttpSession userSession = Kojima_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(get("/mypage/toUpdateUser").session(userSession))
				.andExpect(view().name("/mypage/updateUser")).andReturn();
	    ModelAndView mav = mvcResult.getModelAndView();
        assertEquals("/mypage/updateUser", mav.getViewName());
		userSession.clearAttributes();
		MvcResult mvcResultfailure = mockMvc.perform(get("/mypage/toUpdateUser").session(userSession))
				.andExpect(view().name("forward:/user/toLogin")).andReturn();
	    ModelAndView mav1 = mvcResultfailure.getModelAndView();
        assertEquals("forward:/user/toLogin", mav1.getViewName());
		
	}
	@Test
	@DisplayName("???????????????????????????????????????")
	void toUpdateUserFinishedTest() throws Exception {
		MockHttpSession userSession = Kojima_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(get("/mypage/toUpdateUserFinished").session(userSession))
				.andExpect(view().name("/mypage/updateUserFinished")).andReturn();
	    ModelAndView mav = mvcResult.getModelAndView();
        assertEquals("/mypage/updateUserFinished", mav.getViewName());
		userSession.clearAttributes();
		MvcResult mvcResultfailure = mockMvc.perform(get("/mypage/toUpdateUserFinished").session(userSession))
				.andExpect(view().name("forward:/user/toLogin")).andReturn();
		   ModelAndView mav1 = mvcResultfailure.getModelAndView();
	        assertEquals("forward:/user/toLogin", mav1.getViewName());
		
	}
	@Test
	@DisplayName("?????????????????????????????????????????????")
	void toDeleteUserConfirmTest() throws Exception {
		MockHttpSession userSession = Kojima_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(get("/mypage/toDeleteUserConfirm").session(userSession))
				.andExpect(view().name("/mypage/deleteUserConfirm")).andReturn();
	    ModelAndView mav = mvcResult.getModelAndView();
        assertEquals("/mypage/deleteUserConfirm", mav.getViewName());
		userSession.clearAttributes();
		MvcResult mvcResultfailure = mockMvc.perform(get("/mypage/toDeleteUserConfirm").session(userSession))
				.andExpect(view().name("forward:/user/toLogin")).andReturn();
		   ModelAndView mav1 = mvcResultfailure.getModelAndView();
	        assertEquals("forward:/user/toLogin", mav1.getViewName());
		
	}
	@Test
	@DisplayName("??????????????????????????????????????????????????????")
	void updateUserConfirmTest() throws Exception {
		MockHttpSession userSession = Kojima_SessionUtil.createUserIdAndUserSession();		
		
		MvcResult mvcResultError = mockMvc.perform(post("/mypage/updateUserConfirm")
				.param("name", "??????")
				.param("email","kojima@test.com")
				.param("zipcode", "22222")
				.param("address", "?????????")
				.param("telephone", "????????????")
				.session(userSession))
				.andExpect(view().name("/mypage/updateUser")).andReturn();
	    ModelAndView mav = mvcResultError.getModelAndView();
        assertEquals("/mypage/updateUser", mav.getViewName());
	}
	
	@Test
	@DisplayName("???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????")
	@DatabaseSetup("/myPage/toMypage_01")
	void updateUserConfirmTest2() throws Exception {
		MockHttpSession userSession = Kojima_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResultDuplication = mockMvc.perform(post("/mypage/updateUserConfirm")
				.param("email","nobuhiko.tobita@gmail.com")
				.session(userSession))
				.andExpect(view().name("/mypage/updateUser")).andReturn();
	    ModelAndView mav = mvcResultDuplication.getModelAndView();
        assertEquals("/mypage/updateUser", mav.getViewName());
	}
	
	@Test
	@DisplayName("?????????????????????????????????????????????????????????????????????")
	@DatabaseSetup("/myPage/toMypage_01")
	void updateUserConfirmTest3() throws Exception {
		MockHttpSession userSession = Kojima_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResultSuccess = mockMvc.perform(post("/mypage/updateUserConfirm")
				.param("name", "??????")
				.param("email","kojima@test.com")
				.param("zipcode", "222-2222")
				.param("address", "?????????")
				.param("telephone", "030-2459-3249")
				.session(userSession))
				.andExpect(view().name("/mypage/updateUserConfirm")).andReturn();
	    ModelAndView mav = mvcResultSuccess.getModelAndView();
        assertEquals("/mypage/updateUserConfirm", mav.getViewName());
	}
	@Test
	@DisplayName("???????????????????????????????????????????????????????????????????????????")
	@DatabaseSetup("/myPage/toMypage_01")
	void updateUserConfirmTest4() throws Exception {
		MockHttpSession userSession = Kojima_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(post("/mypage/updateUser")
				.param("password", "morimoriadaaoi")
				.session(userSession))
				.andExpect(view().name("/mypage/updateUserConfirm")).andReturn();
	    ModelAndView mav = mvcResult.getModelAndView();
        assertEquals("/mypage/updateUserConfirm", mav.getViewName());
	}
	@Test
	@DisplayName("??????????????????????????????????????????????????????????????????????????????????????????")
	void updateUserConfirmTest5() throws Exception {
		MockHttpSession userSession = Kojima_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(post("/mypage/updateUser")
				.param("password", "morimori")
				.session(userSession))
				.andExpect(view().name("redirect:/mypage/toUpdateUserFinished")).andReturn();
	    ModelAndView mav = mvcResult.getModelAndView();
        assertEquals("redirect:/mypage/toUpdateUserFinished", mav.getViewName());
	}
	@Test
	@DisplayName("?????????????????????yes?????????")
	void deleteUserConfirmTest1() throws Exception {
		MockHttpSession userSession = Kojima_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult2 = mockMvc.perform(post("/mypage/deleteUserConfirm")
				.param("confirm", "yes")
				.session(userSession))
				.andExpect(view().name("mypage/deleteUser")).andReturn();
	    ModelAndView mav = mvcResult2.getModelAndView();
        assertEquals("mypage/deleteUser", mav.getViewName());
	}
	@Test
	@DisplayName("?????????????????????no?????????")
	@DatabaseSetup("/myPage/toMypage_01")
	void deleteUserConfirmTest2() throws Exception {
		MockHttpSession userSession = Kojima_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(post("/mypage/deleteUserConfirm")
				.param("confilm", "no")
				.session(userSession))
				.andExpect(view().name("mypage/notDeleteUser")).andReturn();
	    ModelAndView mav = mvcResult.getModelAndView();
        assertEquals("mypage/notDeleteUser", mav.getViewName());
	}
	
	@Test
	@DisplayName("???????????????????????????????????????????????????????????????????????????")
	void deleteUserTest1() throws Exception {
		MockHttpSession userSession = Kojima_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(post("/mypage/deleteUser")
				.param("inputPassword", "morimoriadaaoi")
				.session(userSession))
				.andExpect(view().name("/mypage/deleteUserConfirm")).andReturn();
	    ModelAndView mav = mvcResult.getModelAndView();
        assertEquals("/mypage/deleteUserConfirm", mav.getViewName());
	}
	@Test
	@DisplayName("??????????????????????????????????????????????????????????????????????????????????????????")
	void deleteUserTest2() throws Exception {
		MockHttpSession userSession = Kojima_SessionUtil.createUserIdAndUserSession();
		MvcResult mvcResult = mockMvc.perform(post("/mypage/deleteUser")
				.param("inputPassword", "morimori")
				.session(userSession))
				.andExpect(view().name("/mypage/deleteUserFinished")).andReturn();
		   ModelAndView mav = mvcResult.getModelAndView();
	        assertEquals("/mypage/deleteUserFinished", mav.getViewName());
	}

	
}
