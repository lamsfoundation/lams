<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c" %>

<!DOCTYPE html>

<lams:html>
<lams:head>
	<c:remove var="notifyCloseURL" scope="session" />
	       	
	<lams:css />
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/common.js"></script>
	<script type="text/javascript">
		function closeWindow() {
			var notifyCloseURL = "${param.notifyCloseURL}";
			if (notifyCloseURL == ""){
				refreshParentMonitoringWindow();
			} else {
				if (window.parent.opener == null || '${param.noopener}' == 'true' || notifyCloseURL.indexOf('noopener=true') >= 0){
					window.location.href = notifyCloseURL + '&action=save';
				} else {
					window.parent.opener.location.href = notifyCloseURL;
				}
			}
			

			//just for depress alert window when call window.close()
			//only available for IE browser			  
			var userAgent=navigator.userAgent;
			if(userAgent.indexOf('MSIE') != -1)
				window.opener = "authoring"
			window.close();
		}  				
		</script>
</lams:head>
<body class="stripes">

	<div id="content" align="center">

		<h1>
			<fmt:message key="authoring.msg.save.success" />
		</h1>
		<div class="space-bottom-top">
			<html:link href="javascript:;" onclick="javascript:location.href='${param.reEditUrl}'"
				property="reedit" styleClass="button">
				<span class="editForm"><fmt:message key="label.authoring.re.edit" /></span>
			</html:link>
			<html:link href="javascript:;" onclick="javascript:closeWindow();" property="close"
				styleClass="button">
				<span class="close"><fmt:message key="label.authoring.close" /></span>
			</html:link>
		</div>

	</div>

	<div id="footer"></div>

</body>
</lams:html>
