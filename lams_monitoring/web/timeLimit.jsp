<%@ include file="/taglibs.jsp"%>

<style>
	.countdown-timeout {
		color: #FF3333 !important;
	}
	
	#time-limit-table th {
		vertical-align: middle;
	}
	
	#time-limit-table td.centered {
		text-align: center;
	}
		
	#time-limit-widget {
	  /* The main widget sticks to right browser edge */
	  position: fixed;
	  top: 45px;
	  right: 0;
	  /* Collapsed by default */
	  width: 130px;
	  display: none;
	}	
	
	#time-limit-widget .btn-success {
		float: none;
	}
	
	#time-limit-widget .panel-heading a:after, #time-limit-widget .panel-heading a:after {
		content: none;
	}
	
	#time-limit-widget .panel-title a {
		/* Underlining the link in title does not look nice*/
		text-decoration: none !important;
		color: black;
		font-size: 20px;
		font-weight: normal;
	}

	#time-limit-widget #time-limit-widget-content > div {
		text-align: center;
		padding: 10px 0;
	}
	
	#time-limit-widget #time-limit-widget-content > div button {
		width: 70%;
	}
	
	#time-limit-widget #absolute-time-limit-widget-value {
		display: inline-block;
		margin-left: 5px;
	}
</style>

<script>
// in minutes since learner entered the activity
var relativeTimeLimit = ${param.relativeTimeLimit},
	// in seconds since epoch started
	absoluteTimeLimit = ${empty param.absoluteTimeLimit ? 'null' : param.absoluteTimeLimit};

$(document).ready(function(){
	let timeLimitWidget = $('#time-limit-widget'),
		timeLimitContent = $('#time-limit-widget-content', timeLimitWidget)
			.on('hidden.bs.collapse', function () {
				// leave just the timer icon
				$('.panel-heading', timeLimitWidget).removeClass('collapsable-icon-left');
				
				timeLimitWidget.animate({
					width: '130px'
				}, function(){
					// if hideTimeLimitLearnerWidget() set it to true, then we are deleting the widget
					var remove = timeLimitContent.data('remove');
					if (remove) {
						window.setTimeout(function(){
							// remove after a short delay
							timeLimitWidget.hide();
						}, 1000);
					}
				});
			})
			.on('show.bs.collapse', function() {
				// hide content at first as it does not look nice when widget get expanded
				timeLimitContent.css('visibility', 'hidden');
				timeLimitWidget.animate({
					width: '200px'
				}, function(){
					$('.panel-heading', timeLimitWidget).addClass('collapsable-icon-left');
					timeLimitContent.css('visibility', 'visible');
				});
			});

	$('#time-limit-widget-toggle', timeLimitWidget).click(function(){
		if ($(this).data('prevent-toggle') === true){
			return;
		}
		timeLimitContent.collapse('toggle');
	});
	
	// create counter if absolute time limit is set
	if (absoluteTimeLimit) {
		showTimeLimitWidget(false);
		updateAbsoluteTimeLimitCounter();
		
		// expand time limit panel if absolute time limit is set and not expired
		if (absoluteTimeLimit > new Date().getTime() / 1000) {
			$('#time-limit-collapse').collapse('show');
		}
	}
	
	initInidividualTimeLimitAutocomplete();
});

