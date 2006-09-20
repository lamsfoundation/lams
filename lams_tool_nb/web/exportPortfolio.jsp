<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ include file="/includes/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<lams:html>
  	<head>    
	    <title><c:out value="${NbExportForm.title}"/></title>
	  	
		<lams:css localLinkPath="../"/>
	</head>  
  	<body>
		<div id="page-learner"/>

			<h1 class="no-tabs-below">
				<c:out value="${NbExportForm.title}" escapeXml="false" />
			</h1>

			<div id="header-no-tabs-learner">
			</div>

			<div id="content-learner">

				<p>
					<c:out value="${NbExportForm.content}" escapeXml="false" />
				</p>
				
			</div>

			<div id="footer-learner">
			</div>

			<c:if test="${learner}">
				<h2><fmt:message key="titleHeading.reflection" /></h2>
				<logic:empty name="nbEntry"><p><fmt:message key="message.no.reflection" /></p></logic:empty>
				<p><lams:out value="${nbEntry}" /></p>
			</c:if>
			<logic:empty name="learner">
				<h2><fmt:message key="titleHeading.reflections" /></h2>
				<table>
					<logic:empty name="reflections">
						<tr>
							<td colspan="2"><fmt:message key="message.no.reflections" /></td>
						</tr>
					</logic:empty>
					<c:forEach var="reflection" items="${reflections}">
						<tr>
							<td><c:out value="${reflection.fullName}" /></td>
							<td><lams:out value="${reflection.entry}" /></td>
						</tr>
					</c:forEach>
				</table>
			</logic:empty>

		</div>

	</body>
</lams:html>