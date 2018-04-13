<%@ include file="/common/taglibs.jsp"%>

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

		<html:form action="/ChooseActivity" method="post" onsubmit="return validate();">
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
						<html:submit styleClass="btn btn-default">
							<fmt:message key="label.activity.options.choose" />
						</html:submit>
					</c:otherwise>
				</c:choose>
			</div>

		</html:form>
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

		<html:form action="/CompleteActivity" method="post" styleId="messageForm">
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
				<html:link href="javascript:;" styleClass="btn btn-primary pull-right na" styleId="finishButton"
					onclick="submitForm('finish')">
					<c:choose>
						<c:when test="${activityPosition.last}">
							<fmt:message key="label.submit.button" />
						</c:when>
						<c:otherwise>
							<fmt:message key="label.finish.button" />
						</c:otherwise>
					</c:choose>
				</html:link>
			</div>

		</html:form>
	</c:if>
</lams:Page>