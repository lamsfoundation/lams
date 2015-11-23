<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>	
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
	</lams:head>
	<body>
		<script type="text/javascript"> 
			if (window.parent && window.parent.hideMessage) {
				window.parent.hideMessage();
			} else if (window.top && window.top.hideMessage) {
				window.top.hideMessage();
			}
			if (window.parent && window.parent.updateMarkAfterSaving) {
				window.parent.updateMarkAfterSaving(${userUid}, ${mark});
			} else if (window.top && window.top.updateMarkAfterSaving) {
				window.top.updateMarkAfterSaving(${userUid}, ${mark});
			}
		</script>
		<p>Mark Updated to ${mark}</p>
	</body>
</lams:html>