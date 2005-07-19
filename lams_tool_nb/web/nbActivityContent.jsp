 <% 
	Long toolContentId = (Long)request.getSession().getAttribute("toolContentId");
	String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	String url = basePath + "tool/nb/starter/authoring.do?toolContentId="+toolContentId;
%>
<h2><fmt:message key="titleHeading.editActivity"/></h2>
<div id="datatablecontainer">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<c:choose>
			<c:when test='${sessionScope.contentInUse != "true"}'>
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
	<c:if test='${sessionScope.contentInUse != "true"}'>
		<tr>
			<td align="right">
				<html:link forward="forwardToAuthorPage" paramName="NbMonitoringForm" paramProperty="toolContentId" paramId="toolContentId" styleClass="button">
					<fmt:message key="button.edit" />
				</html:link>
					<!-- <html:submit property="method" styleClass="button"><fmt:message key="button.edit"/></html:submit> -->
			</td>	
		</tr>
	</c:if>
</table>
</div>
<hr>

