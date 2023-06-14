/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : wuye_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2018-02-06 13:01:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL,
  `password` varchar(20) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `t_building`
-- ----------------------------
DROP TABLE IF EXISTS `t_building`;
CREATE TABLE `t_building` (
  `buildingId` int(11) NOT NULL auto_increment,
  `buildingName` varchar(20) default NULL,
  `memo` varchar(50) default NULL,
  PRIMARY KEY  (`buildingId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_building
-- ----------------------------
INSERT INTO `t_building` VALUES ('1', '南苑1号楼', '民用');
INSERT INTO `t_building` VALUES ('2', '南苑2号楼', '商用');

-- ----------------------------
-- Table structure for `t_employee`
-- ----------------------------
DROP TABLE IF EXISTS `t_employee`;
CREATE TABLE `t_employee` (
  `employeeNo` varchar(20) NOT NULL,
  `name` varchar(20) default NULL,
  `sex` varchar(4) default NULL,
  `positionName` varchar(20) default NULL,
  `telephone` varchar(20) default NULL,
  `address` varchar(60) default NULL,
  `employeeDesc` varchar(200) default NULL,
  PRIMARY KEY  (`employeeNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_employee
-- ----------------------------
INSERT INTO `t_employee` VALUES ('EM001', '李大锤', '男', '小区保安', '13594938343', '四川成都', '很负责');
INSERT INTO `t_employee` VALUES ('EM002', '李倩', '女', '清洁员', '13983908342', '四川成都红星路', '很复杂的阿姨');

-- ----------------------------
-- Table structure for `t_facility`
-- ----------------------------
DROP TABLE IF EXISTS `t_facility`;
CREATE TABLE `t_facility` (
  `facilityId` int(11) NOT NULL auto_increment,
  `name` varchar(20) default NULL,
  `count` int(11) default NULL,
  `startTime` varchar(20) default NULL,
  `facilityState` varchar(20) default NULL,
  PRIMARY KEY  (`facilityId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_facility
-- ----------------------------
INSERT INTO `t_facility` VALUES ('1', '小区休闲运动场', '1', '2017-12-07', '正常');
INSERT INTO `t_facility` VALUES ('2', '小区歌舞比赛场地', '1', '2017-12-06', '待维修');

-- ----------------------------
-- Table structure for `t_fee`
-- ----------------------------
DROP TABLE IF EXISTS `t_fee`;
CREATE TABLE `t_fee` (
  `feeId` int(11) NOT NULL auto_increment,
  `feeTypeObj` int(11) default NULL,
  `ownerObj` int(11) default NULL,
  `feeDate` varchar(20) default NULL,
  `feeMoney` float default NULL,
  `feeContent` varchar(50) default NULL,
  `opUser` varchar(20) default NULL,
  PRIMARY KEY  (`feeId`),
  KEY `FK68EE91B2A7194D5` (`feeTypeObj`),
  KEY `FK68EE91BEB662F55` (`ownerObj`),
  CONSTRAINT `FK68EE91B2A7194D5` FOREIGN KEY (`feeTypeObj`) REFERENCES `t_feetype` (`typeId`),
  CONSTRAINT `FK68EE91BEB662F55` FOREIGN KEY (`ownerObj`) REFERENCES `t_owner` (`ownerId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_fee
-- ----------------------------
INSERT INTO `t_fee` VALUES ('1', '1', '1', '2017-11-24', '300', '11月份物业费', 'user1');
INSERT INTO `t_fee` VALUES ('2', '3', '1', '2017-11-25', '152.5', '11月电费', 'user1');
INSERT INTO `t_fee` VALUES ('3', '2', '1', '2017-12-08', '80', '11月水费', 'user3');
INSERT INTO `t_fee` VALUES ('4', '3', '2', '2017-12-08', '108.5', '11月电费', 'user3');
INSERT INTO `t_fee` VALUES ('5', '2', '2', '2017-12-15', '69', '11月水费', 'user3');

-- ----------------------------
-- Table structure for `t_feetype`
-- ----------------------------
DROP TABLE IF EXISTS `t_feetype`;
CREATE TABLE `t_feetype` (
  `typeId` int(11) NOT NULL auto_increment,
  `typeName` varchar(20) default NULL,
  PRIMARY KEY  (`typeId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_feetype
-- ----------------------------
INSERT INTO `t_feetype` VALUES ('1', '物业费');
INSERT INTO `t_feetype` VALUES ('2', '水费');
INSERT INTO `t_feetype` VALUES ('3', '电费');

-- ----------------------------
-- Table structure for `t_leaveword`
-- ----------------------------
DROP TABLE IF EXISTS `t_leaveword`;
CREATE TABLE `t_leaveword` (
  `leaveWordId` int(11) NOT NULL auto_increment,
  `title` varchar(20) default NULL,
  `content` longtext,
  `addTime` varchar(20) default NULL,
  `ownerObj` int(11) default NULL,
  `replyContent` longtext,
  `replyTime` varchar(20) default NULL,
  `opUser` varchar(20) default NULL,
  PRIMARY KEY  (`leaveWordId`),
  KEY `FKF95A7036EB662F55` (`ownerObj`),
  CONSTRAINT `FKF95A7036EB662F55` FOREIGN KEY (`ownerObj`) REFERENCES `t_owner` (`ownerId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_leaveword
-- ----------------------------
INSERT INTO `t_leaveword` VALUES ('1', '天还没亮鸡叫扰民', '最近晚上快天亮的时候，小区有好几个鸡乱叫扰民！', '2017-11-23 15:16:58', '1', '叫人去宰杀了！', '2017-12-08 01:30:19', 'user2');
INSERT INTO `t_leaveword` VALUES ('2', '门口经常有垃圾', '我们这楼经常有人扔垃圾', '2017-11-24 21:32:11', '1', '处罚100元', '2017-12-15 22:14:37', 'user2');
INSERT INTO `t_leaveword` VALUES ('3', '不满意处理', '效率太低了，要投诉', '2017-12-15 22:21:08', '2', '我们尽快协商解决', '2017-12-15 22:21:52', 'user2');

-- ----------------------------
-- Table structure for `t_manager`
-- ----------------------------
DROP TABLE IF EXISTS `t_manager`;
CREATE TABLE `t_manager` (
  `manageUserName` varchar(20) NOT NULL,
  `password` varchar(20) default NULL,
  `manageType` varchar(20) default NULL,
  `name` varchar(20) default NULL,
  `sex` varchar(4) default NULL,
  `telephone` varchar(20) default NULL,
  PRIMARY KEY  (`manageUserName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_manager
-- ----------------------------
INSERT INTO `t_manager` VALUES ('user1', '123', '经营部', '王美玲', '女', '13598430843');
INSERT INTO `t_manager` VALUES ('user2', '123', '管理部', '汪曾祺', '男', '13083415631');
INSERT INTO `t_manager` VALUES ('user3', '123', '财务部', '李明霞', '女', '13598324324');

-- ----------------------------
-- Table structure for `t_owner`
-- ----------------------------
DROP TABLE IF EXISTS `t_owner`;
CREATE TABLE `t_owner` (
  `ownerId` int(11) NOT NULL auto_increment,
  `buildingObj` int(11) default NULL,
  `roomNo` varchar(20) default NULL,
  `ownerName` varchar(20) default NULL,
  `area` varchar(20) default NULL,
  `telephone` varchar(20) default NULL,
  `memo` longtext,
  `password` varchar(20) default NULL,
  PRIMARY KEY  (`ownerId`),
  KEY `FK9F003F087B6F2C79` (`buildingObj`),
  CONSTRAINT `FK9F003F087B6F2C79` FOREIGN KEY (`buildingObj`) REFERENCES `t_building` (`buildingId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_owner
-- ----------------------------
INSERT INTO `t_owner` VALUES ('1', '1', '108', '王铮亮', '105平米', '13939843134', '业主信息test', '123');
INSERT INTO `t_owner` VALUES ('2', '2', '107', '李文慧', '123', '13939843134', 'test', '123');

-- ----------------------------
-- Table structure for `t_parking`
-- ----------------------------
DROP TABLE IF EXISTS `t_parking`;
CREATE TABLE `t_parking` (
  `parkingId` int(11) NOT NULL auto_increment,
  `parkingName` varchar(20) default NULL,
  `plateNumber` varchar(20) default NULL,
  `ownerObj` int(11) default NULL,
  `opUser` varchar(20) default NULL,
  PRIMARY KEY  (`parkingId`),
  KEY `FKEF833ECDEB662F55` (`ownerObj`),
  CONSTRAINT `FKEF833ECDEB662F55` FOREIGN KEY (`ownerObj`) REFERENCES `t_owner` (`ownerId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_parking
-- ----------------------------
INSERT INTO `t_parking` VALUES ('1', '1号楼103', '川A-39842', '1', 'user1');
INSERT INTO `t_parking` VALUES ('2', '2号楼208', '川A-24934', '2', 'user1');

-- ----------------------------
-- Table structure for `t_repair`
-- ----------------------------
DROP TABLE IF EXISTS `t_repair`;
CREATE TABLE `t_repair` (
  `repairId` int(11) NOT NULL auto_increment,
  `ownerObj` int(11) default NULL,
  `repairDate` varchar(20) default NULL,
  `questionDesc` longtext,
  `repairState` varchar(20) default NULL,
  `handleResult` longtext,
  PRIMARY KEY  (`repairId`),
  KEY `FK45295DD8EB662F55` (`ownerObj`),
  CONSTRAINT `FK45295DD8EB662F55` FOREIGN KEY (`ownerObj`) REFERENCES `t_owner` (`ownerId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_repair
-- ----------------------------
INSERT INTO `t_repair` VALUES ('1', '1', '2017-11-24', '下水道漏水', '维修中', '技术正在搞。。');
INSERT INTO `t_repair` VALUES ('3', '2', '2017-12-15', '厕所堵了！！', '维修中', '技术正在赶来中。。。');

-- ----------------------------
-- Table structure for `t_salary`
-- ----------------------------
DROP TABLE IF EXISTS `t_salary`;
CREATE TABLE `t_salary` (
  `salaryId` int(11) NOT NULL auto_increment,
  `employeeObj` varchar(20) default NULL,
  `year` varchar(20) default NULL,
  `month` varchar(20) default NULL,
  `salaryMoney` float default NULL,
  `fafang` varchar(20) default NULL,
  PRIMARY KEY  (`salaryId`),
  KEY `FK46A40815A2CBF239` (`employeeObj`),
  CONSTRAINT `FK46A40815A2CBF239` FOREIGN KEY (`employeeObj`) REFERENCES `t_employee` (`employeeNo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_salary
-- ----------------------------
INSERT INTO `t_salary` VALUES ('1', 'EM001', '2017', '11', '3850', '是');
INSERT INTO `t_salary` VALUES ('2', 'EM001', '2017', '10', '3200', '是');
INSERT INTO `t_salary` VALUES ('3', 'EM002', '2017', '11', '3800', '是');
