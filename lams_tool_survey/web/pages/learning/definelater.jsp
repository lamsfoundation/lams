<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<%@ include file="/common/header.jsp"%>
	<meta http-equiv="refresh" content="60">
</head>

<body>

	<div id="page-learner">
		<h1 class="no-tabs-below">
			${survey.title}
		</h1>
		<div id="header-no-tabs-learner">
		</div>
		<!--closes header-->

		<div id="content-learner">
			<lams:DefineLater/>
		</div>
		
		<div id="footer-learner">
		</div>
		<!--closes footer-->

	</div>

</body>
</html:html>
