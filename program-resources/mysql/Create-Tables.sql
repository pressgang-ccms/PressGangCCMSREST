-- phpMyAdmin SQL Dump
-- version 3.5.3
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 08, 2012 at 06:17 PM
-- Server version: 5.5.28
-- PHP Version: 5.3.16

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `PressGangCCMS`
--
CREATE DATABASE /*!32312 IF NOT EXISTS*/ `PressGangCCMS` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `Skynet`;

-- --------------------------------------------------------

--
-- Table structure for table `BlobConstants`
--

CREATE TABLE IF NOT EXISTS `BlobConstants` (
  `BlobConstantsID` int(11) NOT NULL AUTO_INCREMENT,
  `ConstantName` varchar(45) NOT NULL,
  `ConstantValue` longblob,
  PRIMARY KEY (`BlobConstantsID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='This table holds blob constants for things like image files ' ;

-- --------------------------------------------------------

--
-- Table structure for table `BlobConstants_AUD`
--

CREATE TABLE IF NOT EXISTS `BlobConstants_AUD` (
  `BlobConstantsID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `ConstantName` varchar(45) DEFAULT NULL,
  `ConstantValue` longblob,
  PRIMARY KEY (`BlobConstantsID`,`REV`),
  KEY `FKEEA7C6E3DF74E053` (`REV`),
  KEY `FKEEA7C6E3A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `BugzillaBug`
--

CREATE TABLE IF NOT EXISTS `BugzillaBug` (
  `BugzillaBugID` int(11) NOT NULL AUTO_INCREMENT,
  `BugzillaBugBuzillaID` int(11) NOT NULL,
  `BugzillaBugSummary` text,
  `BugzillaBugOpen` bit(1) DEFAULT NULL,
  `BugzillaBugBugzillaID` int(11) NOT NULL,
  PRIMARY KEY (`BugzillaBugID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `BugzillaBug_AUD`
--

CREATE TABLE IF NOT EXISTS `BugzillaBug_AUD` (
  `BugzillaBugID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `BugzillaBugBugzillaID` int(11) DEFAULT NULL,
  `BugzillaBugOpen` bit(1) DEFAULT NULL,
  `BugzillaBugSummary` text,
  PRIMARY KEY (`BugzillaBugID`,`REV`),
  KEY `FK8122C0E7DF74E053` (`REV`),
  KEY `FK8122C0E7A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Category`
--

CREATE TABLE IF NOT EXISTS `Category` (
  `CategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `CategoryName` varchar(255) NOT NULL,
  `CategoryDescription` text,
  `CategorySort` int(11) DEFAULT NULL,
  `MutuallyExclusive` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`CategoryID`),
  UNIQUE KEY `index3` (`CategoryName`),
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Categories contain tags. The relationship between a tag and ' ;

-- --------------------------------------------------------

--
-- Table structure for table `Category_AUD`
--

CREATE TABLE IF NOT EXISTS `Category_AUD` (
  `CategoryID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `CategoryDescription` text,
  `CategoryName` varchar(255) DEFAULT NULL,
  `CategorySort` int(11) DEFAULT NULL,
  `MutuallyExclusive` bit(1) DEFAULT NULL,
  PRIMARY KEY (`CategoryID`,`REV`),
  KEY `FK23378FEFDF74E053` (`REV`),
  KEY `FK23378FEFA7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Filter`
--

CREATE TABLE IF NOT EXISTS `Filter` (
  `FilterID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterName` varchar(255) NOT NULL,
  `FilterDescription` text,
  PRIMARY KEY (`FilterID`),
  UNIQUE KEY `index2` (`FilterName`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='A filter is a saved query. A filter is made up of a row in t' ;

-- --------------------------------------------------------

--
-- Table structure for table `FilterCategory`
--

CREATE TABLE IF NOT EXISTS `FilterCategory` (
  `FilterCategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterID` int(11) NOT NULL,
  `CategoryID` int(11) NOT NULL,
  `CategoryState` int(11) NOT NULL DEFAULT '0',
  `ProjectID` int(11) DEFAULT NULL,
  PRIMARY KEY (`FilterCategoryID`),
  KEY `fk_FilterCategory_1` (`CategoryID`),
  KEY `fk_FilterCategory_2` (`FilterID`),
  KEY `FKBB45C0B6EB3A6876` (`ProjectID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `FilterCategory_AUD`
--

CREATE TABLE IF NOT EXISTS `FilterCategory_AUD` (
  `FilterCategoryID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `CategoryState` int(11) DEFAULT NULL,
  `CategoryID` int(11) DEFAULT NULL,
  `FilterID` int(11) DEFAULT NULL,
  `ProjectID` int(11) DEFAULT NULL,
  PRIMARY KEY (`FilterCategoryID`,`REV`),
  KEY `FK2C96A387DF74E053` (`REV`),
  KEY `FK2C96A387A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FilterField`
--

CREATE TABLE IF NOT EXISTS `FilterField` (
  `FilterFieldID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterID` int(11) NOT NULL,
  `Field` varchar(255) NOT NULL,
  `Value` text NOT NULL,
  `Description` text,
  PRIMARY KEY (`FilterFieldID`),
  UNIQUE KEY `index3` (`Field`,`FilterID`),
  KEY `fk_Filter_Field_1` (`FilterID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `FilterField_AUD`
--

CREATE TABLE IF NOT EXISTS `FilterField_AUD` (
  `FilterFieldID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `Description` text,
  `Field` varchar(255) DEFAULT NULL,
  `Value` text,
  `FilterID` int(11) DEFAULT NULL,
  PRIMARY KEY (`FilterFieldID`,`REV`),
  KEY `FK9B8B3513DF74E053` (`REV`),
  KEY `FK9B8B3513A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FilterLocale`
--

CREATE TABLE IF NOT EXISTS `FilterLocale` (
  `FilterLocaleID` int(11) NOT NULL AUTO_INCREMENT,
  `LocaleName` varchar(255) NOT NULL,
  `LocaleState` int(11) NOT NULL,
  `FilterID` int(11) NOT NULL,
  PRIMARY KEY (`FilterLocaleID`),
  UNIQUE KEY `FilterLocaleID` (`FilterLocaleID`),
  UNIQUE KEY `LocaleName` (`LocaleName`,`FilterID`),
  KEY `FK7CB6A01259256D82` (`FilterID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `FilterLocale_AUD`
--

CREATE TABLE IF NOT EXISTS `FilterLocale_AUD` (
  `FilterLocaleID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `LocaleName` varchar(255) DEFAULT NULL,
  `LocaleState` int(11) DEFAULT NULL,
  `FilterID` int(11) DEFAULT NULL,
  PRIMARY KEY (`FilterLocaleID`,`REV`),
  KEY `FK19074E3A7C21108` (`REVEND`),
  KEY `FK19074E3DF74E053` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FilterOption`
--

CREATE TABLE IF NOT EXISTS `FilterOption` (
  `FilterOptionID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterOptionName` varchar(255) NOT NULL,
  `FilterOptionValue` varchar(255) NOT NULL,
  `FilterID` int(11) NOT NULL,
  PRIMARY KEY (`FilterOptionID`),
  UNIQUE KEY `FilterOptionID` (`FilterOptionID`),
  UNIQUE KEY `FilterOptionName` (`FilterOptionName`,`FilterID`),
  KEY `FK81EB1A2D59256D82` (`FilterID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `FilterOption_AUD`
--

CREATE TABLE IF NOT EXISTS `FilterOption_AUD` (
  `FilterOptionID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `FilterOptionName` varchar(255) DEFAULT NULL,
  `FilterOptionValue` varchar(255) DEFAULT NULL,
  `FilterID` int(11) DEFAULT NULL,
  PRIMARY KEY (`FilterOptionID`,`REV`),
  KEY `FK574697EDF74E053` (`REV`),
  KEY `FK574697EA7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `FilterTag`
--

CREATE TABLE IF NOT EXISTS `FilterTag` (
  `FilterTagID` int(11) NOT NULL AUTO_INCREMENT,
  `FilterID` int(11) NOT NULL,
  `TagID` int(11) NOT NULL,
  `TagState` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`FilterTagID`),
  KEY `fk_FilterTag_1` (`TagID`),
  KEY `fk_FilterTag_2` (`FilterID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `FilterTag_AUD`
--

CREATE TABLE IF NOT EXISTS `FilterTag_AUD` (
  `FilterTagID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `TagState` int(11) DEFAULT NULL,
  `FilterID` int(11) DEFAULT NULL,
  `TagID` int(11) DEFAULT NULL,
  PRIMARY KEY (`FilterTagID`,`REV`),
  KEY `FKA9A235B3DF74E053` (`REV`),
  KEY `FKA9A235B3A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Filter_AUD`
--

CREATE TABLE IF NOT EXISTS `Filter_AUD` (
  `FilterID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `FilterDescription` text,
  `FilterName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`FilterID`,`REV`),
  KEY `FK1A445969DF74E053` (`REV`),
  KEY `FK1A445969A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Help`
--

CREATE TABLE IF NOT EXISTS `Help` (
  `HelpID` int(11) NOT NULL AUTO_INCREMENT,
  `TableColID` varchar(255) DEFAULT NULL,
  `HelpText` text,
  PRIMARY KEY (`HelpID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `Help_AUD`
--

CREATE TABLE IF NOT EXISTS `Help_AUD` (
  `HelpID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `HelpText` text,
  `TableColID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HelpID`,`REV`),
  KEY `FKD4CBD8B2DF74E053` (`REV`),
  KEY `FKD4CBD8B2A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ImageFile`
--

CREATE TABLE IF NOT EXISTS `ImageFile` (
  `ImageFileID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` text,
  PRIMARY KEY (`ImageFileID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `ImageFile_AUD`
--

CREATE TABLE IF NOT EXISTS `ImageFile_AUD` (
  `ImageFileID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `Description` text,
  PRIMARY KEY (`ImageFileID`,`REV`),
  KEY `FK553BB7A8DF74E053` (`REV`),
  KEY `FK553BB7A8A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `IntegerConstants`
--

CREATE TABLE IF NOT EXISTS `IntegerConstants` (
  `IntegerConstantsID` int(11) NOT NULL AUTO_INCREMENT,
  `ConstantName` varchar(45) NOT NULL,
  `ConstantValue` int(11) DEFAULT NULL,
  PRIMARY KEY (`IntegerConstantsID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `IntegerConstants_AUD`
--

CREATE TABLE IF NOT EXISTS `IntegerConstants_AUD` (
  `IntegerConstantsID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `ConstantName` varchar(45) DEFAULT NULL,
  `ConstantValue` int(11) DEFAULT NULL,
  PRIMARY KEY (`IntegerConstantsID`,`REV`),
  KEY `FKA7C8D722DF74E053` (`REV`),
  KEY `FKA7C8D722A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `LanguageImage`
--

CREATE TABLE IF NOT EXISTS `LanguageImage` (
  `LanguageImageID` int(11) NOT NULL AUTO_INCREMENT,
  `ImageFileID` int(11) NOT NULL,
  `ThumbnailData` mediumblob,
  `ImageDataBase64` mediumblob,
  `Locale` varchar(255) NOT NULL,
  `OriginalFileName` varchar(255) DEFAULT NULL,
  `ImageData` mediumblob,
  PRIMARY KEY (`LanguageImageID`),
  KEY `FK15D2ACC3E0AD6B52` (`ImageFileID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `LanguageImage_AUD`
--

CREATE TABLE IF NOT EXISTS `LanguageImage_AUD` (
  `LanguageImageID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `ImageData` mediumblob,
  `ImageDataBase64` mediumblob,
  `Locale` varchar(255) DEFAULT NULL,
  `OriginalFileName` varchar(255) DEFAULT NULL,
  `ThumbnailData` mediumblob,
  `ImageFileID` int(11) DEFAULT NULL,
  PRIMARY KEY (`LanguageImageID`,`REV`),
  KEY `FK5F84C114A7C21108` (`REVEND`),
  KEY `FK5F84C114DF74E053` (`REV`),
  KEY `FK5F84C11416D0EC8F` (`REVEND`),
  KEY `FK5F84C1144E83BBDA` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Project`
--

CREATE TABLE IF NOT EXISTS `Project` (
  `ProjectID` int(11) NOT NULL AUTO_INCREMENT,
  `ProjectName` varchar(255) NOT NULL,
  `ProjectDescription` text,
  PRIMARY KEY (`ProjectID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `Project_AUD`
--

CREATE TABLE IF NOT EXISTS `Project_AUD` (
  `ProjectID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `ProjectDescription` text,
  `ProjectName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ProjectID`,`REV`),
  KEY `FK2B68EC4ADF74E053` (`REV`),
  KEY `FK2B68EC4AA7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `PropertyTag`
--

CREATE TABLE IF NOT EXISTS `PropertyTag` (
  `PropertyTagID` int(11) NOT NULL AUTO_INCREMENT,
  `PropertyTagName` varchar(255) NOT NULL,
  `PropertyTagDescription` text,
  `PropertyTagRegex` text NOT NULL,
  `PropertyTagCanBeNull` bit(1) NOT NULL,
  `PropertyTagIsUnique` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`PropertyTagID`),
  UNIQUE KEY `PropertyTagUnique` (`PropertyTagName`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `PropertyTagCategory`
--

CREATE TABLE IF NOT EXISTS `PropertyTagCategory` (
  `PropertyTagCategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `PropertyTagCategoryName` varchar(255) NOT NULL,
  `PropertyTagCategoryDescription` text,
  PRIMARY KEY (`PropertyTagCategoryID`),
  UNIQUE KEY `PropertyTagCategoryNameUnique` (`PropertyTagCategoryName`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `PropertyTagCategory_AUD`
--

CREATE TABLE IF NOT EXISTS `PropertyTagCategory_AUD` (
  `PropertyTagCategoryID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `PropertyTagCategoryDescription` text,
  `PropertyTagCategoryName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`PropertyTagCategoryID`,`REV`),
  KEY `FKE73E65D4DF74E053` (`REV`),
  KEY `FKE73E65D4A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `PropertyTagToPropertyTagCategory`
--

CREATE TABLE IF NOT EXISTS `PropertyTagToPropertyTagCategory` (
  `PropertyTagToPropertyTagCategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `PropertyTagID` int(11) NOT NULL,
  `PropertyTagCategoryID` int(11) NOT NULL,
  `Sorting` int(11) DEFAULT NULL,
  PRIMARY KEY (`PropertyTagToPropertyTagCategoryID`),
  UNIQUE KEY `PropertyTagToPropertTagCategoryUnique` (`PropertyTagID`,`PropertyTagCategoryID`),
  KEY `PropertyTagToPropertTagCategoryFk2` (`PropertyTagCategoryID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='PropertyTagToPropertyTagCategoryID\nPropertyTagToPropertyTagC' ;

-- --------------------------------------------------------

--
-- Table structure for table `PropertyTagToPropertyTagCategory_AUD`
--

CREATE TABLE IF NOT EXISTS `PropertyTagToPropertyTagCategory_AUD` (
  `PropertyTagToPropertyTagCategoryID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `Sorting` int(11) DEFAULT NULL,
  `PropertyTagID` int(11) DEFAULT NULL,
  `PropertyTagCategoryID` int(11) DEFAULT NULL,
  PRIMARY KEY (`PropertyTagToPropertyTagCategoryID`,`REV`),
  KEY `FK95A54314DF74E053` (`REV`),
  KEY `FK95A54314A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `PropertyTag_AUD`
--

CREATE TABLE IF NOT EXISTS `PropertyTag_AUD` (
  `PropertyTagID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `PropertyTagCanBeNull` bit(1) DEFAULT NULL,
  `PropertyTagDescription` text,
  `PropertyTagName` varchar(255) DEFAULT NULL,
  `PropertyTagRegex` text,
  `PropertyTagIsUnique` bit(1) DEFAULT NULL,
  PRIMARY KEY (`PropertyTagID`,`REV`),
  KEY `FK4825B8B6DF74E053` (`REV`),
  KEY `FK4825B8B6A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `RelationshipTag`
--

CREATE TABLE IF NOT EXISTS `RelationshipTag` (
  `RelationshipTagID` int(11) NOT NULL AUTO_INCREMENT,
  `RelationshipTagName` varchar(255) NOT NULL,
  `RelationshipTagDescription` text,
  PRIMARY KEY (`RelationshipTagID`),
  UNIQUE KEY `RelationshipTagNameUnique` (`RelationshipTagName`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `RelationshipTag_AUD`
--

CREATE TABLE IF NOT EXISTS `RelationshipTag_AUD` (
  `RelationshipTagId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `RelationshipTagDescription` text,
  `RelationshipTagName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`RelationshipTagId`,`REV`),
  KEY `FK6CA98AF3DF74E053` (`REV`),
  KEY `FK6CA98AF3A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `REVINFO`
--

CREATE TABLE IF NOT EXISTS `REVINFO` (
  `REV` int(11) NOT NULL AUTO_INCREMENT,
  `REVTSTMP` bigint(20) DEFAULT NULL,
  `Flag` tinyint(4) DEFAULT NULL,
  `Message` text,
  `UserName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`REV`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `Role`
--

CREATE TABLE IF NOT EXISTS `Role` (
  `RoleID` int(11) NOT NULL AUTO_INCREMENT,
  `RoleName` varchar(255) NOT NULL,
  `Description` text,
  PRIMARY KEY (`RoleID`),
  UNIQUE KEY `index2` (`RoleName`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `RoleToRole`
--

CREATE TABLE IF NOT EXISTS `RoleToRole` (
  `RoleToRoleID` int(11) NOT NULL AUTO_INCREMENT,
  `PrimaryRole` int(11) NOT NULL,
  `RelationshipType` int(11) NOT NULL,
  `SecondaryRole` int(11) NOT NULL,
  PRIMARY KEY (`RoleToRoleID`),
  UNIQUE KEY `RoleToRoleID` (`RoleToRoleID`),
  UNIQUE KEY `PrimaryRole` (`PrimaryRole`,`SecondaryRole`,`RelationshipType`),
  KEY `FKD0A0E5C78433D197` (`SecondaryRole`),
  KEY `FKD0A0E5C7844F7725` (`PrimaryRole`),
  KEY `fk_RoleToRole_1` (`RelationshipType`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `RoleToRoleRelationship`
--

CREATE TABLE IF NOT EXISTS `RoleToRoleRelationship` (
  `RoleToRoleRelationshipID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` text,
  PRIMARY KEY (`RoleToRoleRelationshipID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `RoleToRoleRelationship_AUD`
--

CREATE TABLE IF NOT EXISTS `RoleToRoleRelationship_AUD` (
  `RoleToRoleRelationshipID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `Description` text,
  PRIMARY KEY (`RoleToRoleRelationshipID`,`REV`),
  KEY `FKA68183F0DF74E053` (`REV`),
  KEY `FKA68183F0A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `RoleToRole_AUD`
--

CREATE TABLE IF NOT EXISTS `RoleToRole_AUD` (
  `RoleToRoleID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `PrimaryRole` int(11) DEFAULT NULL,
  `RelationshipType` int(11) DEFAULT NULL,
  `SecondaryRole` int(11) DEFAULT NULL,
  PRIMARY KEY (`RoleToRoleID`,`REV`),
  KEY `FK26C6D818DF74E053` (`REV`),
  KEY `FK26C6D818A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Role_AUD`
--

CREATE TABLE IF NOT EXISTS `Role_AUD` (
  `RoleID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `Description` text,
  `RoleName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`RoleID`,`REV`),
  KEY `FKF3FAE767DF74E053` (`REV`),
  KEY `FKF3FAE767A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `StringConstants`
--

CREATE TABLE IF NOT EXISTS `StringConstants` (
  `StringConstantsID` int(11) NOT NULL AUTO_INCREMENT,
  `ConstantName` varchar(45) NOT NULL,
  `ConstantValue` mediumtext,
  PRIMARY KEY (`StringConstantsID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='This table holds the string constants used by Skynet, includ' ;

-- --------------------------------------------------------

--
-- Table structure for table `StringConstants_AUD`
--

CREATE TABLE IF NOT EXISTS `StringConstants_AUD` (
  `StringConstantsID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `ConstantName` varchar(45) DEFAULT NULL,
  `ConstantValue` mediumtext,
  PRIMARY KEY (`StringConstantsID`,`REV`),
  KEY `FK399D7AEFDF74E053` (`REV`),
  KEY `FK399D7AEFA7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Tag`
--

CREATE TABLE IF NOT EXISTS `Tag` (
  `TagID` int(11) NOT NULL AUTO_INCREMENT,
  `TagName` varchar(255) NOT NULL,
  `TagDescription` text,
  PRIMARY KEY (`TagID`),
  UNIQUE KEY `index4` (`TagName`),
  KEY `fk_Tag_1` (`TagID`),
  KEY `fk_Tag_2` (`TagID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TagExclusion`
--

CREATE TABLE IF NOT EXISTS `TagExclusion` (
  `Tag1ID` int(11) NOT NULL,
  `Tag2ID` int(11) NOT NULL,
  PRIMARY KEY (`Tag1ID`,`Tag2ID`),
  KEY `fk_TagExclusion_1` (`Tag1ID`),
  KEY `fk_TagExclusion_2` (`Tag2ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagExclusion_AUD`
--

CREATE TABLE IF NOT EXISTS `TagExclusion_AUD` (
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `Tag1ID` int(11) NOT NULL,
  `Tag2ID` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`Tag1ID`,`Tag2ID`),
  KEY `FK5BBFB345DF74E053` (`REV`),
  KEY `FK5BBFB345A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToCategory`
--

CREATE TABLE IF NOT EXISTS `TagToCategory` (
  `TagToCategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `TagID` int(11) NOT NULL COMMENT 'References the TagID that is to be placed into the Category.',
  `CategoryID` int(11) NOT NULL COMMENT 'References the CategoryID that the Tag is to be made a child of.',
  `Sorting` int(11) DEFAULT NULL COMMENT 'Defines the sorting order of the Tag in the Category.',
  PRIMARY KEY (`TagToCategoryID`),
  UNIQUE KEY `Unique` (`TagID`,`CategoryID`),
  KEY `fk_TagToCategory_1` (`TagID`),
  KEY `fk_TagToCategory_2` (`CategoryID`),
  KEY `FK8DF417B37D4F054E` (`CategoryID`),
  KEY `FK8DF417B36F9851B8` (`TagID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TagToCategory_AUD`
--

CREATE TABLE IF NOT EXISTS `TagToCategory_AUD` (
  `TagToCategoryID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `Sorting` int(11) DEFAULT NULL,
  `CategoryID` int(11) DEFAULT NULL,
  `TagID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TagToCategoryID`,`REV`),
  KEY `FKC2173404DF74E053` (`REV`),
  KEY `FKC2173404A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToProject`
--

CREATE TABLE IF NOT EXISTS `TagToProject` (
  `TagToProjectID` int(11) NOT NULL AUTO_INCREMENT,
  `ProjectID` int(11) NOT NULL,
  `TagID` int(11) NOT NULL,
  PRIMARY KEY (`TagToProjectID`),
  UNIQUE KEY `index4` (`ProjectID`,`TagID`),
  KEY `fk_TagToProject_1` (`ProjectID`),
  KEY `fk_TagToProject_2` (`TagID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TagToProject_AUD`
--

CREATE TABLE IF NOT EXISTS `TagToProject_AUD` (
  `TagToProjectID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `ProjectID` int(11) DEFAULT NULL,
  `TagID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TagToProjectID`,`REV`),
  KEY `FKB4A9F195DF74E053` (`REV`),
  KEY `FKB4A9F195A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToPropertyTag`
--

CREATE TABLE IF NOT EXISTS `TagToPropertyTag` (
  `TagToPropertyTagID` int(11) NOT NULL AUTO_INCREMENT,
  `TagID` int(11) NOT NULL,
  `PropertyTagID` int(11) NOT NULL,
  `Value` text,
  PRIMARY KEY (`TagToPropertyTagID`),
  KEY `TagIDFK` (`TagID`),
  KEY `PropertyTagIDFK` (`PropertyTagID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TagToPropertyTag_AUD`
--

CREATE TABLE IF NOT EXISTS `TagToPropertyTag_AUD` (
  `TagToPropertyTagID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `Value` text,
  `PropertyTagID` int(11) DEFAULT NULL,
  `TagID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TagToPropertyTagID`,`REV`),
  KEY `FK937D2081DF74E053` (`REV`),
  KEY `FK937D2081A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToTag`
--

CREATE TABLE IF NOT EXISTS `TagToTag` (
  `TagToTagID` int(11) NOT NULL AUTO_INCREMENT,
  `PrimaryTagID` int(11) NOT NULL,
  `SecondaryTagID` int(11) NOT NULL,
  `RelationshipType` int(11) NOT NULL,
  PRIMARY KEY (`TagToTagID`),
  UNIQUE KEY `UNIQUE` (`PrimaryTagID`,`SecondaryTagID`,`RelationshipType`),
  KEY `fk_TagToTag_1` (`RelationshipType`),
  KEY `fk_TagToTag_2` (`PrimaryTagID`),
  KEY `fk_TagToTag_3` (`SecondaryTagID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TagToTagRelationship`
--

CREATE TABLE IF NOT EXISTS `TagToTagRelationship` (
  `TagToTagRelationshipType` int(11) NOT NULL,
  `TagToTagRelationshipDescription` text,
  PRIMARY KEY (`TagToTagRelationshipType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToTagRelationship_AUD`
--

CREATE TABLE IF NOT EXISTS `TagToTagRelationship_AUD` (
  `TagToTagRelationshipType` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `TagToTagRelationshipDescription` text,
  PRIMARY KEY (`TagToTagRelationshipType`,`REV`),
  KEY `FK583EA9EEDF74E053` (`REV`),
  KEY `FK583EA9EEA7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToTag_AUD`
--

CREATE TABLE IF NOT EXISTS `TagToTag_AUD` (
  `TagToTagID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `RelationshipType` int(11) DEFAULT NULL,
  `PrimaryTagID` int(11) DEFAULT NULL,
  `SecondaryTagID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TagToTagID`,`REV`),
  KEY `FKF27E8B16DF74E053` (`REV`),
  KEY `FKF27E8B16A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Tag_AUD`
--

CREATE TABLE IF NOT EXISTS `Tag_AUD` (
  `TagID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `TagDescription` text,
  `TagName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`TagID`,`REV`),
  KEY `FK6E9284BDF74E053` (`REV`),
  KEY `FK6E9284BA7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Topic`
--

CREATE TABLE IF NOT EXISTS `Topic` (
  `TopicID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicTitle` varchar(255) DEFAULT NULL,
  `TopicText` text,
  `TopicTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `TopicSVNURL` varchar(255) DEFAULT NULL,
  `TopicXML` mediumtext,
  `TopicLocale` varchar(255) NOT NULL,
  `TopicRendered` text,
  PRIMARY KEY (`TopicID`),
  KEY `fk_Topic_1` (`TopicID`),
  KEY `FK4D3DD0FE2436E96` (`TopicID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TopicSecondOrderData`
--

CREATE TABLE IF NOT EXISTS `TopicSecondOrderData` (
  `TopicSecondOrderDataID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicXMLErrors` text,
  `TopicHTMLView` mediumtext,
  `TopicID` int(11) NOT NULL,
  PRIMARY KEY (`TopicSecondOrderDataID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TopicSecondOrderData_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicSecondOrderData_AUD` (
  `TopicID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `TopicHTMLView` mediumtext,
  `TopicXMLErrors` text,
  `TopicSecondOrderDataID` int(11) NOT NULL,
  PRIMARY KEY (`TopicSecondOrderDataID`,`REV`),
  KEY `FK8E541846DF74E053` (`REV`),
  KEY `FK8E541846A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicSourceURL`
--

CREATE TABLE IF NOT EXISTS `TopicSourceURL` (
  `TopicSourceURLID` int(11) NOT NULL AUTO_INCREMENT,
  `SourceURL` varchar(2048) NOT NULL,
  `Description` text,
  `Title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`TopicSourceURLID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TopicSourceURL_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicSourceURL_AUD` (
  `TopicSourceURLID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `SourceURL` varchar(2048) DEFAULT NULL,
  `Title` varchar(255) DEFAULT NULL,
  `Description` text,
  PRIMARY KEY (`TopicSourceURLID`,`REV`),
  KEY `FK4FDDCE56DF74E053` (`REV`),
  KEY `FK4FDDCE56A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToBugzillaBug`
--

CREATE TABLE IF NOT EXISTS `TopicToBugzillaBug` (
  `TopicToBugzillaBugID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `BugzillaBugID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToBugzillaBugID`),
  UNIQUE KEY `TopicIDBugzillaBugIDUnique` (`TopicID`,`BugzillaBugID`),
  UNIQUE KEY `TopicToBugzillaBugBugzillaBugIDUnique` (`BugzillaBugID`),
  KEY `TopicToBugzillaBugFK2` (`BugzillaBugID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToBugzillaBug_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicToBugzillaBug_AUD` (
  `TopicToBugzillaBugID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `BugzillaBugID` int(11) DEFAULT NULL,
  `TopicID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TopicToBugzillaBugID`,`REV`),
  KEY `FK176CCFBDDF74E053` (`REV`),
  KEY `FK176CCFBDA7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToPropertyTag`
--

CREATE TABLE IF NOT EXISTS `TopicToPropertyTag` (
  `TopicToPropertyTagID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `PropertyTagID` int(11) NOT NULL,
  `Value` text,
  PRIMARY KEY (`TopicToPropertyTagID`),
  KEY `TopicToPropertyTagFK1` (`TopicID`),
  KEY `TopicToPropertyTagFK2` (`PropertyTagID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToPropertyTag_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicToPropertyTag_AUD` (
  `TopicToPropertyTagID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `Value` text,
  `PropertyTagID` int(11) DEFAULT NULL,
  `TopicID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TopicToPropertyTagID`,`REV`),
  KEY `FKDE6FC78CDF74E053` (`REV`),
  KEY `FKDE6FC78CA7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToTag`
--

CREATE TABLE IF NOT EXISTS `TopicToTag` (
  `TopicToTagID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `TagID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToTagID`),
  UNIQUE KEY `UNIQUE` (`TopicID`,`TagID`),
  KEY `fk_TopicToTag_1` (`TopicID`),
  KEY `fk_TopicToTag_2` (`TagID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToTag_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicToTag_AUD` (
  `TopicToTagID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `TagID` int(11) DEFAULT NULL,
  `TopicID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TopicToTagID`,`REV`),
  KEY `FK27826D21DF74E053` (`REV`),
  KEY `FK27826D21A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToTopic`
--

CREATE TABLE IF NOT EXISTS `TopicToTopic` (
  `TopicToTopicID` int(11) NOT NULL AUTO_INCREMENT,
  `MainTopicID` int(11) NOT NULL,
  `RelatedTopicID` int(11) NOT NULL,
  `RelationshipTagID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToTopicID`),
  UNIQUE KEY `TopicToTopicUnique` (`MainTopicID`,`RelatedTopicID`,`RelationshipTagID`),
  KEY `fk_TopicToTopic_1` (`MainTopicID`),
  KEY `fk_TopicToTopic_2` (`RelatedTopicID`),
  KEY `fk_TopicToTopic_3` (`RelationshipTagID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToTopicSecondOrderData`
--

CREATE TABLE IF NOT EXISTS `TopicToTopicSecondOrderData` (
  `TopicToTopicSecondOrderDataID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `TopicSecondOrderDataID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToTopicSecondOrderDataID`),
  UNIQUE KEY `TopicToTopicSecondOrderDataTopicIdUnique` (`TopicID`),
  UNIQUE KEY `TopicToTopicSecondOrderDataTopicSecondOrderDataIdUnique` (`TopicSecondOrderDataID`),
  KEY `FK303D33DF3340562` (`TopicID`),
  KEY `fk_TopicToTopicSecondOrderData_1` (`TopicSecondOrderDataID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToTopicSourceURL`
--

CREATE TABLE IF NOT EXISTS `TopicToTopicSourceURL` (
  `TopicToTopicSourceURLID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `TopicSourceURLID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToTopicSourceURLID`),
  UNIQUE KEY `UniqueIndex` (`TopicID`,`TopicSourceURLID`),
  UNIQUE KEY `UniqueTopicSourceURLID` (`TopicSourceURLID`),
  KEY `ForeignTopicSourceURLID` (`TopicSourceURLID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToTopicSourceURL_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicToTopicSourceURL_AUD` (
  `TopicToTopicSourceURLID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `TopicID` int(11) DEFAULT NULL,
  `TopicSourceURLID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TopicToTopicSourceURLID`,`REV`),
  KEY `FKA41247C0DF74E053` (`REV`),
  KEY `FKA41247C0A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicToTopic_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicToTopic_AUD` (
  `TopicToTopicID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `MainTopicID` int(11) DEFAULT NULL,
  `RelatedTopicID` int(11) DEFAULT NULL,
  `RelationshipTagID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TopicToTopicID`,`REV`),
  KEY `FKEDF852B6DF74E053` (`REV`),
  KEY `FKEDF852B6A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Topic_AUD`
--

CREATE TABLE IF NOT EXISTS `Topic_AUD` (
  `TopicID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `TopicAddedBy` varchar(255) DEFAULT NULL,
  `TopicSVNURL` varchar(255) DEFAULT NULL,
  `TopicText` text,
  `TopicTimeStamp` datetime DEFAULT NULL,
  `TopicTitle` varchar(255) DEFAULT NULL,
  `TopicXML` mediumtext,
  `TopicRendered` text,
  `TopicLocale` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`TopicID`,`REV`),
  KEY `FK8E9CEB60DF74E053` (`REV`),
  KEY `FK8E9CEB60A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TranslatedTopic`
--

CREATE TABLE IF NOT EXISTS `TranslatedTopic` (
  `TranslatedTopicID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicID` int(11) NOT NULL,
  `TopicRevision` int(11) NOT NULL,
  PRIMARY KEY (`TranslatedTopicID`),
  UNIQUE KEY `TopicRevision` (`TopicRevision`,`TopicID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TranslatedTopicData`
--

CREATE TABLE IF NOT EXISTS `TranslatedTopicData` (
  `TranslatedTopicDataID` int(11) NOT NULL AUTO_INCREMENT,
  `TranslatedXML` mediumtext,
  `TranslatedXMLErrors` text,
  `TranslatedXMLRendered` mediumtext,
  `TranslatedXMLRenderedUpdated` datetime DEFAULT NULL,
  `TranslationLocale` varchar(45) NOT NULL,
  `TranslationPercentage` int(11) NOT NULL,
  `TranslatedTopicID` int(11) NOT NULL,
  PRIMARY KEY (`TranslatedTopicDataID`),
  UNIQUE KEY `TranslatedTopicID` (`TranslatedTopicID`,`TranslationLocale`),
  KEY `FKBEAB41239248FD56` (`TranslatedTopicID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TranslatedTopicData_AUD`
--

CREATE TABLE IF NOT EXISTS `TranslatedTopicData_AUD` (
  `TranslatedTopicDataID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `TranslatedXML` mediumtext,
  `TranslatedXMLErrors` text,
  `TranslatedXMLRendered` mediumtext,
  `TranslatedXMLRenderedUpdated` datetime DEFAULT NULL,
  `TranslationLocale` varchar(45) DEFAULT NULL,
  `TranslationPercentage` int(11) DEFAULT NULL,
  `TranslatedTopicID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TranslatedTopicDataID`,`REV`),
  KEY `FK73C2574A7C21108` (`REVEND`),
  KEY `FK73C2574DF74E053` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TranslatedTopicString`
--

CREATE TABLE IF NOT EXISTS `TranslatedTopicString` (
  `TranslatedTopicStringID` int(11) NOT NULL AUTO_INCREMENT,
  `OriginalString` text,
  `TranslatedString` text,
  `TranslatedTopicDataID` int(11) NOT NULL,
  `FuzzyTranslation` bit(1) NOT NULL,
  PRIMARY KEY (`TranslatedTopicStringID`),
  UNIQUE KEY `TranslatedTopicStringID` (`TranslatedTopicStringID`),
  KEY `FKDB83374A17E278CA` (`TranslatedTopicDataID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `TranslatedTopicString_AUD`
--

CREATE TABLE IF NOT EXISTS `TranslatedTopicString_AUD` (
  `TranslatedTopicStringID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `OriginalString` text,
  `TranslatedString` text,
  `TranslatedTopicDataID` int(11) DEFAULT NULL,
  `FuzzyTranslation` bit(1) DEFAULT NULL,
  PRIMARY KEY (`TranslatedTopicStringID`,`REV`),
  KEY `FK6D4EB01BA7C21108` (`REVEND`),
  KEY `FK6D4EB01BDF74E053` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TranslatedTopic_AUD`
--

CREATE TABLE IF NOT EXISTS `TranslatedTopic_AUD` (
  `TranslatedTopicID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `TopicID` int(11) DEFAULT NULL,
  `TopicRevision` int(11) DEFAULT NULL,
  PRIMARY KEY (`TranslatedTopicID`,`REV`),
  KEY `FKBEB70B2AA7C21108` (`REVEND`),
  KEY `FKBEB70B2ADF74E053` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `User`
--

CREATE TABLE IF NOT EXISTS `User` (
  `UserID` int(11) NOT NULL AUTO_INCREMENT,
  `UserName` varchar(255) NOT NULL,
  `Description` text,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `index2` (`UserName`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `UserRole`
--

CREATE TABLE IF NOT EXISTS `UserRole` (
  `UserRoleID` int(11) NOT NULL AUTO_INCREMENT,
  `UserNameID` int(11) NOT NULL,
  `RoleNameID` int(11) NOT NULL,
  PRIMARY KEY (`UserRoleID`),
  UNIQUE KEY `UserRoleUnique` (`UserNameID`,`RoleNameID`),
  KEY `fk_UserRole_1` (`RoleNameID`),
  KEY `fk_UserRole_2` (`UserNameID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `UserRole_AUD`
--

CREATE TABLE IF NOT EXISTS `UserRole_AUD` (
  `UserRoleID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `RoleNameID` int(11) DEFAULT NULL,
  `UserNameID` int(11) DEFAULT NULL,
  PRIMARY KEY (`UserRoleID`,`REV`),
  KEY `FKCC262C52DF74E053` (`REV`),
  KEY `FKCC262C52A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `User_AUD`
--

CREATE TABLE IF NOT EXISTS `User_AUD` (
  `UserID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `Description` text,
  `UserName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UserID`,`REV`),
  KEY `FKF3FCA03CDF74E053` (`REV`),
  KEY `FKF3FCA03CA7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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