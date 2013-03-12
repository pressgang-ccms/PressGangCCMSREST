-- phpMyAdmin SQL Dump
-- version 3.5.3
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 08, 2012 at 06:17 PM
-- Server version: 5.5.28
-- PHP Version: 5.3.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `Skynet`
--
 CREATE DATABASE /*!32312 IF NOT EXISTS*/ `PressGangCCMS` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `Skynet`;

-- --------------------------------------------------------

--
-- Table structure for table `BlobConstants`
--

CREATE TABLE IF NOT EXISTS `BlobConstants` (
  `BlobConstantsID` INT(11)     NOT NULL AUTO_INCREMENT,
  `ConstantName`    VARCHAR(45) NOT NULL,
  `ConstantValue`   LONGBLOB,
  PRIMARY KEY (`BlobConstantsID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'This table holds blob constants for things like image files ';

-- --------------------------------------------------------

--
-- Table structure for table `BlobConstants_AUD`
--

CREATE TABLE IF NOT EXISTS `BlobConstants_AUD` (
  `BlobConstantsID` INT(11) NOT NULL,
  `REV`             INT(11) NOT NULL,
  `REVEND`          INT(11) DEFAULT NULL,
  `REVTYPE`         TINYINT(4) DEFAULT NULL,
  `ConstantName`    VARCHAR(45) DEFAULT NULL,
  `ConstantValue`   LONGBLOB,
  PRIMARY KEY (`BlobConstantsID`, `REV`),
  KEY `FKEEA7C6E3DF74E053` (`REV`),
  KEY `FKEEA7C6E3A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `BugzillaBug`
--

CREATE TABLE IF NOT EXISTS `BugzillaBug` (
  `BugzillaBugID`         INT(11) NOT NULL AUTO_INCREMENT,
  `BugzillaBugBuzillaID`  INT(11) NOT NULL,
  `BugzillaBugSummary`    TEXT,
  `BugzillaBugOpen`       BIT(1) DEFAULT NULL,
  `BugzillaBugBugzillaID` INT(11) NOT NULL,
  PRIMARY KEY (`BugzillaBugID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `BugzillaBug_AUD`
--

CREATE TABLE IF NOT EXISTS `BugzillaBug_AUD` (
  `BugzillaBugID`         INT(11) NOT NULL,
  `REV`                   INT(11) NOT NULL,
  `REVEND`                INT(11) DEFAULT NULL,
  `REVTYPE`               TINYINT(4) DEFAULT NULL,
  `BugzillaBugBugzillaID` INT(11) DEFAULT NULL,
  `BugzillaBugOpen`       BIT(1) DEFAULT NULL,
  `BugzillaBugSummary`    TEXT,
  PRIMARY KEY (`BugzillaBugID`, `REV`),
  KEY `FK8122C0E7DF74E053` (`REV`),
  KEY `FK8122C0E7A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Category`
--

CREATE TABLE IF NOT EXISTS `Category` (
  `CategoryID`          INT(11)      NOT NULL AUTO_INCREMENT,
  `CategoryName`        VARCHAR(255) NOT NULL,
  `CategoryDescription` TEXT,
  `CategorySort`        INT(11) DEFAULT NULL,
  `MutuallyExclusive`   TINYINT(1)   NOT NULL DEFAULT '0',
  PRIMARY KEY (`CategoryID`),
  UNIQUE KEY `index3` (`CategoryName`),
  KEY `fk_Category_1` (`CategoryID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'Categories contain tags. The relationship between a tag and ';

-- --------------------------------------------------------

--
-- Table structure for table `Category_AUD`
--

CREATE TABLE IF NOT EXISTS `Category_AUD` (
  `CategoryID`          INT(11) NOT NULL,
  `REV`                 INT(11) NOT NULL,
  `REVEND`              INT(11) DEFAULT NULL,
  `REVTYPE`             TINYINT(4) DEFAULT NULL,
  `CategoryDescription` TEXT,
  `CategoryName`        VARCHAR(255) DEFAULT NULL,
  `CategorySort`        INT(11) DEFAULT NULL,
  `MutuallyExclusive`   BIT(1) DEFAULT NULL,
  PRIMARY KEY (`CategoryID`, `REV`),
  KEY `FK23378FEFDF74E053` (`REV`),
  KEY `FK23378FEFA7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpec`
--

CREATE TABLE IF NOT EXISTS `ContentSpec` (
  `ContentSpecID`    INT(11)      NOT NULL AUTO_INCREMENT,
  `ContentSpecTitle` VARCHAR(255) NOT NULL,
  `ContentSpecType`  INT(11)      NOT NULL,
  `Locale`           VARCHAR(512) NOT NULL,
  `lastPublished`    DATETIME DEFAULT NULL,
  PRIMARY KEY (`ContentSpecID`),
  UNIQUE KEY `ContentSpecID` (`ContentSpecID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecMetaData`
--

CREATE TABLE IF NOT EXISTS `ContentSpecMetaData` (
  `ContentSpecMetaDataID`          INT(11)      NOT NULL AUTO_INCREMENT,
  `ContentSpecMetaDataTitle`       VARCHAR(255) NOT NULL,
  `ContentSpecMetaDataDescription` TEXT,
  PRIMARY KEY (`ContentSpecMetaDataID`),
  UNIQUE KEY `ContentSpecMetaDataID` (`ContentSpecMetaDataID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecMetaData_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecMetaData_AUD` (
  `ContentSpecMetaDataID`          INT(11) NOT NULL,
  `REV`                            INT(11) NOT NULL,
  `REVTYPE`                        TINYINT(4) DEFAULT NULL,
  `REVEND`                         INT(11) DEFAULT NULL,
  `ContentSpecMetaDataTitle`       VARCHAR(255) DEFAULT NULL,
  `ContentSpecMetaDataDescription` TEXT,
  PRIMARY KEY (`ContentSpecMetaDataID`, `REV`),
  KEY `FK6DBBA77416D0EC8F` (`REVEND`),
  KEY `FK6DBBA7744E83BBDA` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecNode`
--

CREATE TABLE IF NOT EXISTS `ContentSpecNode` (
  `ContentSpecNodeID` INT(11)      NOT NULL AUTO_INCREMENT,
  `NodeTitle`         VARCHAR(255) NOT NULL,
  `NodeType`          INT(11)      NOT NULL,
  `NodeCondition`     VARCHAR(255) DEFAULT NULL,
  `Flag`              INT(11) DEFAULT NULL,
  `TopicID`           INT(11) DEFAULT NULL,
  `TopicRevision`     INT(11) DEFAULT NULL,
  `ContentSpecID`     INT(11) DEFAULT NULL,
  `NextNodeID`        INT(11) DEFAULT NULL,
  `ParentID`          INT(11) DEFAULT NULL,
  `PreviousNodeID`    INT(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecNodeID`),
  UNIQUE KEY `ContentSpecNodeID` (`ContentSpecNodeID`),
  UNIQUE KEY `ContentSpecID` (`ContentSpecID`, `PreviousNodeID`),
  UNIQUE KEY `ContentSpecID_2` (`ContentSpecID`, `NextNodeID`),
  KEY `FKD5DB8BD6ED197BA6` (`ContentSpecID`),
  KEY `FKD5DB8BD6367D70E3` (`PreviousNodeID`),
  KEY `FKD5DB8BD6EBE853DF` (`NextNodeID`),
  KEY `FKD5DB8BD64F855BD4` (`ParentID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecNodeToContentSpecNode`
--

CREATE TABLE IF NOT EXISTS `ContentSpecNodeToContentSpecNode` (
  `ContentSpecNodeToContentSpecNodeID` INT(11) NOT NULL AUTO_INCREMENT,
  `RelationshipType`                   INT(11) NOT NULL,
  `MainNodeID`                         INT(11) NOT NULL,
  `RelatedNodeID`                      INT(11) NOT NULL,
  PRIMARY KEY (`ContentSpecNodeToContentSpecNodeID`),
  UNIQUE KEY `ContentSpecNodeToContentSpecNodeID` (`ContentSpecNodeToContentSpecNodeID`),
  KEY `FKDF1672C540009B17` (`RelatedNodeID`),
  KEY `FKDF1672C552F62225` (`MainNodeID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecNodeToContentSpecNode_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecNodeToContentSpecNode_AUD` (
  `ContentSpecNodeToContentSpecNodeID` INT(11) NOT NULL,
  `REV`                                INT(11) NOT NULL,
  `REVTYPE`                            TINYINT(4) DEFAULT NULL,
  `REVEND`                             INT(11) DEFAULT NULL,
  `RelationshipType`                   INT(11) DEFAULT NULL,
  `MainNodeID`                         INT(11) DEFAULT NULL,
  `RelatedNodeID`                      INT(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecNodeToContentSpecNodeID`, `REV`),
  KEY `FKB591B61616D0EC8F` (`REVEND`),
  KEY `FKB591B6164E83BBDA` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecNodeToPropertyTag`
--

CREATE TABLE IF NOT EXISTS `ContentSpecNodeToPropertyTag` (
  `ContentSpecNodeToPropertyTagID` INT(11) NOT NULL AUTO_INCREMENT,
  `Value`                          TEXT,
  `ContentSpecNodeID`              INT(11) NOT NULL,
  `PropertyTagID`                  INT(11) NOT NULL,
  PRIMARY KEY (`ContentSpecNodeToPropertyTagID`),
  UNIQUE KEY `ContentSpecNodeToPropertyTagID` (`ContentSpecNodeToPropertyTagID`),
  KEY `FK4F3E7BD4ABA6BBE0` (`ContentSpecNodeID`),
  KEY `FK4F3E7BD49612C5C2` (`PropertyTagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecNodeToPropertyTag_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecNodeToPropertyTag_AUD` (
  `ContentSpecNodeToPropertyTagID` INT(11) NOT NULL,
  `REV`                            INT(11) NOT NULL,
  `REVTYPE`                        TINYINT(4) DEFAULT NULL,
  `REVEND`                         INT(11) DEFAULT NULL,
  `Value`                          TEXT,
  `ContentSpecNodeID`              INT(11) DEFAULT NULL,
  `PropertyTagID`                  INT(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecNodeToPropertyTagID`, `REV`),
  KEY `FK51609FA5D25E3A3B` (`REVEND`),
  KEY `FK51609FA5A110986` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecNode_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecNode_AUD` (
  `ContentSpecNodeID` INT(11) NOT NULL,
  `REV`               INT(11) NOT NULL,
  `REVTYPE`           TINYINT(4) DEFAULT NULL,
  `REVEND`            INT(11) DEFAULT NULL,
  `NodeTitle`         VARCHAR(255) DEFAULT NULL,
  `NodeType`          INT(11) DEFAULT NULL,
  `NodeCondition`     VARCHAR(255) DEFAULT NULL,
  `Flag`              INT(11) DEFAULT NULL,
  `TopicID`           INT(11) DEFAULT NULL,
  `TopicRevision`     INT(11) DEFAULT NULL,
  `ContentSpecID`     INT(11) DEFAULT NULL,
  `NextNodeID`        INT(11) DEFAULT NULL,
  `ParentID`          INT(11) DEFAULT NULL,
  `PreviousNodeID`    INT(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecNodeID`, `REV`),
  KEY `FK2311DEA7D25E3A3B` (`REVEND`),
  KEY `FK2311DEA7A110986` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToContentSpecMetaData`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToContentSpecMetaData` (
  `ContentSpecToContentSpecMetaDataID` INT(11) NOT NULL AUTO_INCREMENT,
  `Value`                              TEXT,
  `ContentSpecMetaDataID`              INT(11) NOT NULL,
  `ContentSpecID`                      INT(11) NOT NULL,
  PRIMARY KEY (`ContentSpecToContentSpecMetaDataID`),
  UNIQUE KEY `ContentSpecToContentSpecMetaDataID` (`ContentSpecToContentSpecMetaDataID`),
  KEY `FKEFF3F974CA8809D2` (`ContentSpecID`),
  KEY `FKEFF3F9747E34A6EE` (`ContentSpecMetaDataID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToContentSpecMetaData_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToContentSpecMetaData_AUD` (
  `ContentSpecToContentSpecMetaDataID` INT(11) NOT NULL,
  `REV`                                INT(11) NOT NULL,
  `REVTYPE`                            TINYINT(4) DEFAULT NULL,
  `REVEND`                             INT(11) DEFAULT NULL,
  `Value`                              TEXT,
  `ContentSpecMetaDataID`              INT(11) DEFAULT NULL,
  `ContentSpecID`                      INT(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecToContentSpecMetaDataID`, `REV`),
  KEY `FK79DE4D4516D0EC8F` (`REVEND`),
  KEY `FK79DE4D454E83BBDA` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToProject`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToProject` (
  `ContentSpecToProjectID` INT(11) NOT NULL AUTO_INCREMENT,
  `ContentSpecID`          INT(11) NOT NULL,
  `ProjectID`              INT(11) NOT NULL,
  PRIMARY KEY (`ContentSpecToProjectID`),
  UNIQUE KEY `ContentSpecToProjectID` (`ContentSpecToProjectID`),
  KEY `FKADEB4ACACA8809D2` (`ContentSpecID`),
  KEY `FKADEB4ACAEB3A6876` (`ProjectID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToProject_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToProject_AUD` (
  `ContentSpecToProjectID` INT(11) NOT NULL,
  `REV`                    INT(11) NOT NULL,
  `REVTYPE`                TINYINT(4) DEFAULT NULL,
  `REVEND`                 INT(11) DEFAULT NULL,
  `ContentSpecID`          INT(11) DEFAULT NULL,
  `ProjectID`              INT(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecToProjectID`, `REV`),
  KEY `FKDE81039B16D0EC8F` (`REVEND`),
  KEY `FKDE81039B4E83BBDA` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToPropertyTag`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToPropertyTag` (
  `ContentSpecToPropertyTagID` INT(11) NOT NULL AUTO_INCREMENT,
  `Value`                      TEXT,
  `ContentSpecID`              INT(11) NOT NULL,
  `PropertyTagID`              INT(11) NOT NULL,
  PRIMARY KEY (`ContentSpecToPropertyTagID`),
  UNIQUE KEY `ContentSpecToPropertyTagID` (`ContentSpecToPropertyTagID`),
  KEY `FK676CDCB6CA8809D2` (`ContentSpecID`),
  KEY `FK676CDCB6F3A435EE` (`PropertyTagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToPropertyTag_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToPropertyTag_AUD` (
  `ContentSpecToPropertyTagID` INT(11) NOT NULL,
  `REV`                        INT(11) NOT NULL,
  `REVTYPE`                    TINYINT(4) DEFAULT NULL,
  `REVEND`                     INT(11) DEFAULT NULL,
  `Value`                      TEXT,
  `ContentSpecID`              INT(11) DEFAULT NULL,
  `PropertyTagID`              INT(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecToPropertyTagID`, `REV`),
  KEY `FKF7CFBF8716D0EC8F` (`REVEND`),
  KEY `FKF7CFBF874E83BBDA` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToTag`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToTag` (
  `ContentSpecToTagID` INT(11) NOT NULL AUTO_INCREMENT,
  `ContentSpecID`      INT(11) NOT NULL,
  `TagID`              INT(11) NOT NULL,
  PRIMARY KEY (`ContentSpecToTagID`),
  UNIQUE KEY `ContentSpecToTagID` (`ContentSpecToTagID`),
  UNIQUE KEY `ContentSpecID` (`ContentSpecID`, `TagID`),
  KEY `FKE5EA3FCBED197BA6` (`ContentSpecID`),
  KEY `FKE5EA3FCB9BADF58C` (`TagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToTag_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToTag_AUD` (
  `ContentSpecToTagID` INT(11) NOT NULL,
  `REV`                INT(11) NOT NULL,
  `REVTYPE`            TINYINT(4) DEFAULT NULL,
  `REVEND`             INT(11) DEFAULT NULL,
  `ContentSpecID`      INT(11) DEFAULT NULL,
  `TagID`              INT(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecToTagID`, `REV`),
  KEY `FK640B901CD25E3A3B` (`REVEND`),
  KEY `FK640B901CA110986` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecTranslatedString`
--

CREATE TABLE IF NOT EXISTS `ContentSpecTranslatedString` (
  `ContentSpecTranslatedStringID` INT(11)      NOT NULL AUTO_INCREMENT,
  `Locale`                        VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ContentSpecTranslatedStringID`),
  UNIQUE KEY `ContentSpecTranslatedStringID` (`ContentSpecTranslatedStringID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecTranslatedString_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecTranslatedString_AUD` (
  `ContentSpecTranslatedStringID` INT(11) NOT NULL,
  `REV`                           INT(11) NOT NULL,
  `REVTYPE`                       TINYINT(4) DEFAULT NULL,
  `REVEND`                        INT(11) DEFAULT NULL,
  `Locale`                        VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecTranslatedStringID`, `REV`),
  KEY `FKF97154ACD25E3A3B` (`REVEND`),
  KEY `FKF97154ACA110986` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpec_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpec_AUD` (
  `ContentSpecID`    INT(11) NOT NULL,
  `REV`              INT(11) NOT NULL,
  `REVTYPE`          TINYINT(4) DEFAULT NULL,
  `REVEND`           INT(11) DEFAULT NULL,
  `ContentSpecTitle` VARCHAR(255) DEFAULT NULL,
  `ContentSpecType`  INT(11) DEFAULT NULL,
  `Locale`           VARCHAR(255) DEFAULT NULL,
  `lastPublished`    DATETIME DEFAULT NULL,
  PRIMARY KEY (`ContentSpecID`, `REV`),
  KEY `FKD5E2978516D0EC8F` (`REVEND`),
  KEY `FKD5E297854E83BBDA` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `CSMetaDataToCSTranslatedString`
--

CREATE TABLE IF NOT EXISTS `CSMetaDataToCSTranslatedString` (
  `ContentSpecMetaDataID`         INT(11) NOT NULL AUTO_INCREMENT,
  `ContentSpecTranslatedStringID` INT(11) NOT NULL,
  `ContentSpecToCSMetaDataID`     INT(11) NOT NULL,
  PRIMARY KEY (`ContentSpecMetaDataID`),
  UNIQUE KEY `ContentSpecMetaDataID` (`ContentSpecMetaDataID`),
  UNIQUE KEY `ContentSpecMetaDataID_2` (`ContentSpecMetaDataID`, `ContentSpecTranslatedStringID`),
  KEY `FK70BF79518DF9AE0A` (`ContentSpecTranslatedStringID`),
  KEY `FK70BF7951CCD88DBA` (`ContentSpecToCSMetaDataID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `CSMetaDataToCSTranslatedString_AUD`
--

CREATE TABLE IF NOT EXISTS `CSMetaDataToCSTranslatedString_AUD` (
  `ContentSpecMetaDataID`         INT(11) NOT NULL,
  `REV`                           INT(11) NOT NULL,
  `REVTYPE`                       TINYINT(4) DEFAULT NULL,
  `REVEND`                        INT(11) DEFAULT NULL,
  `ContentSpecTranslatedStringID` INT(11) DEFAULT NULL,
  `ContentSpecToCSMetaDataID`     INT(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecMetaDataID`, `REV`),
  KEY `FKA6FC96A2D25E3A3B` (`REVEND`),
  KEY `FKA6FC96A2A110986` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `CSNodeToCSTranslatedString`
--

CREATE TABLE IF NOT EXISTS `CSNodeToCSTranslatedString` (
  `TopicToTagID`                  INT(11) NOT NULL AUTO_INCREMENT,
  `ContentSpecNodeID`             INT(11) NOT NULL,
  `ContentSpecTranslatedStringID` INT(11) NOT NULL,
  PRIMARY KEY (`TopicToTagID`),
  UNIQUE KEY `TopicToTagID` (`TopicToTagID`),
  UNIQUE KEY `ContentSpecNodeID` (`ContentSpecNodeID`, `ContentSpecTranslatedStringID`),
  KEY `FKEE5DA884ABA6BBE0` (`ContentSpecNodeID`),
  KEY `FKEE5DA8848DF9AE0A` (`ContentSpecTranslatedStringID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `CSNodeToCSTranslatedString_AUD`
--

CREATE TABLE IF NOT EXISTS `CSNodeToCSTranslatedString_AUD` (
  `TopicToTagID`                  INT(11) NOT NULL,
  `REV`                           INT(11) NOT NULL,
  `REVTYPE`                       TINYINT(4) DEFAULT NULL,
  `REVEND`                        INT(11) DEFAULT NULL,
  `ContentSpecNodeID`             INT(11) DEFAULT NULL,
  `ContentSpecTranslatedStringID` INT(11) DEFAULT NULL,
  PRIMARY KEY (`TopicToTagID`, `REV`),
  KEY `FKBEB9F455D25E3A3B` (`REVEND`),
  KEY `FKBEB9F455A110986` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Exception`
--

CREATE TABLE IF NOT EXISTS `Exception` (
  `ExceptionID` INT(11)    NOT NULL AUTO_INCREMENT,
  `Timestamp`   TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Details`     TEXT       NOT NULL,
  `Expected`    TINYINT(1) NOT NULL,
  `Description` VARCHAR(255) DEFAULT NULL,
  `User`        VARCHAR(45) DEFAULT NULL,
  PRIMARY KEY (`ExceptionID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Filter`
--

CREATE TABLE IF NOT EXISTS `Filter` (
  `FilterID`          INT(11)      NOT NULL AUTO_INCREMENT,
  `FilterName`        VARCHAR(255) NOT NULL,
  `FilterDescription` TEXT,
  PRIMARY KEY (`FilterID`),
  UNIQUE KEY `index2` (`FilterName`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'A filter is a saved query. A filter is made up of a row in t';

-- --------------------------------------------------------

--
-- Table structure for table `FilterCategory`
--

CREATE TABLE IF NOT EXISTS `FilterCategory` (
  `FilterCategoryID` INT(11) NOT NULL AUTO_INCREMENT,
  `FilterID`         INT(11) NOT NULL,
  `CategoryID`       INT(11) NOT NULL,
  `CategoryState`    INT(11) NOT NULL DEFAULT '0',
  `ProjectID`        INT(11) DEFAULT NULL,
  PRIMARY KEY (`FilterCategoryID`),
  KEY `fk_FilterCategory_1` (`CategoryID`),
  KEY `fk_FilterCategory_2` (`FilterID`),
  KEY `FKBB45C0B6EB3A6876` (`ProjectID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FilterCategory_AUD`
--

CREATE TABLE IF NOT EXISTS `FilterCategory_AUD` (
  `FilterCategoryID` INT(11) NOT NULL,
  `REV`              INT(11) NOT NULL,
  `REVEND`           INT(11) DEFAULT NULL,
  `REVTYPE`          TINYINT(4) DEFAULT NULL,
  `CategoryState`    INT(11) DEFAULT NULL,
  `CategoryID`       INT(11) DEFAULT NULL,
  `FilterID`         INT(11) DEFAULT NULL,
  `ProjectID`        INT(11) DEFAULT NULL,
  PRIMARY KEY (`FilterCategoryID`, `REV`),
  KEY `FK2C96A387DF74E053` (`REV`),
  KEY `FK2C96A387A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FilterField`
--

CREATE TABLE IF NOT EXISTS `FilterField` (
  `FilterFieldID` INT(11)      NOT NULL AUTO_INCREMENT,
  `FilterID`      INT(11)      NOT NULL,
  `Field`         VARCHAR(255) NOT NULL,
  `Value`         TEXT         NOT NULL,
  `Description`   TEXT,
  PRIMARY KEY (`FilterFieldID`),
  UNIQUE KEY `index3` (`Field`, `FilterID`),
  KEY `fk_Filter_Field_1` (`FilterID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FilterField_AUD`
--

CREATE TABLE IF NOT EXISTS `FilterField_AUD` (
  `FilterFieldID` INT(11) NOT NULL,
  `REV`           INT(11) NOT NULL,
  `REVEND`        INT(11) DEFAULT NULL,
  `REVTYPE`       TINYINT(4) DEFAULT NULL,
  `Description`   TEXT,
  `Field`         VARCHAR(255) DEFAULT NULL,
  `Value`         TEXT,
  `FilterID`      INT(11) DEFAULT NULL,
  PRIMARY KEY (`FilterFieldID`, `REV`),
  KEY `FK9B8B3513DF74E053` (`REV`),
  KEY `FK9B8B3513A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FilterLocale`
--

CREATE TABLE IF NOT EXISTS `FilterLocale` (
  `FilterLocaleID` INT(11)      NOT NULL AUTO_INCREMENT,
  `LocaleName`     VARCHAR(255) NOT NULL,
  `LocaleState`    INT(11)      NOT NULL,
  `FilterID`       INT(11)      NOT NULL,
  PRIMARY KEY (`FilterLocaleID`),
  UNIQUE KEY `FilterLocaleID` (`FilterLocaleID`),
  UNIQUE KEY `LocaleName` (`LocaleName`, `FilterID`),
  KEY `FK7CB6A01259256D82` (`FilterID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FilterLocale_AUD`
--

CREATE TABLE IF NOT EXISTS `FilterLocale_AUD` (
  `FilterLocaleID` INT(11) NOT NULL,
  `REV`            INT(11) NOT NULL,
  `REVTYPE`        TINYINT(4) DEFAULT NULL,
  `REVEND`         INT(11) DEFAULT NULL,
  `LocaleName`     VARCHAR(255) DEFAULT NULL,
  `LocaleState`    INT(11) DEFAULT NULL,
  `FilterID`       INT(11) DEFAULT NULL,
  PRIMARY KEY (`FilterLocaleID`, `REV`),
  KEY `FK19074E3A7C21108` (`REVEND`),
  KEY `FK19074E3DF74E053` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FilterOption`
--

CREATE TABLE IF NOT EXISTS `FilterOption` (
  `FilterOptionID`    INT(11)      NOT NULL AUTO_INCREMENT,
  `FilterOptionName`  VARCHAR(255) NOT NULL,
  `FilterOptionValue` VARCHAR(255) NOT NULL,
  `FilterID`          INT(11)      NOT NULL,
  PRIMARY KEY (`FilterOptionID`),
  UNIQUE KEY `FilterOptionID` (`FilterOptionID`),
  UNIQUE KEY `FilterOptionName` (`FilterOptionName`, `FilterID`),
  KEY `FK81EB1A2D59256D82` (`FilterID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FilterOption_AUD`
--

CREATE TABLE IF NOT EXISTS `FilterOption_AUD` (
  `FilterOptionID`    INT(11) NOT NULL,
  `REV`               INT(11) NOT NULL,
  `REVEND`            INT(11) DEFAULT NULL,
  `REVTYPE`           TINYINT(4) DEFAULT NULL,
  `FilterOptionName`  VARCHAR(255) DEFAULT NULL,
  `FilterOptionValue` VARCHAR(255) DEFAULT NULL,
  `FilterID`          INT(11) DEFAULT NULL,
  PRIMARY KEY (`FilterOptionID`, `REV`),
  KEY `FK574697EDF74E053` (`REV`),
  KEY `FK574697EA7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FilterTag`
--

CREATE TABLE IF NOT EXISTS `FilterTag` (
  `FilterTagID` INT(11) NOT NULL AUTO_INCREMENT,
  `FilterID`    INT(11) NOT NULL,
  `TagID`       INT(11) NOT NULL,
  `TagState`    INT(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`FilterTagID`),
  KEY `fk_FilterTag_1` (`TagID`),
  KEY `fk_FilterTag_2` (`FilterID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FilterTag_AUD`
--

CREATE TABLE IF NOT EXISTS `FilterTag_AUD` (
  `FilterTagID` INT(11) NOT NULL,
  `REV`         INT(11) NOT NULL,
  `REVEND`      INT(11) DEFAULT NULL,
  `REVTYPE`     TINYINT(4) DEFAULT NULL,
  `TagState`    INT(11) DEFAULT NULL,
  `FilterID`    INT(11) DEFAULT NULL,
  `TagID`       INT(11) DEFAULT NULL,
  PRIMARY KEY (`FilterTagID`, `REV`),
  KEY `FKA9A235B3DF74E053` (`REV`),
  KEY `FKA9A235B3A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Filter_AUD`
--

CREATE TABLE IF NOT EXISTS `Filter_AUD` (
  `FilterID`          INT(11) NOT NULL,
  `REV`               INT(11) NOT NULL,
  `REVEND`            INT(11) DEFAULT NULL,
  `REVTYPE`           TINYINT(4) DEFAULT NULL,
  `FilterDescription` TEXT,
  `FilterName`        VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`FilterID`, `REV`),
  KEY `FK1A445969DF74E053` (`REV`),
  KEY `FK1A445969A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Help`
--

CREATE TABLE IF NOT EXISTS `Help` (
  `HelpID`     INT(11) NOT NULL AUTO_INCREMENT,
  `TableColID` VARCHAR(255) DEFAULT NULL,
  `HelpText`   TEXT,
  PRIMARY KEY (`HelpID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Help_AUD`
--

CREATE TABLE IF NOT EXISTS `Help_AUD` (
  `HelpID`     INT(11) NOT NULL,
  `REV`        INT(11) NOT NULL,
  `REVEND`     INT(11) DEFAULT NULL,
  `REVTYPE`    TINYINT(4) DEFAULT NULL,
  `HelpText`   TEXT,
  `TableColID` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`HelpID`, `REV`),
  KEY `FKD4CBD8B2DF74E053` (`REV`),
  KEY `FKD4CBD8B2A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ImageFile`
--

CREATE TABLE IF NOT EXISTS `ImageFile` (
  `ImageFileID` INT(11) NOT NULL AUTO_INCREMENT,
  `Description` TEXT,
  PRIMARY KEY (`ImageFileID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ImageFile_AUD`
--

CREATE TABLE IF NOT EXISTS `ImageFile_AUD` (
  `ImageFileID` INT(11) NOT NULL,
  `REV`         INT(11) NOT NULL,
  `REVEND`      INT(11) DEFAULT NULL,
  `REVTYPE`     TINYINT(4) DEFAULT NULL,
  `Description` TEXT,
  PRIMARY KEY (`ImageFileID`, `REV`),
  KEY `FK553BB7A8DF74E053` (`REV`),
  KEY `FK553BB7A8A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `IntegerConstants`
--

CREATE TABLE IF NOT EXISTS `IntegerConstants` (
  `IntegerConstantsID` INT(11)     NOT NULL AUTO_INCREMENT,
  `ConstantName`       VARCHAR(45) NOT NULL,
  `ConstantValue`      INT(11) DEFAULT NULL,
  PRIMARY KEY (`IntegerConstantsID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `IntegerConstants_AUD`
--

CREATE TABLE IF NOT EXISTS `IntegerConstants_AUD` (
  `IntegerConstantsID` INT(11) NOT NULL,
  `REV`                INT(11) NOT NULL,
  `REVEND`             INT(11) DEFAULT NULL,
  `REVTYPE`            TINYINT(4) DEFAULT NULL,
  `ConstantName`       VARCHAR(45) DEFAULT NULL,
  `ConstantValue`      INT(11) DEFAULT NULL,
  PRIMARY KEY (`IntegerConstantsID`, `REV`),
  KEY `FKA7C8D722DF74E053` (`REV`),
  KEY `FKA7C8D722A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `LanguageImage`
--

CREATE TABLE IF NOT EXISTS `LanguageImage` (
  `LanguageImageID`  INT(11)      NOT NULL AUTO_INCREMENT,
  `ImageFileID`      INT(11)      NOT NULL,
  `ThumbnailData`    MEDIUMBLOB,
  `ImageDataBase64`  MEDIUMBLOB,
  `Locale`           VARCHAR(255) NOT NULL,
  `OriginalFileName` VARCHAR(255) DEFAULT NULL,
  `ImageData`        MEDIUMBLOB,
  PRIMARY KEY (`LanguageImageID`),
  KEY `FK15D2ACC3E0AD6B52` (`ImageFileID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `LanguageImage_AUD`
--

CREATE TABLE IF NOT EXISTS `LanguageImage_AUD` (
  `LanguageImageID`  INT(11) NOT NULL,
  `REV`              INT(11) NOT NULL,
  `REVTYPE`          TINYINT(4) DEFAULT NULL,
  `REVEND`           INT(11) DEFAULT NULL,
  `ImageData`        MEDIUMBLOB,
  `ImageDataBase64`  MEDIUMBLOB,
  `Locale`           VARCHAR(255) DEFAULT NULL,
  `OriginalFileName` VARCHAR(255) DEFAULT NULL,
  `ThumbnailData`    MEDIUMBLOB,
  `ImageFileID`      INT(11) DEFAULT NULL,
  PRIMARY KEY (`LanguageImageID`, `REV`),
  KEY `FK5F84C114A7C21108` (`REVEND`),
  KEY `FK5F84C114DF74E053` (`REV`),
  KEY `FK5F84C11416D0EC8F` (`REVEND`),
  KEY `FK5F84C1144E83BBDA` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Project`
--

CREATE TABLE IF NOT EXISTS `Project` (
  `ProjectID`          INT(11)      NOT NULL AUTO_INCREMENT,
  `ProjectName`        VARCHAR(255) NOT NULL,
  `ProjectDescription` TEXT,
  PRIMARY KEY (`ProjectID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Project_AUD`
--

CREATE TABLE IF NOT EXISTS `Project_AUD` (
  `ProjectID`          INT(11) NOT NULL,
  `REV`                INT(11) NOT NULL,
  `REVEND`             INT(11) DEFAULT NULL,
  `REVTYPE`            TINYINT(4) DEFAULT NULL,
  `ProjectDescription` TEXT,
  `ProjectName`        VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`ProjectID`, `REV`),
  KEY `FK2B68EC4ADF74E053` (`REV`),
  KEY `FK2B68EC4AA7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `PropertyTag`
--

CREATE TABLE IF NOT EXISTS `PropertyTag` (
  `PropertyTagID`          INT(11)      NOT NULL AUTO_INCREMENT,
  `PropertyTagName`        VARCHAR(255) NOT NULL,
  `PropertyTagDescription` TEXT,
  `PropertyTagRegex`       TEXT         NOT NULL,
  `PropertyTagCanBeNull`   BIT(1)       NOT NULL,
  `PropertyTagIsUnique`    BIT(1)       NOT NULL DEFAULT b'0',
  PRIMARY KEY (`PropertyTagID`),
  UNIQUE KEY `PropertyTagUnique` (`PropertyTagName`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `PropertyTagCategory`
--

CREATE TABLE IF NOT EXISTS `PropertyTagCategory` (
  `PropertyTagCategoryID`          INT(11)      NOT NULL AUTO_INCREMENT,
  `PropertyTagCategoryName`        VARCHAR(255) NOT NULL,
  `PropertyTagCategoryDescription` TEXT,
  PRIMARY KEY (`PropertyTagCategoryID`),
  UNIQUE KEY `PropertyTagCategoryNameUnique` (`PropertyTagCategoryName`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `PropertyTagCategory_AUD`
--

CREATE TABLE IF NOT EXISTS `PropertyTagCategory_AUD` (
  `PropertyTagCategoryID`          INT(11) NOT NULL,
  `REV`                            INT(11) NOT NULL,
  `REVEND`                         INT(11) DEFAULT NULL,
  `REVTYPE`                        TINYINT(4) DEFAULT NULL,
  `PropertyTagCategoryDescription` TEXT,
  `PropertyTagCategoryName`        VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`PropertyTagCategoryID`, `REV`),
  KEY `FKE73E65D4DF74E053` (`REV`),
  KEY `FKE73E65D4A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `PropertyTagToPropertyTagCategory`
--

CREATE TABLE IF NOT EXISTS `PropertyTagToPropertyTagCategory` (
  `PropertyTagToPropertyTagCategoryID` INT(11) NOT NULL AUTO_INCREMENT,
  `PropertyTagID`                      INT(11) NOT NULL,
  `PropertyTagCategoryID`              INT(11) NOT NULL,
  `Sorting`                            INT(11) DEFAULT NULL,
  PRIMARY KEY (`PropertyTagToPropertyTagCategoryID`),
  UNIQUE KEY `PropertyTagToPropertTagCategoryUnique` (`PropertyTagID`, `PropertyTagCategoryID`),
  KEY `PropertyTagToPropertTagCategoryFk2` (`PropertyTagCategoryID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'PropertyTagToPropertyTagCategoryID\nPropertyTagToPropertyTagC';

-- --------------------------------------------------------

--
-- Table structure for table `PropertyTagToPropertyTagCategory_AUD`
--

CREATE TABLE IF NOT EXISTS `PropertyTagToPropertyTagCategory_AUD` (
  `PropertyTagToPropertyTagCategoryID` INT(11) NOT NULL,
  `REV`                                INT(11) NOT NULL,
  `REVEND`                             INT(11) DEFAULT NULL,
  `REVTYPE`                            TINYINT(4) DEFAULT NULL,
  `Sorting`                            INT(11) DEFAULT NULL,
  `PropertyTagID`                      INT(11) DEFAULT NULL,
  `PropertyTagCategoryID`              INT(11) DEFAULT NULL,
  PRIMARY KEY (`PropertyTagToPropertyTagCategoryID`, `REV`),
  KEY `FK95A54314DF74E053` (`REV`),
  KEY `FK95A54314A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `PropertyTag_AUD`
--

CREATE TABLE IF NOT EXISTS `PropertyTag_AUD` (
  `PropertyTagID`          INT(11) NOT NULL,
  `REV`                    INT(11) NOT NULL,
  `REVEND`                 INT(11) DEFAULT NULL,
  `REVTYPE`                TINYINT(4) DEFAULT NULL,
  `PropertyTagCanBeNull`   BIT(1) DEFAULT NULL,
  `PropertyTagDescription` TEXT,
  `PropertyTagName`        VARCHAR(255) DEFAULT NULL,
  `PropertyTagRegex`       TEXT,
  `PropertyTagIsUnique`    BIT(1) DEFAULT NULL,
  PRIMARY KEY (`PropertyTagID`, `REV`),
  KEY `FK4825B8B6DF74E053` (`REV`),
  KEY `FK4825B8B6A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `RelationshipTag`
--

CREATE TABLE IF NOT EXISTS `RelationshipTag` (
  `RelationshipTagID`          INT(11)      NOT NULL AUTO_INCREMENT,
  `RelationshipTagName`        VARCHAR(255) NOT NULL,
  `RelationshipTagDescription` TEXT,
  PRIMARY KEY (`RelationshipTagID`),
  UNIQUE KEY `RelationshipTagNameUnique` (`RelationshipTagName`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `RelationshipTag_AUD`
--

CREATE TABLE IF NOT EXISTS `RelationshipTag_AUD` (
  `RelationshipTagId`          INT(11) NOT NULL,
  `REV`                        INT(11) NOT NULL,
  `REVEND`                     INT(11) DEFAULT NULL,
  `REVTYPE`                    TINYINT(4) DEFAULT NULL,
  `RelationshipTagDescription` TEXT,
  `RelationshipTagName`        VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`RelationshipTagId`, `REV`),
  KEY `FK6CA98AF3DF74E053` (`REV`),
  KEY `FK6CA98AF3A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `REVINFO`
--

CREATE TABLE IF NOT EXISTS `REVINFO` (
  `REV`      INT(11) NOT NULL AUTO_INCREMENT,
  `REVTSTMP` BIGINT(20) DEFAULT NULL,
  `Flag`     TINYINT(4) DEFAULT NULL,
  `Message`  TEXT,
  `UserName` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Role`
--

CREATE TABLE IF NOT EXISTS `Role` (
  `RoleID`      INT(11)      NOT NULL AUTO_INCREMENT,
  `RoleName`    VARCHAR(255) NOT NULL,
  `Description` TEXT,
  PRIMARY KEY (`RoleID`),
  UNIQUE KEY `index2` (`RoleName`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `RoleToRole`
--

CREATE TABLE IF NOT EXISTS `RoleToRole` (
  `RoleToRoleID`     INT(11) NOT NULL AUTO_INCREMENT,
  `PrimaryRole`      INT(11) NOT NULL,
  `RelationshipType` INT(11) NOT NULL,
  `SecondaryRole`    INT(11) NOT NULL,
  PRIMARY KEY (`RoleToRoleID`),
  UNIQUE KEY `RoleToRoleID` (`RoleToRoleID`),
  UNIQUE KEY `PrimaryRole` (`PrimaryRole`, `SecondaryRole`, `RelationshipType`),
  KEY `FKD0A0E5C78433D197` (`SecondaryRole`),
  KEY `FKD0A0E5C7844F7725` (`PrimaryRole`),
  KEY `fk_RoleToRole_1` (`RelationshipType`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `RoleToRoleRelationship`
--

CREATE TABLE IF NOT EXISTS `RoleToRoleRelationship` (
  `RoleToRoleRelationshipID` INT(11) NOT NULL AUTO_INCREMENT,
  `Description`              TEXT,
  PRIMARY KEY (`RoleToRoleRelationshipID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `RoleToRoleRelationship_AUD`
--

CREATE TABLE IF NOT EXISTS `RoleToRoleRelationship_AUD` (
  `RoleToRoleRelationshipID` INT(11) NOT NULL,
  `REV`                      INT(11) NOT NULL,
  `REVEND`                   INT(11) DEFAULT NULL,
  `REVTYPE`                  TINYINT(4) DEFAULT NULL,
  `Description`              TEXT,
  PRIMARY KEY (`RoleToRoleRelationshipID`, `REV`),
  KEY `FKA68183F0DF74E053` (`REV`),
  KEY `FKA68183F0A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `RoleToRole_AUD`
--

CREATE TABLE IF NOT EXISTS `RoleToRole_AUD` (
  `RoleToRoleID`     INT(11) NOT NULL,
  `REV`              INT(11) NOT NULL,
  `REVEND`           INT(11) DEFAULT NULL,
  `REVTYPE`          TINYINT(4) DEFAULT NULL,
  `PrimaryRole`      INT(11) DEFAULT NULL,
  `RelationshipType` INT(11) DEFAULT NULL,
  `SecondaryRole`    INT(11) DEFAULT NULL,
  PRIMARY KEY (`RoleToRoleID`, `REV`),
  KEY `FK26C6D818DF74E053` (`REV`),
  KEY `FK26C6D818A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Role_AUD`
--

CREATE TABLE IF NOT EXISTS `Role_AUD` (
  `RoleID`      INT(11) NOT NULL,
  `REV`         INT(11) NOT NULL,
  `REVEND`      INT(11) DEFAULT NULL,
  `REVTYPE`     TINYINT(4) DEFAULT NULL,
  `Description` TEXT,
  `RoleName`    VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`RoleID`, `REV`),
  KEY `FKF3FAE767DF74E053` (`REV`),
  KEY `FKF3FAE767A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `StringConstants`
--

CREATE TABLE IF NOT EXISTS `StringConstants` (
  `StringConstantsID` INT(11)     NOT NULL AUTO_INCREMENT,
  `ConstantName`      VARCHAR(45) NOT NULL,
  `ConstantValue`     MEDIUMTEXT,
  PRIMARY KEY (`StringConstantsID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT = 'This table holds the string constants used by Skynet, includ';

-- --------------------------------------------------------

--
-- Table structure for table `StringConstants_AUD`
--

CREATE TABLE IF NOT EXISTS `StringConstants_AUD` (
  `StringConstantsID` INT(11) NOT NULL,
  `REV`               INT(11) NOT NULL,
  `REVEND`            INT(11) DEFAULT NULL,
  `REVTYPE`           TINYINT(4) DEFAULT NULL,
  `ConstantName`      VARCHAR(45) DEFAULT NULL,
  `ConstantValue`     MEDIUMTEXT,
  PRIMARY KEY (`StringConstantsID`, `REV`),
  KEY `FK399D7AEFDF74E053` (`REV`),
  KEY `FK399D7AEFA7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Tag`
--

CREATE TABLE IF NOT EXISTS `Tag` (
  `TagID`          INT(11)      NOT NULL AUTO_INCREMENT,
  `TagName`        VARCHAR(255) NOT NULL,
  `TagDescription` TEXT,
  PRIMARY KEY (`TagID`),
  UNIQUE KEY `index4` (`TagName`),
  KEY `fk_Tag_1` (`TagID`),
  KEY `fk_Tag_2` (`TagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagExclusion`
--

CREATE TABLE IF NOT EXISTS `TagExclusion` (
  `Tag1ID` INT(11) NOT NULL,
  `Tag2ID` INT(11) NOT NULL,
  PRIMARY KEY (`Tag1ID`, `Tag2ID`),
  KEY `fk_TagExclusion_1` (`Tag1ID`),
  KEY `fk_TagExclusion_2` (`Tag2ID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagExclusion_AUD`
--

CREATE TABLE IF NOT EXISTS `TagExclusion_AUD` (
  `REV`     INT(11) NOT NULL,
  `REVEND`  INT(11) DEFAULT NULL,
  `Tag1ID`  INT(11) NOT NULL,
  `Tag2ID`  INT(11) NOT NULL,
  `REVTYPE` TINYINT(4) DEFAULT NULL,
  PRIMARY KEY (`REV`, `Tag1ID`, `Tag2ID`),
  KEY `FK5BBFB345DF74E053` (`REV`),
  KEY `FK5BBFB345A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToCategory`
--

CREATE TABLE IF NOT EXISTS `TagToCategory` (
  `TagToCategoryID` INT(11) NOT NULL AUTO_INCREMENT,
  `TagID`           INT(11) NOT NULL
  COMMENT 'References the TagID that is to be placed into the Category.',
  `CategoryID`      INT(11) NOT NULL
  COMMENT 'References the CategoryID that the Tag is to be made a child of.',
  `Sorting`         INT(11) DEFAULT NULL
  COMMENT 'Defines the sorting order of the Tag in the Category.',
  PRIMARY KEY (`TagToCategoryID`),
  UNIQUE KEY `Unique` (`TagID`, `CategoryID`),
  KEY `fk_TagToCategory_1` (`TagID`),
  KEY `fk_TagToCategory_2` (`CategoryID`),
  KEY `FK8DF417B37D4F054E` (`CategoryID`),
  KEY `FK8DF417B36F9851B8` (`TagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToCategory_AUD`
--

CREATE TABLE IF NOT EXISTS `TagToCategory_AUD` (
  `TagToCategoryID` INT(11) NOT NULL,
  `REV`             INT(11) NOT NULL,
  `REVEND`          INT(11) DEFAULT NULL,
  `REVTYPE`         TINYINT(4) DEFAULT NULL,
  `Sorting`         INT(11) DEFAULT NULL,
  `CategoryID`      INT(11) DEFAULT NULL,
  `TagID`           INT(11) DEFAULT NULL,
  PRIMARY KEY (`TagToCategoryID`, `REV`),
  KEY `FKC2173404DF74E053` (`REV`),
  KEY `FKC2173404A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToProject`
--

CREATE TABLE IF NOT EXISTS `TagToProject` (
  `TagToProjectID` INT(11) NOT NULL AUTO_INCREMENT,
  `ProjectID`      INT(11) NOT NULL,
  `TagID`          INT(11) NOT NULL,
  PRIMARY KEY (`TagToProjectID`),
  UNIQUE KEY `index4` (`ProjectID`, `TagID`),
  KEY `fk_TagToProject_1` (`ProjectID`),
  KEY `fk_TagToProject_2` (`TagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToProject_AUD`
--

CREATE TABLE IF NOT EXISTS `TagToProject_AUD` (
  `TagToProjectID` INT(11) NOT NULL,
  `REV`            INT(11) NOT NULL,
  `REVEND`         INT(11) DEFAULT NULL,
  `REVTYPE`        TINYINT(4) DEFAULT NULL,
  `ProjectID`      INT(11) DEFAULT NULL,
  `TagID`          INT(11) DEFAULT NULL,
  PRIMARY KEY (`TagToProjectID`, `REV`),
  KEY `FKB4A9F195DF74E053` (`REV`),
  KEY `FKB4A9F195A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToPropertyTag`
--

CREATE TABLE IF NOT EXISTS `TagToPropertyTag` (
  `TagToPropertyTagID` INT(11) NOT NULL AUTO_INCREMENT,
  `TagID`              INT(11) NOT NULL,
  `PropertyTagID`      INT(11) NOT NULL,
  `Value`              TEXT,
  PRIMARY KEY (`TagToPropertyTagID`),
  KEY `TagIDFK` (`TagID`),
  KEY `PropertyTagIDFK` (`PropertyTagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToPropertyTag_AUD`
--

CREATE TABLE IF NOT EXISTS `TagToPropertyTag_AUD` (
  `TagToPropertyTagID` INT(11) NOT NULL,
  `REV`                INT(11) NOT NULL,
  `REVEND`             INT(11) DEFAULT NULL,
  `REVTYPE`            TINYINT(4) DEFAULT NULL,
  `Value`              TEXT,
  `PropertyTagID`      INT(11) DEFAULT NULL,
  `TagID`              INT(11) DEFAULT NULL,
  PRIMARY KEY (`TagToPropertyTagID`, `REV`),
  KEY `FK937D2081DF74E053` (`REV`),
  KEY `FK937D2081A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToTag`
--

CREATE TABLE IF NOT EXISTS `TagToTag` (
  `TagToTagID`       INT(11) NOT NULL AUTO_INCREMENT,
  `PrimaryTagID`     INT(11) NOT NULL,
  `SecondaryTagID`   INT(11) NOT NULL,
  `RelationshipType` INT(11) NOT NULL,
  PRIMARY KEY (`TagToTagID`),
  UNIQUE KEY `UNIQUE` (`PrimaryTagID`, `SecondaryTagID`, `RelationshipType`),
  KEY `fk_TagToTag_1` (`RelationshipType`),
  KEY `fk_TagToTag_2` (`PrimaryTagID`),
  KEY `fk_TagToTag_3` (`SecondaryTagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToTagRelationship`
--

CREATE TABLE IF NOT EXISTS `TagToTagRelationship` (
  `TagToTagRelationshipType`        INT(11) NOT NULL,
  `TagToTagRelationshipDescription` TEXT,
  PRIMARY KEY (`TagToTagRelationshipType`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToTagRelationship_AUD`
--

CREATE TABLE IF NOT EXISTS `TagToTagRelationship_AUD` (
  `TagToTagRelationshipType`        INT(11) NOT NULL,
  `REV`                             INT(11) NOT NULL,
  `REVEND`                          INT(11) DEFAULT NULL,
  `REVTYPE`                         TINYINT(4) DEFAULT NULL,
  `TagToTagRelationshipDescription` TEXT,
  PRIMARY KEY (`TagToTagRelationshipType`, `REV`),
  KEY `FK583EA9EEDF74E053` (`REV`),
  KEY `FK583EA9EEA7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToTag_AUD`
--

CREATE TABLE IF NOT EXISTS `TagToTag_AUD` (
  `TagToTagID`       INT(11) NOT NULL,
  `REV`              INT(11) NOT NULL,
  `REVEND`           INT(11) DEFAULT NULL,
  `REVTYPE`          TINYINT(4) DEFAULT NULL,
  `RelationshipType` INT(11) DEFAULT NULL,
  `PrimaryTagID`     INT(11) DEFAULT NULL,
  `SecondaryTagID`   INT(11) DEFAULT NULL,
  PRIMARY KEY (`TagToTagID`, `REV`),
  KEY `FKF27E8B16DF74E053` (`REV`),
  KEY `FKF27E8B16A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Tag_AUD`
--

CREATE TABLE IF NOT EXISTS `Tag_AUD` (
  `TagID`          INT(11) NOT NULL,
  `REV`            INT(11) NOT NULL,
  `REVEND`         INT(11) DEFAULT NULL,
  `REVTYPE`        TINYINT(4) DEFAULT NULL,
  `TagDescription` TEXT,
  `TagName`        VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`TagID`, `REV`),
  KEY `FK6E9284BDF74E053` (`REV`),
  KEY `FK6E9284BA7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Topic`
--

CREATE TABLE IF NOT EXISTS `Topic` (
  `TopicID`        INT(11)      NOT NULL AUTO_INCREMENT,
  `TopicTitle`     VARCHAR(255) DEFAULT NULL,
  `TopicText`      TEXT,
  `TopicTimeStamp` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `TopicSVNURL`    VARCHAR(255) DEFAULT NULL,
  `TopicXML`       MEDIUMTEXT,
  `TopicLocale`    VARCHAR(255) NOT NULL,
  `TopicRendered`  TEXT,
  PRIMARY KEY (`TopicID`),
  KEY `fk_Topic_1` (`TopicID`),
  KEY `FK4D3DD0FE2436E96` (`TopicID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicSecondOrderData`
--

CREATE TABLE IF NOT EXISTS `TopicSecondOrderData` (
  `TopicSecondOrderDataID` INT(11) NOT NULL AUTO_INCREMENT,
  `TopicXMLErrors`         TEXT,
  `TopicHTMLView`          MEDIUMTEXT,
  `TopicID`                INT(11) NOT NULL,
  PRIMARY KEY (`TopicSecondOrderDataID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicSecondOrderData_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicSecondOrderData_AUD` (
  `TopicID`                INT(11) NOT NULL,
  `REV`                    INT(11) NOT NULL,
  `REVEND`                 INT(11) DEFAULT NULL,
  `REVTYPE`                TINYINT(4) DEFAULT NULL,
  `TopicHTMLView`          MEDIUMTEXT,
  `TopicXMLErrors`         TEXT,
  `TopicSecondOrderDataID` INT(11) NOT NULL,
  PRIMARY KEY (`TopicSecondOrderDataID`, `REV`),
  KEY `FK8E541846DF74E053` (`REV`),
  KEY `FK8E541846A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicSourceURL`
--

CREATE TABLE IF NOT EXISTS `TopicSourceURL` (
  `TopicSourceURLID` INT(11)       NOT NULL AUTO_INCREMENT,
  `SourceURL`        VARCHAR(2048) NOT NULL,
  `Description`      TEXT,
  `Title`            VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`TopicSourceURLID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicSourceURL_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicSourceURL_AUD` (
  `TopicSourceURLID` INT(11) NOT NULL,
  `REV`              INT(11) NOT NULL,
  `REVEND`           INT(11) DEFAULT NULL,
  `REVTYPE`          TINYINT(4) DEFAULT NULL,
  `TopicXML`         TEXT,
  `SourceURL`        VARCHAR(2048) DEFAULT NULL,
  `Title`            VARCHAR(255) DEFAULT NULL,
  `Description`      TEXT,
  PRIMARY KEY (`TopicSourceURLID`, `REV`),
  KEY `FK4FDDCE56DF74E053` (`REV`),
  KEY `FK4FDDCE56A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToBugzillaBug`
--

CREATE TABLE IF NOT EXISTS `TopicToBugzillaBug` (
  `TopicToBugzillaBugID` INT(11) NOT NULL AUTO_INCREMENT,
  `TopicID`              INT(11) NOT NULL,
  `BugzillaBugID`        INT(11) NOT NULL,
  PRIMARY KEY (`TopicToBugzillaBugID`),
  UNIQUE KEY `TopicIDBugzillaBugIDUnique` (`TopicID`, `BugzillaBugID`),
  UNIQUE KEY `TopicToBugzillaBugBugzillaBugIDUnique` (`BugzillaBugID`),
  KEY `TopicToBugzillaBugFK2` (`BugzillaBugID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToBugzillaBug_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicToBugzillaBug_AUD` (
  `TopicToBugzillaBugID` INT(11) NOT NULL,
  `REV`                  INT(11) NOT NULL,
  `REVEND`               INT(11) DEFAULT NULL,
  `REVTYPE`              TINYINT(4) DEFAULT NULL,
  `BugzillaBugID`        INT(11) DEFAULT NULL,
  `TopicID`              INT(11) DEFAULT NULL,
  PRIMARY KEY (`TopicToBugzillaBugID`, `REV`),
  KEY `FK176CCFBDDF74E053` (`REV`),
  KEY `FK176CCFBDA7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToPropertyTag`
--

CREATE TABLE IF NOT EXISTS `TopicToPropertyTag` (
  `TopicToPropertyTagID` INT(11) NOT NULL AUTO_INCREMENT,
  `TopicID`              INT(11) NOT NULL,
  `PropertyTagID`        INT(11) NOT NULL,
  `Value`                TEXT,
  PRIMARY KEY (`TopicToPropertyTagID`),
  KEY `TopicToPropertyTagFK1` (`TopicID`),
  KEY `TopicToPropertyTagFK2` (`PropertyTagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToPropertyTag_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicToPropertyTag_AUD` (
  `TopicToPropertyTagID` INT(11) NOT NULL,
  `REV`                  INT(11) NOT NULL,
  `REVEND`               INT(11) DEFAULT NULL,
  `REVTYPE`              TINYINT(4) DEFAULT NULL,
  `Value`                TEXT,
  `PropertyTagID`        INT(11) DEFAULT NULL,
  `TopicID`              INT(11) DEFAULT NULL,
  PRIMARY KEY (`TopicToPropertyTagID`, `REV`),
  KEY `FKDE6FC78CDF74E053` (`REV`),
  KEY `FKDE6FC78CA7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToTag`
--

CREATE TABLE IF NOT EXISTS `TopicToTag` (
  `TopicToTagID` INT(11) NOT NULL AUTO_INCREMENT,
  `TopicID`      INT(11) NOT NULL,
  `TagID`        INT(11) NOT NULL,
  PRIMARY KEY (`TopicToTagID`),
  UNIQUE KEY `UNIQUE` (`TopicID`, `TagID`),
  KEY `fk_TopicToTag_1` (`TopicID`),
  KEY `fk_TopicToTag_2` (`TagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToTag_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicToTag_AUD` (
  `TopicToTagID` INT(11) NOT NULL,
  `REV`          INT(11) NOT NULL,
  `REVEND`       INT(11) DEFAULT NULL,
  `REVTYPE`      TINYINT(4) DEFAULT NULL,
  `TagID`        INT(11) DEFAULT NULL,
  `TopicID`      INT(11) DEFAULT NULL,
  PRIMARY KEY (`TopicToTagID`, `REV`),
  KEY `FK27826D21DF74E053` (`REV`),
  KEY `FK27826D21A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToTopic`
--

CREATE TABLE IF NOT EXISTS `TopicToTopic` (
  `TopicToTopicID`    INT(11) NOT NULL AUTO_INCREMENT,
  `MainTopicID`       INT(11) NOT NULL,
  `RelatedTopicID`    INT(11) NOT NULL,
  `RelationshipTagID` INT(11) NOT NULL,
  PRIMARY KEY (`TopicToTopicID`),
  UNIQUE KEY `TopicToTopicUnique` (`MainTopicID`, `RelatedTopicID`, `RelationshipTagID`),
  KEY `fk_TopicToTopic_1` (`MainTopicID`),
  KEY `fk_TopicToTopic_2` (`RelatedTopicID`),
  KEY `fk_TopicToTopic_3` (`RelationshipTagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToTopicSecondOrderData`
--

CREATE TABLE IF NOT EXISTS `TopicToTopicSecondOrderData` (
  `TopicToTopicSecondOrderDataID` INT(11) NOT NULL AUTO_INCREMENT,
  `TopicID`                       INT(11) NOT NULL,
  `TopicSecondOrderDataID`        INT(11) NOT NULL,
  PRIMARY KEY (`TopicToTopicSecondOrderDataID`),
  UNIQUE KEY `TopicToTopicSecondOrderDataTopicIdUnique` (`TopicID`),
  UNIQUE KEY `TopicToTopicSecondOrderDataTopicSecondOrderDataIdUnique` (`TopicSecondOrderDataID`),
  KEY `FK303D33DF3340562` (`TopicID`),
  KEY `fk_TopicToTopicSecondOrderData_1` (`TopicSecondOrderDataID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToTopicSourceURL`
--

CREATE TABLE IF NOT EXISTS `TopicToTopicSourceURL` (
  `TopicToTopicSourceURLID` INT(11) NOT NULL AUTO_INCREMENT,
  `TopicID`                 INT(11) NOT NULL,
  `TopicSourceURLID`        INT(11) NOT NULL,
  PRIMARY KEY (`TopicToTopicSourceURLID`),
  UNIQUE KEY `UniqueIndex` (`TopicID`, `TopicSourceURLID`),
  UNIQUE KEY `UniqueTopicSourceURLID` (`TopicSourceURLID`),
  KEY `ForeignTopicSourceURLID` (`TopicSourceURLID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToTopicSourceURL_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicToTopicSourceURL_AUD` (
  `TopicToTopicSourceURLID` INT(11) NOT NULL,
  `REV`                     INT(11) NOT NULL,
  `REVEND`                  INT(11) DEFAULT NULL,
  `REVTYPE`                 TINYINT(4) DEFAULT NULL,
  `TopicID`                 INT(11) DEFAULT NULL,
  `TopicSourceURLID`        INT(11) DEFAULT NULL,
  PRIMARY KEY (`TopicToTopicSourceURLID`, `REV`),
  KEY `FKA41247C0DF74E053` (`REV`),
  KEY `FKA41247C0A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToTopic_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicToTopic_AUD` (
  `TopicToTopicID`    INT(11) NOT NULL,
  `REV`               INT(11) NOT NULL,
  `REVEND`            INT(11) DEFAULT NULL,
  `REVTYPE`           TINYINT(4) DEFAULT NULL,
  `MainTopicID`       INT(11) DEFAULT NULL,
  `RelatedTopicID`    INT(11) DEFAULT NULL,
  `RelationshipTagID` INT(11) DEFAULT NULL,
  PRIMARY KEY (`TopicToTopicID`, `REV`),
  KEY `FKEDF852B6DF74E053` (`REV`),
  KEY `FKEDF852B6A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Topic_AUD`
--

CREATE TABLE IF NOT EXISTS `Topic_AUD` (
  `TopicID`        INT(11) NOT NULL,
  `REV`            INT(11) NOT NULL,
  `REVEND`         INT(11) DEFAULT NULL,
  `REVTYPE`        TINYINT(4) DEFAULT NULL,
  `TopicAddedBy`   VARCHAR(255) DEFAULT NULL,
  `TopicSVNURL`    VARCHAR(255) DEFAULT NULL,
  `TopicText`      TEXT,
  `TopicTimeStamp` DATETIME DEFAULT NULL,
  `TopicTitle`     VARCHAR(255) DEFAULT NULL,
  `TopicXML`       MEDIUMTEXT,
  `TopicRendered`  TEXT,
  `TopicLocale`    VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`TopicID`, `REV`),
  KEY `FK8E9CEB60DF74E053` (`REV`),
  KEY `FK8E9CEB60A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TranslatedTopic`
--

CREATE TABLE IF NOT EXISTS `TranslatedTopic` (
  `TranslatedTopicID` INT(11) NOT NULL AUTO_INCREMENT,
  `TopicID`           INT(11) NOT NULL,
  `TopicRevision`     INT(11) NOT NULL,
  PRIMARY KEY (`TranslatedTopicID`),
  UNIQUE KEY `TranslatedTopicID` (`TranslatedTopicID`),
  UNIQUE KEY `TopicRevision` (`TopicRevision`, `TopicID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TranslatedTopicData`
--

CREATE TABLE IF NOT EXISTS `TranslatedTopicData` (
  `TranslatedTopicDataID`        INT(11)     NOT NULL AUTO_INCREMENT,
  `TranslatedXML`                MEDIUMTEXT,
  `TranslatedXMLErrors`          TEXT,
  `TranslatedXMLRendered`        MEDIUMTEXT,
  `TranslatedXMLRenderedUpdated` DATETIME DEFAULT NULL,
  `TranslationLocale`            VARCHAR(45) NOT NULL,
  `TranslationPercentage`        INT(11)     NOT NULL,
  `TranslatedTopicID`            INT(11)     NOT NULL,
  PRIMARY KEY (`TranslatedTopicDataID`),
  UNIQUE KEY `TranslatedTopicDataID` (`TranslatedTopicDataID`),
  UNIQUE KEY `TranslatedTopicID` (`TranslatedTopicID`, `TranslationLocale`),
  KEY `FKBEAB41239248FD56` (`TranslatedTopicID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TranslatedTopicData_AUD`
--

CREATE TABLE IF NOT EXISTS `TranslatedTopicData_AUD` (
  `TranslatedTopicDataID`        INT(11) NOT NULL,
  `REV`                          INT(11) NOT NULL,
  `REVTYPE`                      TINYINT(4) DEFAULT NULL,
  `REVEND`                       INT(11) DEFAULT NULL,
  `TranslatedXML`                MEDIUMTEXT,
  `TranslatedXMLErrors`          TEXT,
  `TranslatedXMLRendered`        MEDIUMTEXT,
  `TranslatedXMLRenderedUpdated` DATETIME DEFAULT NULL,
  `TranslationLocale`            VARCHAR(45) DEFAULT NULL,
  `TranslationPercentage`        INT(11) DEFAULT NULL,
  `TranslatedTopicID`            INT(11) DEFAULT NULL,
  PRIMARY KEY (`TranslatedTopicDataID`, `REV`),
  KEY `FK73C2574A7C21108` (`REVEND`),
  KEY `FK73C2574DF74E053` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TranslatedTopicString`
--

CREATE TABLE IF NOT EXISTS `TranslatedTopicString` (
  `TranslatedTopicStringID` INT(11) NOT NULL AUTO_INCREMENT,
  `OriginalString`          TEXT,
  `TranslatedString`        TEXT,
  `TranslatedTopicDataID`   INT(11) NOT NULL,
  `FuzzyTranslation`        BIT(1)  NOT NULL,
  PRIMARY KEY (`TranslatedTopicStringID`),
  UNIQUE KEY `TranslatedTopicStringID` (`TranslatedTopicStringID`),
  KEY `FKDB83374A17E278CA` (`TranslatedTopicDataID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TranslatedTopicString_AUD`
--

CREATE TABLE IF NOT EXISTS `TranslatedTopicString_AUD` (
  `TranslatedTopicStringID` INT(11) NOT NULL,
  `REV`                     INT(11) NOT NULL,
  `REVTYPE`                 TINYINT(4) DEFAULT NULL,
  `REVEND`                  INT(11) DEFAULT NULL,
  `OriginalString`          TEXT,
  `TranslatedString`        TEXT,
  `TranslatedTopicDataID`   INT(11) DEFAULT NULL,
  `FuzzyTranslation`        BIT(1) DEFAULT NULL,
  PRIMARY KEY (`TranslatedTopicStringID`, `REV`),
  KEY `FK6D4EB01BA7C21108` (`REVEND`),
  KEY `FK6D4EB01BDF74E053` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TranslatedTopic_AUD`
--

CREATE TABLE IF NOT EXISTS `TranslatedTopic_AUD` (
  `TranslatedTopicID` INT(11) NOT NULL,
  `REV`               INT(11) NOT NULL,
  `REVTYPE`           TINYINT(4) DEFAULT NULL,
  `REVEND`            INT(11) DEFAULT NULL,
  `TopicID`           INT(11) DEFAULT NULL,
  `TopicRevision`     INT(11) DEFAULT NULL,
  PRIMARY KEY (`TranslatedTopicID`, `REV`),
  KEY `FKBEB70B2AA7C21108` (`REVEND`),
  KEY `FKBEB70B2ADF74E053` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `UserID`      INT(11)      NOT NULL AUTO_INCREMENT,
  `UserName`    VARCHAR(255) NOT NULL,
  `Description` TEXT,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `index2` (`UserName`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `UserRole`
--

CREATE TABLE IF NOT EXISTS `UserRole` (
  `UserRoleID` INT(11) NOT NULL AUTO_INCREMENT,
  `UserNameID` INT(11) NOT NULL,
  `RoleNameID` INT(11) NOT NULL,
  PRIMARY KEY (`UserRoleID`),
  UNIQUE KEY `UserRoleUnique` (`UserNameID`, `RoleNameID`),
  KEY `fk_UserRole_1` (`RoleNameID`),
  KEY `fk_UserRole_2` (`UserNameID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `UserRole_AUD`
--

CREATE TABLE IF NOT EXISTS `UserRole_AUD` (
  `UserRoleID` INT(11) NOT NULL,
  `REV`        INT(11) NOT NULL,
  `REVEND`     INT(11) DEFAULT NULL,
  `REVTYPE`    TINYINT(4) DEFAULT NULL,
  `RoleNameID` INT(11) DEFAULT NULL,
  `UserNameID` INT(11) DEFAULT NULL,
  PRIMARY KEY (`UserRoleID`, `REV`),
  KEY `FKCC262C52DF74E053` (`REV`),
  KEY `FKCC262C52A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `User_AUD`
--

CREATE TABLE IF NOT EXISTS `User_AUD` (
  `UserID`      INT(11) NOT NULL,
  `REV`         INT(11) NOT NULL,
  `REVEND`      INT(11) DEFAULT NULL,
  `REVTYPE`     TINYINT(4) DEFAULT NULL,
  `Description` TEXT,
  `UserName`    VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`UserID`, `REV`),
  KEY `FKF3FCA03CDF74E053` (`REV`),
  KEY `FKF3FCA03CA7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
