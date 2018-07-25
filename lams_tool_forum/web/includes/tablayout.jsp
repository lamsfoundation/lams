<!DOCTYPE html>
        

<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="tags-tiles" prefix="tiles"%>

<lams:html>
	<lams:head>
		<title><fmt:message key="activity.title" /></title>
		<%@ include file="/common/tabbedheader.jsp"%>

		<tiles:insert attribute="header" />
	</lams:head>

	<body class="stripes" onLoad="init()">
	
		<tiles:insert attribute="body" />
		
	</body>
</lams:html>
