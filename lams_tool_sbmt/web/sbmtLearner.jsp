
<%@ page language="java"%>
<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-c" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    <html:base />
    
    <title><c:out value="${sessionScope.title}"/></title>    
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
	<link href="css/aqua.css" rel="stylesheet" type="text/css">

  </head>
  
  <body>
  	<H1><c:out value="${sessionScope.title}"/></H1>
  	<H2><c:out value="${sessionScope.instructions}"/></H2>
  	<div id="datatablecontainer">
  	<!--Checks if the filesUploaded property of the SbmtLearnerForm is set -->
	<logic:present name="SbmtLearnerForm" property="filesUploaded">
		<bean:size id="size" name="SbmtLearnerForm" property="filesUploaded"/>
			<logic:equal name="size" value="0">
					<center><b>No files have been uploaded yet.</b><p>Only one file can be uploaded at a time - Maximum file size is 1.0 MB.</p></center>
			</logic:equal>
			
			<logic:greaterThan name="size" value="0">
				<table width="100%"  border="0" cellspacing="0" cellpadding="0">
				<!-- Header information
  				  <tr>
    				<th scope="col">A colum header </th>
 				   <th scope="col">The other one </th>
 				 </tr>
 				-->
 				
				<logic:iterate id="filesUploaded" name="SbmtLearnerForm" property="filesUploaded">				
				<tr>
				<!--First Row displaying the name of the File -->
				<td><BR><b><bean:message key="label.learner.fileName"/></b></td>
				<td><BR><bean:write name="filesUploaded" property="filePath"/></td>
				</tr>
				<tr>
				<!--Second Row displaying the description of the File -->
				<td><b><bean:message key="label.learner.fileDescription"/></b></td>
				<td><bean:write name="filesUploaded" property="fileDescription"/></td>
				</tr>
				<tr>
				<!--Third row displaying the date of submission of the File -->
				<td><b><bean:message key="label.learner.time"/></b></td>
				<td><bean:write name="filesUploaded" property="dateOfSubmission"/></td>
				</tr>
				<tr>
				<!--Fourth row displaying the comments -->
				<td><b><bean:message key="label.learner.comments"/></b></td>
				<td><logic:notEmpty name="filesUploaded" property="comments">
						<bean:write name="filesUploaded" property="comments"/><br>
				</logic:notEmpty>
				<logic:empty  name="filesUploaded" property="comments">
						<bean:message key="label.learner.notAvailable"/><br>
				</logic:empty>
				</td>
				</tr>
				<tr>
				<!--Fifth row displaying the marks-->
				<td><b><bean:message key="label.learner.marks"/></b></td>
				<td><logic:notEmpty name="filesUploaded" property="marks">
						<bean:write name="filesUploaded" property="marks"/><br>
				</logic:notEmpty>
				<logic:empty name="filesUploaded" property="marks">
					<bean:message key="label.learner.notAvailable"/><br>
				</logic:empty>
				</td>
				</tr>
				</logic:iterate>
			</table>
			</logic:greaterThan>
	</logic:present>
	</div>
					
  	<div id="formtablecontainer">
    <html:form action="/learner?method=uploadFile" method="post" >
      <p><html:errors/></p>
      <table class="forms">
      	<!-- Hidden fields -->
      	<html:hidden property="toolSessionID" value="${param.toolSessionID}"/>
      	<html:hidden property="userID" value ="${param.userID}"/>
      	
      	<!--File path row -->
        <tr>
          <td class="formlabel"><bean:message key="label.learner.filePath"/></td>
          <td class="formcontrol"><html:file property="filePath" /></td>
        </tr>
        <!--File Description row -->
        <tr>
          <td class="formlabel"><bean:message key="label.learner.fileDescription"/></td>
          <td class="formcontrol">
            <FCK:editor id="fileDescription" 
    			basePath="/lams/fckEditor/"
    			height="150"    
    			width="85%">                                     
			</FCK:editor>

            </td>
        </tr>
        <tr>
          <td class="formcontrol"><html:submit><bean:message key="label.learner.upload"/></html:submit></td>
          <td class="formcontrol">
          	  <html:button property="finished" onclick="window.close()">
          	  		<bean:message key="label.learner.finished"/>
          	  </html:button>
          </td>
        </tr>
      </table>
    </html:form>
    </div>
  </body>
</html:html>
