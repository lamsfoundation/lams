<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<<c:set var="lams">
	<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:headItems />
		
	</lams:head>
	<body class="stripes">
	
		<tiles:useAttribute name="pageTitleKey" ignore="true"/>
		<bean:define name="pageTitleKey" id="pTitleKey" type="String" />
		<c:set var="title"><fmt:message key="${pTitleKey}" /></c:set>
		
		<lams:Page title="${title}" type="learner">
			<div id="content">
				<p>
					${requestScope.message};
				</p>
			</div>
			<div id="footer"></div>
		</lams:Page>
		
	</body>
</lams:html>
