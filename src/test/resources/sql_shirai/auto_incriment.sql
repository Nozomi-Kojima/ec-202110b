--　オーダーアイテムの削除とシーケンスのリセット
delete from order_items;
select setval('order_items_id_seq',1,false);

delete from orders;
select setval('orders_id_seq',1,false);