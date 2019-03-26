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

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<!DOCTYPE html>
<lams:html xhtml="true">

<lams:head>
	<META HTTP-EQUIV="Refresh"
		CONTENT="60;URL=<lams:WebAppURL/>/gate/knockGate.do?activityID=${gateForm.activityID}&lessonID=${gateForm.lessonID }">

	<c:set var="title"><fmt:message key="label.synch.gate.title"/></c:set>
	<title><c:out value="${title}" /></title>
	
	<lams:css />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.timeago.js"></script>
</lams:head>

<body class="stripes">
	<lams:Page type="learner" title="${title}">

		<%@ include file="gateDescription.jsp"%>
		
		<lams:Alert type="info" close="false" id="waitingAtTheGate">
			<fmt:message key="label.gate.waiting.learners">
				<fmt:param value="${gateForm.waitingLearners}" />
				<fmt:param value="${gateForm.totalLearners}" />
			</fmt:message>
		</lams:Alert>

		<div class="voffset5">
			<fmt:message key="label.synch.gate.message" />
		</div>

		<%@ include file="../gate/gateNext.jsp"%>

	</lams:Page>
</body>

</lams:html>
