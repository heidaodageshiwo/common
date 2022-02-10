/*
Navicat MySQL Data Transfer

Source Server         : 张强本机mysql
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : zq

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2022-02-10 17:34:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` varchar(100) NOT NULL COMMENT '主键ID',
  `name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `pid` varchar(100) DEFAULT NULL COMMENT '年龄',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('000', '根菜单', '0000');
INSERT INTO `menu` VALUES ('1', '菜单1', '000');
INSERT INTO `menu` VALUES ('2', '菜单2', '000');
INSERT INTO `menu` VALUES ('3', '菜单3', '000');
INSERT INTO `menu` VALUES ('4', '菜单4', '000');
INSERT INTO `menu` VALUES ('5', '菜单11', '1');
INSERT INTO `menu` VALUES ('6', '菜单12', '1');
INSERT INTO `menu` VALUES ('7', '菜单111', '5');