function updateTimeLimit(type, toggle, adjust) {
	// relavite time limit set
	if (type == 'relative') {
		// what is set at the moment on screen, not at server
		var displayedRelativeTimeLimit = +$('#relative-time-limit-value').text();
		
		// start/stop
		if (toggle !== null) {
			
			if (toggle === false) {
				// stop, i.e. set time limit to 0
				relativeTimeLimit = 0;
				updateTimeLimitOnServer();
				return;
			}
			
			// start, i.e. set backend time limit to whatever is set on screen
			if (toggle === true && displayedRelativeTimeLimit > 0) {
				relativeTimeLimit = displayedRelativeTimeLimit;
				// when teacher enables relative time limit, absolute one gets disabled
				absoluteTimeLimit = null;
				updateTimeLimitOnServer();
			}
			return;
		}
		
		// no negative time limit is allowed
		if (displayedRelativeTimeLimit == 0 && adjust < 0) {
			return;
		}
		
		var adjustedRelativeTimeLimit = displayedRelativeTimeLimit + adjust;
		// at least one minute is required
		// if teacher wants to set less, he should disable the limit or click "finish now"
		if (adjustedRelativeTimeLimit < 1) {
			adjustedRelativeTimeLimit = 1;
		}
		
		// is time limit already enforced? if so, update the server
		if (relativeTimeLimit > 0) {
			relativeTimeLimit = adjustedRelativeTimeLimit;
			updateTimeLimitOnServer();
			return;
		}
		
		// if time limit is not enforced yet, just update the screen
		displayedRelativeTimeLimit = adjustedRelativeTimeLimit;
		$('#relative-time-limit-value').text(displayedRelativeTimeLimit);
		$('#relative-time-limit-start').prop('disabled', false);
		return;
	}
	
	if (type == 'absolute') {
		// get existing value on counter, if it is set already
		var counters = $('.absolute-time-limit-counter'),
			secondsLeft = null;
		if (counters.length > 0) {
			var periods = counters.first().countdown('getTimes');
			secondsLeft = $.countdown.periodsToSeconds(periods);
		}
		
		if (toggle !== null) {
			
			// start/stop
			if (toggle === false) {
				absoluteTimeLimit = null;
				updateAbsoluteTimeLimitCounter();
				return;
			} 
			
			// turn on the time limit, if there is any value on counter set already
			if (toggle === true && secondsLeft) {
				updateAbsoluteTimeLimitCounter(secondsLeft, true);
				return;
			}
			
			if (toggle === 'stop') {
				absoluteTimeLimit =  Math.round(new Date().getTime() / 1000);
				updateAbsoluteTimeLimitCounter();
			}
			return;
		}
		
		// counter is not set yet and user clicked negative value
		if (!secondsLeft && adjust < 0) {
			return;
		}
		
		// adjust time
		secondsLeft += adjust * 60;
		if (secondsLeft < 60) {
			secondsLeft = 60;
		}

		// is time limit already enforced, update the server
		// if time limit is not enforced yet, just update the screen
		updateAbsoluteTimeLimitCounter(secondsLeft);
		$('#absolute-time-limit-start').prop('disabled', false);
		return;
	}
	
	if (type == 'individual') {
		// this method is called with updateTimeLimit.call() so we can change meaning of "this"
		// and identify row and userUid
		var button = $(this),
			row = button.closest('.individual-time-limit-row'),
			userId = row.data('userId'),
			sessionId = row.data('sessionId'),
			itemId = userId ? 'user-' + userId : 'group-' + sessionId;
		
		// disable individual time adjustment
		if (toggle === false) {
			updateIndividualTimeLimitOnServer(itemId);
			return;
		}
		var existingAdjustment = +$('.individual-time-limit-value', row).text(),
			newAdjustment = existingAdjustment + adjust;
		
		updateIndividualTimeLimitOnServer(itemId, newAdjustment);
		return;
	}
}

function updateTimeLimitOnServer() {
	
	// absolute time limit has higher priority
	if (absoluteTimeLimit != null) {
		relativeTimeLimit = 0;
	}
	
	$.ajax({
		'url' : '/lams/${param.controllerContext}/updateTimeLimit.do',
		'type': 'post',
		'cache' : 'false',
		'data': {
			'toolContentID' : '${param.toolContentId}',
			'relativeTimeLimit' : relativeTimeLimit,
			'absoluteTimeLimit' : absoluteTimeLimit,
			'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
		},
		success : function(){
			// update widgets
			$('#relative-time-limit-value').text(relativeTimeLimit);
			
			if (relativeTimeLimit > 0) {
				$('#relative-time-limit-disabled').addClass('hidden');
				$('#relative-time-limit-cancel').removeClass('hidden');
				$('#relative-time-limit-enabled').removeClass('hidden');
				$('#relative-time-limit-start').addClass('hidden');
			} else {
				$('#relative-time-limit-disabled').removeClass('hidden');
				$('#relative-time-limit-cancel').addClass('hidden');
				$('#relative-time-limit-enabled').addClass('hidden');
				$('#relative-time-limit-start').removeClass('hidden').prop('disabled', true);
			}
			
			if (absoluteTimeLimit === null) {
				// no absolute time limit? destroy the counter
				$('.absolute-time-limit-counter').countdown('destroy');
				$('.absolute-time-limit-value').empty();
				hideTimeLimitWidget(true);
				
				$('#absolute-time-limit-disabled').removeClass('hidden');
				$('#absolute-time-limit-cancel').addClass('hidden');
				$('#absolute-time-limit-enabled').addClass('hidden');
				$('#absolute-time-limit-start').removeClass('hidden').prop('disabled', true);
				$('#absolute-time-limit-finish-now').prop('disabled', false);
			} else {
				showTimeLimitWidget(false);
				
				$('#absolute-time-limit-disabled').addClass('hidden');
				$('#absolute-time-limit-cancel').removeClass('hidden');
				$('#absolute-time-limit-enabled').removeClass('hidden');
				$('#absolute-time-limit-start').addClass('hidden');
				$('#absolute-time-limit-finish-now').prop('disabled', absoluteTimeLimit <= Math.round(new Date().getTime() / 1000));
			}
		}
	});
}

