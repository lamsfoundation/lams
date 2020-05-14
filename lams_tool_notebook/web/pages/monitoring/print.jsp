<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.notebook.util.NotebookConstants"%>
<lams:html>
	<lams:head>
		
	</lams:head>

	<body>
		<h2><c:out value="${printDTO.title}" /></h2>
		<c:out value="${printDTO.instructions}" escapeXml="false"/>		
		<hr>
		
		<c:forEach items="${printDTO.usersBySession}" var="sessionEntry">
			<c:if test="${printDTO.groupedActivity}">
				<h2><c:out value="${sessionEntry.key}" /></h2>
			</c:if>
			
			<c:forEach items="${sessionEntry.value}" var="printUserDTO">
				<h3><c:out value="${printUserDTO.lastName}" />&nbsp;<c:out value="${printUserDTO.firstName}" />&nbsp;<c:out value="${printUserDTO.email}" /></h3>
				<span><small><lams:Date value="${printUserDTO.entryModifiedDate}"/></small></span> 
				
				<p><c:out value="${printUserDTO.entry}" escapeXml="false" /></p>
				
				<c:if test="${not empty printUserDTO.teacherComment}">
					<h4><fmt:message key="label.comment" /></h4>
 					<p><c:out value="${printUserDTO.teacherComment}" /></p>
				
				</c:if>
				<hr>
			</c:forEach>
		</c:forEach>	
	</body>
</lams:html>
