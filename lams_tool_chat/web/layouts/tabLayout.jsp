<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<tiles:insert attribute="header" />
	<body class="stripes" onload="init();">
		<div id="page">
			<tiles:useAttribute name="pageTitleKey" />
			<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
			<h1>
				<fmt:message key="${pTitleKey}" />
			</h1>
			<tiles:insert attribute="body" />
		</div>
	</body>
</lams:html>
