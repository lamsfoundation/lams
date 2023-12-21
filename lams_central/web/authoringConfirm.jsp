<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:remove var="notifyCloseURL" scope="session" />

	<lams:JSImport src="includes/javascript/common.js" />
	<script type="text/javascript">
		function closeWindow() {
			if (window.parent.ActivityLib) {
				window.parent.ActivityLib.closeActivityAuthoring(window.frameElement.id);
				return;
			}
			
			var notifyCloseURL = "<c:out value='${param.notifyCloseURL}' />";
			if (notifyCloseURL == "") {
				refreshParentMonitoringWindow();
			} else {
				if (window.parent.opener == null
						|| ${param.noopener eq "true" or param.notifyCloseURL.indexOf("noopener=true") >= 0}) {
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