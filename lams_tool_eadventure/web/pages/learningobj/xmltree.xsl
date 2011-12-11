<?xml version="1.0"?>

<!-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt 
-->

   <xsl:stylesheet 
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
      version="1.0">

   <xsl:output method="html"></xsl:output>
   
   <xsl:param name="urlPrefix"/>
   <xsl:param name="urlSuffix"/>

   <xsl:template match="/">
      <xsl:apply-templates/>
   </xsl:template>
   
   <xsl:template match="organizations">
var TREE_ITEMS = [
      		<xsl:apply-templates/>
];
   </xsl:template>
   

   <xsl:template match="item">
	<xsl:choose>
		<xsl:when test="@resource">
			['<xsl:value-of select="@title" />','<xsl:value-of select="$urlPrefix" /><xsl:value-of select="@resource"/><xsl:value-of select="$urlSuffix" />',
		    <xsl:apply-templates/>
		    ],
		</xsl:when>
		<xsl:otherwise>
		    ['<xsl:value-of select="@title" />',0,
		    <xsl:apply-templates/>
		    ],		    
		</xsl:otherwise>
	</xsl:choose>
   </xsl:template>
   
   </xsl:stylesheet>