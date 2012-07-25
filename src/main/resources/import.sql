-- MySQL dump 10.13  Distrib 5.5.25a, for Linux (i686)
--
-- Host: localhost    Database: SkynetTemp
-- ------------------------------------------------------
-- Server version	5.5.25a

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.5.25a, for Linux (i686)
--
-- Host: localhost    Database: SkynetTest
-- ------------------------------------------------------
-- Server version	5.5.25a

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `BlobConstants`
--

DROP TABLE IF EXISTS `BlobConstants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BlobConstants` (
  `BlobConstantsID` int(11) NOT NULL AUTO_INCREMENT,
  `ConstantName` varchar(45) NOT NULL,
  `ConstantValue` longblob,
  PRIMARY KEY (`BlobConstantsID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1 COMMENT='This table holds blob constants for things like image files ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BugzillaBug`
--

DROP TABLE IF EXISTS `BugzillaBug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BugzillaBug` (
  `BugzillaBugID` int(11) NOT NULL AUTO_INCREMENT,
  `BugzillaBugBuzillaID` int(11) NOT NULL,
  `BugzillaBugSummary` text,
  `BugzillaBugOpen` bit(1) DEFAULT NULL,
  `BugzillaBugBugzillaID` int(11) NOT NULL,
  PRIMARY KEY (`BugzillaBugID`)
) ENGINE=InnoDB AUTO_INCREMENT=937 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Category`
--

DROP TABLE IF EXISTS `Category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Category` (
  `CategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `CategoryName` varchar(512) NOT NULL,
  `CategoryDescription` text,
  `CategorySort` int(11) DEFAULT NULL,
  `MutuallyExclusive` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`CategoryID`),
  UNIQUE KEY `index3` (`CategoryName`),
  KEY `fk_Category_1` (`CategoryID`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1 COMMENT='Categories contain tags. The relationship between a tag and ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Exception`
--

DROP TABLE IF EXISTS `Exception`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Exception` (
  `ExceptionID` int(11) NOT NULL AUTO_INCREMENT,
  `Timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Details` text NOT NULL,
  `Expected` tinyint(1) NOT NULL,
  `Description` varchar(512) DEFAULT NULL,
  `User` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ExceptionID`)
) ENGINE=InnoDB AUTO_INCREMENT=11286 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Filter`
--

DROP TABLE IF EXISTS `Filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Filter` (
  `FilterID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterName` varchar(512) NOT NULL,
  `FilterDescription` text,
  PRIMARY KEY (`FilterID`),
  UNIQUE KEY `index2` (`FilterName`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=latin1 COMMENT='A filter is a saved query. A filter is made up of a row in t';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FilterCategory`
--

