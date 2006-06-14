<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<%@include file="/common/header.jsp"%>
	<meta http-equiv="refresh" content="60">
</head>

<body>
	<div align="center">
		<h1>
			<fmt:message key="label.authoring.heading" />
		</h1>
		<P>		
		<b><fmt:message key="define.later.message" /></b>
		<P>
		<a href="javascript:location.reload(true);" class="button"><b><fmt:message key="button.try.again" /></b></a>
	</div>

</body>
</html:html>
