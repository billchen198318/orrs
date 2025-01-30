/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.1-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: orrs
-- ------------------------------------------------------
-- Server version	11.7.1-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_account`
--

LOCK TABLES `tb_account` WRITE;
/*!40000 ALTER TABLE `tb_account` DISABLE KEYS */;
INSERT INTO `tb_account` VALUES
('0','admin','$2y$12$Q4x02Q0WKHWXAQ.NoGCs8ObX4sac890xeRnaNUxNnz/VEiHWazIp.','Y','admin','2012-11-11 10:56:23','admin','2014-04-19 11:32:04'),
('15822da5-25dc-490c-bdfb-be75f5ff4843','tester','$2y$12$Q4x02Q0WKHWXAQ.NoGCs8ObX4sac890xeRnaNUxNnz/VEiHWazIp.','Y','admin','2015-04-23 11:26:53','admin','2015-08-29 17:54:08'),
('52cb274e-388d-419f-a81e-67ca599bfb63','steven','$2y$12$Q4x02Q0WKHWXAQ.NoGCs8ObX4sac890xeRnaNUxNnz/VEiHWazIp.','Y','admin','2015-09-11 10:33:53',NULL,NULL),
('9c239d19-3646-41db-b394-d34c5bf34671','tiffany','$2y$12$Q4x02Q0WKHWXAQ.NoGCs8ObX4sac890xeRnaNUxNnz/VEiHWazIp.','Y','admin','2015-09-11 10:15:29',NULL,NULL);
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
  `DOC_RETRIEVAL` varchar(1) NOT NULL DEFAULT 'N',
  `SIM_THRESHOLD` decimal(3,2) NOT NULL DEFAULT 1.00,
  `CUSERID` varchar(24) NOT NULL,
  `CDATE` datetime NOT NULL,
  `UUSERID` varchar(24) DEFAULT NULL,
  `UDATE` datetime DEFAULT NULL,
  PRIMARY KEY (`OID`),
  UNIQUE KEY `UK_1` (`CMD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_orrs_command`
--

LOCK TABLES `tb_orrs_command` WRITE;
/*!40000 ALTER TABLE `tb_orrs_command` DISABLE KEYS */;
INSERT INTO `tb_orrs_command` VALUES
('2f2b7190-b636-11ef-a8f7-5fe83f07214d','TEST123','立法委員質詢汙水問題','','提出5組關於汙水相關問題提問','','TEXT','llama3.2-vision','N','Y',0.00,'admin','2024-12-09 22:02:10','admin','2024-12-09 22:37:18'),
('974b7fff-9aaa-11ef-b609-a1eae7d5353c','PP01','取得水量拆分檔內容','','### 資料庫資訊\n1. sql server 帳戶 sa 密碼 P@ssw0rd@@@@\n2. sql server IP位置 127.0.0.1 , 服務 port 號是 1433\n3. database file is: NSC_B21\n\n### 資料表資訊\n1. 資料表 WATER_QUANTITY_SPLIT\n2. 資料表 WATER_QUANTITY_SPLIT欄位 [W_ID, QUANTITY, TOTAL_AMOUNT]\n3. 資料表 WATER_QUANTITY_SPLIT欄位型態 [W_ID(int), QUANTITY(decimal), TOTAL_AMOUNT(decimal)]\n4. 資料表 COMPANY 欄位[F_NAMEC] , COMPANY資料表欄位型態 [ F_NAMEC(varchar2)]\n5. 資料表 WATER_SOURCES_HISTORY 欄位[ WATER_SOURCES_ID ] , WATER_SOURCES_HISTORY 資料表欄位型態 [ WATER_SOURCES_ID(varchar2) ]\n\n### 資料表關聯關係\n1. 資料表關聯關係 \n	WATER_QUANTITY_SPLIT.FACTORY_WATER_SOURCES_ID1 = FACTORY_WATER_SOURCES.OID \n	FACTORY_WATER_SOURCES.FACTORY_ID = FACTORY_HISTORY.OID\n	COMPANY.OID = FACTORY_HISTORY.COMPANY_OID\n	WATER_SOURCES_HISTORY.OID = FACTORY_WATER_SOURCES.WATER_SOURCES_ID\n\n### 最後步驟產生groovy規範\n1. 用 groovy 產出連線資料庫, 資料表要串聯關係, \n	並取出資料 WATER_QUANTITY_SPLIT,COMPANY,WATER_SOURCES_HISTORY 這3個資料表定義的欄位 前10筆(如: select top 10 * from ....) \n	條件: \n		a. WATER_QUANTITY_SPLIT.CHARGE_SDATE 大於等於2023年2月1日 , 且 WATER_QUANTITY_SPLIT.CHARGE_EDATE 小於等於2023年4月30日\n		b. FACTORY_HISTORY.BASE_ID 為字串 \'01\'\n		c. 以QUANTITY desc排序\n2. 建議需要 import java.sql.*\n3. jdbc driver class 為 com.microsoft.sqlserver.jdbc.SQLServerDriver 所以JDBC url 開頭因該為 jdbc:sqlserver://\n4. 請一定要在 jdbc url 加上 encrypt=false; 參數\n5. 程式碼不要產在自訂義method中, 請用平舖直敘式方式產生程式碼\n6. 需要 import 的 package 或 class 請寫在程式最上方\n7. 將資料放入List<Map> 中\n8. 用 com.fasterxml.jackson 將 List<Map> 內容轉成字串 json資料, 配置到jsonResult變數\n9. 程式不需要增加 try catch 處理\n10. 最後用 return  jsonResult, 給我 groovy code','result','GROOVY','qwen2.5-coder','N','Y',0.75,'admin','2024-11-04 20:44:54','admin','2024-11-19 21:13:58'),
('a34e225a-9d17-11ef-a316-d9ccce9e80b5','PP02','產生echarts 圖表','','### json資料內容\n```json\n $P{previousInvokeResult}\n```\n### 產生html code條件\n1. 請將json資料, 使用 echarts 產生 pie, bar, line圖 (這3個圖表id必須不相同)\n2. 各項目標題為 json資料的 F_NAMEC 加上 \'(\' 符號 再加上 json資料 WATER_SOURCES_ID 在加上 \')\'符號\n3. 各項目的實際值為 json資料的 QUANTITY\n5. 給我 html code','','HTML','qwen2.5-coder','N','Y',0.70,'admin','2024-11-07 22:50:32','admin','2024-11-19 21:17:39'),
('e834c237-a679-11ef-b507-fb1cb0fe9c41','test001','測試文件檢索','','幫我介紹丧彪的職業, 丧彪是哪國人?','','TEXT','llama3.2-vision','N','Y',0.40,'admin','2024-11-19 21:26:38','admin','2024-11-19 21:36:19');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_orrs_command_prompt`
--

