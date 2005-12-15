<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:set var="lams"><lams:LAMSURL/></c:set>
<c:set var="tool"><lams:WebAppURL/></c:set>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD hTML 4.01 Transitional//EN">
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Noticeboard tool</title>

<script lang="javascript">
var imgRoot="<c:out value="${lams}"/>images/";
var themeName="aqua";
</script>
<script type="text/javascript" src="<c:out value="${lams}"/>includes/javascript/tabcontroller.js"></script>
<script src="<c:out value="${lams}"/>includes/javascript/common.js"></script>

<!-- this is the custom CSS for hte tool -->
<link href="<c:out value="${tool}"/>css/tool_custom.css" rel="stylesheet" type="text/css">
<lams:css/>
</head>
<%-- chooses which tab to highlight --%>
<c:choose>
        <c:when test='${NbMonitoringForm.method == "Instructions"}'>
           <c:set var="tabId" value="i" />
        </c:when>
         <c:when test='${NbMonitoringForm.method == "Edit Activity"}'>
           <c:set var="tabId" value="e" />
        </c:when>
        <c:when test='${NbMonitoringForm.method == "Statistics"}'>
           <c:set var="tabId" value="st" />
        </c:when>
        <c:otherwise> 
      	   <c:set var="tabId" value="su" />
        </c:otherwise>
</c:choose>

<body onLoad='showMonitoringTab("<c:out value='${tabId}'/>")'>
<html:form action="/monitoring" target="_self">

<c:set var="monitoringURL">
	<html:rewrite page="/monitoring.do" />
</c:set>
    
    <!-- start tabs -->
<!-- tab holder table -->
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="bottom">
	<!-- table for tab 1 (summary)-->
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td><img src="<c:out value="${lams}"/>images/aqua_tab_s_left.gif" name="tab_left_su" width="8" height="25" border="0" id="tab_left_su"/></td>
		<td class="tab tabcentre_selected" width="90" id="tab_tbl_centre_su" ><label for="su" accesskey="su"><a
			href="<c:out value='${monitoringURL}'/>?method=Summary&toolContentID=<c:out value='${NbMonitoringForm.toolContentID}'/>" id="su" >Summary</a></label></td>
		<td><a href="<c:out value='${monitoringURL}'/>?method=Summary&toolContentID=<c:out value='${NbMonitoringForm.toolContentID}'/>"><img src="<c:out value="${lams}"/>images/aqua_tab_s_right.gif"  name="tab_right_su" width="8" height="25" border="0" id="tab_right_su"/></a></td>
	  </tr>
	</table>

</td>
    <td valign="bottom">
	<!-- table for tab 2 (instructions) -->
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td><a href="<c:out value='${monitoringURL}'/>?method=Instructions&toolContentID=<c:out value='${NbMonitoringForm.toolContentID}'/>"><img src="<c:out value="${lams}"/>images/aqua_tab_left.gif" name="tab_left_i" width="8" height="22" border="0" id="tab_left_i" /></a></td>
		<td class="tab tabcentre" width="90" id="tab_tbl_centre_i"><label for="i" accesskey="i"><a
			href="<c:out value='${monitoringURL}'/>?method=Instructions&toolContentID=<c:out value='${NbMonitoringForm.toolContentID}'/>" id="i" >Instructions</a></label></td>
		<td><a href="<c:out value='${monitoringURL}'/>?method=Instructions&toolContentID=<c:out value='${NbMonitoringForm.toolContentID}'/>"><img src="<c:out value="${lams}"/>images/aqua_tab_right.gif" name="tab_right_i" width="9" height="22" border="0" id="tab_right_i" /></a></td>
	  </tr>
	</table>

</td>
    <td valign="bottom">
	<!-- table for tab 3 (edit activity) -->
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td ><a href="<c:out value='${monitoringURL}'/>?method=Edit%20Activity&toolContentID=<c:out value='${NbMonitoringForm.toolContentID}'/>"><img border="0" src="<c:out value="${lams}"/>images/aqua_tab_left.gif" width="8" height="22" id="tab_left_e"   name="tab_left_e" /></a></td>
		<td class="tab tabcentre" width="90" id="tab_tbl_centre_e"><label for="e" accesskey="e"><a href="<c:out value='${monitoringURL}'/>?method=Edit%20Activity&toolContentID=<c:out value='${NbMonitoringForm.toolContentID}'/>" id="e" >Edit Activity</a></label></td>
		<td ><a href="<c:out value='${monitoringURL}'/>?method=Edit%20Activity&toolContentID=<c:out value='${NbMonitoringForm.toolContentID}'/>"><img src="<c:out value="${lams}"/>images/aqua_tab_right.gif"  width="9" height="22" border="0" id="tab_right_e"  name="tab_right_e"/></a></td>
	  </tr>
	</table>

</td>

    <td valign="bottom">
	<!-- table for tab 4 (statistics) -->
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
		<td ><a href="<c:out value='${monitoringURL}'/>?method=Statistics&toolContentID=<c:out value='${NbMonitoringForm.toolContentID}'/>"><img border="0" src="<c:out value="${lams}"/>images/aqua_tab_left.gif" width="8" height="22" id="tab_left_st"   name="tab_left_st" /></a></td>
		<td class="tab tabcentre" width="90" id="tab_tbl_centre_st"><label for="st" accesskey="st"><a href="<c:out value='${monitoringURL}'/>?method=Statistics&toolContentID=<c:out value='${NbMonitoringForm.toolContentID}'/>" id="st" >Statistics</a></label></td>
		<td ><a href="<c:out value='${monitoringURL}'/>?method=Statistics&toolContentID=<c:out value='${NbMonitoringForm.toolContentID}'/>"><img src="<c:out value="${lams}"/>images/aqua_tab_right.gif"  width="9" height="22" border="0" id="tab_right_st"  name="tab_right_st"/></a></td>
	  </tr>
	</table>
	
	

</td>
  </tr>
</table>
    <!-- end tab buttons -->
    
    <div id="datatablecontainer" class="tabbody">
		<c:choose>
		        <c:when test='${NbMonitoringForm.method == "Instructions"}'>
		           <%@ include file="m_Instructions.jsp" %>
		        </c:when>
		         <c:when test='${NbMonitoringForm.method == "Edit Activity"}'>
		           <%@ include file="m_EditActivity.jsp" %>
		        </c:when>
		        <c:when test='${NbMonitoringForm.method == "Statistics"}'>
		           <%@ include file="m_Statistics.jsp" %>
		        </c:when>
		        <c:otherwise> 
		      	   <%@ include file="m_Summary.jsp" %>
		        </c:otherwise>
		</c:choose>
</div>

</html:form>

</body>


</html:html>

