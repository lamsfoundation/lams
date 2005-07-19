
<%@ page language="java"%>

<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-c" prefix="c" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>    
    <title>Files Submitted</title>
  </head>  
  <body>	  
	<c:set var="filesUploaded" value ="${sessionScope.userReport}"/>
	<c:set var="user" value="${sessionScope.user}" />
	<b>Following files have been submitted by 
		 <c:out value="${user.login}" /> , <c:out value="${user.firstName}" />  <c:out value="${user.lastName}" /> 	
	</b>

	<c:forEach items="${filesUploaded}"  var ="details" >			
		<span><p>			

			File Path: <c:out value="${details.filePath}" /><br/>
			File Description: <c:out value="${details.fileDescription}" /><br/>
			Date of Submission: <c:out value="${details.dateOfSubmission}" /><br/>
			Comments:
							<c:choose>
								<c:when test="${empty details.comments}">
									<c:out value="Not Available"/><br/>									
								</c:when>
								<c:otherwise>
									<c:out value="${details.comments}" /><br/>
								</c:otherwise>
							</c:choose>
			Marks: 
							<c:choose>
								<c:when test="${empty details.marks}">
									<c:out value="Not Available"/></p><br/>
								</c:when>
								<c:otherwise>
									<c:out value="${details.marks}" /></p></br>
								</c:otherwise>
							</c:choose>
			<form action="monitoring.do?method=markFile" method="post">
					<input type="hidden" name="detailID" value=<c:out value='${details.submissionID}' /> >
					<input type="hidden" name="reportID" value=<c:out value='${details.reportID}' /> >
					<input type="hidden" name="toolSessionID" value=<c:out value='${sessionScope.toolSessionID}' /> >
					<input type="hidden" name="userID" value=<c:out value='${user.userID}' /> >
					<input type="submit" value="Update Marks"/>
			</form>
		</span>
	</c:forEach>
  </body>
</html:html>

