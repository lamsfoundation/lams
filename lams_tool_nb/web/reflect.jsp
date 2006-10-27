<%@ include file="/includes/taglibs.jsp"%>

<div id="content">
<h1>
	<c:out value="${title}" escapeXml="false" />
</h1>

	<html:form action="/learner" method="post">
		 
					<p><lams:out value="${reflectInstructions}" /></p>				
			 
					<html:textarea cols="60" rows="8" property="reflectionText" styleClass="text-area"></html:textarea>
				 
					<div align="right" class="space-bottom-top">
						<html:hidden property="toolSessionID" />
						<html:hidden property="mode" />
						<html:submit property="method" styleClass="button">
							<fmt:message key="button.finish" />
						</html:submit>
					</div>
			 
	</html:form>
</div>

