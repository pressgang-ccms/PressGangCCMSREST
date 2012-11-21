-- phpMyAdmin SQL Dump
-- version 3.5.3
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 08, 2012 at 01:13 PM
-- Server version: 5.5.28
-- PHP Version: 5.3.16

SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `PressGangCCMS`
--
CREATE DATABASE /*!32312 IF NOT EXISTS*/ `PressGangCCMS` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE PressGangCCMS;

-- --------------------------------------------------------

--
-- Table structure for table `BlobConstants`
--

CREATE TABLE IF NOT EXISTS `BlobConstants` (
  `BlobConstantsID` int(11) NOT NULL AUTO_INCREMENT,
  `ConstantName` varchar(45) NOT NULL,
  `ConstantValue` longblob,
  PRIMARY KEY (`BlobConstantsID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='This table holds blob constants for things like image files ' AUTO_INCREMENT=10 ;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

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
  KEY `fk_Category_1` (`CategoryID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Categories contain tags. The relationship between a tag and ';

--
-- Dumping data for table `Category`
--

INSERT INTO `Category` (`CategoryID`, `CategoryName`, `CategoryDescription`, `CategorySort`, `MutuallyExclusive`) VALUES
(1, 'Audiences', '', 15, 0),
(2, 'Concerns', '', 25, 0),
(3, 'Technologies', 'Typically Technology maps a component of EAP to an Engineering lead who is the SME for that component. It is also used to categorise information for end-users.', 20, 0),
(4, 'Topic Types', '', 10, 1),
(5, 'Topic Lifecycle', '', 5, 0),
(6, 'Book', '', 50, 0),
(7, 'Engineering Priority', 'This is the priority set by the Engineering SME, usually the Engineering lead. ', 40, 1),
(8, 'PM Priority', 'This is the priority set by the Product Manager ', 45, 1),
(9, 'IA Priority', 'The Information Architect priority level.', 30, 1),
(10, 'SME Priority', 'The Subject Matter Expert priority level.', 35, 1),
(11, 'Writer Priority', 'The Writer priority level.', NULL, 1),
(12, 'Assigned Writer', '', NULL, 0),
(13, 'SME Reviewers', 'This is the name and email of the SME who will review the final output of this topic.', NULL, 0),
(14, 'Nav Priority', 'The highest level of priority given to a topic, which will affect its visibility on the "top 5" list in the nav pages.', 50, 1),
(15, 'Release', 'Used for tagging topics into a particular documentation release', 60, 0),
(16, 'Special Tags', 'This category holds tags that have special meanings to SkyNet', 70, 0),
(17, 'Common Names', '', 19, 0),
(19, 'Information Sensitivity', '', NULL, 0),
(20, 'Needs Info', 'We need further information from SMEs to flesh this out or to check for accuracy.', 80, 0),
(21, 'Provenance', 'Tags in this category capture "where something came from" - such as a URL to a source book.', NULL, 0),
(22, 'Programming Language', 'Tags in this category help users to filter information based on the language that they program in. ', NULL, 0),
(23, 'Content Type', 'Type of content of the topic - text, audio, video, etc...', NULL, 0),
(24, 'SEO Metadata', 'Tags that are for discoverability on the web, but have no semantic meaning for processing', NULL, 0),
(25, 'User Task Analysis Nodes', 'Used by RHEV', NULL, 0),
(26, 'Migration Workflow', '', NULL, 0),
(27, 'Model', 'Used to disambiguate terms that belong to different models, as well as to construct a model by tagging terms into it.', NULL, 0),
(28, 'Technical Notes Role', 'Structural role in Technical Notes assembly. Refer to: https://bugzilla.redhat.com/page.cgi?id=fields.html#cf_release_notes', NULL, 0),
(29, 'Bugzilla', 'This category represents bugzilla. The tags in this category represent specific bugs.\r\n\r\n\r\nTopics that represent some aspect of a bug -- such as Cause, Consequence, Fix and Workaround elements of a technical note entry -- declare their identity using tags in this category.', NULL, 0),
(30, 'Content Warnings', 'A collection of tags that can be used to indicate the presence of potential issues such as spelling errors and repeated words.', NULL, 0),
(33, 'Operating System', '', NULL, 0),
(34, 'Application', 'Applications for CloudForms', NULL, 0),
(35, 'Topic Transition', '', NULL, 0),
(36, 'Product', 'The specific product for which the topics and content maps are targeted.', NULL, 0),
(37, 'OS Project', 'Identifies the open source project to which this topic is related.', NULL, 0);

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

--
-- Dumping data for table `Category_AUD`
--

INSERT INTO `Category_AUD` (`CategoryID`, `REV`, `REVEND`, `REVTYPE`, `CategoryDescription`, `CategoryName`, `CategorySort`, `MutuallyExclusive`) VALUES
(1, 1, NULL, 0, '', 'Audiences', 15, b'0'),
(2, 1, NULL, 0, '', 'Concerns', 25, b'0'),
(3, 1, NULL, 0, 'Typically Technology maps a component of EAP to an Engineering lead who is the SME for that component. It is also used to categorise information for end-users.', 'Technologies', 20, b'0'),
(4, 1, NULL, 0, '', 'Topic Types', 10, b'1'),
(5, 1, NULL, 0, '', 'Topic Lifecycle', 5, b'0'),
(6, 1, NULL, 0, '', 'Book', 50, b'0'),
(7, 1, NULL, 0, 'This is the priority set by the Engineering SME, usually the Engineering lead. ', 'Engineering Priority', 40, b'1'),
(8, 1, NULL, 0, 'This is the priority set by the Product Manager ', 'PM Priority', 45, b'1'),
(9, 1, NULL, 0, 'The Information Architect priority level.', 'IA Priority', 30, b'1'),
(10, 1, NULL, 0, 'The Subject Matter Expert priority level.', 'SME Priority', 35, b'1'),
(11, 1, NULL, 0, 'The Writer priority level.', 'Writer Priority', NULL, b'1'),
(12, 1, NULL, 0, '', 'Assigned Writer', NULL, b'0'),
(13, 1, NULL, 0, 'This is the name and email of the SME who will review the final output of this topic.', 'SME Reviewers', NULL, b'0'),
(14, 1, NULL, 0, 'The highest level of priority given to a topic, which will affect its visibility on the "top 5" list in the nav pages.', 'Nav Priority', 50, b'1'),
(15, 1, NULL, 0, 'Used for tagging topics into a particular documentation release', 'Release', 60, b'0'),
(16, 1, NULL, 0, 'This category holds tags that have special meanings to SkyNet', 'Special Tags', 70, b'0'),
(17, 1, NULL, 0, '', 'Common Names', 19, b'0'),
(19, 1, NULL, 0, '', 'Information Sensitivity', NULL, b'0'),
(20, 1, NULL, 0, 'We need further information from SMEs to flesh this out or to check for accuracy.', 'Needs Info', 80, b'0'),
(21, 1, NULL, 0, 'Tags in this category capture "where something came from" - such as a URL to a source book.', 'Provenance', NULL, b'0'),
(22, 1, NULL, 0, 'Tags in this category help users to filter information based on the language that they program in. ', 'Programming Language', NULL, b'0'),
(23, 1, NULL, 0, 'Type of content of the topic - text, audio, video, etc...', 'Content Type', NULL, b'0'),
(24, 1, NULL, 0, 'Tags that are for discoverability on the web, but have no semantic meaning for processing', 'SEO Metadata', NULL, b'0'),
(25, 1, NULL, 0, 'Used by RHEV', 'User Task Analysis Nodes', NULL, b'0'),
(26, 1, NULL, 0, '', 'Migration Workflow', NULL, b'0'),
(27, 1, NULL, 0, 'Used to disambiguate terms that belong to different models, as well as to construct a model by tagging terms into it.', 'Model', NULL, b'0'),
(28, 1, NULL, 0, 'Structural role in Technical Notes assembly. Refer to: https://bugzilla.redhat.com/page.cgi?id=fields.html#cf_release_notes', 'Technical Notes Role', NULL, b'0'),
(29, 1, NULL, 0, 'This category represents bugzilla. The tags in this category represent specific bugs.\r\n\r\n\r\nTopics that represent some aspect of a bug -- such as Cause, Consequence, Fix and Workaround elements of a technical note entry -- declare their identity using tags in this category.', 'Bugzilla', NULL, b'0'),
(30, 1, NULL, 0, 'A collection of tags that can be used to indicate the presence of potential issues such as spelling errors and repeated words.', 'Content Warnings', NULL, b'0'),
(33, 1, NULL, 0, '', 'Operating System', NULL, b'0'),
(34, 1, NULL, 0, 'Applications for CloudForms', 'Application', NULL, b'0'),
(35, 1, NULL, 0, '', 'Topic Transition', NULL, b'0'),
(36, 1, NULL, 0, 'The specific product for which the topics and content maps are targeted.', 'Product', NULL, b'0'),
(37, 1, NULL, 0, 'Identifies the open source project to which this topic is related.', 'OS Project', NULL, b'0');

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpec`
--

CREATE TABLE IF NOT EXISTS `ContentSpec` (
  `ContentSpecID` int(11) NOT NULL AUTO_INCREMENT,
  `ContentSpecTitle` varchar(255) NOT NULL,
  `ContentSpecType` int(11) NOT NULL,
  `Locale` varchar(512) NOT NULL,
  `lastPublished` datetime DEFAULT NULL,
  PRIMARY KEY (`ContentSpecID`),
  UNIQUE KEY `ContentSpecID` (`ContentSpecID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecMetaData`
--

CREATE TABLE IF NOT EXISTS `ContentSpecMetaData` (
  `ContentSpecMetaDataID` int(11) NOT NULL AUTO_INCREMENT,
  `ContentSpecMetaDataTitle` varchar(255) NOT NULL,
  `ContentSpecMetaDataDescription` text,
  PRIMARY KEY (`ContentSpecMetaDataID`),
  UNIQUE KEY `ContentSpecMetaDataID` (`ContentSpecMetaDataID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecMetaData_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecMetaData_AUD` (
  `ContentSpecMetaDataID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `ContentSpecMetaDataTitle` varchar(255) DEFAULT NULL,
  `ContentSpecMetaDataDescription` text,
  PRIMARY KEY (`ContentSpecMetaDataID`,`REV`),
  KEY `FK6DBBA77416D0EC8F` (`REVEND`),
  KEY `FK6DBBA7744E83BBDA` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecNode`
--

CREATE TABLE IF NOT EXISTS `ContentSpecNode` (
  `ContentSpecNodeID` int(11) NOT NULL AUTO_INCREMENT,
  `NodeTitle` varchar(255) NOT NULL,
  `NodeType` int(11) NOT NULL,
  `NodeCondition` varchar(255) DEFAULT NULL,
  `Flag` int(11) DEFAULT NULL,
  `TopicID` int(11) DEFAULT NULL,
  `TopicRevision` int(11) DEFAULT NULL,
  `ContentSpecID` int(11) DEFAULT NULL,
  `NextNodeID` int(11) DEFAULT NULL,
  `ParentID` int(11) DEFAULT NULL,
  `PreviousNodeID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecNodeID`),
  UNIQUE KEY `ContentSpecNodeID` (`ContentSpecNodeID`),
  UNIQUE KEY `ContentSpecID` (`ContentSpecID`,`PreviousNodeID`),
  UNIQUE KEY `ContentSpecID_2` (`ContentSpecID`,`NextNodeID`),
  KEY `FKD5DB8BD6ED197BA6` (`ContentSpecID`),
  KEY `FKD5DB8BD6367D70E3` (`PreviousNodeID`),
  KEY `FKD5DB8BD6EBE853DF` (`NextNodeID`),
  KEY `FKD5DB8BD64F855BD4` (`ParentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecNodeToContentSpecNode`
--

CREATE TABLE IF NOT EXISTS `ContentSpecNodeToContentSpecNode` (
  `ContentSpecNodeToContentSpecNodeID` int(11) NOT NULL AUTO_INCREMENT,
  `RelationshipType` int(11) NOT NULL,
  `MainNodeID` int(11) NOT NULL,
  `RelatedNodeID` int(11) NOT NULL,
  PRIMARY KEY (`ContentSpecNodeToContentSpecNodeID`),
  UNIQUE KEY `ContentSpecNodeToContentSpecNodeID` (`ContentSpecNodeToContentSpecNodeID`),
  KEY `FKDF1672C540009B17` (`RelatedNodeID`),
  KEY `FKDF1672C552F62225` (`MainNodeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecNodeToContentSpecNode_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecNodeToContentSpecNode_AUD` (
  `ContentSpecNodeToContentSpecNodeID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `RelationshipType` int(11) DEFAULT NULL,
  `MainNodeID` int(11) DEFAULT NULL,
  `RelatedNodeID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecNodeToContentSpecNodeID`,`REV`),
  KEY `FKB591B61616D0EC8F` (`REVEND`),
  KEY `FKB591B6164E83BBDA` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecNodeToPropertyTag`
--

CREATE TABLE IF NOT EXISTS `ContentSpecNodeToPropertyTag` (
  `ContentSpecNodeToPropertyTagID` int(11) NOT NULL AUTO_INCREMENT,
  `Value` text,
  `ContentSpecNodeID` int(11) NOT NULL,
  `PropertyTagID` int(11) NOT NULL,
  PRIMARY KEY (`ContentSpecNodeToPropertyTagID`),
  UNIQUE KEY `ContentSpecNodeToPropertyTagID` (`ContentSpecNodeToPropertyTagID`),
  KEY `FK4F3E7BD4ABA6BBE0` (`ContentSpecNodeID`),
  KEY `FK4F3E7BD49612C5C2` (`PropertyTagID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecNodeToPropertyTag_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecNodeToPropertyTag_AUD` (
  `ContentSpecNodeToPropertyTagID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `Value` text,
  `ContentSpecNodeID` int(11) DEFAULT NULL,
  `PropertyTagID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecNodeToPropertyTagID`,`REV`),
  KEY `FK51609FA5D25E3A3B` (`REVEND`),
  KEY `FK51609FA5A110986` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecNode_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecNode_AUD` (
  `ContentSpecNodeID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `NodeTitle` varchar(255) DEFAULT NULL,
  `NodeType` int(11) DEFAULT NULL,
  `NodeCondition` varchar(255) DEFAULT NULL,
  `Flag` int(11) DEFAULT NULL,
  `TopicID` int(11) DEFAULT NULL,
  `TopicRevision` int(11) DEFAULT NULL,
  `ContentSpecID` int(11) DEFAULT NULL,
  `NextNodeID` int(11) DEFAULT NULL,
  `ParentID` int(11) DEFAULT NULL,
  `PreviousNodeID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecNodeID`,`REV`),
  KEY `FK2311DEA7D25E3A3B` (`REVEND`),
  KEY `FK2311DEA7A110986` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToContentSpecMetaData`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToContentSpecMetaData` (
  `ContentSpecToContentSpecMetaDataID` int(11) NOT NULL AUTO_INCREMENT,
  `Value` text,
  `ContentSpecMetaDataID` int(11) NOT NULL,
  `ContentSpecID` int(11) NOT NULL,
  PRIMARY KEY (`ContentSpecToContentSpecMetaDataID`),
  UNIQUE KEY `ContentSpecToContentSpecMetaDataID` (`ContentSpecToContentSpecMetaDataID`),
  KEY `FKEFF3F974CA8809D2` (`ContentSpecID`),
  KEY `FKEFF3F9747E34A6EE` (`ContentSpecMetaDataID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToContentSpecMetaData_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToContentSpecMetaData_AUD` (
  `ContentSpecToContentSpecMetaDataID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `Value` text,
  `ContentSpecMetaDataID` int(11) DEFAULT NULL,
  `ContentSpecID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecToContentSpecMetaDataID`,`REV`),
  KEY `FK79DE4D4516D0EC8F` (`REVEND`),
  KEY `FK79DE4D454E83BBDA` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToProject`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToProject` (
  `ContentSpecToProjectID` int(11) NOT NULL AUTO_INCREMENT,
  `ContentSpecID` int(11) NOT NULL,
  `ProjectID` int(11) NOT NULL,
  PRIMARY KEY (`ContentSpecToProjectID`),
  UNIQUE KEY `ContentSpecToProjectID` (`ContentSpecToProjectID`),
  KEY `FKADEB4ACACA8809D2` (`ContentSpecID`),
  KEY `FKADEB4ACAEB3A6876` (`ProjectID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToProject_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToProject_AUD` (
  `ContentSpecToProjectID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `ContentSpecID` int(11) DEFAULT NULL,
  `ProjectID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecToProjectID`,`REV`),
  KEY `FKDE81039B16D0EC8F` (`REVEND`),
  KEY `FKDE81039B4E83BBDA` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToPropertyTag`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToPropertyTag` (
  `ContentSpecToPropertyTagID` int(11) NOT NULL AUTO_INCREMENT,
  `Value` text,
  `ContentSpecID` int(11) NOT NULL,
  `PropertyTagID` int(11) NOT NULL,
  PRIMARY KEY (`ContentSpecToPropertyTagID`),
  UNIQUE KEY `ContentSpecToPropertyTagID` (`ContentSpecToPropertyTagID`),
  KEY `FK676CDCB6CA8809D2` (`ContentSpecID`),
  KEY `FK676CDCB6F3A435EE` (`PropertyTagID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToPropertyTag_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToPropertyTag_AUD` (
  `ContentSpecToPropertyTagID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `Value` text,
  `ContentSpecID` int(11) DEFAULT NULL,
  `PropertyTagID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecToPropertyTagID`,`REV`),
  KEY `FKF7CFBF8716D0EC8F` (`REVEND`),
  KEY `FKF7CFBF874E83BBDA` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToTag`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToTag` (
  `ContentSpecToTagID` int(11) NOT NULL AUTO_INCREMENT,
  `ContentSpecID` int(11) NOT NULL,
  `TagID` int(11) NOT NULL,
  PRIMARY KEY (`ContentSpecToTagID`),
  UNIQUE KEY `ContentSpecToTagID` (`ContentSpecToTagID`),
  UNIQUE KEY `ContentSpecID` (`ContentSpecID`,`TagID`),
  KEY `FKE5EA3FCBED197BA6` (`ContentSpecID`),
  KEY `FKE5EA3FCB9BADF58C` (`TagID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecToTag_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecToTag_AUD` (
  `ContentSpecToTagID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `ContentSpecID` int(11) DEFAULT NULL,
  `TagID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecToTagID`,`REV`),
  KEY `FK640B901CD25E3A3B` (`REVEND`),
  KEY `FK640B901CA110986` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecTranslatedString`
--

CREATE TABLE IF NOT EXISTS `ContentSpecTranslatedString` (
  `ContentSpecTranslatedStringID` int(11) NOT NULL AUTO_INCREMENT,
  `Locale` varchar(255) NOT NULL,
  PRIMARY KEY (`ContentSpecTranslatedStringID`),
  UNIQUE KEY `ContentSpecTranslatedStringID` (`ContentSpecTranslatedStringID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpecTranslatedString_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpecTranslatedString_AUD` (
  `ContentSpecTranslatedStringID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `Locale` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecTranslatedStringID`,`REV`),
  KEY `FKF97154ACD25E3A3B` (`REVEND`),
  KEY `FKF97154ACA110986` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ContentSpec_AUD`
--

CREATE TABLE IF NOT EXISTS `ContentSpec_AUD` (
  `ContentSpecID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `ContentSpecTitle` varchar(255) DEFAULT NULL,
  `ContentSpecType` int(11) DEFAULT NULL,
  `Locale` varchar(255) DEFAULT NULL,
  `lastPublished` datetime DEFAULT NULL,
  PRIMARY KEY (`ContentSpecID`,`REV`),
  KEY `FKD5E2978516D0EC8F` (`REVEND`),
  KEY `FKD5E297854E83BBDA` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `CSMetaDataToCSTranslatedString`
--

CREATE TABLE IF NOT EXISTS `CSMetaDataToCSTranslatedString` (
  `ContentSpecMetaDataID` int(11) NOT NULL AUTO_INCREMENT,
  `ContentSpecTranslatedStringID` int(11) NOT NULL,
  `ContentSpecToCSMetaDataID` int(11) NOT NULL,
  PRIMARY KEY (`ContentSpecMetaDataID`),
  UNIQUE KEY `ContentSpecMetaDataID` (`ContentSpecMetaDataID`),
  UNIQUE KEY `ContentSpecMetaDataID_2` (`ContentSpecMetaDataID`,`ContentSpecTranslatedStringID`),
  KEY `FK70BF79518DF9AE0A` (`ContentSpecTranslatedStringID`),
  KEY `FK70BF7951CCD88DBA` (`ContentSpecToCSMetaDataID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `CSMetaDataToCSTranslatedString_AUD`
--

CREATE TABLE IF NOT EXISTS `CSMetaDataToCSTranslatedString_AUD` (
  `ContentSpecMetaDataID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `ContentSpecTranslatedStringID` int(11) DEFAULT NULL,
  `ContentSpecToCSMetaDataID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ContentSpecMetaDataID`,`REV`),
  KEY `FKA6FC96A2D25E3A3B` (`REVEND`),
  KEY `FKA6FC96A2A110986` (`REV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `CSNodeToCSTranslatedString`
--

CREATE TABLE IF NOT EXISTS `CSNodeToCSTranslatedString` (
  `TopicToTagID` int(11) NOT NULL AUTO_INCREMENT,
  `ContentSpecNodeID` int(11) NOT NULL,
  `ContentSpecTranslatedStringID` int(11) NOT NULL,
  PRIMARY KEY (`TopicToTagID`),
  UNIQUE KEY `TopicToTagID` (`TopicToTagID`),
  UNIQUE KEY `ContentSpecNodeID` (`ContentSpecNodeID`,`ContentSpecTranslatedStringID`),
  KEY `FKEE5DA884ABA6BBE0` (`ContentSpecNodeID`),
  KEY `FKEE5DA8848DF9AE0A` (`ContentSpecTranslatedStringID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `CSNodeToCSTranslatedString_AUD`
--

CREATE TABLE IF NOT EXISTS `CSNodeToCSTranslatedString_AUD` (
  `TopicToTagID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `ContentSpecNodeID` int(11) DEFAULT NULL,
  `ContentSpecTranslatedStringID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TopicToTagID`,`REV`),
  KEY `FKBEB9F455D25E3A3B` (`REVEND`),
  KEY `FKBEB9F455A110986` (`REV`)
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
-- Table structure for table `ImageFile`
--

CREATE TABLE IF NOT EXISTS `ImageFile` (
  `ImageFileID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` text,
  PRIMARY KEY (`ImageFileID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `IntegerConstants`
--

INSERT INTO `IntegerConstants` (`IntegerConstantsID`, `ConstantName`, `ConstantValue`) VALUES
(1, 'InvisibleTagID', 124);

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

--
-- Dumping data for table `IntegerConstants_AUD`
--

INSERT INTO `IntegerConstants_AUD` (`IntegerConstantsID`, `REV`, `REVEND`, `REVTYPE`, `ConstantName`, `ConstantValue`) VALUES
(1, 1, NULL, 0, 'InvisibleTagID', 124);

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
  KEY `FK15D2ACC3E0AD6B52` (`ImageFileID`),
  KEY `FK15D2ACC34F65E026` (`ImageFileID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

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
  `PropertyTagIsUnqiue` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`PropertyTagID`),
  UNIQUE KEY `PropertyTagUnique` (`PropertyTagName`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `PropertyTag`
--

INSERT INTO `PropertyTag` (`PropertyTagID`, `PropertyTagName`, `PropertyTagDescription`, `PropertyTagRegex`, `PropertyTagCanBeNull`, `PropertyTagIsUnqiue`) VALUES
(1, 'First Name', '', '^[a-zA-Z][a-zA-Z\\-'' ]*[a-zA-Z ]$', b'0', b'0'),
(2, 'Last Name', '', '^[a-zA-Z][a-zA-Z\\-'' ]*[a-zA-Z ]$', b'0', b'0'),
(3, 'Email Address', '', '^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$', b'0', b'0'),
(4, 'Kerberos ID', '', '^[a-z0-9]+$', b'0', b'0'),
(5, 'Bugzilla ID', '', '^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$', b'0', b'0'),
(6, 'JIRA ID', '', '^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$', b'0', b'0'),
(14, 'Added By', '', '.*', b'0', b'0'),
(15, 'CSP-ID', 'An ID assigned to topics created with the Content Spec tool', '.*', b'0', b'1'),
(16, 'DTD', '', '.*', b'0', b'0'),
(17, 'Content Specification Type', '', '^(Pre|Post) Processed$', b'0', b'0'),
(18, 'Organization', '', '.*', b'0', b'0'),
(19, 'Organization Division', '', '.*', b'0', b'0'),
(20, 'Fixed URL', 'This Property Tag defines the URL that a topic can be accessed by.\r\n\r\nIt can start with a letter, and then contain any combination of letters, numbers and underscore characters.', '^[a-zA-Z]+[a-zA-Z0-9_.-]*$', b'0', b'1'),
(21, 'Bugzilla Product', 'This tag is used when creating the "Report a Bug" link to prepopulate the product', '.*', b'0', b'0'),
(22, 'Bugzilla Version', 'This tag is used when creating the "Report a Bug" link to prepopulate the version', '.*', b'0', b'0'),
(23, 'Bugzilla Component', 'This tag is used when creating the "Report a Bug" link to prepopulate the component', '.*', b'0', b'0'),
(24, 'Bugzilla Keyword', 'This tag is used when creating the "Report a Bug" link to prepopulate the keywords', '.*', b'0', b'0'),
(25, 'CSP Read Only', 'If set this property will stop the CSP from updating the Content Specification unless their username is with in this property value, as comma separated values.', '.*', b'0', b'1'),
(26, 'Spelling Error Details', '', '.*', b'1', b'0'),
(27, 'Grammar Errors', '', '.*', b'1', b'0'),
(28, 'Original File Name', 'Used when importing topics into the system from existing files. This tag holds the original file name of the topic.', '', b'0', b'0'),
(29, 'Topic Import Errors, Warnings and Notes', '', '.*', b'0', b'0');

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `PropertyTagCategory`
--

INSERT INTO `PropertyTagCategory` (`PropertyTagCategoryID`, `PropertyTagCategoryName`, `PropertyTagCategoryDescription`) VALUES
(3, 'Person', 'Properties that relate to a specific person'),
(4, 'Topic', 'Properties that relate to topics'),
(5, 'Content Spec Processor', 'Tags related to the Content Spec Processor'),
(6, 'Compiler Properties', ''),
(7, 'Bugzilla Product Details', 'Properties that relate to the "Report Bug" link');

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

--
-- Dumping data for table `PropertyTagCategory_AUD`
--

INSERT INTO `PropertyTagCategory_AUD` (`PropertyTagCategoryID`, `REV`, `REVEND`, `REVTYPE`, `PropertyTagCategoryDescription`, `PropertyTagCategoryName`) VALUES
(3, 1, NULL, 0, 'Properties that relate to a specific person', 'Person'),
(4, 1, NULL, 0, 'Properties that relate to topics', 'Topic'),
(5, 1, NULL, 0, 'Tags related to the Content Spec Processor', 'Content Spec Processor'),
(6, 1, NULL, 0, '', 'Compiler Properties'),
(7, 1, NULL, 0, 'Properties that relate to the "Report Bug" link', 'Bugzilla Product Details');

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

--
-- Dumping data for table `PropertyTagToPropertyTagCategory`
--

INSERT INTO `PropertyTagToPropertyTagCategory` (`PropertyTagToPropertyTagCategoryID`, `PropertyTagID`, `PropertyTagCategoryID`, `Sorting`) VALUES
(5, 1, 3, NULL),
(6, 2, 3, NULL),
(7, 14, 4, NULL),
(8, 15, 5, 0),
(9, 16, 5, 0),
(10, 17, 5, 0),
(11, 18, 3, 0),
(12, 19, 3, 0),
(13, 20, 6, 0),
(14, 22, 7, NULL),
(15, 24, 7, NULL),
(16, 5, 7, NULL),
(17, 23, 7, NULL),
(18, 21, 7, NULL),
(19, 25, 5, 0);

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

--
-- Dumping data for table `PropertyTagToPropertyTagCategory_AUD`
--

INSERT INTO `PropertyTagToPropertyTagCategory_AUD` (`PropertyTagToPropertyTagCategoryID`, `REV`, `REVEND`, `REVTYPE`, `Sorting`, `PropertyTagID`, `PropertyTagCategoryID`) VALUES
(5, 1, NULL, 0, NULL, 1, 3),
(6, 1, NULL, 0, NULL, 2, 3),
(7, 1, NULL, 0, NULL, 14, 4),
(8, 1, NULL, 0, 0, 15, 5),
(9, 1, NULL, 0, 0, 16, 5),
(10, 1, NULL, 0, 0, 17, 5),
(11, 1, NULL, 0, 0, 18, 3),
(12, 1, NULL, 0, 0, 19, 3),
(13, 1, NULL, 0, 0, 20, 6),
(14, 1, NULL, 0, NULL, 22, 7),
(15, 1, NULL, 0, NULL, 24, 7),
(16, 1, NULL, 0, NULL, 5, 7),
(17, 1, NULL, 0, NULL, 23, 7),
(18, 1, NULL, 0, NULL, 21, 7),
(19, 1, NULL, 0, 0, 25, 5);

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
  `PropertyTagIsUnqiue` bit(1) DEFAULT NULL,
  PRIMARY KEY (`PropertyTagID`,`REV`),
  KEY `FK4825B8B6DF74E053` (`REV`),
  KEY `FK4825B8B6A7C21108` (`REVEND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `PropertyTag_AUD`
--

INSERT INTO `PropertyTag_AUD` (`PropertyTagID`, `REV`, `REVEND`, `REVTYPE`, `PropertyTagCanBeNull`, `PropertyTagDescription`, `PropertyTagName`, `PropertyTagRegex`, `PropertyTagIsUnqiue`) VALUES
(1, 1, NULL, 0, b'0', '', 'First Name', '^[a-zA-Z][a-zA-Z\\-'' ]*[a-zA-Z ]$', b'0'),
(2, 1, NULL, 0, b'0', '', 'Last Name', '^[a-zA-Z][a-zA-Z\\-'' ]*[a-zA-Z ]$', b'0'),
(3, 1, NULL, 0, b'0', '', 'Email Address', '^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$', b'0'),
(4, 1, NULL, 0, b'0', '', 'Kerberos ID', '^[a-z0-9]+$', b'0'),
(5, 1, NULL, 0, b'0', '', 'Bugzilla ID', '^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$', b'0'),
(6, 1, NULL, 0, b'0', '', 'JIRA ID', '^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$', b'0'),
(14, 1, NULL, 0, b'0', '', 'Added By', '.*', b'0'),
(15, 1, NULL, 0, b'0', 'An ID assigned to topics created with the Content Spec tool', 'CSP-ID', '.*', b'1'),
(16, 1, NULL, 0, b'0', '', 'DTD', '.*', b'0'),
(17, 1, NULL, 0, b'0', '', 'Content Specification Type', '^(Pre|Post) Processed$', b'0'),
(18, 1, NULL, 0, b'0', '', 'Organization', '.*', b'0'),
(19, 1, NULL, 0, b'0', '', 'Organization Division', '.*', b'0'),
(20, 1, NULL, 0, b'0', 'This Property Tag defines the URL that a topic can be accessed by.\r\n\r\nIt can start with a letter, and then contain any combination of letters, numbers and underscore characters.', 'Fixed URL', '^[a-zA-Z]+[a-zA-Z0-9_.-]*$', b'1'),
(21, 1, NULL, 0, b'0', 'This tag is used when creating the "Report a Bug" link to prepopulate the product', 'Bugzilla Product', '.*', b'0'),
(22, 1, NULL, 0, b'0', 'This tag is used when creating the "Report a Bug" link to prepopulate the version', 'Bugzilla Version', '.*', b'0'),
(23, 1, NULL, 0, b'0', 'This tag is used when creating the "Report a Bug" link to prepopulate the component', 'Bugzilla Component', '.*', b'0'),
(24, 1, NULL, 0, b'0', 'This tag is used when creating the "Report a Bug" link to prepopulate the keywords', 'Bugzilla Keyword', '.*', b'0'),
(25, 1, NULL, 0, b'0', 'If set this property will stop the CSP from updating the Content Specification unless their username is with in this property value, as comma separated values.', 'CSP Read Only', '.*', b'1'),
(26, 1, NULL, 0, b'1', '', 'Spelling Error Details', '.*', b'0'),
(27, 1, NULL, 0, b'1', '', 'Grammar Errors', '.*', b'0'),
(28, 1, NULL, 0, b'0', 'Used when importing topics into the system from existing files. This tag holds the original file name of the topic.', 'Original File Name', '', b'0'),
(29, 1, NULL, 0, b'0', '', 'Topic Import Errors, Warnings and Notes', '.*', b'0');

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `RelationshipTag`
--

INSERT INTO `RelationshipTag` (`RelationshipTagID`, `RelationshipTagName`, `RelationshipTagDescription`) VALUES
(1, 'Relates To', 'Generic Relationship'),
(2, 'Go Next To', NULL),
(3, 'Go Back To', NULL),
(4, 'Disambiguates', NULL),
(5, 'Obsoletes', NULL),
(6, 'Includes', NULL),
(7, 'Has Prerequisite Of', NULL),
(8, 'License', 'A relationship to a license agreemenet');

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

--
-- Dumping data for table `RelationshipTag_AUD`
--

INSERT INTO `RelationshipTag_AUD` (`RelationshipTagId`, `REV`, `REVEND`, `REVTYPE`, `RelationshipTagDescription`, `RelationshipTagName`) VALUES
(1, 1, NULL, 0, 'Generic Relationship', 'Relates To'),
(2, 1, NULL, 0, NULL, 'Go Next To'),
(3, 1, NULL, 0, NULL, 'Go Back To'),
(4, 1, NULL, 0, NULL, 'Disambiguates'),
(5, 1, NULL, 0, NULL, 'Obsoletes'),
(6, 1, NULL, 0, NULL, 'Includes'),
(7, 1, NULL, 0, NULL, 'Has Prerequisite Of'),
(8, 1, NULL, 0, 'A relationship to a license agreemenet', 'License');

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `REVINFO`
--

INSERT INTO `REVINFO` (`REV`, `REVTSTMP`, `Flag`, `Message`, `UserName`) VALUES
(1, 1307078254098, NULL, NULL, NULL);

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Role`
--

INSERT INTO `Role` (`RoleID`, `RoleName`, `Description`) VALUES
(1, 'adminRole', 'Administrator'),
(2, 'writerRole', 'Writer'),
(3, 'iaRole', 'Information Architect'),
(4, 'editorRole', 'Editor'),
(5, 'qeRole', 'Quality Engineering'),
(6, 'pmRole', 'Product Manager'),
(7, 'smeRole', 'Subject Matter Expert'),
(8, 'engRole', 'Engineering'),
(9, 'basicRole', 'The default role');

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

--
-- Dumping data for table `RoleToRole`
--

INSERT INTO `RoleToRole` (`RoleToRoleID`, `PrimaryRole`, `RelationshipType`, `SecondaryRole`) VALUES
(1, 1, 1, 9),
(2, 2, 1, 9),
(3, 3, 1, 9),
(4, 4, 1, 9),
(5, 5, 1, 9),
(6, 6, 1, 9),
(7, 7, 1, 9),
(8, 8, 1, 9);

-- --------------------------------------------------------

--
-- Table structure for table `RoleToRoleRelationship`
--

CREATE TABLE IF NOT EXISTS `RoleToRoleRelationship` (
  `RoleToRoleRelationshipID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` text,
  PRIMARY KEY (`RoleToRoleRelationshipID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `RoleToRoleRelationship`
--

INSERT INTO `RoleToRoleRelationship` (`RoleToRoleRelationshipID`, `Description`) VALUES
(1, 'Inherit');

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

--
-- Dumping data for table `RoleToRoleRelationship_AUD`
--

INSERT INTO `RoleToRoleRelationship_AUD` (`RoleToRoleRelationshipID`, `REV`, `REVEND`, `REVTYPE`, `Description`) VALUES
(1, 1, NULL, 0, 'Inherit');

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

--
-- Dumping data for table `RoleToRole_AUD`
--

INSERT INTO `RoleToRole_AUD` (`RoleToRoleID`, `REV`, `REVEND`, `REVTYPE`, `PrimaryRole`, `RelationshipType`, `SecondaryRole`) VALUES
(1, 1, NULL, 0, 1, 1, 9),
(2, 1, NULL, 0, 2, 1, 9),
(3, 1, NULL, 0, 3, 1, 9),
(4, 1, NULL, 0, 4, 1, 9),
(5, 1, NULL, 0, 5, 1, 9),
(6, 1, NULL, 0, 6, 1, 9),
(7, 1, NULL, 0, 7, 1, 9),
(8, 1, NULL, 0, 8, 1, 9);

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

--
-- Dumping data for table `Role_AUD`
--

INSERT INTO `Role_AUD` (`RoleID`, `REV`, `REVEND`, `REVTYPE`, `Description`, `RoleName`) VALUES
(1, 1, NULL, 0, 'Administrator', 'adminRole'),
(2, 1, NULL, 0, 'Writer', 'writerRole'),
(3, 1, NULL, 0, 'Information Architect', 'iaRole'),
(4, 1, NULL, 0, 'Editor', 'editorRole'),
(5, 1, NULL, 0, 'Quality Engineering', 'qeRole'),
(6, 1, NULL, 0, 'Product Manager', 'pmRole'),
(7, 1, NULL, 0, 'Subject Matter Expert', 'smeRole'),
(8, 1, NULL, 0, 'Engineering', 'engRole'),
(9, 1, NULL, 0, 'The default role', 'basicRole');

-- --------------------------------------------------------

--
-- Table structure for table `StringConstants`
--

CREATE TABLE IF NOT EXISTS `StringConstants` (
  `StringConstantsID` int(11) NOT NULL AUTO_INCREMENT,
  `ConstantName` varchar(45) NOT NULL,
  `ConstantValue` mediumtext,
  PRIMARY KEY (`StringConstantsID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='This table holds the string constants used by Skynet, includ';

--
-- Dumping data for table `StringConstants`
--

INSERT INTO `StringConstants` (`StringConstantsID`, `ConstantName`, `ConstantValue`) VALUES
(1, 'en-US/Book.xml', '<?xml version=''1.0'' encoding=''utf-8'' ?>\r\n<!DOCTYPE book PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN" "http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd" [\r\n<!ENTITY % BOOK_ENTITIES SYSTEM "<<contentSpec.escapedTitle>>.ent">\r\n%BOOK_ENTITIES;\r\n]>\r\n<book <<contentSpec.draft>>>\r\n	<xi:include href="Book_Info.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n	<!-- Inject Preface -->\r\n	<!-- Inject XIIncludes -->\r\n    <!-- Inject Revision History -->\r\n</book>'),
(2, 'en-US/Book.ent', '<!ENTITY PRODUCT "<<contentSpec.product>>">\r\n<!ENTITY BOOKID "<<contentSpec.escapedTitle>>">\r\n<!ENTITY YEAR "YYYY">\r\n<!ENTITY TITLE "<<contentSpec.title>>">\r\n<!ENTITY HOLDER "<<contentSpec.copyrightHolder>>">\r\n<!ENTITY BZURL "<<contentSpec.bugzillaUrl>>">\r\n<!ENTITY BZCOMPONENT "<<contentSpec.bzcomponent>>">\r\n<!ENTITY BZPRODUCT "<<contentSpec.bzproduct>>">'),
(3, 'en-US/Book_Info.xml', '<?xml version=''1.0'' encoding=''utf-8'' ?>\r\n<!DOCTYPE bookinfo PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN"\r\n"http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd" [\r\n<!ENTITY % BOOK_ENTITIES SYSTEM "<<contentSpec.escapedTitle>>.ent">\r\n%BOOK_ENTITIES;\r\n]>\r\n<bookinfo id="book-<<contentSpec.escapedTitle>>-<<contentSpec.escapedTitle>>">\r\n	<title><<contentSpec.title>></title>\r\n	<subtitle><<contentSpec.subtitle>></subtitle>\r\n	<productname><<contentSpec.product>></productname>\r\n	<productnumber><<contentSpec.version>></productnumber>\r\n	<edition><<contentSpec.edition>></edition>\r\n	<!-- Inject Abstract -->\r\n	<corpauthor>\r\n		<inlinemediaobject>\r\n	 	<imageobject>\r\n	     	<imagedata fileref="Common_Content/images/title_logo.svg" format="SVG" />\r\n	     </imageobject>\r\n	    </inlinemediaobject>\r\n	</corpauthor>\r\n	<!-- Inject Legal Notice -->\r\n	<xi:include href="Author_Group.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n</bookinfo>'),
(4, 'en-US/Author_Group.xml', '<authorgroup>\r\n	<author>\r\n 	<firstname>SkyNet</firstname>\r\n	<surname>Alpha Build System</surname>\r\n       	<affiliation>\r\n        	<orgname>Engineering Content Services</orgname>\r\n           	<orgdiv>Red Hat</orgdiv>\r\n       	</affiliation>\r\n    </author>\r\n</authorgroup>'),
(5, 'publican.cfg', 'xml_lang: en-US\r\ntype: Book\r\nbrand: <<contentSpec.brand>>\r\nchunk_first: 0\r\ntoc_section_depth: 4\r\nmax_image_width: 600\r\ngit_branch: docs-rhel-6'),
(6, 'en-US/images/icon.svg', '<?xml version="1.0" encoding="UTF-8" standalone="no"?>\n<svg xmlns:svg="http://www.w3.org/2000/svg" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.0" width="32" height="32" id="svg3017">\n  <defs id="defs3019">\n    <linearGradient id="linearGradient2381">\n      <stop id="stop2383" style="stop-color:#ffffff;stop-opacity:1" offset="0"/>\n      <stop id="stop2385" style="stop-color:#ffffff;stop-opacity:0" offset="1"/>\n    </linearGradient>\n    <linearGradient x1="296.4996" y1="188.81061" x2="317.32471" y2="209.69398" id="linearGradient2371" xlink:href="#linearGradient2381" gradientUnits="userSpaceOnUse" gradientTransform="matrix(0.90776,0,0,0.90776,24.35648,49.24131)"/>\n  </defs>\n  <g transform="matrix(0.437808,-0.437808,0.437808,0.437808,-220.8237,43.55311)" id="g5089">\n    <path d="m 8.4382985,-6.28125 c -0.6073916,0 -4.3132985,5.94886271 -4.3132985,8.25 l 0,26.71875 c 0,0.846384 0.5818159,1.125 1.15625,1.125 l 25.5625,0 c 0.632342,0 1.125001,-0.492658 1.125,-1.125 l 0,-5.21875 0.28125,0 c 0.49684,0 0.906249,-0.409411 0.90625,-0.90625 l 0,-27.9375 c 0,-0.4968398 -0.40941,-0.90625 -0.90625,-0.90625 l -23.8117015,0 z" transform="translate(282.8327,227.1903)" id="path5091" style="fill:#5c5c4f;stroke:#000000;stroke-width:3.23021388;stroke-miterlimit:4;stroke-dasharray:none"/>\n    <rect width="27.85074" height="29.369793" rx="1.1414107" ry="1.1414107" x="286.96509" y="227.63805" id="rect5093" style="fill:#032c87"/>\n    <path d="m 288.43262,225.43675 25.2418,0 0,29.3698 -26.37615,0.0241 1.13435,-29.39394 z" id="rect5095" style="fill:#ffffff"/>\n    <path d="m 302.44536,251.73726 c 1.38691,7.85917 -0.69311,11.28365 -0.69311,11.28365 2.24384,-1.60762 3.96426,-3.47694 4.90522,-5.736 0.96708,2.19264 1.83294,4.42866 4.27443,5.98941 0,0 -1.59504,-7.2004 -1.71143,-11.53706 l -6.77511,0 z" id="path5097" style="fill:#a70000;fill-opacity:1;stroke-width:2"/>\n    <rect width="25.241802" height="29.736675" rx="0.89682275" ry="0.89682275" x="290.73544" y="220.92249" id="rect5099" style="fill:#809cc9"/>\n    <path d="m 576.47347,725.93939 6.37084,0.41502 0.4069,29.51809 c -1.89202,-1.31785 -6.85427,-3.7608 -8.26232,-1.68101 l 0,-26.76752 c 0,-0.82246 0.66212,-1.48458 1.48458,-1.48458 z" transform="matrix(0.499065,-0.866565,0,1,0,0)" id="rect5101" style="fill:#4573b3;fill-opacity:1"/>\n    <path d="m 293.2599,221.89363 20.73918,0 c 0.45101,0 0.8141,0.3631 0.8141,0.81411 0.21547,6.32836 -19.36824,21.7635 -22.36739,17.59717 l 0,-17.59717 c 0,-0.45101 0.3631,-0.81411 0.81411,-0.81411 z" id="path5103" style="opacity:0.65536726;fill:url(#linearGradient2371);fill-opacity:1"/>\n  </g>\n</svg>'),
(10, 'Task Overview Topic Template', '<section> \n	<!-- INJECT DETAILS HERE -->\n	\n	<!-- Topic type: Task Overview --> 			 \n	<!-- See https://bugzilla.redhat.com/show_bug.cgi?id=700661 for implementation status --> \n	<!-- Characteristics of a Task Overview: --> \n	<!-- 1. It has single information role: "Present the sequence". --> \n	<!-- 2. It focuses on one subject: the relationship between the several tasks in a process. --> \n	<!-- 3. It provides a sequential overview of the tasks in a process --> \n	\n	<!-- INJECT TITLE HERE -->\n	\n	<!-- Injection Point: Related Overviews --> \n	<formalpara> \n	   <title>Introduction</title>    \n	   <para> \n	      Introductory text goes here. \n	   </para> \n	</formalpara> \n	<para>Introductory text can continue here.</para> \n	\n	<!-- To inject an itemized list of references to tasks, use the InjectList comment, supplying a comma-separated list of topicIDs. --> \n	<!-- *Only topicIDs in this list that are "related" to this topic in Skynet will be injected*. --> \n	<!-- Skynet will inject an itemizedlist of xrefs --> \n	<!-- InjectList: 44, 56, 678 -->\n	<!-- Skynet will inject an itemizedlist of xrefs, alphabetically sorted by topic title --> \n	<!-- InjectListAlphaSort: 1, 2, 675, 8631 -->\n	<!-- To inject an ordered list of references to tasks, use the InjectSequence comment, supplying a comma-separated list of topicIDs. --> \n	<!-- *Only topicIDs in this list that are "related" to this topic in Skynet will be injected*. --> \n	<!-- Skynet will inject an orderedlist of xrefs --> \n	<!-- InjectSequence: 44, 56, 678 --> 			  \n	<!-- To inject a single topic, use the Inject comment, with a single topicID --> \n	<!-- A reference to the topic will only be injected if the topic is "related" to this topic in Skynet --> \n	<!-- Inject: 56 --> \n	\n	<!-- Do not edit below this line! -->\n	\n	<!-- Injection Point: Related Overviews -->\n	<!-- Injection Point: Related Concepts -->\n	<!-- Injection Point: Related Tasks -->\n	\n	<!-- INJECT RELATIONS HERE -->\n</section>'),
(11, 'Conceptual Overview Topic Template', '<section>\r\n	<!-- Topic type: Conceptual Overview -->\r\n	\r\n	<!-- Characteristics of a Overview: -->\r\n	<!-- 1. It has single information role: \\"Give context\\". -->\r\n	<!-- 2. It focuses on one subject: the relationship between several related concepts. -->\r\n	<!-- 3. It provides the wider context for a two or more single, focused concepts. -->\r\n	<!-- 4. It is indicated when concepts have bi-directional references (Concept A needs to refer to Concept B to clearly explain itself, and vice versa). -->\r\n	\r\n	<!-- INJECT TITLE HERE -->\r\n	\r\n	<formalpara>\r\n	  <title>Introduction</title>   \r\n	  <para>\r\n		 Introductory text goes here.\r\n	  </para>\r\n	</formalpara>\r\n	\r\n	<para>Introductory text can continue here.</para>\r\n	\r\n	<formalpara>\r\n	  <title>Section</title>\r\n	  <para>Use formalparas to section your content.</para>\r\n	</formalpara>\r\n</section>'),
(12, 'Reference Topic Template', '<section>\r\n	<!-- INJECT TITLE HERE -->\r\n	\r\n	<!-- A Reference provides additional information. Typically this is a table or variable list. -->\r\n	\r\n	<!-- Use the appropriate template and remove the other -->\r\n\r\n	<table id="table-">\r\n		<title>** table title **</title>\r\n		<tgroup align="left" cols="2" colsep="1" rowsep="1">\r\n			<colspec colname="c1" colwidth="1*"/>\r\n			<colspec colname="c2" colwidth="1*"/>\r\n			<thead>\r\n				<row>\r\n					<entry>** column 1 header **</entry>\r\n					<entry>** column 2 header **</entry>\r\n				</row>\r\n			</thead>\r\n			<tbody>\r\n				<row>\r\n					<entry>** column 1 content **</entry>\r\n					<entry>** column 2 content **</entry>\r\n				</row>\r\n			</tbody>\r\n		</tgroup>\r\n	</table>\r\n\r\n	<variablelist>\r\n		<varlistentry>\r\n			<term>** item name **</term>\r\n			<listitem>\r\n				<para>** item description **</para>\r\n			</listitem>\r\n		</varlistentry>\r\n	</variablelist>\r\n	\r\n</section>'),
(13, 'Task Topic Template', '<section>\r\n	<!-- INJECT TITLE HERE -->\r\n\r\n	<!-- Uncomment prerequisites and result if required. -->\r\n	<!-- Replace all text marked with ** and leave other text -->\r\n\r\n	<!--\r\n     	<itemizedlist>\r\n        	<title>Prerequisites</title>\r\n        	** inject list items here **\r\n     	</itemizedlist>\r\n	-->\r\n\r\n	<procedure>\r\n        	<title>Task</title>   <!-- "Task" title only required to visually separate task from prerequisites -->\r\n		<step>\r\n           		<title>** step title **</title>\r\n           		<para>** step instructions **</para>\r\n        	</step>\r\n        	<step>\r\n           		<title>** step title **</title>\r\n           		<para>** explain sub steps **</para>\r\n           		<!-- or use <stepalternatives> for one step or the other -->\r\n           		<substeps>\r\n              			<step>\r\n                 			<para>** step instructions **</para>\r\n              			</step>\r\n              			<step>\r\n                 			<para>** step instructions **</para>\r\n              			</step>\r\n           		</substeps>\r\n        	</step>\r\n	</procedure>\r\n\r\n	<!--\r\n	<formalpara>\r\n        	<title>Result</title>\r\n        	<para>** describe resulting state **</para>\r\n	</formalpara>\r\n	-->\r\n</section>   '),
(14, 'Concept Topic Template', '<section>\r\n	<!-- Topic type: Concept -->\r\n	\r\n	<!-- The 4 Characteristics of a Concept: -->\r\n	<!-- 1. It has single information role: "illuminate". -->\r\n	<!-- 2. It treats one, and only one subject. -->\r\n	<!-- 3. It builds a bridge from the known to the unknown. -->\r\n	<!-- 4. It reduces uncertainty. -->\r\n	\r\n	<!-- INJECT TITLE HERE -->\r\n	\r\n	<para>\r\n		The concept body goes here.\r\n	</para>\r\n</section>'),
(15, 'en-US/Revision_History.xml', '<appendix id="appe-test-Revision_History">\r\n	<title>Revision History</title>\r\n	<simpara>\r\n		<revhistory>\r\n		</revhistory>\r\n	</simpara>\r\n</appendix>'),
(20, 'en-US/images/jboss.svg', '<?xml version="1.0" encoding="UTF-8" standalone="no"?>\n<!-- Created with Inkscape (http://www.inkscape.org/) -->\n\n<svg\n   xmlns:svg="http://www.w3.org/2000/svg"\n   xmlns="http://www.w3.org/2000/svg"\n   version="1.0"\n   width="265"\n   height="150"\n   id="svg2898">\n  <defs\n     id="defs21" />\n  <g\n     transform="scale(1.2295957,1.2260211)"\n     id="g2622">\n    <g\n       id="g2624">\n      <path\n         d="m 140.253,110.221 2.945,5.891 -2.492,0 -2.863,-5.705 -3.255,0 0,5.705 -2.121,0 0,-14.419 6.323,0 c 2.514,0 4.635,1.339 4.635,4.306 0,2.306 -1.215,3.728 -3.172,4.222 z m -1.463,-6.489 -4.202,0 0,4.635 4.202,0 c 1.442,0 2.451,-0.741 2.451,-2.307 0,-1.504 -0.988,-2.328 -2.451,-2.328 z"\n         id="path2626"\n         style="fill:#cc0000" />\n      <path\n         d="m 155.164,111.458 -7.148,0 c 0.227,2.08 1.401,2.966 2.72,2.966 0.906,0 1.627,-0.329 2.348,-0.865 l 1.257,1.359 c -0.947,0.906 -2.08,1.421 -3.729,1.421 -2.533,0 -4.676,-2.039 -4.676,-5.623 0,-3.667 1.937,-5.645 4.737,-5.645 3.069,0 4.553,2.492 4.553,5.418 0,0.39 -0.041,0.742 -0.062,0.969 z m -4.635,-4.471 c -1.422,0 -2.287,0.988 -2.473,2.719 l 5.026,0 c -0.102,-1.483 -0.802,-2.719 -2.553,-2.719 z"\n         id="path2628"\n         style="fill:#cc0000" />\n      <path\n         d="m 164.37,116.112 0,-1.029 c -0.783,0.721 -1.689,1.256 -2.822,1.256 -2.328,0 -4.161,-1.688 -4.161,-5.809 0,-3.708 2.019,-5.459 4.264,-5.459 1.092,0 2.122,0.577 2.72,1.236 l 0,-4.12 2.101,-1.092 0,15.017 -2.102,0 z m 0.021,-7.662 c -0.474,-0.639 -1.463,-1.422 -2.534,-1.422 -1.524,0 -2.348,1.154 -2.348,3.44 0,2.719 0.865,3.913 2.431,3.913 1.009,0 1.895,-0.68 2.451,-1.379 l 0,-4.552 z"\n         id="path2630"\n         style="fill:#cc0000" />\n      <path\n         d="m 184.266,116.112 0,-6.468 -6.634,0 0,6.468 -2.162,0 0,-14.419 2.162,0 0,5.829 6.634,0 0,-5.829 2.162,0 0,14.419 -2.162,0 z"\n         id="path2632"\n         style="fill:#cc0000" />\n      <path\n         d="m 196.065,116.112 0,-1.07 c -0.741,0.741 -1.792,1.297 -2.966,1.297 -1.751,0 -3.749,-0.988 -3.749,-3.646 0,-2.41 1.854,-3.502 4.305,-3.502 1.01,0 1.813,0.144 2.41,0.412 l 0,-0.804 c 0,-1.174 -0.721,-1.833 -2.039,-1.833 -1.112,0 -1.978,0.206 -2.822,0.68 l -0.824,-1.606 c 1.03,-0.639 2.184,-0.969 3.708,-0.969 2.41,0 4.059,1.174 4.059,3.626 l 0,7.415 -2.082,0 0,0 z m 0,-4.613 c -0.576,-0.289 -1.318,-0.475 -2.472,-0.475 -1.359,0 -2.225,0.618 -2.225,1.607 0,1.07 0.68,1.792 2.08,1.792 1.134,0 2.122,-0.7 2.616,-1.38 l 0,-1.544 0.001,0 z"\n         id="path2634"\n         style="fill:#cc0000" />\n      <path\n         d="m 206.363,115.844 c -0.516,0.289 -1.236,0.494 -2.081,0.494 -1.504,0 -2.431,-0.926 -2.431,-2.863 l 0,-6.241 -1.545,0 0,-1.937 1.545,0 0,-3.09 2.081,-1.112 0,4.202 2.678,0 0,1.937 -2.678,0 0,5.871 c 0,1.009 0.329,1.298 1.112,1.298 0.556,0 1.174,-0.206 1.565,-0.433 l -0.246,1.874 z"\n         id="path2636"\n         style="fill:#cc0000" />\n    </g>\n    <g\n       id="g2638">\n      <path\n         d="m 106.389,51.025 c 3.57,-1.787 6,-5.101 6,-9.181 0,-9.509 -8.614,-11.555 -16.465,-11.421 l -21.186,0 -0.121,0 -11.746,0 0,30.362 c 0,4.409 -1.537,5.936 -4.274,5.936 -2.941,0 -4.595,-1.654 -4.595,-4.658 l 0,-4.205 -11.17,0 0,1.969 c 0,10.154 3.892,17.099 15.952,17.099 9.837,0 15.038,-4.356 15.833,-12.958 l 0,12.004 21.879,0 c 9.761,0 17.737,-3.315 17.737,-14.161 0,-5.165 -2.991,-9.376 -7.844,-10.786 z m -19.902,-11.42 9.181,0 c 2.493,0 4.852,1.092 4.852,4.405 0,3.253 -2.806,4.338 -4.852,4.338 l -9.181,0 0,-8.743 z m 9.502,26.864 -9.502,0 0,-10.469 9.502,0 c 3.576,0 6.384,1.345 6.384,5.355 0,3.77 -2.617,5.114 -6.384,5.114 z"\n         id="path2640"\n         style="fill:#cc0000" />\n      <path\n         d="m 90.067,108.399 c 0,-7.695 -6.245,-13.947 -13.944,-13.947 -7.714,0 -13.955,6.252 -13.955,13.947 0,7.709 6.241,13.948 13.955,13.948 7.699,0 13.944,-6.239 13.944,-13.948 z"\n         id="path2642"\n         style="fill:#cc0000" />\n      <path\n         d="m 53.012,103.999 c 0,-6.818 -5.533,-12.349 -12.357,-12.349 -6.823,0 -12.352,5.53 -12.352,12.349 0,6.824 5.528,12.357 12.352,12.357 6.824,0 12.357,-5.533 12.357,-12.357 z"\n         id="path2644"\n         style="fill:#cc0000" />\n      <path\n         d="m 25.097,81.68 c 0,-6.157 -4.984,-11.151 -11.15,-11.151 -6.168,0 -11.16,4.994 -11.16,11.151 0,6.174 4.992,11.168 11.16,11.168 6.165,0 11.15,-4.994 11.15,-11.168 z"\n         id="path2646"\n         style="fill:#cc0000" />\n      <path\n         d="m 19.918,50.615 c 0,-5.506 -4.455,-9.956 -9.955,-9.956 -5.499,0 -9.963,4.449 -9.963,9.956 0,5.5 4.464,9.964 9.963,9.964 5.5,0 9.955,-4.465 9.955,-9.964 z"\n         id="path2648"\n         style="fill:#cc0000" />\n      <path\n         d="m 33.88,22.719 c 0,-4.619 -3.756,-8.366 -8.372,-8.366 -4.619,0 -8.369,3.747 -8.369,8.366 0,4.623 3.75,8.367 8.369,8.367 4.616,0 8.372,-3.744 8.372,-8.367 z"\n         id="path2650"\n         style="fill:#cc0000" />\n      <path\n         d="m 57.78,10.364 c 0,-4.18 -3.385,-7.571 -7.566,-7.571 -4.18,0 -7.571,3.391 -7.571,7.571 0,4.187 3.392,7.578 7.571,7.578 4.182,0 7.566,-3.391 7.566,-7.578 z"\n         id="path2652"\n         style="fill:#cc0000" />\n      <path\n         d="M 82.891,6.377 C 82.891,2.855 80.042,0 76.517,0 73.001,0 70.14,2.855 70.14,6.377 c 0,3.526 2.861,6.38 6.377,6.38 3.525,0 6.374,-2.854 6.374,-6.38 z"\n         id="path2654"\n         style="fill:#cc0000" />\n    </g>\n    <g\n       id="g2656">\n      <g\n         id="g2658">\n        <path\n           d="m 161.415,62.895 c -5.338,-1.352 -11.706,-1.777 -14.243,-6.153 0.121,0.882 0.204,1.78 0.204,2.706 0,1.985 -0.299,3.867 -0.84,5.619 l 9.258,0 c 0,1.654 0.71,2.866 1.788,3.695 1.022,0.77 2.494,1.142 4.022,1.142 2.097,0 5.102,-0.884 5.102,-3.504 0,-2.545 -3.385,-3.064 -5.291,-3.505 z"\n           id="path2660"\n           style="fill:none" />\n        <path\n           d="m 129.896,50.193 c -5.045,0 -6.578,5.051 -6.578,9.255 0,4.217 1.533,9.187 6.578,9.187 5.039,0 6.633,-4.97 6.633,-9.187 -0.001,-4.204 -1.594,-9.255 -6.633,-9.255 z"\n           id="path2662"\n           style="fill:none" />\n        <path\n           d="M 192.015,62.895 C 185.337,61.204 176.724,60.97 176.338,52.616 l -9.62,0 c 0,-1.396 -0.512,-2.29 -1.396,-2.866 -0.903,-0.569 -2.107,-0.827 -3.447,-0.827 -1.781,0 -4.992,0.188 -4.992,2.487 0,3.132 7.273,3.705 12.247,4.787 6.649,1.335 8.399,6.118 8.423,8.869 l 8.842,0 c 0,1.654 0.71,2.866 1.788,3.695 1.023,0.77 2.494,1.142 4.021,1.142 2.097,0 5.103,-0.884 5.103,-3.504 -10e-4,-2.544 -3.385,-3.063 -5.292,-3.504 z"\n           id="path2664"\n           style="fill:none" />\n        <path\n           d="m 199.729,56.198 c -4.975,-1.082 -12.581,-1.654 -12.581,-4.787 0,-2.3 2.879,-2.487 4.66,-2.487 1.339,0 2.544,0.258 3.447,0.827 0.884,0.576 1.396,1.47 1.396,2.866 l 10.019,0 c -0.385,-8.606 -7.98,-10.714 -15.239,-10.714 -6.048,0 -13.905,1.882 -14.992,8.54 -1.485,-6.781 -8.344,-8.54 -14.943,-8.54 -6.51,0 -15.461,2.172 -15.461,10.146 0,0.141 0.021,0.261 0.024,0.398 -2.525,-6.296 -8.49,-10.544 -16.163,-10.544 -8.299,0 -14.504,4.847 -16.602,11.996 1.604,2.158 2.448,4.973 2.448,7.912 0,2.413 -0.384,4.549 -1.111,6.419 2.853,5.273 8.343,8.696 15.265,8.696 7.354,0 13.123,-3.872 15.815,-9.719 1.414,7.509 8.762,9.719 15.957,9.719 6.187,0 13.02,-2.027 15.179,-7.772 2.235,5.949 8.89,7.772 15.421,7.772 7.462,0 15.886,-2.931 15.886,-11.801 -0.001,-2.742 -1.731,-7.583 -8.425,-8.927 z m -69.833,12.437 c -5.045,0 -6.578,-4.97 -6.578,-9.187 0,-4.205 1.533,-9.255 6.578,-9.255 5.039,0 6.633,5.051 6.633,9.255 -0.001,4.218 -1.594,9.187 -6.633,9.187 z m 31.708,1.269 c -1.528,0 -3,-0.372 -4.022,-1.142 -1.078,-0.829 -1.788,-2.041 -1.788,-3.695 l -9.258,0 c 0.541,-1.752 0.84,-3.634 0.84,-5.619 0,-0.926 -0.083,-1.824 -0.204,-2.706 2.537,4.375 8.905,4.801 14.243,6.153 1.906,0.441 5.291,0.96 5.291,3.505 0,2.62 -3.005,3.504 -5.102,3.504 z m 30.599,0 c -1.527,0 -2.998,-0.372 -4.021,-1.142 -1.078,-0.829 -1.788,-2.041 -1.788,-3.695 l -8.842,0 c -0.023,-2.751 -1.773,-7.534 -8.423,-8.869 -4.974,-1.082 -12.247,-1.654 -12.247,-4.787 0,-2.3 3.211,-2.487 4.992,-2.487 1.34,0 2.544,0.258 3.447,0.827 0.885,0.576 1.396,1.47 1.396,2.866 l 9.62,0 c 0.386,8.354 8.999,8.587 15.677,10.279 1.907,0.441 5.291,0.96 5.291,3.505 0.001,2.619 -3.005,3.503 -5.102,3.503 z"\n           id="path2666"\n           style="fill:#60605b" />\n      </g>\n      <path\n         d="m 209.127,36.16 0.965,0 1.452,2.386 0.941,0 -1.571,-2.43 c 0.807,-0.102 1.42,-0.53 1.42,-1.509 0,-1.099 -0.638,-1.573 -1.938,-1.573 l -2.102,0 0,5.512 0.833,0 0,-2.386 z m 0,-0.714 0,-1.711 1.143,0 c 0.567,0 1.2,0.132 1.2,0.815 0,0.847 -0.633,0.896 -1.339,0.896 l -1.004,0 z"\n         id="path2668"\n         style="fill:#60605b" />\n      <path\n         d="m 215.518,35.8 c 0,2.98 -2.42,5.392 -5.399,5.392 -2.986,0 -5.406,-2.412 -5.406,-5.392 0,-2.987 2.42,-5.405 5.406,-5.405 2.979,0.001 5.399,2.418 5.399,5.405 z m -5.4,-4.444 c -2.464,0 -4.452,1.982 -4.452,4.444 0,2.451 1.988,4.432 4.452,4.432 2.45,0 4.438,-1.981 4.438,-4.432 10e-4,-2.462 -1.988,-4.444 -4.438,-4.444 z"\n         id="path2670"\n         style="fill:#60605b" />\n    </g>\n    <g\n       id="g2672">\n      <path\n         d="m 108.227,116.338 c -1.092,0 -2.122,-0.576 -2.719,-1.235 l 0,1.009 -2.102,0 0,-13.925 2.102,-1.092 0,5.232 c 0.782,-0.722 1.688,-1.257 2.822,-1.257 2.327,0 4.16,1.689 4.16,5.809 0,3.709 -2.018,5.459 -4.263,5.459 z m -0.289,-9.31 c -1.01,0 -1.896,0.68 -2.451,1.381 l 0,4.552 c 0.474,0.639 1.462,1.421 2.533,1.421 1.524,0 2.349,-1.152 2.349,-3.439 0,-2.72 -0.865,-3.915 -2.431,-3.915 z"\n         id="path2674"\n         style="fill:#60605b" />\n      <path\n         d="m 118.915,119.923 -2.245,0 1.565,-4.017 -3.976,-10.609 2.328,0 1.771,5.295 c 0.329,0.947 0.824,2.554 0.947,3.15 0.186,-0.638 0.639,-2.183 0.968,-3.109 l 1.834,-5.336 2.245,0 -5.437,14.626 z"\n         id="path2676"\n         style="fill:#60605b" />\n    </g>\n  </g>\n</svg>'),
(30, 'Docbook Elements To Ignore For Spelling', 'abbrev\r\naccel\r\nacronym\r\nclassname\r\ncode\r\ncommand\r\ncomputeroutput\r\nfilename\r\nforeignphrase\r\nguimenu\r\nhardware\r\ninterfacename\r\nkeycap\r\nliteral\r\nmenuchoice\r\noption\r\norgname\r\npackage\r\nparameter\r\nprogramlisting\r\nreplaceable\r\nscreen\r\nsgmltag\r\nsurname\r\nuserinput'),
(31, 'EmptyTopicError.xml', '<section>\r\n  <title><!-- Inject TopicTitle --></title>\r\n  <note>\r\n    <para>This topic has no XML content, and is included here as a placeholder.</para>\r\n  </note>\r\n</section>'),
(32, 'FailedInjectionTopic.xml', '<section>\r\n  <title><!-- Inject TopicTitle --></title>\r\n  <warning>\r\n    <title>Error</title>\r\n    <para>Topic <!-- Inject TopicID --> failed Injection processing and is not included in this build.</para>\r\n	<!-- Inject ErrorXREF -->\r\n  </warning>\r\n</section>'),
(33, 'FailedValidationTopic.xml', '<section>\r\n  <title><!-- Inject TopicTitle --></title>\r\n  <warning>\r\n    <title>Error</title>\r\n    <para>Topic <!-- Inject TopicID --> failed validation and is not included in this build.</para>\r\n    <!-- Inject ErrorXREF -->\r\n  </warning>\r\n</section>'),
(34, 'en-US/Preface.xml', '<?xml version="1.0" encoding="UTF-8"?>\r\n<!DOCTYPE preface PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN"\r\n"http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd" [\r\n<!ENTITY % BOOK_ENTITIES SYSTEM "<<contentSpec.escapedTitle>>.ent">\r\n%BOOK_ENTITIES;\r\n]>\r\n<preface id="pref-<<contentSpec.escapedTitle>>-Preface">\r\n	<title>Preface</title>\r\n	<xi:include href="Conventions.xml" xmlns:xi="http://www.w3.org/2001/XInclude">\r\n		<xi:fallback xmlns:xi="http://www.w3.org/2001/XInclude">\r\n			<xi:include href="Common_Content/Conventions.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n		</xi:fallback>\r\n	</xi:include>\r\n	<xi:include href="Feedback.xml" xmlns:xi="http://www.w3.org/2001/XInclude">\r\n		<xi:fallback xmlns:xi="http://www.w3.org/2001/XInclude">\r\n			<xi:include href="Common_Content/Feedback.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n		</xi:fallback>\r\n	</xi:include>\r\n</preface>'),
(35, 'en-US/Article_Info.xml', '<?xml version=''1.0'' encoding=''utf-8'' ?>\r\n<!DOCTYPE articleinfo PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN"\r\n"http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd" [\r\n<!ENTITY % BOOK_ENTITIES SYSTEM "<<contentSpec.escapedTitle>>.ent">\r\n%BOOK_ENTITIES;\r\n]>\r\n<articleinfo id="arti-<<contentSpec.escapedTitle>>-<<contentSpec.escapedTitle>>">\r\n	<title><<contentSpec.title>></title>\r\n	<subtitle><<contentSpec.subtitle>></subtitle>\r\n	<productname><<contentSpec.product>></productname>\r\n	<productnumber><<contentSpec.version>></productnumber>\r\n	<edition><<contentSpec.edition>></edition>\r\n	<!-- Inject Abstract -->\r\n	<corpauthor>\r\n		<inlinemediaobject>\r\n	 	<imageobject>\r\n	     	<imagedata fileref="Common_Content/images/title_logo.svg" format="SVG" />\r\n	     </imageobject>\r\n	    </inlinemediaobject>\r\n	</corpauthor>\r\n	<!-- Inject Legal Notice -->\r\n	<xi:include href="Author_Group.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n</articleinfo>'),
(36, 'en-US/Article.xml', '<?xml version=''1.0'' encoding=''utf-8'' ?>\r\n<!DOCTYPE book PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN" "http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd" [\r\n<!ENTITY % BOOK_ENTITIES SYSTEM "<<contentSpec.escapedTitle>>.ent">\r\n%BOOK_ENTITIES;\r\n]>\r\n<article  <<contentSpec.draft>>>\r\n	<xi:include href="Article_Info.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n	<!-- Inject XIIncludes -->\r\n    <!-- Inject Revision History -->\r\n</article>'),
(37, 'XML Element Properties', 'VERBATIM_XML_ELEMENTS=screen, programlisting, literallayout, synopsis, address\r\nINLINE_XML_ELEMENTS=code, prompt, command, firstterm, ulink, guilabel, filename, replaceable, parameter, literal, classname, sgmltag, guibutton, guimenuitem, guimenu, menuchoice, citetitle, systemitem, application, acronym, keycap, emphasis, package, quote, trademark, abbrev, phrase, anchor, citation, glossterm, link, xref, markup, tag, keycode, keycombo, accel, guisubmenu, keysym, shortcut, mousebutton, constant, errorcode, errorname, errortype, function, msgtext, property, returnvalue, symbol, token, varname, database, email, hardware, option, optional, type, methodname, interfacename, uri, productname, productversion, revnumber, date, computeroutput\r\nCONTENTS_INLINE_XML_ELEMENTS=title, term'),
(38, 'Locales', 'ja,\r\npt-BR,\r\nfr,\r\nes,\r\nzh-Hans,\r\nen-US,\r\nde');

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

--
-- Dumping data for table `StringConstants_AUD`
--

INSERT INTO `StringConstants_AUD` (`StringConstantsID`, `REV`, `REVEND`, `REVTYPE`, `ConstantName`, `ConstantValue`) VALUES
(1, 1, NULL, 0, 'en-US/Book.xml', '<?xml version=''1.0'' encoding=''utf-8'' ?>\r\n<!DOCTYPE book PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN" "http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd" [\r\n<!ENTITY % BOOK_ENTITIES SYSTEM "<<contentSpec.escapedTitle>>.ent">\r\n%BOOK_ENTITIES;\r\n]>\r\n<book <<contentSpec.draft>>>\r\n	<xi:include href="Book_Info.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n	<!-- Inject Preface -->\r\n	<!-- Inject XIIncludes -->\r\n    <!-- Inject Revision History -->\r\n</book>'),
(2, 1, NULL, 0, 'en-US/Book.ent', '<!ENTITY PRODUCT "<<contentSpec.product>>">\r\n<!ENTITY BOOKID "<<contentSpec.escapedTitle>>">\r\n<!ENTITY YEAR "YYYY">\r\n<!ENTITY TITLE "<<contentSpec.title>>">\r\n<!ENTITY HOLDER "<<contentSpec.copyrightHolder>>">\r\n<!ENTITY BZURL "<<contentSpec.bugzillaUrl>>">\r\n<!ENTITY BZCOMPONENT "<<contentSpec.bzcomponent>>">\r\n<!ENTITY BZPRODUCT "<<contentSpec.bzproduct>>">'),
(3, 1, NULL, 0, 'en-US/Book_Info.xml', '<?xml version=''1.0'' encoding=''utf-8'' ?>\r\n<!DOCTYPE bookinfo PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN"\r\n"http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd" [\r\n<!ENTITY % BOOK_ENTITIES SYSTEM "<<contentSpec.escapedTitle>>.ent">\r\n%BOOK_ENTITIES;\r\n]>\r\n<bookinfo id="book-<<contentSpec.escapedTitle>>-<<contentSpec.escapedTitle>>">\r\n	<title><<contentSpec.title>></title>\r\n	<subtitle><<contentSpec.subtitle>></subtitle>\r\n	<productname><<contentSpec.product>></productname>\r\n	<productnumber><<contentSpec.version>></productnumber>\r\n	<edition><<contentSpec.edition>></edition>\r\n	<!-- Inject Abstract -->\r\n	<corpauthor>\r\n		<inlinemediaobject>\r\n	 	<imageobject>\r\n	     	<imagedata fileref="Common_Content/images/title_logo.svg" format="SVG" />\r\n	     </imageobject>\r\n	    </inlinemediaobject>\r\n	</corpauthor>\r\n	<!-- Inject Legal Notice -->\r\n	<xi:include href="Author_Group.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n</bookinfo>'),
(4, 1, NULL, 0, 'en-US/Author_Group.xml', '<authorgroup>\r\n	<author>\r\n 	<firstname>SkyNet</firstname>\r\n	<surname>Alpha Build System</surname>\r\n       	<affiliation>\r\n        	<orgname>Engineering Content Services</orgname>\r\n           	<orgdiv>Red Hat</orgdiv>\r\n       	</affiliation>\r\n    </author>\r\n</authorgroup>'),
(5, 1, NULL, 0, 'publican.cfg', 'xml_lang: en-US\r\ntype: Book\r\nbrand: <<contentSpec.brand>>\r\nchunk_first: 0\r\ntoc_section_depth: 4\r\nmax_image_width: 600\r\ngit_branch: docs-rhel-6'),
(6, 1, NULL, 0, 'en-US/images/icon.svg', '<?xml version="1.0" encoding="UTF-8" standalone="no"?>\n<svg xmlns:svg="http://www.w3.org/2000/svg" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.0" width="32" height="32" id="svg3017">\n  <defs id="defs3019">\n    <linearGradient id="linearGradient2381">\n      <stop id="stop2383" style="stop-color:#ffffff;stop-opacity:1" offset="0"/>\n      <stop id="stop2385" style="stop-color:#ffffff;stop-opacity:0" offset="1"/>\n    </linearGradient>\n    <linearGradient x1="296.4996" y1="188.81061" x2="317.32471" y2="209.69398" id="linearGradient2371" xlink:href="#linearGradient2381" gradientUnits="userSpaceOnUse" gradientTransform="matrix(0.90776,0,0,0.90776,24.35648,49.24131)"/>\n  </defs>\n  <g transform="matrix(0.437808,-0.437808,0.437808,0.437808,-220.8237,43.55311)" id="g5089">\n    <path d="m 8.4382985,-6.28125 c -0.6073916,0 -4.3132985,5.94886271 -4.3132985,8.25 l 0,26.71875 c 0,0.846384 0.5818159,1.125 1.15625,1.125 l 25.5625,0 c 0.632342,0 1.125001,-0.492658 1.125,-1.125 l 0,-5.21875 0.28125,0 c 0.49684,0 0.906249,-0.409411 0.90625,-0.90625 l 0,-27.9375 c 0,-0.4968398 -0.40941,-0.90625 -0.90625,-0.90625 l -23.8117015,0 z" transform="translate(282.8327,227.1903)" id="path5091" style="fill:#5c5c4f;stroke:#000000;stroke-width:3.23021388;stroke-miterlimit:4;stroke-dasharray:none"/>\n    <rect width="27.85074" height="29.369793" rx="1.1414107" ry="1.1414107" x="286.96509" y="227.63805" id="rect5093" style="fill:#032c87"/>\n    <path d="m 288.43262,225.43675 25.2418,0 0,29.3698 -26.37615,0.0241 1.13435,-29.39394 z" id="rect5095" style="fill:#ffffff"/>\n    <path d="m 302.44536,251.73726 c 1.38691,7.85917 -0.69311,11.28365 -0.69311,11.28365 2.24384,-1.60762 3.96426,-3.47694 4.90522,-5.736 0.96708,2.19264 1.83294,4.42866 4.27443,5.98941 0,0 -1.59504,-7.2004 -1.71143,-11.53706 l -6.77511,0 z" id="path5097" style="fill:#a70000;fill-opacity:1;stroke-width:2"/>\n    <rect width="25.241802" height="29.736675" rx="0.89682275" ry="0.89682275" x="290.73544" y="220.92249" id="rect5099" style="fill:#809cc9"/>\n    <path d="m 576.47347,725.93939 6.37084,0.41502 0.4069,29.51809 c -1.89202,-1.31785 -6.85427,-3.7608 -8.26232,-1.68101 l 0,-26.76752 c 0,-0.82246 0.66212,-1.48458 1.48458,-1.48458 z" transform="matrix(0.499065,-0.866565,0,1,0,0)" id="rect5101" style="fill:#4573b3;fill-opacity:1"/>\n    <path d="m 293.2599,221.89363 20.73918,0 c 0.45101,0 0.8141,0.3631 0.8141,0.81411 0.21547,6.32836 -19.36824,21.7635 -22.36739,17.59717 l 0,-17.59717 c 0,-0.45101 0.3631,-0.81411 0.81411,-0.81411 z" id="path5103" style="opacity:0.65536726;fill:url(#linearGradient2371);fill-opacity:1"/>\n  </g>\n</svg>'),
(10, 1, NULL, 0, 'Task Overview Topic Template', '<section> \n	<!-- INJECT DETAILS HERE -->\n	\n	<!-- Topic type: Task Overview --> 			 \n	<!-- See https://bugzilla.redhat.com/show_bug.cgi?id=700661 for implementation status --> \n	<!-- Characteristics of a Task Overview: --> \n	<!-- 1. It has single information role: "Present the sequence". --> \n	<!-- 2. It focuses on one subject: the relationship between the several tasks in a process. --> \n	<!-- 3. It provides a sequential overview of the tasks in a process --> \n	\n	<!-- INJECT TITLE HERE -->\n	\n	<!-- Injection Point: Related Overviews --> \n	<formalpara> \n	   <title>Introduction</title>    \n	   <para> \n	      Introductory text goes here. \n	   </para> \n	</formalpara> \n	<para>Introductory text can continue here.</para> \n	\n	<!-- To inject an itemized list of references to tasks, use the InjectList comment, supplying a comma-separated list of topicIDs. --> \n	<!-- *Only topicIDs in this list that are "related" to this topic in Skynet will be injected*. --> \n	<!-- Skynet will inject an itemizedlist of xrefs --> \n	<!-- InjectList: 44, 56, 678 -->\n	<!-- Skynet will inject an itemizedlist of xrefs, alphabetically sorted by topic title --> \n	<!-- InjectListAlphaSort: 1, 2, 675, 8631 -->\n	<!-- To inject an ordered list of references to tasks, use the InjectSequence comment, supplying a comma-separated list of topicIDs. --> \n	<!-- *Only topicIDs in this list that are "related" to this topic in Skynet will be injected*. --> \n	<!-- Skynet will inject an orderedlist of xrefs --> \n	<!-- InjectSequence: 44, 56, 678 --> 			  \n	<!-- To inject a single topic, use the Inject comment, with a single topicID --> \n	<!-- A reference to the topic will only be injected if the topic is "related" to this topic in Skynet --> \n	<!-- Inject: 56 --> \n	\n	<!-- Do not edit below this line! -->\n	\n	<!-- Injection Point: Related Overviews -->\n	<!-- Injection Point: Related Concepts -->\n	<!-- Injection Point: Related Tasks -->\n	\n	<!-- INJECT RELATIONS HERE -->\n</section>'),
(11, 1, NULL, 0, 'Conceptual Overview Topic Template', '<section>\r\n	<!-- Topic type: Conceptual Overview -->\r\n	\r\n	<!-- Characteristics of a Overview: -->\r\n	<!-- 1. It has single information role: \\"Give context\\". -->\r\n	<!-- 2. It focuses on one subject: the relationship between several related concepts. -->\r\n	<!-- 3. It provides the wider context for a two or more single, focused concepts. -->\r\n	<!-- 4. It is indicated when concepts have bi-directional references (Concept A needs to refer to Concept B to clearly explain itself, and vice versa). -->\r\n	\r\n	<!-- INJECT TITLE HERE -->\r\n	\r\n	<formalpara>\r\n	  <title>Introduction</title>   \r\n	  <para>\r\n		 Introductory text goes here.\r\n	  </para>\r\n	</formalpara>\r\n	\r\n	<para>Introductory text can continue here.</para>\r\n	\r\n	<formalpara>\r\n	  <title>Section</title>\r\n	  <para>Use formalparas to section your content.</para>\r\n	</formalpara>\r\n</section>'),
(12, 1, NULL, 0, 'Reference Topic Template', '<section>\r\n	<!-- INJECT TITLE HERE -->\r\n	\r\n	<!-- A Reference provides additional information. Typically this is a table or variable list. -->\r\n	\r\n	<!-- Use the appropriate template and remove the other -->\r\n\r\n	<table id="table-">\r\n		<title>** table title **</title>\r\n		<tgroup align="left" cols="2" colsep="1" rowsep="1">\r\n			<colspec colname="c1" colwidth="1*"/>\r\n			<colspec colname="c2" colwidth="1*"/>\r\n			<thead>\r\n				<row>\r\n					<entry>** column 1 header **</entry>\r\n					<entry>** column 2 header **</entry>\r\n				</row>\r\n			</thead>\r\n			<tbody>\r\n				<row>\r\n					<entry>** column 1 content **</entry>\r\n					<entry>** column 2 content **</entry>\r\n				</row>\r\n			</tbody>\r\n		</tgroup>\r\n	</table>\r\n\r\n	<variablelist>\r\n		<varlistentry>\r\n			<term>** item name **</term>\r\n			<listitem>\r\n				<para>** item description **</para>\r\n			</listitem>\r\n		</varlistentry>\r\n	</variablelist>\r\n	\r\n</section>'),
(13, 1, NULL, 0, 'Task Topic Template', '<section>\r\n	<!-- INJECT TITLE HERE -->\r\n\r\n	<!-- Uncomment prerequisites and result if required. -->\r\n	<!-- Replace all text marked with ** and leave other text -->\r\n\r\n	<!--\r\n     	<itemizedlist>\r\n        	<title>Prerequisites</title>\r\n        	** inject list items here **\r\n     	</itemizedlist>\r\n	-->\r\n\r\n	<procedure>\r\n        	<title>Task</title>   <!-- "Task" title only required to visually separate task from prerequisites -->\r\n		<step>\r\n           		<title>** step title **</title>\r\n           		<para>** step instructions **</para>\r\n        	</step>\r\n        	<step>\r\n           		<title>** step title **</title>\r\n           		<para>** explain sub steps **</para>\r\n           		<!-- or use <stepalternatives> for one step or the other -->\r\n           		<substeps>\r\n              			<step>\r\n                 			<para>** step instructions **</para>\r\n              			</step>\r\n              			<step>\r\n                 			<para>** step instructions **</para>\r\n              			</step>\r\n           		</substeps>\r\n        	</step>\r\n	</procedure>\r\n\r\n	<!--\r\n	<formalpara>\r\n        	<title>Result</title>\r\n        	<para>** describe resulting state **</para>\r\n	</formalpara>\r\n	-->\r\n</section>   '),
(14, 1, NULL, 0, 'Concept Topic Template', '<section>\r\n	<!-- Topic type: Concept -->\r\n	\r\n	<!-- The 4 Characteristics of a Concept: -->\r\n	<!-- 1. It has single information role: "illuminate". -->\r\n	<!-- 2. It treats one, and only one subject. -->\r\n	<!-- 3. It builds a bridge from the known to the unknown. -->\r\n	<!-- 4. It reduces uncertainty. -->\r\n	\r\n	<!-- INJECT TITLE HERE -->\r\n	\r\n	<para>\r\n		The concept body goes here.\r\n	</para>\r\n</section>'),
(15, 1, NULL, 0, 'en-US/Revision_History.xml', '<appendix id="appe-test-Revision_History">\r\n	<title>Revision History</title>\r\n	<simpara>\r\n		<revhistory>\r\n		</revhistory>\r\n	</simpara>\r\n</appendix>'),
(20, 1, NULL, 0, 'en-US/images/jboss.svg', '<?xml version="1.0" encoding="UTF-8" standalone="no"?>\n<!-- Created with Inkscape (http://www.inkscape.org/) -->\n\n<svg\n   xmlns:svg="http://www.w3.org/2000/svg"\n   xmlns="http://www.w3.org/2000/svg"\n   version="1.0"\n   width="265"\n   height="150"\n   id="svg2898">\n  <defs\n     id="defs21" />\n  <g\n     transform="scale(1.2295957,1.2260211)"\n     id="g2622">\n    <g\n       id="g2624">\n      <path\n         d="m 140.253,110.221 2.945,5.891 -2.492,0 -2.863,-5.705 -3.255,0 0,5.705 -2.121,0 0,-14.419 6.323,0 c 2.514,0 4.635,1.339 4.635,4.306 0,2.306 -1.215,3.728 -3.172,4.222 z m -1.463,-6.489 -4.202,0 0,4.635 4.202,0 c 1.442,0 2.451,-0.741 2.451,-2.307 0,-1.504 -0.988,-2.328 -2.451,-2.328 z"\n         id="path2626"\n         style="fill:#cc0000" />\n      <path\n         d="m 155.164,111.458 -7.148,0 c 0.227,2.08 1.401,2.966 2.72,2.966 0.906,0 1.627,-0.329 2.348,-0.865 l 1.257,1.359 c -0.947,0.906 -2.08,1.421 -3.729,1.421 -2.533,0 -4.676,-2.039 -4.676,-5.623 0,-3.667 1.937,-5.645 4.737,-5.645 3.069,0 4.553,2.492 4.553,5.418 0,0.39 -0.041,0.742 -0.062,0.969 z m -4.635,-4.471 c -1.422,0 -2.287,0.988 -2.473,2.719 l 5.026,0 c -0.102,-1.483 -0.802,-2.719 -2.553,-2.719 z"\n         id="path2628"\n         style="fill:#cc0000" />\n      <path\n         d="m 164.37,116.112 0,-1.029 c -0.783,0.721 -1.689,1.256 -2.822,1.256 -2.328,0 -4.161,-1.688 -4.161,-5.809 0,-3.708 2.019,-5.459 4.264,-5.459 1.092,0 2.122,0.577 2.72,1.236 l 0,-4.12 2.101,-1.092 0,15.017 -2.102,0 z m 0.021,-7.662 c -0.474,-0.639 -1.463,-1.422 -2.534,-1.422 -1.524,0 -2.348,1.154 -2.348,3.44 0,2.719 0.865,3.913 2.431,3.913 1.009,0 1.895,-0.68 2.451,-1.379 l 0,-4.552 z"\n         id="path2630"\n         style="fill:#cc0000" />\n      <path\n         d="m 184.266,116.112 0,-6.468 -6.634,0 0,6.468 -2.162,0 0,-14.419 2.162,0 0,5.829 6.634,0 0,-5.829 2.162,0 0,14.419 -2.162,0 z"\n         id="path2632"\n         style="fill:#cc0000" />\n      <path\n         d="m 196.065,116.112 0,-1.07 c -0.741,0.741 -1.792,1.297 -2.966,1.297 -1.751,0 -3.749,-0.988 -3.749,-3.646 0,-2.41 1.854,-3.502 4.305,-3.502 1.01,0 1.813,0.144 2.41,0.412 l 0,-0.804 c 0,-1.174 -0.721,-1.833 -2.039,-1.833 -1.112,0 -1.978,0.206 -2.822,0.68 l -0.824,-1.606 c 1.03,-0.639 2.184,-0.969 3.708,-0.969 2.41,0 4.059,1.174 4.059,3.626 l 0,7.415 -2.082,0 0,0 z m 0,-4.613 c -0.576,-0.289 -1.318,-0.475 -2.472,-0.475 -1.359,0 -2.225,0.618 -2.225,1.607 0,1.07 0.68,1.792 2.08,1.792 1.134,0 2.122,-0.7 2.616,-1.38 l 0,-1.544 0.001,0 z"\n         id="path2634"\n         style="fill:#cc0000" />\n      <path\n         d="m 206.363,115.844 c -0.516,0.289 -1.236,0.494 -2.081,0.494 -1.504,0 -2.431,-0.926 -2.431,-2.863 l 0,-6.241 -1.545,0 0,-1.937 1.545,0 0,-3.09 2.081,-1.112 0,4.202 2.678,0 0,1.937 -2.678,0 0,5.871 c 0,1.009 0.329,1.298 1.112,1.298 0.556,0 1.174,-0.206 1.565,-0.433 l -0.246,1.874 z"\n         id="path2636"\n         style="fill:#cc0000" />\n    </g>\n    <g\n       id="g2638">\n      <path\n         d="m 106.389,51.025 c 3.57,-1.787 6,-5.101 6,-9.181 0,-9.509 -8.614,-11.555 -16.465,-11.421 l -21.186,0 -0.121,0 -11.746,0 0,30.362 c 0,4.409 -1.537,5.936 -4.274,5.936 -2.941,0 -4.595,-1.654 -4.595,-4.658 l 0,-4.205 -11.17,0 0,1.969 c 0,10.154 3.892,17.099 15.952,17.099 9.837,0 15.038,-4.356 15.833,-12.958 l 0,12.004 21.879,0 c 9.761,0 17.737,-3.315 17.737,-14.161 0,-5.165 -2.991,-9.376 -7.844,-10.786 z m -19.902,-11.42 9.181,0 c 2.493,0 4.852,1.092 4.852,4.405 0,3.253 -2.806,4.338 -4.852,4.338 l -9.181,0 0,-8.743 z m 9.502,26.864 -9.502,0 0,-10.469 9.502,0 c 3.576,0 6.384,1.345 6.384,5.355 0,3.77 -2.617,5.114 -6.384,5.114 z"\n         id="path2640"\n         style="fill:#cc0000" />\n      <path\n         d="m 90.067,108.399 c 0,-7.695 -6.245,-13.947 -13.944,-13.947 -7.714,0 -13.955,6.252 -13.955,13.947 0,7.709 6.241,13.948 13.955,13.948 7.699,0 13.944,-6.239 13.944,-13.948 z"\n         id="path2642"\n         style="fill:#cc0000" />\n      <path\n         d="m 53.012,103.999 c 0,-6.818 -5.533,-12.349 -12.357,-12.349 -6.823,0 -12.352,5.53 -12.352,12.349 0,6.824 5.528,12.357 12.352,12.357 6.824,0 12.357,-5.533 12.357,-12.357 z"\n         id="path2644"\n         style="fill:#cc0000" />\n      <path\n         d="m 25.097,81.68 c 0,-6.157 -4.984,-11.151 -11.15,-11.151 -6.168,0 -11.16,4.994 -11.16,11.151 0,6.174 4.992,11.168 11.16,11.168 6.165,0 11.15,-4.994 11.15,-11.168 z"\n         id="path2646"\n         style="fill:#cc0000" />\n      <path\n         d="m 19.918,50.615 c 0,-5.506 -4.455,-9.956 -9.955,-9.956 -5.499,0 -9.963,4.449 -9.963,9.956 0,5.5 4.464,9.964 9.963,9.964 5.5,0 9.955,-4.465 9.955,-9.964 z"\n         id="path2648"\n         style="fill:#cc0000" />\n      <path\n         d="m 33.88,22.719 c 0,-4.619 -3.756,-8.366 -8.372,-8.366 -4.619,0 -8.369,3.747 -8.369,8.366 0,4.623 3.75,8.367 8.369,8.367 4.616,0 8.372,-3.744 8.372,-8.367 z"\n         id="path2650"\n         style="fill:#cc0000" />\n      <path\n         d="m 57.78,10.364 c 0,-4.18 -3.385,-7.571 -7.566,-7.571 -4.18,0 -7.571,3.391 -7.571,7.571 0,4.187 3.392,7.578 7.571,7.578 4.182,0 7.566,-3.391 7.566,-7.578 z"\n         id="path2652"\n         style="fill:#cc0000" />\n      <path\n         d="M 82.891,6.377 C 82.891,2.855 80.042,0 76.517,0 73.001,0 70.14,2.855 70.14,6.377 c 0,3.526 2.861,6.38 6.377,6.38 3.525,0 6.374,-2.854 6.374,-6.38 z"\n         id="path2654"\n         style="fill:#cc0000" />\n    </g>\n    <g\n       id="g2656">\n      <g\n         id="g2658">\n        <path\n           d="m 161.415,62.895 c -5.338,-1.352 -11.706,-1.777 -14.243,-6.153 0.121,0.882 0.204,1.78 0.204,2.706 0,1.985 -0.299,3.867 -0.84,5.619 l 9.258,0 c 0,1.654 0.71,2.866 1.788,3.695 1.022,0.77 2.494,1.142 4.022,1.142 2.097,0 5.102,-0.884 5.102,-3.504 0,-2.545 -3.385,-3.064 -5.291,-3.505 z"\n           id="path2660"\n           style="fill:none" />\n        <path\n           d="m 129.896,50.193 c -5.045,0 -6.578,5.051 -6.578,9.255 0,4.217 1.533,9.187 6.578,9.187 5.039,0 6.633,-4.97 6.633,-9.187 -0.001,-4.204 -1.594,-9.255 -6.633,-9.255 z"\n           id="path2662"\n           style="fill:none" />\n        <path\n           d="M 192.015,62.895 C 185.337,61.204 176.724,60.97 176.338,52.616 l -9.62,0 c 0,-1.396 -0.512,-2.29 -1.396,-2.866 -0.903,-0.569 -2.107,-0.827 -3.447,-0.827 -1.781,0 -4.992,0.188 -4.992,2.487 0,3.132 7.273,3.705 12.247,4.787 6.649,1.335 8.399,6.118 8.423,8.869 l 8.842,0 c 0,1.654 0.71,2.866 1.788,3.695 1.023,0.77 2.494,1.142 4.021,1.142 2.097,0 5.103,-0.884 5.103,-3.504 -10e-4,-2.544 -3.385,-3.063 -5.292,-3.504 z"\n           id="path2664"\n           style="fill:none" />\n        <path\n           d="m 199.729,56.198 c -4.975,-1.082 -12.581,-1.654 -12.581,-4.787 0,-2.3 2.879,-2.487 4.66,-2.487 1.339,0 2.544,0.258 3.447,0.827 0.884,0.576 1.396,1.47 1.396,2.866 l 10.019,0 c -0.385,-8.606 -7.98,-10.714 -15.239,-10.714 -6.048,0 -13.905,1.882 -14.992,8.54 -1.485,-6.781 -8.344,-8.54 -14.943,-8.54 -6.51,0 -15.461,2.172 -15.461,10.146 0,0.141 0.021,0.261 0.024,0.398 -2.525,-6.296 -8.49,-10.544 -16.163,-10.544 -8.299,0 -14.504,4.847 -16.602,11.996 1.604,2.158 2.448,4.973 2.448,7.912 0,2.413 -0.384,4.549 -1.111,6.419 2.853,5.273 8.343,8.696 15.265,8.696 7.354,0 13.123,-3.872 15.815,-9.719 1.414,7.509 8.762,9.719 15.957,9.719 6.187,0 13.02,-2.027 15.179,-7.772 2.235,5.949 8.89,7.772 15.421,7.772 7.462,0 15.886,-2.931 15.886,-11.801 -0.001,-2.742 -1.731,-7.583 -8.425,-8.927 z m -69.833,12.437 c -5.045,0 -6.578,-4.97 -6.578,-9.187 0,-4.205 1.533,-9.255 6.578,-9.255 5.039,0 6.633,5.051 6.633,9.255 -0.001,4.218 -1.594,9.187 -6.633,9.187 z m 31.708,1.269 c -1.528,0 -3,-0.372 -4.022,-1.142 -1.078,-0.829 -1.788,-2.041 -1.788,-3.695 l -9.258,0 c 0.541,-1.752 0.84,-3.634 0.84,-5.619 0,-0.926 -0.083,-1.824 -0.204,-2.706 2.537,4.375 8.905,4.801 14.243,6.153 1.906,0.441 5.291,0.96 5.291,3.505 0,2.62 -3.005,3.504 -5.102,3.504 z m 30.599,0 c -1.527,0 -2.998,-0.372 -4.021,-1.142 -1.078,-0.829 -1.788,-2.041 -1.788,-3.695 l -8.842,0 c -0.023,-2.751 -1.773,-7.534 -8.423,-8.869 -4.974,-1.082 -12.247,-1.654 -12.247,-4.787 0,-2.3 3.211,-2.487 4.992,-2.487 1.34,0 2.544,0.258 3.447,0.827 0.885,0.576 1.396,1.47 1.396,2.866 l 9.62,0 c 0.386,8.354 8.999,8.587 15.677,10.279 1.907,0.441 5.291,0.96 5.291,3.505 0.001,2.619 -3.005,3.503 -5.102,3.503 z"\n           id="path2666"\n           style="fill:#60605b" />\n      </g>\n      <path\n         d="m 209.127,36.16 0.965,0 1.452,2.386 0.941,0 -1.571,-2.43 c 0.807,-0.102 1.42,-0.53 1.42,-1.509 0,-1.099 -0.638,-1.573 -1.938,-1.573 l -2.102,0 0,5.512 0.833,0 0,-2.386 z m 0,-0.714 0,-1.711 1.143,0 c 0.567,0 1.2,0.132 1.2,0.815 0,0.847 -0.633,0.896 -1.339,0.896 l -1.004,0 z"\n         id="path2668"\n         style="fill:#60605b" />\n      <path\n         d="m 215.518,35.8 c 0,2.98 -2.42,5.392 -5.399,5.392 -2.986,0 -5.406,-2.412 -5.406,-5.392 0,-2.987 2.42,-5.405 5.406,-5.405 2.979,0.001 5.399,2.418 5.399,5.405 z m -5.4,-4.444 c -2.464,0 -4.452,1.982 -4.452,4.444 0,2.451 1.988,4.432 4.452,4.432 2.45,0 4.438,-1.981 4.438,-4.432 10e-4,-2.462 -1.988,-4.444 -4.438,-4.444 z"\n         id="path2670"\n         style="fill:#60605b" />\n    </g>\n    <g\n       id="g2672">\n      <path\n         d="m 108.227,116.338 c -1.092,0 -2.122,-0.576 -2.719,-1.235 l 0,1.009 -2.102,0 0,-13.925 2.102,-1.092 0,5.232 c 0.782,-0.722 1.688,-1.257 2.822,-1.257 2.327,0 4.16,1.689 4.16,5.809 0,3.709 -2.018,5.459 -4.263,5.459 z m -0.289,-9.31 c -1.01,0 -1.896,0.68 -2.451,1.381 l 0,4.552 c 0.474,0.639 1.462,1.421 2.533,1.421 1.524,0 2.349,-1.152 2.349,-3.439 0,-2.72 -0.865,-3.915 -2.431,-3.915 z"\n         id="path2674"\n         style="fill:#60605b" />\n      <path\n         d="m 118.915,119.923 -2.245,0 1.565,-4.017 -3.976,-10.609 2.328,0 1.771,5.295 c 0.329,0.947 0.824,2.554 0.947,3.15 0.186,-0.638 0.639,-2.183 0.968,-3.109 l 1.834,-5.336 2.245,0 -5.437,14.626 z"\n         id="path2676"\n         style="fill:#60605b" />\n    </g>\n  </g>\n</svg>'),
(30, 1, NULL, 0, 'Docbook Elements To Ignore For Spelling', 'abbrev\r\naccel\r\nacronym\r\nclassname\r\ncode\r\ncommand\r\ncomputeroutput\r\nfilename\r\nforeignphrase\r\nguimenu\r\nhardware\r\ninterfacename\r\nkeycap\r\nliteral\r\nmenuchoice\r\noption\r\norgname\r\npackage\r\nparameter\r\nprogramlisting\r\nreplaceable\r\nscreen\r\nsgmltag\r\nsurname\r\nuserinput'),
(31, 1, NULL, 0, 'EmptyTopicError.xml', '<section>\r\n  <title><!-- Inject TopicTitle --></title>\r\n  <note>\r\n    <para>This topic has no XML content, and is included here as a placeholder.</para>\r\n  </note>\r\n</section>'),
(32, 1, NULL, 0, 'FailedInjectionTopic.xml', '<section>\r\n  <title><!-- Inject TopicTitle --></title>\r\n  <warning>\r\n    <title>Error</title>\r\n    <para>Topic <!-- Inject TopicID --> failed Injection processing and is not included in this build.</para>\r\n	<!-- Inject ErrorXREF -->\r\n  </warning>\r\n</section>'),
(33, 1, NULL, 0, 'FailedValidationTopic.xml', '<section>\r\n  <title><!-- Inject TopicTitle --></title>\r\n  <warning>\r\n    <title>Error</title>\r\n    <para>Topic <!-- Inject TopicID --> failed validation and is not included in this build.</para>\r\n    <!-- Inject ErrorXREF -->\r\n  </warning>\r\n</section>'),
(34, 1, NULL, 0, 'en-US/Preface.xml', '<?xml version="1.0" encoding="UTF-8"?>\r\n<!DOCTYPE preface PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN"\r\n"http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd" [\r\n<!ENTITY % BOOK_ENTITIES SYSTEM "<<contentSpec.escapedTitle>>.ent">\r\n%BOOK_ENTITIES;\r\n]>\r\n<preface id="pref-<<contentSpec.escapedTitle>>-Preface">\r\n	<title>Preface</title>\r\n	<xi:include href="Conventions.xml" xmlns:xi="http://www.w3.org/2001/XInclude">\r\n		<xi:fallback xmlns:xi="http://www.w3.org/2001/XInclude">\r\n			<xi:include href="Common_Content/Conventions.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n		</xi:fallback>\r\n	</xi:include>\r\n	<xi:include href="Feedback.xml" xmlns:xi="http://www.w3.org/2001/XInclude">\r\n		<xi:fallback xmlns:xi="http://www.w3.org/2001/XInclude">\r\n			<xi:include href="Common_Content/Feedback.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n		</xi:fallback>\r\n	</xi:include>\r\n</preface>'),
(35, 1, NULL, 0, 'en-US/Article_Info.xml', '<?xml version=''1.0'' encoding=''utf-8'' ?>\r\n<!DOCTYPE articleinfo PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN"\r\n"http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd" [\r\n<!ENTITY % BOOK_ENTITIES SYSTEM "<<contentSpec.escapedTitle>>.ent">\r\n%BOOK_ENTITIES;\r\n]>\r\n<articleinfo id="arti-<<contentSpec.escapedTitle>>-<<contentSpec.escapedTitle>>">\r\n	<title><<contentSpec.title>></title>\r\n	<subtitle><<contentSpec.subtitle>></subtitle>\r\n	<productname><<contentSpec.product>></productname>\r\n	<productnumber><<contentSpec.version>></productnumber>\r\n	<edition><<contentSpec.edition>></edition>\r\n	<!-- Inject Abstract -->\r\n	<corpauthor>\r\n		<inlinemediaobject>\r\n	 	<imageobject>\r\n	     	<imagedata fileref="Common_Content/images/title_logo.svg" format="SVG" />\r\n	     </imageobject>\r\n	    </inlinemediaobject>\r\n	</corpauthor>\r\n	<!-- Inject Legal Notice -->\r\n	<xi:include href="Author_Group.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n</articleinfo>'),
(36, 1, NULL, 0, 'en-US/Article.xml', '<?xml version=''1.0'' encoding=''utf-8'' ?>\r\n<!DOCTYPE book PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN" "http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd" [\r\n<!ENTITY % BOOK_ENTITIES SYSTEM "<<contentSpec.escapedTitle>>.ent">\r\n%BOOK_ENTITIES;\r\n]>\r\n<article  <<contentSpec.draft>>>\r\n	<xi:include href="Article_Info.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n	<!-- Inject XIIncludes -->\r\n    <!-- Inject Revision History -->\r\n</article>'),
(37, 1, NULL, 0, 'XML Element Properties', 'VERBATIM_XML_ELEMENTS=screen, programlisting, literallayout, synopsis, address\r\nINLINE_XML_ELEMENTS=code, prompt, command, firstterm, ulink, guilabel, filename, replaceable, parameter, literal, classname, sgmltag, guibutton, guimenuitem, guimenu, menuchoice, citetitle, systemitem, application, acronym, keycap, emphasis, package, quote, trademark, abbrev, phrase, anchor, citation, glossterm, link, xref, markup, tag, keycode, keycombo, accel, guisubmenu, keysym, shortcut, mousebutton, constant, errorcode, errorname, errortype, function, msgtext, property, returnvalue, symbol, token, varname, database, email, hardware, option, optional, type, methodname, interfacename, uri, productname, productversion, revnumber, date, computeroutput\r\nCONTENTS_INLINE_XML_ELEMENTS=title, term'),
(38, 1, NULL, 0, 'Locales', 'ja,\r\npt-BR,\r\nfr,\r\nes,\r\nzh-Hans,\r\nen-US,\r\nde');

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `Tag`
--

INSERT INTO `Tag` (`TagID`, `TagName`, `TagDescription`) VALUES
(4, 'Task', 'How to do something. Tasks should start with a verb, and be action-oriented.'),
(5, 'Concept', 'aka Definition'),
(6, 'Reference', 'Information that is not a definition and not action-oriented. Supplementary info.'),
(93, 'Overview', 'Conceptual Overviews describe the RELATIONSHIP between the components of a system.'),
(268, 'Content Specification', '');

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `TagToCategory`
--

INSERT INTO `TagToCategory` (`TagToCategoryID`, `TagID`, `CategoryID`, `Sorting`) VALUES
(4, 4, 4, 0),
(5, 5, 4, 0),
(6, 6, 4, 0),
(81, 93, 4, 0),
(323, 268, 4, 0);

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

--
-- Dumping data for table `TagToCategory_AUD`
--

INSERT INTO `TagToCategory_AUD` (`TagToCategoryID`, `REV`, `REVEND`, `REVTYPE`, `Sorting`, `CategoryID`, `TagID`) VALUES
(4, 1, NULL, 0, 0, 4, 4),
(5, 1, NULL, 0, 0, 4, 5),
(6, 1, NULL, 0, 0, 4, 6),
(81, 1, NULL, 0, 0, 4, 93),
(323, 1, NULL, 0, 0, 4, 268);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TagToTagRelationship`
--

CREATE TABLE IF NOT EXISTS `TagToTagRelationship` (
  `TagToTagRelationshipType` int(11) NOT NULL,
  `TagToTagRelationshipDescription` text,
  PRIMARY KEY (`TagToTagRelationshipType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `TagToTagRelationship`
--

INSERT INTO `TagToTagRelationship` (`TagToTagRelationshipType`, `TagToTagRelationshipDescription`) VALUES
(1, 'Primary Tag is the common name for the Secondary Tag');

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

--
-- Dumping data for table `TagToTagRelationship_AUD`
--

INSERT INTO `TagToTagRelationship_AUD` (`TagToTagRelationshipType`, `REV`, `REVEND`, `REVTYPE`, `TagToTagRelationshipDescription`) VALUES
(1, 1, NULL, 0, 'Primary Tag is the common name for the Secondary Tag');

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

--
-- Dumping data for table `Tag_AUD`
--

INSERT INTO `Tag_AUD` (`TagID`, `REV`, `REVEND`, `REVTYPE`, `TagDescription`, `TagName`) VALUES
(4, 1, NULL, 0, 'How to do something. Tasks should start with a verb, and be action-oriented.', 'Task'),
(5, 1, NULL, 0, 'aka Definition', 'Concept'),
(6, 1, NULL, 0, 'Information that is not a definition and not action-oriented. Supplementary info.', 'Reference'),
(93, 1, NULL, 0, 'Conceptual Overviews describe the RELATIONSHIP between the components of a system.', 'Overview'),
(268, 1, NULL, 0, '', 'Content Specification');

-- --------------------------------------------------------

--
-- Table structure for table `Topic`
--

CREATE TABLE IF NOT EXISTS `Topic` (
  `TopicID` int(11) NOT NULL AUTO_INCREMENT,
  `TopicTitle` varchar(255) DEFAULT NULL,
  `TopicText` text,
  `TopicTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `TopicXML` mediumtext,
  `TopicLocale` varchar(255) NOT NULL,
  PRIMARY KEY (`TopicID`),
  KEY `fk_Topic_1` (`TopicID`),
  KEY `FK4D3DD0FE2436E96` (`TopicID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `TopicSourceURL_AUD`
--

CREATE TABLE IF NOT EXISTS `TopicSourceURL_AUD` (
  `TopicSourceURLID` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVEND` int(11) DEFAULT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `TopicXML` text,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `TopicText` text,
  `TopicTimeStamp` datetime DEFAULT NULL,
  `TopicTitle` varchar(255) DEFAULT NULL,
  `TopicXML` mediumtext,
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
  UNIQUE KEY `TranslatedTopicID` (`TranslatedTopicID`),
  UNIQUE KEY `TopicRevision` (`TopicRevision`,`TopicID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

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
  UNIQUE KEY `TranslatedTopicDataID` (`TranslatedTopicDataID`),
  UNIQUE KEY `TranslatedTopicID` (`TranslatedTopicID`,`TranslationLocale`),
  KEY `FKBEAB41239248FD56` (`TranslatedTopicID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
-- Constraints for table `ContentSpecMetaData_AUD`
--
ALTER TABLE `ContentSpecMetaData_AUD`
  ADD CONSTRAINT `FK6DBBA77416D0EC8F` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
  ADD CONSTRAINT `FK6DBBA7744E83BBDA` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ContentSpecNode`
--
ALTER TABLE `ContentSpecNode`
  ADD CONSTRAINT `FKD5DB8BD64F855BD4` FOREIGN KEY (`ParentID`) REFERENCES `ContentSpecNode` (`ContentSpecNodeID`),
  ADD CONSTRAINT `FKD5DB8BD6367D70E3` FOREIGN KEY (`PreviousNodeID`) REFERENCES `ContentSpecNode` (`ContentSpecNodeID`),
  ADD CONSTRAINT `FKD5DB8BD6EBE853DF` FOREIGN KEY (`NextNodeID`) REFERENCES `ContentSpecNode` (`ContentSpecNodeID`),
  ADD CONSTRAINT `FKD5DB8BD6ED197BA6` FOREIGN KEY (`ContentSpecID`) REFERENCES `ContentSpec` (`ContentSpecID`);

--
-- Constraints for table `ContentSpecNodeToContentSpecNode`
--
ALTER TABLE `ContentSpecNodeToContentSpecNode`
  ADD CONSTRAINT `FKDF1672C552F62225` FOREIGN KEY (`MainNodeID`) REFERENCES `ContentSpecNode` (`ContentSpecNodeID`),
  ADD CONSTRAINT `FKDF1672C540009B17` FOREIGN KEY (`RelatedNodeID`) REFERENCES `ContentSpecNode` (`ContentSpecNodeID`);

--
-- Constraints for table `ContentSpecNodeToContentSpecNode_AUD`
--
ALTER TABLE `ContentSpecNodeToContentSpecNode_AUD`
  ADD CONSTRAINT `FKB591B61616D0EC8F` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
  ADD CONSTRAINT `FKB591B6164E83BBDA` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ContentSpecNodeToPropertyTag`
--
ALTER TABLE `ContentSpecNodeToPropertyTag`
  ADD CONSTRAINT `FK4F3E7BD49612C5C2` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`),
  ADD CONSTRAINT `FK4F3E7BD4ABA6BBE0` FOREIGN KEY (`ContentSpecNodeID`) REFERENCES `ContentSpecNode` (`ContentSpecNodeID`);

--
-- Constraints for table `ContentSpecNodeToPropertyTag_AUD`
--
ALTER TABLE `ContentSpecNodeToPropertyTag_AUD`
  ADD CONSTRAINT `FK51609FA5A110986` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
  ADD CONSTRAINT `FK51609FA5D25E3A3B` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ContentSpecNode_AUD`
--
ALTER TABLE `ContentSpecNode_AUD`
  ADD CONSTRAINT `FK2311DEA7A110986` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
  ADD CONSTRAINT `FK2311DEA7D25E3A3B` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ContentSpecToContentSpecMetaData`
--
ALTER TABLE `ContentSpecToContentSpecMetaData`
  ADD CONSTRAINT `FKEFF3F9747E34A6EE` FOREIGN KEY (`ContentSpecMetaDataID`) REFERENCES `ContentSpecMetaData` (`ContentSpecMetaDataID`),
  ADD CONSTRAINT `FKEFF3F974CA8809D2` FOREIGN KEY (`ContentSpecID`) REFERENCES `ContentSpec` (`ContentSpecID`);

--
-- Constraints for table `ContentSpecToContentSpecMetaData_AUD`
--
ALTER TABLE `ContentSpecToContentSpecMetaData_AUD`
  ADD CONSTRAINT `FK79DE4D4516D0EC8F` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
  ADD CONSTRAINT `FK79DE4D454E83BBDA` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ContentSpecToProject`
--
ALTER TABLE `ContentSpecToProject`
  ADD CONSTRAINT `FKADEB4ACACA8809D2` FOREIGN KEY (`ContentSpecID`) REFERENCES `ContentSpec` (`ContentSpecID`),
  ADD CONSTRAINT `FKADEB4ACAEB3A6876` FOREIGN KEY (`ProjectID`) REFERENCES `Project` (`ProjectID`);

--
-- Constraints for table `ContentSpecToProject_AUD`
--
ALTER TABLE `ContentSpecToProject_AUD`
  ADD CONSTRAINT `FKDE81039B16D0EC8F` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
  ADD CONSTRAINT `FKDE81039B4E83BBDA` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ContentSpecToPropertyTag`
--
ALTER TABLE `ContentSpecToPropertyTag`
  ADD CONSTRAINT `FK676CDCB6CA8809D2` FOREIGN KEY (`ContentSpecID`) REFERENCES `ContentSpec` (`ContentSpecID`),
  ADD CONSTRAINT `FK676CDCB6F3A435EE` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`);

--
-- Constraints for table `ContentSpecToPropertyTag_AUD`
--
ALTER TABLE `ContentSpecToPropertyTag_AUD`
  ADD CONSTRAINT `FKF7CFBF8716D0EC8F` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
  ADD CONSTRAINT `FKF7CFBF874E83BBDA` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ContentSpecToTag`
--
ALTER TABLE `ContentSpecToTag`
  ADD CONSTRAINT `FKE5EA3FCB9BADF58C` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`),
  ADD CONSTRAINT `FKE5EA3FCBED197BA6` FOREIGN KEY (`ContentSpecID`) REFERENCES `ContentSpec` (`ContentSpecID`);

--
-- Constraints for table `ContentSpecToTag_AUD`
--
ALTER TABLE `ContentSpecToTag_AUD`
  ADD CONSTRAINT `FK640B901CA110986` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
  ADD CONSTRAINT `FK640B901CD25E3A3B` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ContentSpecTranslatedString_AUD`
--
ALTER TABLE `ContentSpecTranslatedString_AUD`
  ADD CONSTRAINT `FKF97154ACA110986` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
  ADD CONSTRAINT `FKF97154ACD25E3A3B` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `ContentSpec_AUD`
--
ALTER TABLE `ContentSpec_AUD`
  ADD CONSTRAINT `FKD5E2978516D0EC8F` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`),
  ADD CONSTRAINT `FKD5E297854E83BBDA` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `CSMetaDataToCSTranslatedString`
--
ALTER TABLE `CSMetaDataToCSTranslatedString`
  ADD CONSTRAINT `FK70BF7951CCD88DBA` FOREIGN KEY (`ContentSpecToCSMetaDataID`) REFERENCES `ContentSpecToContentSpecMetaData` (`ContentSpecToContentSpecMetaDataID`),
  ADD CONSTRAINT `FK70BF79518DF9AE0A` FOREIGN KEY (`ContentSpecTranslatedStringID`) REFERENCES `ContentSpecTranslatedString` (`ContentSpecTranslatedStringID`);

--
-- Constraints for table `CSMetaDataToCSTranslatedString_AUD`
--
ALTER TABLE `CSMetaDataToCSTranslatedString_AUD`
  ADD CONSTRAINT `FKA6FC96A2A110986` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
  ADD CONSTRAINT `FKA6FC96A2D25E3A3B` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `CSNodeToCSTranslatedString`
--
ALTER TABLE `CSNodeToCSTranslatedString`
  ADD CONSTRAINT `FKEE5DA8848DF9AE0A` FOREIGN KEY (`ContentSpecTranslatedStringID`) REFERENCES `ContentSpecTranslatedString` (`ContentSpecTranslatedStringID`),
  ADD CONSTRAINT `FKEE5DA884ABA6BBE0` FOREIGN KEY (`ContentSpecNodeID`) REFERENCES `ContentSpecNode` (`ContentSpecNodeID`);

--
-- Constraints for table `CSNodeToCSTranslatedString_AUD`
--
ALTER TABLE `CSNodeToCSTranslatedString_AUD`
  ADD CONSTRAINT `FKBEB9F455A110986` FOREIGN KEY (`REV`) REFERENCES `REVINFO` (`REV`),
  ADD CONSTRAINT `FKBEB9F455D25E3A3B` FOREIGN KEY (`REVEND`) REFERENCES `REVINFO` (`REV`);

--
-- Constraints for table `FilterCategory`
--
ALTER TABLE `FilterCategory`
  ADD CONSTRAINT `FKBB45C0B6EB3A6876` FOREIGN KEY (`ProjectID`) REFERENCES `Project` (`ProjectID`),
  ADD CONSTRAINT `fk_FilterCategory_1` FOREIGN KEY (`CategoryID`) REFERENCES `Category` (`CategoryID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_FilterCategory_2` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`) ON DELETE CASCADE ON UPDATE NO ACTION;

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
  ADD CONSTRAINT `fk_Filter_Field_1` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `fk_FilterTag_1` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_FilterTag_2` FOREIGN KEY (`FilterID`) REFERENCES `Filter` (`FilterID`) ON DELETE CASCADE ON UPDATE NO ACTION;

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
-- Constraints for table `LanguageImage`
--
ALTER TABLE `LanguageImage`
  ADD CONSTRAINT `FK15D2ACC34F65E026` FOREIGN KEY (`ImageFileID`) REFERENCES `ImageFile` (`ImageFileID`);

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
  ADD CONSTRAINT `PropertyTagToPropertTagCategoryFK1` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `PropertyTagToPropertTagCategoryFk2` FOREIGN KEY (`PropertyTagCategoryID`) REFERENCES `PropertyTagCategory` (`PropertyTagCategoryID`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `FKD0A0E5C78433D197` FOREIGN KEY (`SecondaryRole`) REFERENCES `Role` (`RoleID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FKD0A0E5C7844F7725` FOREIGN KEY (`PrimaryRole`) REFERENCES `Role` (`RoleID`),
  ADD CONSTRAINT `fk_RoleToRole_1` FOREIGN KEY (`RelationshipType`) REFERENCES `RoleToRoleRelationship` (`RoleToRoleRelationshipID`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `fk_TagExclusion_1` FOREIGN KEY (`Tag1ID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_TagExclusion_2` FOREIGN KEY (`Tag2ID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `FK8DF417B36F9851B8` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON UPDATE CASCADE,
  ADD CONSTRAINT `FK8DF417B37D4F054E` FOREIGN KEY (`CategoryID`) REFERENCES `Category` (`CategoryID`) ON DELETE CASCADE;

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
  ADD CONSTRAINT `fk_TagToProject_1` FOREIGN KEY (`ProjectID`) REFERENCES `Project` (`ProjectID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_TagToProject_2` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

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
  ADD CONSTRAINT `TagToPropertyTagFK1` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `TagToPropertyTagFK2` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `fk_TagToTag_1` FOREIGN KEY (`RelationshipType`) REFERENCES `TagToTagRelationship` (`TagToTagRelationshipType`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_TagToTag_2` FOREIGN KEY (`PrimaryTagID`) REFERENCES `Tag` (`TagID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_TagToTag_3` FOREIGN KEY (`SecondaryTagID`) REFERENCES `Tag` (`TagID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

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
  ADD CONSTRAINT `TopicToBugzillaBugFK2` FOREIGN KEY (`BugzillaBugID`) REFERENCES `BugzillaBug` (`BugzillaBugID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `TopicToBugzillaBug_ibfk_1` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `TopicToPropertyTagFK1` FOREIGN KEY (`PropertyTagID`) REFERENCES `PropertyTag` (`PropertyTagID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `TopicToPropertyTagFK2` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `fk_TopicToTag_1` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_TopicToTag_2` FOREIGN KEY (`TagID`) REFERENCES `Tag` (`TagID`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `fk_TopicToTopic_1` FOREIGN KEY (`MainTopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_TopicToTopic_2` FOREIGN KEY (`RelatedTopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_TopicToTopic_3` FOREIGN KEY (`RelationshipTagID`) REFERENCES `RelationshipTag` (`RelationshipTagID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `TopicToTopicSecondOrderData`
--
ALTER TABLE `TopicToTopicSecondOrderData`
  ADD CONSTRAINT `FK303D33DF3340562` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_TopicToTopicSecondOrderData_1` FOREIGN KEY (`TopicSecondOrderDataID`) REFERENCES `TopicSecondOrderData` (`TopicSecondOrderDataID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `TopicToTopicSourceURL`
--
ALTER TABLE `TopicToTopicSourceURL`
  ADD CONSTRAINT `ForeignTopicID` FOREIGN KEY (`TopicID`) REFERENCES `Topic` (`TopicID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ForeignTopicSourceURLID` FOREIGN KEY (`TopicSourceURLID`) REFERENCES `TopicSourceURL` (`TopicSourceURLID`) ON DELETE CASCADE ON UPDATE CASCADE;

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
  ADD CONSTRAINT `fk_UserRole_1` FOREIGN KEY (`RoleNameID`) REFERENCES `Role` (`RoleID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_UserRole_2` FOREIGN KEY (`UserNameID`) REFERENCES `User` (`UserID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

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

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
