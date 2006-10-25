<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMapID" value="${param.sessionMapID}" />
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />


<html:form action="/learning/submitReflection" method="post">
	<html:hidden property="userID" />
	<html:hidden property="sessionMapID" />

	<div id="content">
		<h1>
			${sessionMap.title}
		</h1>

		<%@ include file="/common/messages.jsp"%>


		<p>
			<lams:out value="${sessionMap.reflectInstructions}" />
		</p>

		<html:textarea cols="52" rows="6" property="entryText" styleClass="text-area" />

		<div class="space-bottom-top">
			<div class="right-buttons">
			<html:submit styleClass="button">
				<fmt:message key="label.finish" />
			</html:submit>
			</div>
		</div>

	</div>
</html:form>

