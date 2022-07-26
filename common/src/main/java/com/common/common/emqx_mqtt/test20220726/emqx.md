```powershell
[root@iZuf6ftuvpyd3qltklyo91Z ~]# docker pull emqx/emqx:4.3.12
4.3.12: Pulling from emqx/emqx
8572bc8fb8a3: Pull complete 
8e63e810b891: Pull complete 
51a19d47e52e: Pull complete 
fa330d65a158: Pull complete 
7e4240272cbc: Pull complete 
28d4012981b5: Pull complete 
f0052a46056f: Pull complete 
Digest: sha256:aa8648228cebc4a15941dda4f48a825b67289b08adf99861db4e33e82c5ec0d8
Status: Downloaded newer image for emqx/emqx:4.3.12
docker.io/emqx/emqx:4.3.12
[root@iZuf6ftuvpyd3qltklyo91Z ~]# docker run -d --name emqx -p 18083:18083 -p 1883:1883 emqx/emqx:4.3.12
16997c484651b171708b1f8ebf1f168de2a62735dedd6bb49a6d787c410f1a6e
[root@iZuf6ftuvpyd3qltklyo91Z ~]# docker cp emqx:/opt/emqx/data /opt/emqx/data
[root@iZuf6ftuvpyd3qltklyo91Z ~]# rm -rf /opt/emqx/
[root@iZuf6ftuvpyd3qltklyo91Z ~]# mkdir -p /opt/emqx
[root@iZuf6ftuvpyd3qltklyo91Z ~]# ls
[root@iZuf6ftuvpyd3qltklyo91Z ~]# docker cp emqx:/opt/emqx/data /opt/emqx/data
[root@iZuf6ftuvpyd3qltklyo91Z ~]# docker cp emqx:/opt/emqx/etc /opt/emqx/etc
[root@iZuf6ftuvpyd3qltklyo91Z ~]# docker cp emqx:/opt/emqx/log /opt/emqx/log
[root@iZuf6ftuvpyd3qltklyo91Z ~]# 





emqx_auth_mysql.conf

##--------------------------------------------------------------------
## MySQL Auth/ACL Plugin
##--------------------------------------------------------------------

## MySQL server address.
##
## Value: Port | IP:Port
##
## Examples: 3306, 127.0.0.1:3306, localhost:3306
auth.mysql.server = 152.136.98.72:3306

## MySQL pool size.
##
## Value: Number
auth.mysql.pool = 8

## MySQL username.
##
## Value: String
auth.mysql.username =root

## MySQL password.
##
## Value: String
auth.mysql.password =root

## MySQL database.
##
## Value: String
auth.mysql.database = test1

## MySQL query timeout
##
## Value: Duration
## auth.mysql.query_timeout = 5s

## Variables: %u = username, %c = clientid

## Authentication query.
##
## Note that column names should be 'password' and 'salt' (if used).
## In case column names differ in your DB - please use aliases,
## e.g. "my_column_name as password".
##
## Value: SQL
##
## Variables:
##  - %u: username
##  - %c: clientid
##  - %C: common name of client TLS cert
##  - %d: subject of client TLS cert
##
auth.mysql.auth_query = select password from mqtt_user where username = '%u' and clientid='%c'  limit 1
## auth.mysql.auth_query = select password_hash as password from mqtt_user where username = '%u' limit 1

## Password hash.
##
## Value: plain | md5 | sha | sha256 | bcrypt
auth.mysql.password_hash = md5

## sha256 with salt prefix
## auth.mysql.password_hash = salt,sha256

## bcrypt with salt only prefix
## auth.mysql.password_hash = salt,bcrypt

## sha256 with salt suffix
## auth.mysql.password_hash = sha256,salt

## pbkdf2 with macfun iterations dklen
## macfun: md4, md5, ripemd160, sha, sha224, sha256, sha384, sha512
## auth.mysql.password_hash = pbkdf2,sha256,1000,20

## Superuser query.
##
## Value: SQL
##
## Variables:
##  - %u: username
##  - %c: clientid
##  - %C: common name of client TLS cert
##  - %d: subject of client TLS cert
##
auth.mysql.super_query = select is_superuser from mqtt_user where username = '%u' limit 1

## ACL query.
##
## Value: SQL
##
## Variables:
##  - %a: ipaddr
##  - %u: username
##  - %c: clientid
##
## Note: You can add the 'ORDER BY' statement to control the rules match order
auth.mysql.acl_query = select allow, ipaddr, username, clientid, access, topic from mqtt_acl where ipaddr = '%a' or username = '%u' or username = '$all' or clientid = '%c'

## Mysql ssl configuration.
##
## Value: on | off
#auth.mysql.ssl = off

## CA certificate.
##
## Value: File
#auth.mysql.ssl.cacertfile  = /path/to/ca.pem

## Client ssl certificate.
##
## Value: File
#auth.mysql.ssl.certfile = /path/to/your/clientcert.pem

## Client ssl keyfile.
##
## Value: File
#auth.mysql.ssl.keyfile = /path/to/your/clientkey.pem

## In mode verify_none the default behavior is to allow all x509-path
## validation errors.
##
## Value: true | false
#auth.mysql.ssl.verify = false

## If not specified, the server's names returned in server's certificate is validated against
## what's provided `auth.mysql.server` config's host part.
## Setting to 'disable' will make EMQ X ignore unmatched server names.
## If set with a host name, the server's names returned in server's certificate is validated
## against this value.
##
## Value: String | disable
## auth.mysql.ssl.server_name_indication = disable





acl.conf

{allow, {user, "dashboard"}, subscribe, ["$SYS/#"]}.

{allow, {ipaddr, "127.0.0.1"}, pubsub, ["$SYS/#", "#"]}.

{deny, all, subscribe, ["$SYS/#", {eq, "#"}]}.

%% {allow, all}.


emqx.conf

allow_anonymous = false


[root@iZuf6ftuvpyd3qltklyo91Z ~]# docker rm -f emqx

还需要为 data  etc  log  赋予777权限 
chmod 777 /opt/emqx/


docker run -d --name emqx \
-v  /opt/emqx/data:/opt/emqx/data \
-v  /opt/emqx/etc:/opt/emqx/etc \
-v  /opt/emqx/log:/opt/emqx/log \
-p 18083:18083 -p 1883:1883 emqx/emqx:4.3.12



INSERT INTO `test1`.`mqtt_acl`(`id`, `allow`, `ipaddr`, `username`, `clientid`, `access`, `topic`) VALUES (1, 0, NULL, '$all', NULL, 1, '$SYS/#');
INSERT INTO `test1`.`mqtt_acl`(`id`, `allow`, `ipaddr`, `username`, `clientid`, `access`, `topic`) VALUES (2, 1, '192.168.2.145', NULL, NULL, 1, '#');
INSERT INTO `test1`.`mqtt_acl`(`id`, `allow`, `ipaddr`, `username`, `clientid`, `access`, `topic`) VALUES (3, 1, NULL, '$all', NULL, 3, '/smarthome/+/temperature');
INSERT INTO `test1`.`mqtt_acl`(`id`, `allow`, `ipaddr`, `username`, `clientid`, `access`, `topic`) VALUES (4, 1, NULL, '$all', NULL, 1, '/smarthome/%c/temperature');
INSERT INTO `test1`.`mqtt_acl`(`id`, `allow`, `ipaddr`, `username`, `clientid`, `access`, `topic`) VALUES (5, 1, '', 'zhangqiang', 'zhangqiang', 3, '#');


```