LOCK TABLES `tb_orrs_command_prompt` WRITE;
/*!40000 ALTER TABLE `tb_orrs_command_prompt` DISABLE KEYS */;
INSERT INTO `tb_orrs_command_prompt` VALUES
('1734171f-b63b-11ef-b3e4-e9c6c65f8577','TEST123',0,'以立法委員角度詢問問題','admin','2024-12-09 22:37:18',NULL,NULL),
('22c49788-a678-11ef-b507-9f86c2f12ada','PP01',0,'json library 使用的是 com.fasterxml.jackson','admin','2024-11-19 21:13:58',NULL,NULL),
('22c581e9-a678-11ef-b507-212d9c33a895','PP01',1,'回應訊息不需要給 implementation , maven, gradle 資訊','admin','2024-11-19 21:13:58',NULL,NULL),
('22c61e2a-a678-11ef-b507-15ae91167362','PP01',2,'你必須遵守: 不要把結果用 print 或 println 方式輸出, 請用 return 方式輸出值','admin','2024-11-19 21:13:58',NULL,NULL),
('a69ba815-a678-11ef-b507-836b5603e9d6','PP02',0,'echarts 版本請用 5.3.3 , CDN 位置 https://cdnjs.cloudflare.com/ajax/libs/echarts/5.3.3/echarts.min.js','admin','2024-11-19 21:17:39',NULL,NULL),
('a69bf636-a678-11ef-b507-ad45992901f9','PP02',1,'echarts 圖表寬度740px , 高度600px , echarts 圖表請配置 tooltip','admin','2024-11-19 21:17:39',NULL,NULL),
('a69c4457-a678-11ef-b507-8f8ccf12a11b','PP02',2,'這很重要必須遵守: json資料筆數不要刪減','admin','2024-11-19 21:17:39',NULL,NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_orrs_doc`
--

LOCK TABLES `tb_orrs_doc` WRITE;
/*!40000 ALTER TABLE `tb_orrs_doc` DISABLE KEYS */;
INSERT INTO `tb_orrs_doc` VALUES
('04031e07-a495-11ef-90f4-91f56f58a2c7','DOC0001','資料表參考內容','### 資料表欄位\n1. 資料表 WATER_QUANTITY_SPLIT \n2. 資料表 FACTORY_WATER_SOURCES \n3. 資料表 COMPANY \n4. 資料表 FACTORY_HISTORY \n\n### 資料表關係\n	WATER_QUANTITY_SPLIT.FACTORY_WATER_SOURCES_ID1 = FACTORY_WATER_SOURCES.OID \n	FACTORY_WATER_SOURCES.FACTORY_ID = FACTORY_HISTORY.OID\n	COMPANY.OID = FACTORY_HISTORY.COMPANY_OID\n	WATER_SOURCES_HISTORY.OID = FACTORY_WATER_SOURCES.WATER_SOURCES_ID\n	\n### SQL範例\n1. 範例1\n```sql\nselect * \nfrom \nWATER_QUANTITY_SPLIT wqs, FACTORY_WATER_SOURCES fws, COMPANY c, FACTORY_HISTORY fh, WATER_SOURCES_HISTORY wsh\nwhere\nwqs.FACTORY_WATER_SOURCES_ID1 = fws.OID \nand fws.FACTORY_ID = fh.OID\nand c.OID = fh.COMPANY_OID\nand wsh.OID = fws.WATER_SOURCES_ID\n```\n\n2. 範例2\n```sql\nselect top 10 wqs.W_ID, wqs.QUANTITY, wqs.TOTAL_AMOUNT, c.F_NAMEC, wsh.WATER_SOURCES_ID \nfrom WATER_QUANTITY_SPLIT wqs\njoin FACTORY_WATER_SOURCES fws on wqs.FACTORY_WATER_SOURCES_ID1 = fws.OID\njoin COMPANY c on c.OID = wqs.COMPANY_OID\njoin FACTORY_HISTORY fh on fws.FACTORY_ID = fh.OID\njoin WATER_SOURCES_HISTORY wsh on fws.WATER_SOURCES_ID = wsh.OID\nwhere wqs.CHARGE_SDATE >= \'2022-01-01\' and wqs.CHARGE_EDATE <= \'2023-12-30\'\nand fh.BASE_ID = \'01\'\norder by wqs.QUANTITY desc\n```   \n','如果與資料表有關係的話,請參考以下說明文件:\n{information}','information','admin','2024-11-17 11:35:39','admin','2024-11-17 15:42:04'),
('0911e2cf-a67b-11ef-b507-018449ec6e8e','DOC9999','印度司机丧彪','印度司机丧彪 , 印度大巴司机丧彪：\n直线加油谁不会？弯道快才是真的快！\n过得去就是坎，过不去就是命！\n只要不出啥意外，油门必须往死了踹！\n印度大巴司机有多疯狂，半路甩飞乘客实属正常！\n刹车片拆下来扔掉，胆小的乘客当场被吓尿！\n心不慌、手不抖，一车乘客全送走！\n到终点站，乘客少一半！\n大巴当成赛车开，这条路上我最跩！\n加速过弯真的帅，乘客飞出去也很快！\n乘客走得没有一点痛苦，嗖的一下就直达地府！\n一路不要命，全靠八字硬！\n油一踩，车一超，阎王面前走一遭！','如果與丧彪有關係, 請參考以下資料:\n{msg}','msg','admin','2024-11-19 21:34:43',NULL,NULL),
('66325dc5-a4b4-11ef-9d44-cb7452674910','DOC0002','參考的groovy程式','### groovy參考資料\n1. groovy參考程式碼1:\n```groovy\nimport java.sql.*\nimport com.fasterxml.jackson.databind.ObjectMapper\n\ndef connectionUrl = \'jdbc:sqlserver://127.0.0.1:1433;databaseName=DB_FILE_NAME;encrypt=false;\'\ndef username = \'我的database帳號\'\ndef password = \'我的database密碼\'\n\ndef connection = DriverManager.getConnection(connectionUrl, username, password)\n\ndef sqlQuery = \'\'\'\nselect top 10 wqs.W_ID, wqs.QUANTITY, wqs.TOTAL_AMOUNT, c.F_NAMEC, wsh.WATER_SOURCES_ID \nfrom WATER_QUANTITY_SPLIT wqs\njoin FACTORY_WATER_SOURCES fws on wqs.FACTORY_WATER_SOURCES_ID1 = fws.OID\njoin COMPANY c on c.OID = wqs.COMPANY_OID\njoin FACTORY_HISTORY fh on fws.FACTORY_ID = fh.OID\njoin WATER_SOURCES_HISTORY wsh on fws.WATER_SOURCES_ID = wsh.OID\nwhere wqs.CHARGE_SDATE >= \'2023-01-01\' and wqs.CHARGE_EDATE <= \'2023-12-30\'\nand fh.BASE_ID = \'01\'\norder by wqs.QUANTITY desc\n\'\'\'\n\ndef statement = connection.createStatement()\ndef resultSet = statement.executeQuery(sqlQuery)\n\ndef jsonResult = new ObjectMapper().writeValueAsString(resultSetToMapList(resultSet))\n\nconnection.close()\n\nreturn jsonResult\n\ndef resultSetToMapList(ResultSet resultSet) {\n    List<Map> result = []\n    while (resultSet.next()) {\n        Map<String, Object> row = [:]\n        ResultSetMetaData metaData = resultSet.getMetaData()\n        int columnCount = metaData.getColumnCount()\n        for (int i = 1; i <= columnCount; i++) {\n            row[metaData.getColumnName(i)] = resultSet.getObject(i)\n        }\n        result.add(row)\n    }\n    return result\n}\n```\n\n2. groovy參考程式碼2:\n```groovy\nimport java.sql.*\nimport com.fasterxml.jackson.databind.ObjectMapper\n\ndef connectionUrl = \'jdbc:sqlserver://127.0.0.1:1433;databaseName=DB_FILE_NAME;encrypt=false;\'\ndef username = \'我的database帳號\'\ndef password = \'我的database密碼\'\n\ndef connection = DriverManager.getConnection(connectionUrl, username, password)\n\ndef sqlQuery = \'\'\'\nselect top 10 wqs.W_ID, wqs.QUANTITY, wqs.TOTAL_AMOUNT, c.F_NAMEC, wsh.WATER_SOURCES_ID \nfrom WATER_QUANTITY_SPLIT wqs\njoin FACTORY_WATER_SOURCES fws on wqs.FACTORY_WATER_SOURCES_ID1 = fws.OID\njoin COMPANY c on c.OID = wqs.COMPANY_OID\njoin FACTORY_HISTORY fh on fws.FACTORY_ID = fh.OID\njoin WATER_SOURCES_HISTORY wsh on fws.WATER_SOURCES_ID = wsh.OID\nwhere wqs.CHARGE_SDATE >= \'2023-01-01\' and wqs.CHARGE_EDATE <= \'2023-12-30\'\nand fh.BASE_ID = \'01\'\norder by wqs.QUANTITY desc\n\'\'\'\n\ndef statement = connection.createStatement()\ndef resultSet = statement.executeQuery(sqlQuery)\n\nList<Map> result = []\nwhile (resultSet.next()) {\n	Map<String, Object> row = [:]\n	ResultSetMetaData metaData = resultSet.getMetaData()\n	int columnCount = metaData.getColumnCount()\n	for (int i = 1; i <= columnCount; i++) {\n		row[metaData.getColumnName(i)] = resultSet.getObject(i)\n	}\n    result.add(row)\n}\n\ndef jsonResult = new ObjectMapper().writeValueAsString(result)\n\nconnection.close()\n\nreturn jsonResult\n```\n','如果與 groovy 程式有關係的話,請參考以下說明文件:\n{information}','information','admin','2024-11-17 15:20:18',NULL,NULL),
('8d8107a2-b635-11ef-a8f7-21dc5b31ff23','AA001','竹科銅鑼園區初審過關 專管專排沒溝通 鄉長質疑太倉促','竹科銅鑼園區初審過關 專管專排沒溝通 鄉長質疑太倉促\n\n因應進駐廠商提高水電用量，新竹科學園區銅鑼園區提出環差變更，昨（16日）通過第三次環評初審。此案用水、用電需求大增，且取水於鯉魚潭水庫，環保團體呼籲應慎重評估為科學園區截水的必要性。銅鑼鄉長謝昌年則出席表示，開發單位沒來和居民協商就制定了污水專管排放計畫，相關規劃都還不完善，他無奈表示，昨日通過環評有點太倉促，恐對地方造成傷害。\n\n新竹科學園區銅鑼園區位於苗栗縣銅鑼鄉，占地351.24公頃，為了因應科技產業發展需求，竹科管理局提出環境影響差異分析，變更用水、用電等規劃，用水需求由1萬6000CMD（立方公尺／日）提高至3萬5600CMD，用電需求則由174.7MW提高至476.83MW，規模相當可觀。外傳進駐廠商包含Google資料中心、力積電、世界先進等，也讓環評委員、民間團體格外關注用水電量攀升引發的問題。\n\n晶片廠、Google資料中心擬進駐 用水用電激增備受關注\n環保署昨（16日）進行第三次環評初審，前一次初審決議要求補充進駐產業的製程、產品、產量變化等。開發單位竹科管理局昨日說明，潛在進駐之Google資料中心廠商規劃用水量1500CMD、用電量為45MW。擬進駐積體電路晶片製造廠商推估需水量約為2萬800CMD，推估需電量約為269.5MW，進一步分析不同產品產量規劃，12吋晶圓每月約產20萬片，推估需水量2萬CMD、6吋化合物半導體晶圓月產約1.5萬片，推估需水量800CMD。綜整積體電路晶片製造廠商及資料中心廠商之預估用水量為2萬2300CMD，用電量為314.5MW。\n\n竹科管理局強調，進駐廠商均需提用水計畫，也會要求其建置用水回收措施，而園區本身設置的污水處理廠處理量能可在2025年達3600CMD，積體電路晶片製造廠商須配合規劃使用再生水。用電部分，進駐之積體電路晶片製造廠商及資料中心廠商均承諾RE100，現階段以20%為目標，預估2040年達45%，2050達100%使用再生能源。\n苗栗縣環境保護協會理事長陳祺忠指出，當地是從鯉魚潭水庫取水，此計畫用水量激增，勢必加劇大安溪枯水期生態基流量不足問題。如2015年大安溪下游苑裡因為稀釋水體不夠導致稻米生長不良與枯萎、2021年大安溪大安段的芋頭同樣遭遇此問題。應加強與水利署溝通，評估是否要為了科學園區大量截水，才能共同維繫生態基流量。\n\n專管計畫挨批沒溝通 鄉長質疑環評太倉促\n除了水電用量變更疑慮，先前園區擬將污水排入西湖溪，在地民眾憂心影響農作物，連署要求設置污水專管放流入海，避開灌溉取水口。竹科管理局也在環差變更中調整放流位置，將設置一條16.9公里的專管，避開19處灌溉取水口，在福和大橋上游約0.9公里處左岸排放。\n\n由於專管下游正是國家級重要濕地「西湖濕地」，環評委員在前一次會議上要求加強相關監測，竹科管理局昨日承諾，將會加強專管在施工及營運期間的陸域、潮間帶及海域生態監測與保育措施，營運期間也會每季針對河川、海域或潮間帶的水質進行調查。\n銅鑼鄉長謝昌年無奈表示，當地居民要求專管排到外海，但開發單位這段期間也沒來和居民協商，就制定了專管計畫，相關規劃都還不完善、有爭議，昨日通過環評有點太倉促，恐對地方造成傷害，屆時再來補救恐怕為時已晚。\n\n陳祺忠也指出，西湖溪後龍出海口段，還是有一些農民取河川水灌溉，出海口還有鰻魚苗產業，然而專管專排僅到福和大橋，可能要預想日後鰻魚苗削減、農作物受損的情形，呼籲慎重處理後龍段的問題。','汙水相關資訊參考:\n{info}','info','admin','2024-12-09 21:57:39','admin','2024-12-09 22:40:46'),
('a8789055-b635-11ef-a8f7-45ee04d935d2','AA002','新竹人疑喝髒水 竹科管理局駁斥','新竹人疑喝髒水 竹科管理局駁斥\n\n有民眾批評竹科用頭前溪上游的好水，但新竹人則用下游被污染的水，竹科管理局今天澄清，園區廢水處理都符合環評承諾值及嚴格放流水標準，並沒有讓新竹人喝「髒」水。\n\n鏡週刊報導，一群高學歷的竹科媽媽組成「我們要喝乾淨水」聯盟，溯溪調查後發現，頭前溪上游沒有被垃圾汙水、家戶用水、科技廢水汙染的「好水」，80%作為新竹科學園區的工業用水，下游被汙染的水，卻是50萬新竹人每天在喝的飲用水。\n\n新竹科學工業園區管理局今天透過新聞稿指出，竹科的廢水經環保處理符合環評承諾值及放流水標準，而且主要是排放至客雅溪流域，並非頭前溪；頭前溪下游被污染的水跟竹科園區根本無關。\n\n竹科管理局表示，所有園區廠商排放的廢水都要遵守一定要求，每年也會定期檢查，只要違規就會立即開罰，同時，除了新竹生醫園區是委託新竹縣政府協助處理外，其餘每個園區都有設置污水處理廠，透過廠商加上處理廠雙管齊下，確保處理過排放水質都符合相關標準。\n\n竹科管理局指出，受限地理環境及高度，園區平常供水來源包含寶山淨水廠，水源為寶山與寶二水庫；以及新竹給水廠，水源為頭前溪隆恩堰 ，園區內用水未特別區分工業用水與民生用水，皆為同一供水管網。\n','汙水相關資訊參考:\n{info}','info','admin','2024-12-09 21:58:24','admin','2024-12-09 22:40:54'),
('ad68a6ea-a4b4-11ef-9d44-ffb88b49d6ab','DOC0003','參考的echarts資料','### echarts在html使用參考資料\n```html\n<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n    <meta charset=\"UTF-8\">\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n    <title>ECharts Example</title>\n    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/echarts/5.3.3/echarts.min.js\"></script>\n    <style>\n        .chart {\n            width: 740px;\n            height: 600px;\n        }\n    </style>\n</head>\n<body>\n    <h1>Pie Chart</h1>\n    <div id=\"pieChart\" class=\"chart\"></div>\n\n    <h1>Bar Chart</h1>\n    <div id=\"barChart\" class=\"chart\"></div>\n\n    <h1>Line Chart</h1>\n    <div id=\"lineChart\" class=\"chart\"></div>\n\n    <script type=\"text/javascript\">\n        var jsonData = [\n            {\"W_ID\":235733,\"QUANTITY\":932165.000000,\"TOTAL_AMOUNT\":6435667.00,\"F_NAMEC\":\"測試公司\",\"WATER_SOURCES_ID\":\"A1098F-3112-3492-04-K\"},\n            {\"W_ID\":235836,\"QUANTITY\":447715.000000,\"TOTAL_AMOUNT\":3091024.00,\"F_NAMEC\":\"好好公司\",\"WATER_SOURCES_ID\":\"A12672-3112-0010-27-6\"},\n            {\"W_ID\":235771,\"QUANTITY\":388852.000000,\"TOTAL_AMOUNT\":2684634.00,\"F_NAMEC\":\"宏一電子股份有限公司\",\"WATER_SOURCES_ID\":\"A11525-3112-0010-30-2\"}\n        ];\n\n        var pieData = jsonData.map(item => ({\n            name: item.F_NAMEC + \' (\' + item.WATER_SOURCES_ID + \')\',\n            value: item.QUANTITY\n        }));\n\n        var barData = jsonData.map(item => ({\n            name: item.F_NAMEC + \' (\' + item.WATER_SOURCES_ID + \')\',\n            value: item.QUANTITY\n        }));\n\n        var lineData = jsonData.map(item => ({\n            x: item.F_NAMEC + \' (\' + item.WATER_SOURCES_ID + \')\',\n            y: item.QUANTITY\n        }));\n\n        var pieChart = echarts.init(document.getElementById(\'pieChart\'));\n        var barChart = echarts.init(document.getElementById(\'barChart\'));\n        var lineChart = echarts.init(document.getElementById(\'lineChart\'));\n\n        optionPie = {\n            title: {\n                text: \'Pie Chart\',\n                subtext: \'Quantity Distribution\'\n            },\n            tooltip: {},\n            series: [{\n                name: \'Quantity\',\n                type: \'pie\',\n                radius: \'50%\',\n                data: pieData\n            }]\n        };\n\n        optionBar = {\n            title: {\n                text: \'Bar Chart\',\n                subtext: \'Quantity Distribution\'\n            },\n            tooltip: {},\n            xAxis: {\n                type: \'category\',\n                data: barData.map(item => item.name)\n            },\n            yAxis: {\n                type: \'value\'\n            },\n            series: [{\n                name: \'Quantity\',\n                type: \'bar\',\n                data: barData.map(item => item.value)\n            }]\n        };\n\n        optionLine = {\n            title: {\n                text: \'Line Chart\',\n                subtext: \'Quantity Distribution\'\n            },\n            tooltip: {},\n            xAxis: {\n                type: \'category\',\n                data: lineData.map(item => item.x)\n            },\n            yAxis: {\n                type: \'value\'\n            },\n            series: [{\n                name: \'Quantity\',\n                type: \'line\',\n                data: lineData.map(item => item.y)\n            }]\n        };\n\n        pieChart.setOption(optionPie);\n        barChart.setOption(optionBar);\n        lineChart.setOption(optionLine);\n    </script>\n</body>\n</html>\n```','如果與 echarts 相關的話, 參考下面相關資料:\n{information}','information','admin','2024-11-17 15:22:18','admin','2024-11-17 15:22:44'),
('d78034c7-b635-11ef-a8f7-c75dd86b2d8f','AA003','水污染防治許可','水污染防治許可\n\nA.	申請時機\n 	(1)	新申請者：水污染防治措施計畫及許可申請審查管理辦法(以下簡稱許可審查管理辦法)第9條規定，申請審查水措計畫之時機如下：\n 	 	a.	設置廢(污)水(前)處理設施前。\n 	 	b.	納管事業於取得核准同意納入污水下水道系統文件（以下簡稱同意納管文件）後，未取得聯接使用證明前。\n 	 	c.	簽訂廢（污）水委託處理契約前。\n 	 	d.	設置海洋放流管線前。\n 	 	e.	設置貯留設施前。\n 	 	f.	設置稀釋設施前。\n 	 	g.	設置回收使用設施前。\n 	 	h.	設置逕流廢水污染削減設施前。\n 	(2)	登記：許可審查管理辦法第22條規定，無排放廢（污）水於地面水體者，應於取得規定之相關證明文件後，辦理水措計畫核准文件基本資料之登記後，始得營運產生廢（污）水；其水措設施之建造、裝置與原核准之水措計畫登記事項不同者，於辦理前項之登記時，應另檢具變更後之相關文件。\n 	(3)	變更：\n 	◎許可審查管理辦法第21條規定，變更下列水措計畫核准文件或許可證(文件)登記事項，應於事實發生後30日內，向核發機關辦理變更：\n 	 	a.	基本資料。\n 	 	b.	廢（污）水水量、污泥量之計測設施、計量方式及其校正維護方法。\n 	 	c.	除中央主管機關依本法第十四條之一第一項指定公告之事業外，其增加或變更作業系統用水來源、產生廢（污）水之主要製程設施，或其每日最大生產或服務規模，且未增加用水量、廢（污）水產生量，及未變更廢（污）水（前）處理設施及污泥處理設施功能。\n 	 	d.	受託處理同業別之廢（污）水者，於核准餘裕量內之委託者及其委託廢（污）水量。\n 	 	e.	廢（污）水處理設施單元汰舊換新，且其規格條件及功能皆與原許可證（文件）登記相符。\n 	 	f.	僅變更廢（污）水處理設施單元之附屬機具設施，未涉及廢（污）水處理設施或其操作參數變更。\n 	 	g.	畜牧業設置之厭氧沼氣收集袋或貯存槽。\n 	 	h.	縮減每日最大用水量、廢（污）水產生量，且未涉及廢（污）水處理設施、操作參數或其他登記事項之變更。\n 	 	i.	其他未涉及廢（污）水、污泥之產生、收集、處理或排放之變更，經中央主管機關認定者。\n 	◎許可審查管理辦法第22條規定，變更前條以外之水措計畫核准文件或許可證(文件)登記事項，應於變更前，送核發機關審查，經核准後，始得變更。\n 	(4)	展延：\n 	許可審查管理辦法第31條規定，許可證(文件)水措計畫核准文件，有效期間為五年。 期滿仍繼續使用者，應自期滿六個月前起算五個月之期間內，向核發機關申請核准展延，每次展延，不得超過五年。\n\nB.	內容說明\n 	依水污染防治法第13規定辦理。\n\nC.	應備文件\n 	G水污染防治許可-應備文件及說明(PDF/ODF)\n\nD.	申請程序說明\n 	申請程序說明下載\n\nE.	相關法規\n 	(1)	水污染防治法。\n 	(2)	水污染防治施行細則。\n 	(3)	水污染防治各項許可申請收費標準。\n 	(4)	水污染防治措施計畫及許可申請審查管理辦法。\n 	(5)	水污染防治措施及檢測申報管理辦法。\n 	(6)	應先檢具水污染防治措施計畫之事業種類、範圍及規模。\n 	(7)	應以網路傳輸方式辦理水污染防治措施計畫與許可證（文件）之申請、變更或展延，及檢測申報之對象與作業方式。\n\nF.	承辦單位\n \n 	環安組環境保護科\n \n 	鄭小姐(ext:2335) \n\nG.	常用問與答\n 	Q：	工廠名稱／負責人異動或工廠合併應如何辦理？\n 	A：	如事業負責人變更或公司名稱變更，且涉及組織變更，則應重新申請管制編號。如事業負責人變更或公司名稱變更，但未涉及組織變更（可由相關證照及統一編號判斷），則無須重新申請管制編號。\n \n 	Q：	事業單位水污染防治措施計畫如有申請委(受)託或回收使用，依規定須設置相關累計型流量計，當有設置困難時，應該如何辦理?\n 	A：	一、依「水污染防治措施及檢測申報管理辦法」第66條規定，事業或污水下水道系統依本辦法規定設置之獨立專用累計型水量計測設施，有設置困難，經主管機關(環保局)同意者，得以足以證明水量之計測設施或計量方式為之。故應於提報水措申請文件時檢附所在地環保局之核准同意函。 二、另依97 年1 月15 日環署水字第0970003454 號函釋：委託者以連續管線輸送廢（污）水委託處理，在受託者之廢（污）水處理設施足以處理委託者之廢水水質、水量，且受託者之貯留槽或調勻池容積足以涵蓋雙方緊急狀況廢水暫存空間之情況下，經受託者開具之證明文件，委託者得免設置貯留設施。免設置貯留設施之情況，由主管機關(環保局)認定。\n \n 	Q：	事業冷卻水塔用於降溫循環之冷卻水、冷凝水及鍋爐蒸氣安全裝置（疏水閥）所產生之蒸氣水是否屬「事業廢水」?\n 	A：	一、如事業所產生之冷卻水，屬作業環境內之辦公場所、員工宿舍及其他活動場所、建築物所產生之污水，依「水污染防治措施及檢測申報管理辦法」第67條規定，其與事業廢水合併處理者，依事業廢水管理方式辦理；其與事業廢水分別處理者，依建築物污水處理設施管理方式辦理，亦即可由生活污水放流口排放，其排放水質應符合建築物污水處理設施放流水標準。 二、如於製程中無塵室溫、溼度控制使用、製程中加熱循環使用用水及鍋爐蒸氣安全裝置（疏水閥）所產生之蒸氣水，即符合「事業廢水」之未接觸冷卻水定義，其排放時應由許可之放流口排放，排放水質亦應符合所屬事業別之放流水標準。 三、承上，若水質乾淨，無須處理即可符合放流水標準，無須一定要納入廢水處理設施處理；惟其排放時，須由核准之放流口排放，並須避免與處理後之廢（污）水混合排放，以免有稀釋廢水之嫌。\n \n 	Q：	事業辦理水措變更若有涉及工程者，辦理變更程序該如何申請?\n 	A：	一、依據「水污染防治措施計畫及許可申請審查管理辦法」第24條規定，應於變更前送核發機關審查，經核准後始得變更。 二、依同法第25條規定，涉及工程者應同時檢具水措工程計畫書，送核發機關送審，依同意變更之內容及期間執行；但有調整變更內容之必要者，應檢具相關資料，經核發機關同意後，始得變更。 三、完工後檢附原申請內容及完工照片和原廢水、排放水之水質檢測報告，一式二份送至本局審核，據以完成許可證(文件)變更登記程序。 四、事業無法依前述規定於核發機關同意變更之期限內，完成變更登記程序者，應向核發機關申請延長期限，延長期限最多不超過90日。屆期仍無法完成者，應依原許可證（文件）登記事項辦理。','汙水相關資訊參考:\n{info}','info','admin','2024-12-09 21:59:43','admin','2024-12-09 22:41:01');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_orrs_task`
--

LOCK TABLES `tb_orrs_task` WRITE;
/*!40000 ALTER TABLE `tb_orrs_task` DISABLE KEYS */;
INSERT INTO `tb_orrs_task` VALUES
('5beb6ff7-b636-11ef-a8f7-bfa273be0a4c','test123','測試用','test','0 38 22 * * ?','Y','admin','2024-12-09 22:03:25','admin','2024-12-09 22:37:31'),
('63ae092c-a67c-11ef-aaa9-b1c6c1e492a5','task01','test文件檢索任務','','0 45 21 * * ?','Y','admin','2024-11-19 21:44:25',NULL,NULL),
('7590bed3-9aab-11ef-b609-854d67a445fb','task02','我的水量拆分檔任務','水量資料','0 24 15 * * ?','Y','admin','2024-11-04 20:51:07','admin','2024-11-17 15:23:13');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_orrs_task_cmd`
--

