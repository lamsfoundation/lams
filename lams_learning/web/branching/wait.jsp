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
		<title><fmt:message key="label.branching.title"/></title>
		<lams:css/>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
		<c:set var="formAction">/branching/performBranching.do?type=${branchingForm.type}&activityID=${branchingForm.activityID}&progressID=${branchingForm.progressID}</c:set>
		<c:if test="${branchingForm.previewLesson == true}">
			<c:set var="formAction"><c:out value="${formAction}"/>&amp;force=true</c:set>
		</c:if>
		<META HTTP-EQUIV="Refresh" CONTENT="60;URL=<lams:WebAppURL/>${formAction}">
	  </lams:head>

	<body class="stripes">
	
		<c:set var="formAction">performBranching.do?type=${branchingForm.type}&activityID=${branchingForm.activityID}&progressID=${branchingForm.progressID}</c:set>
	
		<form:form action="${formAction}" modelAttribute="branchingForm" target="_self">
	
			<c:set var="title"><c:out value="${branchingForm.title}" /></c:set>
			<lams:Page type="learner" title="${title}">
	
				<p><fmt:message key="label.branching.wait.message"/></p>
				<p><fmt:message key="label.branching.refresh.message"/></p>
	
				<button class="btn btn-default pull-right"><fmt:message key="label.next.button"/></button>
	
				<div id="footer"></div>
			</lams:Page>
	
		</form:form>
	</body>

</lams:html>
