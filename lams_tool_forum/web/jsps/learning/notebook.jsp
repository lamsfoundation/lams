<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${param.sessionMapID}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>


<html:form action="/learning/submitReflection" method="post">
	<html:hidden property="userID" />
	<html:hidden property="sessionMapID"/>
	
	<div id="content">
	<h1>
		${sessionMap.title}
	</h1>
			<table>
				<tr>
					<td>
						<%@ include file="/common/messages.jsp"%>
					</td>
				</tr>
				<tr>
					<td>
						${sessionMap.reflectInstructions}
					</td>
				</tr>
	
				<tr>
					<td>
						<lams:STRUTS-textarea cols="60" rows="8" property="entryText"/>
					</td>
				</tr>
	
				<tr>
					<td class="right-buttons">
						<html:submit styleClass="button">
							<fmt:message key="label.finish"/>
						</html:submit>
					</td>
				</tr>
			</table>
	</div>
</html:form>

