<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">

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
	<title><fmt:message key="activity.title"/></title>
</lams:head>

<body class="stripes">

	<div id="content">

		<h1>
			<fmt:message key="titleHeading.reflection" />
		</h1>
		
		<!-- if nbEntry is empty, display 'has not reflected' message -->
		<table class="alternative-color">
			<tr>
				<th>
					<b>
						<c:out value="${name}" />
					</b>
				</th>
			</tr>
			<tr>
				<td>
					<p>
						<lams:out value="${nbEntry}" escapeHtml="true" />
					</p>
				</td>
			</tr>
		</table>
	
	</div>
	
</lams:html>

