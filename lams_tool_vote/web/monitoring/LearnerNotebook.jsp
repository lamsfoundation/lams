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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<html:html>
<head>
	<html:base />
	<lams:headItems />
	<title><bean:message key="activity.title" /></title>
	
</head>

<body>
	<div id="page-learner">
	
	<h1 class="no-tabs-below">
		<bean:message key="label.view.reflection"/>
	</h1>
	
	
	<div id="header-no-tabs-learner"></div>
	
	<html:form  action="/learning?validate=false" enctype="multipart/form-data" method="POST" target="_self">		
	
		<div id="content-learner">
		
			<table>
				<tr>
					<td>
						<h2>
							<c:out value="${voteGeneralLearnerFlowDTO.userName}" escapeXml="false"/>
						</h2>
					</td>
				</tr>
				<tr>
					<td>
						<p><c:out value="${voteGeneralLearnerFlowDTO.notebookEntry}" escapeXml="false"/></p>
					</td>
				</tr>
			</table>

			<table cellpadding="0">
				<tr>
					<td>
						<a href="javascript:window.close();" class="button">
							<bean:message key="label.close"/></a>
					</td>
				</tr>
			</table>

		
		</div>
	</html:form>	
	
	<div id="footer-learner"></div>


</div>
</body>
</html:html>








