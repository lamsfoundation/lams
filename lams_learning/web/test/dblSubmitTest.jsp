 
<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  </head>
  
  <body>
  
  	<b>Valid=<c:out value="${doubleSubmit}" /></b><br />
  
  	<html:form action="/test/DblSubmit">
		<input type="hidden" name="lams_token" value="<c:out value='${lams_token}' />" />
  		<html:submit />
  	</html:form>
  
  </body>
</html>
