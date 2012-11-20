insert into REVINFO (REV, REVTSTMP, FLAG, MESSAGE, USERNAME) values (1, 1306078259013, NULL, NULL, NULL);

insert into CATEGORY (CATEGORYID, CATEGORYNAME, CATEGORYDESCRIPTION, CATEGORYSORT, MUTUALLYEXCLUSIVE) values (1, 'Audiences', '', 15, 0), (2, 'Concerns', '', 25, 0), (3, 'Technologies', '', 20, 0);
insert into CATEGORY_AUD (CATEGORYID, REV, REVEND, REVTYPE, CATEGORYDESCRIPTION, CATEGORYNAME, CATEGORYSORT, MUTUALLYEXCLUSIVE) values (1, 1, NULL, 0, '', 'Audiences', 15, 0), (2, 1, NULL, 0, '', 'Concerns', 25, 0), (3, 1, NULL, 0, '', 'Technologies', 20, 0);

insert into PROPERTYTAG (PROPERTYTAGID, PROPERTYTAGNAME, PROPERTYTAGDESCRIPTION, PROPERTYTAGREGEX, PROPERTYTAGCANBENULL, PROPERTYTAGISUNIQUE) values (1, 'First Name', '', '^[a-zA-Z][a-zA-Z\\-'' ]*[a-zA-Z ]$', 0, 0), (2, 'Last Name', '', '^[a-zA-Z][a-zA-Z\\-'' ]*[a-zA-Z ]$', 0, 0), (3, 'Email Address', '', '^([a-zA-Z0-9_\\-\.]+)@((\\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$', 0, 0);
insert into PROPERTYTAG_AUD (PROPERTYTAGID, REV, REVEND, REVTYPE, PROPERTYTAGCANBENULL, PROPERTYTAGDESCRIPTION, PROPERTYTAGNAME, PROPERTYTAGREGEX, PROPERTYTAGISUNIQUE) values (1, 1, NULL, 0, 0, '', 'First Name', '^[a-zA-Z][a-zA-Z\\-'' ]*[a-zA-Z ]$', 0), (2, 1, NULL, 0, 0, '', 'Last Name', '^[a-zA-Z][a-zA-Z\\-'' ]*[a-zA-Z ]$', 0), (3, 1, NULL, 0, 0, '', 'Email Address', '^([a-zA-Z0-9_\\-\.]+)@((\\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$', 0);

insert into PROPERTYTAGCATEGORY (PROPERTYTAGCATEGORYID, PROPERTYTAGCATEGORYNAME, PROPERTYTAGCATEGORYDESCRIPTION) values (1, 'Person', 'Properties that relate to a specific person'), (2, 'Topic', 'Properties that relate to topics'), (3, 'Content Spec Processor', 'Tags related to the Content Spec Processor');
insert into PROPERTYTAGCATEGORY_AUD (PROPERTYTAGCATEGORYID, REV, REVEND, REVTYPE, PROPERTYTAGCATEGORYDESCRIPTION, PROPERTYTAGCATEGORYNAME) values (1, 1, NULL, 0, 'Properties that relate to a specific person', 'Person'), (2, 1, NULL, 0, 'Properties that relate to topics', 'Topic'), (3, 1, NULL, 0, 'Tags related to the Content Spec Processor', 'Content Spec Processor');

insert into PROPERTYTAGTOPROPERTYTAGCATEGORY (PROPERTYTAGTOPROPERTYTAGCATEGORYID, PROPERTYTAGID, PROPERTYTAGCATEGORYID, SORTING) values (1, 1, 1, NULL), (2, 2, 2, NULL), (3, 3, 3, NULL);
insert into PROPERTYTAGTOPROPERTYTAGCATEGORY_AUD (PROPERTYTAGTOPROPERTYTAGCATEGORYID, REV, REVEND, REVTYPE, SORTING, PROPERTYTAGID, PROPERTYTAGCATEGORYID) values (1, 1, NULL, 0, NULL, 1, 1), (2, 1, NULL, 0, NULL, 2, 2), (3, 1, NULL, 0, NULL, 3, 3);

