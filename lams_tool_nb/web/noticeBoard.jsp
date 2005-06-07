<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>

<table width="40%" align="center" border="0" cellspacing="1" cellpadding="1" bgcolor="#FFFFFF">
<html:form action="/tool/nb/authoring?method=processPage" target="_self" >
	<tr>
		<td width="33%">
			<html:submit property="choice">
				<fmt:message key="button.basic" />
			</html:submit>
		</td>
		<td width="33%">
			<html:submit property="choice">
				<fmt:message key="button.advanced" />
			</html:submit>
		</td>
		<td width="33%">
			<html:submit property="choice">
				<fmt:message key="button.instructions" />
			</html:submit>
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<hr />
		</td>
	<tr>


		<td colspan="3">	
			<c:choose>
				<c:when test='${sessionScope.choice == "Advanced"}'>
					<jsp:include page="includes/nbAdvancedContent.jsp" />
				</c:when>
				<c:when test='${sessionScope.choice == "Instructions"}'>
					<jsp:include page="includes/nbInstructionsContent.jsp" />
				</c:when>
				<c:otherwise>
					<jsp:include page="includes/nbBasicContent.jsp" />
				</c:otherwise>	
				
			</c:choose>
		</td>
	</tr>
	
</html:form>
</table>
	