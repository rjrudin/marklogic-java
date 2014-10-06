<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:db="http://marklogic.com/manage/package/databases">

  <xsl:param name="mergePackageFilePath" />
  <xsl:variable name="mergePackage" select="document($mergePackageFilePath)/db:package-database" />

  <xsl:template match="*">
    <xsl:copy>
      <xsl:copy-of select="attribute::*" />
      <xsl:apply-templates />
    </xsl:copy>
  </xsl:template>

  <xsl:template match="db:package-database-properties/node()[text()]">
    <xsl:variable name="nodeName" select="name(.)" />
    <xsl:variable name="mergeNode" select="$mergePackage/db:config/db:package-database-properties/node()[name(.) = $nodeName]" />
    <xsl:choose>
      <xsl:when test="$mergeNode">
        <xsl:copy-of select="$mergeNode" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:copy-of select="." />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="db:word-lexicons">
    <xsl:copy>
      <xsl:apply-templates />
      <xsl:apply-templates select="$mergePackage/db:config/db:package-database-properties/db:word-lexicons/db:word-lexicon" />
    </xsl:copy>
  </xsl:template>

  <xsl:template match="db:range-element-indexes">
    <xsl:copy>
      <xsl:apply-templates />
      <xsl:apply-templates
        select="$mergePackage/db:config/db:package-database-properties/db:range-element-indexes/db:range-element-index" />
    </xsl:copy>
  </xsl:template>

  <xsl:template match="db:range-element-attribute-indexes">
    <xsl:copy>
      <xsl:apply-templates />
      <xsl:apply-templates
        select="$mergePackage/db:config/db:package-database-properties/db:range-element-attribute-indexes/db:range-element-attribute-index" />
    </xsl:copy>
  </xsl:template>

  <xsl:template match="db:path-namespaces">
    <xsl:copy>
      <xsl:apply-templates />
      <xsl:apply-templates select="$mergePackage/db:config/db:package-database-properties/db:path-namespaces/db:path-namespace" />
    </xsl:copy>
  </xsl:template>

  <xsl:template match="db:range-path-indexes">
    <xsl:copy>
      <xsl:apply-templates />
      <xsl:apply-templates
        select="$mergePackage/db:config/db:package-database-properties/db:range-path-indexes/db:range-path-index" />
    </xsl:copy>
  </xsl:template>

  <xsl:template match="db:range-field-indexes">
    <xsl:copy>
      <xsl:apply-templates />
      <xsl:apply-templates
        select="$mergePackage/db:config/db:package-database-properties/db:range-field-indexes/db:range-field-index" />
    </xsl:copy>
  </xsl:template>

  <xsl:template match="db:geospatial-element-indexes">
    <xsl:copy>
      <xsl:apply-templates />
      <xsl:apply-templates
        select="$mergePackage/db:config/db:package-database-properties/db:geospatial-element-indexes/db:geospatial-element-index" />
    </xsl:copy>
  </xsl:template>

  <xsl:template match="db:geospatial-element-attribute-pair-indexes">
    <xsl:copy>
      <xsl:apply-templates />
      <xsl:apply-templates
        select="$mergePackage/db:config/db:package-database-properties/db:geospatial-element-attribite-pair-indexes/db:geospatial-element-attribute-pair-index" />
    </xsl:copy>
  </xsl:template>

  <xsl:template match="db:links">
    <xsl:copy>
      <xsl:copy-of select="attribute::*" />
      <xsl:apply-templates />
      <xsl:if test="not(db:triggers-database)">
        <xsl:copy-of select="$mergePackage/db:config/db:links/db:triggers-database" />
      </xsl:if>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="db:links/node()[text()]">
    <xsl:variable name="nodeName" select="name(.)" />
    <xsl:variable name="mergeNode" select="$mergePackage/db:config/db:links/node()[name(.) = $nodeName]" />
    <xsl:choose>
      <xsl:when test="$mergeNode">
        <xsl:copy-of select="$mergeNode" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:copy-of select="." />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>