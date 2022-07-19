<%@ include file="/taglibs.jsp"%>

<div id="accordion" class="accordion">
	<div class="accordion-item">
		<h2 id="nowHeader" class="accordion-header">
		    <button class="accordion-button" type="button" data-bs-toggle="collapse" aria-expanded="true"
		    		data-bs-target="#nowDiv" aria-controls="nowDiv">
		    	<fmt:message key="email.notifications.table.now" />
		    </button>
		</h2>
		<div id="nowDiv" class="accordion-collapse collapse show" data-bs-parent="#accordion">
			<div class="accordion-body">
				<table id="list3"></table>
				<div id="pager3"></div>
		    </div>
	   </div>
	</div>

   
   <div class="accordion-item">
		  <h2 id="scheduleHeader" class="accordion-header">
		    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" aria-expanded="false"
		    		data-bs-target="#scheduleDiv" aria-controls="scheduleDiv">
		    	<fmt:message key="email.notifications.table.schedule" />
		    </button>
		  </h2>
		  <div id="scheduleDiv" class="accordion-collapse collapse" data-bs-parent="#accordion">
			<div class="accordion-body">
				<p class="body">
					<fmt:message key="email.notifications.schedule.description" />
				</p>
				<div class="form-group">
					<label for="datePicker"><fmt:message key="email.notifications.by.this.date" /></label>
					<input type="text" class="form-control" name="datePicker" id="datePicker" value="" autocomplete="nope" />
				</div>
		    </div>
	     </div>
	</div>
</div>	