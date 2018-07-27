<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<tiles:insert attribute="header" />
	<body class="stripes">	
	
		<tiles:useAttribute name="pageTitleKey" ignore="true"/>
		<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
		<c:set var="title"><fmt:message key="${pTitleKey}" /></c:set>
		<lams:Page title="${title}" type="learner">

			<div id="content">
				<tiles:insert attribute="body" />
			</div>
			<div id="footer"></div>
		</lams:Page>
	</body>
</lams:html>
