<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-html" prefix="html"%>
<html>
	<head>
		<lams:css />
		<script type="text/javascript">
			  function closeWindow() {
	        	window.opener = "authoring"
	        	window.close();
   	          }  				
		</script>
	</head>
	<body>
		<table border="0" cellspacing="5" cellpadding="5" width="90%" align="center">
			<tr>
				<td align="center">
					<fmt:message key="authoring.msg.save.success" />
				</td>
			</tr>
			<tr>
				<td align="center">
					<!-- Button Row -->
					<html:link href="javascript:closeWindow();" property="close" styleClass="button">
						<fmt:message key="label.authoring.close" />
					</html:link>
					<html:link href="${param.reEditUrl}" property="reedit" styleClass="button">
						<fmt:message key="label.authoring.re.edit" />
					</html:link>
				</td>
			</tr>
		</table>
	</body>
</html>
