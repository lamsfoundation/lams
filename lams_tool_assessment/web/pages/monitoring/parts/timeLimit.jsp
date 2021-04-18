<%@ include file="/common/taglibs.jsp"%>

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
</style>

<script>
// in minutes since learner entered the activity
var relativeTimeLimit = ${assessment.relativeTimeLimit},
	// in seconds since epoch started
	absoluteTimeLimit = ${empty assessment.absoluteTimeLimit ? 'null' : assessment.absoluteTimeLimitSeconds};

$(document).ready(function(){
	// create counter if absolute time limit is set
	if (absoluteTimeLimit) {
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
		var counter = $('#absolute-time-limit-counter'),
			secondsLeft = null;
		if (counter.length === 1) {
			var periods = counter.countdown('getTimes');
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
			userId = row.data('userId');
		
		// disable individual time adjustment
		if (toggle === false) {
			updateIndividualTimeLimitOnServer('user-' + userId);
			return;
		}
		var existingAdjustment = +$('.individual-time-limit-value', row).text(),
			newAdjustment = existingAdjustment + adjust;
		
		updateIndividualTimeLimitOnServer('user-' + userId, newAdjustment);
		return;
	}
}

function updateTimeLimitOnServer() {
	
	// absolute time limit has higher priority
	if (absoluteTimeLimit != null) {
		relativeTimeLimit = 0;
	}
	
	$.ajax({
		'url' : '<c:url value="/monitoring/updateTimeLimit.do"/>',
		'type': 'post',
		'cache' : 'false',
		'data': {
			'toolContentID' : '${assessment.contentId}',
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
				$('#absolute-time-limit-counter').countdown('destroy');
				$('#absolute-time-limit-value').empty();
				
				$('#absolute-time-limit-disabled').removeClass('hidden');
				$('#absolute-time-limit-cancel').addClass('hidden');
				$('#absolute-time-limit-enabled').addClass('hidden');
				$('#absolute-time-limit-start').removeClass('hidden').prop('disabled', true);
				$('#absolute-time-limit-finish-now').prop('disabled', false);
			} else {
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
	
	var counter = $('#absolute-time-limit-counter');

	if (counter.length == 0) {
		counter = $('<div />').attr('id', 'absolute-time-limit-counter').appendTo('#absolute-time-limit-value')
			.countdown({
				until: '+' + secondsLeft +'S',
				format: 'hMS',
				compact: true,
				alwaysExpire : true,
				onTick: function(periods) {
					// check for 30 seconds or less and display timer in red
					var secondsLeft = $.countdown.periodsToSeconds(periods);
					if (secondsLeft <= 30) {
						counter.addClass('countdown-timeout');
					} else {
						counter.removeClass('countdown-timeout');
					}				
				},
				expiryText : '<span class="countdown-timeout"><fmt:message key="label.monitoring.summary.time.limit.expired" /></span>'
			});
	} else {
		// if counter is paused, we can not adjust time, so resume it for a moment
		counter.countdown('resume');
		counter.countdown('option', 'until', secondsLeft + 'S');
	}
	
	if (preset) {
		counter.countdown('pause');
		$('#absolute-time-limit-start').removeClass('disabled');
	} else {
		counter.countdown('resume');
	}
}

function timeLimitFinishNow(){
	if (confirm('<fmt:message key="label.monitoring.summary.time.limit.finish.now.confirm" />')) {
		updateTimeLimit('absolute', 'stop');
	}
}


function initInidividualTimeLimitAutocomplete(){
	$('#individual-time-limit-autocomplete').autocomplete({
		'source' : '<c:url value="/monitoring/getPossibleIndividualTimeLimitUsers.do"/>?toolContentID=${assessment.contentId}',
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
	
	refreshInidividualTimeLimitUsers();
}


function updateIndividualTimeLimitOnServer(itemId, adjustment) {
	$.ajax({
		'url' : '<c:url value="/monitoring/updateIndividualTimeLimit.do"/>',
		'type': 'post',
		'cache' : 'false',
		'data': {
			'toolContentID' : '${assessment.contentId}',
			// itemId can user-<userId> or group-<groupId>
			'itemId' : itemId,
			'adjustment' : adjustment,
			'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
		},
		success : function(){
			refreshInidividualTimeLimitUsers();
		}
	});
}


function refreshInidividualTimeLimitUsers() {
	var table = $('#time-limit-table');
	
	$.ajax({
		'url' : '<c:url value="/monitoring/getExistingIndividualTimeLimitUsers.do"/>',
		'dataType' : 'json',
		'cache' : 'false',
		'data': {
			'toolContentID' : '${assessment.contentId}'
		},
		success : function(users) {
			// remove existing users
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
								  .addClass('individual-time-limit-row')
								  .appendTo(table);
				$('.individual-time-limit-user-name', row).text(this.name);
				$('.individual-time-limit-value', row).text(this.adjustment);
				
				row.removeClass('hidden');
			});
		}
	});
}
</script>

<c:set var="absoluteTimeLimitEnabled" value="${not empty assessment.absoluteTimeLimit}" />
<c:set var="relativeTimeLimitEnabled" value="${assessment.relativeTimeLimit != 0}" />
		
<div class="panel panel-default ${isTbl ? 'voffset20' : ''}" >
   	<c:choose>
   		<c:when test="${isTbl}">
   			 <div class="panel-heading">
	        	<span class="panel-title">
	        		<fmt:message key="label.monitoring.summary.time.limit"/>
	      		</span>
	         </div>
   			 <div class="panel-body">
   		</c:when>
   		<c:otherwise>
    		<div class="panel-heading collapsable-icon-left" id="time-limit-heading">
	        	<span class="panel-title">
			    	<a class="collapsed" role="button" data-toggle="collapse" href="#time-limit-collapse" aria-expanded="false" aria-controls="time-limit-collapse" >
		          		<fmt:message key="label.monitoring.summary.time.limit"/>
		        	</a>
	      		</span>
	        </div>
	        <div id="time-limit-collapse" class="panel-body panel-collapse collapse" role="tabpanel" aria-labelledby="time-limit-heading">
   		</c:otherwise>
   	</c:choose>
      
		<table id="time-limit-table" class="table">
			<tr class="info">
				<td colspan="6"><h4><fmt:message key="label.monitoring.summary.time.limit.relative"/></h4>
					<p><fmt:message key="label.monitoring.summary.time.limit.relative.desc"/></p>
				</td>
			</tr>
			<tr>
				<td>
					<span id="relative-time-limit-value">${assessment.relativeTimeLimit}</span>&nbsp;
					<fmt:message key="label.monitoring.summary.time.limit.minutes"/>
				</td>
				<td class="centered">
					<div id="relative-time-limit-enabled" class="text-success ${relativeTimeLimitEnabled ? '' : 'hidden'}">
						<fmt:message key="label.monitoring.summary.time.limit.enabled"/>
					</div>
					<div id="relative-time-limit-disabled" class="text-danger ${relativeTimeLimitEnabled ? 'hidden' : ''}">
						<fmt:message key="label.monitoring.summary.time.limit.disabled"/>
					</div>
				</td>
				<td class="centered">
					<button id="relative-time-limit-start" class="btn btn-success btn-xs ${relativeTimeLimitEnabled ? 'hidden' : ''}"
							onClick="updateTimeLimit('relative', true)" disabled>
						<fmt:message key="label.monitoring.summary.time.limit.start"/>
					</button>
					<button id="relative-time-limit-cancel" class="btn btn-danger btn-xs ${relativeTimeLimitEnabled ? '' : 'hidden'}"
							onClick="updateTimeLimit('relative', false)">
						<fmt:message key="label.monitoring.summary.time.limit.cancel"/>
					</button>
				</td>
				<td>
					<!-- Finish now button at absolute time limit row -->
				</td>
				<td class="centered">
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('relative', null, 1)">
						<fmt:message key="label.monitoring.summary.time.limit.plus.minute.1"/>
					</button>
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('relative', null, 5)">
						<fmt:message key="label.monitoring.summary.time.limit.plus.minute.5"/>
					</button>
				</td>
				<td class="centered">
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('relative', null, -5)">
						<fmt:message key="label.monitoring.summary.time.limit.minus.minute.5"/>
					</button>
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('relative', null, -1)">
						<fmt:message key="label.monitoring.summary.time.limit.minus.minute.1"/>
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
				<td colspan="6"><h4><fmt:message key="label.monitoring.summary.time.limit.absolute"/></h4>
					<p><fmt:message key="label.monitoring.summary.time.limit.absolute.desc"/></p>
				</td>
			</tr>
			<tr>
				<td id="absolute-time-limit-value"></td>
				<td>
					<div id="absolute-time-limit-enabled" class="text-success ${absoluteTimeLimitEnabled ? '' : 'hidden'}">
						<fmt:message key="label.monitoring.summary.time.limit.enabled"/>
					</div>
					<div id="absolute-time-limit-disabled" class="text-danger ${absoluteTimeLimitEnabled ? 'hidden' : ''}">
						<fmt:message key="label.monitoring.summary.time.limit.disabled"/>
					</div>
				</td>
				<td class="centered">
					<button id="absolute-time-limit-start" class="btn btn-success btn-xs ${absoluteTimeLimitEnabled ? 'hidden' : ''}"
							onClick="updateTimeLimit('absolute', true)" disabled>
						<fmt:message key="label.monitoring.summary.time.limit.start"/>
					</button>
					<button id="absolute-time-limit-cancel" class="btn btn-danger btn-xs ${absoluteTimeLimitEnabled ? '' : 'hidden'}"
							onClick="updateTimeLimit('absolute', false)">
						<fmt:message key="label.monitoring.summary.time.limit.cancel"/>
					</button>
				</td>
				<td class="centered">
					<button id="absolute-time-limit-finish-now" class="btn btn-warning btn-xs"
							onClick="timeLimitFinishNow()">
						<fmt:message key="label.monitoring.summary.time.limit.finish.now"/>
					</button>
				</td>
				<td class="centered">
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('absolute', null, 1)">
						<fmt:message key="label.monitoring.summary.time.limit.plus.minute.1"/>
					</button>
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('absolute', null, 5)">
						<fmt:message key="label.monitoring.summary.time.limit.plus.minute.5"/>
					</button>
				</td>
				<td class="centered">
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('absolute', null, -5)">
						<fmt:message key="label.monitoring.summary.time.limit.minus.minute.5"/>
					</button>
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit('absolute', null, -1)">
						<fmt:message key="label.monitoring.summary.time.limit.minus.minute.1"/>
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
					<h4><fmt:message key="label.monitoring.summary.time.limit.individual"/></h4>
					<p><fmt:message key="label.monitoring.summary.time.limit.individual.desc"/></p>
				</td>
			</tr>
			<tr>	
				<td colspan="6">
					<div class="input-group">
	    				<span class="input-group-addon"><i class="fa fa-search"></i></span>
	    				<input id="individual-time-limit-autocomplete" type="text" class="ui-autocomplete-input form-control input-sm" 
	    	   				   placeholder='<fmt:message key="label.monitoring.summary.time.limit.individual.placeholder" />' />
					</div>
				</td>
			</tr>
			
			<tr id="individual-time-limit-template-row" class="hidden">
				<td class="individual-time-limit-user-name"></td>
				<td  class="centered">
					<span class="individual-time-limit-value"></span>
					<fmt:message key="label.monitoring.summary.time.limit.minutes"/>
					<!-- (<time class="timeago" />)  -->
				</td>
				<td class="centered">
					<button id="individual-time-limit-cancel" class="btn btn-danger btn-xs"
							onClick="updateTimeLimit.call(this, 'individual', false)">
						<fmt:message key="label.monitoring.summary.time.limit.cancel"/>
					</button>
				</td>
				<td>
					<!-- Finish now button at absolute time limit row -->
				</td>
				<td class="centered">
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit.call(this, 'individual', null, 1)">
						<fmt:message key="label.monitoring.summary.time.limit.plus.minute.1"/>
					</button>
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit.call(this, 'individual', null, 5)">
						<fmt:message key="label.monitoring.summary.time.limit.plus.minute.5"/>
					</button>
				</td>
				<td class="centered">
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit.call(this, 'individual', null, -5)">
						<fmt:message key="label.monitoring.summary.time.limit.minus.minute.5"/>
					</button>
					<button class="btn btn-default btn-xs"
							onClick="updateTimeLimit.call(this, 'individual', null, -1)">
						<fmt:message key="label.monitoring.summary.time.limit.minus.minute.1"/>
					</button>
				</td>
			</tr>
			
		</table>
	</div>
</div>