insert into RELATIONSHIPTAG (RELATIONSHIPTAGID, RELATIONSHIPTAGNAME, RELATIONSHIPTAGDESCRIPTION) values (1, 'Relates To', 'Generic Relationship'), (2, 'Go Next To', NULL), (3, 'Go Back To', NULL);
insert into RELATIONSHIPTAG_AUD (RELATIONSHIPTAGID, REV, REVEND, REVTYPE, RELATIONSHIPTAGDESCRIPTION, RELATIONSHIPTAGNAME) values (1, 1, NULL, 0, 'Generic Relationship', 'Relates To'), (2, 1, NULL, 0, NULL, 'Go Next To'), (3, 1, NULL, 0, NULL, 'Go Back To');

insert into ROLE (ROLEID, ROLENAME, DESCRIPTION) values (1, 'adminRole', 'Administrator'), (2, 'writerRole', 'Writer'), (3, 'editorRole', 'Editor');
insert into ROLE_AUD (ROLEID, REV, REVEND, REVTYPE, DESCRIPTION, ROLENAME) values (1, 1, NULL, 0, 'Administrator', 'adminRole'), (2, 1, NULL, 0, 'Writer', 'writerRole'), (3, 1, NULL, 0, 'Editor', 'editorRole');

insert into ROLETOROLERELATIONSHIP (ROLETOROLERELATIONSHIPID, DESCRIPTION) values (1, 'Inherit');
insert into ROLETOROLERELATIONSHIP_AUD (ROLETOROLERELATIONSHIPID, REV, REVEND, REVTYPE, DESCRIPTION) values (1, 1, NULL, 0, 'Inherit');

insert into ROLETOROLE (ROLETOROLEID, PRIMARYROLE, RELATIONSHIPTYPE, SECONDARYROLE) values (1, 1, 1, 2), (2, 2, 1, 3), (3, 3, 1, 1);
insert into ROLETOROLE_AUD (ROLETOROLEID, REV, REVEND, REVTYPE, PRIMARYROLE, RELATIONSHIPTYPE, SECONDARYROLE) values (1, 1, NULL, 0, 1, 1, 2), (2, 1, NULL, 0, 2, 1, 3), (3, 1, NULL, 0, 3, 1, 1);

