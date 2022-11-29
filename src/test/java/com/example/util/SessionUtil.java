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
		user.setName("テスト");
		user.setEmail("test@test.co.jp");
		user.setZipcode("000-0000");
		user.setAddress("テスト住所");
		user.setTelephone("000-1111-2222");
		user.setPassword("$2a$10$Utoo6nr3XIFEh4xOZ9Zr1.n/PtEYBb8HhlLDDklaJwsj.T3uux4kq");
		user.setPasswordConfirm("$2a$10$Utoo6nr3XIFEh4xOZ9Zr1.n/PtEYBb8HhlLDDklaJwsj.T3uux4kq");
		userList.add(user); 
		sessionMap.put("userId", user.getId());
		sessionMap.put("user",userList);
		OrderItem orderItem = new OrderItem();
		List<OrderItem> orderItemList = new ArrayList<>();
		orderItem.setId(1);
		orderItem.setItemId(1);
		orderItem.setOrderId(1);
		orderItem.setQuantity(1);
		orderItem.setSize('M');
		orderItemList.add(orderItem);
		List<Integer> subTotalList = new ArrayList<>();
		subTotalList.add(1000);
		subTotalList.add(2000);
		subTotalList.add(3000);
		sessionMap.put("subTotalList", subTotalList);
		Item itemName = new Item();
		List<Item> itemList = new ArrayList<>();
		itemName.setPriceL(200);
		itemName.setPriceM(100);
		itemName.setName("とんかつ");
		itemName.setDescription("とんかつはあげものです");
		itemList.add(itemName);
		sessionMap.put("itemList", itemList);
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
