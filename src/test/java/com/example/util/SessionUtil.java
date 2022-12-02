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
//		user.setId(2);
		user.setName("テストユーザ");
		user.setEmail("testtest@gmail.com");
		user.setPassword("$2a$10$Utoo6nr3XIFEh4xOZ9Zr1.n/PtEYBb8HhlLDDklaJwsj.T3uux4kq");
		user.setPasswordConfirm("$2a$10$Utoo6nr3XIFEh4xOZ9Zr1.n/PtEYBb8HhlLDDklaJwsj.T3uux4kq");
		user.setAddress("神奈川県相模原市");
		user.setZipcode("123-4567");
		user.setTelephone("090-1234-5678");
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
