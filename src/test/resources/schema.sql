--ユーザー
drop table if exists users cascade;

create table users (
id serial primary key
, name varchar(100) not null
, email varchar(100) not null unique
, password text not null
, zipcode varchar(8) not null
, address varchar(200) not null
, telephone varchar(15) not null
) ;


-- 商品
drop table if exists items cascade;

create table items (
id integer primary key
, name text not null
, description text not null
, price_m integer not null
, price_l integer not null
, image_path text not null
, deleted boolean default false not null
) ;

-- トッピング
drop table if exists toppings cascade;

create table toppings (
id integer primary key
, name text not null
, price_m integer not null
, price_l integer not null
) ;


-- 注文
drop table if exists orders cascade;

create table orders (
id serial primary key
, user_id integer not null
, status integer not null
, total_price integer not null
, order_date date
, destination_name varchar(100)
, destination_email varchar(100)
, destination_zipcode varchar(8)
, destination_address varchar(200)
, destination_tel varchar(15)
, delivery_time timestamp
, payment_method integer
) ;

-- 注文商品
drop table if exists order_items cascade;

create table order_items (
id serial primary key
, item_id integer not null
, order_id integer not null
, quantity integer not null
, size varchar(1)
) ;

-- 注文トッピング
drop table if exists order_toppings cascade;

create table order_toppings (
id serial primary key
, topping_id integer not null
, order_item_id integer not null
) ;

--パスワードリセットテーブル
drop table if exists password_resets cascade;

CREATE TABLE password_resets(
id serial primary key,
user_email varchar(100) NOT NULL,
unique_url varchar(128) NOT NULL UNIQUE
);