function updateAbsoluteTimeLimitCounter(secondsLeft, start) {
	var now = Math.round(new Date().getTime() / 1000),
		// preset means that counter is set just on screen and the time limit is not enforced for learners
		preset = start !== true && absoluteTimeLimit == null;
	
	if (secondsLeft) {
		if (!preset) {
			// time limit is already enforced on server, so update it there now
			absoluteTimeLimit = now + secondsLeft;
			updateTimeLimitOnServer();
		}
	} else {
		if (absoluteTimeLimit == null) {
			// disable the counter
			updateTimeLimitOnServer();
			return;
		}
		// counter initialisation on page load or "finish now"
		secondsLeft = absoluteTimeLimit - now;
		if (secondsLeft <= 0) {
			// finish now
			updateTimeLimitOnServer();
		}
	}
	
	var counters = $('.absolute-time-limit-counter');

	if (counters.length == 0) {
		counters = $('<div />').attr('id', 'absolute-time-limit-panel-counter')
							  .addClass('absolute-time-limit-counter')
							  .appendTo('#absolute-time-limit-panel-value')
				  			  .add(
						  		$('<div />').attr('id', 'absolute-time-limit-widget-counter')
						  					.addClass('absolute-time-limit-counter')
						  					.appendTo('#absolute-time-limit-widget-value'))
							  .countdown({
									until: '+' + secondsLeft +'S',
									format: 'hMS',
									compact: true,
									alwaysExpire : true,
									onTick: function(periods) {
										// check for 30 seconds or less and display timer in red
										var secondsLeft = $.countdown.periodsToSeconds(periods),
											keepOpen = secondsLeft <= 60 && secondsLeft > 0 
													   && $('#absolute-time-limit-start').hasClass('hidden'),
											widgetToggle = $('#time-limit-widget-toggle'),
											expiredHideContainers = $('.expired-hide-container');

										counters.data('keepOpen', keepOpen);
										widgetToggle.data('prevent-toggle', keepOpen)

										if (keepOpen) {
											showTimeLimitWidget(true);
											counters.addClass('countdown-timeout');
										}
										
										if (secondsLeft > 0) {
											expiredHideContainers.show();
											
											if (secondsLeft > 60) {
												counters.removeClass('countdown-timeout');
											}
										} else {
											expiredHideContainers.hide();
										}
									},
									expiryText : '<span class="countdown-timeout"><fmt:message key="label.monitoring.time.limit.expired" /></span>'
								});
	} else {
		// if counter is paused, we can not adjust time, so resume it for a moment
		counters.countdown('resume');
		counters.countdown('option', 'until', secondsLeft + 'S');
	}
	
	if (preset) {
		counters.countdown('pause');
		$('#absolute-time-limit-start').removeClass('disabled');
	} else {
		counters.countdown('resume');
	}
}

function timeLimitFinishNow(){
	if (confirm('<fmt:message key="label.monitoring.time.limit.finish.now.confirm" />')) {
		updateTimeLimit('absolute', 'stop');
	}
}


