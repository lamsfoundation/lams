<%@include file="../sharing/share.jsp" %>
<%@ taglib uri="fck-editor" prefix="FCK"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>    
    <title>Monitoring define it later</title>
    <html:base/>
  	<link href="<%=LAMS_WEB_ROOT%>/css/aqua.css" rel="stylesheet" type="text/css">
  </head>  
  <body>
  
  <html:form action="monitoring" method="post">
  <html:errors/>
	  	<html:hidden property="mode" value="definelater"/>
		 <%@ include file="basic.jsp"%>
		 <table>
		 		<tr>
		 		<td>
				<html:button property="close" onclick="javascript:window.close()">
					<fmt:message key="label.monitoring.edit.activity.cancel"/>
				</html:button>
				</td>
				<td>
				<html:submit onclick="javascript:window.close()">
					<fmt:message key="label.monitoring.edit.activity.update"/>
				</html:submit>
			</td>
			</tr>
		</table>
		 
  </html:form>
  
   </body>
</html:html>
  