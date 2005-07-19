
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
		  <c:set var="details" value="${sessionScope.fileDetails}"/>
		  <c:set var="user" value="${sessionScope.user}" />
		  <c:set var="toolSessionID" value="${sessionScope.toolSessionID}" />		  
		  <b>Please assign a mark and a comment for the report by 
		  		 <c:out value="${user.login}" /> , <c:out value="${user.firstName}" />  <c:out value="${user.lastName}" /> 	
		  </b>
			  <span><p>			
					File Path: <c:out value="${details.filePath}" /><br/>
					File Description: <c:out value="${details.fileDescription}" /><br/>
					Date of Submission: <c:out value="${details.dateOfSubmission}" /><br/>
					<form name="commentForm" action="monitoring.do?method=updateMarks"  method="post">	
							 <input type="hidden" name="toolSessionID" value=<c:out value="${toolSessionID}"/> />						
							 <input type="hidden" name="reportID" value=<c:out value="${details.reportID}"/> />						
 							 <input type="hidden" name="userID" value=<c:out value="${user.userID}"/> />						 							 
                	Marks:<input type="text" name="marks" value=<c:out value="${details.marks}" /> ></br>
					Comments:<textarea name="comments" rows= "5" cols="20">
										<c:out value="${details.comments}" />
									 </textarea></br>					
							<input type="submit" value="Update Marks" />
					</form>
				</span>
  </body>
</html:html>

