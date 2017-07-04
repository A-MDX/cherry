/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.5.39 : Database - test
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`test` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `test`;

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login_name` char(10) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `salt` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `open_id` varchar(300) COLLATE utf8_bin DEFAULT NULL,
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `creator` int(11) DEFAULT NULL,
  `creation_time` datetime DEFAULT NULL,
  `modifier` int(11) DEFAULT NULL,
  `mofify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `sys_user` */

insert  into `sys_user`(`id`,`login_name`,`password`,`salt`,`name`,`open_id`,`remark`,`creator`,`creation_time`,`modifier`,`mofify_time`) values (1,'madx','9295','1234','马哈哈','oEYIP0wamw-fSnN103-JYGN5eHq8','啊',1,'2017-06-26 22:27:37',NULL,NULL);

/*Table structure for table `wechat_msg` */

DROP TABLE IF EXISTS `wechat_msg`;

CREATE TABLE `wechat_msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL COMMENT '数据类型，若为文本，则data为文本，其他则为id',
  `msg_type` char(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据的类型，文字表示',
  `data` varchar(1000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '根据type的不同会产生不同的数据，如果是文本，则会是文字，若是其他，则会传输的id',
  `time` datetime DEFAULT NULL,
  `user` char(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '来源',
  `status` int(11) DEFAULT NULL COMMENT '状态，一般为有效无效',
  `modifier` char(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `wechat_msg` */

insert  into `wechat_msg`(`id`,`type`,`msg_type`,`data`,`time`,`user`,`status`,`modifier`,`modify_time`) values (1,10041001,'text','哎呀妈呀','2017-06-26 22:39:54','madx',10031001,NULL,NULL),(2,10041001,'text','呵呵','2017-06-26 22:41:52','madx',10031001,NULL,NULL),(3,10041001,'text','hehe','2017-06-26 22:42:02','madx',10031001,NULL,NULL),(4,10041001,'text','哎','2017-06-29 00:09:18','madx',10031001,NULL,NULL),(5,10041005,'image','http://mmbiz.qpic.cn/mmbiz_jpg/IUWdxTWzmFOSteTFSnvprwyr5NmVTgJQtq9orJ59bknAXAQlgRYJ7dV2KoosicERjMhaWM2YZsqe3rFYUduTPeg/0','2017-06-29 00:09:46','madx',10031001,NULL,NULL),(6,10041005,'image','http://mmbiz.qpic.cn/mmbiz_jpg/IUWdxTWzmFMyDc8csncym3cZiaghXD8c5kfw3DiaVNR9mT6gicADyIWDnIcusMKDPEnaPBMDtduxAeiaU79ONrE79w/0','2017-06-29 23:50:54','madx',10031001,NULL,NULL),(7,10041005,'image','http://mmbiz.qpic.cn/mmbiz_jpg/IUWdxTWzmFMyDc8csncym3cZiaghXD8c5BEDNiaGo8pUDACju4f49NnnadhibHPau5CC6leX5fzBibUAZIJuso7bkg/0','2017-06-29 23:54:21','madx',10031001,NULL,NULL),(8,10041005,'image','http://mmbiz.qpic.cn/mmbiz_jpg/IUWdxTWzmFMyDc8csncym3cZiaghXD8c5u9aTibNzYz1uLKGGJjRdgQDI2ibgeVraK8unnyYp5wWQWge2Olsw4ywg/0','2017-06-30 00:42:32','madx',10031001,NULL,NULL),(9,10041005,'image','{ \"type\" : \"image\", \"url\" : \"http://mmbiz.qpic.cn/mmbiz_jpg/IUWdxTWzmFMibUiaP6biboj6KylYO30SuVpNPHiagiaLdAYKnsQAPUXVcEKcJsQtm44bFY2EjibiaZVZxujgsjvMGATUw/0\", \"mediaId\" : \"zyH-_azPk4xXP0PjK4esXGDANvITgMeoZqSKtDjRL2AVZ9plhxn6wxbYwPSxEeyp\" }','2017-07-01 12:22:26','madx',10031001,NULL,NULL),(10,10041005,'image','{ \"type\" : \"image\", \"url\" : \"http://mmbiz.qpic.cn/mmbiz_jpg/IUWdxTWzmFMibUiaP6biboj6KylYO30SuVpx22s5vGLHS16icxvvVY5QRLLNIQuib35FoNBWUfib1YLMGowBDMOkv8Og/0\", \"mediaId\" : \"mF0DMOfDyr-vb7q07jKAKEuRCBqkEezPoM4U9tpsbD53btuoZSIGvHEGo1syOSC4\" }','2017-07-01 12:48:50','madx',10031001,NULL,NULL),(11,10041005,'image','{ \"type\" : \"image\", \"url\" : \"http://mmbiz.qpic.cn/mmbiz_jpg/IUWdxTWzmFMibUiaP6biboj6KylYO30SuVpx22s5vGLHS16icxvvVY5QRLLNIQuib35FoNBWUfib1YLMGowBDMOkv8Og/0\", \"mediaId\" : \"NxBOhbGBL2QqltggNKVYe2McgRq5iyEc79-XFxIwt2_mNw-fOVUKCwxSR2T7ohwT\" }','2017-07-01 12:55:42','madx',10031001,NULL,NULL),(12,10041001,'text','大家好呀','2017-07-01 12:56:30','madx',10031001,NULL,NULL),(13,10041001,'text','我就是想说些什么','2017-07-01 12:56:37','madx',10031001,NULL,NULL),(14,10041001,'text','好烦躁呀','2017-07-01 12:56:41','madx',10031001,NULL,NULL),(15,10041001,'text','也不知道怎么办','2017-07-01 12:56:49','madx',10031001,NULL,NULL),(16,10041001,'text','哎呀妈呀','2017-07-01 12:56:54','madx',10031001,NULL,NULL),(17,10041001,'text','气死我了','2017-07-01 12:56:56','madx',10031001,NULL,NULL),(18,10041001,'text','我想大人了','2017-07-01 12:57:01','madx',10031001,NULL,NULL),(19,10041001,'text','呵呵哒','2017-07-01 12:57:07','madx',10031001,NULL,NULL),(20,10041001,'text','呵呵','2017-07-01 12:57:10','madx',10031001,NULL,NULL),(21,10041001,'text','切','2017-07-01 12:57:18','madx',10031001,NULL,NULL),(22,10041001,'text','好气人呀','2017-07-01 12:57:23','madx',10031001,NULL,NULL),(23,10041001,'text','但是10','2017-07-01 12:57:31','madx',10031001,NULL,NULL),(24,10041001,'text','烦死你了','2017-07-01 12:57:36','madx',10031001,NULL,NULL),(25,10041001,'text','哎呀妈呀','2017-07-01 12:57:41','madx',10031001,NULL,NULL),(26,10041001,'text','？','2017-07-01 13:14:07','madx',10031001,NULL,NULL),(27,10041001,'text','先建立在上海的时候呢估计','2017-07-01 13:14:34','madx',10031001,NULL,NULL),(28,10041001,'text','哈哈哈','2017-07-01 13:16:34','madx',10031001,NULL,NULL),(29,10041001,'text','哈哈哈','2017-07-01 13:16:34','madx',10031001,NULL,NULL),(30,10041001,'text','哈哈哈','2017-07-01 13:16:34','madx',10031001,NULL,NULL),(31,10041001,'text','来家里','2017-07-01 13:17:08','madx',10031001,NULL,NULL),(32,10041001,'text','来家里','2017-07-01 13:17:08','madx',10031001,NULL,NULL),(33,10041001,'text','哥哥的','2017-07-01 13:20:39','madx',10031001,NULL,NULL),(34,10041001,'text','呵呵','2017-07-01 13:23:54','madx',10031001,NULL,NULL),(35,10041001,'text','额\n我去\n哎呀\n想想看\n到期了\n哎呀\n呵呵哒\n…你这一天\n1234567989\n到期了\n我去','2017-07-01 13:28:43','madx',10031001,'madx','2017-07-01 13:29:40'),(36,10041001,'text','高人气','2017-07-01 13:30:01','madx',10031001,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
