
<%@ page language="java"%>

<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-c" prefix="c" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>    
    <title>All Learner Submission Details</title>
    
  	<link href="includes/css/aqua.css" rel="stylesheet" type="text/css">
  	
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
					<td>
					File Path: </td>
					<td><c:out value="${details.filePath}" /> 
					(<a href="monitoring.do?method=downloadFile&uuID=<c:out value="${details.uuID}" /> &versionID=<c:out value="${details.versionID}" /> ">Download</a>)
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
							<input type="hidden" name="toolSessionID" value=<c:out value='${sessionScope.toolSessionID}' /> >
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

