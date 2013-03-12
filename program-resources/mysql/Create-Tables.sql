-- phpMyAdmin SQL Dump
-- version 3.5.6
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 12, 2013 at 03:47 AM
-- Server version: 5.5.30
-- PHP Version: 5.4.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `PressGangCCMS`
--
 CREATE DATABASE /*!32312 IF NOT EXISTS*/ `PressGangCCMS` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `PressGangCCMS`;

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
  ROW_FORMAT = COMPRESSED
  COMMENT = 'This table holds blob constants for things like image files '
  AUTO_INCREMENT = 10;

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
  PRIMARY KEY (`BlobConstantsID`, `REV`) KEY_BLOCK_SIZE = 4,
  KEY `FKEEA7C6E3DF74E053` (`REV`) KEY_BLOCK_SIZE = 4,
  KEY `FKEEA7C6E3A7C21108` (`REVEND`) KEY_BLOCK_SIZE = 4
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  KEY_BLOCK_SIZE = 8;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1367;

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
  UNIQUE KEY `index3` (`CategoryName`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  COMMENT = 'Categories contain tags. The relationship between a tag and '
  AUTO_INCREMENT = 44;

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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpec`
--

CREATE TABLE IF NOT EXISTS `ContentSpec` (
  `ContentSpecID`   INT(11)      NOT NULL AUTO_INCREMENT,
  `ContentSpecType` INT(11)      NOT NULL,
  `lastPublished`   DATETIME DEFAULT NULL,
  `Locale`          VARCHAR(255) NOT NULL,
  `GlobalCondition` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecID`),
  UNIQUE KEY `ContentSpecID` (`ContentSpecID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 17;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecNode`
--

CREATE TABLE IF NOT EXISTS `ContentSpecNode` (
  `ContentSpecNodeID` INT(11)      NOT NULL AUTO_INCREMENT,
  `NodeTitle`         VARCHAR(255) NOT NULL,
  `NodeTargetID`      VARCHAR(255) DEFAULT NULL,
  `NodeType`          INT(11)      NOT NULL,
  `NodeCondition`     VARCHAR(255) DEFAULT NULL,
  `AdditionalText`    TEXT,
  `EntityID`          INT(11) DEFAULT NULL,
  `EntityRevision`    INT(11) DEFAULT NULL,
  `ContentSpecID`     INT(11) DEFAULT NULL,
  `NextNodeID`        INT(11) DEFAULT NULL,
  `ParentID`          INT(11) DEFAULT NULL,
  `PreviousNodeID`    INT(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecNodeID`),
  UNIQUE KEY `ContentSpecNodeID` (`ContentSpecNodeID`),
  UNIQUE KEY `ContentSpecID` (`ContentSpecID`, `PreviousNodeID`),
  UNIQUE KEY `ContentSpecID_2` (`ContentSpecID`, `NextNodeID`),
  KEY `FKD5DB8BD6FBF105B5` (`ContentSpecID`),
  KEY `FKD5DB8BD626ED9974` (`PreviousNodeID`),
  KEY `FKD5DB8BD6DC587C70` (`NextNodeID`),
  KEY `FKD5DB8BD63FF58465` (`ParentID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 74;

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
  `NodeTargetID`      VARCHAR(255) DEFAULT NULL,
  `NodeType`          INT(11) DEFAULT NULL,
  `NodeCondition`     VARCHAR(255) DEFAULT NULL,
  `AdditionalText`    TEXT,
  `EntityID`          INT(11) DEFAULT NULL,
  `EntityRevision`    INT(11) DEFAULT NULL,
  `ContentSpecID`     INT(11) DEFAULT NULL,
  `NextNodeID`        INT(11) DEFAULT NULL,
  `ParentID`          INT(11) DEFAULT NULL,
  `PreviousNodeID`    INT(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecNodeID`, `REV`),
  KEY `FK2311DEA7DE0FB5A8` (`REVEND`),
  KEY `FK2311DEA715C284F3` (`REV`)
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
  KEY `FKADEB4ACAFBF105B5` (`ContentSpecID`),
  KEY `FKADEB4ACA66658A59` (`ProjectID`)
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
  KEY `FKDE81039BDE0FB5A8` (`REVEND`),
  KEY `FKDE81039B15C284F3` (`REV`),
  KEY `ProjectID` (`ProjectID`),
  KEY `ContentSpecID` (`ContentSpecID`)
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
  KEY `FK676CDCB6FBF105B5` (`ContentSpecID`),
  KEY `FK676CDCB6BED5AE51` (`PropertyTagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 16;

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
  KEY `FKF7CFBF87DE0FB5A8` (`REVEND`),
  KEY `FKF7CFBF8715C284F3` (`REV`),
  KEY `ContentSpecID` (`ContentSpecID`),
  KEY `PropertyTagID` (`PropertyTagID`)
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
  KEY `FKE5EA3FCBFBF105B5` (`ContentSpecID`),
  KEY `FKE5EA3FCB10DDD1B` (`TagID`)
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
  KEY `FK640B901CDE0FB5A8` (`REVEND`),
  KEY `FK640B901C15C284F3` (`REV`),
  KEY `TagID` (`TagID`),
  KEY `ContentSpecID` (`ContentSpecID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpec_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpec_AUD` (
  `ContentSpecID`   INT(11) NOT NULL,
  `REV`             INT(11) NOT NULL,
  `REVTYPE`         TINYINT(4) DEFAULT NULL,
  `REVEND`          INT(11) DEFAULT NULL,
  `ContentSpecType` INT(11) DEFAULT NULL,
  `lastPublished`   DATETIME DEFAULT NULL,
  `Locale`          VARCHAR(255) DEFAULT NULL,
  `GlobalCondition` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecID`, `REV`),
  KEY `FKD5E29785DE0FB5A8` (`REVEND`),
  KEY `FKD5E2978515C284F3` (`REV`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `CSNodeToCSNode`
--

CREATE TABLE IF NOT EXISTS `CSNodeToCSNode` (
  `CSNodeToCSNodeID` INT(11) NOT NULL AUTO_INCREMENT,
  `RelationshipType` INT(11) NOT NULL,
  `MainNodeID`       INT(11) NOT NULL,
  `RelatedNodeID`    INT(11) NOT NULL,
  `Sort`             INT(11) DEFAULT NULL,
  PRIMARY KEY (`CSNodeToCSNodeID`),
  UNIQUE KEY `CSNodeToCSNodeID` (`CSNodeToCSNodeID`),
  KEY `FKC1429D7F3070C3A8` (`RelatedNodeID`),
  KEY `FKC1429D7F43664AB6` (`MainNodeID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 3;

-- --------------------------------------------------------

--
-- Table structure for table `CSNodeToCSNode_AUD`
--

CREATE TABLE IF NOT EXISTS `CSNodeToCSNode_AUD` (
  `CSNodeToCSNodeID` INT(11) NOT NULL,
  `REV`              INT(11) NOT NULL,
  `REVTYPE`          TINYINT(4) DEFAULT NULL,
  `REVEND`           INT(11) DEFAULT NULL,
  `RelationshipType` INT(11) DEFAULT NULL,
  `MainNodeID`       INT(11) DEFAULT NULL,
  `RelatedNodeID`    INT(11) DEFAULT NULL,
  `Sort`             INT(11) DEFAULT NULL,
  PRIMARY KEY (`CSNodeToCSNodeID`, `REV`),
  KEY `FKFBD5F3D0DE0FB5A8` (`REVEND`),
  KEY `FKFBD5F3D015C284F3` (`REV`),
  KEY `MainNodeID` (`MainNodeID`),
  KEY `RelatedNodeID` (`RelatedNodeID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `CSNodeToPropertyTag`
--

CREATE TABLE IF NOT EXISTS `CSNodeToPropertyTag` (
  `CSNodeToPropertyTagID` INT(11) NOT NULL AUTO_INCREMENT,
  `Value`                 TEXT,
  `CSNodeID`              INT(11) NOT NULL,
  `PropertyTagID`         INT(11) NOT NULL,
  PRIMARY KEY (`CSNodeToPropertyTagID`),
  UNIQUE KEY `CSNodeToPropertyTagID` (`CSNodeToPropertyTagID`),
  KEY `FK74746698BED5AE51` (`PropertyTagID`),
  KEY `FK74746698AB6B2C2D` (`CSNodeID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `CSNodeToPropertyTag_AUD`
--

CREATE TABLE IF NOT EXISTS `CSNodeToPropertyTag_AUD` (
  `CSNodeToPropertyTagID` INT(11) NOT NULL,
  `REV`                   INT(11) NOT NULL,
  `REVTYPE`               TINYINT(4) DEFAULT NULL,
  `REVEND`                INT(11) DEFAULT NULL,
  `Value`                 TEXT,
  `CSNodeID`              INT(11) DEFAULT NULL,
  `PropertyTagID`         INT(11) DEFAULT NULL,
  PRIMARY KEY (`CSNodeToPropertyTagID`, `REV`),
  KEY `FKC05B8869DE0FB5A8` (`REVEND`),
  KEY `FKC05B886915C284F3` (`REV`),
  KEY `CSNodeID` (`CSNodeID`),
  KEY `PropertyTagID` (`PropertyTagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `CSTranslatedNode`
--

CREATE TABLE IF NOT EXISTS `CSTranslatedNode` (
  `CSTranslatedNodeID` INT(11) NOT NULL AUTO_INCREMENT,
  `CSNodeID`           INT(11) NOT NULL,
  `CSNodeRevision`     INT(11) NOT NULL,
  PRIMARY KEY (`CSTranslatedNodeID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `CSTranslatedNodeString`
--

CREATE TABLE IF NOT EXISTS `CSTranslatedNodeString` (
  `CSTranslatedNodeStringID` INT(11)      NOT NULL AUTO_INCREMENT,
  `FuzzyTranslation`         BIT(1)       NOT NULL,
  `Locale`                   VARCHAR(255) NOT NULL,
  `OriginalString`           TEXT,
  `TranslatedString`         TEXT,
  `CSTranslatedNodeID`       INT(11)      NOT NULL,
  PRIMARY KEY (`CSTranslatedNodeStringID`),
  KEY `CSTranslatedNodeID` (`CSTranslatedNodeID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

-- --------------------------------------------------------

--
-- Table structure for table `CSTranslatedNodeString_AUD`
--

CREATE TABLE IF NOT EXISTS `CSTranslatedNodeString_AUD` (
  `CSTranslatedNodeStringID` INT(11) NOT NULL,
  `REV`                      INT(11) NOT NULL,
  `REVTYPE`                  TINYINT(4) DEFAULT NULL,
  `REVEND`                   INT(11) DEFAULT NULL,
  `FuzzyTranslation`         BIT(1) DEFAULT NULL,
  `Locale`                   VARCHAR(255) DEFAULT NULL,
  `OriginalString`           TEXT,
  `TranslatedString`         TEXT,
  `CSTranslatedNodeID`       INT(11) DEFAULT NULL,
  PRIMARY KEY (`CSTranslatedNodeStringID`, `REV`),
  KEY `FK36D44368DE0FB5A8` (`REVEND`),
  KEY `FK36D4436815C284F3` (`REV`),
  KEY `CSTranslatedNodeID` (`CSTranslatedNodeID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `CSTranslatedNode_AUD`
--

CREATE TABLE IF NOT EXISTS `CSTranslatedNode_AUD` (
  `CSTranslatedNodeID` INT(11) NOT NULL,
  `REV`                INT(11) NOT NULL,
  `REVTYPE`            TINYINT(4) DEFAULT NULL,
  `REVEND`             INT(11) DEFAULT NULL,
  `CSNodeID`           INT(11) DEFAULT NULL,
  `CSNodeRevision`     INT(11) DEFAULT NULL,
  PRIMARY KEY (`CSTranslatedNodeID`, `REV`),
  KEY `REVEND` (`REVEND`),
  KEY `REV` (`REV`)
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
  COMMENT = 'A filter is a saved query. A filter is made up of a row in t'
  AUTO_INCREMENT = 82;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 5113;

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
  KEY `FK2C96A387A7C21108` (`REVEND`),
  KEY `FilterID` (`FilterID`)
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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 83;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1;

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
  KEY `FK19074E3DF74E053` (`REV`),
  KEY `FilterID` (`FilterID`)
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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 557;

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
  KEY `FK574697EA7C21108` (`REVEND`),
  KEY `FilterID` (`FilterID`)
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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 469;

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
  KEY `FKA9A235B3A7C21108` (`REVEND`),
  KEY `FilterID` (`FilterID`)
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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 9;

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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 1309;

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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 2;

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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 1319;

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
  KEY `FK5F84C1144E83BBDA` (`REV`),
  KEY `ImageFileID` (`ImageFileID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 25;

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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 30;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 8;

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
  COMMENT = 'PropertyTagToPropertyTagCategoryID\nPropertyTagToPropertyTagC'
  AUTO_INCREMENT = 20;

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
  KEY `FK95A54314A7C21108` (`REVEND`),
  KEY `PropertyTagID` (`PropertyTagID`),
  KEY `PropertyTagCategoryID` (`PropertyTagCategoryID`)
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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 9;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 337603;

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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 10;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 9;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 2;

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
  KEY `FK26C6D818A7C21108` (`REVEND`),
  KEY `PrimaryRoleID` (`PrimaryRole`),
  KEY `SecondaryRoleID` (`SecondaryRole`)
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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

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
  COMMENT = 'This table holds the string constants used by Skynet, includ'
  AUTO_INCREMENT = 59;

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
  `TagDescription` TEXT
                   CHARACTER SET latin1,
  PRIMARY KEY (`TagID`),
  UNIQUE KEY `index4` (`TagName`),
  KEY `fk_Tag_1` (`TagID`),
  KEY `fk_Tag_2` (`TagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 551;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 611;

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
  KEY `FKC2173404A7C21108` (`REVEND`),
  KEY `TagID` (`TagID`),
  KEY `CategoryID` (`CategoryID`)
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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 599;

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
  KEY `FKB4A9F195A7C21108` (`REVEND`),
  KEY `TagID` (`TagID`),
  KEY `ProjectID` (`ProjectID`)
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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 219;

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
  KEY `FK937D2081A7C21108` (`REVEND`),
  KEY `TagID` (`TagID`),
  KEY `PropertyTagID` (`PropertyTagID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 73;

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
  KEY `FKF27E8B16A7C21108` (`REVEND`),
  KEY `PrimaryTagID` (`PrimaryTagID`),
  KEY `SecondaryTagID` (`SecondaryTagID`)
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
  `TagDescription` TEXT
                   CHARACTER SET latin1,
  `TagName`        VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`TagID`, `REV`),
  KEY `FK6E9284BDF74E053` (`REV`),
  KEY `FK6E9284BA7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

-- --------------------------------------------------------

--
-- Table structure for table `Topic`
--

CREATE TABLE IF NOT EXISTS `Topic` (
  `TopicID`         INT(11)      NOT NULL AUTO_INCREMENT,
  `TopicTitle`      VARCHAR(255) NOT NULL,
  `TopicText`       TEXT,
  `TopicTimeStamp`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `TopicSVNURL`     VARCHAR(512)
                    CHARACTER SET latin1 DEFAULT NULL,
  `TopicXML`        MEDIUMTEXT,
  `TopicLocale`     VARCHAR(255) NOT NULL,
  `TopicRendered`   TEXT
                    CHARACTER SET latin1,
  `TopicXMLDoctype` INT(11) DEFAULT NULL,
  PRIMARY KEY (`TopicID`),
  KEY `fk_Topic_1` (`TopicID`),
  KEY `FK4D3DD0FE2436E96` (`TopicID`),
  KEY `TopicTitle` (`TopicTitle`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 12745;

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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 12524;

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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 12935;

-- --------------------------------------------------------

--
-- Table structure for table `TopicSourceURL_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicSourceURL_AUD` (
  `TopicSourceURLID` INT(11) NOT NULL,
  `REV`              INT(11) NOT NULL,
  `REVEND`           INT(11) DEFAULT NULL,
  `REVTYPE`          TINYINT(4) DEFAULT NULL,
  `SourceURL`        VARCHAR(2048) DEFAULT NULL,
  `Title`            VARCHAR(255) DEFAULT NULL,
  `Description`      TEXT,
  PRIMARY KEY (`TopicSourceURLID`, `REV`),
  KEY `FK4FDDCE56DF74E053` (`REV`),
  KEY `FK4FDDCE56A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 1365;

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
  KEY `FK176CCFBDA7C21108` (`REVEND`),
  KEY `TopicID` (`TopicID`),
  KEY `BugzillaBugID` (`BugzillaBugID`)
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
  KEY `TopicToPropertyTagFK2` (`PropertyTagID`),
  KEY `PropertyTagToValue` (`PropertyTagID`, `Value`(64))
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 834308;

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
  KEY `FKDE6FC78CA7C21108` (`REVEND`),
  KEY `TopicID` (`TopicID`),
  KEY `PropertyTagID` (`PropertyTagID`),
  KEY `PropertyTagToValue` (`PropertyTagID`, `Value`(64))
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 105636;

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
  KEY `FK27826D21A7C21108` (`REVEND`),
  KEY `TagID` (`TagID`),
  KEY `TopicID` (`TopicID`)
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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 7367;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 12519;

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
  DEFAULT CHARSET = utf8
  AUTO_INCREMENT = 12915;

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
  KEY `FKA41247C0A7C21108` (`REVEND`),
  KEY `TopicID` (`TopicID`),
  KEY `TopicSourceURLID` (`TopicSourceURLID`)
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
  KEY `FKEDF852B6A7C21108` (`REVEND`),
  KEY `MainTopicID` (`MainTopicID`),
  KEY `RelatedTopicID` (`RelatedTopicID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Topic_AUD`
--

CREATE TABLE IF NOT EXISTS `Topic_AUD` (
  `TopicID`         INT(11) NOT NULL,
  `REV`             INT(11) NOT NULL,
  `REVEND`          INT(11) DEFAULT NULL,
  `REVTYPE`         TINYINT(4) DEFAULT NULL,
  `TopicAddedBy`    VARCHAR(512)
                    CHARACTER SET latin1 DEFAULT NULL,
  `TopicSVNURL`     VARCHAR(512)
                    CHARACTER SET latin1 DEFAULT NULL,
  `TopicText`       TEXT,
  `TopicTimeStamp`  DATETIME DEFAULT NULL,
  `TopicTitle`      VARCHAR(255) DEFAULT NULL,
  `TopicXML`        MEDIUMTEXT,
  `TopicRendered`   TEXT
                    CHARACTER SET latin1,
  `TopicLocale`     VARCHAR(255) DEFAULT NULL,
  `TopicXMLDoctype` INT(11) DEFAULT NULL,
  PRIMARY KEY (`TopicID`, `REV`),
  KEY `FK8E9CEB60DF74E053` (`REV`),
  KEY `FK8E9CEB60A7C21108` (`REVEND`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

-- --------------------------------------------------------

--
-- Table structure for table `TranslatedTopic`
--

CREATE TABLE IF NOT EXISTS `TranslatedTopic` (
  `TranslatedTopicID` INT(11) NOT NULL AUTO_INCREMENT,
  `TopicID`           INT(11) NOT NULL,
  `TopicRevision`     INT(11) NOT NULL,
  PRIMARY KEY (`TranslatedTopicID`),
  UNIQUE KEY `TopicRevision` (`TopicRevision`, `TopicID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 2334;

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
  UNIQUE KEY `TranslatedTopicID` (`TranslatedTopicID`, `TranslationLocale`),
  KEY `FKBEAB41239248FD56` (`TranslatedTopicID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 18890;

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
  KEY `FK73C2574DF74E053` (`REV`),
  KEY `TranslatedTopicID` (`TranslatedTopicID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 121698;

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
  KEY `FK6D4EB01BDF74E053` (`REV`),
  KEY `TranslatedTopicData` (`TranslatedTopicDataID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 90;

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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED
  AUTO_INCREMENT = 170;

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
  KEY `FKCC262C52A7C21108` (`REVEND`),
  KEY `RoleID` (`RoleNameID`),
  KEY `UserID` (`UserNameID`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

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
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPRESSED;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `BlobConstants_AUD`
--
 ALTER TABLE `BlobConstants_AUD`
ADD CONSTRAINT `FKEEA7C6E3A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKEEA7C6E3DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `BugzillaBug_AUD`
--
 ALTER TABLE `BugzillaBug_AUD`
ADD CONSTRAINT `FK8122C0E7A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK8122C0E7DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `Category_AUD`
--
ALTER TABLE `Category_AUD`
ADD CONSTRAINT `FK23378FEFA7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK23378FEFDF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ContentSpecNode`
--
 ALTER TABLE `ContentSpecNode`
ADD CONSTRAINT `FKD5DB8BD626ED9974` FOREIGN KEY (`PreviousNodeID`) REFERENCES `ContentSpecNode` (`ContentSpecNodeID`),
ADD CONSTRAINT `FKD5DB8BD63FF58465` FOREIGN KEY (`ParentID`) REFERENCES `ContentSpecNode` (`ContentSpecNodeID`),
ADD CONSTRAINT `FKD5DB8BD6DC587C70` FOREIGN KEY (`NextNodeID`) REFERENCES `ContentSpecNode` (`ContentSpecNodeID`),
ADD CONSTRAINT `FKD5DB8BD6FBF105B5` FOREIGN KEY (`ContentSpecID`) REFERENCES `ContentSpec` (`ContentSpecID`);

--
-- Constraints for table `ContentSpecNode_AUD`
--
 ALTER TABLE `ContentSpecNode_AUD`
ADD CONSTRAINT `FK2311DEA715C284F3` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK2311DEA7DE0FB5A8` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ContentSpecToProject`
--
 ALTER TABLE `ContentSpecToProject`
ADD CONSTRAINT `FKADEB4ACA66658A59` FOREIGN KEY (`ProjectID`) REFERENCES `Project` (`ProjectID`),
ADD CONSTRAINT `FKADEB4ACAFBF105B5` FOREIGN KEY (`ContentSpecID`) REFERENCES `ContentSpec` (`ContentSpecID`);

--
-- Constraints for table `ContentSpecToProject_AUD`
--
 ALTER TABLE `ContentSpecToProject_AUD`
ADD CONSTRAINT `FKDE81039B15C284F3` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKDE81039BDE0FB5A8` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ContentSpecToPropertyTag`
--
 ALTER TABLE `ContentSpecToPropertyTag`
ADD CONSTRAINT `FK676CDCB6BED5AE51` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`),
ADD CONSTRAINT `FK676CDCB6FBF105B5` FOREIGN KEY (`ContentSpecID`) REFERENCES `ContentSpec` (`ContentSpecID`);

--
-- Constraints for table `ContentSpecToPropertyTag_AUD`
--
 ALTER TABLE `ContentSpecToPropertyTag_AUD`
ADD CONSTRAINT `FKF7CFBF8715C284F3` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKF7CFBF87DE0FB5A8` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ContentSpecToTag`
--
 ALTER TABLE `ContentSpecToTag`
ADD CONSTRAINT `FKE5EA3FCB10DDD1B` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`),
ADD CONSTRAINT `FKE5EA3FCBFBF105B5` FOREIGN KEY (`ContentSpecID`) REFERENCES `ContentSpec` (`ContentSpecID`);

--
-- Constraints for table `ContentSpecToTag_AUD`
--
 ALTER TABLE `ContentSpecToTag_AUD`
ADD CONSTRAINT `FK640B901C15C284F3` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK640B901CDE0FB5A8` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ContentSpec_AUD`
--
 ALTER TABLE `ContentSpec_AUD`
ADD CONSTRAINT `FKD5E2978515C284F3` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKD5E29785DE0FB5A8` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `CSNodeToCSNode`
--
ALTER TABLE `CSNodeToCSNode`
ADD CONSTRAINT `FKC1429D7F3070C3A8` FOREIGN KEY (`RelatedNodeID`) REFERENCES `ContentSpecNode` (`ContentSpecNodeID`),
ADD CONSTRAINT `FKC1429D7F43664AB6` FOREIGN KEY (`MainNodeID`) REFERENCES `ContentSpecNode` (`ContentSpecNodeID`);

--
-- Constraints for table `CSNodeToCSNode_AUD`
--
 ALTER TABLE `CSNodeToCSNode_AUD`
ADD CONSTRAINT `FKFBD5F3D015C284F3` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKFBD5F3D0DE0FB5A8` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `CSNodeToPropertyTag`
--
 ALTER TABLE `CSNodeToPropertyTag`
ADD CONSTRAINT `FK74746698AB6B2C2D` FOREIGN KEY (`CSNodeID`) REFERENCES `ContentSpecNode` (`ContentSpecNodeID`),
ADD CONSTRAINT `FK74746698BED5AE51` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`);

--
-- Constraints for table `CSNodeToPropertyTag_AUD`
--
 ALTER TABLE `CSNodeToPropertyTag_AUD`
ADD CONSTRAINT `FKC05B886915C284F3` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKC05B8869DE0FB5A8` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `CSTranslatedNodeString`
--
 ALTER TABLE `CSTranslatedNodeString`
ADD CONSTRAINT `CSTranslatedNodeString_ibfk_1` FOREIGN KEY (`CSTranslatedNodeID`) REFERENCES `CSTranslatedNode` (`CSTranslatedNodeID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

--
-- Constraints for table `CSTranslatedNodeString_AUD`
--
 ALTER TABLE `CSTranslatedNodeString_AUD`
ADD CONSTRAINT `FK36D4436815C284F3` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK36D44368DE0FB5A8` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `CSTranslatedNode_AUD`
--
 ALTER TABLE `CSTranslatedNode_AUD`
ADD CONSTRAINT `CSTranslatedNode_AUD_ibfk_1` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `CSTranslatedNode_AUD_ibfk_2` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `FilterCategory`
--
 ALTER TABLE `FilterCategory`
ADD CONSTRAINT `FKBB45C0B6EB3A6876` FOREIGN KEY (`ProjectID`) REFERENCES `Project` (`ProjectID`),
ADD CONSTRAINT `fk_FilterCategory_1` FOREIGN KEY (`CategoryID`) REFERENCES `Category` (`CategoryID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_FilterCategory_2` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

--
-- Constraints for table `FilterCategory_AUD`
--
 ALTER TABLE `FilterCategory_AUD`
ADD CONSTRAINT `FK2C96A387A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK2C96A387DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `FilterField`
--
 ALTER TABLE `FilterField`
ADD CONSTRAINT `fk_Filter_Field_1` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

--
-- Constraints for table `FilterField_AUD`
--
ALTER TABLE `FilterField_AUD`
ADD CONSTRAINT `FK9B8B3513A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK9B8B3513DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `FilterLocale`
--
 ALTER TABLE `FilterLocale`
ADD CONSTRAINT `FK7CB6A01259256D82` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`);

--
-- Constraints for table `FilterLocale_AUD`
--
 ALTER TABLE `FilterLocale_AUD`
ADD CONSTRAINT `FK19074E3A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK19074E3DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `FilterOption`
--
 ALTER TABLE `FilterOption`
ADD CONSTRAINT `FK81EB1A2D59256D82` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`);

--
-- Constraints for table `FilterOption_AUD`
--
 ALTER TABLE `FilterOption_AUD`
ADD CONSTRAINT `FK574697EA7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK574697EDF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `FilterTag`
--
 ALTER TABLE `FilterTag`
ADD CONSTRAINT `fk_FilterTag_1` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_FilterTag_2` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

--
-- Constraints for table `FilterTag_AUD`
--
 ALTER TABLE `FilterTag_AUD`
ADD CONSTRAINT `FKA9A235B3A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKA9A235B3DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `Filter_AUD`
--
 ALTER TABLE `Filter_AUD`
ADD CONSTRAINT `FK1A445969A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK1A445969DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `Help_AUD`
--
 ALTER TABLE `Help_AUD`
ADD CONSTRAINT `FKD4CBD8B2A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKD4CBD8B2DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ImageFile_AUD`
--
 ALTER TABLE `ImageFile_AUD`
ADD CONSTRAINT `FK553BB7A8A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK553BB7A8DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `IntegerConstants_AUD`
--
ALTER TABLE `IntegerConstants_AUD`
ADD CONSTRAINT `FKA7C8D722A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKA7C8D722DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `LanguageImage_AUD`
--
 ALTER TABLE `LanguageImage_AUD`
ADD CONSTRAINT `FK5F84C11416D0EC8F` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK5F84C1144E83BBDA` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `Project_AUD`
--
 ALTER TABLE `Project_AUD`
ADD CONSTRAINT `FK2B68EC4AA7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK2B68EC4ADF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `PropertyTagCategory_AUD`
--
 ALTER TABLE `PropertyTagCategory_AUD`
ADD CONSTRAINT `FKE73E65D4A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKE73E65D4DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `PropertyTagToPropertyTagCategory`
--
 ALTER TABLE `PropertyTagToPropertyTagCategory`
ADD CONSTRAINT `PropertyTagToPropertTagCategoryFK1` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `PropertyTagToPropertTagCategoryFk2` FOREIGN KEY (`PropertyTagCategoryID`) REFERENCES `PropertyTagCategory` (`PropertyTagCategoryID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

--
-- Constraints for table `PropertyTagToPropertyTagCategory_AUD`
--
 ALTER TABLE `PropertyTagToPropertyTagCategory_AUD`
ADD CONSTRAINT `FK95A54314A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK95A54314DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `PropertyTag_AUD`
--
 ALTER TABLE `PropertyTag_AUD`
ADD CONSTRAINT `FK4825B8B6A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK4825B8B6DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `RelationshipTag_AUD`
--
 ALTER TABLE `RelationshipTag_AUD`
ADD CONSTRAINT `FK6CA98AF3A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK6CA98AF3DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `RoleToRole`
--
 ALTER TABLE `RoleToRole`
ADD CONSTRAINT `FKD0A0E5C78433D197` FOREIGN KEY (`SecondaryRole`) REFERENCES `Role` (`RoleID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `FKD0A0E5C7844F7725` FOREIGN KEY (`PrimaryRole`) REFERENCES `Role` (`RoleID`),
ADD CONSTRAINT `fk_RoleToRole_1` FOREIGN KEY (`RelationshipType`) REFERENCES `RoleToRoleRelationship` (`RoleToRoleRelationshipID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

--
-- Constraints for table `RoleToRoleRelationship_AUD`
--
 ALTER TABLE `RoleToRoleRelationship_AUD`
ADD CONSTRAINT `FKA68183F0A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKA68183F0DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `RoleToRole_AUD`
--
ALTER TABLE `RoleToRole_AUD`
ADD CONSTRAINT `FK26C6D818A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK26C6D818DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `Role_AUD`
--
 ALTER TABLE `Role_AUD`
ADD CONSTRAINT `FKF3FAE767A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKF3FAE767DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `StringConstants_AUD`
--
 ALTER TABLE `StringConstants_AUD`
ADD CONSTRAINT `FK399D7AEFA7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK399D7AEFDF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TagExclusion`
--
 ALTER TABLE `TagExclusion`
ADD CONSTRAINT `fk_TagExclusion_1` FOREIGN KEY (`Tag1ID`) REFERENCES `Tag` (`TagID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_TagExclusion_2` FOREIGN KEY (`Tag2ID`) REFERENCES `Tag` (`TagID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

--
-- Constraints for table `TagExclusion_AUD`
--
 ALTER TABLE `TagExclusion_AUD`
ADD CONSTRAINT `FK5BBFB345A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK5BBFB345DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TagToCategory`
--
 ALTER TABLE `TagToCategory`
ADD CONSTRAINT `FK8DF417B36F9851B8` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`)
  ON UPDATE CASCADE,
ADD CONSTRAINT `FK8DF417B37D4F054E` FOREIGN KEY (`CategoryID`) REFERENCES `Category` (`CategoryID`)
  ON DELETE CASCADE;

--
-- Constraints for table `TagToCategory_AUD`
--
 ALTER TABLE `TagToCategory_AUD`
ADD CONSTRAINT `FKC2173404A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKC2173404DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TagToProject`
--
 ALTER TABLE `TagToProject`
ADD CONSTRAINT `fk_TagToProject_1` FOREIGN KEY (`ProjectID`) REFERENCES `Project` (`ProjectID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_TagToProject_2` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

--
-- Constraints for table `TagToProject_AUD`
--
 ALTER TABLE `TagToProject_AUD`
ADD CONSTRAINT `FKB4A9F195A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKB4A9F195DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TagToPropertyTag`
--
 ALTER TABLE `TagToPropertyTag`
ADD CONSTRAINT `TagToPropertyTagFK1` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `TagToPropertyTagFK2` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

--
-- Constraints for table `TagToPropertyTag_AUD`
--
ALTER TABLE `TagToPropertyTag_AUD`
ADD CONSTRAINT `FK937D2081A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK937D2081DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TagToTag`
--
 ALTER TABLE `TagToTag`
ADD CONSTRAINT `fk_TagToTag_1` FOREIGN KEY (`RelationshipType`) REFERENCES `TagToTagRelationship` (`TagToTagRelationshipType`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_TagToTag_2` FOREIGN KEY (`PrimaryTagID`) REFERENCES `Tag` (`TagID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_TagToTag_3` FOREIGN KEY (`SecondaryTagID`) REFERENCES `Tag` (`TagID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

--
-- Constraints for table `TagToTagRelationship_AUD`
--
 ALTER TABLE `TagToTagRelationship_AUD`
ADD CONSTRAINT `FK583EA9EEA7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK583EA9EEDF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TagToTag_AUD`
--
 ALTER TABLE `TagToTag_AUD`
ADD CONSTRAINT `FKF27E8B16A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKF27E8B16DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `Tag_AUD`
--
 ALTER TABLE `Tag_AUD`
ADD CONSTRAINT `FK6E9284BA7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK6E9284BDF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TopicSecondOrderData_AUD`
--
 ALTER TABLE `TopicSecondOrderData_AUD`
ADD CONSTRAINT `FK8E541846A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK8E541846DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TopicSourceURL_AUD`
--
 ALTER TABLE `TopicSourceURL_AUD`
ADD CONSTRAINT `FK4FDDCE56A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK4FDDCE56DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TopicToBugzillaBug`
--
 ALTER TABLE `TopicToBugzillaBug`
ADD CONSTRAINT `TopicToBugzillaBugFK2` FOREIGN KEY (`BugzillaBugID`) REFERENCES `BugzillaBug` (`BugzillaBugID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `TopicToBugzillaBug_ibfk_1` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

--
-- Constraints for table `TopicToBugzillaBug_AUD`
--
 ALTER TABLE `TopicToBugzillaBug_AUD`
ADD CONSTRAINT `FK176CCFBDA7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK176CCFBDDF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TopicToPropertyTag`
--
 ALTER TABLE `TopicToPropertyTag`
ADD CONSTRAINT `TopicToPropertyTagFK1` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `TopicToPropertyTagFK2` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

--
-- Constraints for table `TopicToPropertyTag_AUD`
--
ALTER TABLE `TopicToPropertyTag_AUD`
ADD CONSTRAINT `FKDE6FC78CA7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKDE6FC78CDF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TopicToTag`
--
 ALTER TABLE `TopicToTag`
ADD CONSTRAINT `fk_TopicToTag_1` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_TopicToTag_2` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

--
-- Constraints for table `TopicToTag_AUD`
--
 ALTER TABLE `TopicToTag_AUD`
ADD CONSTRAINT `FK27826D21A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK27826D21DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TopicToTopic`
--
 ALTER TABLE `TopicToTopic`
ADD CONSTRAINT `fk_TopicToTopic_1` FOREIGN KEY (`MainTopicID`) REFERENCES `Topic` (`TopicID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_TopicToTopic_2` FOREIGN KEY (`RelatedTopicID`) REFERENCES `Topic` (`TopicID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_TopicToTopic_3` FOREIGN KEY (`RelationshipTagID`) REFERENCES `RelationshipTag` (`RelationshipTagID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

--
-- Constraints for table `TopicToTopicSecondOrderData`
--
 ALTER TABLE `TopicToTopicSecondOrderData`
ADD CONSTRAINT `FK303D33DF3340562` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `fk_TopicToTopicSecondOrderData_1` FOREIGN KEY (`TopicSecondOrderDataID`) REFERENCES `TopicSecondOrderData` (`TopicSecondOrderDataID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

--
-- Constraints for table `TopicToTopicSourceURL`
--
 ALTER TABLE `TopicToTopicSourceURL`
ADD CONSTRAINT `ForeignTopicID` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
ADD CONSTRAINT `ForeignTopicSourceURLID` FOREIGN KEY (`TopicSourceURLID`) REFERENCES `TopicSourceURL` (`TopicSourceURLID`)
  ON DELETE CASCADE
  ON UPDATE CASCADE;

--
-- Constraints for table `TopicToTopicSourceURL_AUD`
--
 ALTER TABLE `TopicToTopicSourceURL_AUD`
ADD CONSTRAINT `FKA41247C0A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKA41247C0DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TopicToTopic_AUD`
--
 ALTER TABLE `TopicToTopic_AUD`
ADD CONSTRAINT `FKEDF852B6A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKEDF852B6DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `Topic_AUD`
--
 ALTER TABLE `Topic_AUD`
ADD CONSTRAINT `FK8E9CEB60A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK8E9CEB60DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TranslatedTopicData`
--
 ALTER TABLE `TranslatedTopicData`
ADD CONSTRAINT `FKBEAB41239248FD56` FOREIGN KEY (`TranslatedTopicID`) REFERENCES `TranslatedTopic` (`TranslatedTopicID`);

--
-- Constraints for table `TranslatedTopicData_AUD`
--
 ALTER TABLE `TranslatedTopicData_AUD`
ADD CONSTRAINT `FK73C2574A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK73C2574DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TranslatedTopicString`
--
 ALTER TABLE `TranslatedTopicString`
ADD CONSTRAINT `FKDB83374A17E278CA` FOREIGN KEY (`TranslatedTopicDataID`) REFERENCES `TranslatedTopicData` (`TranslatedTopicDataID`);

--
-- Constraints for table `TranslatedTopicString_AUD`
--
 ALTER TABLE `TranslatedTopicString_AUD`
ADD CONSTRAINT `FK6D4EB01BA7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FK6D4EB01BDF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `TranslatedTopic_AUD`
--
 ALTER TABLE `TranslatedTopic_AUD`
ADD CONSTRAINT `FKBEB70B2AA7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKBEB70B2ADF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `UserRole`
--
 ALTER TABLE `UserRole`
ADD CONSTRAINT `fk_UserRole_1` FOREIGN KEY (`RoleNameID`) REFERENCES `Role` (`RoleID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `fk_UserRole_2` FOREIGN KEY (`UserNameID`) REFERENCES `User` (`UserID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

--
-- Constraints for table `UserRole_AUD`
--
 ALTER TABLE `UserRole_AUD`
ADD CONSTRAINT `FKCC262C52A7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKCC262C52DF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `User_AUD`
--
 ALTER TABLE `User_AUD`
ADD CONSTRAINT `FKF3FCA03CA7C21108` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
ADD CONSTRAINT `FKF3FCA03CDF74E053` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);