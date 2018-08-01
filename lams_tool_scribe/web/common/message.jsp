<!DOCTYPE html>
            

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<c:set var="lams">
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
		<lams:Page title=<fmt:message key="pageTitle.authoring" /> type="monitor">
			<p>
				${requestScope.message};
			</p>
			<div id="footer"></div>
		</lams:Page>
	</body>
</lams:html>


