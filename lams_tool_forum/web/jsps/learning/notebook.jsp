<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${param.sessionMapID}"/>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>


<html:form action="/learning/submitReflection" method="post">
	<html:hidden property="userID" />
	<html:hidden property="sessionMapID"/>
	<h1 class="no-tabs-below">
		${sessionMap.title}
	</h1>
	<div id="header-no-tabs-learner"></div>
	<div id="content-learner">
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
						<lams:STRUTS-textarea cols="66" rows="8" property="entryText"/>
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
<div id="footer-learner"></div>
