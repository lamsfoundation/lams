
<%@ page language="java"%>

<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-c" prefix="c" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>    
    <title>Files Submitted</title>
  	<link href="<html:rewrite page='/includes/css/aqua.css'/>" rel="stylesheet" type="text/css">

  </head>  
  <body>	  
	<c:set var="filesUploaded" value ="${sessionScope.userReport}"/>
	<c:set var="user" value="${sessionScope.user}" />
	<b>Following files have been submitted by 
		 <c:out value="${user.login}" /> , <c:out value="${user.firstName}" />  <c:out value="${user.lastName}" /> 	
	</b>
	</p>
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
	<c:forEach items="${filesUploaded}"  var ="details" >			
		<span><p>			
		<tr>
			<td>
			File Path: </td><td><c:out value="${details.filePath}" /> 
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
					<input type="hidden" name="toolSessionID" value=<c:out value='${sessionScope.toolSessionID}' /> >
					<input type="hidden" name="userID" value=<c:out value='${user.userID}' /> >
					<input type="submit" value="Update Marks"/>
			</form>
			</td>
		</tr>
		</span>
	</c:forEach>
</table>						
  </body>
</html:html>

