<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ include file="/includes/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
	"http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
  	<head>
  		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	    <title><c:out value="${NbExportForm.title}"/></title>
	  	
		<lams:css localLinkPath="../"/>
	</head>  
  	<body class="stripes">
	
			<div id="content">

			<h1>
				<c:out value="${NbExportForm.title}" escapeXml="false" />
			</h1>

				<p>
					<c:out value="${NbExportForm.content}" escapeXml="false" />
				</p>
				
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

			<div id="footer">
			</div>


	</body>
</lams:html>