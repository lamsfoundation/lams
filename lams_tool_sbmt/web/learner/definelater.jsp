<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>

<html:html>
<head>
	<%@ include file="/common/header.jsp"%>
	<script type="text/javascript">
		var locked =  <c:out value="${learner.locked}"/>;
		function finish(){
			var finishUrl= "<html:rewrite page='/learner.do?method=finish&toolSessionID=${learner.toolSessionID}'/>";
			location.href= finishUrl;
		}
	</script>
</head>

<body class="stripes">

		<div id="content">
		<h1>
			<fmt:message key="activity.title"></fmt:message>
		</h1>

			<lams:DefineLater/>
		</div>
		<div id="footer"></div>

</body>
</html:html>


