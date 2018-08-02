<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>

	<lams:head>
			<title>
				<fmt:message key="activity.title" />
			</title>
			<lams:headItems />
			<script type="text/javascript">
				var initialTabId = "${mindmapDTO.currentTab}";
			</script>
			<script type="text/javascript" src="<lams:WebAppURL />includes/javascript/monitoring.js"></script>
	</lams:head>

	<body class="stripes">	
	
		<lams:Page title="${pageTitle.monitoring}" type="learner">

			<div id="content">
				<h3>
					<fmt:message>label.notebookEntry</fmt:message>
				</h3>
				
				<table class="table">
					<tr>
						<th>
							<c:out value="${mindmapUser}" escapeXml="true"/>
						</th>
					</tr>
					<tr>
						<td>
							<lams:out value="${reflectEntry}" escapeHtml="true" />
						</td>
					</tr>
				</table>
						
				<button class="btn btn-primary voffset10 pull-right" property="backButton" onclick="history.go(-1)">
					<fmt:message>button.back</fmt:message>
				</button>

			</div>
			<div id="footer"></div>
		</lams:Page>
	</body>
</lams:html>