insert into STRINGCONSTANTS (STRINGCONSTANTSID, CONSTANTNAME, CONSTANTVALUE) values (1, 'en-US/Book.xml', '<?xml version=''1.0'' encoding=''utf-8'' ?>\r\n<!DOCTYPE book PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN" "http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd" [\r\n<!ENTITY % BOOK_ENTITIES SYSTEM "<<contentSpec.escapedTitle>>.ent">\r\n%BOOK_ENTITIES;\r\n]>\r\n<book <<contentSpec.draft>>>\r\n	<xi:include href="Book_Info.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n	<!-- Inject Preface -->\r\n	<!-- Inject XIIncludes -->\r\n    <!-- Inject Revision History -->\r\n</book>'), (2, 'en-US/Book.ent', '<!ENTITY PRODUCT "<<contentSpec.product>>">\r\n<!ENTITY BOOKID "<<contentSpec.escapedTitle>>">\r\n<!ENTITY YEAR "YYYY">\r\n<!ENTITY TITLE "<<contentSpec.title>>">\r\n<!ENTITY HOLDER "<<contentSpec.copyrightHolder>>">\r\n<!ENTITY BZURL "<<contentSpec.bugzillaUrl>>">\r\n<!ENTITY BZCOMPONENT "<<contentSpec.bzcomponent>>">\r\n<!ENTITY BZPRODUCT "<<contentSpec.bzproduct>>">'), (3, 'en-US/Book_Info.xml', '<?xml version=''1.0'' encoding=''utf-8'' ?>\r\n<!DOCTYPE bookinfo PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN"\r\n"http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd" [\r\n<!ENTITY % BOOK_ENTITIES SYSTEM "<<contentSpec.escapedTitle>>.ent">\r\n%BOOK_ENTITIES;\r\n]>\r\n<bookinfo id="book-<<contentSpec.escapedTitle>>-<<contentSpec.escapedTitle>>">\r\n	<title><<contentSpec.title>></title>\r\n	<subtitle><<contentSpec.subtitle>></subtitle>\r\n	<productname><<contentSpec.product>></productname>\r\n	<productnumber><<contentSpec.version>></productnumber>\r\n	<edition><<contentSpec.edition>></edition>\r\n	<!-- Inject Abstract -->\r\n	<corpauthor>\r\n		<inlinemediaobject>\r\n	 	<imageobject>\r\n	     	<imagedata fileref="Common_Content/images/title_logo.svg" format="SVG" />\r\n	     </imageobject>\r\n	    </inlinemediaobject>\r\n	</corpauthor>\r\n	<!-- Inject Legal Notice -->\r\n	<xi:include href="Author_Group.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n</bookinfo>');
insert into STRINGCONSTANTS_AUD (STRINGCONSTANTSID, REV, REVEND, REVTYPE, CONSTANTNAME, CONSTANTVALUE) values (1, 1, NULL, 0, 'en-US/Book.xml', '<?xml version=''1.0'' encoding=''utf-8'' ?>\r\n<!DOCTYPE book PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN" "http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd" [\r\n<!ENTITY % BOOK_ENTITIES SYSTEM "<<contentSpec.escapedTitle>>.ent">\r\n%BOOK_ENTITIES;\r\n]>\r\n<book <<contentSpec.draft>>>\r\n	<xi:include href="Book_Info.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n	<!-- Inject Preface -->\r\n	<!-- Inject XIIncludes -->\r\n    <!-- Inject Revision History -->\r\n</book>'), (2, 1, NULL, 0, 'en-US/Book.ent', '<!ENTITY PRODUCT "<<contentSpec.product>>">\r\n<!ENTITY BOOKID "<<contentSpec.escapedTitle>>">\r\n<!ENTITY YEAR "YYYY">\r\n<!ENTITY TITLE "<<contentSpec.title>>">\r\n<!ENTITY HOLDER "<<contentSpec.copyrightHolder>>">\r\n<!ENTITY BZURL "<<contentSpec.bugzillaUrl>>">\r\n<!ENTITY BZCOMPONENT "<<contentSpec.bzcomponent>>">\r\n<!ENTITY BZPRODUCT "<<contentSpec.bzproduct>>">'), (3, 1, NULL, 0, 'en-US/Book_Info.xml', '<?xml version=''1.0'' encoding=''utf-8'' ?>\r\n<!DOCTYPE bookinfo PUBLIC "-//OASIS//DTD DocBook XML V4.5//EN"\r\n"http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd" [\r\n<!ENTITY % BOOK_ENTITIES SYSTEM "<<contentSpec.escapedTitle>>.ent">\r\n%BOOK_ENTITIES;\r\n]>\r\n<bookinfo id="book-<<contentSpec.escapedTitle>>-<<contentSpec.escapedTitle>>">\r\n	<title><<contentSpec.title>></title>\r\n	<subtitle><<contentSpec.subtitle>></subtitle>\r\n	<productname><<contentSpec.product>></productname>\r\n	<productnumber><<contentSpec.version>></productnumber>\r\n	<edition><<contentSpec.edition>></edition>\r\n	<!-- Inject Abstract -->\r\n	<corpauthor>\r\n		<inlinemediaobject>\r\n	 	<imageobject>\r\n	     	<imagedata fileref="Common_Content/images/title_logo.svg" format="SVG" />\r\n	     </imageobject>\r\n	    </inlinemediaobject>\r\n	</corpauthor>\r\n	<!-- Inject Legal Notice -->\r\n	<xi:include href="Author_Group.xml" xmlns:xi="http://www.w3.org/2001/XInclude" />\r\n</bookinfo>');

insert into TAG (TAGID, TAGNAME, TAGDESCRIPTION) values (1, 'Task', 'How to do something. Tasks should start with a verb, and be action-oriented.'), (2, 'Concept', 'aka Definition'), (3, 'Reference', 'Information that is not a definition and not action-oriented. Supplementary info.');
insert into TAG_AUD (TAGID, REV, REVEND, REVTYPE, TAGDESCRIPTION, TAGNAME) values (1, 1, NULL, 0, 'How to do something. Tasks should start with a verb, and be action-oriented.', 'Task'), (2, 1, NULL, 0, 'aka Definition', 'Concept'), (3, 1, NULL, 0, 'Information that is not a definition and not action-oriented. Supplementary info.', 'Reference');

