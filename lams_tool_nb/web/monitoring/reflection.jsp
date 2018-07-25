<!DOCTYPE html>
	

<%@ include file="/includes/taglibs.jsp"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<lams:html>
<lams:head>
	<lams:css />
	<title><fmt:message key="activity.title" /> <fmt:message key="activity.title"/></title>
</lams:head>

<body class="stripes">

	<c:set var="title"><fmt:message key="activity.title" />: <fmt:message key="titleHeading.reflection" /></c:set>
	<lams:Page title="${title}" type="learner">

		<table class=table table-striped">
			<tr>
				<th>
						<c:out value="${name}" />
				</th>
			</tr>
			<tr>
				<td>
						<lams:out value="${nbEntry}" escapeHtml="true" />
				</td>
			</tr>
		</table>
	</lams:Page>
	
</lams:html>

