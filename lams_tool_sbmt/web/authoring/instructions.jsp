
<%@ page language="java"%>

<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    <html:base />    
    <title>Submit Files</title>    
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
	<link href="../css/aqua.css" rel="stylesheet" type="text/css">
    
  </head>
  
  <body>
    <html:form action="authoring?method=instructions" method="post">
     <h1> <fmt:message key="label.authoring.heading.instructions"/></h1>
     <h2><fmt:message key="label.authoring.heading.instructions.desc"/></h2>
	<table class="forms">
      	<!--hidden field contentID passed by flash-->
      	<html:hidden property="toolContentID" value="${toolContentID}"/>
        <!-- Title Row -->
        <tr>
        </tr>
        <!-- Instructions Row -->
        <tr>
          <td colspan="2" class="formcontrol">
          	  <fmt:message key="label.authoring.online.instruction"/>:<br>
              <FCK:editor id="online_instruction" 
    			basePath="/lams/fckEditor/"
    			height="150"    
    			width="85%">                                     
			  </FCK:editor>
			</td>
        </tr>
        <tr>
          <td colspan="2" class="formcontrol">
          		<fmt:message key="label.authoring.offline.instruction"/>:<br>
              <FCK:editor id="offline_instruction" 
    			basePath="/lams/fckEditor/"
    			height="150"    
    			width="85%">                                     
			  </FCK:editor>
			</td>
        </tr>
        <!-- Button Row -->
        <tr>
          <td class="formcontrol">
	          	<html:button property="cancel" onclick="window.close()">
	          		<fmt:message key="label.authoring.cancel.button"/>
	          	</html:button>
          	</td>
          <td class="formcontrol">
          		<html:submit property="save">
          			<fmt:message key="label.authoring.save.button"/>
          		</html:submit>
          </td>
        </tr>
      </table>
    </html:form>
  </body>
</html:html>
