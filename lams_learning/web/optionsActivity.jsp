  
<%@ page language="java"%>
<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
	
<div align="center">
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
				if (elements[i].name == "activityId") {
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
		
		<table width="100%" height="231" border="0" align="center" cellpadding="5" cellspacing="0" bgcolor="#FFFFFF" summary="This table is being used for layout purposes">
			<tr> 
				<td height="179" valign="top" align="center">
					<table width="90%" border="0" cellspacing="1" cellpadding="0" summary="This table is being used for layout purposes">
						<tr> 
							<td colspan="3"> <div align="center"></div></td>
						</tr>
						<tr> 
							<td width="28%" height="31">&nbsp;</td>
							<td width="44%">
								<div align="center" class="mainHeader">
									<c:out value="${optionsActivityForm.title}"/>
								</div>
							</td>
							<td width="28%">&nbsp;</td>
						</tr>
						<tr> 
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr> 
							<td colspan="3" class="body">
								<c:out value="${optionsActivityForm.description}"/><p>&nbsp;</p>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<div align="center" class="bodyBold">
									<fmt:message key="message.activity.options.activityCount">
										<fmt:param value="${optionsActivityForm.minimum}" />
										<fmt:param value="${optionsActivityForm.maximum}" />
									</fmt:message>
								</div>
							</td>
						</tr>
						<tr> 
							<td colspan="3">&nbsp;</td>
						</tr>
						<c:forEach items="${optionsActivityForm.activityURLs}" var="activityURL" varStatus="loop">
							<c:set var="rowColor" value="#B5D2E3" />
							<c:if test="${loop.index % 2 == 0}">
								<c:set var="rowColor" value="#B5CECE" />
							</c:if>
							<tr bgcolor="<c:out value="${rowColor}"/>">
								<td colspan="3">
									<table width="100%" border="0" cellspacing="1" cellpadding="0" summary="This table is being used for layout purposes">
										<tr onclick="selectActivity(<c:out value="${activityURL.activityId}" />)">
											<td width="6%">
												<c:choose>
													<c:when test="${activityURL.complete}">
														<%--html:img page="/images/tick.gif" /--%>
													</c:when>
													<c:otherwise>
														<input type="radio" name="activityId"
															value="<c:out value="${activityURL.activityId}"/>" />
													</c:otherwise>
												</c:choose>
											</td>
											<td width="55%" class="bodyBold">
												<c:out value="${activityURL.title}"/>
											</td>
											<td width="38%" class="body"><c:out value="${activityURL.description}"/></td>
										</tr>
									</table>
								</td>
							</tr>
						</c:forEach> 
					</table>
				</td>
			</tr>
			<tr>
				<td valign="bottom">
					<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" summary="This table is being used for layout purposes">
						<tr>
							<td colspan="3" class="body">
								<p><font size="1"><fmt:message key="message.activity.options.note" /></font>
								</p>
								<p>&nbsp;</p>
							</td>
						</tr>
						<tr>
							<td width="28%">
								<%--input name="submit" type="button" id="submit" class="button" onClick="validateSubmit()" onMouseOver="pviiClassNew(this,'buttonover')" onMouseOut="pviiClassNew(this,'button')" value="Finish"--%>
								<%--html:submit value="Choose" styleClass="button" onmouseover="setClass(this,'buttonover')" onmouseout="setClass(this,'button')" /--%>
								<input name="chooseBtn" type="button" class="button" id="chooseBtn" onClick="submitChoose()" onmouseover="setClass(this,'buttonover')" onmouseout="setClass(this,'button')" value="<fmt:message key="label.activity.options.choose" />">
							</td>
              				<td width="31%" align="right" valign="bottom">&nbsp;</td>
              				<td width="41%" align="right" valign="bottom">
              					<c:if test="${optionsActivityForm.finished}">
									<input name="finishBtn" type="button" class="button" id="finishBtn" onClick="submitFinish()" onmouseover="setClass(this,'buttonover')" onmouseout="setClass(this,'button')" value="<fmt:message key="label.activity.finish" />">
								</c:if>
              				</td>
      					</tr>
  					</table>
  				</td>
  			</tr>
		</table>
	</html:form>
	<html:form action="/CompleteActivity" method="POST">
		<input type="hidden" name="lams_token" value="<c:out value='${lams_token}' />" />
		<input type="hidden" name="activityId" value="<c:out value="${optionsActivityForm.activityId}" />" />
	</html:form>
</div>

