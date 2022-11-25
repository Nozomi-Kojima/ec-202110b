package com.example.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
		user.setPassword("morimori");
		user.setZipcode("111-1111");
		user.setAddress("テスト住所");
		user.setTelephone("000-0000-0000");
		user.setPasswordConfirm("morimori");
		sessionMap.put("userId", user.getId());
		sessionMap.put("user", userList);
		return createMockHttpSession(sessionMap);
	}
	
	public static MockHttpSession createCartSession() {
		Map<String, Object> sessionMap = new LinkedHashMap<String, Object>();
		OrderItem orderitem = new OrderItem();
		Item item = new Item(1, "とんこつラーメン", "創業当時から今に引き継ぐとんこつラーメンの本流であり、原点の味。18時間の調理と、丸1日の熟成を経て、とんこつの旨味を極限まで抽出した豊かで香り高いシルキーなスープに、博多らしい細麺がマッチします。"
				, 700, 800, "1.jpg",false, null);
		List<OrderItem> orderItemList = new LinkedList<>();
		orderitem.setId(1);
		orderitem.setItem(item);
		orderitem.setItemId(1);
		orderitem.setOrderId(1);
		orderitem.setOrderToppingList(null);
		orderitem.setQuantity(1);
		orderitem.setSize('M');
		List<User> userList = new LinkedList<>(List.of());
		sessionMap.put("orderitemId", orderitem.getId());
		sessionMap.put("orderItemList", orderitem);
		sessionMap.put("userList", userList);
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
