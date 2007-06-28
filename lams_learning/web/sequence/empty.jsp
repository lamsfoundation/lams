  
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
	
	<div id="content">
		<h1><c:out value="${requestScope.title}"/></h1>

		<lams:Passon id="${requestScope.lessonID}" progress="${activityForm.progressSummary}"/>
		
		<p>&nbsp;</p>

		<p><fmt:message key="label.sequence.empty.message"/></p>

		<html:form action="/CompleteActivity" method="POST">
			<input type="hidden" name="activityID" value="<c:out value='${requestScope.activityID}' />" />
			<input type="hidden" name="lessonID" value="<c:out value='${requestScope.lessonID}' />" /> 
			<input type="hidden" name="progressID" value="<c:out value='${requestScope.progressID}' />" /> 
			
			<div class="right-buttons">
				<html:submit styleClass="button"><fmt:message key="label.next.button"/></html:submit>
			</div>

		</html:form>

		<p>&nbsp;</p>
	</div>  <!--closes content-->


	<div id="footer">
	</div><!--closes footer-->