```sql

/*
 Navicat Premium Data Transfer

 Source Server         : 152.136.98.72
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : 152.136.98.72:3306
 Source Schema         : test1

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 26/07/2022 10:05:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mqtt_acl
-- ----------------------------
DROP TABLE IF EXISTS `mqtt_acl`;
CREATE TABLE `mqtt_acl`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `allow` int(1) DEFAULT 1 COMMENT '0: deny, 1: allow',
  `ipaddr` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'IpAddress',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Username',
  `clientid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'ClientId',
  `access` int(2) NOT NULL COMMENT '1: subscribe, 2: publish, 3: pubsub',
  `topic` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'Topic Filter',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ipaddr`(`ipaddr`) USING BTREE,
  INDEX `username`(`username`) USING BTREE,
  INDEX `clientid`(`clientid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mqtt_acl
-- ----------------------------
INSERT INTO `mqtt_acl` VALUES (6, 0, NULL, '$all', NULL, 1, '$SYS/#');

-- ----------------------------
-- Table structure for mqtt_user
-- ----------------------------
DROP TABLE IF EXISTS `mqtt_user`;
CREATE TABLE `mqtt_user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `salt` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_superuser` tinyint(1) DEFAULT 0,
  `created` datetime(0) DEFAULT NULL,
  `clientid` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'ClientId',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `mqtt_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mqtt_user
-- ----------------------------
INSERT INTO `mqtt_user` VALUES (1, 'shen', 'e10adc3949ba59abbe56e057f20f883e', '', 0, NULL, NULL);
INSERT INTO `mqtt_user` VALUES (2, 'li', 'e10adc3949ba59abbe56e057f20f883e', '', 0, NULL, NULL);
INSERT INTO `mqtt_user` VALUES (3, 'zhangqiang1', 'e10adc3949ba59abbe56e057f20f883e', '', 1, NULL, 'e10adc3949ba59abbe56e057f20f883e');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '1', 1, '1');
INSERT INTO `user` VALUES (3156, '444', NULL, NULL);
INSERT INTO `user` VALUES (3370, '323333', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;

```





