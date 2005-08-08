
<%@ page language="java"%>

<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>    
    <title>All Learner Submission Details</title>
    
  	<link href="<html:rewrite page='/includes/css/aqua.css'/>" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/common.js'/>"></script>
  	
  </head>  
  <body>

		<table width="100%"  border="0" cellspacing="0" cellpadding="0">
		<c:forEach items="${report}"  var ="user" >		
			<c:set var="filesUploaded" value="${user.value}"/>
			<c:set var="first" value="true"/>
			<c:forEach items="${filesUploaded}"  var ="details" >			
				<span><p>			
					<c:if test="${first}">
						<c:set var="first" value="false"/>
					    <tr>
					    	<td colspan="2">
					    	<c:out value="${details.userDTO.firstName}"/> <c:out value="${details.userDTO.lastName}"/>,
					    	<c:out value="${details.userDTO.login}"/>, provides following submisstion:
					    	</td>
					    <tr>
				    </c:if>
				<tr>
					<td>File Path: </td>
						<td>
						File Path: </td><td><c:out value="${details.filePath}" /> 
						<c:set var="viewURL">
							<html:rewrite page="/download/?uuid=${details.uuID}&versionID=${details.versionID}&preferDownload=false"/>
						</c:set>
						<a href="javascript:launchInstructionsPopup('<c:out value='${viewURL}' escapeXml='false'/>')">
									<fmt:message key="label.view"/>
						</a>&nbsp;
						<c:set var="downloadURL">
							<html:rewrite page="/download/?uuid=${details.uuID}&versionID=${details.versionID}&preferDownload=true"/>
						</c:set>
						<a href="<c:out value='${downloadURL}' escapeXml='false'/>">
									<fmt:message key="label.download"/>
						</a>
						</td>					
					
				</tr>
				<tr>
					<td>File Description: </td><td><c:out value="${details.fileDescription}"  escapeXml="false"/></td>
				</tr>
				<tr>
					<td>Date of Submission: </td><td><c:out value="${details.dateOfSubmission}" /></td>
				</tr>
				<tr>
					<td>Marks:</td>
					<td> 		<c:choose>
										<c:when test="${empty details.marks}">
											<c:out value="Not Available"/>
										</c:when>
										<c:otherwise>
											<c:out value="${details.marks}" escapeXml="false"/>
										</c:otherwise>
									</c:choose>
					</td>
				</tr>			
				<tr>
					<td>Comments:</td>
					<td>
									<c:choose>
										<c:when test="${empty details.comments}">
											<c:out value="Not Available"/>								
										</c:when>
										<c:otherwise>
											<c:out value="${details.comments}" escapeXml="false"/>
										</c:otherwise>
									</c:choose>
					</td>
				</tr>
		
				<tr>
					<td colspan="2">
					<form action="monitoring.do?method=markFile" method="post">
							<input type="hidden" name="detailID" value=<c:out value='${details.submissionID}' /> >
							<input type="hidden" name="reportID" value=<c:out value='${details.reportID}' /> >
							<input type="hidden" name="userID" value=<c:out value='${details.userID}' /> >
							<input type="hidden" name="toolSessionID" value=<c:out value='${toolSessionID}' /> >
							<input type="submit" value="Update Marks"/>
					</form>
					</td>
				</tr>
				</span>
			</c:forEach>
		</c:forEach>
		</table>						
  </body>
</html:html>

