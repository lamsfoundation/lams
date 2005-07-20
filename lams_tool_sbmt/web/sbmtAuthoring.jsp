
<%@ page language="java"%>

<%@ taglib uri="fck-editor" prefix="FCK" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-html-el" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-c" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    <html:base />    
    <title>Submit Files</title>    
	<!-- depending on user / site preference this will get changed probbably use passed in variable from flash to select which one to use-->
	<link href="css/aqua.css" rel="stylesheet" type="text/css">
    
  </head>
  
  <body>
    <html:form action="authoring?method=updateContent" method="post" focus="login">
     <h1> <bean:message key="label.authoring.heading"/></h1>
	<table class="forms">
      	<!--hidden field contentID passed by flash-->
      	<html:hidden property="toolContentID" value="${toolContentID}"/>
        <!-- Title Row -->
        <tr>
          <td class="formlabel"><bean:message key="label.authoring.title"/>:</td>
          <td class="formcontrol"><html:text property="title" /></td>
        </tr>
        <!-- Instructions Row -->
        <tr>
          <td class="formlabel"><bean:message key="label.authoring.instruction"/>:</td>
          <td class="formcontrol">
              <FCK:editor id="instructions" 
    			basePath="/lams/fckEditor/"
    			height="150"    
    			width="85%">                                     
			  </FCK:editor>
			</td>
        </tr>
        <!-- Button Row -->
        <tr>
          <td class="formcontrol"><html:button property="cancel" onclick="window.close()"><bean:message key="label.authoring.cancel.button"/></html:button></td>
          <td class="formcontrol"><html:submit property="save"><bean:message key="label.authoring.save.button"/></html:submit></td>
        </tr>
      </table>
    </html:form>
  </body>
</html:html>
