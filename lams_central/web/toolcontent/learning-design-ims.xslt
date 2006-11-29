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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:lams="http://www.lamsfoundation.org/xsd/lams_ims_export_v1p0.xsd"  xmlns="http://www.imsglobal.org/xsd/imscp_v1p1" version="1.0">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:param name="lamsLanguage"/>
	<xsl:param name="resourcesFile"/>
	<xsl:param name="transitionsFile"/>
	<xsl:template match="/">
		<manifest xmlns="http://www.imsglobal.org/xsd/imscp_v1p1" xsl:use-attribute-sets="ldIdentifier">
			<metadata>
				<schema>IMS Metadata</schema>
				<schemaversion>1.2</schemaversion>
			</metadata>
			<organizations default="default">
				<learning-design identifier="default" uri="" level="A" xmlns="http://www.imsglobal.org/xsd/imsld_v1p0" xmlns:lams="http://www.lamsfoundation.org/xsd/lams_ims_export_v1p0.xsd">
					<title>
						<lams:langstring>
							<xsl:value-of select="*/title"/>
						</lams:langstring>
					</title>
					<!-- ================================== lams LD ================================== -->
					<xsl:for-each select="/*/*">
						<xsl:if test="(count(ancestor::*) =1) and (name(.) !='activities') and (name(.) != 'transitions')">
								<xsl:element name="lams:{local-name()}" namespace="http://www.lamsfoundation.org/xsd/lams_ims_export_v1p0.xsd"><xsl:copy-of select="@*"/><xsl:value-of select="."/></xsl:element>
						</xsl:if>
					</xsl:for-each>

					<components>
						<roles>
							<learner identifier="Learner">
								<title>Learner</title>
							</learner>
						</roles>
						<activities>
							<!-- ================================== lams activities ================================== -->
							<xsl:apply-templates select="*//org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO" mode="tool"/>
							<xsl:apply-templates select="*//org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO" mode="group"/>
							<xsl:apply-templates select="*//org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO" mode="gate"/>
							<xsl:apply-templates select="*//org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO" mode="complex"/>
							<!-- ================================== lams Tansitions ================================== -->
							<xsl:apply-templates select="*/transitions"/>
						</activities>
						<!-- ================================== Tools' content ================================== -->
						<xsl:apply-templates select="*/activities" mode="env"/>
					</components>
					<method>
						<play>
							<act>
								<role-part>
									<role-ref ref="Learner"/>
									<activity-structure-ref ref="A-Sequence"/>
								</role-part>
							</act>
						</play>
					</method>
				</learning-design>
			</organizations>
			<resources>
				<xsl:copy-of select="document($resourcesFile)"/>
			</resources>
		</manifest>
	</xsl:template>
	
	<!-- ================================== Tempaltes ================================== -->
	
	<!-- ================================== lams activities ================================== -->
	<xsl:template match="org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO" mode="tool">
		<xsl:if test="(activityTypeID = 1)">
			<xsl:call-template name="tool"/>
		</xsl:if>
		<xsl:apply-templates select="org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO" mode="tool"/>
	</xsl:template>
	<xsl:template match="org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO" mode="group">
		<xsl:if test="(activityTypeID = 2)">
			<xsl:call-template name="group"/>
		</xsl:if>
		<xsl:apply-templates select="org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO" mode="group"/>
	</xsl:template>
	<xsl:template match="org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO" mode="gate">
		<xsl:if test="(activityTypeID = 3) or (activityTypeID = 4) or (activityTypeID = 5)">
			<xsl:call-template name="gate"/>
		</xsl:if>
		<xsl:apply-templates select="org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO" mode="gate"/>
	</xsl:template>
	<xsl:template match="org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO" mode="complex">
		<xsl:if test="(activityTypeID = 6) or (activityTypeID = 7) or (activityTypeID = 8)">
			<xsl:call-template name="complex"/>
		</xsl:if>
		<xsl:apply-templates select="org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO" mode="complex"/>
	</xsl:template>

	<!-- ================================== lams Tool activities ================================== -->
	<xsl:template name="tool">
		<learning-activity xsl:use-attribute-sets="toolIdentifier">
			<title>
				<xsl:value-of select="activityTitle"/>
			</title>
			<environment-ref xsl:use-attribute-sets="toolEnvRef"/>
			<complete-activity>
				<user-choice/>
			</complete-activity>
			<lams:lams-tool-activity>
				<xsl:for-each select="node() | @*">
					<xsl:element name="lams:{local-name()}" namespace="http://www.lamsfoundation.org/xsd/lams_ims_export_v1p0.xsd"><xsl:copy-of select="@*"/><xsl:value-of select="."/></xsl:element>
				</xsl:for-each>
			</lams:lams-tool-activity>
		</learning-activity>
	</xsl:template>
	<!-- ================================== lams Grouping activities ================================== -->
	<xsl:template name="group">
		<learning-activity xsl:use-attribute-sets="groupIdentifier">
			<title>
				<xsl:value-of select="activityTitle"/>
			</title>
			<environment-ref xsl:use-attribute-sets="groupEnvRef"/>
			<complete-activity>
				<user-choice/>
			</complete-activity>
			<lams:lams-tool-activity>
				<xsl:for-each select="node() | @*">
					<xsl:element name="lams:{local-name()}" namespace="http://www.lamsfoundation.org/xsd/lams_ims_export_v1p0.xsd"><xsl:copy-of select="@*"/><xsl:value-of select="."/></xsl:element>
				</xsl:for-each>
			</lams:lams-tool-activity>
		</learning-activity>
	</xsl:template>
	<!-- ================================== lams Gate activities ================================== -->
	<xsl:template name="gate">
		<learning-activity xsl:use-attribute-sets="gateIdentifier">
			<title>
				<xsl:value-of select="activityTitle"/>
			</title>
			<environment-ref xsl:use-attribute-sets="gateEnvRef"/>
			<complete-activity>
				<user-choice/>
			</complete-activity>
			<lams:lams-tool-activity>
				<xsl:for-each select="node() | @*">
					<xsl:element name="lams:{local-name()}" namespace="http://www.lamsfoundation.org/xsd/lams_ims_export_v1p0.xsd"><xsl:copy-of select="@*"/><xsl:value-of select="."/></xsl:element>
				</xsl:for-each>
			</lams:lams-tool-activity>
		</learning-activity>
	</xsl:template>
	<!-- ================================== lams Complex activities ================================== -->
	<xsl:template name="complex">
		<activity-structure xsl:use-attribute-sets="complexAttr">
			<title>
				<xsl:value-of select="activityTitle"/>
			</title>
			<xsl:variable name="myid" select="activityID"/>
			<xsl:for-each select="/*/*/*">
				<xsl:sort select="orderID" order="ascending"/>
				<xsl:if test="parentActivityID = $myid">
					<xsl:if test="(activityTypeID = 1)">
						<learning-activity-ref  xsl:use-attribute-sets="toolRef"/>
					</xsl:if>
					<xsl:if test="(activityTypeID = 2)">
						<learning-activity-ref  xsl:use-attribute-sets="groupRef"/>
					</xsl:if>
					<xsl:if test="(activityTypeID = 6) or (activityTypeID = 7) or (activityTypeID = 8)">
						<learning-activity-ref  xsl:use-attribute-sets="complexRef"/>
					</xsl:if>
				</xsl:if>
			</xsl:for-each>
			<lams:lams-complex-activity>
				<xsl:for-each select="node() | @*">
					<xsl:element name="lams:{local-name()}" namespace="http://www.lamsfoundation.org/xsd/lams_ims_export_v1p0.xsd"><xsl:copy-of select="@*"/><xsl:value-of select="."/></xsl:element>
				</xsl:for-each>
			</lams:lams-complex-activity>
		</activity-structure>
	</xsl:template>
	<!-- ================================== lams Transitions ================================== -->
	<xsl:template match="transitions">
		<activity-structure identifier="A-sequence" structure-type="sequence">
			<title>LAMS Learning design sequence</title>
			<!-- copy sorted transition learning-activity-ref -->
			<xsl:copy-of select="document($transitionsFile)/*/*"/>
			<lams:transitions>
				<xsl:apply-templates select="org.lamsfoundation.lams.learningdesign.dto.TransitionDTO"/>
			</lams:transitions>
		</activity-structure>
	</xsl:template>
	
	<xsl:template match="org.lamsfoundation.lams.learningdesign.dto.TransitionDTO">
		<lams:transition>
			<xsl:for-each select="node() | @*">
				<xsl:element name="lams:{local-name()}" namespace="http://www.lamsfoundation.org/xsd/lams_ims_export_v1p0.xsd"><xsl:copy-of select="@*"/><xsl:value-of select="."/></xsl:element>
			</xsl:for-each>
		</lams:transition>
		<xsl:apply-templates select="org.lamsfoundation.lams.learningdesign.dto.TransitionDTO"/>
	</xsl:template>
	<!-- ================================== Tools' content  Environments ================================== -->
	<xsl:template match="activities" mode="env">
		<environments>
			<xsl:apply-templates select="org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO" mode="env"/>
		</environments>
	</xsl:template>
	<xsl:template match="org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO" mode="env">
		<xsl:if test="(activityTypeID = 2)">
			<environment xsl:use-attribute-sets="groupEnvIdentifier"></environment>
		</xsl:if>
		<xsl:if test="(activityTypeID = 3) or (activityTypeID = 4) or (activityTypeID = 5)">
			<environment xsl:use-attribute-sets="gateEnvIdentifier"></environment>
		</xsl:if>
		<xsl:if test="(activityTypeID = 1)">
			<environment xsl:use-attribute-sets="toolEnvIdentifier">
				<title>
					<xsl:value-of select="activityTitle"/>
				</title>
				<service  xsl:use-attribute-sets="serviceAttr">
					<tool_interface xmlns="http://www.lamsfoundation.org/xsd/lams_ims_export_v1p0.xsd">
						<tool_id>
							<identifier type="URN">URN:LAMS:<xsl:value-of select="toolSignature"/>-<xsl:value-of select="toolContentID"/></identifier>
						</tool_id>
						<tool_version><xsl:value-of select="toolVersion"/></tool_version>
						<tool_contents>
							<xsl:variable name="filename">
								<xsl:value-of select="toolContentID"/>.xml
							</xsl:variable>
							<xsl:copy-of select="document($filename)"/>
						</tool_contents>
					</tool_interface>
				</service>
			</environment>
		</xsl:if>
	</xsl:template>
	<!-- ================================== some attributes ================================== -->	
	<xsl:attribute-set name="ldIdentifier">
		<xsl:attribute name="identifier"><xsl:value-of select="/*/title"/></xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="toolIdentifier">
		<xsl:attribute name="identifier">A-<xsl:value-of select="toolSignature"/>-<xsl:value-of select="toolContentID"/></xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="toolRef">
		<xsl:attribute name="ref">A-<xsl:value-of select="toolSignature"/>-<xsl:value-of select="toolContentID"/></xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="groupIdentifier">
		<xsl:attribute name="identifier">A-group-<xsl:value-of select="activityID"/></xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="groupRef">
		<xsl:attribute name="ref">A-group-<xsl:value-of select="activityID"/></xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="gateIdentifier">
		<xsl:attribute name="identifier">A-gate-<xsl:value-of select="activityID"/></xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="gateRef">
		<xsl:attribute name="ref">A-gate-<xsl:value-of select="activityID"/></xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="complexAttr">
		<xsl:attribute name="identifier">S-<xsl:if test="activityTypeID=6">PARALLEL</xsl:if><xsl:if test="activityTypeID=7">OPTIONS</xsl:if><xsl:if test="activityTypeID=8">SEQUENCE</xsl:if>-<xsl:value-of select="activityID"/></xsl:attribute>
		<xsl:attribute name="structure-type">
			<xsl:if test="activityTypeID=6">PARALLEL</xsl:if>
			<xsl:if test="activityTypeID=7">OPTIONS</xsl:if>
			<xsl:if test="activityTypeID=8">SEQUENCE</xsl:if>
		</xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="complexRef">
		<xsl:attribute name="ref">S-<xsl:if test="activityTypeID=6">PARALLEL</xsl:if><xsl:if test="activityTypeID=7">OPTIONS</xsl:if><xsl:if test="activityTypeID=8">SEQUENCE</xsl:if>-<xsl:value-of select="activityID"/></xsl:attribute>
	</xsl:attribute-set>
	
	
	<xsl:attribute-set name="toolEnvIdentifier">
		<xsl:attribute name="identifier">E-<xsl:value-of select="toolSignature"/>-<xsl:value-of select="toolContentID"/></xsl:attribute>
	</xsl:attribute-set>	
	<xsl:attribute-set name="toolEnvRef">
		<xsl:attribute name="ref">E-<xsl:value-of select="toolSignature"/>-<xsl:value-of select="toolContentID"/></xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="groupEnvIdentifier">
		<xsl:attribute name="identifier">E-group-<xsl:value-of select="activityID"/></xsl:attribute>
	</xsl:attribute-set>	
	<xsl:attribute-set name="groupEnvRef">
		<xsl:attribute name="ref">E-group-<xsl:value-of select="activityID"/></xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="gateEnvIdentifier">
		<xsl:attribute name="identifier">E-gate-<xsl:value-of select="activityID"/></xsl:attribute>
	</xsl:attribute-set>	
	<xsl:attribute-set name="gateEnvRef">
		<xsl:attribute name="ref">E-gate-<xsl:value-of select="activityID"/></xsl:attribute>
	</xsl:attribute-set>
	
	<xsl:attribute-set name="serviceAttr">
		<xsl:attribute name="identifier">S-<xsl:value-of select="toolSignature"/>-<xsl:value-of select="toolContentID"/></xsl:attribute>
		<xsl:attribute name="isvisible">true</xsl:attribute>
	</xsl:attribute-set>	
	
	</xsl:stylesheet>

