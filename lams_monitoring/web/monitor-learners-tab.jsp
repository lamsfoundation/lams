<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<div id="learners-tab-content" class="container-fluid">
	<div class="row" id="learners-pager">
		<div class="col offset-4 text-end" >
			<button class="btn btn-secondary pager-element d-none" onClick="javascript:learnersPageShift(-1)"
				    id="learners-previous-page" title="<fmt:message key='learner.group.backward.10'/>">
				<i class="fa fa-fw fa-backward" ></i>
			</button>
		</div>
		<div class="col text-center">
			<p class="pager-element mt-1 d-none">
				<fmt:message key='learners.page' />&nbsp;<span id="learners-page" />
			</p>
		</div>
		<div class="col text-start">
			<button class="btn btn-secondary pager-element d-none" onClick="javascript:learnersPageShift(1)"
				    id="learners-next-page" title="<fmt:message key='learner.group.forward.10'/>">
				<i class="fa fa-fw fa-forward" ></i>
			</button>
		</div>
		<div class="col-2 offset-3">
			<lams:Switch id="learnes-order-by-completion" labelKey="learners.order" 
						 iconClass="fa fa-sm fa-arrow-down-wide-short" />
		</div>
	</div>
	
	<div id="learners-accordion" class="accordion">
	</div>
</div>

<div class="learners-accordion-item-template accordion-item d-none">
	<div class="accordion-header">
	    <button class="accordion-button fs-4 collapsed" type="button" data-bs-toggle="collapse" aria-expanded="false">
	    	<div class="ms-auto me-5">
	    		<i class="me-2 fa-solid fa-check text-success accordion-completed-lesson" 
	    		   title="<fmt:message key='learner.completed.lesson'/>"></i>
		 		<span class="badge bg-success accordion-completed-activity-count"
		 			  title="<fmt:message key='learner.completed.activity.count'/>"></span> 
	    	</div>
    	</button>
    </div>
  <div class="accordion-collapse collapse">
	<div class="accordion-body container-fluid">
		<div class="row">
			<div class="col-4 learners-accordion-info">
				<div class="row">
					<div class="col">
						<h3 class="learners-accordion-name"></h3>
						<p class="learners-accordion-login"></p>
						<p class="learners-accordion-email"></p>
					</div>
					<div class="col learners-accordion-portrait">
						
					</div>
				</div>
			</div>
			<div class="col">
				<h5 class="no-progress align-self-center">
					<fmt:message key='label.monitoring.learners.no.progress' />
				</h5>
				<div class="vertical-timeline-container">
					<div class="vertical-timeline timeline-sm"></div>
				</div>
			</div>
		</div>
    </div>
   </div>
</div>

<article class="learners-timeline-entry-template timeline-entry d-none">
	<div class="timeline-entry-inner">
		<div class="timeline-icon">
		</div>
		<div class="timeline-label">
			<h4 class="timeline-title"></h4>
			<table class="table">
				<tr>
					<th scope="row"><fmt:message key='label.monitoring.learners.time.taken' /></th>
					<td class="timeline-activity-duration text-end"></td>
				</tr>
				<tr>
					<th scope="row"><fmt:message key='label.monitoring.learners.mark' /></th>
					<td class="timeline-activity-mark text-end"></td>
				</tr>
			</table>
		</div>
	</div>
</article>