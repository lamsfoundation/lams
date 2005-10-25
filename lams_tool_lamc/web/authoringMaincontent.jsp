<%@ taglib uri="/WEB-INF/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic-el.tld" prefix="logic-el" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="fck-editor" prefix="FCK" %>

<%
String protocol = request.getProtocol();
if(protocol.startsWith("HTTPS")){
	protocol = "https://";
}else{
	protocol = "http://";
}
String root = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
String pathToLams = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/../..";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Tool</title>
<script type="text/javascript" src="<%=root%>author_page/js/tabcontroller.js"></script>
<script src="<%=pathToLams%>/includes/javascript/common.js"></script>
<!-- this is the custom CSS for hte tool -->
<link href="<%=root%>author_page/css/tool_custom.css" rel="stylesheet" type="text/css">
<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
<link href="<%=root%>author_page/css/aqua.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);
//-->
</script>
</head>
<body onLoad="initTabs()">

<h1><bean:message key="label.authoring.mc"/></h1>
    
    <!-- start tabs -->
<!-- tab holder table -->
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="bottom">
	<!-- table for tab 1 (basic)-->
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td><a href="#"  onClick="showTab('b');return false;" ><img src="author_page/images/aqua_tab_s_left.gif" name="tab_left_b" width="8" height="25" border="0" id="tab_left_b"/></a></td>
		<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_b"  onClick="showTab('b');return false;" ><label for="b" accesskey="b"><a href="#" onClick="showTab('b');return false;" id="b" >Basic</a></label></td>
		<td><a href="#" onClick="showTab('b');return false;"><img src="author_page/images/aqua_tab_s_right.gif"  name="tab_right_b" width="8" height="25" border="0" id="tab_right_b"/></a></td>
	  </tr>
	</table>

</td>
    <td valign="bottom">
	<!-- table for tab 2 (advanced) -->
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td><a href="#" onClick="showTab('a');return false;"><img src="author_page/images/aqua_tab_left.gif" name="tab_left_a" width="8" height="22" border="0" id="tab_left_a" /></a></td>
		<td class="tab tabcentre" width="90" id="tab_tbl_centre_a" onClick="showTab('a');return false;"><label for="a" accesskey="a"><a href="#" onClick="showTab('a');return false;" id="a" >Advanced</a></label></td>
		<td><a href="#" onClick="showTab('a');return false;"><img src="author_page/images/aqua_tab_right.gif" name="tab_right_a" width="9" height="22" border="0" id="tab_right_a" /></a></td>
	  </tr>
	</table>

</td>
    <td valign="bottom">
	<!-- table for ab 3 (instructions) -->
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td ><a href="#" onClick="showTab('i');return false;"><img border="0" src="author_page/images/aqua_tab_left.gif" width="8" height="22" id="tab_left_i"   name="tab_left_i" /></a></td>
		<td class="tab tabcentre" width="90" id="tab_tbl_centre_i"  onClick="showTab('i');return false;" ><label for="i" accesskey="i"><a href="#" onClick="showTab('i');return false;" id="i" >Instructions</a></label></td>
		<td ><a href="#" onClick="showTab('i');return false;"><img src="author_page/images/aqua_tab_right.gif"  width="9" height="22" border="0" id="tab_right_i"  name="tab_right_i"/></a></td>
	  </tr>
	</table>

</td>
  </tr>
</table>

<!-- end tab buttons -->

<html:form  action="/authoring?method=loadQ&validate=false" enctype="multipart/form-data" method="POST" target="_self">
 <!-- tab content one (basic)-->
<div id='content_b' class="tabbody content_b" >
<h2><bean:message key="label.authoring.mc.basic"/></h2>

<table align=center> 	  
<tr>   
<td class="error">
	<%@ include file="errorbox.jsp" %> <!-- show any error messages here -->
</td>
</tr> 
</table>

