<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>

<div id="learners-accordion" class="accordion">
</div>


<div class="learners-accordion-item-template accordion-item">
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
				<h5 class="no-progress align-self-center">No progress yet</h5>
				<div class="vertical-timeline-container">
					<div class="vertical-timeline timeline-sm"></div>
				</div>
			</div>
		</div>
    </div>
   </div>
</div>

<%--
<div class="vertical-timeline timeline-sm">
	<article class="timeline-entry">
		<div class="timeline-entry-inner">
			<time datetime="2014-01-10T03:45" class="timeline-time">
				<span>12:45 AM</span><span>Today</span>
			</time>
			<div class="timeline-icon bg-violet">
				<i class="fa fa-exclamation"></i>
			</div>
			<div class="timeline-label">
				<h4 class="timeline-title">New Project</h4>

				<p>Tolerably earnestly middleton extremely distrusts she boy now
					not. Add and offered prepare how cordial.</p>
			</div>
		</div>
	</article>
	<article class="timeline-entry">
		<div class="timeline-entry-inner">
			<time datetime="2014-01-10T03:45" class="timeline-time">
				<span>9:15 AM</span><span>Today</span>
			</time>
			<div class="timeline-icon bg-green">
				<i class="fa fa-users"></i>
			</div>
			<div class="timeline-label bg-green">
				<h4 class="timeline-title">Job Meeting</h4>

				<p>Caulie dandelion maize lentil collard greens radish arugula
					sweet pepper water spinach kombu courgette.</p>
			</div>
		</div>
	</article>
	<article class="timeline-entry">
		<div class="timeline-entry-inner">
			<time datetime="2014-01-09T13:22" class="timeline-time">
				<span>8:20 PM</span><span>04/03/2013</span>
			</time>
			<div class="timeline-icon bg-orange">
				<i class="fa fa-paper-plane"></i>
			</div>
			<div class="timeline-label bg-orange">
				<h4 class="timeline-title">Daily Feeds</h4>

				<p>
					<img src="https://via.placeholder.com/45x45/" alt=""
						class="timeline-img pull-left">Parsley amaranth tigernut
					silver beet maize fennel spinach ricebean black-eyed. Tolerably
					earnestly middleton extremely distrusts she boy now not. Add and
					offered prepare how cordial.
				</p>
			</div>
		</div>
		<div class="timeline-entry-inner">
			<div
				style="-webkit-transform: rotate(-90deg); -moz-transform: rotate(-90deg);"
				class="timeline-icon">
				<i class="fa fa-plus"></i>
			</div>
		</div>
	</article>
</div>
 --%>