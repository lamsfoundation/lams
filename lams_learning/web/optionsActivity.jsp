<%@ include file="/common/taglibs.jsp"%>

<%
	if (request.getAttribute("activity") instanceof org.lamsfoundation.lams.learningdesign.OptionsWithSequencesActivity) {
		request.setAttribute("isOptionsWithSequencesActivity", "true");
	}
%>

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
         function submitForm(methodName){
                var f = document.getElementById('messageForm');
                f.submit();
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


		<html:form action="/ChooseActivity" method="post"
			onsubmit="return validate();">
			<input type="hidden" name="lams_token"
				value="<c:out value='${lams_token}' />" >

			<table class="alternative-color" cellspacing="0">
				<c:forEach items="${optionsActivityForm.activityURLs}"
					var="activityURL" varStatus="loop">
					<c:set var="numActivities" value="${loop.count}" />
					<tr>
						<td width="2%">
							<c:choose>
								<c:when test="${not activityURL.complete and not optionsActivityForm.maxActivitiesReached}">
									<input type="radio" name="activityID" class="noBorder"
										id="activityID-${activityURL.activityId}"
										value="${activityURL.activityId}">
								
								
								</c:when>
								<c:when test="${activityURL.complete}">
									<img src="<lams:LAMSURL/>/images/tick.gif" />
								</c:when>
							</c:choose>
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

	    <c:if test="${optionsActivityForm.maximum != numActivities or optionsActivityForm.minimum != 0}">
		<p class="error">
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
		</p>
	    </c:if>
			<div align="center" class="small-space-bottom">
				
				<c:choose>
					<c:when test="${optionsActivityForm.maxActivitiesReached}">
						<c:choose>
							<c:when test="${isOptionsWithSequencesActivity}">
								<p class="warning">
									<fmt:message key="label.optional.maxSequencesReached" /> 
								</p>
							</c:when>
							<c:otherwise>
								<p class="warning">
									<fmt:message key="label.optional.maxActivitiesReached" /> 
								</p>
							</c:otherwise>
						</c:choose>
						
					</c:when>
					<c:otherwise>
						<html:submit styleClass="button">
							<fmt:message key="label.activity.options.choose" />
						</html:submit>
					</c:otherwise>
				</c:choose>
			</div>

		</html:form>
	</div>

	<p class="info">
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

	<c:if test="${optionsActivityForm.finished}">
	      <script language="JavaScript" type="text/JavaScript"><!--
	           function submitForm(methodName){
              	       var f = document.getElementById('messageForm');
                       f.submit();
        	   }
		//-->
	      </script>

		<html:form action="/CompleteActivity" method="post" styleId="messageForm">
			<input type="hidden" name="lams_token"
				value="<c:out value='${lams_token}' />">
			<input type="hidden" name="activityID"
				value="<c:out value='${optionsActivityForm.activityID}' />">
			<input type="hidden" name="lessonID"
				value="<c:out value='${optionsActivityForm.lessonID}' />">
			<input type="hidden" name="progressID"
				value="<c:out value='${optionsActivityForm.progressID}' />">

			<div class="align-right space-bottom-top">
                               <html:link href="javascript:;" styleClass="button" styleId="finishButton" onclick="submitForm('finish')">
                                     <span class="nextActivity">
                                     	<c:choose>
						 					<c:when test="${activityPosition.last}">
						 						<fmt:message key="label.submit.button" />
						 					</c:when>
						 					<c:otherwise>
						 		 				<fmt:message key="label.finish.button" />
						 					</c:otherwise>
						 				</c:choose>
						 			 </span>
                               </html:link>
			</div>

		</html:form>
	</c:if>

</div>
<!--closes content-->

<div id="footer"></div>
