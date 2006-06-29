<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@include file="/common/taglibs.jsp"%>

<html:html>
<head>
	<lams:headItems />
	<script type="text/javascript">
		var locked =  <c:out value="${learner.locked}"/>;
		function finish(){
			var finishUrl= "<html:rewrite page='/learner.do?method=finish&toolSessionID=${learner.toolSessionID}'/>";
			location.href= finishUrl;
		}
	</script>
</head>

<body>
	<div id="page">

		<h1 class="no-tabs-below">
			<fmt:message key="activity.title"></fmt:message>
		</h1>

		<div id="header-no-tabs"></div>

		<div id="content">
			<h1>
				<fmt:message key="run.offline.message" />
			</h1>
			<p>
				<html:button property="finished" onclick="finish()" disabled="${learner.locked}" styleClass="buttonStyle">
					<fmt:message key="button.finish" />
				</html:button>
			</p>
		</div>
		<div id="footer"></div>
	</div>
</body>
</html:html>
