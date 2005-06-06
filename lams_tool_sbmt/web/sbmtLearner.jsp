
<%@ page language="java"%>

<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-c" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    <html:base />
    
    <title>Submit Files</title>    
    
  </head>
  
  <body>
  	<H1><c:out value="${sessionScope.title}"/></H1>
  	<H2><c:out value="${sessionScope.instructions}"/></H2>
  	
  	<!--Checks if the filesUploaded property of the SbmtLearnerForm is set -->
	<logic:present name="SbmtLearnerForm" property="filesUploaded">
		<bean:size id="size" name="SbmtLearnerForm" property="filesUploaded"/>
			<logic:equal name="size" value="0">
					<center><b>No files have been uploaded yet.</b><p>Only one file can be uploaded at a time - Maximum file size is 1.0 MB.</p></center>
			</logic:equal>
			
			<logic:greaterThan name="size" value="0">
				<logic:iterate id="filesUploaded" name="SbmtLearnerForm" property="filesUploaded">				
				<span>
				<p>
				<!--First Row displaying the name of the File -->
				<b><bean:message key="label.learner.fileName"/></b>
				<bean:write name="filesUploaded" property="filePath"/><br>
				
				<!--Second Row displaying the description of the File -->
				<b><bean:message key="label.learner.fileDescription"/></b>
				<bean:write name="filesUploaded" property="fileDescription"/><br>
				
				<!--Third row displaying the date of submission of the File -->
				<b><bean:message key="label.learner.time"/></b>
				<bean:write name="filesUploaded" property="dateOfSubmission"/><br>
				
				<!--Fourth row displaying the comments -->
				<b><bean:message key="label.learner.comments"/></b>
				<logic:notEmpty name="filesUploaded" property="comments">
						<bean:write name="filesUploaded" property="comments"/><br>
				</logic:notEmpty>
				<logic:empty  name="filesUploaded" property="comments">
						<bean:message key="label.learner.notAvailable"/><br>
				</logic:empty>
				
				<!--Fifth row displaying the marks-->
				<b><bean:message key="label.learner.marks"/></b>
				<logic:notEmpty name="filesUploaded" property="marks">
						<bean:write name="filesUploaded" property="marks"/><br>
				</logic:notEmpty>
				<logic:empty name="filesUploaded" property="marks">
					<bean:message key="label.learner.notAvailable"/><br>
				</logic:empty>
				</p>
			</span>
				</logic:iterate>
			</logic:greaterThan>
	</logic:present>
  	
  	
    <html:form action="/tool/sbmt/learner?method=uploadFile" method="post" focus="login">
      <p><html:errors/></p>
      <table width="100%" height="190" border="0"
      		 align="center" cellpadding="5" cellspacing="0"
      		 summary="This table is being used for layout purposes only">
      	<!-- Hidden fields -->
      	<html:hidden property="contentID" value="${requestScope.contentID}"/>
      	<html:hidden property="userID" value ="${requestScope.userID}"/>
      	
      	<!--File path row -->
        <tr>
          <td><bean:message key="label.learner.filePath"/></td>
          <td><html:file property="filePath" /></td>
        </tr>
        <!--File Description row -->
        <tr>
          <td><bean:message key="label.learner.fileDescription"/></td>
          <td><html:text property="fileDescription" /></td>
        </tr>
        <tr>
          <td><html:submit><bean:message key="label.learner.upload"/></html:submit></td>
          <td>
          	  <html:button property="finished" onclick="window.close()">
          	  		<bean:message key="label.learner.finished"/>
          	  </html:button>
          </td>
        </tr>
      </table>
    </html:form>
  </body>
</html:html>