function initInidividualTimeLimitAutocomplete(){
	$('#individual-time-limit-autocomplete').autocomplete({
		'source' : '/lams/${param.controllerContext}/getPossibleIndividualTimeLimits.do?toolContentID=${param.toolContentId}',
		'delay'  : 700,
		'minLength' : 3,
		'select' : function(event, ui){
			// user ID or group ID, and default 0 adjustment
			updateIndividualTimeLimitOnServer(ui.item.value, 0);

			// clear search field
			$(this).val('');
			return false;
		},
		'focus': function() {
			// Stop the autocomplete of resetting the value to the selected one
			// It puts LAMS user ID instead of user name
			event.preventDefault();
		}
	});
	
	refreshInidividualTimeLimits();
}


function updateIndividualTimeLimitOnServer(itemId, adjustment) {
	$.ajax({
		'url' : '/lams/${param.controllerContext}/updateIndividualTimeLimit.do',
		'type': 'post',
		'cache' : 'false',
		'data': {
			'toolContentID' : '${param.toolContentId}',
			// itemId can user-<userId> or group-<groupId>
			'itemId' : itemId,
			'adjustment' : adjustment,
			'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
		},
		success : function(){
			refreshInidividualTimeLimits();
		}
	});
}


function refreshInidividualTimeLimits() {
	var table = $('#time-limit-table');
	
	$.ajax({
		'url' : '/lams/${param.controllerContext}/getExistingIndividualTimeLimits.do',
		'dataType' : 'json',
		'cache' : 'false',
		'data': {
			'toolContentID' : '${param.toolContentId}'
		},
		success : function(users) {
			// remove existing time limits
			$('.individual-time-limit-row', table).remove();
			
			if (!users) {
				return;
			}
			
			var template = $('#individual-time-limit-template-row'),
				now = new Date().getTime();
			$.each(users, function(){
				var row = template.clone()
								  .attr('id', 'individual-time-limit-row-' + this.userId)
								  .data('userId', this.userId)
								  .data('sessionId', this.sessionId)
								  .addClass('individual-time-limit-row')
								  .appendTo(table);
				$('.individual-time-limit-user-name', row).text(this.name);
				$('.individual-time-limit-value', row).text(this.adjustment);
				
				row.removeClass('hidden');
			});
		}
	});
}

function hideTimeLimitWidget(remove){
	var timeLimitWidget = $('#time-limit-widget'),
		timeLimitContent = $('#time-limit-widget-content', timeLimitWidget);
	if (timeLimitContent.hasClass('in')){
		// if the widget is expanded, collapse it and then hide it
		timeLimitContent.data('remove', remove).collapse('hide');
	} else if (remove){
		// if the widget is collapsed, hide it straight away
		timeLimitWidget.hide();
	}
}

function showTimeLimitWidget(expand) {
	var timeLimitWidget = $('#time-limit-widget'),
		timeLimitContent = $('#time-limit-widget-content', timeLimitWidget);
	timeLimitWidget.show();
	
	if (expand && !timeLimitContent.hasClass('in')){
		timeLimitContent.collapse('show');
	}
}

function scrollToTimeLimitPanel() {
	    $([document.documentElement, document.body]).animate({
	        scrollTop: $("#time-limit-panel").offset().top
	    }, 2000);
	    
	    $('#time-limit-collapse').collapse('show');
	    
	    if (!$('#absolute-time-limit-widget-counter').data('keepOpen')){
	    	hideTimeLimitWidget(false);
		}
}
</script>

<c:set var="absoluteTimeLimitEnabled" value="${not empty param.absoluteTimeLimit}" />
<c:set var="relativeTimeLimitEnabled" value="${param.relativeTimeLimit != 0}" />
		
