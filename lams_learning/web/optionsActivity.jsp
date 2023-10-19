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

<lams:PageLearner title="${optionsActivityForm.title}" toolSessionID="" lessonID="${optionsActivityForm.lessonID}">	
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '', ${optionsActivityForm.activityID}, finishActivity);
		
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
				alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='message.activity.options.noActivitySelected' /></spring:escapeBody>");
				return false;
			} else {
				return true;
			}
		}
		
		function finishActivity() {
			document.getElementById('messageForm').submit();
		}
	</script>

	<div id="container-main">
		<c:if test="${not empty optionsActivityForm.description}">
			<div id="instructions" class="instructions">
				<c:out value="${optionsActivityForm.description}" />
			</div>
		</c:if>

		<c:if test="${optionsActivityForm.minimum != 0 && !optionsActivityForm.minimumLimitReached}">
			<lams:Alert5 id="min-liimt" close="false" type="danger">	
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
			</lams:Alert5>
		</c:if>
								
		<c:if test="${optionsActivityForm.maximum != 0}">
			<lams:Alert5 id="max-limit" type="danger" close="false">
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
			</lams:Alert5>
		</c:if>
		
		<c:if test="${optionsActivityForm.hasCompletedActivities}">
			<lams:Alert5 id="can-revisit" type="info" close="true">
				<c:choose>
					<c:when test="${isOptionsWithSequencesActivity}">
						<fmt:message key="message.activity.set.options.note" />
					</c:when>
					<c:otherwise>
						<fmt:message key="message.activity.options.note" />
					</c:otherwise>
				</c:choose>
			</lams:Alert5>
		</c:if>

		<form:form action="ChooseActivity.do" modelAttribute="activityForm" method="post" onsubmit="return validate();">
			<input type="hidden" name="lams_token" value="<c:out value='${lams_token}' />">
			
			<fieldset>
				<legend class="visually-hidden">
					<fmt:message key="message.activity.options.noActivitySelected" />
				</legend>
		
				<div class="ltable table-stripped">
					<c:forEach items="${optionsActivityForm.activityURLs}" var="activityURL">
						<div class="row">
							<div class="col">
								<c:choose>
									<c:when test="${not activityURL.complete and not optionsActivityForm.maxActivitiesReached}">
										<div class="form-check">
											<input type="radio" name="activityID" class="form-check-input" id="activityID-${activityURL.activityId}"
													value="${activityURL.activityId}" onchange="$('#choose-branch-button').prop('disabled', false);">
											<label class="form-check-label" for="activityID-${activityURL.activityId}">
												<c:out value="${activityURL.title}" />
											</label>
										</div>
									</c:when>
									
									<c:when test="${activityURL.complete}">
										<div class="form-check">
											<i class="fa fa-lg fa-check-circle text-success radio-button-offset" style="cursor: auto;"></i>
										
											<c:choose>
												<c:when test="${not empty activityURL.url}">
													<a href="${activityURL.url}"><c:out value="${activityURL.title}" /></a>
												</c:when>
												
												<c:when test="${empty activityURL.url}"><!-- sequence activity -->
													<span class="text-muted">
														<c:out value="${activityURL.title}" />
													</span>
													
													<div class="ms-4">
														<c:forEach items="${activityURL.childActivities}" var="childActivityURL">
															<div>
																<a href="${childActivityURL.url}"><c:out value="${childActivityURL.title}" /></a>
															</div>
														</c:forEach>
													</div>
												</c:when>
											</c:choose>										
										</div>
									</c:when>
											
									<c:otherwise>
										<div class="form-check ms-3">
											<c:out value="${activityURL.title}" />
										</div>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</c:forEach>
				</div>
			</fieldset>
		
			<div class="float-end mb-3">
				<c:if test="${!optionsActivityForm.maxActivitiesReached}">
					<button id="choose-branch-button" class="btn btn-secondary" disabled="disabled">
						<fmt:message key="label.activity.options.choose" />
					</button>						
				</c:if>
			</div>
		
		</form:form>
		
		<c:if test="${optionsActivityForm.minimumLimitReached or isPreview}">
			<div class="activity-bottom-buttons">
				<form:form action="/lams/learning/CompleteActivity.do" modelAttribute="messageForm" method="post" id="messageForm">
					<input type="hidden" name="lams_token" value="<c:out value='${lams_token}' />">
					<input type="hidden" name="activityID" value="<c:out value='${optionsActivityForm.activityID}' />">
					<input type="hidden" name="lessonID" value="<c:out value='${optionsActivityForm.lessonID}' />">
					<input type="hidden" name="progressID" value="<c:out value='${optionsActivityForm.progressID}' />">
				
					<button type="submit" class="btn btn-primary na" id="finishButton">
						<c:choose>
							<c:when test="${activityPosition.last}">
								<fmt:message key="label.submit.button" />
							</c:when>
							<c:otherwise>
								<fmt:message key="label.finish.button" />
							</c:otherwise>
						</c:choose>
					</button>
				</form:form>
			</div>
		</c:if>
			
	</div>
</lams:PageLearner>
