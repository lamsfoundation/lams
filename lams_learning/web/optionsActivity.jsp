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
<%
	if (request.getAttribute("activity") instanceof org.lamsfoundation.lams.learningdesign.OptionsWithSequencesActivity) {
		request.setAttribute("isOptionsWithSequencesActivity", "true");
	}
%>
<c:set var="lams"><lams:LAMSURL /></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="learner.title" /></title>
	<lams:css />

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/JavaScript">
		function validate() {
			var validated = false;
		
			var form = document.forms[0];
			var elements = form.elements;
			for (var i = 0; i < elements.length; i++) {
				if (elements[i].name == "activityID") {
					if (elements[i].checked) {
						validated = true;
						break;
					}
				}
			}
			if (!validated) {
				alert("<fmt:message key="message.activity.options.noActivitySelected" />");
				return false;
			} else {
				return true;
			}
		}
		function finishActivity() {
			document.getElementById('messageForm').submit();
		}
	</script>
</lams:head>
<body class="stripes">
	<lams:Page type="learner" title="${optionsActivityForm.title}">
		
		<c:if test="${not empty optionsActivityForm.description}">
			<div class="panel">
				<c:out value="${optionsActivityForm.description}" />
			</div>
		</c:if>

		<c:if test="${optionsActivityForm.minimum != 0 && !optionsActivityForm.minimumLimitReached}">
			<lams:Alert id="min-liimt" close="false" type="danger">	
				<c:choose>
					<c:when test="${isOptionsWithSequencesActivity}">
						<fmt:message key="message.activity.set.options.activityCount">
							<fmt:param value="${optionsActivityForm.minimum}" />
						</fmt:message>
					</c:when>
					<c:otherwise>
						<fmt:message key="message.activity.options.activityCount">
							<fmt:param value="${optionsActivityForm.minimum}" />
						</fmt:message>
					</c:otherwise>
				</c:choose>
			</lams:Alert>
		</c:if>
								
		<c:if test="${optionsActivityForm.maximum != 0}">
			<lams:Alert id="max-limit" type="danger" close="false">
				<c:choose>
					<c:when test="${isOptionsWithSequencesActivity}">
						<fmt:message key="message.activity.set.options.note.maximum">
							<fmt:param value="${optionsActivityForm.maximum}" />
						</fmt:message>
					</c:when>
					<c:otherwise>
						<fmt:message key="message.activity.options.note.maximum">
							<fmt:param value="${optionsActivityForm.maximum}" />
						</fmt:message>
					</c:otherwise>
				</c:choose>
			
				<c:if test="${optionsActivityForm.maxActivitiesReached}">
					<c:choose>
						<c:when test="${isOptionsWithSequencesActivity}">
							<fmt:message key="label.optional.maxSequencesReached" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.optional.maxActivitiesReached" />
						</c:otherwise>
					</c:choose>
				</c:if>
			</lams:Alert>
		</c:if>
		
		<c:if test="${optionsActivityForm.hasCompletedActivities}">
			<lams:Alert id="can-revisit" type="info" close="true">
				<c:choose>
					<c:when test="${isOptionsWithSequencesActivity}">
						<fmt:message key="message.activity.set.options.note" />
					</c:when>
					<c:otherwise>
						<fmt:message key="message.activity.options.note" />
					</c:otherwise>
				</c:choose>
			</lams:Alert>
		</c:if>
		
		<div class="group-box">
			<form:form action="ChooseActivity.do" modelAttribute="activityForm" method="post" onsubmit="return validate();">
				<input type="hidden" name="lams_token" value="<c:out value='${lams_token}' />">
		
				<div class="options">
					<table class="table table-condensed table-hover">
						<c:forEach items="${optionsActivityForm.activityURLs}" var="activityURL">
							<tr><td>

								<c:choose>
									<c:when test="${not activityURL.complete and not optionsActivityForm.maxActivitiesReached}">
										<div class="radio">
											<label>
												<input type="radio" name="activityID" class="noBorder" id="activityID-${activityURL.activityId}"
													value="${activityURL.activityId}" onchange="$('#choose-branch-button').prop('disabled', false);">
												<c:out value="${activityURL.title}" />
											</label>
										</div>
									</c:when>
									
									<c:when test="${activityURL.complete}">
										<div class="radio loffset20">
											<i class="fa fa-lg fa-check-circle text-success radio-button-offset" style="cursor: auto;"></i>
										
											<c:choose>
												<c:when test="${not empty activityURL.url}">
													<a href="${activityURL.url}"><c:out value="${activityURL.title}" /></a>
												</c:when>
												
												<c:when test="${empty activityURL.url}"><!-- sequence activity -->
													<span class="text-muted">
														<c:out value="${activityURL.title}" />
													</span>
													
													<div class="loffset20">
														<c:forEach items="${activityURL.childActivities}" var="childActivityURL">
															<a href="${childActivityURL.url}"><c:out value="${childActivityURL.title}" /></a>
															<br>
														</c:forEach>
													</div>
												</c:when>
											</c:choose>										
										</div>
									</c:when>
											
									<c:otherwise>
										<div class="radio loffset20">
											<c:out value="${activityURL.title}" />
										</div>
									</c:otherwise>
								</c:choose>
							</td></tr>
						</c:forEach>
					</table>
				</div>
		
				<div align="center" class="voffset10">
					<c:if test="${!optionsActivityForm.maxActivitiesReached}">
						<button id="choose-branch-button" class="btn btn-default" disabled="disabled">
							<fmt:message key="label.activity.options.choose" />
						</button>						
					</c:if>
				</div>
		
			</form:form>
		</div>
		
		<c:if test="${optionsActivityForm.minimumLimitReached or isPreview}">
			<form:form action="/lams/learning/CompleteActivity.do" modelAttribute="messageForm" method="post" id="messageForm">
				<input type="hidden" name="lams_token" value="<c:out value='${lams_token}' />">
				<input type="hidden" name="activityID" value="<c:out value='${optionsActivityForm.activityID}' />">
				<input type="hidden" name="lessonID" value="<c:out value='${optionsActivityForm.lessonID}' />">
				<input type="hidden" name="progressID" value="<c:out value='${optionsActivityForm.progressID}' />">
		
				<hr class="msg-hr">
		
				<div class="voffset10">
					<a href="javascript:;" class="btn btn-primary pull-right na" id="finishButton"
						onclick="finishActivity()">
						<c:choose>
							<c:when test="${activityPosition.last}">
								<fmt:message key="label.submit.button" />
							</c:when>
							<c:otherwise>
								<fmt:message key="label.finish.button" />
							</c:otherwise>
						</c:choose>
					</a>
				</div>
		
			</form:form>
		</c:if>
			
	</lams:Page>
</body>
</lams:html>

