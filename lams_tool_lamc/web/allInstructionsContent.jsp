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

<script language="JavaScript" type="text/JavaScript">
<!--

// The following method submit and the submit methods in the included jsp files submit the 
// form as required for the DispatchAction. All form submissions must go via these scripts - do not
// define an submit button with "dispatch" as the property or 
// "document.McAuthoringForm.dispatch.value=buttonResourceText" will not work

// general submit
// actionMethod: name of the method to be called in the DispatchAction
function submitMethod(actionMethod) {
	document.McAuthoringForm.dispatch.value=actionMethod; 
	document.McAuthoringForm.submit();
}

//-->     
</script>

</head>
<body onLoad="initTabs(); showTab('i');">


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

<html:form  action="/authoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">

<html:hidden property="dispatch"/>

 <!-- tab content one (basic)-->
<div id='content_b' class="tabbody content_b" >

		<h2><bean:message key="label.authoring.mc.basic"/></h2>
		
		<table align=center> 	  
		<tr>   
		<td class=error>
			<%@ include file="errorbox.jsp" %> <!-- show any error messages here -->
		</td>
		</tr> 
		</table>
		
		<div id="formtablecontainer1">
					<jsp:include page="BasicContent.jsp" />
		</div>
		<a href="javascript:;" class="button">Cancel</a>
			<!-- end of content_b -->
</div>
	

<div id='content_a'  class="tabbody content_a">
	<h2>Advanced Definitions </h2>
	
		<table align=center> 	  
		<tr>   
		<td class=error>
			<%@ include file="errorbox.jsp" %> <!-- show any error messages here -->
		</td>
		</tr> 
		</table>
	
	<div id="formtablecontainer2">
				<jsp:include page="AdvancedContent.jsp" />
	</div>
</div>


<div id='content_i'  class="tabbody content_i">
		<h2>Instructions</h2>

		<table align=center> 	  
		<tr>   
		<td class=error>
			<%@ include file="errorbox.jsp" %> <!-- show any error messages here -->
		</td>
		</tr> 
		</table>
		
		<div id="formtablecontainer3">
				<jsp:include page="InstructionsContent.jsp" />
		</div>
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







