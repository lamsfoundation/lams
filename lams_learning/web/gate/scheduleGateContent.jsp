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
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="WebAppURL"><lams:WebAppURL/></c:set>

<lams:PageLearner title="${gateForm.gate.title}" toolSessionID="" lessonID="${gateForm.lessonID}"
		refresh="60;URL=${WebAppURL}/gate/knockGate.do?activityID=${gateForm.activityID}&lessonID=${gateForm.lessonID}">
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.timeago.js"></script>	
	<script type="text/javascript">
	    jQuery(document).ready(function() {
	    	jQuery.timeago.settings.allowFuture = true;
	    	jQuery("time.timeago").timeago();
	    });
	</script>

	<div id="container-main">
		<lams:Alert5 type="info" close="false" id="whenOpens">
			<fmt:message key="label.schedule.gate.open.remaining" />&nbsp;<strong><lams:Date value="${gateForm.startingTime}" timeago="true"/></strong>
		
			<div class="text-center">
				<c:choose>
					<c:when test="${not empty gateForm.reachDate}">
						<fmt:message key="label.schedule.gate.reach" />&nbsp;
						<strong><lams:Date value="${gateForm.reachDate}" /></strong>
					</c:when>
					<c:otherwise>
						<c:if test="${gateForm.startingTime!=null}">
							<fmt:message key="label.schedule.gate.open.message" />&nbsp;<strong><lams:Date value="${gateForm.startingTime}" /></strong>
						</c:if>
						
						<c:if test="${gateForm.endingTime!=null}">
							<fmt:message key="label.schedule.gate.close.message" />
							<lams:Date value="${gateForm.endingTime}" />
						</c:if>
					</c:otherwise>
				</c:choose>
			</div>
		</lams:Alert5>

		<%@ include file="gateDescription.jsp"%>
		<%@ include file="gateNext.jsp"%>
	</div>
</lams:PageLearner>


