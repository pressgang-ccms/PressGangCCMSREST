<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:d="DAV:">
 <xsl:output method="xml" />
 <xsl:strip-space elements="*" />
 <xsl:template match="*">
  <xsl:copy-of select="."/>
 </xsl:template>
 <xsl:template match="d:*[local-name() = name()]">
  <xsl:element namespace="{namespace-uri()}" name="d:{local-name()}">
   <xsl:copy-of select="@*"/>
   <xsl:apply-templates/>
  </xsl:element>
 </xsl:template>
</xsl:stylesheet>
