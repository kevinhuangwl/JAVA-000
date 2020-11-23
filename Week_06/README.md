CREATE TABLE t_user(  
  id int NOT NULL primary key AUTO_INCREMENT comment 'primary key',
  created_time DATETIME COMMENT 'created time',
  updated_time DATETIME COMMENT 'updated time',
  username varchar(255) comment '用户名',
  pwd varchar(255) comment '密码',
  contact varchar(255) comment '联系电话'
) default charset utf8 comment '';

CREATE TABLE t_order(
  id int NOT NULL primary key AUTO_INCREMENT comment 'primary key',
  created_time DATETIME COMMENT 'created time',
  updated_time DATETIME COMMENT 'updated time',
  code varchar(255) comment '订单编号',
  state varchar(255) comment '订单状态',
  user_id varchar(255) comment '用户ID'
) default charset utf8 comment '';

CREATE TABLE t_order_booking_good(
  id int NOT NULL primary key AUTO_INCREMENT comment 'primary key',
  created_time DATETIME COMMENT 'created time',
  updated_time DATETIME COMMENT 'updated time',
  order_code VARCHAR(255) COMMENT '订单编号',
  good_code VARCHAR(255) COMMENT '商品编码'
) default charset utf8 comment '';

CREATE TABLE t_good(
  id int NOT NULL primary key AUTO_INCREMENT comment 'primary key',
  created_time DATETIME COMMENT 'created time',
  updated_time DATETIME COMMENT 'updated time',
  goodname varchar(255) comment '商品名',
  category varchar(255) comment '商品类别',
  price int comment '价格',
  code varchar(255) comment '商品编码',
  inventory int COMMENT '库存'
) default charset utf8 comment '';
