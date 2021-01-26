<%@ include file="/taglibs.jsp"%>

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
			<input type="text" class="form-control" name="datePicker" id="datePicker" value="" autocomplete="off" />
		</div>
	</div>
</div>	
