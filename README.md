The PressGang CCMS REST Server project provides a RESTful web interface for the PressGang CCMS (Component Content Management System). It offers create, update, get and delete endpoints for the system's primary entities. These endpoints produce both JSON and JSONP, which adds cross-domain functionality. Topic entities also have ATOM, XML and HTML get endpoints.

PressGang CCMS REST Server
==========================

This project is setup to run on JBoss AS 7.1.1. See below for setup steps:

Prerequisites
-------------

* Downloaded and Extracted JBoss AS 7.1.1 to a location of your choice (referred to as `<JBOSS_DIR>` in this guide).
* Setup and installed a RDBMS database of your choice.
	* Note: Currently we only have a setup for MySQL 5.x
* Installed and configured Maven.
* Have the OSS Sonatype Repository setup in your local settings.xml (to be able to compile).


Configure JBoss AS 7.1.1
------------------------

1. Open the Standalone Configuration located at `<JBOSS_DIR>/standalone/configuration/standalone.xml`
2. Add the following System Properties and populate it with relevant data.

        <system-properties>
            <property name="topicIndex.bugzillaUrl" value="bugzilla.redhat.com"/>
            <property name="topicIndex.bugzillaUsername" value=""/>
            <property name="topicIndex.bugzillaPassword" value=""/>
            <property name="NumberOfWorkerThreads" value="1"/>
            <property name="topicIndex.kerberosEnabled" value="false"/>
            <property name="topicindex.rerenderTopic" value="false"/>
            <property name="java.security.krb5.kdc" value=""/>
            <property name="java.security.krb5.realm" value=""/>
            <property name="topicIndex.instanceName" value=""/>
            <property name="topicIndex.zanataServer" value=""/>
            <property name="topicIndex.zanataProject" value=""/>
            <property name="topicIndex.zanataUsername" value=""/>
            <property name="topicIndex.zanataProjectVersion" value=""/>
            <property name="topicIndex.zanataToken" value=""/>
            <property name="topicIndex.defaultLocale" value="en-US"/>
        </system-properties>

3. The next step is to add the data source configuration. Below is a sample configuration:

		<datasource jta="true" jndi-name="java:/PressGangCCMSDatasource" pool-name="PressGangCCMSDatasource" enabled="true" use-java-context="true" use-ccm="true">
		    <connection-url>jdbc:mysql://localhost:3306/PressGangCCMS?jdbcCompliantTruncation=false&amp;useUnicode=true&amp;characterEncoding=UTF-8</connection-url>
		    <driver-class>com.mysql.jdbc.Driver</driver-class>
		    <driver>mysql</driver>
		    <transaction-isolation>TRANSACTION_READ_COMMITTED</transaction-isolation>
		    <pool>
		        <min-pool-size>20</min-pool-size>
		        <max-pool-size>151</max-pool-size>
		        <prefill>true</prefill>
		        <use-strict-min>false</use-strict-min>
		        <flush-strategy>FailingConnectionOnly</flush-strategy>
		    </pool>
		    <security>
		        <user-name></user-name>
		        <password></password>
		    </security>
		    <validation>
		        <validate-on-match>false</validate-on-match>
		        <background-validation>false</background-validation>
		        <use-fast-fail>false</use-fast-fail>
		    </validation>
		    <statement>
		        <prepared-statement-cache-size>50</prepared-statement-cache-size>
		        <share-prepared-statements>true</share-prepared-statements>
		    </statement>
		</datasource>
		<drivers>
		    <driver name="mysql" module="com.mysql"/>
		</drivers>

4. The last step is to unzip the MySQL Driver into the Modules directory. The archive can be found at *program-resources/modules.zip* in
this repository.

Importing the Base Database
---------------------------

Supplied is a MySQL Database backups that contains the core components for the Server to run. The supplied SQL will create a Database called *PressGangCCMS*, relevant Tables and any initial data.

Compiling the Source
--------------------

1. Ensure that you have Maven setup and the OSS Repository setup in your settings.xml
2. Run:

    	mvn clean package

Installing to the Application Server
------------------------------------

1. Ensure that the Application Server is running by opening another terminal and navigating to *<JBOSS-DIR>/bin* and execute the *standalone.sh* script.
2. In the first terminal run the following command:

    	mvn clean package jboss-as:deploy