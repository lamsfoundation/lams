
<%@ page language="java"%>

<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-c" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>
    <html:base />    
    <title>Submit Files</title>    
  </head>
  
  <body>
    <html:form action="/tool/sbmt/authoring?method=updateContent" method="post" focus="login">
      <bean:message key="label.authoring.heading"/>
      <table width="100%" height="190" border="0" 
      		 align="center" cellpadding="5" cellspacing="0" 
      		 bgcolor="#FFFFFF" 
      		 summary="This table is being used for layout purposes only">
      	<!--hidden field contentID passed by flash-->
      	<html:hidden property="contentID" value="${requestScope.contentID}"/>
        <!-- Title Row -->
        <tr>
          <td><bean:message key="label.authoring.title"/>:</td>
          <td><html:text property="title" /></td>
        </tr>
        <!-- Instructions Row -->
        <tr>
          <td><bean:message key="label.authoring.instruction"/>:</td>
          <td><html:textarea property="instructions" /></td>
        </tr>
        <!-- Button Row -->
        <tr>
          <td><html:button property="cancel" onclick="window.close()"><bean:message key="label.authoring.cancel.button"/></html:button></td>
          <td><html:submit property="save"><bean:message key="label.authoring.save.button"/></html:submit></td>
        </tr>
      </table>
    </html:form>
  </body>
</html:html>
