create database littlenote;

USE littlenote;

--用户认证

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(100) NOT NULL,
  `nickname` varchar(100) DEFAULT NULL,
  `avatar_url` varchar(100) DEFAULT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
);

create table authorities (
  username varchar(50) not null,
  authority varchar(50) not null,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  key(username),
  UNIQUE KEY `ix_auth_username` (username,authority)
);

create table persistent_logins (username varchar(64) not null,
                                series varchar(64) primary key,
                                token varchar(64) not null,
                                last_used timestamp not null);

--业务实体

CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `type` varchar(20) NOT NULL COMMENT '账户类型',
  `name` varchar(20) NOT NULL COMMENT '账户名',
  `password` varchar(100) NOT NULL,
  `notes` varchar(100) NOT NULL COMMENT '备注',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
);






