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
	<title><fmt:message key="learner.title" />
	</title>

	<lams:css />
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript"
		src="${lams}includes/javascript/common.js"></script>
</lams:head>

	<body class="stripes">
		<%
			if (request.getAttribute("activity") instanceof org.lamsfoundation.lams.learningdesign.OptionsWithSequencesActivity) {
				request.setAttribute("isOptionsWithSequencesActivity", "true");
			}
		%>
		
		<script language="JavaScript" type="text/JavaScript">
		<!--
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
			function submitForm(methodName) {
				var f = document.getElementById('messageForm');
				f.submit();
			}
		//-->
		</script>
		
		<lams:Page type="learner" title="${optionsActivityForm.title}">
		
			<c:if test="${not empty optionsActivityForm.description}">
				<div class="panel">
					<c:out value="${optionsActivityForm.description}" />
				</div>
			</c:if>
		
			<div class="group-box">
		
				<form:form action="ChooseActivity.do mo`" modelAttribute="activityForm" method="post" onsubmit="return validate();">
					<input type="hidden" name="lams_token" value="<c:out value='${lams_token}' />">
		
					<div class="options">
		
						<c:if test="${optionsActivityForm.maximum != numActivities or optionsActivityForm.minimum != 0}">
							<lams:Alert id="options" close="false" type="danger">
								<c:choose>
									<c:when test="${isOptionsWithSequencesActivity}">
										<fmt:message key="message.activity.set.options.activityCount">
											<fmt:param value="${optionsActivityForm.minimum}" />
											<fmt:param value="${optionsActivityForm.maximum}" />
										</fmt:message>
									</c:when>
									<c:otherwise>
										<fmt:message key="message.activity.options.activityCount">
											<fmt:param value="${optionsActivityForm.minimum}" />
											<fmt:param value="${optionsActivityForm.maximum}" />
										</fmt:message>
									</c:otherwise>
								</c:choose>
							</lams:Alert>
						</c:if>
		
						<c:forEach items="${optionsActivityForm.activityURLs}" var="activityURL" varStatus="loop">
							<c:set var="numActivities" value="${loop.count}" />
							<div class="form-group">
								<div class="radio">
									<label> <c:choose>
											<c:when test="${not activityURL.complete and not optionsActivityForm.maxActivitiesReached}">
												<input type="radio" name="activityID" class="noBorder" id="activityID-${activityURL.activityId}"
													value="${activityURL.activityId}">
											</c:when>
											<c:when test="${activityURL.complete}">
												<i class="fa fa-lg fa-check text-success radio-button-offset"></i>
											</c:when>
										</c:choose> <c:choose>
											<c:when test="${activityURL.complete}">
												<a href="${activityURL.url}"><c:out value="${activityURL.title}" /></a>
											</c:when>
											<c:otherwise>
												<c:out value="${activityURL.title}" />
											</c:otherwise>
										</c:choose>
									</label>
								</div>
							</div>
						</c:forEach>
					</div>
		
					<div align="center" class="voffset10">
						<c:choose>
							<c:when test="${optionsActivityForm.maxActivitiesReached}">
								<c:choose>
									<c:when test="${isOptionsWithSequencesActivity}">
										<lams:Alert id="limits" type="info" close="false">
											<fmt:message key="label.optional.maxSequencesReached" />
										</lams:Alert>
									</c:when>
									<c:otherwise>
										<lams:Alert id="maxReached" type="danger" close="false">
											<fmt:message key="label.optional.maxActivitiesReached" />
										</lams:Alert>
									</c:otherwise>
								</c:choose>
		
							</c:when>
							<c:otherwise>
								<button class="btn btn-default">
									<fmt:message key="label.activity.options.choose" />
								</button>
							</c:otherwise>
						</c:choose>
					</div>
		
				</form:form>
			</div>
		
		
		
			<c:if test="${optionsActivityForm.finished}">
				<script language="JavaScript" type="text/JavaScript">
				<!--
					function submitForm(methodName) {
						var f = document.getElementById('messageForm');
						f.submit();
					}
				//-->
				</script>
		
				<form:form action="CompleteActivity.do" modelAttribute="messageForm" method="post" id="messageForm">
					<input type="hidden" name="lams_token" value="<c:out value='${lams_token}' />">
					<input type="hidden" name="activityID" value="<c:out value='${optionsActivityForm.activityID}' />">
					<input type="hidden" name="lessonID" value="<c:out value='${optionsActivityForm.lessonID}' />">
					<input type="hidden" name="progressID" value="<c:out value='${optionsActivityForm.progressID}' />">
		
					<p class="voffset10 help-text">
						<c:choose>
							<c:when test="${optionsActivityForm.maximum gt 0 and optionsActivityForm.maximum lt numActivities}">
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
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${isOptionsWithSequencesActivity}">
										<fmt:message key="message.activity.set.options.note" />
									</c:when>
									<c:otherwise>
										<fmt:message key="message.activity.options.note" />
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</p>
		
		
					<hr class="msg-hr">
		
					<div class="voffset10">
						<a href="javascript:;" class="btn btn-primary pull-right na" id="finishButton"
							onclick="submitForm('finish')">
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

