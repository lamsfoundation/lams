<%@ include file="/includes/taglibs.jsp"%>

<div id="content">
<h1>
	<c:out value="${title}" escapeXml="false" />
</h1>

	<html:form action="/learner" method="post">
		<table width="100%">
			<tr>
				<td>
					<lams:out value="${reflectInstructions}" />				
				</td>
			</tr>

			<tr>
				<td>
					<html:textarea cols="60" rows="8" property="reflectionText"></html:textarea>
				</td>
			</tr>

			<tr>
				<td>
					<div class="right-buttons">
						<html:hidden property="toolSessionID" />
						<html:hidden property="mode" />
						<html:submit property="method" styleClass="button">
							<fmt:message key="button.finish" />
						</html:submit>
					</div>
				</td>
			</tr>
		</table>
	</html:form>
</div>

