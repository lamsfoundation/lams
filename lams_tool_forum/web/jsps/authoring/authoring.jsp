<%@include file="../sharing/share.jsp" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
	<html:base />
	<title>Forum Tool</title>
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
    <link href="<%=LAMS_WEB_ROOT%>/css/aqua.css" rel="stylesheet" type="text/css">
    <script lang="javascript">
	    var imgRoot="<%=LAMS_WEB_ROOT%>/images/";
	    var themeName="aqua";
	    function setTab(curr){
	    	var tag = document.getElementById("currentTab");
	    	tag.value = curr;
	    }
	    function submitForm(actionUrl){
	    	document.forms[0].action=actionUrl;
	    	document.forms[0].submit();
	    }
    </script>
	<script type="text/javascript" src="<%=LAMS_WEB_ROOT%>/includes/javascript/tabcontroller.js"></script>    
	<script type="text/javascript" src="<%=LAMS_WEB_ROOT%>/includes/javascript/common.js"></script>
	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/xmlrequest.js'/>"></script>
</head>
<c:if test="${empty currentTab}"><c:set var="currentTab" value="b"/></c:if>
<body onLoad="initTabs();showTab('<c:out value='${currentTab}'/>');">
<html:form action="authoring/update" method="post"	 enctype="multipart/form-data">
		<c_rt:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<html:hidden property="toolContentID"/>
		<html:hidden  property="currentTab" styleId="currentTab"/>

<!-- start tabs -->
<!-- tab holder table -->
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="bottom">
	<!-- table for tab 1 (basic)-->
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td><a href="#"  onClick="showTab('b');setTab('b');return false;" ><img src="<%=LAMS_WEB_ROOT%>/images/aqua_tab_s_left.gif" name="tab_left_b" width="8" height="25" border="0" id="tab_left_b"/></a></td>
		<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_b"  onClick="showTab('b');setTab('b');return false;" ><a href="#" onClick="showTab('b');setTab('b');return false;" id="b" >Basic</a></td>
		<td><a href="#" onClick="showTab('b');setTab('b');return false;"><img src="<%=LAMS_WEB_ROOT%>/images/aqua_tab_s_right.gif"  name="tab_right_b" width="8" height="25" border="0" id="tab_right_b"/></a></td>
	  </tr>
	</table>

</td>
    <td valign="bottom">
	<!-- table for tab 2 (advanced) -->
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td><a href="#" onClick="showTab('a');setTab('a');return false;"><img src="<%=LAMS_WEB_ROOT%>/images/aqua_tab_left.gif" name="tab_left_a" width="8" height="22" border="0" id="tab_left_a" /></a></td>
		<td class="tab tabcentre" width="90" id="tab_tbl_centre_a" onClick="showTab('a');setTab('a');return false;"><a href="#" onClick="showTab('a');setTab('a');return false;" id="a" >Advanced</a></td>
		<td><a href="#" onClick="showTab('a');setTab('a');return false;"><img src="<%=LAMS_WEB_ROOT%>/images/aqua_tab_right.gif" name="tab_right_a" width="9" height="22" border="0" id="tab_right_a" /></a></td>
	  </tr>
	</table>

</td>
    <td valign="bottom">
	<!-- table for ab 3 (instructions) -->
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td ><a href="#" onClick="showTab('i');setTab('i');return false;"><img border="0" src="<%=LAMS_WEB_ROOT%>/images/aqua_tab_left.gif" width="8" height="22" id="tab_left_i"   name="tab_left_i" /></a></td>
		<td class="tab tabcentre" width="90" id="tab_tbl_centre_i"  onClick="showTab('i');setTab('i');return false;" ><a href="#" onClick="showTab('i');setTab('i');return false;" id="i" >Instructions</a></td>
		<td ><a href="#" onClick="showTab('i');setTab('i');return false;"><img src="<%=LAMS_WEB_ROOT%>/images/aqua_tab_right.gif"  width="9" height="22" border="0" id="tab_right_i"  name="tab_right_i"/></a></td>
	  </tr>
	</table>

</td>
  </tr>
</table>
    <!-- end tab buttons -->

	<%@ include file="basic.jsp"%>
	<%@ include file="advance.jsp"%>
	<%@ include file="instructions.jsp"%>
</html:form>
</body>
</html:html>
