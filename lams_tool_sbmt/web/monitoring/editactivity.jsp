<%@include file="../sharing/share.jsp" %>
<%@ taglib uri="fck-editor" prefix="FCK"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
<head>
<html:base />
<title>Submit Files</title>
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
    <link href="<%=LAMS_WEB_ROOT%>/css/aqua.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=LAMS_WEB_ROOT%>/includes/javascript/common.js"></script>

	<script type="text/javascript" src="<html:rewrite page='/includes/javascript/xmlrequest.js'/>"></script>
</head>

<body>
    <div>
    <%@ include file="tabmenu.jsp"%>
    </div>
	<html:form action="monitoring" method="post" >
		<html:errors/>
  	    <%@ include file="basic.jsp"%>
  	   	<table>
		 		<tr>
		 		<td>
				<html:button property="cancel" onclick="javascript:history.back()">
					<fmt:message key="label.monitoring.edit.activity.cancel"/>
				</html:button>
				</td>
				<td>
				<html:submit>
					<fmt:message key="label.monitoring.edit.activity.update"/>
				</html:submit>
			</td>
			</tr>
		</table>
  	    
	</html:form>
</body>
</html:html>