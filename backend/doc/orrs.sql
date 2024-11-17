-- MariaDB dump 10.19  Distrib 10.6.5-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: orrs
-- ------------------------------------------------------
-- Server version	10.6.5-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tb_account`
--

DROP TABLE IF EXISTS `tb_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_account` (
  `OID` char(36) NOT NULL,
  `ACCOUNT` varchar(24) NOT NULL,
  `PASSWORD` varchar(255) NOT NULL,
  `ON_JOB` varchar(50) NOT NULL DEFAULT 'Y',
  `CUSERID` varchar(24) DEFAULT NULL,
  `CDATE` datetime DEFAULT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ACCOUNT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_account`
--

LOCK TABLES `tb_account` WRITE;
/*!40000 ALTER TABLE `tb_account` DISABLE KEYS */;
INSERT INTO `tb_account` VALUES ('0','admin','$2y$12$Q4x02Q0WKHWXAQ.NoGCs8ObX4sac890xeRnaNUxNnz/VEiHWazIp.','Y','admin','2012-11-11 10:56:23','admin','2014-04-19 11:32:04'),('15822da5-25dc-490c-bdfb-be75f5ff4843','tester','$2y$12$Q4x02Q0WKHWXAQ.NoGCs8ObX4sac890xeRnaNUxNnz/VEiHWazIp.','Y','admin','2015-04-23 11:26:53','admin','2015-08-29 17:54:08'),('52cb274e-388d-419f-a81e-67ca599bfb63','steven','$2y$12$Q4x02Q0WKHWXAQ.NoGCs8ObX4sac890xeRnaNUxNnz/VEiHWazIp.','Y','admin','2015-09-11 10:33:53',NULL,NULL),('9c239d19-3646-41db-b394-d34c5bf34671','tiffany','$2y$12$Q4x02Q0WKHWXAQ.NoGCs8ObX4sac890xeRnaNUxNnz/VEiHWazIp.','Y','admin','2015-09-11 10:15:29',NULL,NULL);
/*!40000 ALTER TABLE `tb_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_orrs_command`
--

DROP TABLE IF EXISTS `tb_orrs_command`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_orrs_command` (
  `OID` char(36) NOT NULL,
  `CMD_ID` varchar(10) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  `USER_MESSAGE` varchar(8000) NOT NULL,
  `RESULT_VARIABLE` varchar(50) NOT NULL,
  `RESULT_TYPE` varchar(10) NOT NULL DEFAULT 'GROOVY',
  `LLM_MODEL` varchar(25) NOT NULL DEFAULT 'gemma2',
  `RESULT_ALW_NUL` varchar(1) NOT NULL DEFAULT 'N',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`CMD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_orrs_command`
--

LOCK TABLES `tb_orrs_command` WRITE;
/*!40000 ALTER TABLE `tb_orrs_command` DISABLE KEYS */;
INSERT INTO `tb_orrs_command` VALUES ('974b7fff-9aaa-11ef-b609-a1eae7d5353c','PP01','取得水量拆分檔內容','','### 資料庫資訊\n1. sql server 帳戶 sa 密碼 P@ssw0rd@@@@\n2. sql server IP位置 127.0.0.1 , 服務 port 號是 1433\n3. database file is: NSC_B21\n\n### 資料表資訊\n1. 資料表 WATER_QUANTITY_SPLIT\n2. 資料表 WATER_QUANTITY_SPLIT欄位 [W_ID, QUANTITY, TOTAL_AMOUNT]\n3. 資料表 WATER_QUANTITY_SPLIT欄位型態 [W_ID(int), QUANTITY(decimal), TOTAL_AMOUNT(decimal)]\n4. 資料表 COMPANY 欄位[F_NAMEC] , COMPANY資料表欄位型態 [ F_NAMEC(varchar2)]\n5. 資料表 WATER_SOURCES_HISTORY 欄位[ WATER_SOURCES_ID ] , WATER_SOURCES_HISTORY 資料表欄位型態 [ WATER_SOURCES_ID(varchar2) ]\n\n### 資料表關聯關係\n1. 資料表關聯關係 \n	WATER_QUANTITY_SPLIT.FACTORY_WATER_SOURCES_ID1 = FACTORY_WATER_SOURCES.OID \n	FACTORY_WATER_SOURCES.FACTORY_ID = FACTORY_HISTORY.OID\n	COMPANY.OID = FACTORY_HISTORY.COMPANY_OID\n	WATER_SOURCES_HISTORY.OID = FACTORY_WATER_SOURCES.WATER_SOURCES_ID\n\n### 最後步驟產生groovy規範\n1. 用 groovy 產出連線資料庫, 資料表要串聯關係, \n	並取出資料 WATER_QUANTITY_SPLIT,COMPANY,WATER_SOURCES_HISTORY 這3個資料表定義的欄位 前10筆(如: select top 10 * from ....) \n	條件: \n		a. WATER_QUANTITY_SPLIT.CHARGE_SDATE 大於等於2023年2月1日 , 且 WATER_QUANTITY_SPLIT.CHARGE_EDATE 小於等於2023年4月30日\n		b. FACTORY_HISTORY.BASE_ID 為字串 \'01\'\n		c. 以QUANTITY desc排序\n2. 建議需要 import java.sql.*\n3. jdbc driver class 為 com.microsoft.sqlserver.jdbc.SQLServerDriver 所以JDBC url 開頭因該為 jdbc:sqlserver://\n4. 請一定要在 jdbc url 加上 encrypt=false; 參數\n5. 程式碼不要產在自訂義method中, 請用平舖直敘式方式產生程式碼\n6. 需要 import 的 package 或 class 請寫在程式最上方\n7. 將資料放入List<Map> 中\n8. 用 com.fasterxml.jackson 將 List<Map> 內容轉成字串 json資料, 配置到jsonResult變數\n9. 程式不需要增加 try catch 處理\n10. 最後用 return  jsonResult, 給我 groovy code','','GROOVY','qwen2.5-coder','N','admin','2024-11-04 20:44:54','admin','2024-11-14 22:53:44'),('a34e225a-9d17-11ef-a316-d9ccce9e80b5','PP02','產生echarts 圖表','','### json資料內容\n```json\n $P{previousInvokeResult}\n```\n### 產生html code條件\n1. 請將json資料, 使用 echarts 產生 pie, bar, line圖 (這3個圖表id必須不相同)\n2. 各項目標題為 json資料的 F_NAMEC 加上 \'(\' 符號 再加上 json資料 WATER_SOURCES_ID 在加上 \')\'符號\n3. 各項目的實際值為 json資料的 QUANTITY \n4. 給我 html code','','HTML','qwen2.5-coder','N','admin','2024-11-07 22:50:32','admin','2024-11-14 22:34:07'),('e115efc0-a296-11ef-9831-e5e38b8d0663','PP03','檢核程式碼正確性','','### groovy程式碼\n```groovy\n$P{previousMessage}\n```\n\n### 檢查規則\n如果程式不正確\n  請修正groovy錯誤的內容, 回應修正後的groovy程式碼內容給我\n如果正確\n  回應原本的groovy程式碼內容給我\n','result','GROOVY','qwen2.5-coder','N','admin','2024-11-14 22:43:57','admin','2024-11-14 22:45:20');
/*!40000 ALTER TABLE `tb_orrs_command` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_orrs_command_adv`
--

DROP TABLE IF EXISTS `tb_orrs_command_adv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_orrs_command_adv` (
  `OID` char(36) NOT NULL,
  `CMD_ID` varchar(10) NOT NULL,
  `ADV_TYPE` varchar(1) NOT NULL DEFAULT '0',
  `SCRIPT_CONTENT` varchar(8000) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`CMD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_orrs_command_adv`
--

LOCK TABLES `tb_orrs_command_adv` WRITE;
/*!40000 ALTER TABLE `tb_orrs_command_adv` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_orrs_command_adv` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_orrs_command_prompt`
--

DROP TABLE IF EXISTS `tb_orrs_command_prompt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_orrs_command_prompt` (
  `OID` char(36) NOT NULL,
  `CMD_ID` varchar(10) NOT NULL,
  `ITEM_SEQ` smallint(6) NOT NULL DEFAULT 0,
  `PROMPT_CONTENT` varchar(8000) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`CMD_ID`,`ITEM_SEQ`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_orrs_command_prompt`
--

LOCK TABLES `tb_orrs_command_prompt` WRITE;
/*!40000 ALTER TABLE `tb_orrs_command_prompt` DISABLE KEYS */;
INSERT INTO `tb_orrs_command_prompt` VALUES ('126fe8c3-a297-11ef-9831-954fd19f8fc1','PP03',0,'回應訊息不需要給 implementation , maven, gradle 資訊','admin','2024-11-14 22:45:20',NULL,NULL),('3ef3d335-a298-11ef-9831-89426c06ca95','PP01',0,'json library 使用的是 com.fasterxml.jackson','admin','2024-11-14 22:53:44',NULL,NULL),('3ef44866-a298-11ef-9831-3d1529ed6e93','PP01',1,'回應訊息不需要給 implementation , maven, gradle 資訊','admin','2024-11-14 22:53:44',NULL,NULL),('3ef49687-a298-11ef-9831-2db0a5960625','PP01',2,'你必須遵守: 不要把結果用 print 或 println 方式輸出, 請用 return 方式輸出值','admin','2024-11-14 22:53:44',NULL,NULL),('81235d41-a295-11ef-9831-d71333d1d941','PP02',0,'echarts 版本請用 5.3.3 , CDN 位置 https://cdnjs.cloudflare.com/ajax/libs/echarts/5.3.3/echarts.min.js','admin','2024-11-14 22:34:07',NULL,NULL),('8123d272-a295-11ef-9831-57fcb2b073ea','PP02',1,'echarts 圖表寬度740px , 高度600px , echarts 圖表請配置 tooltip','admin','2024-11-14 22:34:07',NULL,NULL),('812495c3-a295-11ef-9831-3116f35e2a10','PP02',2,'這很重要必須遵守: json資料筆數不要刪減','admin','2024-11-14 22:34:07',NULL,NULL);
/*!40000 ALTER TABLE `tb_orrs_command_prompt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_orrs_doc`
--

DROP TABLE IF EXISTS `tb_orrs_doc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_orrs_doc` (
  `OID` char(36) NOT NULL,
  `DOC_ID` varchar(10) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `CONTENT` varchar(8000) NOT NULL DEFAULT '',
  `SYS_PROMPT_TPL` varchar(1000) NOT NULL DEFAULT '',
  `TPL_VARIABLE` varchar(100) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `DOC_ID` (`DOC_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_orrs_doc`
--

LOCK TABLES `tb_orrs_doc` WRITE;
/*!40000 ALTER TABLE `tb_orrs_doc` DISABLE KEYS */;
INSERT INTO `tb_orrs_doc` VALUES ('04031e07-a495-11ef-90f4-91f56f58a2c7','DOC0001','B21資料表內容 - 01','### 資料表欄位\n1. 資料表 WATER_QUANTITY_SPLIT 欄位型態內容\n	[W_ID] [int] IDENTITY(1,1) NOT NULL,\n	[PROCESS_ID] [int] NOT NULL,\n	[WATER_QUANTITY_ID] [int] NOT NULL,\n	[FACTORY_WATER_SOURCES_ID1] [int] NOT NULL,\n	[QUANTITY_1] [decimal](13, 6) NULL,\n	[CALCULATE_UNIT_OID1] [int] NOT NULL,\n	[FACTORY_WATER_SOURCES_ID2] [int] NOT NULL,\n	[QUANTITY_2] [decimal](13, 6) NULL,\n	[CALCULATE_UNIT_OID2] [int] NOT NULL,\n	[FACTORY_WATER_SOURCES_ID3] [int] NOT NULL,\n	[QUANTITY_3] [decimal](13, 6) NULL,\n	[CALCULATE_UNIT_OID3] [int] NOT NULL,\n	[AMOUNT] [decimal](12, 2) NOT NULL,\n	[CHARGE_SDATE] [datetime] NOT NULL,\n	[CHARGE_EDATE] [datetime] NOT NULL,\n	[CHARGE_DATE1] [int] NULL,\n	[CHARGE_DATE2] [int] NULL,\n	[BILLING_QUANTITY_1] [decimal](13, 6) NULL,\n	[BILLING_QUANTITY_2] [decimal](13, 6) NULL,\n	[BILLING_QUANTITY_3] [decimal](13, 6) NULL,\n	[COD_AVG] [decimal](12, 6) NULL,\n	[COD_RANAGE_ID] [int] NULL,\n	[SS_AVG] [decimal](12, 6) NULL,\n	[SS_RANAGE_ID] [int] NULL,\n	[QUANTITY] [decimal](13, 6) NULL,\n	[BILLING_QUANTITY] [decimal](13, 6) NULL,\n	[QUANTITY_AVG] [decimal](12, 6) NULL,\n	[COD_TOTAL] [decimal](10, 3) NULL,\n	[COD_COUNT] [int] NULL,\n	[COD_WEIGHT] [decimal](12, 6) NULL,\n	[SS_TOTAL] [decimal](10, 3) NULL,\n	[SS_COUNT] [int] NULL,\n	[SS_WEIGHT] [decimal](12, 6) NULL,\n	[EXCEEDED_EV_AMOUNT] [decimal](12, 2) NULL,\n	[TOTAL_AMOUNT] [decimal](12, 2) NULL,\n	[ORIGINAL_QUANTITY_1] [decimal](12, 6) NULL,\n	[ORIGINAL_QUANTITY_2] [decimal](12, 6) NULL,\n	[ORIGINAL_QUANTITY_3] [decimal](12, 6) NULL,\n	[COD_AMOUNT] [decimal](12, 2) NULL,\n	[SS_AMOUNT] [decimal](12, 2) NULL,\n	[COD_RANGE_RATE_FLAG] [char](1) NULL,\n	[SS_RANGE_RATE_FLAG] [char](1) NULL,\n	[ACTIVE_FLAG] [char](1) NULL,\n	[ADJ_AMOUNT] [decimal](12, 2) NULL,\n	[COMPANY_OID] [int] NULL,\n	[NH3N_RANAGE_ID] [int] NULL,\n	[NH3N_AVG] [decimal](12, 6) NULL,\n	[NH3N_TOTAL] [decimal](10, 3) NULL,\n	[NH3N_COUNT] [int] NULL,\n	[HN3N_WEIGHT] [decimal](12, 6) NULL,\n	[NH3N_AMOUNT] [decimal](12, 2) NULL,\n	[NH3N_RANGE_RATE_FLAG] [char](1) NULL,\n	[MEMO] [varchar](500) NULL,\n	[REMARK] [varchar](500) NULL,\n	[SL_WEIGHT] [decimal](10, 3) NULL,\n	[SL_AMOUNT] [decimal](10, 3) NULL,\n	[CX_AMOUNT] [decimal](10, 3) NULL,\n	[COND_AMOUNT] [decimal](12, 2) NULL,\n	[CCR_VAL] [decimal](8, 2) NULL,\n\n\n2. 資料表 FACTORY_WATER_SOURCES 欄位型態內容\n	[OID] [int] IDENTITY(1,1) NOT NULL,\n	[FACTORY_ID] [int] NOT NULL,\n	[WATER_SOURCES_ID] [int] NOT NULL,\n	[AREA] [decimal](10, 3) NULL,\n	[CHARGE] [char](1) NOT NULL,\n	[RENT_NO] [nvarchar](20) NULL,\n	[CHARGE_SDATE] [datetime] NULL,\n	[CHARGE_EDATE] [datetime] NULL,\n	[APPLY_NO] [nvarchar](20) NULL,\n	[CREATE_USER] [varchar](25) NOT NULL,\n	[CREATE_DATE] [datetime] NOT NULL,\n	[END_DATE] [datetime] NULL,\n	[REF_OID] [int] NULL,\n\n\n3. 資料表 COMPANY 欄位型態內容\n	[OID] [int] IDENTITY(1,1) NOT NULL,\n	[REF_OID] [int] NULL,\n	[PARK_ID] [varchar](2) NOT NULL,\n	[BASE_ID] [varchar](2) NOT NULL,\n	[CREATETYPE] [char](1) NOT NULL,\n	[F_ID] [varchar](5) NOT NULL,\n	[F_NAMEC] [nvarchar](60) NOT NULL,\n	[F_SNAME] [nvarchar](24) NULL,\n	[F_NAME] [nvarchar](200) NULL,\n	[F_ADDRC] [nvarchar](250) NULL,\n	[F_ADDR] [nvarchar](500) NULL,\n	[F_REPLC] [nvarchar](100) NULL,\n	[F_REPL] [nvarchar](100) NULL,\n	[F_TEL] [nvarchar](500) NULL,\n	[F_TNO] [varchar](8) NULL,\n	[CONTACT_NAME] [nvarchar](210) NULL,\n	[CONTACT_ADDRESS] [nvarchar](1400) NULL,\n	[CONTACT_PHONE_AREACODE] [varchar](3) NULL,\n	[CONTACT_PHONE_NO] [varchar](63) NULL,\n	[CONTACT_PHONE_EXT] [varchar](10) NULL,\n	[CONTACT_MOBILE] [varchar](70) NULL,\n	[CONTACT_EMAIL] [varchar](350) NULL,\n	[CONTACT_NAME_1] [nvarchar](210) NULL,\n	[CONTACT_PHONE_AREACODE_1] [varchar](3) NULL,\n	[CONTACT_PHONE_NO_1] [varchar](63) NULL,\n	[CONTACT_PHONE_EXT_1] [varchar](10) NULL,\n	[CONTACT_MOBILE_1] [varchar](70) NULL,\n	[CONTACT_EMAIL_1] [varchar](350) NULL,\n	[CONTACT_NAME_2] [nvarchar](210) NULL,\n	[CONTACT_PHONE_AREACODE_2] [varchar](3) NULL,\n	[CONTACT_PHONE_NO_2] [varchar](63) NULL,\n	[CONTACT_PHONE_EXT_2] [varchar](10) NULL,\n	[CONTACT_MOBILE_2] [varchar](70) NULL,\n	[CONTACT_EMAIL_2] [varchar](350) NULL,\n	[AUTO_CHARGE] [char](1) NULL,\n	[BANK_ID] [int] NULL,\n	[APPLY_DATE] [datetime] NULL,\n	[CUSTOMER_NO] [nvarchar](30) NULL,\n	[BANK_ACC] [varchar](20) NULL,\n	[BANK_CODE] [char](3) NULL,\n	[BILL_TYPE] [varchar](1) NULL,\n	[REF_UPDATE_DATETIME] [varchar](30) NULL,\n	[SYNC_DATE] [datetime] NULL,\n	[CREATE_USER] [varchar](25) NULL,\n	[CREATE_DATE] [datetime] NOT NULL,\n	[END_DATE] [datetime] NULL,\n	[CONTACT_FAX_1] [varchar](20) NULL,\n	[REMARK] [varchar](100) NULL,\n	[ACH_BANK_CODE] [char](7) NULL,\n	[ACH_F_TNO] [varchar](8) NULL,\n\n\n4. 資料表 FACTORY_HISTORY 欄位型態內容\n	[OID] [int] IDENTITY(1,1) NOT NULL,\n	[REF_OID] [int] NOT NULL,\n	[BASE_ID] [varchar](2) NOT NULL,\n	[COMPANY_OID] [int] NOT NULL,\n	[FACTORY_ID] [nvarchar](20) NOT NULL,\n	[ADDRESS] [nvarchar](60) NOT NULL,\n	[FACTORY_DESC] [nvarchar](100) NOT NULL,\n	[AGGREGATION] [char](1) NOT NULL,\n	[DISPLAY_NAME] [nvarchar](50) NULL,\n	[CREATE_USER] [varchar](25) NOT NULL,\n	[CREATE_DATE] [datetime] NOT NULL,\n	[END_DATE] [datetime] NULL,\n	[FOUND_TYPE] [int] NULL,\n	[EMAIL] [varchar](50) NULL,\n	\n\n5. 資料表 WATER_SOURCES_HISTORY 欄位型態內容\n	[OID] [int] IDENTITY(1,1) NOT NULL,\n	[REF_OID] [int] NOT NULL,\n	[STW_ID] [int] NOT NULL,\n	[WATER_SOURCES_ID] [nvarchar](30) NOT NULL,\n	[COUNT_BY_AREA] [char](1) NOT NULL,\n	[WATER_QUALITY] [char](1) NOT NULL,\n	[WATER_QUANTITY] [char](1) NOT NULL,\n	[WATER_SOURCES_DESC] [nvarchar](200) NULL,\n	[SORT_VALUE] [nvarchar](20) NULL,\n	[CREATE_USER] [varchar](25) NOT NULL,\n	[CREATE_DATE] [datetime] NOT NULL,\n	[END_DATE] [datetime] NULL,\n	[CMD] [decimal](10, 4) NULL,\n	[COND_TYPE] [varchar](1) NOT NULL,\n\n\n### 資料表關係\n	WATER_QUANTITY_SPLIT.FACTORY_WATER_SOURCES_ID1 = FACTORY_WATER_SOURCES.OID \n	FACTORY_WATER_SOURCES.FACTORY_ID = FACTORY_HISTORY.OID\n	COMPANY.OID = FACTORY_HISTORY.COMPANY_OID\n	WATER_SOURCES_HISTORY.OID = FACTORY_WATER_SOURCES.WATER_SOURCES_ID\n	\n### SQL範例\n```sql\nselect * \nfrom \nWATER_QUANTITY_SPLIT wqs, FACTORY_WATER_SOURCES fws, COMPANY c, FACTORY_HISTORY fh, WATER_SOURCES_HISTORY wsh\nwhere\nwqs.FACTORY_WATER_SOURCES_ID1 = fws.OID \nand fws.FACTORY_ID = fh.OID\nand c.OID = fh.COMPANY_OID\nand wsh.OID = fws.WATER_SOURCES_ID\n```\n','參考以下資料表描敘文件:\n{information}','information','admin','2024-11-17 11:35:39','admin','2024-11-17 12:40:12');
/*!40000 ALTER TABLE `tb_orrs_doc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_orrs_task`
--

DROP TABLE IF EXISTS `tb_orrs_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_orrs_task` (
  `OID` char(36) NOT NULL,
  `TASK_ID` varchar(10) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  `CRON_EXPR` varchar(20) NOT NULL,
  `ENABLE_FLAG` varchar(1) NOT NULL DEFAULT 'Y',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`TASK_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_orrs_task`
--

LOCK TABLES `tb_orrs_task` WRITE;
/*!40000 ALTER TABLE `tb_orrs_task` DISABLE KEYS */;
INSERT INTO `tb_orrs_task` VALUES ('7590bed3-9aab-11ef-b609-854d67a445fb','task02','我的水量拆分檔任務','水量資料','0 55 20 * * ?','Y','admin','2024-11-04 20:51:07','admin','2024-11-15 23:19:55');
/*!40000 ALTER TABLE `tb_orrs_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_orrs_task_cmd`
--

DROP TABLE IF EXISTS `tb_orrs_task_cmd`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_orrs_task_cmd` (
  `OID` char(36) NOT NULL,
  `TASK_ID` varchar(10) NOT NULL,
  `CMD_ID` varchar(10) NOT NULL,
  `ITEM_SEQ` smallint(6) NOT NULL DEFAULT 0,
  `ENABLE_FLAG` varchar(1) NOT NULL DEFAULT 'Y',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `TASK_ID` (`TASK_ID`,`CMD_ID`,`ITEM_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_orrs_task_cmd`
--

LOCK TABLES `tb_orrs_task_cmd` WRITE;
/*!40000 ALTER TABLE `tb_orrs_task_cmd` DISABLE KEYS */;
INSERT INTO `tb_orrs_task_cmd` VALUES ('11ddfda4-a365-11ef-afdb-950e7f9a4017','task02','PP01',0,'Y','admin','2024-11-15 23:19:55',NULL,NULL),('11dec0f5-a365-11ef-afdb-3165ef009868','task02','PP03',1,'Y','admin','2024-11-15 23:19:55',NULL,NULL),('11df5d36-a365-11ef-afdb-45f6c007907f','task02','PP02',2,'Y','admin','2024-11-15 23:19:55',NULL,NULL);
/*!40000 ALTER TABLE `tb_orrs_task_cmd` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_orrs_task_result`
--

DROP TABLE IF EXISTS `tb_orrs_task_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_orrs_task_result` (
  `OID` char(36) NOT NULL,
  `TASK_ID` varchar(10) NOT NULL,
  `CMD_ID` varchar(10) NOT NULL,
  `ITEM_SEQ` smallint(6) NOT NULL,
  `PROCESS_MS_T1` varchar(13) NOT NULL,
  `PROCESS_MS_T2` varchar(13) NOT NULL,
  `CONTENT` mediumblob DEFAULT NULL,
  `INVOKE_CONTENT` mediumblob DEFAULT NULL,
  `LAST_CMD` varchar(1) NOT NULL DEFAULT 'N',
  `PROCESS_ID` varchar(25) NOT NULL,
  `PROCESS_FLAG` varchar(1) NOT NULL DEFAULT 'Y',
  `TASK_USER_MESSAGE` mediumblob NOT NULL,
  `CAUSE_MESSAGE` varchar(5000) DEFAULT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`TASK_ID`),
  KEY `IDX_2` (`CMD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_orrs_task_result`
--

LOCK TABLES `tb_orrs_task_result` WRITE;
/*!40000 ALTER TABLE `tb_orrs_task_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_orrs_task_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role`
--

DROP TABLE IF EXISTS `tb_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_role` (
  `OID` char(36) NOT NULL,
  `ROLE` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(50) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ROLE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role`
--

LOCK TABLES `tb_role` WRITE;
/*!40000 ALTER TABLE `tb_role` DISABLE KEYS */;
INSERT INTO `tb_role` VALUES ('4b1796ad-0bb7-4a65-b45e-439540ba5dbd','admin','administrator role!','admin','2014-10-09 15:02:24',NULL,NULL),('58914623-46ea-4797-bbec-2dadc5d0800e','COMMON01','Common role!','admin','2017-05-09 13:31:42','admin','2024-10-09 21:56:13'),('c7c69396-e5e6-48ca-b09c-9445b69e2ad5','*','all role','admin','2014-10-09 15:02:54',NULL,NULL);
/*!40000 ALTER TABLE `tb_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role_permission`
--

DROP TABLE IF EXISTS `tb_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_role_permission` (
  `OID` char(36) NOT NULL,
  `ROLE` varchar(50) NOT NULL,
  `PERMISSION` varchar(255) NOT NULL,
  `PERM_TYPE` varchar(15) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(50) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ROLE`,`PERMISSION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role_permission`
--

LOCK TABLES `tb_role_permission` WRITE;
/*!40000 ALTER TABLE `tb_role_permission` DISABLE KEYS */;
INSERT INTO `tb_role_permission` VALUES ('7c732a40-4664-11ee-9888-c1ca1a9bff29','COMMON01','/prog001d0004','VIEW','','admin','2023-08-29 20:06:29',NULL,NULL),('7f0260a3-4664-11ee-9888-6f9b2693d639','COMMON01','/prog001d0004/create','VIEW','','admin','2023-08-29 20:06:33',NULL,NULL),('815ccb66-4664-11ee-9888-b170f20e302b','COMMON01','/prog001d0004/edit','VIEW','','admin','2023-08-29 20:06:37',NULL,NULL);
/*!40000 ALTER TABLE `tb_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys`
--

DROP TABLE IF EXISTS `tb_sys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys` (
  `OID` char(36) NOT NULL,
  `SYS_ID` varchar(10) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `HOST` varchar(200) NOT NULL,
  `CONTEXT_PATH` varchar(100) NOT NULL,
  `IS_LOCAL` varchar(1) NOT NULL DEFAULT 'Y',
  `ICON` varchar(20) NOT NULL DEFAULT ' ',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`SYS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys`
--

LOCK TABLES `tb_sys` WRITE;
/*!40000 ALTER TABLE `tb_sys` DISABLE KEYS */;
INSERT INTO `tb_sys` VALUES ('c6643182-85a5-4f91-9e73-10567ebd0dd5','CORE','Core-system','127.0.0.1:8080','core-web','Y','SYSTEM','admin','2017-04-10 20:42:00','admin','2024-02-27 21:31:36');
/*!40000 ALTER TABLE `tb_sys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_bean_help`
--

DROP TABLE IF EXISTS `tb_sys_bean_help`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_bean_help` (
  `OID` char(36) NOT NULL,
  `BEAN_ID` varchar(255) NOT NULL,
  `METHOD` varchar(100) NOT NULL,
  `SYSTEM` varchar(10) NOT NULL,
  `ENABLE_FLAG` varchar(1) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`BEAN_ID`,`METHOD`,`SYSTEM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_bean_help`
--

LOCK TABLES `tb_sys_bean_help` WRITE;
/*!40000 ALTER TABLE `tb_sys_bean_help` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_bean_help` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_bean_help_expr`
--

DROP TABLE IF EXISTS `tb_sys_bean_help_expr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_bean_help_expr` (
  `OID` char(36) NOT NULL,
  `HELP_OID` char(36) NOT NULL,
  `EXPR_ID` varchar(20) NOT NULL,
  `EXPR_SEQ` varchar(10) NOT NULL,
  `RUN_TYPE` varchar(10) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`EXPR_ID`,`HELP_OID`,`RUN_TYPE`),
  KEY `IDX_1` (`HELP_OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_bean_help_expr`
--

LOCK TABLES `tb_sys_bean_help_expr` WRITE;
/*!40000 ALTER TABLE `tb_sys_bean_help_expr` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_bean_help_expr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_bean_help_expr_map`
--

DROP TABLE IF EXISTS `tb_sys_bean_help_expr_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_bean_help_expr_map` (
  `OID` char(36) NOT NULL,
  `HELP_EXPR_OID` char(36) NOT NULL,
  `METHOD_RESULT_FLAG` varchar(1) NOT NULL DEFAULT 'N',
  `METHOD_PARAM_CLASS` varchar(255) NOT NULL DEFAULT ' ',
  `METHOD_PARAM_INDEX` int(3) NOT NULL DEFAULT 0,
  `VAR_NAME` varchar(255) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`VAR_NAME`,`HELP_EXPR_OID`),
  KEY `IDX_1` (`HELP_EXPR_OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_bean_help_expr_map`
--

LOCK TABLES `tb_sys_bean_help_expr_map` WRITE;
/*!40000 ALTER TABLE `tb_sys_bean_help_expr_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_bean_help_expr_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_code`
--

DROP TABLE IF EXISTS `tb_sys_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_code` (
  `OID` char(36) NOT NULL,
  `CODE` varchar(25) NOT NULL,
  `TYPE` varchar(10) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `PARAM1` varchar(100) DEFAULT NULL,
  `PARAM2` varchar(100) DEFAULT NULL,
  `PARAM3` varchar(100) DEFAULT NULL,
  `PARAM4` varchar(100) DEFAULT NULL,
  `PARAM5` varchar(100) DEFAULT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_code`
--

LOCK TABLES `tb_sys_code` WRITE;
/*!40000 ALTER TABLE `tb_sys_code` DISABLE KEYS */;
INSERT INTO `tb_sys_code` VALUES ('2d9c84e4-a956-42ac-96cb-1f6292d182a9','CNF_CONF002','CNF','enable mail sender!','Y',NULL,NULL,NULL,NULL,'admin','2014-12-25 09:09:57','admin','2020-09-14 04:36:34'),('4df770a6-6a9c-4d25-bdcd-1dee819d2ba6','CNF_CONF001','CNF','default mail from account!','root@localhost',NULL,NULL,NULL,NULL,'admin','2014-12-24 21:51:16','admin','2020-09-14 04:36:34'),('57877c4d-4f3e-4679-880a-a262eeba0c3d','TOKEN','AUTH','QiFu3 Client token','9TYM7TRuILqFk9XoR0v6Yx672','COMMON01',NULL,NULL,NULL,'admin','2021-10-30 17:12:04',NULL,NULL),('a5f7ee37-f33f-48a6-b448-92ccb8cdf96a','CNF_CONF003','CNF','first load javascript','addTab(\'CORE_PROG999D9999Q\', null);',NULL,NULL,NULL,NULL,'admin','2014-12-25 09:09:57',NULL,NULL),('caf00ba5-fe63-4dc4-a1a3-32527f6629b2','CMM_CONF001','CMM','Common role for default user!','COMMON01',NULL,NULL,NULL,NULL,'admin','2017-05-09 12:29:00',NULL,NULL);
/*!40000 ALTER TABLE `tb_sys_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_event_log`
--

DROP TABLE IF EXISTS `tb_sys_event_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_event_log` (
  `OID` char(36) NOT NULL,
  `USER` varchar(24) NOT NULL,
  `SYS_ID` varchar(10) NOT NULL,
  `EXECUTE_EVENT` varchar(255) NOT NULL,
  `IS_PERMIT` varchar(1) NOT NULL DEFAULT 'N',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`USER`),
  KEY `IDX_2` (`CDATE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_event_log`
--

LOCK TABLES `tb_sys_event_log` WRITE;
/*!40000 ALTER TABLE `tb_sys_event_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_event_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_expr_job`
--

DROP TABLE IF EXISTS `tb_sys_expr_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_expr_job` (
  `OID` char(36) NOT NULL,
  `SYSTEM` varchar(10) NOT NULL,
  `ID` varchar(20) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `ACTIVE` varchar(1) NOT NULL DEFAULT 'Y',
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  `RUN_STATUS` varchar(1) NOT NULL DEFAULT 'Y',
  `CHECK_FAULT` varchar(1) NOT NULL DEFAULT 'N',
  `EXPR_ID` varchar(20) NOT NULL,
  `RUN_DAY_OF_WEEK` varchar(1) NOT NULL,
  `RUN_HOUR` varchar(2) NOT NULL,
  `RUN_MINUTE` varchar(2) NOT NULL,
  `CONTACT_MODE` varchar(1) NOT NULL DEFAULT '0',
  `CONTACT` varchar(500) DEFAULT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ID`),
  KEY `IDX_1` (`SYSTEM`,`ACTIVE`,`EXPR_ID`,`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_expr_job`
--

LOCK TABLES `tb_sys_expr_job` WRITE;
/*!40000 ALTER TABLE `tb_sys_expr_job` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_expr_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_expr_job_log`
--

DROP TABLE IF EXISTS `tb_sys_expr_job_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_expr_job_log` (
  `OID` char(36) NOT NULL,
  `ID` varchar(20) NOT NULL,
  `LOG_STATUS` varchar(1) NOT NULL DEFAULT 'N',
  `BEGIN_DATETIME` datetime NOT NULL,
  `END_DATETIME` datetime NOT NULL,
  `FAULT_MSG` varchar(2000) DEFAULT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`ID`,`LOG_STATUS`,`BEGIN_DATETIME`),
  KEY `IDX_2` (`CDATE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_expr_job_log`
--

LOCK TABLES `tb_sys_expr_job_log` WRITE;
/*!40000 ALTER TABLE `tb_sys_expr_job_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_expr_job_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_expression`
--

DROP TABLE IF EXISTS `tb_sys_expression`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_expression` (
  `OID` char(36) NOT NULL,
  `EXPR_ID` varchar(20) NOT NULL,
  `TYPE` varchar(10) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `CONTENT` varchar(8000) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`EXPR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_expression`
--

LOCK TABLES `tb_sys_expression` WRITE;
/*!40000 ALTER TABLE `tb_sys_expression` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_expression` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_icon`
--

DROP TABLE IF EXISTS `tb_sys_icon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_icon` (
  `OID` char(36) NOT NULL,
  `ICON_ID` varchar(20) NOT NULL,
  `FILE_NAME` varchar(200) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ICON_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_icon`
--

LOCK TABLES `tb_sys_icon` WRITE;
/*!40000 ALTER TABLE `tb_sys_icon` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_icon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_jreport`
--

DROP TABLE IF EXISTS `tb_sys_jreport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_jreport` (
  `OID` char(36) NOT NULL,
  `REPORT_ID` varchar(50) NOT NULL,
  `FILE` varchar(100) NOT NULL,
  `IS_COMPILE` varchar(50) NOT NULL DEFAULT 'N',
  `CONTENT` mediumblob NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`REPORT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_jreport`
--

LOCK TABLES `tb_sys_jreport` WRITE;
/*!40000 ALTER TABLE `tb_sys_jreport` DISABLE KEYS */;
INSERT INTO `tb_sys_jreport` VALUES ('f55a9ae4-b997-11ee-a0c4-97126ca2fa90','TEST01','TEST01.jrxml','Y',0x504B03041400000000004DAB1A57000000000000000000000000070000005445535430312F504B030414000000080020AB1A578FDF3D9426130000B5450000140000005445535430312F5445535430312E6A6173706572D51C6788335570932BEAE9777EDED97BEF5EB19CBDA49D46F792733767B903E35EF27259DDECAEBB1B2F67171B585050C18ABD8082888A200A961FA2600545EC88584045C4022AE2CC7B6F5B92CB6EF097E1FBEEB2EFCDCC9B99376FDECCBCB7F7E48FC2906D09FBEBC499B06B13E729B6492C8B9886E5D813445F517532712A6D9468A3403FFB7D9414064461F38AD130558D64154771846DC5F3940B9549D5989489A52A9A7A91B2AC916345612B0E56501A446ED66A6ACB11C618B0A6E82B93B263A9FA0A006EE0805646536CFB02E13221D9B284437AF1B6ACD8C0A094865FED0C1E9417769897730BD96259CE49F994583E2327C9F962A1BC90CFE6852D960DC7311A738A0584F2288BD66CE819A3A93B7961037B924DA502AC79BD67AA55A7BE286C545774C322F30A602A8E6AE88BC2B86ACF6A86E26428DCAC6138C442405B6E361A8AB55620AB004E1685DDBDA63355A78E6DA710A54AAC945E75B14655BBA43A1AE138796144233587319A1636372C95E80E1D17BA4C4A415DA903D79BE103E5310D1DA054A768016960DF420057D4CD1CC374A98DAED6895E3070064B6B2611859165A572FE8A056AA83AC2DE22E87ED2AE4D86743FC9743F896AD7AB306F5B32ED64558B54902D4738AC27A203234D4A4D1F21A7371B40678B4A407B38FD835E1B2A89B72D099B56815D9B38B623ECBF14C162968122F52AA9294DCD919D358D38C23E1188140CD086ABC451548DB3B3813DC9AEA0FB4550E1804067AC66580DC599552A8E61ADF9160E2A571B144B268E236CC91646D351B54999F2BD292E9226CC2C071F0554070D23A4A7CD1B8AAA7361A3D9F2B532A82B0D97F2B04E0D8153DC1830B43314AD093A3B347A5A8B1CC99FD51133CC2B6F09CC2858906FAD7CA8E9E8A1E63D1C3ED2E636D33658723C66651F9E925812866D9C7630AC7D97C478F6B189CD9633B7CDCD1CD23035C5411A93513498CB2A310C2436E4E0C2E74A190FAF4DAE9823A2C53AD3C3F3246B9982F0D393E019B786FF09817E9207D15FBC71E0546891E913FD0FBBC241313D2FBA01DFEF6EDADBEF0ED7A9B7A26E4E3635D549699AB14AAA202F3502643ED7322D62DB74851D18A1431F167710D33200C851893DA7988E704804F27C101C7D84459CA645CD1D27706360318AAA8DEB65331B796616B631B089A5D7E8048EB26E7FBAA6A2A78B6AC19F294B382CA6DE731A69C0623B19BCB5E9E91F17F2A695BAAA552DA2A3256D0362111F12B47270944A03D060396009E328E804EA61226559CA1A2AA375E5FBBBDCF9BA72EF8090C80B83B67A1169512E5607F1A7677042C284CF659630D14B2A54C4444811AE6D8E012D4B18A50CA0A627DA3B41A04DE492942B654E8151A62347096E3CEDB4505FBB3A10DB9424EA9DC7E8CF9DBA887508150B7B778B803CDE8784DF362822E6F472EF1498D925616819D61A58E63E4B71B666803715CB89033F0F7030D74D0B417B31C849DFFED9653FDCF8E635472499A442A24D0B09AA8504883B1553DC126939B32AD102AE64BBDEAE64C3B2619C0FBEF77C915C48340866C885B0E814EAD2D506490B5BD6D760484DD5CF2F41BC439CB4B0C16F812F8BC236E0841C58F4953A8663C50B8955036F240A5B2B7AA56E5818B2FA0E06651B1785ED42C376746FE97341575074A8C1161A38C010FF7D38FC5C08CFDD77493B673B78C2A7A8749D927900185FB4752F09E3816E0B34033B3A98D66151A6754A071670B7B3474B223502AEAAD2A9E81DFDC9320CCD51CD1E0CE3B6D13915AA9DD614D659686A9A038D01970D4489A2A3AA7C13E1A1D0A6AE89F0E74D4CC501C675FEB8157BEC1C71C4012B4E55CF6BDA4EAC18A6E481BBCEFFD03E960B77D4FE82D9A1D78201A96AB0A316DC808F3FCBE0B9C38AC9EB0E598159E2003605D82A0040531D34583022F5220051B4940649113283A4B71585EDBB7471839E8956CB296164D44D7838941EBB40F863FA21C7F07C92C36021865645A60F0771553B0F406A853F6F50EDF96A2DD75826D52A7181B6A42E433D9F94EAB06E57EABC790B68C690B08A43F0B6CD557B41AF520325BC6913FC9E365AD131BAC80031AE451C9E8B72F56E0C34C50FCE451FC9951FFD58D3E4F6B099092B74C552CCBA231C10BD61305064D0042DE915A3CA194CB2A6D9366BB30C9618701936B8CF5C80C968012423945A6C7521066F954EEBDBB6A3838F7178F418670450DB060AD9DD51F14985ADAE153F04E858E03BC50CAEB7536D9A1EE5F559159CF4997543A3613AEC78BC07720F02494795C5BA8BC28EBC1D7D659626B9EE6E68532489348C0B095A104250B79A16B6300D5B7553A83424606C2FC5A7BC30B48AA588BC9068C1FF35B02E2C2D403A6F585EE8ACAC3A93196C011D6F1DCCCF2130BF50859503331695CA76C13A3614F232F3DE011880149C5006B0212D0A03E793356E9C830DA34AB8F56C86DFB9C11C143DCB7300ED66A0B0806050648612DB041C61A0091783B7DD795B89AF423668ACF2C9BC8FC507F7F3A7CE6D691BAF8BEA2353072F4E68AC36DD9E3461E3D6B0D7F3C6359F569CBD7EBE030B18DB18308BF8CE8AC7650101079B4DB51ACECA16C0ECFDE456F752DB29817D46A83C5BE18F09937E62E50841EDAE93238CCFE6CF2A4B393155CA9F912B978AF06FDE34A3B2E6B69433544E1DC4854FD50F79A43F27986BD13CAE7DA6C2C52268004598ED01B8234C40559533631B3560CD69565563C2860952AA769D100720749D251BF96C0B44D8CA4FF74E51EC3A901EDAE4E3575EDBF6DCF70684E4AC3002DB7F9515B2B09A588799AEC326DA324F3C89CA33B0BA29FCA4C32337A74321716699CC1C5E3DB276C851B5230F3DE4F0A30EADC0B799E94366C8D461D529E5C8995A4569E1DC4C45CE0DB78B1E5333522896794608C28CFAC2A0B5BC78E7C01F7734AE998474EA54610B08006D888056D2AA639F0A4534C37B6A99373FFDDA76BBFF73FF9E0FDD7BC9592F6D7BEA2D68647486B7F493511A097DFCE31B0F7E72DCCF3F2485C4AC3074215A38F8F88D3E54A109218475ED93B7EFB2F96D5FDE4053A6D4BB82408DF1A84881BB442FEB083E9CC9154A390967D01F9C87B96FED6E5DF1C13D7FFE044C2EBA4C9A909AF69793F2A0C4B7DBCDC12A59257D5EA9BAFBFF91E0437923D1A37C285047CF015401183DF7B2D1CAA0B8D06945172BD20168F4C0582B0FB3B2096D62C5900A86F2A1DE01D3EBD902CBE46DC89BD2361F7B048AE76188616871FB7101C6AFDF00EB69574BBE46853ECE3C80420837E10798C80F861A07C62415A233DC3B8FD80CC7C8781B296FF076BD6DC17FB90D7DD4C1448EC2DC3D23CA8E3390E851303F30557D58C67C009AEF116E2586783FF23861D33175046A15C196DAA60BC9AC745237ED753BE2677930A20406187BC8F8E516A05C32CC9874CD7E287B99814F7904D2A61A06A238C779BDCA23F5234576B61568E9CC768E03DC4013A6ABDC1EC6BC74256015878A31F39690D7C0551E66630B9B8D97AA41A2CFDB36F0B634C148D205749465197C00B554D76BB0366602DBB44CFC306BF33F2C68E11F16981C16B917842B3DEB952F0BC533FBB06A3F56F3276C282F24D52A9C8754EA4DFD7C1BD6EF9218B70E9F41140CD780E398F52B861B0E6747E91E450FCA0249E498DF2A1145F37A681172A40956DABB7CD9C968E3EC773E574F7B68C1AF645AC24CDFAAA3B402A5DA34131FB5009B366771C011060BA9B99CE97FD8C44F464EBC5F285A67D237CD2C94CAA5DC59252F1ABE34547886A13126E867B3E7A784BE5463BDB685452CA6CC2978AABC1D9E5C49C4369A5685CCA9A0207D056716ECA98615DE38E76BB4148CC9434DD5600576142387573093894389A63CA1A3CE25C80B0215CC039762F80C56B8EC2B5B3AAA375DAEDEAE29D3D0054D62AD45978E4F47305C2C16D7761A0EEE35EFA8D8AE58AAE9C071375D30AEEC5E6B1CD96517F858C4843E3A31B1305D603775C3F14F8123D20B15B81FB24C8F590F8822720687C5927137ABF253CA13A2FDCC999D04DA4E481302B8907DA35C08932A79DC17AF170EFCC5741DC7E6B0B40E8EB9B4DA0E3A86610BAA12362FDCC4934183EDC3ECB06BB67FEF09AB1C368E623E4B09CCF979E400CD23C173EDDE3D8FA40B7A023975843D7A8168CA32D1C0A27BC1406A4726A0B65E6FD1F1A58E8472009BEF649CE2D7BB1D88BF97CBF61A85BF8B758034C1EC8BDD2F6212DD4AE51C96CF96CBFF0B511FF598C5A7C743D23EC6FB62084C779DFF81B8CF7056F1FB7321619FE53DD1A29E52944BFF03515FE6ACE2F75743A2BEC27B2245DD22532CE08E5F9E4F954EF91F88FC561BCBD8F64E48F4B7DB202255B0695E2E8BC54C4AFC1F88FF71805D7CFE3424FA2781DE48B107F3A0A5FF81C8DF7256F1FBF72171BFE33D91A26E92598058F37FE1AC7FF5B9C5C7DF4302FFE676C670D743996CAAF43FF0D78984CB2B3E0C04E54D24DD2E94768C4A6B5FA04D60DA6A3B4AC30CCDF1C2FF658E13A33EB7F8B83124F396D819778E17FE2773BC93CB2B3EEC12927767ECEA31C7F001F8524E2E4D4D435C7D60545CEDE7597B0A5BDC74E06BE7EEE1C6D6E37DD69F289540FD895E379C352C48B61AA60353B148CFF1D76CB8AE09278840A10AA7C6FC049226146D0967B700BDEDC251BC1CC4E3AEB394D3761D775427C05E15339442CF44A0FF683F91A053332AE5E68B52A9CCB75C935923ED3AA097B225FA080534AC6D50ACC4BE9CE4369CE47C4A8268AD04E581F25C6A3E4879837F1E030284B1B73D3525CFE7A432232277636C2AEEAB0A7657FE36044708128EFF0E4498E056BE0E0BB94C098A2141A2E3DEA2C878676C61FC8D1C7F2E7516D0582804790A1EE3F04B3961E4318E0C6B3055968B0B5226175F247A0B5CA6C9707796E48C949F2F89B9104B93BD69A696E1B0178E06DDB241BBFA196916E68478DDE89B8568C09D89369EB6E588528E89594E2F14B2610ADBFB14A46035641DE94AF9B95C79B15808D118F369E0AA5C3474D2DDBE678BD25C0A7EA532A5A274768044CFEA1AA33C1B7C13204C7EDCB5263125633098CAE6A420F16D7C83A0EB5A34F016FD3A02E6E6E6C51478DF2081AD7D01E10047EB66919BCB54C07C4ECC06518307A978261D461A9ECD8BB0DA63BB105EFC9A85E2DE7A467D465E2A2DA4C4FC621F64A160A45A4E13DF3E6A27BB751E52E5930B452907CEE9E47C21D5BE563BCF4C5D0202855A13E0D3C46F977A175E2F870828B9E79E267E58F1C1A3D8350068DFEF93AB83F873DD8D1ADE7461EEC3E105B3437AC0F1ED0BBF2B55C504C5AEB7B12751AE8B6139CE61792D9B3E44C325878D1739825839536B544BA737AB279F5AAB1CA65D94CFACB60A32FE6F358A27CF1DBE985D992ECAD3E717B25A1D6E4AAB8B8DB39DB31BB9E9823AB556B828DD289C7CEA79C5ECCAA18543178E6C997D54C568353350440F9C3F1CB414A7124AABF01DEFCCD06BCD07F50E3E42044EBA4F78E0BB170E7FD88D3E927D9CA6F8647C4106C3A701EEFEEE18E7135D26588A064F008B3B70C5325387D60A8642A0040A084A185FEA7C6DAE6582091E2DE744D87C763F7064562ACEED7E815A6B1E3EC1E2B491334FC949B9DD47360BB54E9C0BA67AEEEEC7EFCE37E0AB93CC7AA915CBA78BA6C903F4B35EFAF0C867AE7CF4CEEC23777C7544C638F97450E50111AAF48BB6CBDF5C75C91D073F9F7515B909287232A6225D22318F1FE0F6199870A5A9D1902A0D7737F58A45AF806100D519EEA585CD20BE23B4174E19186E9F2F3B657C2416BF750689DDEE618FBAACF9B7D3A6215A0B311CFFBE623E88C6D9D8C9A3452CBECFB40787BB758274468A1013ABBAEAC0DB9CDD62E270B83A82EA0C49348A2D7DBE0C2321CA7F3A055CFD09DF708A7595283C7F9DA76AACC00AFB283BA53B349260682ED6213958C098C3F4B69444972D657B9DACEEDE11FBED3F7D00DBA061FFCA950B0B73E99C14EFCD1A4FABEB89C9B65CF40427328770B3AB487CB81D86C5DF77B018652E053A91CA99054982FB4665E48676DF1F0F7F8CE3978AA59448B1E5D8E89ED6927D6B6D43A6282ECC153CBD21C5FB603A90816E235380DB20C3A551B93FF2409F2377B03E189FC014637D0B1E11514E7AEBEA297F9CA1F8E3746774383E01CEE8082A33C0E69331D8DC24FE28DDD9DC343E01579FCC1418A39E250CB356CA322EABE322975597F3C49E0B7F411491FA5EF12E3A065EA55DEFCC1FAD424AE54B11AFA0CDC47E596D2F1F32D6B504FF1DDCF558842DBA9487642F96C8FC95B75E4A4CA70A5933428A0303371FBABF97469FA73878D1BB36BCAF3BA2776D38F195E9DDDE39CDFD72060B8DAEBFEE8ECDFFF955FCF5DA639F38EFB1D9FC57480EFC05FEF9831A1B77915DF3C4AFE75032AC7999FDAA056E5A25BEF37E7CCD7AB5CE5ED6B1B25E87B15EC7059D1D2E2BB6DBFC758FAB4908B8EAADB7CDDAD7DB8077101920C1D1AEF226A5189894A82B361045C12DD709F70DE98CFF77264237A6C72B9661DB901A05007080024408FC6F0474F66CEBBE29DFD99B6899A18449A009D3466A5BEB76802CC77795C5FD6B1636AF97F9D1935FFA0B8944DF9BA9B4B1046F0854E9EBAA3E1265054E4EC2CC6EC178E217113A2B47635FDDFFF01F575E775412DED7752F0023FE122203C6B1BD85C00216A85AEF2646E8062AA71DFE1B1FC5E5F3408463E93D03BA1BFCE99A4FE26FF891F41B93F898F01E07C28F831E4AE22FAF71280C338C8F43DEE326E1DE4DC3BD9BE17DA808C9E9658D7584F523E0165D46BF789447F05B7273ECBBB205C95D797AE6E8C3A68E9839F4E8A3A78E3EBC7CE4A147CC1C7134A419B1D6C1A9A0C94A8673F42F504B0304140000000800F9AA1A572E26EF8DF704000051170000130000005445535430312F5445535430312E6A72786D6CBD985973DA3010C79FCBA7503D7DEA8C8D2F7C64209D04C83504DAD8699A7632A96CCBA0D4576C39403AFDEE956CCE346903141EE2B1E4DDDF6AFF5AC768EB1F4661001E509AE1386A7092207200456EECE1A8DFE02EED23DEE03EEC57EA6F791E34530409F2C010930138835942BD629F008BE41E8EA710A009922E88828F2318803CA3A089F1054AE29464A0839D14A6E3270EBCE6998A642AB22A799EE1BAAAACC926920CC33155DF44A2E3D6745946BE0700CFD315DD2D30014D22CA1ADC809064AF5A2D1FA56538218BF3D4457E9CF6911021B2FC942B5DF746199EB90F874361A808D4A12A8BA254FD72DEB1DC010A218FA38CC0C8451CA0F67B5931D9895D48681AEB4407AF721965DE928540273810C1103538BB6DD9A2C48104F6D115F6C8A0C1D5CC5A393E41B83F200DCE50650EB8719087D1D4A4464D02E4937398F6315DBA4CB73DA5D64B13244E16874E4C481C2ECEE439F61A1CAC990692951AAF2A9ACFAB7A4DE21DCFD37845771443829A69AA2AB75F79534FD298E640C693A5BB7128DCCDAA48C88A2A123C48A090DD0702814E80E8FE3CC02067D65741E8D99F72EFF8CC7795E0F1B4391C752DF6370A7BC7E7EAD7565FEA59D28F6E2B1874EC0BFC35BC26D7615BEA6271DC7D3C0CBBC76777BD565FEECA973A575D6D391EF2611E10760F3D981094CE9645E5C0B075C807B40A820917A614488D26E0DE698BCA1FC08C16E81D7C804200A3BE609194BE194C9737F509FF3343B647498A32F65EECD7DF7E6BB60EEC836F1C7773B35FAF3E6FC642566731D9E83E47E9B8E403162B877DBA0CEB53A78836A35AED4EBB6983F795A38BDE39B8C77EAE0AC4B9CDC659E5EAA47DD10695374BB3C2779AC977D000EF3EFEA477BF6E6E8AD00BD1D8D8C728F05E9DF9ABF6A0400ACCA2947D42AEAE4A08A08382CD10244548482019CC30A536135261D442999BE2842C6D61B1814F1F539FC9E453E5AC6BEB766BE231F83AC917FA6D4CD99584DD83F3F6960464E8D5132FE4DB90B12BF14E7A96BD25F1187AF5C40BF13664EC4ABC66AF6BB7BFD8B71F0FEC932D89B8186275210A31FF136B57A29E5AB79D5EF3A0B32541A7F8D50528C4FC0F9C9D0949F77B5B2252F4EA8917026EC8D89578CD4BAB7DB1B50F73415FE79B5A48B83966672A52A7A56F73F98BDFC621A2E7AB30D950454A5FE3D35A68B82964570A5E6EB50E2FD72DA042C3CD313B5371AB7578B95E09151A6E0AD9A6820E747FF4D3388FD8880D230F644980893D4ED8B980C626EEA03C0B57978CEB049300CDBD069306856E72CF119821F54123723489CDC6650FA41DA01045B4F1D3E06823625C5C87D3E606BD9FA3A76D0A43531CE8689087BAAAF1AA8964DE8086CE9B86E8F8C85755D7578B454F82CE42B0FB8300F72336A2FF19E815A5E5E29894313561976E79704329F66936F8910E646D06AC2E10CBA97962CF1DF9DF1DFD2C0F58BF8A1D79CEB632E1CEE529F42E95AE4EA5AE979D20E8A1F44FE1951AF7F2D62D39D6CBFED14B204DFA0B68D995B53B08C4C19F1049AE6D5E07D2422168F47E0697E60D2BC5F474B766D03695442B019A226FF88EC4BB8AA4CBA28FA028CB65D0D7BE745992D2ECB201424470E328422E7B6D4E5BB3B74F7390A67ABA4F03E932AF1AB24BEF3489D790A87822D435DF85F35A594A6DBD8A9C1520C0D9611CD0AC499AA34DCA911D58D72DC672C7E7657414C7E4B932526BFF2CA3D2755AD82F816AEA3F0A7B8EC9F230A4DDE8671623FF8531F3AA57171BD1FB95DF504B01023F001400000000004DAB1A570000000000000000000000000700240000000000000010000000000000005445535430312F0A00200000000000010018001C56D1E920D8D9011C56D1E920D8D901659BDCE720D8D901504B01023F0014000000080020AB1A578FDF3D9426130000B54500001400240000000000000020000000250000005445535430312F5445535430312E6A61737065720A0020000000000001001800CEA52AB620D8D9011C56D1E920D8D9011C56D1E920D8D901504B01023F00140000000800F9AA1A572E26EF8DF70400005117000013002400000000000000200000007D1300005445535430312F5445535430312E6A72786D6C0A00200000000000010018005AF0698C20D8D9011C56D1E920D8D9011C56D1E920D8D901504B0506000000000300030024010000A51800000000,'aaa2222333','admin','2024-01-23 10:34:40','admin','2024-01-23 10:50:22');
/*!40000 ALTER TABLE `tb_sys_jreport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_jreport_param`
--

DROP TABLE IF EXISTS `tb_sys_jreport_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_jreport_param` (
  `OID` char(36) NOT NULL,
  `REPORT_ID` varchar(50) NOT NULL,
  `URL_PARAM` varchar(100) NOT NULL,
  `RPT_PARAM` varchar(100) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`REPORT_ID`,`RPT_PARAM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_jreport_param`
--

LOCK TABLES `tb_sys_jreport_param` WRITE;
/*!40000 ALTER TABLE `tb_sys_jreport_param` DISABLE KEYS */;
INSERT INTO `tb_sys_jreport_param` VALUES ('f4840904-b9a5-11ee-a0c4-3b25c1760c68','TEST01','oid','OID','admin','2024-01-23 12:14:51',NULL,NULL);
/*!40000 ALTER TABLE `tb_sys_jreport_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_login_log`
--

DROP TABLE IF EXISTS `tb_sys_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_login_log` (
  `OID` char(36) NOT NULL,
  `USER` varchar(24) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`USER`),
  KEY `IDX_2` (`CDATE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_login_log`
--

LOCK TABLES `tb_sys_login_log` WRITE;
/*!40000 ALTER TABLE `tb_sys_login_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_login_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_mail_helper`
--

DROP TABLE IF EXISTS `tb_sys_mail_helper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_mail_helper` (
  `OID` char(36) NOT NULL,
  `MAIL_ID` varchar(17) NOT NULL,
  `SUBJECT` varchar(200) NOT NULL,
  `TEXT` blob DEFAULT NULL,
  `MAIL_FROM` varchar(100) NOT NULL,
  `MAIL_TO` varchar(100) NOT NULL,
  `MAIL_CC` varchar(1000) DEFAULT NULL,
  `MAIL_BCC` varchar(1000) DEFAULT NULL,
  `SUCCESS_FLAG` varchar(1) NOT NULL DEFAULT 'N',
  `SUCCESS_TIME` datetime DEFAULT NULL,
  `RETAIN_FLAG` varchar(1) NOT NULL DEFAULT 'N',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`MAIL_ID`),
  KEY `IDX_1` (`MAIL_ID`),
  KEY `IDX_2` (`SUCCESS_FLAG`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_mail_helper`
--

LOCK TABLES `tb_sys_mail_helper` WRITE;
/*!40000 ALTER TABLE `tb_sys_mail_helper` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_mail_helper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_menu`
--

DROP TABLE IF EXISTS `tb_sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_menu` (
  `OID` char(36) NOT NULL,
  `PROG_ID` varchar(50) NOT NULL,
  `PARENT_OID` char(36) NOT NULL,
  `ENABLE_FLAG` varchar(1) NOT NULL DEFAULT 'Y',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`PROG_ID`,`PARENT_OID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_menu`
--

LOCK TABLES `tb_sys_menu` WRITE;
/*!40000 ALTER TABLE `tb_sys_menu` DISABLE KEYS */;
INSERT INTO `tb_sys_menu` VALUES ('122721b3-b876-11ee-ad91-67276b113f6d','CORE_PROG004D0001Q','5e055f61-bfc5-402c-93b4-f241dc17b00b','Y','admin','2024-01-21 23:59:34',NULL,NULL),('12276fd4-b876-11ee-ad91-e77b0e0410dd','CORE_PROG004D0002Q','5e055f61-bfc5-402c-93b4-f241dc17b00b','Y','admin','2024-01-21 23:59:34',NULL,NULL),('4bd4d202-5feb-495b-8c8c-ec6b7f5b8041','CORE_PROG002D0002Q','79e1cf24-2522-4cdf-abcc-6455b47d545b','Y','admin','2017-05-10 14:20:12',NULL,NULL),('5e055f61-bfc5-402c-93b4-f241dc17b00b','CORE_PROG004D','00000000-0000-0000-0000-000000000000','Y','admin','2017-06-03 14:23:17',NULL,NULL),('79e1cf24-2522-4cdf-abcc-6455b47d545b','CORE_PROG002D','00000000-0000-0000-0000-000000000000','Y','admin','2017-05-08 21:32:59',NULL,NULL),('7ea68636-c93a-4669-ac42-dafc3770d20d','CORE_PROG001D','00000000-0000-0000-0000-000000000000','Y','admin','2017-04-20 11:24:53',NULL,NULL),('9972c249-2985-49ac-9b8b-f6c25c65fd4e','CORE_PROG002D0003Q','79e1cf24-2522-4cdf-abcc-6455b47d545b','Y','admin','2017-05-10 14:20:12',NULL,NULL),('b192c6b1-a480-11ef-b0a1-a14f8ea486f4','ORRS001D0001Q','f9b02a8d-8af4-11ef-92ef-b305036c7452','Y','admin','2024-11-17 09:10:11',NULL,NULL),('b194e992-a480-11ef-b0a1-d5e10cd3b008','ORRS001D0002Q','f9b02a8d-8af4-11ef-92ef-b305036c7452','Y','admin','2024-11-17 09:10:11',NULL,NULL),('b195ace3-a480-11ef-b0a1-d561b221528d','ORRS001D0003Q','f9b02a8d-8af4-11ef-92ef-b305036c7452','Y','admin','2024-11-17 09:10:11',NULL,NULL),('b1967034-a480-11ef-b0a1-733752260429','ORRS001D0004Q','f9b02a8d-8af4-11ef-92ef-b305036c7452','Y','admin','2024-11-17 09:10:11',NULL,NULL),('c5349a26-6d6e-4d94-b817-82be6d14d5ed','CORE_PROG002D0001Q','79e1cf24-2522-4cdf-abcc-6455b47d545b','Y','admin','2017-05-10 14:20:12',NULL,NULL),('f0242c17-4487-11ee-b50d-a593cf4a05bf','CORE_PROG001D0001Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2023-08-27 11:15:13',NULL,NULL),('f0253d88-4487-11ee-b50d-7f3d9b9812d0','CORE_PROG001D0002Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2023-08-27 11:15:13',NULL,NULL),('f0264ef9-4487-11ee-b50d-a55549dc8acf','CORE_PROG001D0003Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2023-08-27 11:15:13',NULL,NULL),('f027877a-4487-11ee-b50d-8fe1228e511a','CORE_PROG001D0004Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2023-08-27 11:15:13',NULL,NULL),('f02898eb-4487-11ee-b50d-45ee94442a45','CORE_PROG001D0005Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2023-08-27 11:15:13',NULL,NULL),('f9b02a8d-8af4-11ef-92ef-b305036c7452','ORRS001D','00000000-0000-0000-0000-000000000000','Y','admin','2024-10-15 20:57:03',NULL,NULL);
/*!40000 ALTER TABLE `tb_sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_menu_role`
--

DROP TABLE IF EXISTS `tb_sys_menu_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_menu_role` (
  `OID` char(36) NOT NULL,
  `PROG_ID` varchar(50) NOT NULL,
  `ROLE` varchar(50) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`PROG_ID`,`ROLE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_menu_role`
--

LOCK TABLES `tb_sys_menu_role` WRITE;
/*!40000 ALTER TABLE `tb_sys_menu_role` DISABLE KEYS */;
INSERT INTO `tb_sys_menu_role` VALUES ('7147502a-4abc-11ee-9380-f7f86272e6b6','CORE_PROG001D0004Q','COMMON01','admin','2023-09-04 08:46:10',NULL,NULL),('72a61cdd-4abc-11ee-9380-19ab31a4dc18','CORE_PROG001D','COMMON01','admin','2023-09-04 08:46:13',NULL,NULL),('81f84f17-b9e4-11ee-ae49-63eb5d9c550a','CORE_PROG004D','COMMON01','admin','2024-01-23 19:42:37',NULL,NULL),('924e2206-b9e4-11ee-ae49-efea48f6c66a','CORE_PROG004D0001Q','COMMON01','admin','2024-01-23 19:43:05',NULL,NULL),('924e2207-b9e4-11ee-ae49-d35121d1905d','CORE_PROG004D0001Q','admin','admin','2024-01-23 19:43:05',NULL,NULL);
/*!40000 ALTER TABLE `tb_sys_menu_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_prog`
--

DROP TABLE IF EXISTS `tb_sys_prog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_prog` (
  `OID` char(36) NOT NULL,
  `PROG_ID` varchar(50) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `URL` varchar(255) NOT NULL,
  `EDIT_MODE` varchar(1) NOT NULL DEFAULT 'N',
  `IS_DIALOG` varchar(1) NOT NULL DEFAULT 'N',
  `DIALOG_W` int(4) NOT NULL DEFAULT 0,
  `DIALOG_H` int(4) NOT NULL DEFAULT 0,
  `PROG_SYSTEM` varchar(10) NOT NULL,
  `ITEM_TYPE` varchar(10) NOT NULL,
  `ICON` varchar(20) NOT NULL,
  `FONT_ICON_CLASS_ID` varchar(100) NOT NULL DEFAULT 'circle-o',
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`PROG_ID`),
  KEY `IDX_1` (`PROG_SYSTEM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_prog`
--

LOCK TABLES `tb_sys_prog` WRITE;
/*!40000 ALTER TABLE `tb_sys_prog` DISABLE KEYS */;
INSERT INTO `tb_sys_prog` VALUES ('045c79fd-9728-11ef-af3f-65d7c66b9839','ORRS001D0003Q','AA03 - 任務結果','#/orrs001d0003','N','N',0,0,'CORE','ITEM','SYSTEM','clipboard2-check-fill','admin','2024-10-31 09:32:39',NULL,NULL),('0aa817d1-8afa-11ef-92ef-5bfccd6233c0','ORRS001D0001Q','AA01 - 命令','#/orrs001d0001','N','N',0,0,'CORE','ITEM','SYSTEM','cpu','admin','2024-10-15 21:33:19','admin','2024-10-19 22:31:36'),('186b1fb1-749f-4b6f-97d1-6b7fb8115345','CORE_PROG001D0004E','ZA04 - Freemarker樣板 (Edit)','#/prog001d0004/edit','Y','N',0,0,'CORE','ITEM','TEMPLATE','file-text','admin','2017-05-12 10:40:10','admin','2023-08-16 21:48:56'),('1b11c7eb-6133-48fb-87f0-dfbd098ce914','CORE_PROG001D0001E','ZA01 - System site (Edit)','#/prog001d0001/edit','Y','N',0,0,'CORE','ITEM','COMPUTER','globe2','admin','2014-10-02 00:00:00','admin','2021-01-20 08:20:58'),('1e393fe3-8bbc-482c-aa23-bbb22a1dbafb','CORE_PROG001D0005A','ZA05 - JasperReport (Create)','#/prog001d0005/create','N','N',0,0,'CORE','ITEM','APPLICATION_PDF','file-pdf','admin','2017-05-18 09:55:46','admin','2023-08-24 20:20:27'),('22560527-90fb-4e5a-a89b-353d2aa1d433','CORE_PROG001D0005E','ZA05 - JasperReport (Edit)','#/prog001d0005/edit','Y','N',0,0,'CORE','ITEM','APPLICATION_PDF','file-pdf','admin','2017-05-18 09:56:27','admin','2023-08-24 20:20:40'),('3630ee1b-6169-452f-821f-5c015dfb84d5','CORE_PROG001D','ZA. Config','/','N','N',0,0,'CORE','FOLDER','PROPERTIES','gear-fill','admin','2014-10-02 00:00:00','admin','2023-08-15 19:16:31'),('3862b6d0-0551-45d8-8dd1-cd988a5e8e50','CORE_PROG004D0002Q','ZD02 - Token log','#/prog004d0002','N','N',0,0,'CORE','ITEM','PROPERTIES','clipboard-check','admin','2017-06-03 14:22:29','admin','2024-01-25 07:47:53'),('41fa29d8-3a53-4fbd-b2b1-cdbfd0729767','CORE_PROG001D0004Q','ZA04 - Freemarker樣板','#/prog001d0004','N','N',0,0,'CORE','ITEM','TEMPLATE','file-text','admin','2017-05-12 10:36:41','admin','2023-08-16 21:48:29'),('5e082c7c-1730-4176-89c6-93e235707deb','CORE_PROG002D0001A','ZB01 - Role (Create)','#/prog002d0001/create','N','N',0,0,'CORE','ITEM','PEOPLE','person-square','admin','2017-05-09 11:15:50','admin','2023-08-27 16:46:40'),('61aea7ff-7a42-4a92-9a0b-4a0dfe60858b','CORE_PROG004D0001Q','ZD01 - Event log','#/prog004d0001','N','N',0,0,'CORE','ITEM','PROPERTIES','clipboard-pulse','admin','2017-06-03 14:22:07','admin','2023-08-29 10:17:34'),('6a442973-0e0c-4a7a-d546-464f4ff5f7a9','CORE_PROG001D0003Q','ZA03 - Menu settings','#/prog001d0003','N','N',0,0,'CORE','ITEM','FOLDER','menu-down','admin','2014-10-02 00:00:00','admin','2023-08-15 19:21:23'),('6b1a27ac-a480-11ef-980f-8be3b04dd41b','ORRS001D0004Q','AA04 - 檢索文件','#/orrs001d0004','N','N',0,0,'CORE','ITEM','SYSTEM','filetype-txt','admin','2024-11-17 09:08:13',NULL,NULL),('6b210525-8975-4fb5-954c-fe349f66d3fe','CORE_PROG002D0001S01Q','ZB01 - Role (permission)','#/prog002d0001/setparam','Y','N',0,0,'CORE','ITEM','IMPORTANT','globe2','admin','2017-05-09 14:32:47','admin','2024-02-10 20:17:33'),('72e6e0d1-1818-47d3-99f9-5134fb211b79','CORE_PROG002D','ZB. Role authority','/','N','N',0,0,'CORE','FOLDER','SHARED','person-square','admin','2017-05-08 21:27:52','admin','2023-08-27 16:47:03'),('7746f746-961f-44c2-9b66-fa43c0f49838','CORE_PROG001D0004S01Q','ZA04 - Freemarker樣板 (Parameter)','#/prog001d0004/setparam','Y','N',0,0,'CORE','ITEM','TEMPLATE','file-text','admin','2017-05-12 10:42:04','admin','2023-08-16 21:49:12'),('7d9ddc45-3eab-4f61-8c0a-d5505c0cc748','CORE_PROG001D0004A','ZA04 - Freemarker樣板 (Create)','#/prog001d0004/create','N','N',0,0,'CORE','ITEM','TEMPLATE','file-text','admin','2017-05-12 10:39:20','admin','2023-08-16 21:48:49'),('8499957e-6da9-4160-c2ec-dfb7dbc202fe','CORE_PROG001D0002E','ZA02 - Program (Edit)','#/prog001d0002/edit','Y','N',0,0,'CORE','ITEM','G_APP_INSTALL','filetype-html','admin','2014-10-02 00:00:00','admin','2023-08-15 19:19:17'),('8643db14-8e26-11ef-9028-771f90e53e59','ORRS001D0002Q','AA02 - 任務','#/orrs001d0002','N','N',0,0,'CORE','ITEM','SYSTEM','x-diamond-fill','admin','2024-10-19 22:29:18',NULL,NULL),('95d789da-8c8e-11ef-b80d-6f8dd1830e29','ORRS001D0001A','AA01 - 命令 (Create)','#/orrs001d0001/create','N','N',0,0,'CORE','ITEM','SYSTEM','cpu','admin','2024-10-17 21:49:09',NULL,NULL),('97012256-8e26-11ef-9028-bb15b679bda0','ORRS001D0002A','AA02 - 任務 (Create)','#/orrs001d0002/create','N','N',0,0,'CORE','ITEM','SYSTEM','x-diamond-fill','admin','2024-10-19 22:29:46',NULL,NULL),('9f60e0c8-a480-11ef-b0a1-c93187b52991','ORRS001D0004A','AA04 - 檢索文件 (Create)','#/orrs001d0004/create','N','N',0,0,'CORE','ITEM','SYSTEM','filetype-txt','admin','2024-11-17 09:09:40',NULL,NULL),('a7bfc928-8e26-11ef-9028-995dacc25c2c','ORRS001D0002E','AA02 - 任務 (Edit)','#/orrs001d0002/edit','Y','N',0,0,'CORE','ITEM','SYSTEM','x-diamond-fill','admin','2024-10-19 22:30:14',NULL,NULL),('aba3755a-a480-11ef-b0a1-a3a65648f212','ORRS001D0004E','AA04 - 檢索文件 (Edit)','#/orrs001d0004/edit','N','N',0,0,'CORE','ITEM','SYSTEM','filetype-txt','admin','2024-11-17 09:10:01',NULL,NULL),('ac5bcfd0-4abd-11e4-916c-0800200c9a66','CORE_PROG001D0001A','ZA01 - System site (Create)','#/prog001d0001/create','N','N',0,0,'CORE','ITEM','COMPUTER','globe2','admin','2014-10-02 00:00:00','admin','2021-01-20 08:20:45'),('b39159ad-0707-4515-b78d-e3fc72c53974','CORE_PROG002D0001E','ZB01 - Role (Edit)','#/prog002d0001/edit','Y','N',0,0,'CORE','ITEM','PEOPLE','person-square','admin','2017-05-09 12:11:53','admin','2023-08-27 16:46:35'),('b6b89559-6864-46ab-9ca9-0992dcf238f1','CORE_PROG001D0001Q','ZA01 - System site','#/prog001d0001','N','N',0,0,'CORE','ITEM','COMPUTER','globe2','admin','2014-10-02 00:00:00','admin','2021-01-20 08:20:29'),('b978f706-4c5f-40f8-83b1-395492f141d4','CORE_PROG002D0001Q','ZB01 - Role','#/prog002d0001','N','N',0,0,'CORE','ITEM','PEOPLE','person-square','admin','2017-05-08 21:32:50','admin','2023-08-27 16:46:27'),('c1467c36-8af4-11ef-92ef-ffd759a8a480','ORRS001D','AA. 本地llm工作','/','N','N',0,0,'CORE','FOLDER','SYSTEM','send','admin','2024-10-15 20:55:29','admin','2024-10-15 21:35:05'),('c96ebde8-7044-4b05-a155-68a0c2605619','CORE_PROG002D0003Q','ZB03 - Role for menu','#/prog002d0003','N','N',0,0,'CORE','ITEM','FOLDER','menu-app-fill','admin','2017-05-08 21:37:01','admin','2024-07-05 23:12:41'),('cd5629af-8e18-11ef-ad17-6b820d47cc0f','ORRS001D0001E','AA01 - 命令 (Edit)','#/orrs001d0001/edit','Y','N',0,0,'CORE','ITEM','SYSTEM','cpu','admin','2024-10-19 20:51:04',NULL,NULL),('da7d969a-5efb-4e84-9eab-4fdae236f28c','CORE_PROG002D0002Q','ZB02 - User role','#/prog002d0002','N','N',0,0,'CORE','ITEM','PERSON','person-check','admin','2017-05-08 21:34:39','admin','2023-08-28 19:54:25'),('dda67b1d-e3a2-4534-835a-c62d9e8421f3','CORE_PROG001D0005S01Q','ZA05 - JasperReport (Parameter)','#/prog001d0005/setparam','Y','N',0,0,'CORE','ITEM','APPLICATION_PDF','file-pdf','admin','2017-05-18 09:57:26','admin','2023-08-24 20:21:02'),('df447a07-974c-11ef-840a-9fcc1b9ef968','ORRS001D0003E','AA03 - 任務結果(執行紀錄檢視)','#/orrs001d0003/edit','N','N',0,0,'CORE','ITEM','SYSTEM','clipboard2-check-fill','admin','2024-10-31 13:56:29',NULL,NULL),('e32b9329-bb38-46d7-8552-2307bac77724','CORE_PROG001D0002A','ZA02 - Program (Create)','#/prog001d0002/create','N','N',0,0,'CORE','ITEM','G_APP_INSTALL','filetype-html','admin','2014-10-02 00:00:00','admin','2023-08-15 19:19:42'),('e86dbb1b-6870-4827-8039-72f5e15fa4f2','CORE_PROG004D','ZD. Log','/','N','N',0,0,'CORE','FOLDER','PROPERTIES','clipboard-check-fill','admin','2017-06-03 14:21:03','admin','2024-03-02 15:10:42'),('eb6e199f-c853-4fbf-acf3-0c9c77ba9953','CORE_PROG001D0002Q','ZA02 - Program','#/prog001d0002','N','N',0,0,'CORE','ITEM','G_APP_INSTALL','filetype-html','admin','2014-10-02 00:00:00','admin','2023-08-15 19:19:05'),('eb786ffd-c7d1-4631-aed2-4d9d7368eb13','CORE_PROG001D0005Q','ZA05 - JasperReport','#/prog001d0005','N','N',0,0,'CORE','ITEM','APPLICATION_PDF','file-pdf','admin','2017-05-18 09:54:35','admin','2023-08-24 20:20:16');
/*!40000 ALTER TABLE `tb_sys_prog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_qfield_log`
--

DROP TABLE IF EXISTS `tb_sys_qfield_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_qfield_log` (
  `OID` char(36) NOT NULL,
  `SYSTEM` varchar(10) NOT NULL,
  `PROG_ID` varchar(50) NOT NULL,
  `METHOD_NAME` varchar(255) NOT NULL,
  `FIELD_NAME` varchar(255) NOT NULL,
  `FIELD_VALUE` varchar(500) DEFAULT NULL,
  `QUERY_USER_ID` varchar(24) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`SYSTEM`,`PROG_ID`),
  KEY `IDX_2` (`QUERY_USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_qfield_log`
--

LOCK TABLES `tb_sys_qfield_log` WRITE;
/*!40000 ALTER TABLE `tb_sys_qfield_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_qfield_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_template`
--

DROP TABLE IF EXISTS `tb_sys_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_template` (
  `OID` char(36) NOT NULL,
  `TEMPLATE_ID` varchar(10) NOT NULL,
  `TITLE` varchar(200) NOT NULL,
  `MESSAGE` varchar(4000) NOT NULL,
  `DESCRIPTION` varchar(200) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`TEMPLATE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_template`
--

LOCK TABLES `tb_sys_template` WRITE;
/*!40000 ALTER TABLE `tb_sys_template` DISABLE KEYS */;
INSERT INTO `tb_sys_template` VALUES ('467f92b0-d5fa-11ee-9ec4-c75e54f9ca1d','TPL01','${title}','<h1>Product:${name}</h1>','','admin','2024-02-28 13:28:59',NULL,NULL);
/*!40000 ALTER TABLE `tb_sys_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_template_param`
--

DROP TABLE IF EXISTS `tb_sys_template_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_template_param` (
  `OID` char(36) NOT NULL,
  `TEMPLATE_ID` varchar(10) NOT NULL,
  `IS_TITLE` varchar(1) NOT NULL DEFAULT 'N',
  `TEMPLATE_VAR` varchar(100) NOT NULL,
  `OBJECT_VAR` varchar(100) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`TEMPLATE_ID`,`TEMPLATE_VAR`,`IS_TITLE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_template_param`
--

LOCK TABLES `tb_sys_template_param` WRITE;
/*!40000 ALTER TABLE `tb_sys_template_param` DISABLE KEYS */;
INSERT INTO `tb_sys_template_param` VALUES ('6ff19e9d-d5fa-11ee-9ec4-c330a97e3ff9','TPL01','Y','title','title','admin','2024-02-28 13:30:09',NULL,NULL);
/*!40000 ALTER TABLE `tb_sys_template_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_token`
--

DROP TABLE IF EXISTS `tb_sys_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_token` (
  `OID` char(36) NOT NULL,
  `USER_ID` varchar(24) NOT NULL,
  `TOKEN` varchar(2048) NOT NULL,
  `EXPIRES_DATE` datetime NOT NULL,
  `RF_EXPIRES_DATE` datetime NOT NULL,
  `CDATE` datetime NOT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`USER_ID`),
  KEY `IDX_2` (`TOKEN`(1024))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_token`
--

LOCK TABLES `tb_sys_token` WRITE;
/*!40000 ALTER TABLE `tb_sys_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_upload`
--

DROP TABLE IF EXISTS `tb_sys_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_upload` (
  `OID` char(36) NOT NULL,
  `SYSTEM` varchar(10) NOT NULL,
  `SUB_DIR` varchar(4) NOT NULL,
  `TYPE` varchar(10) NOT NULL,
  `FILE_NAME` varchar(50) NOT NULL,
  `SHOW_NAME` varchar(255) NOT NULL,
  `IS_FILE` varchar(1) NOT NULL DEFAULT 'Y',
  `CONTENT` mediumblob DEFAULT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  KEY `IDX_1` (`SYSTEM`,`TYPE`,`SUB_DIR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_upload`
--

LOCK TABLES `tb_sys_upload` WRITE;
/*!40000 ALTER TABLE `tb_sys_upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_upload` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_usess`
--

DROP TABLE IF EXISTS `tb_sys_usess`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_usess` (
  `OID` char(36) NOT NULL,
  `SESSION_ID` varchar(64) NOT NULL,
  `ACCOUNT` varchar(24) NOT NULL,
  `CURRENT_ID` varchar(36) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`,`SESSION_ID`),
  UNIQUE KEY `UK_1` (`ACCOUNT`,`SESSION_ID`),
  KEY `IDX_1` (`CURRENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_usess`
--

LOCK TABLES `tb_sys_usess` WRITE;
/*!40000 ALTER TABLE `tb_sys_usess` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_usess` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_role`
--

DROP TABLE IF EXISTS `tb_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_user_role` (
  `OID` char(36) NOT NULL,
  `ROLE` varchar(50) NOT NULL,
  `ACCOUNT` varchar(24) NOT NULL,
  `DESCRIPTION` varchar(500) NOT NULL,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(50) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`ROLE`,`ACCOUNT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_role`
--

LOCK TABLES `tb_user_role` WRITE;
/*!40000 ALTER TABLE `tb_user_role` DISABLE KEYS */;
INSERT INTO `tb_user_role` VALUES ('1f37136f-b9e0-11ee-ae49-8d2df8636cc7','COMMON01','tiffany','','admin','2024-01-23 19:11:14',NULL,NULL),('31102b06-b9e0-11ee-ae49-9b67c6a3ca84','COMMON01','tester','','admin','2024-01-23 19:11:44',NULL,NULL),('9243c7de-43b1-46ef-ac4b-2620697f319e','admin','admin','Administrator','admin','2014-09-23 00:00:00',NULL,NULL),('a02e1b62-4988-11ee-8b9a-098c887743fd','COMMON01','steven','','admin','2023-09-02 20:02:44',NULL,NULL);
/*!40000 ALTER TABLE `tb_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-17 12:42:10
