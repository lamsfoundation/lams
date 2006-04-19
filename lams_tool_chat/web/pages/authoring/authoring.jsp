<%@ include file="/common/taglibs.jsp"%>

<lams:Tabs control="true">
	<lams:Tab id="1" key="button.basic" />
	<lams:Tab id="2" key="button.advanced" />
	<lams:Tab id="3" key="button.instructions" />
</lams:Tabs>

<div class="tabbody">
	<html:form action="/authoring" method="post" enctype="multipart/form-data">
		<html:hidden property="toolContentID" />
		<html:hidden property="currentTab" styleId="currentTab" />
		<html:hidden property="dispatch" />
		<html:hidden property="authSessionId" />

		<div id="message" align="center">
			<c:if test="${updateContentSuccess}">
				<img src="${tool}images/good.png" >
				<bean:message key="message.updateSuccess" />
				</img>
			</c:if>
			
			<c:if test="${unsavedChanges}">
				<img src="${tool}images/warning.png"/>
				<bean:message key="message.unsavedChanges"/>
			</c:if>
		</div>

		<%-- Page tabs --%>
		<lams:TabBody id="1" titleKey="button.basic" page="basic.jsp" />
		<lams:TabBody id="2" titleKey="button.advanced" page="advanced.jsp" />
		<lams:TabBody id="3" titleKey="button.instructions" page="instructions.jsp" />

		<hr />

		<%-- Form Controls --%>
		<div align="right">
			<html:link href="javascript:doSubmit('updateContent');" property="submit" styleClass="button">
				<fmt:message key="button.save" />
			</html:link>
			<html:link href="javascript:;" property="cancel" onclick="window.close()" styleClass="button">
				<fmt:message key="button.cancel" />
			</html:link>
		</div>
	</html:form>
</div>

<lams:HTMLEditor />




