<%@include file="../sharing/share.jsp" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    <html:base />
    
    <title><bean:message key="page.title.mark1.userlist"/></title>    
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
    <link href="<%=LAMS_WEB_ROOT%>/css/aqua.css" rel="stylesheet" type="text/css">

  </head>
  
  <body>
    <div>
    <%@ include file="tabmenu.jsp"%>
    </div>
	<html:errors/>
  	<div id="datatablecontainer">
  		<c:set var="sessionUserMap" scope="request" value="${sessionUserMap}"/>
  		<logic:notEmpty name="sessionUserMap">
			<logic:iterate id="element" name="sessionUserMap">
				<bean:define id="sessionDto" name="element" property="key"/>
				<bean:define id="userlist" name="element" property="value"/>
		  		<table class="forms">
				    <tr>
				        <td style="border-bottom:1px #000 solid;" colspan="4"><b>SESSION NAME: <c:out value="${sessionDto.sessionName}" /></td>
				    </tr>
			  		<logic:notEmpty name="userlist">
						<logic:iterate id="user" name="userlist">
							<tr>
							<html:form  action="/monitoring">
							<html:hidden property="method" value="getFilesUploadedByUser"/>
							<html:hidden property="toolSessionID" value="${sessionID}"/>
							
							<bean:define id="details" name="user" property="userID"/>
							<html:hidden property="userID" value="${details}"/>
							
							<td class="formlabel"><b><bean:write name="user" property="firstName"/> <bean:write name="user" property="lastName"/></b></td>
							<td class="formlabel"><b><bean:write name="user" property="login"/></b></td>
							<td class="formcontrol"><b><html:submit property="Mark" value="Mark"/></b></td>
							</html:form>
							</tr>		
						</logic:iterate>
			  		</logic:notEmpty>
			  		<logic:empty name="userlist">
			  			<tr><td colspan="3">NO USERS AVAILABLE</td></tr>
			  		</logic:empty>
				    
	  				<tr>
			  			<td class="formcontrol">
			  			<html:form  action="/monitoring">
			  				<html:hidden property="method" value="viewAllMarks"/>
							<html:hidden property="toolSessionID" value="${sessionID}"/>
				  			<html:submit property="viewAllMarks" value="View all marks"/>
		  				</html:form>
			  			</td>
			  			<td class="formcontrol">
			  			<html:form  action="/monitoring">
			  				<html:hidden property="method" value="releaseMarks"/>
							<html:hidden property="toolSessionID" value="${sessionID}"/>
				  			<html:submit property="releaseMarks" value="Release marks"/>
		  				</html:form>
			  			</td>
			  			<td class="formcontrol">
			  			<html:form  action="/monitoring">
			  				<html:hidden property="method" value="downloadMarks"/>
							<html:hidden property="toolSessionID" value="${sessionID}"/>
				  			<html:submit property="downloadMarks" value="Download marks"/>
		  				</html:form>
			  			</td>
	  				</tr>
				</table>
				<br><br>
			</logic:iterate>

  		</logic:notEmpty>  		
	</div>
					
  </body>
</html:html>