<div id="formtablecontainer">
					<table class="forms" align=center>
 						<tr> 
					 		<td class="formlabel"> <bean:message key="label.authoring.title"/>: </td>
							<td class="formcontrol">
								<FCK:editor id="richTextTitle" basePath="/lams/fckeditor/">
									  <c:out value="${sessionScope.richTextTitle}" escapeXml="false" />						  
								</FCK:editor>
							</td> 
					  	</tr>
					  	
					  	<tr> 
					 		<td class="formlabel"> <bean:message key="label.authoring.instructions"/>: </td>
							<td class="formcontrol">
								<FCK:editor id="richTextInstructions" basePath="/lams/fckeditor/">
									  <c:out value="${sessionScope.richTextInstructions}" escapeXml="false" />						  
								</FCK:editor>
							</td>
						</tr>
				

			  	 		<c:set var="queIndex" scope="session" value="1"/>
						<c:forEach var="questionEntry" items="${sessionScope.mapQuestionsContent}">
								
									  <c:if test="${questionEntry.key == 1}"> 			
									    <tr>
										  	<td class="formlabel"> <c:out value="Question ${queIndex}"/> : </td>
										  	
									  		<td class="input"> 
									  			<input type="text" name="questionContent<c:out value="${queIndex}"/>" value="<c:out value="${questionEntry.value}"/>"   
										  		size="50" maxlength="255" > 
										  	
										  		&nbsp&nbsp&nbsp
										  		
			  										<html:submit property="addQuestion" styleClass="linkbutton" 
													onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
														<bean:message key="button.addNewQuestion"/>
													</html:submit>
	
												&nbsp&nbsp&nbsp	
	
					 								<html:submit property="editOptions" styleClass="linkbutton"  onclick="javascript:document.forms[0].questionIndex.value=${queIndex};"
					 								onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
														<bean:message key="label.edit"/>
													</html:submit>
											</td>
										  </tr>
									</c:if> 			
							  		<c:if test="${questionEntry.key > 1}"> 			
										<c:set var="queIndex" scope="session" value="${queIndex +1}"/>
										  <tr>
										  	<td class="formlabel"> <c:out value="Question ${queIndex}"/> : </td>
										  	
										  		<td class="input"> 
										  			<input type="text" name="questionContent<c:out value="${queIndex}"/>" value="<c:out value="${questionEntry.value}"/>"   
											  		size="50" maxlength="255" > 
											
												&nbsp&nbsp&nbsp
				 								<html:submit property="removeQuestion" styleClass="linkbutton"  onclick="javascript:document.forms[0].questionIndex.value=${queIndex};"
					 								onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
														<bean:message key="button.removeQuestion"/>
											
													</html:submit>
												&nbsp&nbsp&nbsp
																				  										  		
					 								<html:submit property="editOptions" styleClass="linkbutton"  onclick="javascript:document.forms[0].questionIndex.value=${queIndex};"
					 								onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
														<bean:message key="label.edit"/>
													</html:submit>
											  	</td>
										  </tr>
									</c:if> 
						</c:forEach>
							<html:hidden property="questionIndex"/>
					<tr>
	 				 	<td colspan=2 align=center>								
							&nbsp&nbsp
					  	</td>
					</tr>
	 				 <tr>
	 				 	<td colspan=2 align=right>								
							<html:submit property="submitQuestions" styleClass="linkbutton" 
							onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="button.submit"/>
							</html:submit>	 				 		  										  		
		 			  	</td>
	 				 </tr>
	 				</table> 	 
					
</div>
<hr>
<a href="javascript:;" class="button">Cancel</a>

<!-- end of content_b -->
</div>

