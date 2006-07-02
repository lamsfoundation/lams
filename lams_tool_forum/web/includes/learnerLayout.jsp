<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="tags-tiles" prefix="tiles"%>

<html>
	<head>
		<title><bean:message key="activity.title" /></title>
		<%@ include file="/common/header.jsp"%>
	</head>
	<body>
		<div id="page-learner">
			<tiles:insert attribute="body" />
		</div>
	</body>
</html>
