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

<lams:html xhtml="true">
	<lams:head>
		<title><fmt:message key="label.view.groups.title"/></title>
		<lams:css/>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
		<META HTTP-EQUIV="Refresh" CONTENT="60;URL=<lams:WebAppURL/>grouping/performGrouping.do?activityID=${groupingForm.activityID}">
	</lams:head>

	<body class="stripes">
		<lams:Page type="learner" title="${groupingForm.title}">
			<form:form action="performGrouping.do" modelAttribute="groupingForm" target="_self">
				<input type="hidden" name="activityID" value="${groupingForm.activityID}" />
				<input type="hidden" name="force" value="${groupingForm.previewLesson}" />
				
		
				<lams:Alert id="waitingGroups" close="false" type="info">
					<fmt:message key="label.view.view.groups.wait.message" />
				</lams:Alert>
		
				<c:if test="${groupingForm.previewLesson == true}">
					<div class="voffset10">
						<em><fmt:message key="label.grouping.preview.message" /></em>
					</div>
				</c:if>
		
				<div class="right-buttons">
					<button class="btn btn-primary pull-right">
						<fmt:message key="label.next.button" />
					</button>
				</div>
		
			</form:form>
		</lams:Page>
	</body>

</lams:html>
