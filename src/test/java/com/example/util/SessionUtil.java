package com.example.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

import com.example.domain.User;
import com.example.service.UserService;

public class SessionUtil {

	public static MockHttpSession createUserIdAndUserSession() {
		Map<String, Object> sessionMap = new LinkedHashMap<String, Object>();
		User user = new User();
		List<User> userList = new ArrayList<>();
		user.setId(1);
		user.setName("テストユーザ");
		user.setEmail("ramen.test@gmail.com");
		user.setAddress("テスト住所");
		user.setZipcode("1111111");
		user.setTelephone("テスト電話番号");
		userList.add(user);
		sessionMap.put("userId", user.getId());
		sessionMap.put("user", userList);
		return createMockHttpSession(sessionMap);
	}

	private static MockHttpSession createMockHttpSession(Map<String, Object> sessions) {
		MockHttpSession mockHttpSession = new MockHttpSession();
		for (Map.Entry<String, Object> session : sessions.entrySet()) {
			mockHttpSession.setAttribute(session.getKey(), session.getValue());
		}
		return mockHttpSession;
	}
}
