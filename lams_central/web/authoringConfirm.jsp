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

	<c:set var="title">
		<fmt:message key="authoring.msg.save.success" />
	</c:set>
	
	<lams:Page type="admin" title="${title}">

		<div class="voffset10 text-center">
			<html:link href="javascript:;" onclick="javascript:location.href='${param.reEditUrl}'"
				property="reedit" styleClass="btn btn-primary roffset10">
				<fmt:message key="label.authoring.re.edit" />&nbsp;<i class="fa fa-pencil-square-o"></i>
			</html:link>
			<html:link href="javascript:;" onclick="javascript:closeWindow();" property="close"
				styleClass="btn btn-primary">
				<fmt:message key="label.authoring.close" />&nbsp;<i class="fa fa-times-circle-o"></i>
			</html:link>
		</div>

	<div id="footer"></div>
</lams:Page>
</body>
</lams:html>
