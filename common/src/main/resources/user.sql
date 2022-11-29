/*
 Navicat Premium Data Transfer

 Source Server         : 张强阿里云mysql服务器
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 47.102.156.253:31306
 Source Schema         : zq

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 15/11/2022 17:27:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (6632, '张强1', NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (44123, 'aa', NULL, NULL, 'aa', '7B4ACF357EDE93898620D1767E7B01F2');
INSERT INTO `user` VALUES (86525, 'bb', NULL, NULL, 'bb1', '435F491B62B97DBF3EFC8ACD29CA769E');

SET FOREIGN_KEY_CHECKS = 1;
