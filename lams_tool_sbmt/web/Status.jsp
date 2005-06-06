<%@ page language="java"%>

<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-c" prefix="c" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html locale="true">
  <head>    
    <title>All Learners</title>
  </head>  
  <body>		  
  		<table border="1">
		 <c:set var="status" value="${sessionScope.status}"/>		 
		 <c:set var="contentID"  value="${sessionScope.contentID}"/>		 		 
  		 <c:forEach items="${status}" var="user">
			 <c:set var="userID"  value="${user.userID}"/>		 		 
			 <form action="monitoring.do?method=getFilesUploadedByUser&contentID=<c:out value='${contentID}'/>&userID=<c:out value='${userID}'/>"   method="post">					 
				 <c:out value="${userID}"/>
				 <c:out value="${user.login}"/>
				 <c:out value="${user.fullName}"/>
				 <c:if test="${user.unMarked == true}">
					 <bean:message key="label.monitoring.needMarking"/>
				 </c:if>
				 <input type="submit"  value="Mark" />
			 </form>
  		</c:forEach>  		
		</table>		
  </body>
</html:html>