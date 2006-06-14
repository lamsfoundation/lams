<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<%@ include file="/common/header.jsp"%>
	<meta http-equiv="refresh" content="60">
</head>

<body>
	<table border="0" align="center" class="forms" width="95%">
		<tr>
			<td>
				<h1>
					${resource.title}
				</h1>
				<h2>
					${resource.instructions}
				</h2>
			</td>
		</tr>
	</table>

	<div align="center">
		<b> <fmt:message key="define.later.message" /> </b>
		<P>
			<a href="javascript:location.reload(true);" class="button"><b><fmt:message key="button.try.again" /></b></a>
	</div>

</body>
</html:html>
