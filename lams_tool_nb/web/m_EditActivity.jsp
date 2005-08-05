<h2><fmt:message key="titleHeading.editActivity"/></h2>
<br>
<div id="datatablecontainer">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<c:choose>
			<c:when test='${requestScope.isPageEditable == "true"}'>
				<tr>
					<td><fmt:message key="basic.title"/></td>
					<td>
						<c:out value="${sessionScope.title}" escapeXml="false"/>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><fmt:message key="basic.content"/></td>
					<td>
						<c:out value="${sessionScope.content}" escapeXml="false"/>
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td><font color="red"><fmt:message key="message.contentInUseSet"/></font></td>
				
				</tr>			
			</c:otherwise>
		
		</c:choose>
	</table>
</div>

<div id="formtablecontainer">
<table width="100%">
	<c:if test='${requestScope.isPageEditable == "true"}'>
		<tr>
			<td align="right">
			<!--	<html:link forward="forwardToAuthorPage" paramName="NbMonitoringForm" paramProperty="toolContentId" paramId="toolContentId" styleClass="button">
					<fmt:message key="button.edit" />
				</html:link> -->
				<html:link forward="forwardToAuthorPage" name="NbMonitoringForm" property="parametersToAppend" styleClass="button" target="_blank">
					<fmt:message key="button.edit" />
				</html:link> 
			</td>	
		</tr>
	</c:if>
</table>
</div>
<br>
<hr>

