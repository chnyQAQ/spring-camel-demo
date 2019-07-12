/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1(mysql)
Source Server Version : 50726
Source Host           : 127.0.0.1:3306
Source Database       : spring-security-db

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2019-07-12 14:13:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('14788fe1-3587-4195-8566-cd8aa894d17a', 'admin1');
INSERT INTO `role` VALUES ('5ea5c1d3-e57d-4ef2-b511-c4a31d77699a', 'root1');
INSERT INTO `role` VALUES ('613b48a6-4d51-42f8-8e80-73400961bc71', 'root');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('14788fe1-3587-4195-8566-cd8aa894d17a', 'admin', '{bcrypt}$2a$10$q8CZYHgxkNu4nwp94ddSQueoi0POscOVdZNE2q8bt.NPqWvzig3yu');
INSERT INTO `user` VALUES ('5ea5c1d3-e57d-4ef2-b511-c4a31d77699a', 'root1', '{bcrypt}$2a$10$q8CZYHgxkNu4nwp94ddSQueoi0POscOVdZNE2q8bt.NPqWvzig3yu');
INSERT INTO `user` VALUES ('613b48a6-4d51-42f8-8e80-73400961bc71', 'root', '{bcrypt}$2a$10$q8CZYHgxkNu4nwp94ddSQueoi0POscOVdZNE2q8bt.NPqWvzig3yu');
SET FOREIGN_KEY_CHECKS=1;
