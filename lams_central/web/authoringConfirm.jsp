<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-html" prefix="html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<lams:html>
<head>
	<META http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<lams:css />
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
<body class="stripes">

	<div id="content" align="center">

		<h1>
			<fmt:message key="authoring.msg.save.success" />
		</h1>
		<div class="space-bottom-top">
			<html:button onclick="javascript:location.href='${param.reEditUrl}'"
				property="reedit" styleClass="button">
				<fmt:message key="label.authoring.re.edit" />
			</html:button>
			<html:button onclick="javascript:closeWindow();" property="close"
				styleClass="button">
				<fmt:message key="label.authoring.close" />
			</html:button>
		</div>

	</div>

	<div id="footer"></div>

</body>
</lams:html>
