<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<script>
	$(document).ready(function(){
		$.ajax({
			'url' : LAMS_URL + 'learning/learner/getLearnerProgress.do',
			'data': {
				lessonID: lessonId
			},
			'dataType' : 'json',
			'success'  : function(response) {
				$(response.activities).each(function(){
					let activity = this,
						entry = $('<article />').addClass('timeline-entry').appendTo('.vertical-timeline'),
						innerEntry = $('<div />').addClass('timeline-entry-inner').appendTo(entry),
						icon = $('<div />').addClass('timeline-icon').appendTo(innerEntry),
						content = $('<div />').addClass('timeline-label').appendTo(innerEntry),
						title = $('<h4 />').addClass('timeline-title').text(activity.name).appendTo(content);

					switch(activity.status){
						case 0: icon.addClass('bg-primary');break;
						case 1: icon.addClass('bg-success');break;
					}

					if (activity.iconURL) {
						$('<img />').attr('src', LAMS_URL + activity.iconURL).appendTo(icon);
					}
				});
			}
		});
	});
</script>

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
