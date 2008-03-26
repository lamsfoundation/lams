<%@ include file="/includes/taglibs.jsp"%>

<div id="content">

<h1>
	<fmt:message key="titleHeading.reflection" />
</h1>

<!-- if wikiEntry is empty, display 'has not reflected' message -->
<table>
	<tr>
		<td>
			<h2>
				<c:out value="${name}" />
			</h2>
		</td>
	</tr>
	<tr>
		<td>
			<p>
				<lams:out value="${wikiEntry}" />
			</p>
		</td>
	</tr>
</table>

</div>

