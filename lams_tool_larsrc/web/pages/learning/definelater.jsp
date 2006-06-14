<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<%@ include file="/common/header.jsp"%>

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
		<b>
			<fmt:message key="define.later.message" />
		</b>
	</div>

</body>
</html:html>
