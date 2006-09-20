<%@ include file="/includes/taglibs.jsp"%>

<h1 class="no-tabs-below">
	<fmt:message key="titleHeading.reflection" />
</h1>

<div id="header-no-tabs-learner"></div>

<div id="content-learner">

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

<div id="footer-learner"></div>