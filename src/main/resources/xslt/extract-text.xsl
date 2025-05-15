<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:cda="urn:hl7-org:v3"
    exclude-result-prefixes="cda">

  <xsl:output method="html" indent="yes"/>

  <!-- Root template -->
  <xsl:template match="/">
    <html>
      <body>
        <h2>Extracted CDA Content</h2>
        <xsl:apply-templates select="//cda:section"/>
      </body>
    </html>
  </xsl:template>

  <!-- Render section title and its text -->
  <xsl:template match="cda:section">
    <div style="margin-bottom: 1em; padding: 0.5em; border: 1px solid #ccc;">
      <xsl:if test="cda:title">
        <h3><xsl:value-of select="cda:title"/></h3>
      </xsl:if>
      <xsl:apply-templates select="cda:text"/>
    </div>
  </xsl:template>

  <!-- Convert paragraph to <p> -->
  <xsl:template match="cda:paragraph">
    <p>
      <xsl:apply-templates/>
    </p>
  </xsl:template>

  <!-- Default rule: copy everything else -->
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
