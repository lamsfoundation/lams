<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />

<html:hidden property="questionIndex"/>
<table class="table table-condensed">
	<tr>
		<td width="10%" ><fmt:message key="label.authoring.title.col"></fmt:message></td>
		<td><c:out value="${mcGeneralAuthoringDTO.activityTitle}" escapeXml="false"/></td>
	</tr>		

	<tr>
		<td width="10%" ><fmt:message key="label.authoring.instructions.col"></fmt:message></td>
		<td><c:out value="${mcGeneralAuthoringDTO.activityInstructions}" escapeXml="false"/></td>
	</tr>

	<c:set var="queIndex" scope="request" value="0"/>
		
	<c:forEach items="${listQuestionContentDTO}" var="currentDTO" varStatus="status">
		<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
		<c:set var="question" scope="request" value="${currentDTO.question}"/>
		<c:set var="feedback" scope="request" value="${currentDTO.feedback}"/>
		<c:set var="displayOrder" scope="request" value="${currentDTO.displayOrder}"/>	
		
		<tr>
			<td width="10%">
				<fmt:message key="label.question" />:
		</td>
		
		<td>
			<c:out value="${question}" escapeXml="false"/> 
			</td>		
		</tr>
	</c:forEach>
		
</table>

<c:url  var="authoringUrl" value="/authoringStarter.do">
	<c:param name="toolContentID" value="${formBean.toolContentID}" />
	<c:param name="contentFolderID" value="${formBean.contentFolderID}" />
	<c:param name="mode" value="teacher" />
</c:url>
<html:link href="javascript:;" onclick="launchPopup('${authoringUrl}','definelater')" styleClass="btn btn-default pull-right">
	<fmt:message key="label.edit"/>
</html:link>				 		  					
