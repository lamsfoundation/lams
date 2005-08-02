
<%@ page language="java"%>
<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    <html:base />
    
    <title>Submission tool for Learner</title>    
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
  	<link href="<html:rewrite page='/includes/css/aqua.css'/>" rel="stylesheet" type="text/css">

	<script language="javascript">
	var lockOnFinished = <c:out value="${learner.contentLockOnFinished}"/>;
	var uploadFileNum = <c:choose><c:when test="${empty filesUploaded}">0</c:when><c:otherwise>1</c:otherwise></c:choose>;
	function finish(){
		if(lockOnFinished && uploadFileNum==0){
			if(confirm("<fmt:message key='learner.finish.without.upload'/>"))
				window.close();
			else
				return false;
		}else
			window.close();
	}
	</script>
	<html:javascript formName="SbmtLearnerForm" method="validateForm"/>

  </head>
  
  <body>
  	<H1><c:out value="${learner.contentTitle}"/></H1>
  	<H2><c:out value="${learner.contentInstruction}" escapeXml="false"/></H2>
  	<div id="datatablecontainer">
  	<!--Checks if the filesUploaded property of the SbmtLearnerForm is set -->
	<c:choose>
			<c:when test="${empty filesUploaded}">
					<center><b>No files have been uploaded yet.</b><p>Only one file can be uploaded at a time - Maximum file size is 1.0 MB.</p></center>
			</c:when>
					
			<c:otherwise>
				<table width="100%"  border="0" cellspacing="0" cellpadding="0">
				<!-- Header information
  				  <tr>
    				<th scope="col">A colum header </th>
 				   <th scope="col">The other one </th>
 				 </tr>
 				-->
 				
				<c:forEach var="file" items="${filesUploaded}">				
				<tr>
				<!--First Row displaying the name of the File -->
				<td><BR><b><fmt:message key="label.learner.fileName"/></b></td>
				<td><BR><c:out value="${file.filePath}"/></td>
				</tr>
				<tr>
				<!--Second Row displaying the description of the File -->
				<td><b><fmt:message key="label.learner.fileDescription"/></b></td>
				<td><c:out value="${file.fileDescription}" escapeXml="false"/></td>
				</tr>
				<tr>
				<!--Third row displaying the date of submission of the File -->
				<td><b><fmt:message key="label.learner.time"/></b></td>
				<td><c:out value="${file.dateOfSubmission}"/></td>
				</tr>
				<tr>
				<!--Fourth row displaying the comments -->
				<td><b><fmt:message key="label.learner.comments"/></b></td>
				<td>
				<c:choose>
					<c:when test="${empty file.comments}">	
						<fmt:message key="label.learner.notAvailable"/><br>
					</c:when>
					<c:otherwise>
						<c:out value="${file.comments}" escapeXml="false"/><br>
					</c:otherwise>
				</c:choose>
				</td>
				</tr>
				<tr>
				<!--Fifth row displaying the marks-->
				<td><b><fmt:message key="label.learner.marks"/></b></td>
				<td>
				<c:choose>
					<c:when test="${empty file.comments}">	
						<fmt:message key="label.learner.notAvailable"/><br>
					</c:when>
					<c:otherwise>
						<c:out value="{$file.marks}" escapeXml="false"/><br>
					</c:otherwise>
				</c:choose>
				</td>
				</tr>
				</c:forEach>
			</table>
			</c:otherwise>
	</c:choose>
	</div>
					
  	<div id="formtablecontainer">
    <html:form action="/learner?method=uploadFile" method="post" enctype="multipart/form-data" focus="filePath"  onsubmit="return validateForm(this);">
      <p><html:errors/></p>
      <table class="forms">
      	<!-- Hidden fields -->
      	<html:hidden property="toolSessionID" value="${learner.toolSessionID}"/>
      	<html:hidden property="userID" value ="${learner.userID}"/>
      	
      	<!--File path row -->
        <tr>
          <td class="formlabel"><fmt:message key="label.learner.filePath"/></td>
          <td class="formcontrol"><html:file property="filePath" /></td>
        </tr>
        <!--File Description row -->
        <tr>
          <td class="formlabel"><fmt:message key="label.learner.fileDescription"/></td>
          <td class="formcontrol">
          <html:textarea property="fileDescription" />
           </td>
        </tr>
        <tr>
          <td class="formcontrol"><html:submit><fmt:message key="label.learner.upload"/></html:submit></td>
          <td class="formcontrol">
          	  <html:button property="finished" onclick="finish()">
          	  		<fmt:message key="label.learner.finished"/>
          	  </html:button>
          </td>
        </tr>
      </table>
    </html:form>
    </div>
  </body>
</html:html>
