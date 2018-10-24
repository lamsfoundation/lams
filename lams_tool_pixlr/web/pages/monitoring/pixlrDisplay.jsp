<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>

<lams:html>
	<<c:set var="lams">
	<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<lams:headItems />
		<c:set var="tool">
			<lams:WebAppURL />
		</c:set>

		<script type="text/javascript" src="${tool}includes/javascript/monitoring.js"></script>
		<script type="text/javascript" src="${tool}includes/javascript/common.js"></script>
	</lams:head>
	<body class="stripes">
		
		<lams:Page title="<fmt:message key="pageTitle.monitoring" />" type="learner">
			<div id="content">
				<table class="table">
				<tr>
					<td colspan="2">
						<h2>
							${userDTO.firstName} ${userDTO.lastName }
						</h2>
					</td>
				</tr>
				
				<tr>
					<td width="20%">
						<fmt:message key="label.created" />
					</td>
					<td>
						<lams:Date value="${userDTO.entryDTO.createDate }"></lams:Date>
					</td>
				</tr>
				
				<tr>
					<td width="20%">
						<fmt:message key="label.lastModified" />
					</td>
					<td>
						<lams:Date value="${userDTO.entryDTO.lastModified }"></lams:Date>
					</td>
				</tr>
			
				<tr>
					<td>
						<fmt:message key="label.notebookEntry" />
					</td>
					<td>
						<c:out value="${userDTO.entryDTO.entry}" escapeXml="false"></c:out>
					</td>
				</tr>
			</table>
			</div>
			<div id="footer"></div>
		</lams:Page>
		
	</body>
</lams:html>
