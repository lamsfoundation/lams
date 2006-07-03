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
	<div id="page-learner">

		<h1 class="no-tabs-below">
			<fmt:message key="activity.title"></fmt:message>
		</h1>

		<div id="header-no-tabs-learner"></div>

		<div id="content-learner">
			<table>
				<tr>
					<td>
						<p>
							<fmt:message key="run.offline.message" />
						</p>
					</td>
				</tr>
			</table>
			
									<div class="right-buttons">
							<html:button property="finished" onclick="finish()" disabled="${learner.locked}" styleClass="button">
								<fmt:message key="button.finish" />
							</html:button>
						</div>
		</div>
		<div id="footer-learner"></div>
	</div>
</body>
</html:html>
