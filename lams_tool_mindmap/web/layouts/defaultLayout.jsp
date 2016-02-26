<!DOCTYPE html>
            

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<tiles:insert attribute="header" />
	<body class="stripes">
		<div id="page">	
			<tiles:useAttribute name="pageTitleKey" ignore="true"/>
			<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
			<h1 class="no-tabs-below">
				<fmt:message key="${pTitleKey}" />
			</h1>
<!--			<div id="header-no-tabs"></div>-->
			<div id="content">
				<tiles:insert attribute="body" />
			</div>
			<div id="footer"></div>
		</div>
	</body>
</lams:html>
