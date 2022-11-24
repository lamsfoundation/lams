<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<div id="learners-accordion" class="accordion">
</div>


<div class="learners-accordion-item-template accordion-item d-none">
  <h2 class="accordion-header">
    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" aria-expanded="false">
    </button>
  </h2>
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