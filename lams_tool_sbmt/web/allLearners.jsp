
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
  		<c:set var="report" scope="request" value="${sessionScope.report}"/>
  		<logic:notEmpty name="report">
			<logic:iterate id="element" name="report">
				<tr>
				<td><span><p>
				<b><bean:write name="element" property="key.login"/><br/>
				<bean:write name="element" property="key.firstName"/>
				<bean:write name="element" property="key.lastName"/></b></p></span></td>
				<td>
				<bean:define id="details" name="element" property="value"/>
				<logic:iterate id="dt" name="details">
					<span><p>
					<bean:message key="label.learner.fileName"/>
					<bean:write name="dt" property="name"/><br/>
					
					<bean:message key="label.learner.fileDescription"/>
					<bean:write name="dt" property="fileDescription"/><br/>
					
					<bean:message key="label.learner.dateOfSubmission"/>
					<bean:write name="dt" property="dateOfSubmission"/><br/>
					
					<bean:message key="label.learner.comments"/>
					<logic:empty name="dt" property="comments">
						<bean:message key="label.learner.notAvailable"/><br/>
					</logic:empty>
					<logic:notEmpty name="dt" property="comments">
						<bean:write name="dt" property="comments"/><br/>
					</logic:notEmpty>
					
					<bean:message key="label.learner.marks"/>
					<logic:empty name="dt" property="marks">
						<bean:message key="label.learner.notAvailable"/><br/>
					</logic:empty>
					<logic:notEmpty name="dt" property="marks">
						<bean:write name="dt" property="marks"/><br/>
					</logic:notEmpty>
					
					<bean:message key="label.learner.dateMarksReleased"/>
					<logic:notEmpty name="dt" property="dateMarksReleased">
						<bean:write name="dt" property="dateMarksReleased"/><br/>
					</logic:notEmpty>
					<logic:empty name="dt" property="dateMarksReleased">
						<bean:message key="label.learner.notAvailable"/><br/>
					</logic:empty>
					</p></span>
				</logic:iterate>		
				</td>
				</tr>		
			</logic:iterate>
  		</logic:notEmpty>  		
		</table>
  </body>
</html:html>

