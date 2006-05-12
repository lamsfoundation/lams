<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-html" prefix="html"%>
<html>
	<head>
		<lams:css />
		<STYLE>
		.buttonStyle{
			font-size: 12px;
			color: #000000;
			text-decoration:none;
			text-align: center;
			background-color:#B4C8FE;
			width: 150px;
			height: 25px;
		}
		</STYLE>
		<script type="text/javascript">
			  function closeWindow() {
				//just for depress alert window when call window.close()
	        	//only available for IE browser			  
				var userAgent=navigator.userAgent;
	        	if(userAgent.indexOf('MSIE') != -1)
		        	window.opener = "authoring"
		    	window.close();
   	          }  				
		</script>
	</head>
	<body>
		<table border="0" cellspacing="5" cellpadding="5" width="90%" align="center">
			<tr>
				<td align="center">
					<h1><fmt:message key="authoring.msg.save.success" /></h1>
				</td>
			</tr>
			<tr>
				<td align="center">
					<!-- Button Row -->
					<html:button onclick="javascript:location.href='${param.reEditUrl}'" property="reedit" styleClass="buttonStyle">
						<fmt:message key="label.authoring.re.edit" />
					</html:button>
					<html:button onclick="javascript:closeWindow();" property="close" styleClass="buttonStyle">
						<fmt:message key="label.authoring.close" />
					</html:button>
				</td>
			</tr>
		</table>
	</body>
</html>
