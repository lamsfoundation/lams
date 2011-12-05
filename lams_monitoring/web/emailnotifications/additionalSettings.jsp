<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-function" prefix="fn"%>

<div id="accordion">
	<h3 id="nowHeader">
		<a href="#"><fmt:message key="email.notifications.table.now" /></a>
	</h3>
	<div id="nowDiv">
		<table id="list3"></table>
		<div id="pager3"></div>
	</div>
			
	<h3 id="scheduleHeader">
		<a href="#"><fmt:message key="email.notifications.table.schedule" /></a>
	</h3>
	<div>
		<div>
			<fmt:message key="email.notifications.schedule.description" />
		</div>
	
		<div id="datePickerDiv">
			<span>
				<fmt:message key="email.notifications.by.this.date" />
			</span>
			
			<input type="text" name="datePicker" id="datePicker" value=""/>
		</div>
		
		<c:choose>
			<c:when test="${lesson.lessonId != null}">
				<c:set var="additionalParams">lessonID=${lesson.lessonId}</c:set>
			</c:when>
			<c:otherwise>
				<c:set var="additionalParams">organisationID=${org.organisationId}</c:set>
			</c:otherwise>
		</c:choose>
		<a href="<c:url value='/emailNotifications.do'/>?method=showScheduledEmails&${additionalParams}" id="listEmailsHref">
			<fmt:message key="email.notifications.scheduled.messages.list" />
		</a>				
	</div>
</div>	