insert into TAGTOCATEGORY (TAGTOCATEGORYID, TAGID, CATEGORYID, SORTING) values (1, 1, 1, 0), (2, 2, 2, 0), (3, 3, 3, 0);
insert into TAGTOCATEGORY_AUD (TAGTOCATEGORYID, REV, REVEND, REVTYPE, SORTING, CATEGORYID, TAGID) values (1, 1, NULL, 0, 0, 1, 1), (2, 1, NULL, 0, 0, 2, 2), (3, 1, NULL, 0, 0, 3, 3);

insert into TAGTOTAGRELATIONSHIP (TAGTOTAGRELATIONSHIPTYPE, TAGTOTAGRELATIONSHIPDESCRIPTION) values (1, 'Primary Tag is the common name for the Secondary Tag');
insert into TAGTOTAGRELATIONSHIP_AUD (TAGTOTAGRELATIONSHIPTYPE, REV, REVEND, REVTYPE, TAGTOTAGRELATIONSHIPDESCRIPTION) values (1, 1, NULL, 0, 'Primary Tag is the common name for the Secondary Tag');


insert into RS_SCOPE (SCOPE_ID, SCOPE_NAME) values (-1, 'DEFAULT')
insert into RS_SCOPE (SCOPE_ID, SCOPE_NAME) values (-2, 'PERFORM_SUPER_USER_ACTION')

insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-1, 'https://localhost:8443/testrestserver/rest/1/settings/rerenderTopic', 'POST', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-2, 'https://localhost:8443/testrestserver/rest/1/expanddatatrunk/get/json', 'GET', false)

insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-3, 'https://localhost:8443/testrestserver/rest/1/category/get/jsonp/\d+', 'GET', true)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-4, 'https://localhost:8443/testrestserver/rest/1/categories/get/jsonp/all', 'GET', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-5, 'https://localhost:8443/testrestserver/rest/1/categories/get/jsonp/query;([-\w%~\.]+=[-\w%~\.]*;)*([-\w%~\.]+=[-\w%~\.]*;?)?', 'GET', true)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-6, 'https://localhost:8443/testrestserver/rest/1/category/update/jsonp', 'GET', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-7, 'https://localhost:8443/testrestserver/rest/1/categories/update/jsonp', 'GET', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-8, 'https://localhost:8443/testrestserver/rest/1/category/create/jsonp', 'GET', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-9, 'https://localhost:8443/testrestserver/rest/1/categories/create/jsonp', 'GET', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-10, 'https://localhost:8443/testrestserver/rest/1/category/delete/jsonp/\d+', 'GET', true)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-11, 'https://localhost:8443/testrestserver/rest/1/categories/delete/jsonp/ids;(\d+;)*\d+;?', 'GET', true)

insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-12, 'https://localhost:8443/testrestserver/rest/1/category/get/json/\d+', 'GET', true)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-13, 'https://localhost:8443/testrestserver/rest/1/categories/get/json/all', 'GET', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-14, 'https://localhost:8443/testrestserver/rest/1/categories/get/json/query;([-\w%~\.]+=[-\w%~\.]*;)*([-\w%~\.]+=[-\w%~\.]*;?)?', 'GET', true)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-15, 'https://localhost:8443/testrestserver/rest/1/category/update/json', 'GET', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-16, 'https://localhost:8443/testrestserver/rest/1/categories/update/json', 'GET', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-17, 'https://localhost:8443/testrestserver/rest/1/category/create/json', 'GET', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-18, 'https://localhost:8443/testrestserver/rest/1/categories/create/json', 'GET', false)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-19, 'https://localhost:8443/testrestserver/rest/1/category/delete/json/\d+', 'GET', true)
insert into RS_ENDPOINT (ENDPOINT_ID, ENDPOINT_URL, ENDPOINT_METHOD, URL_REGEX) values (-20, 'https://localhost:8443/testrestserver/rest/1/categories/delete/json/ids;(\d+;)*\d+;?', 'GET', true)

insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -1)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -2)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -3)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -4)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -5)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -6)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -7)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -8)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -9)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -10)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -11)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -12)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -13)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -14)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -15)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -16)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -17)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-1, -18)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-2, -19)
insert into RS_SCOPE_RS_ENDPOINT (SCOPE_ID, ENDPOINT_ID) values (-2, -20);