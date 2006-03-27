<%@ include file="/common/taglibs.jsp" %>
<html>
<head>
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
    <link href="<html:rewrite page='/includes/css/aqua.css'/>" rel="stylesheet" type="text/css">
	<!-- this is the custom CSS for hte tool -->
	<link href="<html:rewrite page='/includes/css/tool_custom.css'/>" rel="stylesheet" type="text/css">
	<link href="<html:rewrite page='/includes/css/rsrc.css'/>" rel="stylesheet" type="text/css">
</head>
<body class="tabpart">
<!-- This table is just for layout -->
<table class="forms">
<tr><td>
	<table class="forms">
		<!-- Basic Info Form-->
		<tr>
			<table>
				<tr>
					<td colspan="2"><h2><fmt:message key="label.authoring.basic.add.url"/></h2></td>
				</tr>
				<tr>
					<td><fmt:message key="label.authoring.basic.resource.title.input"/></td>
					<td><input type="text" name="title"></td>
				</tr>
			</table>
		</tr>
		<tr>
			<!-- Instructions -->
			<table>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td></td>
				</tr>
			</table>
		</tr>
	</table>
</td></tr>
</table>
</body>
</html>