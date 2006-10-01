<%@ include file="/includes/taglibs.jsp"%>

<div id="content">

<h1>
	<fmt:message key="titleHeading.reflection" />
</h1>

<!-- if nbEntry is empty, display 'has not reflected' message -->
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
				<lams:out value="${nbEntry}" />
			</p>
		</td>
	</tr>
</table>

</div>