```powershell
 curl -sSL https://get.daocloud.io/docker | sh
systemctl start docker && systemctl enable docker





```

```powershell
http://192.168.56.211:18083/  admin public   界面访问地址
  //订阅地址  tcp://192.168.56.211:1883
 [root@master ~]# docker pull emqx/emqx
  Using default tag: latest
  latest: Pulling from emqx/emqx
  e519532ddf75: Pull complete
615f43685b81: Pull complete
414a2d1abdea: Pull complete
  db03ba517cb5: Pull complete
  ed62ea000403: Pull complete
  cd1ed20ea0c6: Pull complete
0b5d6c612afb: Pull complete
  Digest: sha256:0cc667c503c9faa57b6f364e6f0ee3df91d3bf42c376aa1507334876d50be6cd
  Status: Downloaded newer image for emqx/emqx:latest
  docker.io/emqx/emqx:latest
[root@master ~]# docker images
  REPOSITORY                                                        TAG           IMAGE ID       CREATED         SIZE
  emqx/emqx                                                         latest        d9427f0f475e   11 days ago     154MB
[root@master ~]# docker run -d --name emqx -p 18083:18083 -p 1883:1883 emqx/emqx
4f21cd70f8b5948b85e80a4489dbc857ecff4ce0b016a1698219d8c945c6cd4d
[root@master ~]# docker ps
  CONTAINER ID   IMAGE       COMMAND                  CREATED         STATUS         PORTS                                                                                                                                                                            NAMES
4f21cd70f8b5   emqx/emqx   "/usr/bin/docker-ent…"   5 seconds ago   Up 3 seconds   4369-4370/tcp, 5369/tcp, 6369-6370/tcp, 8081/tcp, 8083-8084/tcp, 8883/tcp, 0.0.0.0:1883->1883/tcp, :::1883->1883/tcp, 0.0.0.0:18083->18083/tcp, :::18083->18083/tcp, 11883/tcp   emqx
[root@master ~]#

```



博客地址：

```pw
https://blog.csdn.net/weixin_50005386/article/details/123717623
官网： windows版本下载去官网下载
https://www.emqx.io/docs/zh/v4.3/


```



```sql

/*
 Navicat Premium Data Transfer

 Source Server         : 152.136.98.72
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : 152.136.98.72:3306
 Source Schema         : test1

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 25/07/2022 08:51:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mqtt_acl
-- ----------------------------
DROP TABLE IF EXISTS `mqtt_acl`;
CREATE TABLE `mqtt_acl`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `allow` int(1) DEFAULT 1 COMMENT '0: deny, 1: allow',
  `ipaddr` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'IpAddress',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Username',
  `clientid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'ClientId',
  `access` int(2) NOT NULL COMMENT '1: subscribe, 2: publish, 3: pubsub',
  `topic` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'Topic Filter',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ipaddr`(`ipaddr`) USING BTREE,
  INDEX `username`(`username`) USING BTREE,
  INDEX `clientid`(`clientid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mqtt_acl
