<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c" %>

<!DOCTYPE html>

<lams:html>
<lams:head>
	<c:remove var="notifyCloseURL" scope="session" />
	       	
	<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/common.js"></script>
	<script type="text/javascript">
		function closeWindow() {
			if (window.parent.ActivityLib) {
				window.parent.ActivityLib.closeActivityAuthoring();
				return;
			}

			var notifyCloseURL = "${param.notifyCloseURL}";
			if (notifyCloseURL == "") {
				refreshParentMonitoringWindow();
			} else {
				if (window.parent.opener == null
						|| '${param.noopener}' == 'true'
						|| notifyCloseURL.indexOf('noopener=true') >= 0) {
					window.location.href = notifyCloseURL + '&action=save';
				} else {
					window.parent.opener.location.href = notifyCloseURL;
				}
			}

			//just for depress alert window when call window.close()
			//only available for IE browser			  
			var userAgent = navigator.userAgent;
			if (userAgent.indexOf('MSIE') != -1) {
				window.opener = "authoring";
			}
			window.close();
		}

		closeWindow();
	</script>
</lams:head>
</lams:html>