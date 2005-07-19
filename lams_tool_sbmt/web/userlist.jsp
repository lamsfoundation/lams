
<%@ page language="java"%>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-c" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    <html:base />
    
    <title><bean:message key="page.title.mark1.userlist"/></title>    
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
	<link href="css/aqua.css" rel="stylesheet" type="text/css">

  </head>
  
  <body>

  	<div id="datatablecontainer">
  		
  		<c:set var="userlist" scope="request" value="${sessionScope.USERLIST}"/>
  		<logic:notEmpty name="userlist">
			<logic:iterate id="element" name="userlist">
				<tr>
				<html:form  action="/monitoring">
				<html:hidden property="method" value="getFilesUploadedByUser"/>
				<html:hidden property="toolSessionID" value="${toolSessionID}"/>
				<bean:define id="details" name="element" property="userID"/>

				<html:hidden property="userID" value="${details}"/>
				<td><b><bean:write name="element" property="firstName"/> <bean:write name="element" property="lastName"/></b></td>
				<td><b><bean:write name="element" property="login"/></b></td>
				<td><b><html:submit property="Mark" value="Mark"/></b></td>
				</html:form>
				</tr>		
			</logic:iterate>
  		</logic:notEmpty>  		
		</table>
  		
	</div>
					
  </body>
</html:html>