<div class="panel panel-default ${param.isTbl ? 'voffset20' : ''}" id="time-limit-panel">
   	<c:choose>
   		<c:when test="${param.isTbl}">
   			 <div class="panel-heading">
	        	<span class="panel-title">
	        		<fmt:message key="label.monitoring.time.limit"/>
	      		</span>
	         </div>
   			 <div class="panel-body">
   		</c:when>
   		<c:otherwise>
    		<div class="panel-heading collapsable-icon-left" id="time-limit-heading">
	        	<span class="panel-title">
			    	<a class="collapsed" role="button" data-toggle="collapse" href="#time-limit-collapse" aria-expanded="false" aria-controls="time-limit-collapse" >
		          		<fmt:message key="label.monitoring.time.limit"/>
		        	</a>
	      		</span>
	        </div>
	        <div id="time-limit-collapse" class="panel-body panel-collapse collapse" role="tabpanel" aria-labelledby="time-limit-heading">
   		</c:otherwise>
   	</c:choose>
      
		<table id="time-limit-table" class="table">
			<tr class="info">
				<td colspan="6"><h4><fmt:message key="label.monitoring.time.limit.relative"/></h4>
					<p><fmt:message key="label.monitoring.time.limit.relative.desc"/></p>
				</td>
			</tr>
			<tr>
				<td>
					<span id="relative-time-limit-value">${param.relativeTimeLimit}</span>&nbsp;
					<fmt:message key="label.monitoring.time.limit.minutes"/>
				</td>
				<td class="centered">
					<div id="relative-time-limit-enabled" class="text-success ${relativeTimeLimitEnabled ? '' : 'hidden'}">
						<fmt:message key="label.monitoring.time.limit.enabled"/>
					</div>
					<div id="relative-time-limit-disabled" class="text-danger ${relativeTimeLimitEnabled ? 'hidden' : ''}">
						<fmt:message key="label.monitoring.time.limit.disabled"/>
					</div>
				</td>
				<td class="centered">
					<button id="relative-time-limit-start" class="btn btn-success btn-xs ${relativeTimeLimitEnabled ? 'hidden' : ''}"
							onClick="updateTimeLimit('relative', true)" disabled>
						<fmt:message key="label.monitoring.time.limit.start"/>
					</button>
					<button id="relative-time-limit-cancel" class="btn btn-danger btn-xs ${relativeTimeLimitEnabled ? '' : 'hidden'}"
							onClick="updateTimeLimit('relative', false)">
						<fmt:message key="label.monitoring.time.limit.cancel"/>
					</button>
				</td>
				<td>
					<!-- Finish now button at absolute time limit row -->
				</td>
				<td class="centered">
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('relative', null, 1)">
						<fmt:message key="label.monitoring.time.limit.plus.minute.1"/>
					</button>
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('relative', null, 5)">
						<fmt:message key="label.monitoring.time.limit.plus.minute.5"/>
					</button>
				</td>
				<td class="centered">
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('relative', null, -5)">
						<fmt:message key="label.monitoring.time.limit.minus.minute.5"/>
					</button>
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('relative', null, -1)">
						<fmt:message key="label.monitoring.time.limit.minus.minute.1"/>
					</button>
				</td>
			</tr>
			<tr>
				<td colspan="6">
	                        <div style="height: 30px; overflow:hidden;">
	                        </div>
				</td>
			</tr>	
			<tr class="info">
				<td colspan="6"><h4><fmt:message key="label.monitoring.time.limit.absolute"/></h4>
					<p><fmt:message key="label.monitoring.time.limit.absolute.desc"/></p>
				</td>
			</tr>
			<tr>
				<td id="absolute-time-limit-panel-value" class="absolute-time-limit-value"></td>
				<td>
					<div id="absolute-time-limit-enabled" class="text-success ${absoluteTimeLimitEnabled ? '' : 'hidden'}">
						<fmt:message key="label.monitoring.time.limit.enabled"/>
					</div>
					<div id="absolute-time-limit-disabled" class="text-danger ${absoluteTimeLimitEnabled ? 'hidden' : ''}">
						<fmt:message key="label.monitoring.time.limit.disabled"/>
					</div>
				</td>
				<td class="centered">
					<button id="absolute-time-limit-start" class="btn btn-success btn-xs ${absoluteTimeLimitEnabled ? 'hidden' : ''}"
							onClick="updateTimeLimit('absolute', true)" disabled>
						<fmt:message key="label.monitoring.time.limit.start"/>
					</button>
					<button id="absolute-time-limit-cancel" class="btn btn-danger btn-xs ${absoluteTimeLimitEnabled ? '' : 'hidden'}"
							onClick="updateTimeLimit('absolute', false)">
						<fmt:message key="label.monitoring.time.limit.cancel"/>
					</button>
				</td>
				<td class="centered">
					<button id="absolute-time-limit-finish-now" class="btn btn-warning btn-xs"
							onClick="timeLimitFinishNow()">
						<fmt:message key="label.monitoring.time.limit.finish.now"/>
					</button>
				</td>
				<td class="centered">
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('absolute', null, 1)">
						<fmt:message key="label.monitoring.time.limit.plus.minute.1"/>
					</button>
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('absolute', null, 5)">
						<fmt:message key="label.monitoring.time.limit.plus.minute.5"/>
					</button>
				</td>
				<td class="centered">
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('absolute', null, -5)">
						<fmt:message key="label.monitoring.time.limit.minus.minute.5"/>
					</button>
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('absolute', null, -1)">
						<fmt:message key="label.monitoring.time.limit.minus.minute.1"/>
					</button>
				</td>
			</tr>
			<tr>
	                  <td colspan="6">
					  <div style="height: 30px; overflow:hidden;">
					  </div>
	                  </td>
	              </tr>
							
			<tr>
				<td colspan="6" class="info">
					<h4><fmt:message key="label.monitoring.time.limit.individual"/></h4>
					<p><fmt:message key="label.monitoring.time.limit.individual.desc"/></p>
				</td>
			</tr>
			<tr>	
				<td colspan="6">
					<div class="input-group">
	    				<span class="input-group-addon"><i class="fa fa-search"></i></span>
	    				<input id="individual-time-limit-autocomplete" type="text" class="ui-autocomplete-input form-control input-sm" 
	    	   				   placeholder='<fmt:message key="label.monitoring.time.limit.individual.placeholder" />' />
					</div>
				</td>
			</tr>
			
			<tr id="individual-time-limit-template-row" class="hidden">
				<td class="individual-time-limit-user-name"></td>
				<td  class="centered">
					<span class="individual-time-limit-value"></span>
					<fmt:message key="label.monitoring.time.limit.minutes"/>
					<!-- (<time class="timeago" />)  -->
				</td>
				<td class="centered">
					<button id="individual-time-limit-cancel" class="btn btn-danger btn-xs"
							onClick="updateTimeLimit.call(this, 'individual', false)">
						<fmt:message key="label.monitoring.time.limit.cancel"/>
					</button>
				</td>
				<td>
					<!-- Finish now button at absolute time limit row -->
				</td>
				<td class="centered">
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit.call(this, 'individual', null, 1)">
						<fmt:message key="label.monitoring.time.limit.plus.minute.1"/>
					</button>
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit.call(this, 'individual', null, 5)">
						<fmt:message key="label.monitoring.time.limit.plus.minute.5"/>
					</button>
				</td>
				<td class="centered">
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit.call(this, 'individual', null, -5)">
						<fmt:message key="label.monitoring.time.limit.minus.minute.5"/>
					</button>
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit.call(this, 'individual', null, -1)">
						<fmt:message key="label.monitoring.time.limit.minus.minute.1"/>
					</button>
				</td>
			</tr>
			
		</table>
	</div>
