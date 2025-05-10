<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet 
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:cda="urn:hl7-org:v3"
    exclude-result-prefixes="cda">

  <xsl:output method="html" indent="yes"/>

  <xsl:template match="/">
    <html>
      <body>
        <h2>Extracted &lt;text&gt; Elements</h2>
        <xsl:for-each select="//cda:text">
          <div style="margin-bottom: 1em; padding: 0.5em; border: 1px solid #ccc;">
            <xsl:copy-of select="node()"/>
          </div>
        </xsl:for-each>
      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>
