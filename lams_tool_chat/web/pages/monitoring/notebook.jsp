<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.chat.util.ChatConstants"%>
<lams:html>

	<c:set var="lams">
	<lams:LAMSURL />
	</c:set>
	<c:set var="tool">
		<lams:WebAppURL />
	</c:set>

		<lams:head>
			<title>
				<fmt:message key="activity.title" />
			</title>
			<link href="${tool}includes/css/chat.css" rel="stylesheet" type="text/css">
			<lams:headItems />
			<script type="text/javascript" src="${tool}includes/javascript/monitoring.js">
			</script>

		</lams:head>
	<body class="stripes">
		<c:set var="title"><fmt:message key="heading.reflection"/></c:set>
		<lams:Page title="${title}" type="learner">
		
		<table>
			<tr>
				<td>
					<h2>
						<c:out value="${chatUserDTO.firstName} ${chatUserDTO.lastName}" escapeXml="true"/>
					</h2>
				</td>
			</tr>
			<tr>
				<td>
					<p>
						<lams:out value="${chatUserDTO.notebookEntry}" escapeHtml="true"/>
					</p>
				</td>
			</tr>
		</table>
		
		</lams:Page>
	</body>
</lams:html>
