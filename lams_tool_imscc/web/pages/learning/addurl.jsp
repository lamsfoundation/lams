<!DOCTYPE html>
		

<%@ include file="/common/taglibs.jsp"%>
<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
	</lams:head>
	<body>
		<html:form action="/learning/saveOrUpdateItem" method="post"
			styleId="commonCartridgeItemForm">
			<html:hidden property="itemType" styleId="itemType" value="1" />
			<html:hidden property="mode" />
			<html:hidden property="sessionMapID" />

			<div class="field-name-alternative-color space-top">
				<fmt:message key="label.learning.new.url" />
			</div>
			<%@ include file="/common/messages.jsp"%>

			<div class="field-name space-top">
				<fmt:message key="label.authoring.basic.resource.title.input" />
			</div>

			<html:text property="title" size="40" tabindex="1" />

			<div class="field-name space-top">
				<fmt:message key="label.authoring.basic.resource.url.input" />
			</div>

			<html:text property="url" size="40" tabindex="2" />


			<html:checkbox property="openUrlNewWindow" tabindex="3"
				styleId="openUrlNewWindow" styleClass="noBorder">
			</html:checkbox>
			<label for="openUrlNewWindow">
				<fmt:message key="open.in.new.window" />
			</label>


			<div class="field-name space-top">
				<fmt:message key="label.learning.comment.or.instruction" />
			</div>

			<html:text tabindex="2" property="description"
				styleClass="text-area" maxlength="255" />

			<div class="space-bottom-top">
				<a href="#"
					onclick="document.getElementById('commonCartridgeItemForm').submit()"
					class="button"> <fmt:message key="button.add" /> </a>
			</div>
		</html:form>
	</body>
</lams:html>