</div>

<div class="panel panel-default" id="time-limit-widget">
	<div class="panel-heading"
		 id="time-limit-widget-heading">
       	<span class="panel-title">
	    	<a class="collapsed" role="button" id="time-limit-widget-toggle">
	    	   <i class="fa fa-clock-o"></i>
	    	   <div id="absolute-time-limit-widget-value" class="absolute-time-limit-value"
	    	   		 title="<fmt:message key="label.monitoring.time.limit" />"></div>
	       	</a>
     	</span>
    </div>

    <div id="time-limit-widget-content" class="panel-collapse collapse" role="tabpanel"
       	 aria-labelledby="time-limit-widget-heading">
		<div class="expired-hide-container">				
			<button class="btn btn-success" id="time-limit-widget-add-1-minute"
					onClick="updateTimeLimit('absolute', null, 1)">
				<fmt:message key="label.monitoring.time.limit.plus.minute.1"/>
			</button>
		</div>
		
		<div class="expired-hide-container">				
			<button class="btn btn-danger"
					onClick="updateTimeLimit('absolute', false)">
				<fmt:message key="label.monitoring.time.limit.cancel"/>
			</button>
		</div>
		
		<div>				
			<button class="btn btn-default"
					onClick="scrollToTimeLimitPanel()">
				<fmt:message key="label.monitoring.time.limit.show.controls"/>
			</button>
		</div>
	</div>
</div>