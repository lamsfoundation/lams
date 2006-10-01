<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
            "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<tiles:insert attribute="header" />
	<body class="stripes">

			<div id="content">
				<tiles:useAttribute name="pageTitleKey" ignore="true"/>
				<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
				<h1>
					<fmt:message key="${pTitleKey}" />
				</h1>
				<tiles:insert attribute="body" />
			</div>
			<div id="footer"></div>

	</body>
</html>