<!-- tab content 2 Advanced-->
<div id='content_a'  class="tabbody content_a">
<h2>Advanced Definitions </h2>
<div id="formtablecontainer">
			<table class="forms">
				<tr>
	      			<td class="formlabel">
		      			<bean:message key="radiobox.synchInMonitor"/>:
					</td>
					<td>
						<html:radio property="synchInMonitor" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="synchInMonitor" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>
		         </tr>
		         
				<tr>
					<td class="formlabel">
		      			<bean:message key="radiobox.usernameVisible"/>:
					</td>
					<td>
						<html:radio property="usernameVisible" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="usernameVisible" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>
		         </tr>
		         
   				<tr>
					<td class="formlabel">
		      			<bean:message key="radiobox.questionsSequenced"/>:		      			
					</td>
					<td>
						<html:radio property="questionsSequenced" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="questionsSequenced" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>
		        </tr>
		         
		         <tr>
					<td class="formlabel">
		      			<bean:message key="radiobox.retries"/>:		      			
					</td>
					<td>
						<html:radio property="retries" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="retries" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>

				</tr>	      			
				<tr>
	      			<td class="formlabel">
		      			<bean:message key="radiobox.passmark"/>:		      			
					</td>
					
			 		<td> 
			 			<html:text property="passmark" size="60" maxlength="100"/>
			 		</td>

		        </tr>
		        
   				<tr>
					<td class="formlabel">
		      			<bean:message key="radiobox.showFeedback"/>:		      			
					</td>
					<td>
						<html:radio property="showFeedback" value="ON">
						 <bean:message key="option.on"/>
						</html:radio>

						<html:radio property="showFeedback" value="OFF">
						 <bean:message key="option.off"/>
						</html:radio>
	      			</td>
		        </tr>
		         
		         <tr> 
					<td class="formlabel">
			 			<bean:message key="label.report.title"/>: </td>
			 		<td> 
			 			<html:text property="reportTitle" size="60" maxlength="100"/>
			 		</td>
			  	</tr>
			  	
			  	<tr> 
					<td class="formlabel">
			 			<bean:message key="label.monitoringReport.title"/>: 
			 		</td>
			 		<td> 
			 			<html:text property="monitoringReportTitle" size="60" maxlength="100"/>
			 		</td>
			  	</tr>
			  	
			  	<tr> 
					<td class="formlabel">
			 			<bean:message key="label.report.endLearningMessage"/>: 
			 		</td>
			 		<td> 
			 			<html:text property="endLearningMessage" size="60" maxlength="100"/>
			 		</td>
			  	</tr>
			  	
		  		<tr>
 				 	<td colspan=2 align=center>								
						&nbsp&nbsp
				  	</td>
				</tr>
				<tr>
					 <td class="body" colspan=2> 
						 <html:submit property="advancedTabDone" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
							<bean:message key="button.done"/>
						</html:submit>
					</td> 
 				 </tr>
			</table>	  	

</div>

<!-- end of content_b -->
</div>

<!-- tab content 3 instructions -->
<div id='content_i'  class="tabbody content_i">
<h2>Instructions</h2>
<div id="formtablecontainer">
		<table class="forms">
				<tr> 
					<td class="formlabel">
          				<fmt:message key="label.offlineInstructions" />:
          			</td>
					<td class="formcontrol">
					<FCK:editor id="richTextOfflineInstructions" basePath="/lams/fckeditor/">
						  <c:out value="${richTextOfflineInstructions}" escapeXml="false" />						  
					</FCK:editor>
					</td> 
				</tr>

				<tr> 
					<td class="formlabel">
          				<fmt:message key="label.offlineFiles" />
          			</td>
          			<td NOWRAP> 
						<html:file  property="theOfflineFile"></html:file>
					 	<html:submit property="submitOfflineFile" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="label.upload"/>
						</html:submit>
					</td> 

				</tr>
				<tr> 
					<td class="formlabel">
          				<fmt:message key="label.uploadedOfflineFiles" />
          			</td>
					<td class="formlabel">
						<c:forEach var='item' items='${sessionScope.listUploadedOfflineFileNames}'>
				            <li><c:out value='${item}'/></li>
	         			</c:forEach>
					</td> 
				</tr>
          		<tr> 
					<td class="formlabel">
          				<fmt:message key="label.onlineInstructions" />
          			</td>
					<td class="formcontrol">
					<FCK:editor id="richTextOnlineInstructions" basePath="/lams/fckeditor/">
						  <c:out value="${sessionScope.richTextOnlineInstructions}" escapeXml="false" />						  
					</FCK:editor>
					</td> 
				</tr>
				
				<tr> 
					<td class="formlabel">
          				<fmt:message key="label.onlineFiles" />
          			</td>
          			<td NOWRAP> 
						<html:file  property="theOnlineFile"></html:file>
					 	<html:submit property="submitOnlineFile" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="label.upload"/>
						</html:submit>
					</td> 
				
				</tr>
				<tr>
					<td class="formlabel">
          				<fmt:message key="label.uploadedOnlineFiles" />
          			</td>
					<td class="formlabel">
						<c:forEach var='item' items='${sessionScope.listUploadedOnlineFileNames}'>
				            <li><c:out value='${item}'/></li>
	         			</c:forEach>
					</td> 
				</tr>
		  		<tr>
 				 	<td colspan=2 align=center>								
						&nbsp&nbsp
				  	</td>
				</tr>
				<tr>
					 <td class="body" colspan=2> 
						 <html:submit property="instructionsTabDone" styleClass="linkbutton" onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
							<bean:message key="button.done"/>
						</html:submit>
					</td> 
 				 </tr>
			</table>	  	
</div>

	<hr>
<a href="javascript:;" class="button">Cancel</a>



<!-- end of content_a -->
</div>
</html:form>
<p></p>


</body>
</html:html>







