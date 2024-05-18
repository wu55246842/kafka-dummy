/*
Navicat MySQL Data Transfer

Source Server         : 165.22.62.0
Source Server Version : 50741
Source Host           : 165.22.62.0:36693
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50741
File Encoding         : 65001

Date: 2024-05-18 08:22:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `seq` bigint(20) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `produce_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=96987 DEFAULT CHARSET=utf8;
