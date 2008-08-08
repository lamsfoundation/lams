<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>
<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>
</lams:head>
<body class="stripes">
<script type="text/javascript">
			var reqIDVar = new Date();
			document.location.href = "<c:url value='/pages/learning/learning.jsp?sessionMapID=${sessionMapID}&mode=${mode}&reqID="+reqIDVar.getTime()+"' />";
		</script>
</body>
</lams:html>