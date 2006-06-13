<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<%@ include file="/common/header.jsp"%>

	<script type="text/javascript">
	<!--
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?toolSessionID=${toolSessionID}&runOffline=true"/>';
			return false;
		}
	-->        
    </script>
</head>

<body>
	<h1>
		${resource.title}
	</h1>
	<div align="center">
		<h2>
			<fmt:message key="run.offline.message" />
		</h2>
		<p>
		<a href="#" class="button" onclick="return finishSession()"> <fmt:message key="label.finished" /> </a>
	</div>


</body>
</html:html>
