<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<%@ include file="/common/header.jsp"%>
	<meta http-equiv="refresh" content="60">
</head>

<body class="stripes">

		<h1>
			${resource.title}
		</h1>

		<div id="content">
			<lams:DefineLater/>
		</div>
		
		<div id="footer">
		</div>
		<!--closes footer-->

</body>
</html:html>
