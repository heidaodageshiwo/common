/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : PostgreSQL
 Source Server Version : 100016
 Source Host           : localhost:5432
 Source Catalog        : shuangtongdao
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 100016
 File Encoding         : 65001

 Date: 19/04/2021 19:45:57
*/


-- ----------------------------
-- Table structure for school
-- ----------------------------
DROP TABLE IF EXISTS "public"."school";
CREATE TABLE "public"."school" (
  "id" int8 NOT NULL,
  "school_name" varchar(20) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of school
-- ----------------------------
INSERT INTO "public"."school" VALUES (1, '北大');
INSERT INTO "public"."school" VALUES (2, '清华');
INSERT INTO "public"."school" VALUES (3, '复旦');
INSERT INTO "public"."school" VALUES (4, '交大');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS "public"."student";
CREATE TABLE "public"."student" (
  "id" int8 NOT NULL,
  "student_name" varchar(20) COLLATE "pg_catalog"."default",
  "school_id" int8
)
;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO "public"."student" VALUES (1, '张三', 1);
INSERT INTO "public"."student" VALUES (2, '李四', 3);

-- ----------------------------
-- Table structure for student_subject
-- ----------------------------
DROP TABLE IF EXISTS "public"."student_subject";
CREATE TABLE "public"."student_subject" (
  "student_id" int8 NOT NULL,
  "subject_id" int8 NOT NULL
)
;

-- ----------------------------
-- Records of student_subject
-- ----------------------------
INSERT INTO "public"."student_subject" VALUES (1, 1);
INSERT INTO "public"."student_subject" VALUES (1, 3);
INSERT INTO "public"."student_subject" VALUES (1, 5);
INSERT INTO "public"."student_subject" VALUES (2, 1);
INSERT INTO "public"."student_subject" VALUES (2, 2);
INSERT INTO "public"."student_subject" VALUES (2, 6);

-- ----------------------------
-- Table structure for subject
-- ----------------------------
DROP TABLE IF EXISTS "public"."subject";
CREATE TABLE "public"."subject" (
  "id" int8 NOT NULL,
  "subject_name" varchar(20) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Records of subject
-- ----------------------------
INSERT INTO "public"."subject" VALUES (1, '语文');
INSERT INTO "public"."subject" VALUES (2, '数学');
INSERT INTO "public"."subject" VALUES (3, '英语');
INSERT INTO "public"."subject" VALUES (4, '物理');
INSERT INTO "public"."subject" VALUES (5, '化学');
INSERT INTO "public"."subject" VALUES (6, '生物');

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS "public"."test";
CREATE TABLE "public"."test" (
  "id" int8 NOT NULL,
  "name" varchar COLLATE "pg_catalog"."default",
  "age" int2,
  "oter" json
)
;
COMMENT ON COLUMN "public"."test"."id" IS '主键id';
COMMENT ON COLUMN "public"."test"."name" IS '姓名';
COMMENT ON COLUMN "public"."test"."age" IS '年龄';
COMMENT ON COLUMN "public"."test"."oter" IS '其他';

-- ----------------------------
-- Records of test
-- ----------------------------
INSERT INTO "public"."test" VALUES (1, 'lisi', 233, '{"id":1111111111111,"name":"张三"}');
INSERT INTO "public"."test" VALUES (4, 'lisi', 233, '{"id":1111111111111,"name":"张三"}');
INSERT INTO "public"."test" VALUES (5, 'lisi', 233, '[[1,4,4],[3,4]]');
INSERT INTO "public"."test" VALUES (2, 'lisi', 233, '{"name":"张三11212","id":1111111111111}');

-- ----------------------------
-- Primary Key structure for table school
-- ----------------------------
ALTER TABLE "public"."school" ADD CONSTRAINT "school_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table student
-- ----------------------------
ALTER TABLE "public"."student" ADD CONSTRAINT "student_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table student_subject
-- ----------------------------
ALTER TABLE "public"."student_subject" ADD CONSTRAINT "student_subject_pkey" PRIMARY KEY ("student_id", "subject_id");

-- ----------------------------
-- Primary Key structure for table subject
-- ----------------------------
ALTER TABLE "public"."subject" ADD CONSTRAINT "subject_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table test
-- ----------------------------
ALTER TABLE "public"."test" ADD CONSTRAINT "test_pkey" PRIMARY KEY ("id");
