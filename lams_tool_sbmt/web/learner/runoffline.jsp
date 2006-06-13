<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<%@include file="/common/header.jsp"%>
	<script language="javascript">
		var locked =  <c:out value="${learner.locked}"/>;
		function finish(){
			var finishUrl= "<html:rewrite page='/learner.do?method=finish&toolSessionID=${learner.toolSessionID}'/>";
			location.href= finishUrl;
		}
	</script>
</head>

<body>
	<div align="center">
		<h2>
			<fmt:message key="run.offline.message" />
		</h2>
 	<p>
		<html:button property="finished" onclick="finish()" disabled="${learner.locked}" styleClass="buttonStyle">
			<fmt:message key="label.learner.finished" />
		</html:button>

	</div>

</body>
</html:html>
