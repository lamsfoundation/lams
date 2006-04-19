<%@include file="../sharing/share.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
	<title><fmt:message key="tool.display.name" /></title>
    <html:base />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<lams:css />

	<script language="javascript">
	var locked =  <c:out value="${learner.locked}"/>;
	function finish(){
		var lockOnFinished = <c:out value="${learner.contentLockOnFinished}"/>;
		var uploadFileNum = <c:choose><c:when test="${empty learner.filesUploaded}">0</c:when><c:otherwise>1</c:otherwise></c:choose>;
		var finishUrl= "<html:rewrite page='/learner.do?method=finish&toolSessionID=${learner.toolSessionID}'/>";
		if(lockOnFinished && uploadFileNum==0){
			if(confirm("<fmt:message key='learner.finish.without.upload'/>"))
				location.href= finishUrl;
			else
				return false;
		}else
			location.href= finishUrl;
	}
	</script>
	<html:javascript formName="SbmtLearnerForm" method="validateForm"/>

  </head>
  
  <body>
  	<H1><c:out value="${learner.contentTitle}" escapeXml="false"/></H1>
  	<H2><c:out value="${learner.contentInstruction}" escapeXml="false"/></H2>
  	<div id="datatablecontainer">
  	<!--Checks if the filesUploaded property of the SbmtLearnerForm is set -->
	<c:choose>
			<c:when test="${empty learner.filesUploaded}">
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
 				
				<c:forEach var="file" items="${learner.filesUploaded}">				
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
						<c:out value="${file.marks}" escapeXml="false"/><br>
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
      	
      	<!--File path row -->
        <tr>
          <td class="formlabel"><fmt:message key="label.learner.filePath"/></td>
          <td class="formcontrol"><html:file property="filePath" disabled="${learner.locked}" /></td>
        </tr>
        <!--File Description row -->
        <tr>
          <td class="formlabel"><fmt:message key="label.learner.fileDescription"/></td>
          <td class="formcontrol">
          <html:textarea property="fileDescription"  disabled="${learner.locked}" />
           </td>
        </tr>
        <tr>
          <td class="formcontrol"><html:submit disabled="${learner.locked}"><fmt:message key="label.learner.upload" /></html:submit></td>
          <td class="formcontrol">
          	  <html:button property="finished" onclick="finish()"  disabled="${learner.locked}">
          	  		<fmt:message key="label.learner.finished"/>
          	  </html:button>
          </td>
        </tr>
      </table>
    </html:form>
    </div>
  </body>
</html:html>
