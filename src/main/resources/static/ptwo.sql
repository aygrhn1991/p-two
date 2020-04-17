/*
 Navicat Premium Data Transfer

 Source Server         : local-mysql
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : 127.0.0.1:3306
 Source Schema         : ptwo

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 17/04/2020 15:13:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_event
-- ----------------------------
DROP TABLE IF EXISTS `t_event`;
CREATE TABLE `t_event`  (
  `organizer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `member` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `subscribe` int(1) NULL DEFAULT NULL,
  PRIMARY KEY (`organizer`, `member`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_event
-- ----------------------------
INSERT INTO `t_event` VALUES ('ojPllw5w_dZSM5fgKibW9arYFaSM', 'ojPllw7GnwQHz9pG1TFe5Y532ak4', 1);
INSERT INTO `t_event` VALUES ('ojPllw7GnwQHz9pG1TFe5Y532ak4', 'ojPllwxDJcZkdJJofjnsgnK1kfHI', 1);
INSERT INTO `t_event` VALUES ('ojPllw9wyfQh0dpqLFNR1Zla2ktA', 'ojPllw5w_dZSM5fgKibW9arYFaSM', 1);

-- ----------------------------
-- Table structure for t_platform
-- ----------------------------
DROP TABLE IF EXISTS `t_platform`;
CREATE TABLE `t_platform`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `originalid` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `appid` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_platform
-- ----------------------------
INSERT INTO `t_platform` VALUES (1, NULL, 'ccc', 'aaa', 'bbb');
INSERT INTO `t_platform` VALUES (2, NULL, 'ccc', 'aaa', 'bbb');
INSERT INTO `t_platform` VALUES (3, NULL, 'ccc', 'aaa', 'bbb');
INSERT INTO `t_platform` VALUES (4, NULL, 'ccc', 'aaa', 'bbb');
INSERT INTO `t_platform` VALUES (5, NULL, 'ccc', 'aaa', 'bbb');
INSERT INTO `t_platform` VALUES (6, NULL, 'ccc', 'aaa', 'bbb');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `w_openid1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `w_openid2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `w_nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `w_sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `w_province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `w_city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `w_country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `w_headimgurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `w_unionid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `t_time` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, 'o9QXJ1eEan_09JNOP7MoOLJqzyRk', 'oMlWBwllQxE3ZgilEdq7mYeLIl-k', '随便', '1', '黑龙江', '哈尔滨', '中国', 'http://thirdwx.qlogo.cn/mmopen/vi_32/fuoBpUP3aKbY410bReK4Queib2MeOGdaElZz7ZfynA1PoRWQb0QuIzZOblLBt0VF0ADibwdl425tsh6LE4FtvrbQ/132', 'ojPllw9wyfQh0dpqLFNR1Zla2ktA', 1586401715338);
INSERT INTO `t_user` VALUES (2, 'o9QXJ1UTi0Al6xMlKOY5qvIXPKwY', 'oMlWBwqI2s4vll4XXcHw0XgUipug', '陈玉锋', '1', '黑龙江', '哈尔滨', '中国', 'http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK3sk1klAITIYXdhiaFJHQBjdia4fYszQOs6GIrv1HW65ZERpNQfKLeicnbFwt2m78tfEt77yuF7wmEw/132', 'ojPllw5w_dZSM5fgKibW9arYFaSM', 1586477878867);
INSERT INTO `t_user` VALUES (13, 'o9QXJ1SG_iEU8sSKmYRvM7vhJ9WQ', 'oMlWBwrZyb7a5zfxYK7jAGwW7uzY', '张墨、🧸', '2', '黑龙江', '哈尔滨', '中国', 'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eqE7ZKNsGGJmA5u5q6KsXsLjoC8EdCU1XDtBKWyASfHaK04tSlwNw4tducuiauyxLvha7DOqBrRn2Q/132', 'ojPllw7GnwQHz9pG1TFe5Y532ak4', 1586478295374);
INSERT INTO `t_user` VALUES (14, 'o9QXJ1Vhf74g8ItSeWFcdXGGEaNw', 'oMlWBwlWMkqmA0LlL_WkjpH6B9nM', '孔璞🕶', '1', '黑龙江', '哈尔滨', '中国', 'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epXfldxN50Vnib66wic5lGia1WzEpDFyG4717JL6iakVicUuAGpLspyxb9mrWa2Z6DwY9kGbwN25XXGpEA/132', 'ojPllwxDJcZkdJJofjnsgnK1kfHI', 1586478689029);

-- ----------------------------
-- Table structure for ticket
-- ----------------------------
DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket`  (
  `appid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `createtime` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `infotype` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `component_verify_ticket` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `token` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pre_auth_code` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ticket
-- ----------------------------
INSERT INTO `ticket` VALUES ('wxf5d8d7427ebabd90', '2020-04-17 14:56:58', 'component_verify_ticket', 'ticket@@@xI4zg2BRy_O3dvAVOC-AIQc4nTdx2wQqtHAZMDeuYC4AcUfF-hLbrI4fMmqy4_BwVVurEWUaR44K8lhqUSV_hw', '32_MjQqV8-o9eWJUgjUtp9Dawxj2MYt09WKrJqpnEb-w2H8MvVaeF9lBEzA1Tfp1qyBsajY7dQ7cpYHhN7YS5rINmvQm0BS_pSXavpoF4TmMrjFP6fBsNNdZ8bbYFF2a2zcv6_r4SAu2b-oQtRWEOYcAHAUEO', 'preauthcode@@@UpvlhNVagrx-kT6mJB3MbfifazV3UixKHreCQZGcp6y3399rWHERWr4iDzdOhsQd');
INSERT INTO `ticket` VALUES ('wxf5d8d7427ebabd90', '2020-04-17 14:56:59', 'component_verify_ticket', 'ticket@@@xI4zg2BRy_O3dvAVOC-AIQc4nTdx2wQqtHAZMDeuYC4AcUfF-hLbrI4fMmqy4_BwVVurEWUaR44K8lhqUSV_hw', NULL, NULL);
INSERT INTO `ticket` VALUES ('wxf5d8d7427ebabd90', '2020-04-17 14:56:59', 'component_verify_ticket', 'ticket@@@xI4zg2BRy_O3dvAVOC-AIQc4nTdx2wQqtHAZMDeuYC4AcUfF-hLbrI4fMmqy4_BwVVurEWUaR44K8lhqUSV_hw', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
