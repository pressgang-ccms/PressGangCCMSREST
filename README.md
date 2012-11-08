PressGang CCMS REST Server
==========================

This project is setup to run on JBoss AS 7.1.1. See below for setup steps:

Prerequisites
-------------

* Downloaded and Extracted JBoss AS 7.1.1 to a location of your choice.
* Setup and installed a RDMS database of your choice.
	* Note: Currently we only have a setup for MySQL 5.x
* Installed and configured Maven.
* Have the OSS Sonatype Repository setup in your local settings.xml (to be able to compile).


Configure JBoss AS 7.1.1
------------------------

1. Open the Standalone Configuration located at `<JBOSS_DIR>/standalone/configuration/standalone.xml`
2. Add the following System Properties and populate it with relavent data.

        <system-properties>
            <property name="topicIndex.bugzillaUrl" value="bugzilla.redhat.com"/>
            <property name="topicIndex.bugzillaUsername" value=""/>
            <property name="topicIndex.bugzillaPassword" value=""/>
            <property name="topicIndex.stompMessageServer" value="localhost"/>
            <property name="topicIndex.stompMessageServerPort" value="61613"/>
            <property name="topicIndex.stompMessageServerUser" value="guest"/>
            <property name="topicIndex.stompMessageServerPass" value=""/>
            <property name="topicIndex.stompMessageServerRenderTopicQueue" value="jms.queue.PressGangCCMSTopicRenderQueue"/>
            <property name="topicIndex.stompMessageServerRenderTranslatedTopicQueue" value="jms.queue.PressGangCCMSTopicRenderQueue"/>
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

3. Next setup the Messaging subsystem. First add the following into the extensions:

        <extension module="org.jboss.as.messaging"/>

    After that, add the following messaging subsystem configuration:

        <subsystem xmlns="urn:jboss:domain:messaging:1.1">
            <hornetq-server>
                <persistence-enabled>true</persistence-enabled>
                <journal-file-size>102400</journal-file-size>
                <journal-min-files>2</journal-min-files>

                <connectors>
                    <netty-connector name="netty" socket-binding="messaging"/>
                    <netty-connector name="netty-throughput" socket-binding="messaging-throughput">
                        <param key="batch-delay" value="50"/>
                    </netty-connector>
                    <in-vm-connector name="in-vm" server-id="0"/>
                </connectors>
    
                <acceptors>
                    <acceptor name="stomp-acceptor">
                        <factory-class>org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory</factory-class>
                        <param key="protocol" value="stomp"/>
                        <param key="host" value="0.0.0.0"/>
                        <param key="port" value="61613"/>
                    </acceptor>
                    <netty-acceptor name="netty" socket-binding="messaging"/>
                    <netty-acceptor name="netty-throughput" socket-binding="messaging-throughput">
                        <param key="batch-delay" value="50"/>
                        <param key="direct-deliver" value="false"/>
                    </netty-acceptor>
                    <in-vm-acceptor name="in-vm" server-id="0"/>
                </acceptors>
    
                <security-settings>
                    <security-setting match="#">
                        <permission type="send" roles="guest"/>
                        <permission type="consume" roles="guest"/>
                        <permission type="createNonDurableQueue" roles="guest"/>
                        <permission type="deleteNonDurableQueue" roles="guest"/>
                    </security-setting>
                </security-settings>
    
                <address-settings>
                    <address-setting match="#">
                        <dead-letter-address>jms.queue.DLQ</dead-letter-address>
                        <expiry-address>jms.queue.ExpiryQueue</expiry-address>
                        <redelivery-delay>0</redelivery-delay>
                        <max-size-bytes>10485760</max-size-bytes>
                        <address-full-policy>BLOCK</address-full-policy>
                        <message-counter-history-day-limit>10</message-counter-history-day-limit>
                    </address-setting>
                </address-settings>
    
                <jms-connection-factories>
                    <connection-factory name="InVmConnectionFactory">
                        <connectors>
                            <connector-ref connector-name="in-vm"/>
                        </connectors>
                        <entries>
                            <entry name="java:/ConnectionFactory"/>
                        </entries>
                    </connection-factory>
                    <connection-factory name="RemoteConnectionFactory">
                        <connectors>
                            <connector-ref connector-name="netty"/>
                        </connectors>
                        <entries>
                            <entry name="RemoteConnectionFactory"/>
                            <entry name="java:jboss/exported/jms/RemoteConnectionFactory"/>
                        </entries>
                    </connection-factory>
                    <pooled-connection-factory name="hornetq-ra">
                        <transaction mode="xa"/>
                        <connectors>
                            <connector-ref connector-name="in-vm"/>
                        </connectors>
                        <entries>
                            <entry name="java:/JmsXA"/>
                        </entries>
                    </pooled-connection-factory>
                </jms-connection-factories>
    
                <jms-destinations>
                    <jms-queue name="PressGangCCMSTopicRenderQueue">
                        <entry name="queue/PressGangCCMSTopicRenderQueue"/>
                    </jms-queue>
                    <jms-queue name="PressGangCCMSTranslatedTopicRenderQueue">
                        <entry name="queue/PressGangCCMSTranslatedTopicRenderQueue"/>
                    </jms-queue>
                </jms-destinations>
            </hornetq-server>
        </subsystem>

    and finally add the following to the socket bindings:

        <socket-binding name="messaging" port="5445"/>
        <socket-binding name="messaging-throughput" port="5455"/>

4. The next step is to add the data source configuration. Below is a sample configuration:

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

5. The last step is to unzip the MySQL Driver into the Modules directory. The archive can be found at *program-resources/modules.zip* in this repository.

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