<%@ taglib uri="tags-tiles" prefix="tiles" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-function" prefix="fn"%>

<div id="accordion">
	<h4 id="nowHeader">
		<a href="#"><fmt:message key="email.notifications.table.now" /></a>
	</h4>
	<div id="nowDiv">
		<table id="list3"></table>
		<div id="pager3"></div>
	</div>
			
	<h4 id="scheduleHeader">
		<a href="#"><fmt:message key="email.notifications.table.schedule" /></a>
	</h4>
	<div>
		<p class="body">
			<fmt:message key="email.notifications.schedule.description" />
		</p>
			
		<div class="form-group">
			<label for="datePicker"><fmt:message key="email.notifications.by.this.date" /></label>
			<input type="text" class="form-control" name="datePicker" id="datePicker" value=""/>
		</div>
		
		<c:choose>
			<c:when test="${lesson.lessonId != null}">
				<c:set var="additionalParams">lessonID=${lesson.lessonId}</c:set>
			</c:when>
			<c:otherwise>
				<c:set var="additionalParams">organisationID=${org.organisationId}</c:set>
			</c:otherwise>
		</c:choose>
		<a href="<c:url value='/emailNotifications.do'/>?method=showScheduledEmails&${additionalParams}" id="listEmailsHref" class="btn btn-default btn-sm">
			<fmt:message key="email.notifications.scheduled.messages.list" />
		</a>				
	</div>
</div>	