LOCK TABLES `tb_orrs_task_cmd` WRITE;
/*!40000 ALTER TABLE `tb_orrs_task_cmd` DISABLE KEYS */;
INSERT INTO `tb_orrs_task_cmd` VALUES
('1f091866-b63b-11ef-b3e4-1da18b2b6073','test123','TEST123',0,'Y','admin','2024-12-09 22:37:31',NULL,NULL),
('63afb6dd-a67c-11ef-aaa9-0d340787dab9','task01','test001',0,'Y','admin','2024-11-19 21:44:25',NULL,NULL),
('cea1989c-a4b4-11ef-9d44-1df2f82ce80b','task02','PP01',0,'Y','admin','2024-11-17 15:23:13',NULL,NULL),
('cea1989d-a4b4-11ef-9d44-fb067ca029c6','task02','PP02',1,'Y','admin','2024-11-17 15:23:13',NULL,NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role`
--

LOCK TABLES `tb_role` WRITE;
/*!40000 ALTER TABLE `tb_role` DISABLE KEYS */;
INSERT INTO `tb_role` VALUES
('4b1796ad-0bb7-4a65-b45e-439540ba5dbd','admin','administrator role!','admin','2014-10-09 15:02:24',NULL,NULL),
('58914623-46ea-4797-bbec-2dadc5d0800e','COMMON01','Common role!','admin','2017-05-09 13:31:42','admin','2024-10-09 21:56:13'),
('c7c69396-e5e6-48ca-b09c-9445b69e2ad5','*','all role','admin','2014-10-09 15:02:54',NULL,NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role_permission`
--

LOCK TABLES `tb_role_permission` WRITE;
/*!40000 ALTER TABLE `tb_role_permission` DISABLE KEYS */;
INSERT INTO `tb_role_permission` VALUES
('7c732a40-4664-11ee-9888-c1ca1a9bff29','COMMON01','/prog001d0004','VIEW','','admin','2023-08-29 20:06:29',NULL,NULL),
('7f0260a3-4664-11ee-9888-6f9b2693d639','COMMON01','/prog001d0004/create','VIEW','','admin','2023-08-29 20:06:33',NULL,NULL),
('815ccb66-4664-11ee-9888-b170f20e302b','COMMON01','/prog001d0004/edit','VIEW','','admin','2023-08-29 20:06:37',NULL,NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys`
--

LOCK TABLES `tb_sys` WRITE;
/*!40000 ALTER TABLE `tb_sys` DISABLE KEYS */;
INSERT INTO `tb_sys` VALUES
('c6643182-85a5-4f91-9e73-10567ebd0dd5','CORE','Core-system','127.0.0.1:8080','core-web','Y','SYSTEM','admin','2017-04-10 20:42:00','admin','2024-02-27 21:31:36');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_code`
--

LOCK TABLES `tb_sys_code` WRITE;
/*!40000 ALTER TABLE `tb_sys_code` DISABLE KEYS */;
INSERT INTO `tb_sys_code` VALUES
('2d9c84e4-a956-42ac-96cb-1f6292d182a9','CNF_CONF002','CNF','enable mail sender!','Y',NULL,NULL,NULL,NULL,'admin','2014-12-25 09:09:57','admin','2020-09-14 04:36:34'),
('4df770a6-6a9c-4d25-bdcd-1dee819d2ba6','CNF_CONF001','CNF','default mail from account!','root@localhost',NULL,NULL,NULL,NULL,'admin','2014-12-24 21:51:16','admin','2020-09-14 04:36:34'),
('57877c4d-4f3e-4679-880a-a262eeba0c3d','TOKEN','AUTH','QiFu3 Client token','9TYM7TRuILqFk9XoR0v6Yx672','COMMON01',NULL,NULL,NULL,'admin','2021-10-30 17:12:04',NULL,NULL),
('a5f7ee37-f33f-48a6-b448-92ccb8cdf96a','CNF_CONF003','CNF','first load javascript','addTab(\'CORE_PROG999D9999Q\', null);',NULL,NULL,NULL,NULL,'admin','2014-12-25 09:09:57',NULL,NULL),
('caf00ba5-fe63-4dc4-a1a3-32527f6629b2','CMM_CONF001','CMM','Common role for default user!','COMMON01',NULL,NULL,NULL,NULL,'admin','2017-05-09 12:29:00',NULL,NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_jreport`
--

LOCK TABLES `tb_sys_jreport` WRITE;
/*!40000 ALTER TABLE `tb_sys_jreport` DISABLE KEYS */;
INSERT INTO `tb_sys_jreport` VALUES
('f55a9ae4-b997-11ee-a0c4-97126ca2fa90','TEST01','TEST01.jrxml','Y',0x504B03041400000000004DAB1A57000000000000000000000000070000005445535430312F504B030414000000080020AB1A578FDF3D9426130000B5450000140000005445535430312F5445535430312E6A6173706572D51C6788335570932BEAE9777EDED97BEF5EB19CBDA49D46F792733767B903E35EF27259DDECAEBB1B2F67171B585050C18ABD8082888A200A961FA2600545EC88584045C4022AE2CC7B6F5B92CB6EF097E1FBEEB2EFCDCC9B99376FDECCBCB7F7E48FC2906D09FBEBC499B06B13E729B6492C8B9886E5D813445F517532712A6D9468A3403FFB7D9414064461F38AD130558D64154771846DC5F3940B9549D5989489A52A9A7A91B2AC916345612B0E56501A446ED66A6ACB11C618B0A6E82B93B263A9FA0A006EE0805646536CFB02E13221D9B284437AF1B6ACD8C0A094865FED0C1E9417769897730BD96259CE49F994583E2327C9F962A1BC90CFE6852D960DC7311A738A0584F2288BD66CE819A3A93B7961037B924DA502AC79BD67AA55A7BE286C545774C322F30A602A8E6AE88BC2B86ACF6A86E26428DCAC6138C442405B6E361A8AB55620AB004E1685DDBDA63355A78E6DA710A54AAC945E75B14655BBA43A1AE138796144233587319A1636372C95E80E1D17BA4C4A415DA903D79BE103E5310D1DA054A768016960DF420057D4CD1CC374A98DAED6895E3070064B6B2611859165A572FE8A056AA83AC2DE22E87ED2AE4D86743FC9743F896AD7AB306F5B32ED64558B54902D4738AC27A203234D4A4D1F21A7371B40678B4A407B38FD835E1B2A89B72D099B56815D9B38B623ECBF14C162968122F52AA9294DCD919D358D38C23E1188140CD086ABC451548DB3B3813DC9AEA0FB4550E1804067AC66580DC599552A8E61ADF9160E2A571B144B268E236CC91646D351B54999F2BD292E9226CC2C071F0554070D23A4A7CD1B8AAA7361A3D9F2B532A82B0D97F2B04E0D8153DC1830B43314AD093A3B347A5A8B1CC99FD51133CC2B6F09CC2858906FAD7CA8E9E8A1E63D1C3ED2E636D33658723C66651F9E925812866D9C7630AC7D97C478F6B189CD9633B7CDCD1CD23035C5411A93513498CB2A310C2436E4E0C2E74A190FAF4DAE9823A2C53AD3C3F3246B9982F0D393E019B786FF09817E9207D15FBC71E0546891E913FD0FBBC241313D2FBA01DFEF6EDADBEF0ED7A9B7A26E4E3635D549699AB14AAA202F3502643ED7322D62DB74851D18A1431F167710D33200C851893DA7988E704804F27C101C7D84459CA645CD1D27706360318AAA8DEB65331B796616B631B089A5D7E8048EB26E7FBAA6A2A78B6AC19F294B382CA6DE731A69C0623B19BCB5E9E91F17F2A695BAAA552DA2A3256D0362111F12B47270944A03D060396009E328E804EA61226559CA1A2AA375E5FBBBDCF9BA72EF8090C80B83B67A1169512E5607F1A7677042C284CF659630D14B2A54C4444811AE6D8E012D4B18A50CA0A627DA3B41A04DE492942B654E8151A62347096E3CEDB4505FBB3A10DB9424EA9DC7E8CF9DBA887508150B7B778B803CDE8784DF362822E6F472EF1498D925616819D61A58E63E4B71B666803715CB89033F0F7030D74D0B417B31C849DFFED9653FDCF8E635472499A442A24D0B09AA8504883B1553DC126939B32AD102AE64BBDEAE64C3B2619C0FBEF77C915C48340866C885B0E814EAD2D506490B5BD6D760484DD5CF2F41BC439CB4B0C16F812F8BC236E0841C58F4953A8663C50B8955036F240A5B2B7AA56E5818B2FA0E06651B1785ED42C376746FE97341575074A8C1161A38C010FF7D38FC5C08CFDD77493B673B78C2A7A8749D927900185FB4752F09E3816E0B34033B3A98D66151A6754A071670B7B3474B223502AEAAD2A9E81DFDC9320CCD51CD1E0CE3B6D13915AA9DD614D659686A9A038D01970D4489A2A3AA7C13E1A1D0A6AE89F0E74D4CC501C675FEB8157BEC1C71C4012B4E55CF6BDA4EAC18A6E481BBCEFFD03E960B77D4FE82D9A1D78201A96AB0A316DC808F3FCBE0B9C38AC9EB0E598159E2003605D82A0040531D34583022F5220051B4940649113283A4B71585EDBB7471839E8956CB296164D44D7838941EBB40F863FA21C7F07C92C36021865645A60F0771553B0F406A853F6F50EDF96A2DD75826D52A7181B6A42E433D9F94EAB06E57EABC790B68C690B08A43F0B6CD557B41AF520325BC6913FC9E365AD131BAC80031AE451C9E8B72F56E0C34C50FCE451FC9951FFD58D3E4F6B099092B74C552CCBA231C10BD61305064D0042DE915A3CA194CB2A6D9366BB30C9618701936B8CF5C80C968012423945A6C7521066F954EEBDBB6A3838F7178F418670450DB060AD9DD51F14985ADAE153F04E858E03BC50CAEB7536D9A1EE5F559159CF4997543A3613AEC78BC07720F02494795C5BA8BC28EBC1D7D659626B9EE6E68532489348C0B095A104250B79A16B6300D5B7553A83424606C2FC5A7BC30B48AA588BC9068C1FF35B02E2C2D403A6F585EE8ACAC3A93196C011D6F1DCCCF2130BF50859503331695CA76C13A3614F232F3DE011880149C5006B0212D0A03E793356E9C830DA34AB8F56C86DFB9C11C143DCB7300ED66A0B0806050648612DB041C61A0091783B7DD795B89AF423668ACF2C9BC8FC507F7F3A7CE6D691BAF8BEA2353072F4E68AC36DD9E3461E3D6B0D7F3C6359F569CBD7EBE030B18DB18308BF8CE8AC7650101079B4DB51ACECA16C0ECFDE456F752DB29817D46A83C5BE18F09937E62E50841EDAE93238CCFE6CF2A4B393155CA9F912B978AF06FDE34A3B2E6B69433544E1DC4854FD50F79A43F27986BD13CAE7DA6C2C52268004598ED01B8234C40559533631B3560CD69565563C2860952AA769D100720749D251BF96C0B44D8CA4FF74E51EC3A901EDAE4E3575EDBF6DCF70684E4AC3002DB7F9515B2B09A588799AEC326DA324F3C89CA33B0BA29FCA4C32337A74321716699CC1C5E3DB276C851B5230F3DE4F0A30EADC0B799E94366C8D461D529E5C8995A4569E1DC4C45CE0DB78B1E5333522896794608C28CFAC2A0B5BC78E7C01F7734AE998474EA54610B08006D888056D2AA639F0A4534C37B6A99373FFDDA76BBFF73FF9E0FDD7BC9592F6D7BEA2D68647486B7F493511A097DFCE31B0F7E72DCCF3F2485C4AC3074215A38F8F88D3E54A109218475ED93B7EFB2F96D5FDE4053A6D4BB82408DF1A84881BB442FEB083E9CC9154A390967D01F9C87B96FED6E5DF1C13D7FFE044C2EBA4C9A909AF69793F2A0C4B7DBCDC12A59257D5EA9BAFBFF91E0437923D1A37C285047CF015401183DF7B2D1CAA0B8D06945172BD20168F4C0582B0FB3B2096D62C5900A86F2A1DE01D3EBD902CBE46DC89BD2361F7B048AE76188616871FB7101C6AFDF00EB69574BBE46853ECE3C80420837E10798C80F861A07C62415A233DC3B8FD80CC7C8781B296FF076BD6DC17FB90D7DD4C1448EC2DC3D23CA8E3390E851303F30557D58C67C009AEF116E2586783FF23861D33175046A15C196DAA60BC9AC745237ED753BE2677930A20406187BC8F8E516A05C32CC9874CD7E287B99814F7904D2A61A06A238C779BDCA23F5234576B61568E9CC768E03DC4013A6ABDC1EC6BC74256015878A31F39690D7C0551E66630B9B8D97AA41A2CFDB36F0B634C148D205749465197C00B554D76BB0366602DBB44CFC306BF33F2C68E11F16981C16B917842B3DEB952F0BC533FBB06A3F56F3276C282F24D52A9C8754EA4DFD7C1BD6EF9218B70E9F41140CD780E398F52B861B0E6747E91E450FCA0249E498DF2A1145F37A681172A40956DABB7CD9C968E3EC773E574F7B68C1AF645AC24CDFAAA3B402A5DA34131FB5009B366771C011060BA9B99CE97FD8C44F464EBC5F285A67D237CD2C94CAA5DC59252F1ABE34547886A13126E867B3E7A784BE5463BDB685452CA6CC2978AABC1D9E5C49C4369A5685CCA9A0207D056716ECA98615DE38E76BB4148CC9434DD5600576142387573093894389A63CA1A3CE25C80B0215CC039762F80C56B8EC2B5B3AAA375DAEDEAE29D3D0054D62AD45978E4F47305C2C16D7761A0EEE35EFA8D8AE58AAE9C071375D30AEEC5E6B1CD96517F858C4843E3A31B1305D603775C3F14F8123D20B15B81FB24C8F590F8822720687C5927137ABF253CA13A2FDCC999D04DA4E481302B8907DA35C08932A79DC17AF170EFCC5741DC7E6B0B40E8EB9B4DA0E3A86610BAA12362FDCC4934183EDC3ECB06BB67FEF09AB1C368E623E4B09CCF979E400CD23C173EDDE3D8FA40B7A023975843D7A8168CA32D1C0A27BC1406A4726A0B65E6FD1F1A58E8472009BEF649CE2D7BB1D88BF97CBF61A85BF8B758034C1EC8BDD2F6212DD4AE51C96CF96CBFF0B511FF598C5A7C743D23EC6FB62084C779DFF81B8CF7056F1FB7321619FE53DD1A29E52944BFF03515FE6ACE2F75743A2BEC27B2245DD22532CE08E5F9E4F954EF91F88FC561BCBD8F64E48F4B7DB202255B0695E2E8BC54C4AFC1F88FF71805D7CFE3424FA2781DE48B107F3A0A5FF81C8DF7256F1FBF72171BFE33D91A26E92598058F37FE1AC7FF5B9C5C7DF4302FFE676C670D743996CAAF43FF0D78984CB2B3E0C04E54D24DD2E94768C4A6B5FA04D60DA6A3B4AC30CCDF1C2FF658E13A33EB7F8B83124F396D819778E17FE2773BC93CB2B3EEC12927767ECEA31C7F001F8524E2E4D4D435C7D60545CEDE7597B0A5BDC74E06BE7EEE1C6D6E37DD69F289540FD895E379C352C48B61AA60353B148CFF1D76CB8AE09278840A10AA7C6FC049226146D0967B700BDEDC251BC1CC4E3AEB394D3761D775427C05E15339442CF44A0FF683F91A053332AE5E68B52A9CCB75C935923ED3AA097B225FA080534AC6D50ACC4BE9CE4369CE47C4A8268AD04E581F25C6A3E4879837F1E030284B1B73D3525CFE7A432232277636C2AEEAB0A7657FE36044708128EFF0E4498E056BE0E0BB94C098A2141A2E3DEA2C878676C61FC8D1C7F2E7516D0582804790A1EE3F04B3961E4318E0C6B3055968B0B5226175F247A0B5CA6C9707796E48C949F2F89B9104B93BD69A696E1B0178E06DDB241BBFA196916E68478DDE89B8568C09D89369EB6E588528E89594E2F14B2610ADBFB14A46035641DE94AF9B95C79B15808D118F369E0AA5C3474D2DDBE678BD25C0A7EA532A5A274768044CFEA1AA33C1B7C13204C7EDCB5263125633098CAE6A420F16D7C83A0EB5A34F016FD3A02E6E6E6C51478DF2081AD7D01E10047EB66919BCB54C07C4ECC06518307A978261D461A9ECD8BB0DA63BB105EFC9A85E2DE7A467D465E2A2DA4C4FC621F64A160A45A4E13DF3E6A27BB751E52E5930B452907CEE9E47C21D5BE563BCF4C5D0202855A13E0D3C46F977A175E2F870828B9E79E267E58F1C1A3D8350068DFEF93AB83F873DD8D1ADE7461EEC3E105B3437AC0F1ED0BBF2B55C504C5AEB7B12751AE8B6139CE61792D9B3E44C325878D1739825839536B544BA737AB279F5AAB1CA65D94CFACB60A32FE6F358A27CF1DBE985D992ECAD3E717B25A1D6E4AAB8B8DB39DB31BB9E9823AB556B828DD289C7CEA79C5ECCAA18543178E6C997D54C568353350440F9C3F1CB414A7124AABF01DEFCCD06BCD07F50E3E42044EBA4F78E0BB170E7FD88D3E927D9CA6F8647C4106C3A701EEFEEE18E7135D26588A064F008B3B70C5325387D60A8642A0040A084A185FEA7C6DAE6582091E2DE744D87C763F7064562ACEED7E815A6B1E3EC1E2B491334FC949B9DD47360BB54E9C0BA67AEEEEC7EFCE37E0AB93CC7AA915CBA78BA6C903F4B35EFAF0C867AE7CF4CEEC23777C7544C638F97450E50111AAF48BB6CBDF5C75C91D073F9F7515B909287232A6225D22318F1FE0F6199870A5A9D1902A0D7737F58A45AF806100D519EEA585CD20BE23B4174E19186E9F2F3B657C2416BF750689DDEE618FBAACF9B7D3A6215A0B311CFFBE623E88C6D9D8C9A3452CBECFB40787BB758274468A1013ABBAEAC0DB9CDD62E270B83A82EA0C49348A2D7DBE0C2321CA7F3A055CFD09DF708A7595283C7F9DA76AACC00AFB283BA53B349260682ED6213958C098C3F4B69444972D657B9DACEEDE11FBED3F7D00DBA061FFCA950B0B73E99C14EFCD1A4FABEB89C9B65CF40427328770B3AB487CB81D86C5DF77B018652E053A91CA99054982FB4665E48676DF1F0F7F8CE3978AA59448B1E5D8E89ED6927D6B6D43A6282ECC153CBD21C5FB603A90816E235380DB20C3A551B93FF2409F2377B03E189FC014637D0B1E11514E7AEBEA297F9CA1F8E3746774383E01CEE8082A33C0E69331D8DC24FE28DDD9DC343E01579FCC1418A39E250CB356CA322EABE322975597F3C49E0B7F411491FA5EF12E3A065EA55DEFCC1FAD424AE54B11AFA0CDC47E596D2F1F32D6B504FF1DDCF558842DBA9487642F96C8FC95B75E4A4CA70A5933428A0303371FBABF97469FA73878D1BB36BCAF3BA2776D38F195E9DDDE39CDFD72060B8DAEBFEE8ECDFFF955FCF5DA639F38EFB1D9FC57480EFC05FEF9831A1B77915DF3C4AFE75032AC7999FDAA056E5A25BEF37E7CCD7AB5CE5ED6B1B25E87B15EC7059D1D2E2BB6DBFC758FAB4908B8EAADB7CDDAD7DB8077101920C1D1AEF226A5189894A82B361045C12DD709F70DE98CFF77264237A6C72B9661DB901A05007080024408FC6F0474F66CEBBE29DFD99B6899A18449A009D3466A5BEB76802CC77795C5FD6B1636AF97F9D1935FFA0B8944DF9BA9B4B1046F0854E9EBAA3E1265054E4EC2CC6EC178E217113A2B47635FDDFFF01F575E775412DED7752F0023FE122203C6B1BD85C00216A85AEF2646E8062AA71DFE1B1FC5E5F3408463E93D03BA1BFCE99A4FE26FF891F41B93F898F01E07C28F831E4AE22FAF71280C338C8F43DEE326E1DE4DC3BD9BE17DA808C9E9658D7584F523E0165D46BF789447F05B7273ECBBB205C95D797AE6E8C3A68E9839F4E8A3A78E3EBC7CE4A147CC1C7134A419B1D6C1A9A0C94A8673F42F504B0304140000000800F9AA1A572E26EF8DF704000051170000130000005445535430312F5445535430312E6A72786D6CBD985973DA3010C79FCBA7503D7DEA8C8D2F7C64209D04C83504DAD8699A7632A96CCBA0D4576C39403AFDEE956CCE346903141EE2B1E4DDDF6AFF5AC768EB1F4661001E509AE1386A7092207200456EECE1A8DFE02EED23DEE03EEC57EA6F791E34530409F2C010930138835942BD629F008BE41E8EA710A009922E88828F2318803CA3A089F1054AE29464A0839D14A6E3270EBCE6998A642AB22A799EE1BAAAACC926920CC33155DF44A2E3D6745946BE0700CFD315DD2D30014D22CA1ADC809064AF5A2D1FA56538218BF3D4457E9CF6911021B2FC942B5DF746199EB90F874361A808D4A12A8BA254FD72DEB1DC010A218FA38CC0C8451CA0F67B5931D9895D48681AEB4407AF721965DE928540273810C1103538BB6DD9A2C48104F6D115F6C8A0C1D5CC5A393E41B83F200DCE50650EB8719087D1D4A4464D02E4937398F6315DBA4CB73DA5D64B13244E16874E4C481C2ECEE439F61A1CAC990692951AAF2A9ACFAB7A4DE21DCFD37845771443829A69AA2AB75F79534FD298E640C693A5BB7128DCCDAA48C88A2A123C48A090DD0702814E80E8FE3CC02067D65741E8D99F72EFF8CC7795E0F1B4391C752DF6370A7BC7E7EAD7565FEA59D28F6E2B1874EC0BFC35BC26D7615BEA6271DC7D3C0CBBC76777BD565FEECA973A575D6D391EF2611E10760F3D981094CE9645E5C0B075C807B40A820917A614488D26E0DE698BCA1FC08C16E81D7C804200A3BE609194BE194C9737F509FF3343B647498A32F65EECD7DF7E6BB60EEC836F1C7773B35FAF3E6FC642566731D9E83E47E9B8E403162B877DBA0CEB53A78836A35AED4EBB6983F795A38BDE39B8C77EAE0AC4B9CDC659E5EAA47DD10695374BB3C2779AC977D000EF3EFEA477BF6E6E8AD00BD1D8D8C728F05E9DF9ABF6A0400ACCA2947D42AEAE4A08A08382CD10244548482019CC30A536135261D442999BE2842C6D61B1814F1F539FC9E453E5AC6BEB766BE231F83AC917FA6D4CD99584DD83F3F6960464E8D5132FE4DB90B12BF14E7A96BD25F1187AF5C40BF13664EC4ABC66AF6BB7BFD8B71F0FEC932D89B8186275210A31FF136B57A29E5AB79D5EF3A0B32541A7F8D50528C4FC0F9C9D0949F77B5B2252F4EA8917026EC8D89578CD4BAB7DB1B50F73415FE79B5A48B83966672A52A7A56F73F98BDFC621A2E7AB30D950454A5FE3D35A68B82964570A5E6EB50E2FD72DA042C3CD313B5371AB7578B95E09151A6E0AD9A6820E747FF4D3388FD8880D230F644980893D4ED8B980C626EEA03C0B57978CEB049300CDBD069306856E72CF119821F54123723489CDC6650FA41DA01045B4F1D3E06823625C5C87D3E606BD9FA3A76D0A43531CE8689087BAAAF1AA8964DE8086CE9B86E8F8C85755D7578B454F82CE42B0FB8300F72336A2FF19E815A5E5E29894313561976E79704329F66936F8910E646D06AC2E10CBA97962CF1DF9DF1DFD2C0F58BF8A1D79CEB632E1CEE529F42E95AE4EA5AE979D20E8A1F44FE1951AF7F2D62D39D6CBFED14B204DFA0B68D995B53B08C4C19F1049AE6D5E07D2422168F47E0697E60D2BC5F474B766D03695442B019A226FF88EC4BB8AA4CBA28FA028CB65D0D7BE745992D2ECB201424470E328422E7B6D4E5BB3B74F7390A67ABA4F03E932AF1AB24BEF3489D790A87822D435DF85F35A594A6DBD8A9C1520C0D9611CD0AC499AA34DCA911D58D72DC672C7E7657414C7E4B932526BFF2CA3D2755AD82F816AEA3F0A7B8EC9F230A4DDE8671623FF8531F3AA57171BD1FB95DF504B01023F001400000000004DAB1A570000000000000000000000000700240000000000000010000000000000005445535430312F0A00200000000000010018001C56D1E920D8D9011C56D1E920D8D901659BDCE720D8D901504B01023F0014000000080020AB1A578FDF3D9426130000B54500001400240000000000000020000000250000005445535430312F5445535430312E6A61737065720A0020000000000001001800CEA52AB620D8D9011C56D1E920D8D9011C56D1E920D8D901504B01023F00140000000800F9AA1A572E26EF8DF70400005117000013002400000000000000200000007D1300005445535430312F5445535430312E6A72786D6C0A00200000000000010018005AF0698C20D8D9011C56D1E920D8D9011C56D1E920D8D901504B0506000000000300030024010000A51800000000,'aaa2222333','admin','2024-01-23 10:34:40','admin','2024-01-23 10:50:22');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_jreport_param`
--

LOCK TABLES `tb_sys_jreport_param` WRITE;
/*!40000 ALTER TABLE `tb_sys_jreport_param` DISABLE KEYS */;
INSERT INTO `tb_sys_jreport_param` VALUES
('f4840904-b9a5-11ee-a0c4-3b25c1760c68','TEST01','oid','OID','admin','2024-01-23 12:14:51',NULL,NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_menu`
--

LOCK TABLES `tb_sys_menu` WRITE;
/*!40000 ALTER TABLE `tb_sys_menu` DISABLE KEYS */;
INSERT INTO `tb_sys_menu` VALUES
('122721b3-b876-11ee-ad91-67276b113f6d','CORE_PROG004D0001Q','5e055f61-bfc5-402c-93b4-f241dc17b00b','Y','admin','2024-01-21 23:59:34',NULL,NULL),
('12276fd4-b876-11ee-ad91-e77b0e0410dd','CORE_PROG004D0002Q','5e055f61-bfc5-402c-93b4-f241dc17b00b','Y','admin','2024-01-21 23:59:34',NULL,NULL),
('4bd4d202-5feb-495b-8c8c-ec6b7f5b8041','CORE_PROG002D0002Q','79e1cf24-2522-4cdf-abcc-6455b47d545b','Y','admin','2017-05-10 14:20:12',NULL,NULL),
('5262981f-b7c8-11ef-844b-ed447c86d408','ORRS001D0001Q','f9b02a8d-8af4-11ef-92ef-b305036c7452','Y','admin','2024-12-11 22:00:47',NULL,NULL),
('52638280-b7c8-11ef-844b-371616cefd84','ORRS001D0002Q','f9b02a8d-8af4-11ef-92ef-b305036c7452','Y','admin','2024-12-11 22:00:47',NULL,NULL),
('526445d1-b7c8-11ef-844b-1311a7266c69','ORRS001D0003Q','f9b02a8d-8af4-11ef-92ef-b305036c7452','Y','admin','2024-12-11 22:00:47',NULL,NULL),
('52650922-b7c8-11ef-844b-13f244b9234a','ORRS001D0004Q','f9b02a8d-8af4-11ef-92ef-b305036c7452','Y','admin','2024-12-11 22:00:47',NULL,NULL),
('5265cc73-b7c8-11ef-844b-27cc0be6ab3e','ORRS001D0005Q','f9b02a8d-8af4-11ef-92ef-b305036c7452','Y','admin','2024-12-11 22:00:47',NULL,NULL),
('5e055f61-bfc5-402c-93b4-f241dc17b00b','CORE_PROG004D','00000000-0000-0000-0000-000000000000','Y','admin','2017-06-03 14:23:17',NULL,NULL),
('79e1cf24-2522-4cdf-abcc-6455b47d545b','CORE_PROG002D','00000000-0000-0000-0000-000000000000','Y','admin','2017-05-08 21:32:59',NULL,NULL),
('7ea68636-c93a-4669-ac42-dafc3770d20d','CORE_PROG001D','00000000-0000-0000-0000-000000000000','Y','admin','2017-04-20 11:24:53',NULL,NULL),
('9972c249-2985-49ac-9b8b-f6c25c65fd4e','CORE_PROG002D0003Q','79e1cf24-2522-4cdf-abcc-6455b47d545b','Y','admin','2017-05-10 14:20:12',NULL,NULL),
('c5349a26-6d6e-4d94-b817-82be6d14d5ed','CORE_PROG002D0001Q','79e1cf24-2522-4cdf-abcc-6455b47d545b','Y','admin','2017-05-10 14:20:12',NULL,NULL),
('f0242c17-4487-11ee-b50d-a593cf4a05bf','CORE_PROG001D0001Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2023-08-27 11:15:13',NULL,NULL),
('f0253d88-4487-11ee-b50d-7f3d9b9812d0','CORE_PROG001D0002Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2023-08-27 11:15:13',NULL,NULL),
('f0264ef9-4487-11ee-b50d-a55549dc8acf','CORE_PROG001D0003Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2023-08-27 11:15:13',NULL,NULL),
('f027877a-4487-11ee-b50d-8fe1228e511a','CORE_PROG001D0004Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2023-08-27 11:15:13',NULL,NULL),
('f02898eb-4487-11ee-b50d-45ee94442a45','CORE_PROG001D0005Q','7ea68636-c93a-4669-ac42-dafc3770d20d','Y','admin','2023-08-27 11:15:13',NULL,NULL),
('f9b02a8d-8af4-11ef-92ef-b305036c7452','ORRS001D','00000000-0000-0000-0000-000000000000','Y','admin','2024-10-15 20:57:03',NULL,NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_menu_role`
--

LOCK TABLES `tb_sys_menu_role` WRITE;
/*!40000 ALTER TABLE `tb_sys_menu_role` DISABLE KEYS */;
INSERT INTO `tb_sys_menu_role` VALUES
('7147502a-4abc-11ee-9380-f7f86272e6b6','CORE_PROG001D0004Q','COMMON01','admin','2023-09-04 08:46:10',NULL,NULL),
('72a61cdd-4abc-11ee-9380-19ab31a4dc18','CORE_PROG001D','COMMON01','admin','2023-09-04 08:46:13',NULL,NULL),
('81f84f17-b9e4-11ee-ae49-63eb5d9c550a','CORE_PROG004D','COMMON01','admin','2024-01-23 19:42:37',NULL,NULL),
('924e2206-b9e4-11ee-ae49-efea48f6c66a','CORE_PROG004D0001Q','COMMON01','admin','2024-01-23 19:43:05',NULL,NULL),
('924e2207-b9e4-11ee-ae49-d35121d1905d','CORE_PROG004D0001Q','admin','admin','2024-01-23 19:43:05',NULL,NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_prog`
--

LOCK TABLES `tb_sys_prog` WRITE;
/*!40000 ALTER TABLE `tb_sys_prog` DISABLE KEYS */;
INSERT INTO `tb_sys_prog` VALUES
('045c79fd-9728-11ef-af3f-65d7c66b9839','ORRS001D0003Q','AA03 - 任務結果','#/orrs001d0003','N','N',0,0,'CORE','ITEM','SYSTEM','clipboard2-check-fill','admin','2024-10-31 09:32:39',NULL,NULL),
('0aa817d1-8afa-11ef-92ef-5bfccd6233c0','ORRS001D0001Q','AA01 - 命令','#/orrs001d0001','N','N',0,0,'CORE','ITEM','SYSTEM','cpu','admin','2024-10-15 21:33:19','admin','2024-10-19 22:31:36'),
('186b1fb1-749f-4b6f-97d1-6b7fb8115345','CORE_PROG001D0004E','ZA04 - Freemarker樣板 (Edit)','#/prog001d0004/edit','Y','N',0,0,'CORE','ITEM','TEMPLATE','file-text','admin','2017-05-12 10:40:10','admin','2023-08-16 21:48:56'),
('1b11c7eb-6133-48fb-87f0-dfbd098ce914','CORE_PROG001D0001E','ZA01 - System site (Edit)','#/prog001d0001/edit','Y','N',0,0,'CORE','ITEM','COMPUTER','globe2','admin','2014-10-02 00:00:00','admin','2021-01-20 08:20:58'),
('1e393fe3-8bbc-482c-aa23-bbb22a1dbafb','CORE_PROG001D0005A','ZA05 - JasperReport (Create)','#/prog001d0005/create','N','N',0,0,'CORE','ITEM','APPLICATION_PDF','file-pdf','admin','2017-05-18 09:55:46','admin','2023-08-24 20:20:27'),
('22560527-90fb-4e5a-a89b-353d2aa1d433','CORE_PROG001D0005E','ZA05 - JasperReport (Edit)','#/prog001d0005/edit','Y','N',0,0,'CORE','ITEM','APPLICATION_PDF','file-pdf','admin','2017-05-18 09:56:27','admin','2023-08-24 20:20:40'),
('2dbbe7d9-a5a9-11ef-beae-5b496a6e5c29','ORRS001D0004T','AA04 - 檢索文件 (檢索測試)','#/orrs001d0004/test','N','N',0,0,'CORE','ITEM','SYSTEM','filetype-txt','admin','2024-11-18 20:32:30','admin','2024-11-18 20:39:25'),
('3630ee1b-6169-452f-821f-5c015dfb84d5','CORE_PROG001D','ZA. Config','/','N','N',0,0,'CORE','FOLDER','PROPERTIES','gear-fill','admin','2014-10-02 00:00:00','admin','2023-08-15 19:16:31'),
('3862b6d0-0551-45d8-8dd1-cd988a5e8e50','CORE_PROG004D0002Q','ZD02 - Token log','#/prog004d0002','N','N',0,0,'CORE','ITEM','PROPERTIES','clipboard-check','admin','2017-06-03 14:22:29','admin','2024-01-25 07:47:53'),
('41fa29d8-3a53-4fbd-b2b1-cdbfd0729767','CORE_PROG001D0004Q','ZA04 - Freemarker樣板','#/prog001d0004','N','N',0,0,'CORE','ITEM','TEMPLATE','file-text','admin','2017-05-12 10:36:41','admin','2023-08-16 21:48:29'),
('4b9f06da-b7c8-11ef-844b-839662273913','ORRS001D0005Q','AA05 - Chat','#/orrs001d0005','N','N',0,0,'CORE','ITEM','SYSTEM','chat-dots-fill','admin','2024-12-11 22:00:36',NULL,NULL),
('5e082c7c-1730-4176-89c6-93e235707deb','CORE_PROG002D0001A','ZB01 - Role (Create)','#/prog002d0001/create','N','N',0,0,'CORE','ITEM','PEOPLE','person-square','admin','2017-05-09 11:15:50','admin','2023-08-27 16:46:40'),
('61aea7ff-7a42-4a92-9a0b-4a0dfe60858b','CORE_PROG004D0001Q','ZD01 - Event log','#/prog004d0001','N','N',0,0,'CORE','ITEM','PROPERTIES','clipboard-pulse','admin','2017-06-03 14:22:07','admin','2023-08-29 10:17:34'),
('6a442973-0e0c-4a7a-d546-464f4ff5f7a9','CORE_PROG001D0003Q','ZA03 - Menu settings','#/prog001d0003','N','N',0,0,'CORE','ITEM','FOLDER','menu-down','admin','2014-10-02 00:00:00','admin','2023-08-15 19:21:23'),
('6b1a27ac-a480-11ef-980f-8be3b04dd41b','ORRS001D0004Q','AA04 - 檢索文件','#/orrs001d0004','N','N',0,0,'CORE','ITEM','SYSTEM','filetype-txt','admin','2024-11-17 09:08:13',NULL,NULL),
('6b210525-8975-4fb5-954c-fe349f66d3fe','CORE_PROG002D0001S01Q','ZB01 - Role (permission)','#/prog002d0001/setparam','Y','N',0,0,'CORE','ITEM','IMPORTANT','globe2','admin','2017-05-09 14:32:47','admin','2024-02-10 20:17:33'),
('72e6e0d1-1818-47d3-99f9-5134fb211b79','CORE_PROG002D','ZB. Role authority','/','N','N',0,0,'CORE','FOLDER','SHARED','person-square','admin','2017-05-08 21:27:52','admin','2023-08-27 16:47:03'),
('7746f746-961f-44c2-9b66-fa43c0f49838','CORE_PROG001D0004S01Q','ZA04 - Freemarker樣板 (Parameter)','#/prog001d0004/setparam','Y','N',0,0,'CORE','ITEM','TEMPLATE','file-text','admin','2017-05-12 10:42:04','admin','2023-08-16 21:49:12'),
('7d9ddc45-3eab-4f61-8c0a-d5505c0cc748','CORE_PROG001D0004A','ZA04 - Freemarker樣板 (Create)','#/prog001d0004/create','N','N',0,0,'CORE','ITEM','TEMPLATE','file-text','admin','2017-05-12 10:39:20','admin','2023-08-16 21:48:49'),
('8499957e-6da9-4160-c2ec-dfb7dbc202fe','CORE_PROG001D0002E','ZA02 - Program (Edit)','#/prog001d0002/edit','Y','N',0,0,'CORE','ITEM','G_APP_INSTALL','filetype-html','admin','2014-10-02 00:00:00','admin','2023-08-15 19:19:17'),
('8643db14-8e26-11ef-9028-771f90e53e59','ORRS001D0002Q','AA02 - 任務','#/orrs001d0002','N','N',0,0,'CORE','ITEM','SYSTEM','x-diamond-fill','admin','2024-10-19 22:29:18',NULL,NULL),
('95d789da-8c8e-11ef-b80d-6f8dd1830e29','ORRS001D0001A','AA01 - 命令 (Create)','#/orrs001d0001/create','N','N',0,0,'CORE','ITEM','SYSTEM','cpu','admin','2024-10-17 21:49:09',NULL,NULL),
('97012256-8e26-11ef-9028-bb15b679bda0','ORRS001D0002A','AA02 - 任務 (Create)','#/orrs001d0002/create','N','N',0,0,'CORE','ITEM','SYSTEM','x-diamond-fill','admin','2024-10-19 22:29:46',NULL,NULL),
('9f60e0c8-a480-11ef-b0a1-c93187b52991','ORRS001D0004A','AA04 - 檢索文件 (Create)','#/orrs001d0004/create','N','N',0,0,'CORE','ITEM','SYSTEM','filetype-txt','admin','2024-11-17 09:09:40',NULL,NULL),
('a7bfc928-8e26-11ef-9028-995dacc25c2c','ORRS001D0002E','AA02 - 任務 (Edit)','#/orrs001d0002/edit','Y','N',0,0,'CORE','ITEM','SYSTEM','x-diamond-fill','admin','2024-10-19 22:30:14',NULL,NULL),
('aba3755a-a480-11ef-b0a1-a3a65648f212','ORRS001D0004E','AA04 - 檢索文件 (Edit)','#/orrs001d0004/edit','N','N',0,0,'CORE','ITEM','SYSTEM','filetype-txt','admin','2024-11-17 09:10:01',NULL,NULL),
('ac5bcfd0-4abd-11e4-916c-0800200c9a66','CORE_PROG001D0001A','ZA01 - System site (Create)','#/prog001d0001/create','N','N',0,0,'CORE','ITEM','COMPUTER','globe2','admin','2014-10-02 00:00:00','admin','2021-01-20 08:20:45'),
('b39159ad-0707-4515-b78d-e3fc72c53974','CORE_PROG002D0001E','ZB01 - Role (Edit)','#/prog002d0001/edit','Y','N',0,0,'CORE','ITEM','PEOPLE','person-square','admin','2017-05-09 12:11:53','admin','2023-08-27 16:46:35'),
('b6b89559-6864-46ab-9ca9-0992dcf238f1','CORE_PROG001D0001Q','ZA01 - System site','#/prog001d0001','N','N',0,0,'CORE','ITEM','COMPUTER','globe2','admin','2014-10-02 00:00:00','admin','2021-01-20 08:20:29'),
('b978f706-4c5f-40f8-83b1-395492f141d4','CORE_PROG002D0001Q','ZB01 - Role','#/prog002d0001','N','N',0,0,'CORE','ITEM','PEOPLE','person-square','admin','2017-05-08 21:32:50','admin','2023-08-27 16:46:27'),
('c1467c36-8af4-11ef-92ef-ffd759a8a480','ORRS001D','AA. 本地llm工作','/','N','N',0,0,'CORE','FOLDER','SYSTEM','send','admin','2024-10-15 20:55:29','admin','2024-10-15 21:35:05'),
('c96ebde8-7044-4b05-a155-68a0c2605619','CORE_PROG002D0003Q','ZB03 - Role for menu','#/prog002d0003','N','N',0,0,'CORE','ITEM','FOLDER','menu-app-fill','admin','2017-05-08 21:37:01','admin','2024-07-05 23:12:41'),
('cd5629af-8e18-11ef-ad17-6b820d47cc0f','ORRS001D0001E','AA01 - 命令 (Edit)','#/orrs001d0001/edit','Y','N',0,0,'CORE','ITEM','SYSTEM','cpu','admin','2024-10-19 20:51:04',NULL,NULL),
('da7d969a-5efb-4e84-9eab-4fdae236f28c','CORE_PROG002D0002Q','ZB02 - User role','#/prog002d0002','N','N',0,0,'CORE','ITEM','PERSON','person-check','admin','2017-05-08 21:34:39','admin','2023-08-28 19:54:25'),
('dda67b1d-e3a2-4534-835a-c62d9e8421f3','CORE_PROG001D0005S01Q','ZA05 - JasperReport (Parameter)','#/prog001d0005/setparam','Y','N',0,0,'CORE','ITEM','APPLICATION_PDF','file-pdf','admin','2017-05-18 09:57:26','admin','2023-08-24 20:21:02'),
('df447a07-974c-11ef-840a-9fcc1b9ef968','ORRS001D0003E','AA03 - 任務結果(執行紀錄檢視)','#/orrs001d0003/edit','N','N',0,0,'CORE','ITEM','SYSTEM','clipboard2-check-fill','admin','2024-10-31 13:56:29',NULL,NULL),
('e32b9329-bb38-46d7-8552-2307bac77724','CORE_PROG001D0002A','ZA02 - Program (Create)','#/prog001d0002/create','N','N',0,0,'CORE','ITEM','G_APP_INSTALL','filetype-html','admin','2014-10-02 00:00:00','admin','2023-08-15 19:19:42'),
('e86dbb1b-6870-4827-8039-72f5e15fa4f2','CORE_PROG004D','ZD. Log','/','N','N',0,0,'CORE','FOLDER','PROPERTIES','clipboard-check-fill','admin','2017-06-03 14:21:03','admin','2024-03-02 15:10:42'),
('eb6e199f-c853-4fbf-acf3-0c9c77ba9953','CORE_PROG001D0002Q','ZA02 - Program','#/prog001d0002','N','N',0,0,'CORE','ITEM','G_APP_INSTALL','filetype-html','admin','2014-10-02 00:00:00','admin','2023-08-15 19:19:05'),
('eb786ffd-c7d1-4631-aed2-4d9d7368eb13','CORE_PROG001D0005Q','ZA05 - JasperReport','#/prog001d0005','N','N',0,0,'CORE','ITEM','APPLICATION_PDF','file-pdf','admin','2017-05-18 09:54:35','admin','2023-08-24 20:20:16');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_template`
--

LOCK TABLES `tb_sys_template` WRITE;
/*!40000 ALTER TABLE `tb_sys_template` DISABLE KEYS */;
INSERT INTO `tb_sys_template` VALUES
('467f92b0-d5fa-11ee-9ec4-c75e54f9ca1d','TPL01','${title}','<h1>Product:${name}</h1>','','admin','2024-02-28 13:28:59',NULL,NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_template_param`
--

LOCK TABLES `tb_sys_template_param` WRITE;
/*!40000 ALTER TABLE `tb_sys_template_param` DISABLE KEYS */;
INSERT INTO `tb_sys_template_param` VALUES
('6ff19e9d-d5fa-11ee-9ec4-c330a97e3ff9','TPL01','Y','title','title','admin','2024-02-28 13:30:09',NULL,NULL);
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_role`
--

LOCK TABLES `tb_user_role` WRITE;
/*!40000 ALTER TABLE `tb_user_role` DISABLE KEYS */;
INSERT INTO `tb_user_role` VALUES
('1f37136f-b9e0-11ee-ae49-8d2df8636cc7','COMMON01','tiffany','','admin','2024-01-23 19:11:14',NULL,NULL),
('31102b06-b9e0-11ee-ae49-9b67c6a3ca84','COMMON01','tester','','admin','2024-01-23 19:11:44',NULL,NULL),
('9243c7de-43b1-46ef-ac4b-2620697f319e','admin','admin','Administrator','admin','2014-09-23 00:00:00',NULL,NULL),
('a02e1b62-4988-11ee-8b9a-098c887743fd','COMMON01','steven','','admin','2023-09-02 20:02:44',NULL,NULL);
/*!40000 ALTER TABLE `tb_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vector_store`
--

DROP TABLE IF EXISTS `vector_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vector_store` (
  `id` varchar(50) NOT NULL,
  `content` text NOT NULL,
  `metadata` JSON,
  `embedding` VECTOR(384) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `metadata` CHECK (json_valid(`metadata`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vector_store`
--

LOCK TABLES `vector_store` WRITE;
/*!40000 ALTER TABLE `vector_store` DISABLE KEYS */;
/*!40000 ALTER TABLE `vector_store` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2024-12-11 22:04:43
