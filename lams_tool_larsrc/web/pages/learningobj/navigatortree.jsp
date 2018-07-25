<%-- 
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
--%>

<!DOCTYPE html>


<%@ include file="/common/taglibs.jsp"%>
<lams:html>
<lams:head>
<!--<html:base />  -->	
	<%@ include file="/common/header.jsp"%>

	<c:set var="sessionMapID" value="${param.sessionMapID}" />
	<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
	<c:set var="cpPackage" value="${sessionMap.cpPackage}" />

	<style type="text/css">
		table {
			margin-left: 0px;
			text-align: left;
		}
		
		td {
			padding: 0px;
			font-size: 12px;
		}
		
		a, A:link, a:visited, a:active {
			color: #0000aa;
			text-decoration: none;
			font-family: Tahoma, Verdana;
			font-size: 15px
		}
		
		A:hover {
			color: #ff0000;
			text-decoration: none;
			font-family: Tahoma, Verdana;
			font-size: 15px
		}
</style>
	<script type="text/javascript">
      	parent.contentFrame.location = "<c:url value="/download/?uuid=${cpPackage.fileUuid}&preferDownload=false"/>";
      	parent.contentFrame.focus();
      	
        <c:set var="downloadUrlPrefix">
        	<c:url value="/download/${cpPackage.fileUuid}/${cpPackage.fileVersionId}/"/>
        </c:set>
        <c:set var="downloadUrlSuffix">
        	<c:url value="?preferDownload=false"/>
        </c:set>
		<x:parse xml="${cpPackage.organizationXml}" var="xml"/>
		<c:import var="xsl" url="xmltree.xsl"/>
		<x:transform xml="${xml}" xslt="${xsl}">
 				<x:param name="urlPrefix" value="${downloadUrlPrefix}"/>
 				<x:param name="urlSuffix" value="${downloadUrlSuffix}"/>
 			</x:transform>
  	  </script>
	<script type="text/javascript"
		src="<lams:LAMSURL/>includes/javascript/tree.js"></script>
	<script type="text/javascript"
		src="<lams:LAMSURL/>includes/javascript/tree_tpl.js"></script>
</lams:head>
<body class="stripes">
	<c:choose>
		<c:when test="${empty cpPackage.organizationXml}">
			<p class="body">The content package details are missing.</p>
		</c:when>
		<c:otherwise>
			<h2>
				<c:out value="${cpPackage.title}" />
			</h2>
			<c:out value="${cpPackage.description}" escapeXml="false" />
			<BR>
			<BR>
			<script type="text/javascript">
				new tree (TREE_ITEMS, TREE_TPL);
			</script>
		</c:otherwise>
	</c:choose>
</body>
</lams:html>