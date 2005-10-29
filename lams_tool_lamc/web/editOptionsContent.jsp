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
<h2><bean:message key="label.authoring.mc.basic.editOptions"/></h2>

<table align=center> 	  
<tr>   
<td class="error">
	<%@ include file="errorbox.jsp" %> <!-- show any error messages here -->
</td>
</tr> 
</table>

<div id="formtablecontainer">
				<table align=center>
					<tr>
	 				 	<td class="formlabel" align=right> 
							<bean:message key="label.question"/>:
						</td>
						<td colspan=3>
			  				<input type="text" name="selectedQuestion" value="<c:out value="${sessionScope.selectedQuestion}"/>"   
					  			size="50" maxlength="255"> 
					  	</td>
					</tr>
					<tr>
	 				 	<td colspan=2 align=center>								
					  	</td>
					</tr>


					<tr>
					  	<td class="formlabel">  </td>
						<td class=input colspan=3>
							<h5> <b> <bean:message key="label.candidateAnswers"/> </h5> </b>

							 &nbsp&nbsp


							<h5> <b> <bean:message key="label.isCorrect"/>  </h5> </b>
							
							&nbsp&nbsp


							<h5> <b> <bean:message key="label.actions"/> </h5> </b>
						</td>
					</tr>

			  	 		<c:set var="optionIndex" scope="session" value="1"/>
			  	 		<c:set var="selectedOptionFound" scope="request" value="0"/>
						<c:forEach var="optionEntry" items="${sessionScope.mapOptionsContent}">
							  <c:if test="${optionEntry.key == 1}"> 			
								  <tr>
									  	<td class="formlabel"> <c:out value="Candidate Answer ${optionIndex}"/> : </td>

								  		<td class="input" colspan=3> 
								  			<input type="text" name="optionContent<c:out value="${optionIndex}"/>" value="<c:out value="${optionEntry.value}"/>"   
									  		size="50" maxlength="255"> 
									  		
									  		&nbsp&nbsp

									  	
			  				  	 		<c:set var="selectedOptionFound" scope="request" value="0"/>
			  							<c:forEach var="selectedOptionEntry" items="${sessionScope.mapSelectedOptions}">
											  <c:if test="${selectedOptionEntry.value == optionEntry.value}"> 			
						  					
													  	<select name="checkBoxSelected<c:out value="${optionIndex}"/>">
															<option value="Incorrect" > Incorrect </option>
															<option value="Correct" SELECTED>   Correct  </option>
														</select>
											
						  				  	 		<c:set var="selectedOptionFound" scope="request" value="1"/>
						 					</c:if> 			
										</c:forEach>
										
									  <c:if test="${selectedOptionFound == 0}"> 			
								  			
												  	<select name="checkBoxSelected<c:out value="${optionIndex}"/>">
														<option value="Incorrect" SELECTED> Incorrect </option>
														<option value="Correct">   Correct  </option>
													</select>
											
					 					</c:if> 			
									  											
									  		&nbsp&nbsp

	  										<html:submit property="addOption" styleClass="linkbutton" 
											onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
												<bean:message key="button.add"/>
											</html:submit>
									  	</td>
							 </tr>
							</c:if> 			
					  		<c:if test="${optionEntry.key > 1}"> 			
								<c:set var="optionIndex" scope="session" value="${optionIndex +1}"/>
								  <tr>
								  	<td> &nbsp </td>
								  	<td class="input" colspan=3> 
								  			<input type="text" name="optionContent<c:out value="${optionIndex}"/>" value="<c:out value="${optionEntry.value}"/>"   
									  		size="50" maxlength="255"> 

									  		&nbsp&nbsp

			  				  	 		<c:set var="selectedOptionFound" scope="request" value="0"/>
			  							<c:forEach var="selectedOptionEntry" items="${sessionScope.mapSelectedOptions}">
											  <c:if test="${selectedOptionEntry.value == optionEntry.value}"> 			

													  	<select name="checkBoxSelected<c:out value="${optionIndex}"/>">
															<option value="Incorrect" > Incorrect </option>
															<option value="Correct" SELECTED>   Correct  </option>
														</select>

						  				  	 		<c:set var="selectedOptionFound" scope="request" value="1"/>
						 					</c:if> 			
										</c:forEach>
										
									  <c:if test="${selectedOptionFound == 0}"> 			

												  	<select name="checkBoxSelected<c:out value="${optionIndex}"/>">
														<option value="Incorrect" SELECTED> Incorrect </option>
														<option value="Correct">   Correct  </option>
													</select>

					 					</c:if> 			
									  	
									  		&nbsp&nbsp

			 								<html:submit property="removeOption" styleClass="linkbutton"  onclick="javascript:document.forms[0].deletableOptionIndex.value=${optionIndex};"
			 								onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
												<bean:message key="button.remove"/>
											</html:submit>
									  	</td>
								  </tr>
							</c:if> 			
						  		<input type=hidden name="checkBoxSelected<c:out value="${optionIndex}"/>">
						</c:forEach>
							<html:hidden property="deletableOptionIndex"/>							
					<tr>
	 				 	<td colspan=4 align=center>								
							&nbsp&nbsp
					  	</td>
					</tr>
					<tr>
	 				 	<td colspan=4 align=center>								
							&nbsp&nbsp
					  	</td>
					</tr>



					<tr>
	 				 	<td class="formlabel" align=right> 
							<bean:message key="label.feedback.incorrect"/>
						</td>
						<td class="formcontrol" colspan=3>
							<FCK:editor id="richTextFeedbackInCorrect" basePath="/lams/fckeditor/">
								  <c:out value="${sessionScope.richTextFeedbackInCorrect}" escapeXml="false" />						  
							</FCK:editor>
						</td> 
					</tr>
					
					<tr>
	 				 	<td class="formlabel" align=right> 
							<bean:message key="label.feedback.correct"/>
						</td>
						<td class="formcontrol" colspan=3>
							<FCK:editor id="richTextFeedbackCorrect" basePath="/lams/fckeditor/">
								  <c:out value="${sessionScope.richTextFeedbackCorrect}" escapeXml="false" />						  
							</FCK:editor>
						</td> 
					</tr>

			
	 				 <tr>
	 				 <td> &nbsp</td>
	 				 <td class="input" align=left colspan=3>
							<html:submit property="doneOptions" styleClass="linkbutton" 
							onmouseover="pviiClassNew(this,'linkbutton')" onmouseout="pviiClassNew(this,'linkbutton')">
								<bean:message key="button.done"/>
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
<h2>Advanced Definitions</h2>
<div id="formtablecontainer">
		<jsp:include page="AdvancedContent.jsp" />
</div>

<!-- end of content_b -->
</div>

<!-- tab content 3 instructions -->
<div id='content_i'  class="tabbody content_i">
<h2>Instructions</h2>
<div id="formtablecontainer">
Instructions are here
</div>
	<hr>
<a href="javascript:;" class="button">Cancel</a>



<!-- end of content_a -->
</div>
</html:form>
<p></p>


<SCRIPT language="JavaScript"> 

<!--  addQuestion gets called everytime a new question content is added to the UI -->
	
	function removeQuestion(formIndex, questionIndex)
	{
		document.forms[formIndex].questionIndex.value=questionIndex;
		document.forms[formIndex].isRemoveQuestion.value='1';
		document.forms[formIndex].submit();
	}


 </SCRIPT>


</body>
</html:html>




