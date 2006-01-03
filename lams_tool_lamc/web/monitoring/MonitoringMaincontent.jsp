<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic-el" prefix="logic-el" %>
<%@ taglib uri="tags-c" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD hTML 4.01 Transitional//EN">
<html:html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<title>MCQ tool</title>
	
	<script lang="javascript">
		var imgRoot="<c:out value="${lams}"/>images/";
		var themeName="aqua";
	</script>
	
	<script type="text/javascript" src="<c:out value="${lams}"/>includes/javascript/tabcontroller.js"></script>
	<script src="<c:out value="${lams}"/>includes/javascript/common.js"></script>
	
	<!-- this is the custom CSS for hte tool -->
	<link href="<c:out value="${tool}"/>css/tool_custom.css" rel="stylesheet" type="text/css">
	<lams:css/>
	<script language="JavaScript" type="text/JavaScript">

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

	<b> <bean:message key="label.monitoring"/> </b>
	
		<!-- start tabs -->
		<!-- tab holder table -->
		<table border="0" cellspacing="0" cellpadding="0">
		  <tr>
			    <td valign="bottom">
					<!-- table for tab 1 (summary)-->
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td><a href="#"  onClick="showTab('b');return false;" ><img src="author_page/images/aqua_tab_s_left.gif" name="tab_left_b" width="8" height="25" border="0" id="tab_left_b"/></a></td>
						<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_b"  onClick="showTab('b');return false;" ><label for="b" accesskey="b"><a href="#" onClick="showTab('b');return false;" id="b" >Summary</a></label></td>
						<td><a href="#" onClick="showTab('b');return false;"><img src="author_page/images/aqua_tab_s_right.gif"  name="tab_right_b" width="8" height="25" border="0" id="tab_right_b"/></a></td>
					  </tr>
					</table>
				</td>
				
			    <td valign="bottom">
					<!-- table for tab 2 (instructions) -->
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td><a href="#" onClick="showTab('a');return false;"><img src="author_page/images/aqua_tab_left.gif" name="tab_left_a" width="8" height="22" border="0" id="tab_left_a" /></a></td>
						<td class="tab tabcentre" width="90" id="tab_tbl_centre_a" onClick="showTab('a');return false;"><label for="a" accesskey="a"><a href="#" onClick="showTab('a');return false;" id="a" >Instructions</a></label></td>
						<td><a href="#" onClick="showTab('a');return false;"><img src="author_page/images/aqua_tab_right.gif" name="tab_right_a" width="9" height="22" border="0" id="tab_right_a" /></a></td>
					  </tr>
					</table>
				</td>

			    <td valign="bottom">
					<!-- table for tab 3 (edit activity) -->
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td ><a href="#" onClick="showTab('i');return false;"><img border="0" src="author_page/images/aqua_tab_left.gif" width="8" height="22" id="tab_left_i"   name="tab_left_i" /></a></td>
						<td class="tab tabcentre" width="90" id="tab_tbl_centre_i"  onClick="showTab('i');return false;" ><label for="i" accesskey="i"><a href="#" onClick="showTab('i');return false;" id="i" >Edit Activity</a></label></td>
						<td ><a href="#" onClick="showTab('i');return false;"><img src="author_page/images/aqua_tab_right.gif"  width="9" height="22" border="0" id="tab_right_i"  name="tab_right_i"/></a></td>
					  </tr>
					</table>
				</td>

			    <td valign="bottom">
					<!-- table for tab 4 (statistics) -->
					<table border="0" cellspacing="0" cellpadding="0">
					  <tr>
						<td ><a href="#" onClick="showTab('s');return false;"><img border="0" src="author_page/images/aqua_tab_left.gif" width="8" height="22" id="tab_left_s"   name="tab_left_s" /></a></td>
						<td class="tab tabcentre" width="90" id="tab_tbl_centre_s"  onClick="showTab('s');return false;" ><label for="s" accesskey="s"><a href="#" onClick="showTab('s');return false;" id="s" >Statistics</a></label></td>
						<td ><a href="#" onClick="showTab('s');return false;"><img src="author_page/images/aqua_tab_right.gif"  width="9" height="22" border="0" id="tab_right_s"  name="tab_right_s"/></a></td>
					  </tr>
					</table>
				</td>
		  </tr>
		</table>
	
	<!-- end tab buttons -->
	<html:form  action="/monitoring?validate=false" enctype="multipart/form-data" method="POST" target="_self">
	 
		<html:hidden property="dispatch"/>
		<div id='content_b' class="tabbody content_b" >
		
				<h2><bean:message key="button.summary"/></h2>
					<table align=center> 	  
					<tr>   
					<td NOWRAP class=error>
						<%@ include file="/Errorbox.jsp" %> <!-- show any error messages here -->
					</td>
					</tr> 
					</table>
		
				<div id="formtablecontainer1">
							<jsp:include page="/monitoring/SummaryContent.jsp" />
				</div>
		</div>
			
		<div id='content_a'  class="tabbody content_a">
			<h2><bean:message key="button.instructions"/></h2>
			
			<table align=center> 	  
			<tr>   
			<td NOWRAP class=error>
				<%@ include file="/Errorbox.jsp" %> <!-- show any error messages here -->
			</td>
			</tr> 
			</table>
			
			<div id="formtablecontainer2">
						<jsp:include page="/monitoring/InstructionsContent.jsp" />
			</div>
		</div>
		
		
		<div id='content_i'  class="tabbody content_i">
				<h2><bean:message key="button.editActivity"/></h2>
				
				<table align=center> 	  
				<tr>   
				<td NOWRAP class=error>
					<%@ include file="/Errorbox.jsp" %> <!-- show any error messages here -->
				</td>
				</tr> 
				</table>
				
				<div id="formtablecontainer3">
						<jsp:include page="/monitoring/EditActivityContent.jsp" />
				</div>
		</div>
		
		<div id='content_s'  class="tabbody content_i">
				<h2><bean:message key="button.stats"/></h2>
				
				<table align=center> 	  
				<tr>   
				<td NOWRAP class=error>
					<%@ include file="/Errorbox.jsp" %> <!-- show any error messages here -->
				</td>
				</tr> 
				</table>
				
				<div id="formtablecontainer4">
						<jsp:include page="/monitoring/Stats.jsp" />
				</div>
		</div>
	
	</html:form>
	<p></p>
</body>
</html:html>







