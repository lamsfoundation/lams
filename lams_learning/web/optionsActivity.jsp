 
<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
	
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
				alert("Please select an activity from the list");
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
	<html:form action="/Activity" method="POST">
		<input type="hidden" name="method" value="display" />
		
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
									You must complete at lease <c:out value="${optionsActivityForm.minimum}"/>
									of these <c:out value="${optionsActivityForm.maximum}"/> activities.
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
								<p><font size="1"><u><b>Note</b></u>: Once you finish any of the above activities
									you can revisit them by using the progress bar on the left.</font>
								</p>
								<p>&nbsp;</p>
							</td>
						</tr>
						<tr>
							<td width="28%">
								<%--input name="submit" type="button" id="submit" class="button" onClick="validateSubmit()" onMouseOver="pviiClassNew(this,'buttonover')" onMouseOut="pviiClassNew(this,'button')" value="Finish"--%>
								<%--html:submit value="Choose" styleClass="button" onmouseover="setClass(this,'buttonover')" onmouseout="setClass(this,'button')" /--%>
								<input name="chooseBtn" type="button" class="button" id="chooseBtn" onClick="submitChoose()" onmouseover="setClass(this,'buttonover')" onmouseout="setClass(this,'button')" value="Choose">
							</td>
              				<td width="31%" align="right" valign="bottom">&nbsp;</td>
              				<td width="41%" align="right" valign="bottom">
              					<c:if test="${optionsActivityForm.finished}">
									<input name="finishBtn" type="button" class="button" id="finishBtn" onClick="submitFinish()" onmouseover="setClass(this,'buttonover')" onmouseout="setClass(this,'button')" value="Finish">
								</c:if>
              				</td>
      					</tr>
  					</table>
  				</td>
  			</tr>
		</table>
	</html:form>
	<html:form action="/Activity" method="POST">
		<input type="hidden" name="method" value="complete" />
		<html:hidden property="activityId" />
	</html:form>
</div>