DROP TABLE IF EXISTS `FilterCategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FilterCategory` (
  `FilterCategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterID` int(11) NOT NULL,
  `CategoryID` int(11) NOT NULL,
  `CategoryState` int(11) NOT NULL DEFAULT '0',
  `ProjectID` int(11) DEFAULT NULL,
  PRIMARY KEY (`FilterCategoryID`),
  KEY `fk_FilterCategory_1` (`CategoryID`),
  KEY `fk_FilterCategory_2` (`FilterID`),
  KEY `FKBB45C0B6EB3A6876` (`ProjectID`),
  CONSTRAINT `FKBB45C0B6EB3A6876` FOREIGN KEY (`ProjectID`) REFERENCES `Project` (`ProjectID`),
  CONSTRAINT `fk_FilterCategory_1` FOREIGN KEY (`CategoryID`) REFERENCES `Category` (`CategoryID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_FilterCategory_2` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3731 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FilterField`
--

DROP TABLE IF EXISTS `FilterField`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FilterField` (
  `FilterFieldID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterID` int(11) NOT NULL,
  `Field` varchar(512) NOT NULL,
  `Value` text NOT NULL,
  `Description` text,
  PRIMARY KEY (`FilterFieldID`),
  UNIQUE KEY `index3` (`Field`,`FilterID`),
  KEY `fk_Filter_Field_1` (`FilterID`),
  CONSTRAINT `fk_Filter_Field_1` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FilterLocale`
--

DROP TABLE IF EXISTS `FilterLocale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FilterLocale` (
  `FilterLocaleID` int(11) NOT NULL AUTO_INCREMENT,
  `LocaleName` varchar(512) NOT NULL,
  `LocaleState` int(11) NOT NULL,
  `FilterID` int(11) NOT NULL,
  PRIMARY KEY (`FilterLocaleID`),
  UNIQUE KEY `FilterLocaleID` (`FilterLocaleID`),
  UNIQUE KEY `LocaleName` (`LocaleName`,`FilterID`),
  KEY `FK7CB6A01259256D82` (`FilterID`),
  CONSTRAINT `FK7CB6A01259256D82` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FilterOption`
--

DROP TABLE IF EXISTS `FilterOption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FilterOption` (
  `FilterOptionID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterOptionName` varchar(512) NOT NULL,
  `FilterOptionValue` varchar(512) NOT NULL,
  `FilterID` int(11) NOT NULL,
  PRIMARY KEY (`FilterOptionID`),
  UNIQUE KEY `FilterOptionID` (`FilterOptionID`),
  UNIQUE KEY `FilterOptionName` (`FilterOptionName`,`FilterID`),
  KEY `FK81EB1A2D59256D82` (`FilterID`),
  CONSTRAINT `FK81EB1A2D59256D82` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`)
) ENGINE=InnoDB AUTO_INCREMENT=432 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FilterTag`
--

DROP TABLE IF EXISTS `FilterTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FilterTag` (
  `FilterTagID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterID` int(11) NOT NULL,
  `TagID` int(11) NOT NULL,
  `TagState` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`FilterTagID`),
  KEY `fk_FilterTag_1` (`TagID`),
  KEY `fk_FilterTag_2` (`FilterID`),
  CONSTRAINT `fk_FilterTag_1` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_FilterTag_2` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=461 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Help`
--

DROP TABLE IF EXISTS `Help`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Help` (
  `HelpID` int(11) NOT NULL AUTO_INCREMENT,
  `TableColID` varchar(512) DEFAULT NULL,
  `HelpText` text,
  PRIMARY KEY (`HelpID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ImageFile`
--

DROP TABLE IF EXISTS `ImageFile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ImageFile` (
  `ImageFileID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` text,
  PRIMARY KEY (`ImageFileID`)
) ENGINE=InnoDB AUTO_INCREMENT=1086 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IntegerConstants`
--

DROP TABLE IF EXISTS `IntegerConstants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IntegerConstants` (
  `IntegerConstantsID` int(11) NOT NULL AUTO_INCREMENT,
  `ConstantName` varchar(45) NOT NULL,
  `ConstantValue` int(11) DEFAULT NULL,
  PRIMARY KEY (`IntegerConstantsID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LanguageImage`
--

DROP TABLE IF EXISTS `LanguageImage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LanguageImage` (
  `LanguageImageID` int(11) NOT NULL AUTO_INCREMENT,
  `ImageFileID` int(11) NOT NULL,
  `ThumbnailData` longblob,
  `ImageDataBase64` longblob,
  `Locale` varchar(512) NOT NULL,
  `OriginalFileName` varchar(2048) DEFAULT NULL,
  `ImageData` longblob,
  PRIMARY KEY (`LanguageImageID`),
  KEY `FK15D2ACC3E0AD6B52` (`ImageFileID`)
) ENGINE=InnoDB AUTO_INCREMENT=1086 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Project`
--

DROP TABLE IF EXISTS `Project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Project` (
  `ProjectID` int(11) NOT NULL AUTO_INCREMENT,
  `ProjectName` varchar(512) NOT NULL,
  `ProjectDescription` text,
  PRIMARY KEY (`ProjectID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PropertyTag`
--

DROP TABLE IF EXISTS `PropertyTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PropertyTag` (
  `PropertyTagID` int(11) NOT NULL AUTO_INCREMENT,
  `PropertyTagName` varchar(512) NOT NULL,
  `PropertyTagDescription` text,
  `PropertyTagRegex` text NOT NULL,
  `PropertyTagCanBeNull` bit(1) NOT NULL,
  `PropertyTagIsUnqiue` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`PropertyTagID`),
  UNIQUE KEY `PropertyTagUnique` (`PropertyTagName`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PropertyTagCategory`
--

DROP TABLE IF EXISTS `PropertyTagCategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PropertyTagCategory` (
  `PropertyTagCategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `PropertyTagCategoryName` varchar(512) NOT NULL,
  `PropertyTagCategoryDescription` text,
  PRIMARY KEY (`PropertyTagCategoryID`),
  UNIQUE KEY `PropertyTagCategoryNameUnique` (`PropertyTagCategoryName`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PropertyTagToPropertyTagCategory`
--

DROP TABLE IF EXISTS `PropertyTagToPropertyTagCategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PropertyTagToPropertyTagCategory` (
  `PropertyTagToPropertyTagCategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `PropertyTagID` int(11) NOT NULL,
  `PropertyTagCategoryID` int(11) NOT NULL,
  `Sorting` int(11) DEFAULT NULL,
  PRIMARY KEY (`PropertyTagToPropertyTagCategoryID`),
  UNIQUE KEY `PropertyTagToPropertTagCategoryUnique` (`PropertyTagID`,`PropertyTagCategoryID`),
  KEY `PropertyTagToPropertTagCategoryFk2` (`PropertyTagCategoryID`),
  CONSTRAINT `PropertyTagToPropertTagCategoryFK1` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PropertyTagToPropertTagCategoryFk2` FOREIGN KEY (`PropertyTagCategoryID`) REFERENCES `PropertyTagCategory` (`PropertyTagCategoryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1 COMMENT='PropertyTagToPropertyTagCategoryID\nPropertyTagToPropertyTagC';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RelationshipTag`
--

DROP TABLE IF EXISTS `RelationshipTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RelationshipTag` (
  `RelationshipTagID` int(11) NOT NULL AUTO_INCREMENT,
  `RelationshipTagName` varchar(512) NOT NULL,
  `RelationshipTagDescription` text,
  PRIMARY KEY (`RelationshipTagID`),
  UNIQUE KEY `RelationshipTagNameUnique` (`RelationshipTagName`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Role`
--

DROP TABLE IF EXISTS `Role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Role` (
  `RoleID` int(11) NOT NULL AUTO_INCREMENT,
  `RoleName` varchar(512) NOT NULL,
  `Description` text,
  PRIMARY KEY (`RoleID`),
  UNIQUE KEY `index2` (`RoleName`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RoleToRole`
--

DROP TABLE IF EXISTS `RoleToRole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RoleToRole` (
  `RoleToRoleID` int(11) NOT NULL AUTO_INCREMENT,
  `PrimaryRole` int(11) NOT NULL,
  `RelationshipType` int(11) NOT NULL,
  `SecondaryRole` int(11) NOT NULL,
  PRIMARY KEY (`RoleToRoleID`),
  UNIQUE KEY `RoleToRoleID` (`RoleToRoleID`),
  UNIQUE KEY `PrimaryRole` (`PrimaryRole`,`SecondaryRole`,`RelationshipType`),
  KEY `FKD0A0E5C78433D197` (`SecondaryRole`),
  KEY `FKD0A0E5C7844F7725` (`PrimaryRole`),
  KEY `fk_RoleToRole_1` (`RelationshipType`),
  CONSTRAINT `FKD0A0E5C78433D197` FOREIGN KEY (`SecondaryRole`) REFERENCES `Role` (`RoleID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FKD0A0E5C7844F7725` FOREIGN KEY (`PrimaryRole`) REFERENCES `Role` (`RoleID`),
  CONSTRAINT `fk_RoleToRole_1` FOREIGN KEY (`RelationshipType`) REFERENCES `RoleToRoleRelationship` (`RoleToRoleRelationshipID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RoleToRoleRelationship`
--

DROP TABLE IF EXISTS `RoleToRoleRelationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RoleToRoleRelationship` (
  `RoleToRoleRelationshipID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` text,
  PRIMARY KEY (`RoleToRoleRelationshipID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Snapshot`
--

DROP TABLE IF EXISTS `Snapshot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Snapshot` (
  `SnapshotID` int(11) NOT NULL AUTO_INCREMENT,
  `SnapshotName` varchar(512) NOT NULL,
  `SnapshotDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`SnapshotID`),
  UNIQUE KEY `index2` (`SnapshotName`,`SnapshotDate`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SnapshotRevision`
--

DROP TABLE IF EXISTS `SnapshotRevision`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SnapshotRevision` (
  `SnapshotRevisionID` int(11) NOT NULL AUTO_INCREMENT,
  `SnapshotID` int(11) NOT NULL,
  `SnapshotRevisionName` varchar(512) NOT NULL,
  `SnapshotRevisionDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`SnapshotRevisionID`),
  KEY `fk_SnapshotRevision_1` (`SnapshotID`),
  CONSTRAINT `fk_SnapshotRevision_1` FOREIGN KEY (`SnapshotID`) REFERENCES `Snapshot` (`SnapshotID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SnapshotTopic`
--

DROP TABLE IF EXISTS `SnapshotTopic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SnapshotTopic` (
  `SnapshotTopicID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `TopicRevision` int(11) NOT NULL,
  PRIMARY KEY (`SnapshotTopicID`)
) ENGINE=InnoDB AUTO_INCREMENT=246 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SnapshotTopicToSnapshot`
--

DROP TABLE IF EXISTS `SnapshotTopicToSnapshot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SnapshotTopicToSnapshot` (
  `SnapshotTopicToSnapshotID` int(11) NOT NULL AUTO_INCREMENT,
  `SnapshotID` int(11) NOT NULL,
  `SnapshotTopicID` int(11) NOT NULL,
  PRIMARY KEY (`SnapshotTopicToSnapshotID`),
  UNIQUE KEY `SnapshotTopicToiSnapshotSnapshotTopicIDUnique` (`SnapshotTopicID`),
  KEY `FK6E3FD16AFB9C1D5A` (`SnapshotID`),
  KEY `FK6E3FD16A5FDB66BA` (`SnapshotTopicID`),
  KEY `FK6E3FD16A7BC75C04` (`SnapshotTopicID`),
  CONSTRAINT `SnapshotIDFK` FOREIGN KEY (`SnapshotID`) REFERENCES `Snapshot` (`SnapshotID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SnapshotTopicIDFK` FOREIGN KEY (`SnapshotTopicID`) REFERENCES `SnapshotTopic` (`SnapshotTopicID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=246 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SnapshotTranslatedData`
--

DROP TABLE IF EXISTS `SnapshotTranslatedData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SnapshotTranslatedData` (
  `SnapshotTranslatedDataID` int(11) NOT NULL AUTO_INCREMENT,
  `SnapshotRevisionID` int(11) NOT NULL,
  `TranslatedXMLRendered` longtext,
  `TranslationLocale` varchar(45) NOT NULL,
  `SnapshotTopicID` int(11) NOT NULL,
  `TranslatedXML` longtext,
  `TranslatedXMLRenderedUpdated` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`SnapshotTranslatedDataID`),
  KEY `FKE65828A4FDD8B770` (`SnapshotRevisionID`),
  KEY `FKE65828A45FDB66BA` (`SnapshotTopicID`),
  CONSTRAINT `SnapshotRevisionIDFK` FOREIGN KEY (`SnapshotRevisionID`) REFERENCES `SnapshotRevision` (`SnapshotRevisionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=246 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SnapshotTranslatedString`
--

DROP TABLE IF EXISTS `SnapshotTranslatedString`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SnapshotTranslatedString` (
  `SnapshotTranslatedStringID` int(11) NOT NULL AUTO_INCREMENT,
  `ShapshotOriginalString` text,
  `SnapshotTranslatedString` text,
  `SnapshotTranslatedDataID` int(11) NOT NULL,
  PRIMARY KEY (`SnapshotTranslatedStringID`),
  UNIQUE KEY `SnapshotTranslatedStringsID` (`SnapshotTranslatedStringID`),
  KEY `FKCB94428B7BA9BE5A` (`SnapshotTranslatedDataID`),
  CONSTRAINT `FKCB94428B7BA9BE5A` FOREIGN KEY (`SnapshotTranslatedDataID`) REFERENCES `SnapshotTranslatedData` (`SnapshotTranslatedDataID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `StringConstants`
--

DROP TABLE IF EXISTS `StringConstants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `StringConstants` (
  `StringConstantsID` int(11) NOT NULL AUTO_INCREMENT,
  `ConstantName` varchar(45) NOT NULL,
  `ConstantValue` longtext,
  PRIMARY KEY (`StringConstantsID`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=latin1 COMMENT='This table holds the string constants used by Skynet, includ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Tag`
--

DROP TABLE IF EXISTS `Tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Tag` (
  `TagID` int(11) NOT NULL AUTO_INCREMENT,
  `TagName` varchar(512) NOT NULL,
  `TagDescription` text,
  PRIMARY KEY (`TagID`),
  UNIQUE KEY `index4` (`TagName`),
  KEY `fk_Tag_1` (`TagID`),
  KEY `fk_Tag_2` (`TagID`)
) ENGINE=InnoDB AUTO_INCREMENT=501 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TagExclusion`
--

DROP TABLE IF EXISTS `TagExclusion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TagExclusion` (
  `Tag1ID` int(11) NOT NULL,
  `Tag2ID` int(11) NOT NULL,
  PRIMARY KEY (`Tag1ID`,`Tag2ID`),
  KEY `fk_TagExclusion_1` (`Tag1ID`),
  KEY `fk_TagExclusion_2` (`Tag2ID`),
  CONSTRAINT `fk_TagExclusion_1` FOREIGN KEY (`Tag1ID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_TagExclusion_2` FOREIGN KEY (`Tag2ID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TagToCategory`
--

DROP TABLE IF EXISTS `TagToCategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TagToCategory` (
  `TagToCategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `TagID` int(11) NOT NULL COMMENT 'References the TagID that is to be placed into the Category.',
  `CategoryID` int(11) NOT NULL COMMENT 'References the CategoryID that the Tag is to be made a child of.',
  `Sorting` int(11) DEFAULT NULL COMMENT 'Defines the sorting order of the Tag in the Category.',
  PRIMARY KEY (`TagToCategoryID`),
  UNIQUE KEY `Unique` (`TagID`,`CategoryID`),
  KEY `fk_TagToCategory_1` (`TagID`),
  KEY `fk_TagToCategory_2` (`CategoryID`),
  KEY `FK8DF417B37D4F054E` (`CategoryID`),
  KEY `FK8DF417B36F9851B8` (`TagID`),
  CONSTRAINT `FK8DF417B36F9851B8` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON UPDATE CASCADE,
  CONSTRAINT `FK8DF417B37D4F054E` FOREIGN KEY (`CategoryID`) REFERENCES `Category` (`CategoryID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=559 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TagToProject`
--

DROP TABLE IF EXISTS `TagToProject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TagToProject` (
  `TagToProjectID` int(11) NOT NULL AUTO_INCREMENT,
  `ProjectID` int(11) NOT NULL,
  `TagID` int(11) NOT NULL,
  PRIMARY KEY (`TagToProjectID`),
  UNIQUE KEY `index4` (`ProjectID`,`TagID`),
  KEY `fk_TagToProject_1` (`ProjectID`),
  KEY `fk_TagToProject_2` (`TagID`),
  CONSTRAINT `fk_TagToProject_1` FOREIGN KEY (`ProjectID`) REFERENCES `Project` (`ProjectID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_TagToProject_2` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=559 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TagToPropertyTag`
--

DROP TABLE IF EXISTS `TagToPropertyTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TagToPropertyTag` (
  `TagToPropertyTagID` int(11) NOT NULL AUTO_INCREMENT,
  `TagID` int(11) NOT NULL,
  `PropertyTagID` int(11) NOT NULL,
  `Value` longtext,
  PRIMARY KEY (`TagToPropertyTagID`),
  KEY `TagIDFK` (`TagID`),
  KEY `PropertyTagIDFK` (`PropertyTagID`),
  CONSTRAINT `TagToPropertyTagFK1` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `TagToPropertyTagFK2` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=163 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TagToTag`
--

DROP TABLE IF EXISTS `TagToTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TagToTag` (
  `TagToTagID` int(11) NOT NULL AUTO_INCREMENT,
  `PrimaryTagID` int(11) NOT NULL,
  `SecondaryTagID` int(11) NOT NULL,
  `RelationshipType` int(11) NOT NULL,
  PRIMARY KEY (`TagToTagID`),
  UNIQUE KEY `UNIQUE` (`PrimaryTagID`,`SecondaryTagID`,`RelationshipType`),
  KEY `fk_TagToTag_1` (`RelationshipType`),
  KEY `fk_TagToTag_2` (`PrimaryTagID`),
  KEY `fk_TagToTag_3` (`SecondaryTagID`),
  CONSTRAINT `fk_TagToTag_1` FOREIGN KEY (`RelationshipType`) REFERENCES `TagToTagRelationship` (`TagToTagRelationshipType`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_TagToTag_2` FOREIGN KEY (`PrimaryTagID`) REFERENCES `Tag` (`TagID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_TagToTag_3` FOREIGN KEY (`SecondaryTagID`) REFERENCES `Tag` (`TagID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TagToTagRelationship`
--

DROP TABLE IF EXISTS `TagToTagRelationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TagToTagRelationship` (
  `TagToTagRelationshipType` int(11) NOT NULL,
  `TagToTagRelationshipDescription` text,
  PRIMARY KEY (`TagToTagRelationshipType`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Topic`
--

DROP TABLE IF EXISTS `Topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Topic` (
  `TopicID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicTitle` varchar(512) DEFAULT NULL,
  `TopicText` text,
  `TopicTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `TopicSVNURL` varchar(512) DEFAULT NULL,
  `TopicXML` mediumtext,
  `TopicLocale` varchar(512) NOT NULL,
  `TopicRendered` text,
  PRIMARY KEY (`TopicID`),
  KEY `fk_Topic_1` (`TopicID`),
  KEY `FK4D3DD0FE2436E96` (`TopicID`)
) ENGINE=InnoDB AUTO_INCREMENT=10404 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TopicSecondOrderData`
--

DROP TABLE IF EXISTS `TopicSecondOrderData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicSecondOrderData` (
  `TopicSecondOrderDataID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicXMLErrors` longtext,
  `TopicHTMLView` mediumtext,
  `TopicID` int(11) NOT NULL,
  PRIMARY KEY (`TopicSecondOrderDataID`)
) ENGINE=InnoDB AUTO_INCREMENT=10245 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TopicSourceURL`
--

DROP TABLE IF EXISTS `TopicSourceURL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicSourceURL` (
  `TopicSourceURLID` int(11) NOT NULL AUTO_INCREMENT,
  `SourceURL` varchar(2048) NOT NULL,
  `Description` text,
  `Title` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`TopicSourceURLID`)
) ENGINE=InnoDB AUTO_INCREMENT=12898 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TopicToBugzillaBug`
--

DROP TABLE IF EXISTS `TopicToBugzillaBug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicToBugzillaBug` (
  `TopicToBugzillaBugID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `BugzillaBugID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToBugzillaBugID`),
  UNIQUE KEY `TopicIDBugzillaBugIDUnique` (`TopicID`,`BugzillaBugID`),
  UNIQUE KEY `TopicToBugzillaBugBugzillaBugIDUnique` (`BugzillaBugID`),
  KEY `TopicToBugzillaBugFK2` (`BugzillaBugID`),
  CONSTRAINT `TopicToBugzillaBugFK2` FOREIGN KEY (`BugzillaBugID`) REFERENCES `BugzillaBug` (`BugzillaBugID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `TopicToBugzillaBug_ibfk_1` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=935 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TopicToPropertyTag`
--

DROP TABLE IF EXISTS `TopicToPropertyTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicToPropertyTag` (
  `TopicToPropertyTagID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `PropertyTagID` int(11) NOT NULL,
  `Value` longtext,
  PRIMARY KEY (`TopicToPropertyTagID`),
  KEY `TopicToPropertyTagFK1` (`TopicID`),
  KEY `TopicToPropertyTagFK2` (`PropertyTagID`),
  CONSTRAINT `TopicToPropertyTagFK1` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `TopicToPropertyTagFK2` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=127470 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TopicToTag`
--

DROP TABLE IF EXISTS `TopicToTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicToTag` (
  `TopicToTagID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `TagID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToTagID`),
  UNIQUE KEY `UNIQUE` (`TopicID`,`TagID`),
  KEY `fk_TopicToTag_1` (`TopicID`),
  KEY `fk_TopicToTag_2` (`TagID`),
  CONSTRAINT `fk_TopicToTag_1` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_TopicToTag_2` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=91075 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TopicToTopic`
--

DROP TABLE IF EXISTS `TopicToTopic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicToTopic` (
  `TopicToTopicID` int(11) NOT NULL AUTO_INCREMENT,
  `MainTopicID` int(11) NOT NULL,
  `RelatedTopicID` int(11) NOT NULL,
  `RelationshipTagID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToTopicID`),
  UNIQUE KEY `TopicToTopicUnique` (`MainTopicID`,`RelatedTopicID`,`RelationshipTagID`),
  KEY `fk_TopicToTopic_1` (`MainTopicID`),
  KEY `fk_TopicToTopic_2` (`RelatedTopicID`),
  KEY `fk_TopicToTopic_3` (`RelationshipTagID`),
  CONSTRAINT `fk_TopicToTopic_1` FOREIGN KEY (`MainTopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_TopicToTopic_2` FOREIGN KEY (`RelatedTopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_TopicToTopic_3` FOREIGN KEY (`RelationshipTagID`) REFERENCES `RelationshipTag` (`RelationshipTagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7365 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TopicToTopicSecondOrderData`
--

DROP TABLE IF EXISTS `TopicToTopicSecondOrderData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicToTopicSecondOrderData` (
  `TopicToTopicSecondOrderDataID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `TopicSecondOrderDataID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToTopicSecondOrderDataID`),
  UNIQUE KEY `TopicToTopicSecondOrderDataTopicIdUnique` (`TopicID`),
  UNIQUE KEY `TopicToTopicSecondOrderDataTopicSecondOrderDataIdUnique` (`TopicSecondOrderDataID`),
  KEY `FK303D33DF3340562` (`TopicID`),
  KEY `fk_TopicToTopicSecondOrderData_1` (`TopicSecondOrderDataID`),
  CONSTRAINT `FK303D33DF3340562` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_TopicToTopicSecondOrderData_1` FOREIGN KEY (`TopicSecondOrderDataID`) REFERENCES `TopicSecondOrderData` (`TopicSecondOrderDataID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10244 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TopicToTopicSourceURL`
--

DROP TABLE IF EXISTS `TopicToTopicSourceURL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicToTopicSourceURL` (
  `TopicToTopicSourceURLID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `TopicSourceURLID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToTopicSourceURLID`),
  UNIQUE KEY `UniqueIndex` (`TopicID`,`TopicSourceURLID`),
  UNIQUE KEY `UniqueTopicSourceURLID` (`TopicSourceURLID`),
  KEY `ForeignTopicSourceURLID` (`TopicSourceURLID`),
  CONSTRAINT `ForeignTopicID` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ForeignTopicSourceURLID` FOREIGN KEY (`TopicSourceURLID`) REFERENCES `TopicSourceURL` (`TopicSourceURLID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12883 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TranslatedTopic`
--

DROP TABLE IF EXISTS `TranslatedTopic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TranslatedTopic` (
  `TranslatedTopicID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `TopicRevision` int(11) NOT NULL,
  PRIMARY KEY (`TranslatedTopicID`),
  UNIQUE KEY `TranslatedTopicID` (`TranslatedTopicID`),
  UNIQUE KEY `TopicRevision` (`TopicRevision`,`TopicID`)
) ENGINE=InnoDB AUTO_INCREMENT=973 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TranslatedTopicData`
--

DROP TABLE IF EXISTS `TranslatedTopicData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TranslatedTopicData` (
  `TranslatedTopicDataID` int(11) NOT NULL AUTO_INCREMENT,
  `TranslatedXML` mediumtext,
  `TranslatedXMLErrors` text,
  `TranslatedXMLRendered` mediumtext,
  `TranslatedXMLRenderedUpdated` datetime DEFAULT NULL,
  `TranslationLocale` varchar(45) NOT NULL,
  `TranslationPercentage` int(11) NOT NULL,
  `TranslatedTopicID` int(11) NOT NULL,
  PRIMARY KEY (`TranslatedTopicDataID`),
  UNIQUE KEY `TranslatedTopicDataID` (`TranslatedTopicDataID`),
  UNIQUE KEY `TranslatedTopicID` (`TranslatedTopicID`,`TranslationLocale`),
  KEY `FKBEAB41239248FD56` (`TranslatedTopicID`),
  CONSTRAINT `FKBEAB41239248FD56` FOREIGN KEY (`TranslatedTopicID`) REFERENCES `TranslatedTopic` (`TranslatedTopicID`)
) ENGINE=InnoDB AUTO_INCREMENT=2184 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TranslatedTopicString`
--

DROP TABLE IF EXISTS `TranslatedTopicString`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TranslatedTopicString` (
  `TranslatedTopicStringID` int(11) NOT NULL AUTO_INCREMENT,
  `OriginalString` text,
  `TranslatedString` text,
  `TranslatedTopicDataID` int(11) NOT NULL,
  PRIMARY KEY (`TranslatedTopicStringID`),
  UNIQUE KEY `TranslatedTopicStringID` (`TranslatedTopicStringID`),
  KEY `FKDB83374A17E278CA` (`TranslatedTopicDataID`),
  CONSTRAINT `FKDB83374A17E278CA` FOREIGN KEY (`TranslatedTopicDataID`) REFERENCES `TranslatedTopicData` (`TranslatedTopicDataID`)
) ENGINE=InnoDB AUTO_INCREMENT=30864 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `UserID` int(11) NOT NULL AUTO_INCREMENT,
  `UserName` varchar(512) NOT NULL,
  `Description` text,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `index2` (`UserName`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UserRole`
--

DROP TABLE IF EXISTS `UserRole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserRole` (
  `UserRoleID` int(11) NOT NULL AUTO_INCREMENT,
  `UserNameID` int(11) NOT NULL,
  `RoleNameID` int(11) NOT NULL,
  PRIMARY KEY (`UserRoleID`),
  UNIQUE KEY `UserRoleUnique` (`UserNameID`,`RoleNameID`),
  KEY `fk_UserRole_1` (`RoleNameID`),
  KEY `fk_UserRole_2` (`UserNameID`),
  CONSTRAINT `fk_UserRole_1` FOREIGN KEY (`RoleNameID`) REFERENCES `Role` (`RoleID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserRole_2` FOREIGN KEY (`UserNameID`) REFERENCES `User` (`UserID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WorkingSnapshotTranslatedData`
--

DROP TABLE IF EXISTS `WorkingSnapshotTranslatedData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WorkingSnapshotTranslatedData` (
  `WorkingSnapshotTranslatedDataID` int(11) NOT NULL AUTO_INCREMENT,
  `SnapshotTopicID` int(11) NOT NULL,
  `TranslatedXML` longtext,
  `TranslatedXMLRendered` longtext,
  `TranslationLocale` varchar(45) NOT NULL,
  `TranslatedXMLRenderedUpdated` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`WorkingSnapshotTranslatedDataID`),
  UNIQUE KEY `SnapshotTopicID_TranslationLocale_UNIQUE` (`SnapshotTopicID`,`TranslationLocale`),
  KEY `FK17FD8F55FDB66BA` (`SnapshotTopicID`),
  KEY `fk_WorkingSnapshotTranslatedData_1` (`SnapshotTopicID`),
  CONSTRAINT `SnapshotTopicFK` FOREIGN KEY (`SnapshotTopicID`) REFERENCES `SnapshotTopic` (`SnapshotTopicID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=246 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `WorkingSnapshotTranslatedString`
--

DROP TABLE IF EXISTS `WorkingSnapshotTranslatedString`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WorkingSnapshotTranslatedString` (
  `WorkingSnapshotTranslatedStringID` int(11) NOT NULL AUTO_INCREMENT,
  `WorkingSnapshotTranslatedDataID` int(11) NOT NULL,
  `WorkingShapshotOriginalString` text NOT NULL,
  `WorkingSnapshotTranslatedString` text NOT NULL,
  PRIMARY KEY (`WorkingSnapshotTranslatedStringID`),
  KEY `fk_WorkingSnapshotTranslatedString_1` (`WorkingSnapshotTranslatedDataID`),
  KEY `FKBB91229C423C5C4E` (`WorkingSnapshotTranslatedDataID`),
  CONSTRAINT `fk_WorkingSnapshotTranslatedString_1` FOREIGN KEY (`WorkingSnapshotTranslatedDataID`) REFERENCES `WorkingSnapshotTranslatedData` (`WorkingSnapshotTranslatedDataID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-07-26  7:44:45
-- MySQL dump 10.13  Distrib 5.5.25a, for Linux (i686)
--
-- Host: localhost    Database: SkynetTest
-- ------------------------------------------------------
-- Server version	5.5.25a

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `BlobConstants`
--

DROP TABLE IF EXISTS `BlobConstants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BlobConstants` (
  `BlobConstantsID` int(11) NOT NULL AUTO_INCREMENT,
  `ConstantName` varchar(45) NOT NULL,
  `ConstantValue` longblob,
  PRIMARY KEY (`BlobConstantsID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1 COMMENT='This table holds blob constants for things like image files ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BlobConstants`
--

LOCK TABLES `BlobConstants` WRITE;
/*!40000 ALTER TABLE `BlobConstants` DISABLE KEYS */;
/*!40000 ALTER TABLE `BlobConstants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BugzillaBug`
--

DROP TABLE IF EXISTS `BugzillaBug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BugzillaBug` (
  `BugzillaBugID` int(11) NOT NULL AUTO_INCREMENT,
  `BugzillaBugBuzillaID` int(11) NOT NULL,
  `BugzillaBugSummary` text,
  `BugzillaBugOpen` bit(1) DEFAULT NULL,
  `BugzillaBugBugzillaID` int(11) NOT NULL,
  PRIMARY KEY (`BugzillaBugID`)
) ENGINE=InnoDB AUTO_INCREMENT=937 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BugzillaBug`
--

LOCK TABLES `BugzillaBug` WRITE;
/*!40000 ALTER TABLE `BugzillaBug` DISABLE KEYS */;
/*!40000 ALTER TABLE `BugzillaBug` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Category`
--

DROP TABLE IF EXISTS `Category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Category` (
  `CategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `CategoryName` varchar(512) NOT NULL,
  `CategoryDescription` text,
  `CategorySort` int(11) DEFAULT NULL,
  `MutuallyExclusive` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`CategoryID`),
  UNIQUE KEY `index3` (`CategoryName`),
  KEY `fk_Category_1` (`CategoryID`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1 COMMENT='Categories contain tags. The relationship between a tag and ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Category`
--

LOCK TABLES `Category` WRITE;
/*!40000 ALTER TABLE `Category` DISABLE KEYS */;
INSERT INTO `Category` VALUES (1,'Topic Type',NULL,NULL,0),(2,'Concern',NULL,NULL,0),(3,'Technology',NULL,NULL,0),(4,'Release',NULL,NULL,0);
/*!40000 ALTER TABLE `Category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Exception`
--

DROP TABLE IF EXISTS `Exception`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Exception` (
  `ExceptionID` int(11) NOT NULL AUTO_INCREMENT,
  `Timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Details` text NOT NULL,
  `Expected` tinyint(1) NOT NULL,
  `Description` varchar(512) DEFAULT NULL,
  `User` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ExceptionID`)
) ENGINE=InnoDB AUTO_INCREMENT=11286 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Exception`
--

LOCK TABLES `Exception` WRITE;
/*!40000 ALTER TABLE `Exception` DISABLE KEYS */;
/*!40000 ALTER TABLE `Exception` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Filter`
--

DROP TABLE IF EXISTS `Filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Filter` (
  `FilterID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterName` varchar(512) NOT NULL,
  `FilterDescription` text,
  PRIMARY KEY (`FilterID`),
  UNIQUE KEY `index2` (`FilterName`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=latin1 COMMENT='A filter is a saved query. A filter is made up of a row in t';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Filter`
--

LOCK TABLES `Filter` WRITE;
/*!40000 ALTER TABLE `Filter` DISABLE KEYS */;
/*!40000 ALTER TABLE `Filter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FilterCategory`
--

DROP TABLE IF EXISTS `FilterCategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FilterCategory` (
  `FilterCategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterID` int(11) NOT NULL,
  `CategoryID` int(11) NOT NULL,
  `CategoryState` int(11) NOT NULL DEFAULT '0',
  `ProjectID` int(11) DEFAULT NULL,
  PRIMARY KEY (`FilterCategoryID`),
  KEY `fk_FilterCategory_1` (`CategoryID`),
  KEY `fk_FilterCategory_2` (`FilterID`),
  KEY `FKBB45C0B6EB3A6876` (`ProjectID`),
  CONSTRAINT `FKBB45C0B6EB3A6876` FOREIGN KEY (`ProjectID`) REFERENCES `Project` (`ProjectID`),
  CONSTRAINT `fk_FilterCategory_1` FOREIGN KEY (`CategoryID`) REFERENCES `Category` (`CategoryID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_FilterCategory_2` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3731 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FilterCategory`
--

LOCK TABLES `FilterCategory` WRITE;
/*!40000 ALTER TABLE `FilterCategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `FilterCategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FilterField`
--

DROP TABLE IF EXISTS `FilterField`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FilterField` (
  `FilterFieldID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterID` int(11) NOT NULL,
  `Field` varchar(512) NOT NULL,
  `Value` text NOT NULL,
  `Description` text,
  PRIMARY KEY (`FilterFieldID`),
  UNIQUE KEY `index3` (`Field`,`FilterID`),
  KEY `fk_Filter_Field_1` (`FilterID`),
  CONSTRAINT `fk_Filter_Field_1` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FilterField`
--

LOCK TABLES `FilterField` WRITE;
/*!40000 ALTER TABLE `FilterField` DISABLE KEYS */;
/*!40000 ALTER TABLE `FilterField` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FilterLocale`
--

DROP TABLE IF EXISTS `FilterLocale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FilterLocale` (
  `FilterLocaleID` int(11) NOT NULL AUTO_INCREMENT,
  `LocaleName` varchar(512) NOT NULL,
  `LocaleState` int(11) NOT NULL,
  `FilterID` int(11) NOT NULL,
  PRIMARY KEY (`FilterLocaleID`),
  UNIQUE KEY `FilterLocaleID` (`FilterLocaleID`),
  UNIQUE KEY `LocaleName` (`LocaleName`,`FilterID`),
  KEY `FK7CB6A01259256D82` (`FilterID`),
  CONSTRAINT `FK7CB6A01259256D82` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FilterLocale`
--

LOCK TABLES `FilterLocale` WRITE;
/*!40000 ALTER TABLE `FilterLocale` DISABLE KEYS */;
/*!40000 ALTER TABLE `FilterLocale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FilterOption`
--

DROP TABLE IF EXISTS `FilterOption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FilterOption` (
  `FilterOptionID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterOptionName` varchar(512) NOT NULL,
  `FilterOptionValue` varchar(512) NOT NULL,
  `FilterID` int(11) NOT NULL,
  PRIMARY KEY (`FilterOptionID`),
  UNIQUE KEY `FilterOptionID` (`FilterOptionID`),
  UNIQUE KEY `FilterOptionName` (`FilterOptionName`,`FilterID`),
  KEY `FK81EB1A2D59256D82` (`FilterID`),
  CONSTRAINT `FK81EB1A2D59256D82` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`)
) ENGINE=InnoDB AUTO_INCREMENT=432 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FilterOption`
--

LOCK TABLES `FilterOption` WRITE;
/*!40000 ALTER TABLE `FilterOption` DISABLE KEYS */;
/*!40000 ALTER TABLE `FilterOption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FilterTag`
--

DROP TABLE IF EXISTS `FilterTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FilterTag` (
  `FilterTagID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterID` int(11) NOT NULL,
  `TagID` int(11) NOT NULL,
  `TagState` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`FilterTagID`),
  KEY `fk_FilterTag_1` (`TagID`),
  KEY `fk_FilterTag_2` (`FilterID`),
  CONSTRAINT `fk_FilterTag_1` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_FilterTag_2` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=461 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FilterTag`
--

LOCK TABLES `FilterTag` WRITE;
/*!40000 ALTER TABLE `FilterTag` DISABLE KEYS */;
/*!40000 ALTER TABLE `FilterTag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Help`
--

DROP TABLE IF EXISTS `Help`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Help` (
  `HelpID` int(11) NOT NULL AUTO_INCREMENT,
  `TableColID` varchar(512) DEFAULT NULL,
  `HelpText` text,
  PRIMARY KEY (`HelpID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Help`
--

LOCK TABLES `Help` WRITE;
/*!40000 ALTER TABLE `Help` DISABLE KEYS */;
/*!40000 ALTER TABLE `Help` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ImageFile`
--

DROP TABLE IF EXISTS `ImageFile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ImageFile` (
  `ImageFileID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` text,
  PRIMARY KEY (`ImageFileID`)
) ENGINE=InnoDB AUTO_INCREMENT=1086 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ImageFile`
--

LOCK TABLES `ImageFile` WRITE;
/*!40000 ALTER TABLE `ImageFile` DISABLE KEYS */;
/*!40000 ALTER TABLE `ImageFile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IntegerConstants`
--

DROP TABLE IF EXISTS `IntegerConstants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IntegerConstants` (
  `IntegerConstantsID` int(11) NOT NULL AUTO_INCREMENT,
  `ConstantName` varchar(45) NOT NULL,
  `ConstantValue` int(11) DEFAULT NULL,
  PRIMARY KEY (`IntegerConstantsID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IntegerConstants`
--

LOCK TABLES `IntegerConstants` WRITE;
/*!40000 ALTER TABLE `IntegerConstants` DISABLE KEYS */;
/*!40000 ALTER TABLE `IntegerConstants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LanguageImage`
--

DROP TABLE IF EXISTS `LanguageImage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LanguageImage` (
  `LanguageImageID` int(11) NOT NULL AUTO_INCREMENT,
  `ImageFileID` int(11) NOT NULL,
  `ThumbnailData` longblob,
  `ImageDataBase64` longblob,
  `Locale` varchar(512) NOT NULL,
  `OriginalFileName` varchar(2048) DEFAULT NULL,
  `ImageData` longblob,
  PRIMARY KEY (`LanguageImageID`),
  KEY `FK15D2ACC3E0AD6B52` (`ImageFileID`)
) ENGINE=InnoDB AUTO_INCREMENT=1086 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LanguageImage`
--

LOCK TABLES `LanguageImage` WRITE;
/*!40000 ALTER TABLE `LanguageImage` DISABLE KEYS */;
/*!40000 ALTER TABLE `LanguageImage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Project`
--

DROP TABLE IF EXISTS `Project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Project` (
  `ProjectID` int(11) NOT NULL AUTO_INCREMENT,
  `ProjectName` varchar(512) NOT NULL,
  `ProjectDescription` text,
  PRIMARY KEY (`ProjectID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Project`
--

LOCK TABLES `Project` WRITE;
/*!40000 ALTER TABLE `Project` DISABLE KEYS */;
INSERT INTO `Project` VALUES (1,'PressGang CCMS',NULL);
/*!40000 ALTER TABLE `Project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PropertyTag`
--

DROP TABLE IF EXISTS `PropertyTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PropertyTag` (
  `PropertyTagID` int(11) NOT NULL AUTO_INCREMENT,
  `PropertyTagName` varchar(512) NOT NULL,
  `PropertyTagDescription` text,
  `PropertyTagRegex` text NOT NULL,
  `PropertyTagCanBeNull` bit(1) NOT NULL,
  `PropertyTagIsUnqiue` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`PropertyTagID`),
  UNIQUE KEY `PropertyTagUnique` (`PropertyTagName`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PropertyTag`
--

LOCK TABLES `PropertyTag` WRITE;
/*!40000 ALTER TABLE `PropertyTag` DISABLE KEYS */;
/*!40000 ALTER TABLE `PropertyTag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PropertyTagCategory`
--

DROP TABLE IF EXISTS `PropertyTagCategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PropertyTagCategory` (
  `PropertyTagCategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `PropertyTagCategoryName` varchar(512) NOT NULL,
  `PropertyTagCategoryDescription` text,
  PRIMARY KEY (`PropertyTagCategoryID`),
  UNIQUE KEY `PropertyTagCategoryNameUnique` (`PropertyTagCategoryName`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PropertyTagCategory`
--

LOCK TABLES `PropertyTagCategory` WRITE;
/*!40000 ALTER TABLE `PropertyTagCategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `PropertyTagCategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PropertyTagToPropertyTagCategory`
--

DROP TABLE IF EXISTS `PropertyTagToPropertyTagCategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PropertyTagToPropertyTagCategory` (
  `PropertyTagToPropertyTagCategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `PropertyTagID` int(11) NOT NULL,
  `PropertyTagCategoryID` int(11) NOT NULL,
  `Sorting` int(11) DEFAULT NULL,
  PRIMARY KEY (`PropertyTagToPropertyTagCategoryID`),
  UNIQUE KEY `PropertyTagToPropertTagCategoryUnique` (`PropertyTagID`,`PropertyTagCategoryID`),
  KEY `PropertyTagToPropertTagCategoryFk2` (`PropertyTagCategoryID`),
  CONSTRAINT `PropertyTagToPropertTagCategoryFK1` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PropertyTagToPropertTagCategoryFk2` FOREIGN KEY (`PropertyTagCategoryID`) REFERENCES `PropertyTagCategory` (`PropertyTagCategoryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1 COMMENT='PropertyTagToPropertyTagCategoryID\nPropertyTagToPropertyTagC';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PropertyTagToPropertyTagCategory`
--

LOCK TABLES `PropertyTagToPropertyTagCategory` WRITE;
/*!40000 ALTER TABLE `PropertyTagToPropertyTagCategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `PropertyTagToPropertyTagCategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RelationshipTag`
--

DROP TABLE IF EXISTS `RelationshipTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RelationshipTag` (
  `RelationshipTagID` int(11) NOT NULL AUTO_INCREMENT,
  `RelationshipTagName` varchar(512) NOT NULL,
  `RelationshipTagDescription` text,
  PRIMARY KEY (`RelationshipTagID`),
  UNIQUE KEY `RelationshipTagNameUnique` (`RelationshipTagName`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RelationshipTag`
--

LOCK TABLES `RelationshipTag` WRITE;
/*!40000 ALTER TABLE `RelationshipTag` DISABLE KEYS */;
/*!40000 ALTER TABLE `RelationshipTag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Role`
--

DROP TABLE IF EXISTS `Role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Role` (
  `RoleID` int(11) NOT NULL AUTO_INCREMENT,
  `RoleName` varchar(512) NOT NULL,
  `Description` text,
  PRIMARY KEY (`RoleID`),
  UNIQUE KEY `index2` (`RoleName`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Role`
--

LOCK TABLES `Role` WRITE;
/*!40000 ALTER TABLE `Role` DISABLE KEYS */;
/*!40000 ALTER TABLE `Role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RoleToRole`
--

DROP TABLE IF EXISTS `RoleToRole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RoleToRole` (
  `RoleToRoleID` int(11) NOT NULL AUTO_INCREMENT,
  `PrimaryRole` int(11) NOT NULL,
  `RelationshipType` int(11) NOT NULL,
  `SecondaryRole` int(11) NOT NULL,
  PRIMARY KEY (`RoleToRoleID`),
  UNIQUE KEY `RoleToRoleID` (`RoleToRoleID`),
  UNIQUE KEY `PrimaryRole` (`PrimaryRole`,`SecondaryRole`,`RelationshipType`),
  KEY `FKD0A0E5C78433D197` (`SecondaryRole`),
  KEY `FKD0A0E5C7844F7725` (`PrimaryRole`),
  KEY `fk_RoleToRole_1` (`RelationshipType`),
  CONSTRAINT `FKD0A0E5C78433D197` FOREIGN KEY (`SecondaryRole`) REFERENCES `Role` (`RoleID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FKD0A0E5C7844F7725` FOREIGN KEY (`PrimaryRole`) REFERENCES `Role` (`RoleID`),
  CONSTRAINT `fk_RoleToRole_1` FOREIGN KEY (`RelationshipType`) REFERENCES `RoleToRoleRelationship` (`RoleToRoleRelationshipID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RoleToRole`
--

LOCK TABLES `RoleToRole` WRITE;
/*!40000 ALTER TABLE `RoleToRole` DISABLE KEYS */;
/*!40000 ALTER TABLE `RoleToRole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RoleToRoleRelationship`
--

DROP TABLE IF EXISTS `RoleToRoleRelationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RoleToRoleRelationship` (
  `RoleToRoleRelationshipID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` text,
  PRIMARY KEY (`RoleToRoleRelationshipID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RoleToRoleRelationship`
--

LOCK TABLES `RoleToRoleRelationship` WRITE;
/*!40000 ALTER TABLE `RoleToRoleRelationship` DISABLE KEYS */;
/*!40000 ALTER TABLE `RoleToRoleRelationship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Snapshot`
--

DROP TABLE IF EXISTS `Snapshot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Snapshot` (
  `SnapshotID` int(11) NOT NULL AUTO_INCREMENT,
  `SnapshotName` varchar(512) NOT NULL,
  `SnapshotDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`SnapshotID`),
  UNIQUE KEY `index2` (`SnapshotName`,`SnapshotDate`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Snapshot`
--

LOCK TABLES `Snapshot` WRITE;
/*!40000 ALTER TABLE `Snapshot` DISABLE KEYS */;
/*!40000 ALTER TABLE `Snapshot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SnapshotRevision`
--

DROP TABLE IF EXISTS `SnapshotRevision`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SnapshotRevision` (
  `SnapshotRevisionID` int(11) NOT NULL AUTO_INCREMENT,
  `SnapshotID` int(11) NOT NULL,
  `SnapshotRevisionName` varchar(512) NOT NULL,
  `SnapshotRevisionDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`SnapshotRevisionID`),
  KEY `fk_SnapshotRevision_1` (`SnapshotID`),
  CONSTRAINT `fk_SnapshotRevision_1` FOREIGN KEY (`SnapshotID`) REFERENCES `Snapshot` (`SnapshotID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SnapshotRevision`
--

LOCK TABLES `SnapshotRevision` WRITE;
/*!40000 ALTER TABLE `SnapshotRevision` DISABLE KEYS */;
/*!40000 ALTER TABLE `SnapshotRevision` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SnapshotTopic`
--

DROP TABLE IF EXISTS `SnapshotTopic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SnapshotTopic` (
  `SnapshotTopicID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `TopicRevision` int(11) NOT NULL,
  PRIMARY KEY (`SnapshotTopicID`)
) ENGINE=InnoDB AUTO_INCREMENT=246 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SnapshotTopic`
--

LOCK TABLES `SnapshotTopic` WRITE;
/*!40000 ALTER TABLE `SnapshotTopic` DISABLE KEYS */;
/*!40000 ALTER TABLE `SnapshotTopic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SnapshotTopicToSnapshot`
--

DROP TABLE IF EXISTS `SnapshotTopicToSnapshot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SnapshotTopicToSnapshot` (
  `SnapshotTopicToSnapshotID` int(11) NOT NULL AUTO_INCREMENT,
  `SnapshotID` int(11) NOT NULL,
  `SnapshotTopicID` int(11) NOT NULL,
  PRIMARY KEY (`SnapshotTopicToSnapshotID`),
  UNIQUE KEY `SnapshotTopicToiSnapshotSnapshotTopicIDUnique` (`SnapshotTopicID`),
  KEY `FK6E3FD16AFB9C1D5A` (`SnapshotID`),
  KEY `FK6E3FD16A5FDB66BA` (`SnapshotTopicID`),
  KEY `FK6E3FD16A7BC75C04` (`SnapshotTopicID`),
  CONSTRAINT `SnapshotIDFK` FOREIGN KEY (`SnapshotID`) REFERENCES `Snapshot` (`SnapshotID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SnapshotTopicIDFK` FOREIGN KEY (`SnapshotTopicID`) REFERENCES `SnapshotTopic` (`SnapshotTopicID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=246 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SnapshotTopicToSnapshot`
--

LOCK TABLES `SnapshotTopicToSnapshot` WRITE;
/*!40000 ALTER TABLE `SnapshotTopicToSnapshot` DISABLE KEYS */;
/*!40000 ALTER TABLE `SnapshotTopicToSnapshot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SnapshotTranslatedData`
--

DROP TABLE IF EXISTS `SnapshotTranslatedData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SnapshotTranslatedData` (
  `SnapshotTranslatedDataID` int(11) NOT NULL AUTO_INCREMENT,
  `SnapshotRevisionID` int(11) NOT NULL,
  `TranslatedXMLRendered` longtext,
  `TranslationLocale` varchar(45) NOT NULL,
  `SnapshotTopicID` int(11) NOT NULL,
  `TranslatedXML` longtext,
  `TranslatedXMLRenderedUpdated` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`SnapshotTranslatedDataID`),
  KEY `FKE65828A4FDD8B770` (`SnapshotRevisionID`),
  KEY `FKE65828A45FDB66BA` (`SnapshotTopicID`),
  CONSTRAINT `SnapshotRevisionIDFK` FOREIGN KEY (`SnapshotRevisionID`) REFERENCES `SnapshotRevision` (`SnapshotRevisionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=246 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SnapshotTranslatedData`
--

LOCK TABLES `SnapshotTranslatedData` WRITE;
/*!40000 ALTER TABLE `SnapshotTranslatedData` DISABLE KEYS */;
/*!40000 ALTER TABLE `SnapshotTranslatedData` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SnapshotTranslatedString`
--

DROP TABLE IF EXISTS `SnapshotTranslatedString`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SnapshotTranslatedString` (
  `SnapshotTranslatedStringID` int(11) NOT NULL AUTO_INCREMENT,
  `ShapshotOriginalString` text,
  `SnapshotTranslatedString` text,
  `SnapshotTranslatedDataID` int(11) NOT NULL,
  PRIMARY KEY (`SnapshotTranslatedStringID`),
  UNIQUE KEY `SnapshotTranslatedStringsID` (`SnapshotTranslatedStringID`),
  KEY `FKCB94428B7BA9BE5A` (`SnapshotTranslatedDataID`),
  CONSTRAINT `FKCB94428B7BA9BE5A` FOREIGN KEY (`SnapshotTranslatedDataID`) REFERENCES `SnapshotTranslatedData` (`SnapshotTranslatedDataID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SnapshotTranslatedString`
--

LOCK TABLES `SnapshotTranslatedString` WRITE;
/*!40000 ALTER TABLE `SnapshotTranslatedString` DISABLE KEYS */;
/*!40000 ALTER TABLE `SnapshotTranslatedString` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `StringConstants`
--

DROP TABLE IF EXISTS `StringConstants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `StringConstants` (
  `StringConstantsID` int(11) NOT NULL AUTO_INCREMENT,
  `ConstantName` varchar(45) NOT NULL,
  `ConstantValue` longtext,
  PRIMARY KEY (`StringConstantsID`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=latin1 COMMENT='This table holds the string constants used by Skynet, includ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `StringConstants`
--

LOCK TABLES `StringConstants` WRITE;
/*!40000 ALTER TABLE `StringConstants` DISABLE KEYS */;
/*!40000 ALTER TABLE `StringConstants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Tag`
--

DROP TABLE IF EXISTS `Tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Tag` (
  `TagID` int(11) NOT NULL AUTO_INCREMENT,
  `TagName` varchar(512) NOT NULL,
  `TagDescription` text,
  PRIMARY KEY (`TagID`),
  UNIQUE KEY `index4` (`TagName`),
  KEY `fk_Tag_1` (`TagID`),
  KEY `fk_Tag_2` (`TagID`)
) ENGINE=InnoDB AUTO_INCREMENT=501 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tag`
--

LOCK TABLES `Tag` WRITE;
/*!40000 ALTER TABLE `Tag` DISABLE KEYS */;
INSERT INTO `Tag` VALUES (1,'Concept',NULL),(2,'Task',NULL),(3,'Reference',NULL),(4,'Getting Started',NULL),(5,'PressGang CCMS',NULL),(6,'PressGang CCMS 1.0',NULL);
/*!40000 ALTER TABLE `Tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TagExclusion`
--

DROP TABLE IF EXISTS `TagExclusion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TagExclusion` (
  `Tag1ID` int(11) NOT NULL,
  `Tag2ID` int(11) NOT NULL,
  PRIMARY KEY (`Tag1ID`,`Tag2ID`),
  KEY `fk_TagExclusion_1` (`Tag1ID`),
  KEY `fk_TagExclusion_2` (`Tag2ID`),
  CONSTRAINT `fk_TagExclusion_1` FOREIGN KEY (`Tag1ID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_TagExclusion_2` FOREIGN KEY (`Tag2ID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TagExclusion`
--

LOCK TABLES `TagExclusion` WRITE;
/*!40000 ALTER TABLE `TagExclusion` DISABLE KEYS */;
/*!40000 ALTER TABLE `TagExclusion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TagToCategory`
--

DROP TABLE IF EXISTS `TagToCategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TagToCategory` (
  `TagToCategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `TagID` int(11) NOT NULL COMMENT 'References the TagID that is to be placed into the Category.',
  `CategoryID` int(11) NOT NULL COMMENT 'References the CategoryID that the Tag is to be made a child of.',
  `Sorting` int(11) DEFAULT NULL COMMENT 'Defines the sorting order of the Tag in the Category.',
  PRIMARY KEY (`TagToCategoryID`),
  UNIQUE KEY `Unique` (`TagID`,`CategoryID`),
  KEY `fk_TagToCategory_1` (`TagID`),
  KEY `fk_TagToCategory_2` (`CategoryID`),
  KEY `FK8DF417B37D4F054E` (`CategoryID`),
  KEY `FK8DF417B36F9851B8` (`TagID`),
  CONSTRAINT `FK8DF417B36F9851B8` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON UPDATE CASCADE,
  CONSTRAINT `FK8DF417B37D4F054E` FOREIGN KEY (`CategoryID`) REFERENCES `Category` (`CategoryID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=559 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TagToCategory`
--

LOCK TABLES `TagToCategory` WRITE;
/*!40000 ALTER TABLE `TagToCategory` DISABLE KEYS */;
INSERT INTO `TagToCategory` VALUES (553,1,1,NULL),(554,2,1,NULL),(555,3,1,NULL),(556,4,2,NULL),(557,5,3,NULL),(558,6,4,NULL);
/*!40000 ALTER TABLE `TagToCategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TagToProject`
--

DROP TABLE IF EXISTS `TagToProject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TagToProject` (
  `TagToProjectID` int(11) NOT NULL AUTO_INCREMENT,
  `ProjectID` int(11) NOT NULL,
  `TagID` int(11) NOT NULL,
  PRIMARY KEY (`TagToProjectID`),
  UNIQUE KEY `index4` (`ProjectID`,`TagID`),
  KEY `fk_TagToProject_1` (`ProjectID`),
  KEY `fk_TagToProject_2` (`TagID`),
  CONSTRAINT `fk_TagToProject_1` FOREIGN KEY (`ProjectID`) REFERENCES `Project` (`ProjectID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_TagToProject_2` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=559 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TagToProject`
--

LOCK TABLES `TagToProject` WRITE;
/*!40000 ALTER TABLE `TagToProject` DISABLE KEYS */;
INSERT INTO `TagToProject` VALUES (557,1,5),(558,1,6);
/*!40000 ALTER TABLE `TagToProject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TagToPropertyTag`
--

DROP TABLE IF EXISTS `TagToPropertyTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TagToPropertyTag` (
  `TagToPropertyTagID` int(11) NOT NULL AUTO_INCREMENT,
  `TagID` int(11) NOT NULL,
  `PropertyTagID` int(11) NOT NULL,
  `Value` longtext,
  PRIMARY KEY (`TagToPropertyTagID`),
  KEY `TagIDFK` (`TagID`),
  KEY `PropertyTagIDFK` (`PropertyTagID`),
  CONSTRAINT `TagToPropertyTagFK1` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `TagToPropertyTagFK2` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=163 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TagToPropertyTag`
--

LOCK TABLES `TagToPropertyTag` WRITE;
/*!40000 ALTER TABLE `TagToPropertyTag` DISABLE KEYS */;
/*!40000 ALTER TABLE `TagToPropertyTag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TagToTag`
--

DROP TABLE IF EXISTS `TagToTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TagToTag` (
  `TagToTagID` int(11) NOT NULL AUTO_INCREMENT,
  `PrimaryTagID` int(11) NOT NULL,
  `SecondaryTagID` int(11) NOT NULL,
  `RelationshipType` int(11) NOT NULL,
  PRIMARY KEY (`TagToTagID`),
  UNIQUE KEY `UNIQUE` (`PrimaryTagID`,`SecondaryTagID`,`RelationshipType`),
  KEY `fk_TagToTag_1` (`RelationshipType`),
  KEY `fk_TagToTag_2` (`PrimaryTagID`),
  KEY `fk_TagToTag_3` (`SecondaryTagID`),
  CONSTRAINT `fk_TagToTag_1` FOREIGN KEY (`RelationshipType`) REFERENCES `TagToTagRelationship` (`TagToTagRelationshipType`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_TagToTag_2` FOREIGN KEY (`PrimaryTagID`) REFERENCES `Tag` (`TagID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_TagToTag_3` FOREIGN KEY (`SecondaryTagID`) REFERENCES `Tag` (`TagID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TagToTag`
--

LOCK TABLES `TagToTag` WRITE;
/*!40000 ALTER TABLE `TagToTag` DISABLE KEYS */;
/*!40000 ALTER TABLE `TagToTag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TagToTagRelationship`
--

DROP TABLE IF EXISTS `TagToTagRelationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TagToTagRelationship` (
  `TagToTagRelationshipType` int(11) NOT NULL,
  `TagToTagRelationshipDescription` text,
  PRIMARY KEY (`TagToTagRelationshipType`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TagToTagRelationship`
--

LOCK TABLES `TagToTagRelationship` WRITE;
/*!40000 ALTER TABLE `TagToTagRelationship` DISABLE KEYS */;
/*!40000 ALTER TABLE `TagToTagRelationship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Topic`
--

DROP TABLE IF EXISTS `Topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Topic` (
  `TopicID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicTitle` varchar(512) DEFAULT NULL,
  `TopicText` text,
  `TopicTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `TopicSVNURL` varchar(512) DEFAULT NULL,
  `TopicXML` mediumtext,
  `TopicLocale` varchar(512) NOT NULL,
  `TopicRendered` text,
  PRIMARY KEY (`TopicID`),
  KEY `fk_Topic_1` (`TopicID`),
  KEY `FK4D3DD0FE2436E96` (`TopicID`)
) ENGINE=InnoDB AUTO_INCREMENT=10404 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Topic`
--

LOCK TABLES `Topic` WRITE;
/*!40000 ALTER TABLE `Topic` DISABLE KEYS */;
/*!40000 ALTER TABLE `Topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TopicSecondOrderData`
--

DROP TABLE IF EXISTS `TopicSecondOrderData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicSecondOrderData` (
  `TopicSecondOrderDataID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicXMLErrors` longtext,
  `TopicHTMLView` mediumtext,
  `TopicID` int(11) NOT NULL,
  PRIMARY KEY (`TopicSecondOrderDataID`)
) ENGINE=InnoDB AUTO_INCREMENT=10245 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TopicSecondOrderData`
--

LOCK TABLES `TopicSecondOrderData` WRITE;
/*!40000 ALTER TABLE `TopicSecondOrderData` DISABLE KEYS */;
/*!40000 ALTER TABLE `TopicSecondOrderData` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TopicSourceURL`
--

DROP TABLE IF EXISTS `TopicSourceURL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicSourceURL` (
  `TopicSourceURLID` int(11) NOT NULL AUTO_INCREMENT,
  `SourceURL` varchar(2048) NOT NULL,
  `Description` text,
  `Title` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`TopicSourceURLID`)
) ENGINE=InnoDB AUTO_INCREMENT=12898 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TopicSourceURL`
--

LOCK TABLES `TopicSourceURL` WRITE;
/*!40000 ALTER TABLE `TopicSourceURL` DISABLE KEYS */;
/*!40000 ALTER TABLE `TopicSourceURL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TopicToBugzillaBug`
--

DROP TABLE IF EXISTS `TopicToBugzillaBug`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicToBugzillaBug` (
  `TopicToBugzillaBugID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `BugzillaBugID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToBugzillaBugID`),
  UNIQUE KEY `TopicIDBugzillaBugIDUnique` (`TopicID`,`BugzillaBugID`),
  UNIQUE KEY `TopicToBugzillaBugBugzillaBugIDUnique` (`BugzillaBugID`),
  KEY `TopicToBugzillaBugFK2` (`BugzillaBugID`),
  CONSTRAINT `TopicToBugzillaBugFK2` FOREIGN KEY (`BugzillaBugID`) REFERENCES `BugzillaBug` (`BugzillaBugID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `TopicToBugzillaBug_ibfk_1` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=935 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TopicToBugzillaBug`
--

LOCK TABLES `TopicToBugzillaBug` WRITE;
/*!40000 ALTER TABLE `TopicToBugzillaBug` DISABLE KEYS */;
/*!40000 ALTER TABLE `TopicToBugzillaBug` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TopicToPropertyTag`
--

DROP TABLE IF EXISTS `TopicToPropertyTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicToPropertyTag` (
  `TopicToPropertyTagID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `PropertyTagID` int(11) NOT NULL,
  `Value` longtext,
  PRIMARY KEY (`TopicToPropertyTagID`),
  KEY `TopicToPropertyTagFK1` (`TopicID`),
  KEY `TopicToPropertyTagFK2` (`PropertyTagID`),
  CONSTRAINT `TopicToPropertyTagFK1` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `TopicToPropertyTagFK2` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=127470 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TopicToPropertyTag`
--

LOCK TABLES `TopicToPropertyTag` WRITE;
/*!40000 ALTER TABLE `TopicToPropertyTag` DISABLE KEYS */;
/*!40000 ALTER TABLE `TopicToPropertyTag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TopicToTag`
--

DROP TABLE IF EXISTS `TopicToTag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicToTag` (
  `TopicToTagID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `TagID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToTagID`),
  UNIQUE KEY `UNIQUE` (`TopicID`,`TagID`),
  KEY `fk_TopicToTag_1` (`TopicID`),
  KEY `fk_TopicToTag_2` (`TagID`),
  CONSTRAINT `fk_TopicToTag_1` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_TopicToTag_2` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=91075 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TopicToTag`
--

LOCK TABLES `TopicToTag` WRITE;
/*!40000 ALTER TABLE `TopicToTag` DISABLE KEYS */;
/*!40000 ALTER TABLE `TopicToTag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TopicToTopic`
--

DROP TABLE IF EXISTS `TopicToTopic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicToTopic` (
  `TopicToTopicID` int(11) NOT NULL AUTO_INCREMENT,
  `MainTopicID` int(11) NOT NULL,
  `RelatedTopicID` int(11) NOT NULL,
  `RelationshipTagID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToTopicID`),
  UNIQUE KEY `TopicToTopicUnique` (`MainTopicID`,`RelatedTopicID`,`RelationshipTagID`),
  KEY `fk_TopicToTopic_1` (`MainTopicID`),
  KEY `fk_TopicToTopic_2` (`RelatedTopicID`),
  KEY `fk_TopicToTopic_3` (`RelationshipTagID`),
  CONSTRAINT `fk_TopicToTopic_1` FOREIGN KEY (`MainTopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_TopicToTopic_2` FOREIGN KEY (`RelatedTopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_TopicToTopic_3` FOREIGN KEY (`RelationshipTagID`) REFERENCES `RelationshipTag` (`RelationshipTagID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7365 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TopicToTopic`
--

LOCK TABLES `TopicToTopic` WRITE;
/*!40000 ALTER TABLE `TopicToTopic` DISABLE KEYS */;
/*!40000 ALTER TABLE `TopicToTopic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TopicToTopicSecondOrderData`
--

DROP TABLE IF EXISTS `TopicToTopicSecondOrderData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicToTopicSecondOrderData` (
  `TopicToTopicSecondOrderDataID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `TopicSecondOrderDataID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToTopicSecondOrderDataID`),
  UNIQUE KEY `TopicToTopicSecondOrderDataTopicIdUnique` (`TopicID`),
  UNIQUE KEY `TopicToTopicSecondOrderDataTopicSecondOrderDataIdUnique` (`TopicSecondOrderDataID`),
  KEY `FK303D33DF3340562` (`TopicID`),
  KEY `fk_TopicToTopicSecondOrderData_1` (`TopicSecondOrderDataID`),
  CONSTRAINT `FK303D33DF3340562` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_TopicToTopicSecondOrderData_1` FOREIGN KEY (`TopicSecondOrderDataID`) REFERENCES `TopicSecondOrderData` (`TopicSecondOrderDataID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10244 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TopicToTopicSecondOrderData`
--

LOCK TABLES `TopicToTopicSecondOrderData` WRITE;
/*!40000 ALTER TABLE `TopicToTopicSecondOrderData` DISABLE KEYS */;
/*!40000 ALTER TABLE `TopicToTopicSecondOrderData` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TopicToTopicSourceURL`
--

DROP TABLE IF EXISTS `TopicToTopicSourceURL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TopicToTopicSourceURL` (
  `TopicToTopicSourceURLID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `TopicSourceURLID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToTopicSourceURLID`),
  UNIQUE KEY `UniqueIndex` (`TopicID`,`TopicSourceURLID`),
  UNIQUE KEY `UniqueTopicSourceURLID` (`TopicSourceURLID`),
  KEY `ForeignTopicSourceURLID` (`TopicSourceURLID`),
  CONSTRAINT `ForeignTopicID` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ForeignTopicSourceURLID` FOREIGN KEY (`TopicSourceURLID`) REFERENCES `TopicSourceURL` (`TopicSourceURLID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12883 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TopicToTopicSourceURL`
--

LOCK TABLES `TopicToTopicSourceURL` WRITE;
/*!40000 ALTER TABLE `TopicToTopicSourceURL` DISABLE KEYS */;
/*!40000 ALTER TABLE `TopicToTopicSourceURL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TranslatedTopic`
--

DROP TABLE IF EXISTS `TranslatedTopic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TranslatedTopic` (
  `TranslatedTopicID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `TopicRevision` int(11) NOT NULL,
  PRIMARY KEY (`TranslatedTopicID`),
  UNIQUE KEY `TranslatedTopicID` (`TranslatedTopicID`),
  UNIQUE KEY `TopicRevision` (`TopicRevision`,`TopicID`)
) ENGINE=InnoDB AUTO_INCREMENT=973 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TranslatedTopic`
--

LOCK TABLES `TranslatedTopic` WRITE;
/*!40000 ALTER TABLE `TranslatedTopic` DISABLE KEYS */;
/*!40000 ALTER TABLE `TranslatedTopic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TranslatedTopicData`
--

DROP TABLE IF EXISTS `TranslatedTopicData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TranslatedTopicData` (
  `TranslatedTopicDataID` int(11) NOT NULL AUTO_INCREMENT,
  `TranslatedXML` mediumtext,
  `TranslatedXMLErrors` text,
  `TranslatedXMLRendered` mediumtext,
  `TranslatedXMLRenderedUpdated` datetime DEFAULT NULL,
  `TranslationLocale` varchar(45) NOT NULL,
  `TranslationPercentage` int(11) NOT NULL,
  `TranslatedTopicID` int(11) NOT NULL,
  PRIMARY KEY (`TranslatedTopicDataID`),
  UNIQUE KEY `TranslatedTopicDataID` (`TranslatedTopicDataID`),
  UNIQUE KEY `TranslatedTopicID` (`TranslatedTopicID`,`TranslationLocale`),
  KEY `FKBEAB41239248FD56` (`TranslatedTopicID`),
  CONSTRAINT `FKBEAB41239248FD56` FOREIGN KEY (`TranslatedTopicID`) REFERENCES `TranslatedTopic` (`TranslatedTopicID`)
) ENGINE=InnoDB AUTO_INCREMENT=2184 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TranslatedTopicData`
--

LOCK TABLES `TranslatedTopicData` WRITE;
/*!40000 ALTER TABLE `TranslatedTopicData` DISABLE KEYS */;
/*!40000 ALTER TABLE `TranslatedTopicData` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TranslatedTopicString`
--

DROP TABLE IF EXISTS `TranslatedTopicString`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `TranslatedTopicString` (
  `TranslatedTopicStringID` int(11) NOT NULL AUTO_INCREMENT,
  `OriginalString` text,
  `TranslatedString` text,
  `TranslatedTopicDataID` int(11) NOT NULL,
  PRIMARY KEY (`TranslatedTopicStringID`),
  UNIQUE KEY `TranslatedTopicStringID` (`TranslatedTopicStringID`),
  KEY `FKDB83374A17E278CA` (`TranslatedTopicDataID`),
  CONSTRAINT `FKDB83374A17E278CA` FOREIGN KEY (`TranslatedTopicDataID`) REFERENCES `TranslatedTopicData` (`TranslatedTopicDataID`)
) ENGINE=InnoDB AUTO_INCREMENT=30864 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TranslatedTopicString`
--

LOCK TABLES `TranslatedTopicString` WRITE;
/*!40000 ALTER TABLE `TranslatedTopicString` DISABLE KEYS */;
/*!40000 ALTER TABLE `TranslatedTopicString` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `UserID` int(11) NOT NULL AUTO_INCREMENT,
  `UserName` varchar(512) NOT NULL,
  `Description` text,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `index2` (`UserName`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserRole`
--

DROP TABLE IF EXISTS `UserRole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserRole` (
  `UserRoleID` int(11) NOT NULL AUTO_INCREMENT,
  `UserNameID` int(11) NOT NULL,
  `RoleNameID` int(11) NOT NULL,
  PRIMARY KEY (`UserRoleID`),
  UNIQUE KEY `UserRoleUnique` (`UserNameID`,`RoleNameID`),
  KEY `fk_UserRole_1` (`RoleNameID`),
  KEY `fk_UserRole_2` (`UserNameID`),
  CONSTRAINT `fk_UserRole_1` FOREIGN KEY (`RoleNameID`) REFERENCES `Role` (`RoleID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserRole_2` FOREIGN KEY (`UserNameID`) REFERENCES `User` (`UserID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserRole`
--

LOCK TABLES `UserRole` WRITE;
/*!40000 ALTER TABLE `UserRole` DISABLE KEYS */;
/*!40000 ALTER TABLE `UserRole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `WorkingSnapshotTranslatedData`
--

DROP TABLE IF EXISTS `WorkingSnapshotTranslatedData`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WorkingSnapshotTranslatedData` (
  `WorkingSnapshotTranslatedDataID` int(11) NOT NULL AUTO_INCREMENT,
  `SnapshotTopicID` int(11) NOT NULL,
  `TranslatedXML` longtext,
  `TranslatedXMLRendered` longtext,
  `TranslationLocale` varchar(45) NOT NULL,
  `TranslatedXMLRenderedUpdated` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`WorkingSnapshotTranslatedDataID`),
  UNIQUE KEY `SnapshotTopicID_TranslationLocale_UNIQUE` (`SnapshotTopicID`,`TranslationLocale`),
  KEY `FK17FD8F55FDB66BA` (`SnapshotTopicID`),
  KEY `fk_WorkingSnapshotTranslatedData_1` (`SnapshotTopicID`),
  CONSTRAINT `SnapshotTopicFK` FOREIGN KEY (`SnapshotTopicID`) REFERENCES `SnapshotTopic` (`SnapshotTopicID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=246 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `WorkingSnapshotTranslatedData`
--

LOCK TABLES `WorkingSnapshotTranslatedData` WRITE;
/*!40000 ALTER TABLE `WorkingSnapshotTranslatedData` DISABLE KEYS */;
/*!40000 ALTER TABLE `WorkingSnapshotTranslatedData` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `WorkingSnapshotTranslatedString`
--

DROP TABLE IF EXISTS `WorkingSnapshotTranslatedString`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WorkingSnapshotTranslatedString` (
  `WorkingSnapshotTranslatedStringID` int(11) NOT NULL AUTO_INCREMENT,
  `WorkingSnapshotTranslatedDataID` int(11) NOT NULL,
  `WorkingShapshotOriginalString` text NOT NULL,
  `WorkingSnapshotTranslatedString` text NOT NULL,
  PRIMARY KEY (`WorkingSnapshotTranslatedStringID`),
  KEY `fk_WorkingSnapshotTranslatedString_1` (`WorkingSnapshotTranslatedDataID`),
  KEY `FKBB91229C423C5C4E` (`WorkingSnapshotTranslatedDataID`),
  CONSTRAINT `fk_WorkingSnapshotTranslatedString_1` FOREIGN KEY (`WorkingSnapshotTranslatedDataID`) REFERENCES `WorkingSnapshotTranslatedData` (`WorkingSnapshotTranslatedDataID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `WorkingSnapshotTranslatedString`
--

LOCK TABLES `WorkingSnapshotTranslatedString` WRITE;
/*!40000 ALTER TABLE `WorkingSnapshotTranslatedString` DISABLE KEYS */;
/*!40000 ALTER TABLE `WorkingSnapshotTranslatedString` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-07-26  7:47:57