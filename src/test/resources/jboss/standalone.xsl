<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                exclude-result-prefixes="xs xsl xsi">

    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*[@default-virtual-server]">
        <xsl:copy>
            <connector name="http" protocol="HTTP/1.1" scheme="http" socket-binding="http" redirect-port="8443"/>
            <connector name="https" protocol="HTTP/1.1" scheme="https" socket-binding="https" secure="true">
                <ssl name="https" password="${keystore.password}"
                     certificate-key-file="${jboss.config.dir}/server.keystore" protocol="TLS"/>
            </connector>
            <virtual-server name="default-host" enable-welcome-root="true">
                <alias name="localhost"/>
                <alias name="example.com"/>
            </virtual-server>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*[@port='8080']">
        <xsl:copy>
            <xsl:attribute name="name">
                <xsl:value-of select="'http'"/>
            </xsl:attribute>
            <xsl:attribute name="port">
                <xsl:value-of select="'18081'"/>
            </xsl:attribute>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*[@path='deployments']">
        <xsl:copy>
            <xsl:attribute name="path">
                <xsl:value-of select="'deployments'"/>
            </xsl:attribute>
            <xsl:attribute name="relative-to">
                <xsl:value-of select="'jboss.server.base.dir'"/>
            </xsl:attribute>
            <xsl:attribute name="scan-interval">
                <xsl:value-of select="'5000'"/>
            </xsl:attribute>
            <xsl:attribute name="deployment-timeout">
                <xsl:value-of select="'1200'"/>
            </xsl:attribute>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="node()[name(.)='extensions']">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <extension module="org.jboss.as.messaging"/>
        </xsl:copy>
        <system-properties>
            <!--<property name="javax.net.debug" value="ssl"/>-->
            <property name="javax.net.ssl.trustStore" value="${jboss.config.dir}/client.truststore"/>
            <property name="javax.net.ssl.trustStorePassword" value="${truststore.password}"/>
            <property name="javax.net.ssl.trustStoreType" value="jks"/>
        </system-properties>
    </xsl:template>

    <xsl:template match="node()[name(.)='profile']">
        <xsl:copy>
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
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="node()[name(.)='socket-binding-group']">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
            <socket-binding name="messaging" port="5445"/>
            <socket-binding name="messaging-throughput" port="5455"/>
        </xsl:copy>
    </xsl:template>

    <!--<xsl:template match="*[@name='INFO']">-->
    <!--<xsl:copy>-->
    <!--<xsl:attribute name="name">-->
    <!--<xsl:value-of select="'DEBUG'"/>-->
    <!--</xsl:attribute>-->
    <!--</xsl:copy>-->
    <!--</xsl:template>-->

</xsl:stylesheet>