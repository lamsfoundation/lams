  
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
	
	<script language="JavaScript" type="text/JavaScript"><!--
		function selectActivity(activityId) {
			// find the activity in the form
			var form = document.forms[0];
			// use form.elements because we are guaranteed an array is returned
			var elements = form.elements;
			for (var i = 0; i < elements.length; i++) {
				// now check if we have the right element
				if (elements[i].name == "activityId") {
					var thisActivityBtn = elements[i];
					var thisActivityId = thisActivityBtn.value;
					if (activityId == thisActivityId) {
						thisActivityBtn.checked = true;
						break;
					}
				}
			}
		}
		function submitChoose() {
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
				//TODO: this should come from messages
				alert("<fmt:message key="message.activity.options.noActivitySelected" />");
			}
			else {
				form.submit();
			}
		}
		function submitFinish() {
			var form = document.forms[1];
			/*var activityId = form.activityId.value;
			var method = form.method.value;
			alert(method+" "+activityId);*/
			form.submit();
		}
		//-->
	</script>
	
	<html:form action="/ChooseActivity" method="POST">
	<input type="hidden" name="lams_token" value="<c:out value='${lams_token}' />" />
	
	<div id="content">

		<h1><c:out value="${optionsActivityForm.title}"/></h1>

		<lams:Passon id="${optionsActivityForm.lessonID}" progress="${optionsActivityForm.progressSummary}"/>
		
		<p>&nbsp;</p>

		<c:if test="${not empty optionsActivityForm.description}">
			<p><c:out value="${optionsActivityForm.description}"/></p>
		</c:if>
		<p><fmt:message key="message.activity.options.activityCount">
				<fmt:param value="${optionsActivityForm.minimum}" />
				<fmt:param value="${optionsActivityForm.maximum}" />
			</fmt:message>
		</p>

		<table class="alternative-color" cellspacing="0">
			<c:forEach items="${optionsActivityForm.activityURLs}" var="activityURL" varStatus="loop">
				<tr onclick="selectActivity(<c:out value="${activityURL.activityId}" />)">
					<td width="2%">
						<c:choose>
							<c:when test="${activityURL.complete}">
								<%--html:img page="/images/tick.gif" /--%>
							</c:when>
							<c:otherwise>
								<input type="radio" name="activityID"
									value="<c:out value="${activityURL.activityId}"/>" />
							</c:otherwise>
						</c:choose>
					</td>
					<td >
						<c:out value="${activityURL.title}"/>
					</td>
				</tr>
			</c:forEach> 
		</table>

		<p><font size="1"><fmt:message key="message.activity.options.note" /></font></p>
		<p>&nbsp;</p>

		<table><tr><td>
		<div class="left-buttons">
			<a href="#" class="button" id="chooseBtn" onClick="submitChoose()"><fmt:message key="label.activity.options.choose" /></a>
		</div>
		<c:if test="${optionsActivityForm.finished}">
			<div class="right-buttons">
				<a href="#" class="button" id="finishBtn" onClick="submitFinish()"><fmt:message key="label.finish.button" /></a>
			</div>
		</c:if>
		</td></tr></table>

	</html:form>

	<html:form action="/CompleteActivity" method="POST">
		<input type="hidden" name="lams_token" value="<c:out value='${lams_token}' />" />
		<input type="hidden" name="activityID" value="<c:out value="${optionsActivityForm.activityID}" />" />
		
	</html:form>

	</div>  <!--closes content-->


	<div id="footer">
	</div><!--closes footer-->