-- ----------------------------
INSERT INTO `mqtt_acl` VALUES (1, 0, NULL, '$all', NULL, 1, '$SYS/#');
INSERT INTO `mqtt_acl` VALUES (2, 1, '192.168.2.145', NULL, NULL, 1, '#');
INSERT INTO `mqtt_acl` VALUES (3, 0, NULL, '$all', NULL, 1, '/smarthome/+/temperature');
INSERT INTO `mqtt_acl` VALUES (4, 1, NULL, '$all', NULL, 1, '/smarthome/%c/temperature');

-- ----------------------------
-- Table structure for mqtt_user
-- ----------------------------
DROP TABLE IF EXISTS `mqtt_user`;
CREATE TABLE `mqtt_user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `salt` varchar(35) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_superuser` tinyint(1) DEFAULT 0,
  `created` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `mqtt_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mqtt_user
-- ----------------------------
INSERT INTO `mqtt_user` VALUES (1, 'shen', 'e10adc3949ba59abbe56e057f20f883e', '', 0, NULL);
INSERT INTO `mqtt_user` VALUES (2, 'li', 'e10adc3949ba59abbe56e057f20f883e', '', 0, NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '1', 1, '1');
INSERT INTO `user` VALUES (3156, '444', NULL, NULL);
INSERT INTO `user` VALUES (3370, '323333', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;

```





EMQX 启用MySQL ACL 权限控制功能
一、认证
身份认证是大多数应用的重要组成部分，MQTT 协议支持用户名密码认证，启用身份认证能有效阻止非法客户端的连接。

EMQX 中的认证指的是当一个客户端连接到 EMQX 的时候，通过服务器端的配置来控制客户端连接服务器的权限。

EMQX 的认证支持包括两个层面：

MQTT 协议本身在 CONNECT 报文中指定用户名和密码，EMQX 以插件形式支持基于 Username、ClientID、HTTP、JWT、LDAP 及各类数据库如 MongoDB、MySQL、PostgreSQL、Redis 等多种形式的认证。
在传输层上，TLS 可以保证使用客户端证书的客户端到服务器的身份验证，并确保服务器向客户端验证服务器证书。也支持基于 PSK 的 TLS/DTLS 认证。（抄自官网文档）
一、修改EMQX的服务的配置文件
 1、EMQ X 默认配置中启用了匿名认证，任何客户端都能接入 EMQX。没有启用认证插件或认证插件没有显式允许/拒绝（ignore）连接请求时，EMQX 将根据匿名认证启用情况决定是否允许客户端连接。

配置匿名认证开关,emqx的配置文件路劲,(userpath)/etc/emqx.conf

# etc/emqx.conf
## Value: true | false
allow_anonymous = true 
# ------------------------------------------------------------------------------------------
## 警告
#	生产环境中请禁用匿名认证
# 即设计如下:
allow_anonymous = false
1
2
3
4
5
6
7
8
 2、把EMQX最基本的ACL配置文件设置一下，（userpath)/etc/acl.conf


{allow, {user, "dashboard"}, subscribe, ["$SYS/#"]}.
{allow, {ipaddr, "127.0.0.1"}, pubsub, ["$SYS/#", "#"]}.
{deny, all, subscribe, ["$SYS/#", {eq, "#"}]}.
# 把这行注释掉
%% {allow, all}.
1
2
3
4
5
6
 3、修改MySQL ACL 插件的配置文件

 MySQL 基础连接信息，需要保证集群内所有节点均能访问。配置文件路劲：(userpath)/etc/plugins/emqx_auth_mysql.conf

# etc/plugins/emqx_auth_mysql.conf

## 服务器地址
auth.mysql.server = 192.168.2.199:3306 # 数据库服务器地址和端口，我使用另外一台数据库服务器

## 连接池大小
auth.mysql.pool = 8
auth.mysql.username = root #数据库连接用户名
auth.mysql.password = public #数据库连接密码
auth.mysql.database = iot_emqx # 数据库名字
auth.mysql.query_timeout = 5s
auth.mysql.password_hash = plain #数据库中的明明加密方式使用明码方式，即不加密
1
2
3
4
5
6
7
8
9
10
11
12
 4、MySQL 认证插件默认配置下需要确保数据库中有以下两张数据表，用于存储认证规则信息：

