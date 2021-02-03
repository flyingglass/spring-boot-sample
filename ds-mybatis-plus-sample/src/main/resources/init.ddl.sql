-- MySQL
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `nickname` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志',
  `gmt_create` datetime NOT NULL COMMENT '记录创建时间',
  `gmt_updated` datetime NOT NULL COMMENT '记录更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- Phoenix
DROP TABLE IF EXISTS TEST_INFO;
CREATE TABLE TEST_INFO (
  id INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR,
  phone VARCHAR,
  position VARCHAR,
  department VARCHAR,
  company VARCHAR,
  file_name VARCHAR,
  pos_dep_com VARCHAR,
  gmt_updated DATE,
  gmt_create DATE
);
