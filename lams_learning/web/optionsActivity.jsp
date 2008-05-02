<%@ include file="/common/taglibs.jsp"%>

<c:set var="enableFlash">
	<lams:LearnerFlashEnabled />
</c:set>
<c:if test="${enableFlash}">
	<lams:Passon id="${optionsActivityForm.lessonID}"
		progress="${optionsActivityForm.progressSummary}" />
</c:if>

<script language="JavaScript" type="text/JavaScript"><!--
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
		}
		else {
			return true;
		}
	}
	//-->
</script>

<div id="content">
	<h1>
		<c:out value="${optionsActivityForm.title}" />
	</h1>

	<c:if test="${not empty optionsActivityForm.description}">
		<p>
			<c:out value="${optionsActivityForm.description}" />
		</p>
	</c:if>

	<div class="group-box">

		<p>
			<fmt:message key="message.activity.options.activityCount">
				<fmt:param value="${optionsActivityForm.minimum}" />
				<fmt:param value="${optionsActivityForm.maximum}" />
			</fmt:message>
		</p>

		<html:form action="/ChooseActivity" method="post"
			onsubmit="return validate();">
			<input type="hidden" name="lams_token"
				value="<c:out value='${lams_token}' />" >

			<table class="alternative-color" cellspacing="0">
				<c:forEach items="${optionsActivityForm.activityURLs}"
					var="activityURL" varStatus="loop">
					<tr>
						<td width="2%">
							<c:if test="${not activityURL.complete}">
								<input type="radio" name="activityID" class="noBorder"
									id="activityID-${activityURL.activityId}"
									value="${activityURL.activityId}">
							</c:if>
						</td>
						<td>
							<c:choose>
								<c:when test="${not activityURL.complete}">
									<label for="activityID-${activityURL.activityId}">
										<c:out value="${activityURL.title}" />
									</label>
								</c:when>
								<c:otherwise>
									<c:out value="${activityURL.title}" />
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:forEach>
			</table>

			<div class="align-right small-space-bottom">
				<html:submit styleClass="button">
					<fmt:message key="label.activity.options.choose" />
				</html:submit>
			</div>

		</html:form>
	</div>

	<p class="info">
		<fmt:message key="message.activity.options.note" />
	</p>

	<c:if test="${optionsActivityForm.finished}">
		<html:form action="/CompleteActivity" method="post">
			<input type="hidden" name="lams_token"
				value="<c:out value='${lams_token}' />">
			<input type="hidden" name="activityID"
				value="<c:out value='${optionsActivityForm.activityID}' />">
			<input type="hidden" name="lessonID"
				value="<c:out value='${optionsActivityForm.lessonID}' />">
			<input type="hidden" name="progressID"
				value="<c:out value='${optionsActivityForm.progressID}' />">

			<div class="align-right space-bottom-top">
				<html:submit styleClass="button">
					<fmt:message key="label.finish.button" />
				</html:submit>
			</div>

		</html:form>
	</c:if>

</div>
<!--closes content-->

<div id="footer"></div>