1）认证/超级用户表mqtt_user:
CREATE TABLE `mqtt_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `salt` varchar(35) DEFAULT NULL,
  `is_superuser` tinyint(1) DEFAULT 0,
  `created` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mqtt_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
1
2
3
4
5
6
7
8
9
10
2）给mqtt_user表中插入数据例子：
insert into mqtt_user (username, password, is_superuser, salt) values ('shen', '123456', 0, 'salt');
1
3）ACL 规则表：mqtt_acl，用于权限控制
CREATE TABLE `mqtt_acl` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `allow` int(1) DEFAULT 1 COMMENT '0: deny, 1: allow',
  `ipaddr` varchar(60) DEFAULT NULL COMMENT 'IpAddress',
  `username` varchar(100) DEFAULT NULL COMMENT 'Username',
  `clientid` varchar(100) DEFAULT NULL COMMENT 'ClientId',
  `access` int(2) NOT NULL COMMENT '1: subscribe, 2: publish, 3: pubsub',
  `topic` varchar(100) NOT NULL DEFAULT '' COMMENT 'Topic Filter',
  PRIMARY KEY (`id`),
  INDEX (ipaddr),
  INDEX (username),
  INDEX (clientid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

1
2
3
4
5
6
7
8
9
10
11
12
13
14
 规则表字段说明：

allow：禁止（0），允许（1）

ipaddr：设置 IP 地址

username：连接客户端的用户名，此处的值如果设置为 $all 表示该规则适用于所有的用户

clientid：连接客户端的 Client ID

access：允许的操作：订阅（1），发布（2），订阅发布都可以（3）

topic：控制的主题，可以使用通配符，并且可以在主题中加入占位符来匹配客户端信息，例t/%c则在匹配时主题将会替换为当前客户端的 Client ID

%u：用户名
%c：Client ID
————————————————
版权声明：本文为CSDN博主「seeshem」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/weixin_50005386/article/details/123717623





4）默认配置下示例数据：
-- 所有用户不可以订阅系统主题
INSERT INTO mqtt_acl (allow, ipaddr, username, clientid, access, topic) VALUES (0, NULL, '$all', NULL, 1, '$SYS/#');

-- 允许 192.168.2.145 上的客户端订阅所有的主题
INSERT INTO mqtt_acl (allow, ipaddr, username, clientid, access, topic) VALUES (1, '192.168.2.145', NULL, NULL, 1, '#');

-- 禁止客户端订阅 /smarthome/+/temperature 主题
INSERT INTO mqtt_acl (allow, ipaddr, username, clientid, access, topic) VALUES (0, NULL, '$all', NULL, 1, '/smarthome/+/temperature');

-- 允许客户端订阅包含自身 Client ID 的 /smarthome/${clientid}/temperature 主题
INSERT INTO mqtt_acl (allow, ipaddr, username, clientid, access, topic) VALUES (1, NULL, '$all', NULL, 1, '/smarthome/%c/temperature');
1
2
3
4
5
6
7
8
9
10
11
启用 MySQL ACL 后并以用户名 shen成功连接后，客户端应当数据具有相应的主题权限。
二、测试验证
1、启动emqx：
————————————————
版权声明：本文为CSDN博主「seeshem」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/weixin_50005386/article/details/123717623



### 启动emqx：

```
./bin/emqx start
1
```

![img](https://img-blog.csdnimg.cn/fa9b3c3f05e84c73883e4f72fbbe61c3.png#pic_center)
2、启动MySQL ACL 插件![在这里插入图片描述](https://img-blog.csdnimg.cn/b08a482753b04c949c040edf6c769447.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA54y_5Y-I5ZyG,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center)

### 注意！如果配置数据库或者其他信息有错误，会报错的。

3、测试3、测试

按照上面的配置步骤，把相关的数据和用户写入到相对应的mysql表中。
![在这里插入图片描述](https://img-blog.csdnimg.cn/342b1b412c934cd599b4d7af283cf4d1.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA54y_5Y-I5ZyG,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center)

test1:没有填写用户和密码，出现没有授权：

![在这里插入图片描述](https://img-blog.csdnimg.cn/12c91bab3d0f4b32ac8544c9ba900752.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA54y_5Y-I5ZyG,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center)
test2:填写正确的用户和密码，就可以连上了：
![在这里插入图片描述](https://img-blog.csdnimg.cn/d234a7016e4c4914a707caffa0d096e2.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBA54y_5Y-I5ZyG,size_20,color_FFFFFF,t_70,g_se,x_16#pic_center)

test3:使用不是数据库的里面的用户和密码:



































