package com.example.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.mock.web.MockHttpSession;

import com.example.domain.Item;
import com.example.domain.OrderItem;
import com.example.domain.User;
public class SessionUtil {

	public static MockHttpSession createUserIdAndUserSession() {
		Map<String, Object> sessionMap = new LinkedHashMap<String, Object>();
		User user = new User();
		List<User> userList = new ArrayList<>();
		user.setId(1);
		user.setName("テストユーザ");
		user.setEmail("test@test.co.jp");
		user.setAddress("テスト住所");
		user.setZipcode("111-1111");
		user.setTelephone("テスト電話番号");
		userList.add(user);
		sessionMap.put("userId", user.getId());
		sessionMap.put("user", userList);
		User loginUser = new User();
		List<User> loginUserList = new ArrayList<>();
		loginUser.setId(1);
		loginUser.setName("田中佑奈");
		loginUser.setEmail("tanaka@example.com");
		loginUser.setAddress("東京都新宿区");
		loginUser.setZipcode("111-1111");
		loginUser.setPassword("Tanaka2525");
		loginUser.setTelephone("080-0000-0000");
		loginUser.setPasswordConfirm("Tanaka2525");
		loginUserList.add(loginUser);
		sessionMap.put("user", loginUserList);
		OrderItem orderItem = new OrderItem();
		List<OrderItem> orderItemList = new ArrayList<>();
		orderItem.setId(1);
		orderItem.setItemId(1);
		orderItem.setOrderId(1);
		orderItem.setQuantity(1);
		orderItem.setSize('M');
		orderItem.setId(2);
		orderItem.setItemId(2);
		orderItem.setOrderId(2);
		orderItem.setQuantity(1);
		orderItem.setSize('L');
		orderItem.setId(3);
		orderItem.setItemId(2);
		orderItem.setOrderId(3);
		orderItem.setQuantity(1);
		orderItem.setSize('M');
		orderItem.setId(4);
		orderItem.setItemId(2);
		orderItem.setOrderId(4);
		orderItem.setQuantity(1);
		orderItem.setSize('M');
		orderItem.setId(5);
		orderItem.setItemId(1);
		orderItem.setOrderId(5);
		orderItem.setQuantity(1);
		orderItem.setSize('M');
		orderItem.setId(6);
		orderItem.setItemId(1);
		orderItem.setOrderId(6);
		orderItem.setQuantity(1);
		orderItem.setSize(null);
		orderItem.setId(7);
		orderItem.setItemId(3);
		orderItem.setOrderId(7);
		orderItem.setQuantity(1);
		orderItem.setSize(null);
		orderItem.setId(8);
		orderItem.setItemId(4);
		orderItem.setOrderId(8);
		orderItem.setQuantity(1);
		orderItem.setSize(null);
		orderItem.setId(9);
		orderItem.setItemId(4);
		orderItem.setOrderId(9);
		orderItem.setQuantity(1);
		orderItem.setSize(null);
		List<Integer> subTotalList = new ArrayList<>();
		subTotalList.add(1000);
		subTotalList.add(2000);
		subTotalList.add(3000);
		sessionMap.put("subTotalList", subTotalList);
		Integer totalPrice = 6000;
		sessionMap.put("totalPrice", totalPrice);
		List<Integer> yearList = new ArrayList<>();
		yearList.add(2011);
		yearList.add(2013);
		yearList.add(2018);
		List<Integer> monthList = new ArrayList<>();
		monthList.add(1);
		monthList.add(2);
		monthList.add(3);
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
