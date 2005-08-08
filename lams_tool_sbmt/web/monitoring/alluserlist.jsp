
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
  	<link href="<html:rewrite page='/includes/css/aqua.css'/>" rel="stylesheet" type="text/css">

  </head>
  
  <body>

  	<div id="datatablecontainer">
  		<table class="forms">
  		<c:set var="userlist" scope="request" value="${userList}"/>
  		<logic:notEmpty name="userlist">
			<logic:iterate id="element" name="userlist">
				<tr>
				<html:form  action="/monitoring">
				<html:hidden property="method" value="getFilesUploadedByUser"/>
				<html:hidden property="toolSessionID" value="${toolSessionID}"/>
				<bean:define id="details" name="element" property="userID"/>

				<html:hidden property="userID" value="${details}"/>
				<td class="formlabel"><b><bean:write name="element" property="firstName"/> <bean:write name="element" property="lastName"/></b></td>
				<td class="formlabel"><b><bean:write name="element" property="login"/></b></td>
				<td class="formcontrol"><b><html:submit property="Mark" value="Mark"/></b></td>
				</html:form>
				</tr>		
			</logic:iterate>
  		</logic:notEmpty>  		
		</table>
		<html:errors/>
  		<table class="forms">
	  		<tr>
		  			<td class="formcontrol">
		  			<html:form  action="/monitoring">
		  				<INPUT type="hidden" name="method" value="viewAllMarks">
						<INPUT type="hidden" name="toolSessionID" value="<c:out value="${toolSessionID}" />"/>
			  			<html:submit property="viewAllMarks" value="View all marks"/>
	  				</html:form>
		  			</td>
		  			<td class="formcontrol">
		  			<html:form  action="/monitoring">
		  				<INPUT type="hidden" name="method" value="releaseMarks">
						<INPUT type="hidden" name="toolSessionID" value="<c:out value="${toolSessionID}" />"/>
			  			<html:submit property="releaseMarks" value="Release marks"/>
	  				</html:form>
		  			</td>
		  			<td class="formcontrol">
		  			<html:form  action="/monitoring">
		  				<INPUT type="hidden" name="method" value="downloadMarks">
						<INPUT type="hidden" name="toolSessionID" value="<c:out value="${toolSessionID}" />"/>
			  			<html:submit property="downloadMarks" value="Download marks"/>
	  				</html:form>
		  			</td>
	  		</tr>
  		</table>
	</div>
					
  </body>
</html:html>
