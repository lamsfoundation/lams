<%@ taglib uri="/WEB-INF/struts/struts-html-el.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/jstl/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/jstl/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic" %>
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
<title>Noticeboard tool</title>
<script type="text/javascript" src="<%=root%>author_page/js/tabcontroller.js"></script>
<script src="<%=pathToLams%>/common.js"></script>
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
<html:form action="/authoringv2" target="_self" enctype="multipart/form-data">
<h1>Noticeboard</h1>
    
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
    
    
    
    

    <!-- tab content one (basic)-->
<div id='content_b' class="tabbody content_b" >
<h2>Basic tab content</h2>
<%@ include file="errorbox.jsp" %> <!-- show any error messages here -->
<div id="formtablecontainer">
<table class="forms">
	<tr>
		<td>
			<table width="65%" align="center" >
				<tr>
					<td class="formlabel"><fmt:message key="basic.title" /></td>
					<td class="formcontrol">
							<FCK:editor id="richTextTitle" basePath="/lams/fckeditor/"
								height="150"
								width="85%">
								<c:out value="${NbAuthoringForm.title}" escapeXml="false" />
							</FCK:editor>
					</td>
				</tr>
				<tr>
					<td class="formlabel"><fmt:message key="basic.content" /></td>
					<td class="formcontrol">
						<FCK:editor id="richTextContent" basePath="/lams/fckeditor/"
								width="85%"
								height="400">
							<c:out value="${NbAuthoringForm.content}" escapeXml="false" />
						</FCK:editor>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>

<hr>
<a href="javascript:;" class="button">Cancel</a>
<html:submit property="method" styleClass="button"><fmt:message key="button.ok" /></html:submit>
<!-- end of content_b -->
</div>



<!-- tab content 2 Advanced-->
<div id='content_a'  class="tabbody content_a">
<h2>Advanced tab content</h2>
<div id="formtablecontainer">
<table class="forms">
	<tr><td><fmt:message key="message.noAdvancedSection" /></td>
	</tr>
</table>
</div>

<!-- end of content_b -->
</div>

<!-- tab content 3 instructions -->
<div id='content_i'  class="tabbody content_i">
<h2>Instructions tab content </h2>
<div id="formtablecontainer">
<table class="forms">
	<tr>
		<td>
			<table width="65%" align="center">
				<tr>
					<td class="formlabel"><fmt:message key="instructions.onlineInstructions" /></td>
					<td class="formcontrol">
							<FCK:editor id="richTextOnlineInstructions" basePath="/lams/fckeditor/"
								height="200"
								width="85%">
								<c:out value="${NbAuthoringForm.onlineInstructions}" escapeXml="false" />
							</FCK:editor>
					</td>
				</tr>
				
				<tr>
					<td class="formlabel">
						<fmt:message key="instructions.uploadOnlineInstr" />
					</td>
					<td class="formcontrol">
						<html:file property="onlineFile" /><html:submit property="method"><fmt:message key="button.upload" /></html:submit>
											
					</td>						
				</tr>
				<tr>
					<td class="formlabel"><fmt:message key="instructions.offlineInstructions" /></td>
					<td class="formcontrol">
						<FCK:editor id="richTextOfflineInstructions" basePath="/lams/fckeditor/"
								width="85%"
								height="200">
							<c:out value="${NbAuthoringForm.offlineInstructions}" escapeXml="false" />
						</FCK:editor>
					</td>
				</tr>
				<tr>
					<td class="formlabel">
						<fmt:message key="instructions.uploadOfflineInstr" />
					</td>
					<td class="formcontrol">
						<html:file property="offlineFile" /><html:submit property="method"><fmt:message key="button.upload" /></html:submit>
					</td>						
				</tr>
			
			</table>
		</td>
	</tr>
</table>
</div>

<logic:present name="attachmentList">
<bean:size id="count" name="attachmentList" />
<logic:notEqual name="count" value="0">

	<h2><fmt:message key="label.attachments" /></h2>
	<div id="datatablecontainer">
		<table width="100%"  border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<table width="70%" align="left">
		            <tr>
		                <td><fmt:message key="label.filename" /></td>
		                <td><fmt:message key="label.type" /></td>
		            	<td>&nbsp;</td>
		            </tr>
		            <logic:iterate name="attachmentList" id="attachment">
		            	<bean:define id="view">/download/?uuid=<bean:write name="attachment" property="uuid"/>&preferDownload=false</bean:define>
						<bean:define id="download">/download/?uuid=<bean:write name="attachment" property="uuid"/>&preferDownload=true</bean:define>
                        <bean:define id="uuid" name="attachment" property="uuid" />
                        
                        <tr>
			                         	
			            	<td><bean:write name="attachment" property="filename"/></td>
			                <td>
			                	<c:choose>
					            	<c:when test="${attachment.onlineFile}" >
					                	<fmt:message key="instructions.type.online" />
					               	</c:when>
					                <c:otherwise>
					                	<fmt:message key="instructions.type.offline" />
					                </c:otherwise>
				                </c:choose>
				            </td>
				            <td>
					        	<table>
						        	<tr>
						            	<td>
						                	<a href='javascript:launchInstructionsPopup("<html:rewrite page='<%=view%>'/>")' class="button">
						                   		<fmt:message key="link.view" />
						                    </a>
						               	</td>
						                <td>
							            	<html:link page="<%=download%>" styleClass="button">
							                	<fmt:message key="link.download" />
							                </html:link>
						                </td>
						                <td>
							            	<html:link page="/authoringv2.do?method=Delete" 
							                         	paramId="uuid" paramName="attachment" paramProperty="uuid"
							                         	onclick="javascript:return confirm('Are you sure you want to delete this file?')"
							                         	target="_self" styleClass="button">
							                	<fmt:message key="link.delete" />
							                </html:link> 
							            </td>
						           	</tr>
					            </table>
				           	</td>
			   	     	</tr>
		    	    </logic:iterate>
					</table>
			 	</td>
			</tr>
		</table>
	</div>
	
 </logic:notEqual>
 </logic:present>
	<hr>
<a href="javascript:;" class="button">Cancel</a>
<html:submit property="method" styleClass="button"><fmt:message key="button.ok" /></html:submit>


<!-- end of content_a -->
</div>

<html:hidden property="content" />
<html:hidden property="title" />
<html:hidden property="onlineInstructions" />
<html:hidden property="offlineInstructions" />

<!--
<div id="formtablecontainer">
<html:submit property="method" styleClass="button"><fmt:message key="button.ok" /></html:submit>
<div> -->


<p></p>
</html:form>
</body>
</html:html>
