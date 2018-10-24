<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
		<c:set var="lams"> <lams:LAMSURL /> </c:set>
		<c:set var="tool">	<lams:WebAppURL /> </c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:headItems />
		<script type="text/javascript">
			var initialTabId = "${mindmapDTO.currentTab}";
		</script>
		<script type="text/javascript" src="${tool}includes/javascript/monitoring.js"></script>
	</lams:head>

	<body class="stripes">	
	
		<c:set var="title"><fmt:message key="activity.title" /></c:set>
		<lams:Page title="${title}" type="learner">

			
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
		</lams:Page>
		<div id="footer"></div>
	</body>
</lams:html>
