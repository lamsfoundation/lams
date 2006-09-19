<%@ include file="/includes/taglibs.jsp"%>

<h1 class="no-tabs-below">
	<c:out value="${title}" escapeXml="false" />
</h1>
<div id="header-no-tabs-learner"></div>
<div id="content-learner">
	<html:form action="/learner" method="post">
		<table>
			<tr>
				<td>
					<c:out value="${reflectInstructions}" escapeXml="false" />				
				</td>
			</tr>

			<tr>
				<td>
					<html:textarea cols="66" rows="8" property="reflectionText"></html:textarea>
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
<div id="footer-learner"></div>
