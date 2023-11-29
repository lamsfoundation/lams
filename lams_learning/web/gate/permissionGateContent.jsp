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
<c:set var="WebAppURL"><lams:WebAppURL/></c:set>
<c:set var="title">
	<c:choose>
		<c:when test="${not empty gateForm.gate.title}">
			${gateForm.gate.title}
		</c:when>
		<c:otherwise>
			<fmt:message key="label.permission.gate.title"/>
		</c:otherwise>
	</c:choose>
</c:set>

<lams:PageLearner title="${title}" toolSessionID="" lessonID="${gateForm.lessonID}"
		refresh="60;URL=${WebAppURL}/gate/knockGate.do?activityID=${gateForm.activityID}&lessonID=${gateForm.lessonID }">

	<div id="container-main">
		<lams:Alert5 type="info" id="permission-gate">
			<fmt:message key="label.permission.gate.message" />
		</lams:Alert5>
		
		<%@ include file="gateDescription.jsp"%>
		<%@ include file="gateNext.jsp"%>
	</div>
</lams:PageLearner>
