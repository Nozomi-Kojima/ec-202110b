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

	public static MockHttpSession kojima_createUserIdAndUserSession() {
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
    	public static MockHttpSession shirai_createUserIdAndUserSession() {
		Map<String, Object> sessionMap = new LinkedHashMap<String, Object>();
		User user = new User();
		List<User> userList = new ArrayList<>();
    user.setId(1);
		user.setName("テストユーザ");
		user.setEmail("test@test.co.jp");
		user.setPassword("$2a$10$Utoo6nr3XIFEh4xOZ9Zr1.n/PtEYBb8HhlLDDklaJwsj.T3uux4kq");
		user.setZipcode("111-1111");
		user.setAddress("テスト住所");
		user.setTelephone("000-0000-0000");
		user.setPasswordConfirm("$2a$10$Utoo6nr3XIFEh4xOZ9Zr1.n/PtEYBb8HhlLDDklaJwsj.T3uux4kq");
		userList.add(user);
		sessionMap.put("userId", user.getId());
		sessionMap.put("user", userList);
		return createMockHttpSession(sessionMap);
	}
	
	public static MockHttpSession createCartSession() {
		Map<String, Object> sessionMap = new LinkedHashMap<String, Object>();
		OrderItem orderitem = new OrderItem();
		Topping top1 = new Topping(1,"チャーシュー",200,300);
		Topping top2 = new Topping(2,"煮たまご",200,300);
		Topping top3 = new Topping(3,"メンマ",200,300);
		Topping top4 = new Topping(4,"のり",200,300);
		Topping top5 = new Topping(5,"もやし",200,300);
		Topping top6 = new Topping(6,"ほうれん草",200,300);
		Topping top7 = new Topping(7,"車麩",200,300);
		Topping top8 = new Topping(8,"バター",200,300);
		Topping top9 = new Topping(9,"白髪ねぎ",200,300);
		Topping top10 = new Topping(10,"紫たまねぎ",200,300);
		Topping top11= new Topping(11,"うずら煮卵",200,300);
		Topping top12= new Topping(12,"燻製たまご",200,300);
		Topping top13= new Topping(13,"つみれ",200,300);
		Topping top14= new Topping(14,"ワンタン",200,300);
		Topping top15= new Topping(15,"ザーサイ",200,300);
		Topping top16= new Topping(16,"大トロチャーシュー",200,300);
		Topping top17= new Topping(17,"太麺に変更",200,300);
		Topping top18= new Topping(18,"追い飯",200,300);
		List<Topping> toppingList = new ArrayList<>(List.of(top1,top2,top3,top4,top5,top6,top7,top8,top9,top10,
													top11,top12,top13,top14,top15,top16,top17,top18));
		List<OrderTopping> orderTopping = new ArrayList<>();
		Item item = new Item(1, "とんこつラーメン", "創業当時から今に引き継ぐとんこつラーメンの本流であり、原点の味。18時間の調理と、丸1日の熟成を経て、とんこつの旨味を極限まで抽出した豊かで香り高いシルキーなスープに、博多らしい細麺がマッチします。"
				, 700, 800, "1.jpg",false, toppingList);
		List<OrderItem> orderItemList = new ArrayList<>();
		orderitem.setId(1);
		orderitem.setItem(item);
		orderitem.setItemId(1);
		orderitem.setOrderId(1);
		orderitem.setOrderToppingList(orderTopping);
		orderitem.setQuantity(1);
		orderitem.setSize('M');
		orderItemList.add(orderitem);
//		List<User> userList = new ArrayList<>(List.of());
		sessionMap.put("orderitemId", orderitem.getId());
		sessionMap.put("orderItemList", orderItemList);
//		sessionMap.put("userList", userList);
		return createMockHttpSession(sessionMap);
	}
	
	public static MockHttpSession createUserCartSession() {
		Map<String, Object> sessionMap = new LinkedHashMap<String, Object>();
		List<OrderItem> orderItemList = new ArrayList<>();
		List<User> userList = new ArrayList<>();
		List<OrderTopping> orderTopping = new ArrayList<>();
		
			Item item = new Item(1, "とんこつラーメン", "創業当時から今に引き継ぐとんこつラーメンの本流であり、原点の味。18時間の調理と、丸1日の熟成を経て、とんこつの旨味を極限まで抽出した豊かで香り高いシルキーなスープに、博多らしい細麺がマッチします。", 700, 800, "1.jpg",false, null);
		OrderItem orderitem = new OrderItem();
			orderitem.setId(1);
			orderitem.setItem(item);
			orderitem.setItemId(1);
			orderitem.setOrderId(1);
			orderitem.setOrderToppingList(orderTopping);
			orderitem.setQuantity(1);
			orderitem.setSize('M');
			orderItemList.add(orderitem);
		User user = new User();
			user.setId(1);
			user.setName("テストユーザ");
			user.setEmail("test@test.co.jp");
			user.setPassword("$2a$10$Utoo6nr3XIFEh4xOZ9Zr1.n/PtEYBb8HhlLDDklaJwsj.T3uux4kq");
			user.setZipcode("111-1111");
			user.setAddress("テスト住所");
			user.setTelephone("000-0000-0000");
			user.setPasswordConfirm("$2a$10$Utoo6nr3XIFEh4xOZ9Zr1.n/PtEYBb8HhlLDDklaJwsj.T3uux4kq");
			userList.add(user);
		sessionMap.put("orderitemId", orderitem.getId());
		sessionMap.put("user", userList);
		sessionMap.put("orderItemList", orderItemList);
		return createMockHttpSession(sessionMap);
	}
	
	public static MockHttpSession createCombineSession() {
		Map<String, Object> sessionMap = new LinkedHashMap<String, Object>();
		List<OrderItem> orderItemList = new ArrayList<>();
		List<User> userList = new ArrayList<>();
		List<OrderTopping> orderTopping = new ArrayList<>();
		
			Item item = new Item(1, "とんこつラーメン", "創業当時から今に引き継ぐとんこつラーメンの本流であり、原点の味。18時間の調理と、丸1日の熟成を経て、とんこつの旨味を極限まで抽出した豊かで香り高いシルキーなスープに、博多らしい細麺がマッチします。", 700, 800, "1.jpg",false, null);
		OrderItem orderitem = new OrderItem();
			orderitem.setId(2);
			orderitem.setItem(item);
			orderitem.setItemId(1);
			orderitem.setOrderId(2);
			orderitem.setOrderToppingList(orderTopping);
			orderitem.setQuantity(1);
			orderitem.setSize('M');
			orderItemList.add(orderitem);
		User user = new User();
			user.setId(1);
			user.setName("テストユーザ");
			user.setEmail("test@test.co.jp");
			user.setPassword("$2a$10$Utoo6nr3XIFEh4xOZ9Zr1.n/PtEYBb8HhlLDDklaJwsj.T3uux4kq");
			user.setZipcode("111-1111");
			user.setAddress("テスト住所");
			user.setTelephone("000-0000-0000");
			user.setPasswordConfirm("$2a$10$Utoo6nr3XIFEh4xOZ9Zr1.n/PtEYBb8HhlLDDklaJwsj.T3uux4kq");
			userList.add(user);
		sessionMap.put("orderitemId", orderitem.getId());
		sessionMap.put("user", userList);
		sessionMap.put("orderItemList", orderItemList);
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
