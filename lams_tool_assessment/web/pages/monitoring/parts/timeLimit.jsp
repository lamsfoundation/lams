<%@ include file="/common/taglibs.jsp"%>

<div class="panel-group" id="time-limit-panel" role="tablist" aria-multiselectable="true"> 
    <div class="panel panel-default" >
        <div class="panel-heading collapsable-icon-left" id="time-limit-heading">
        	<span class="panel-title">
		    	<a class="collapsed" role="button" data-toggle="collapse" href="#time-limit-collapse" aria-expanded="false" aria-controls="time-limit-collapse" >
	          		<fmt:message key="label.monitoring.summary.time.limit"/>
	        	</a>
      		</span>
        </div>

		<c:set var="absoluteTimeLimitEnabled" value="${not empty assessment.absoluteTimeLimit}" />
		<c:set var="relativeTimeLimitEnabled" value="${assessment.relativeTimeLimit != 0}" />
        <div id="time-limit-collapse" class="panel-collapse collapse" role="tabpanel" aria-labelledby="time-limit-heading">
			<table id="time-limit-table" class="table">
				<tr>
					<th colspan="8"><fmt:message key="label.monitoring.summary.time.limit.relative"/></th>
				</tr>
				<tr>
					<td>
						<span id="relative-time-limit-value">${assessment.relativeTimeLimit}</span>&nbsp;
						<fmt:message key="label.monitoring.summary.time.limit.minutes"/>
					</td>
					<td>
						<div id="relative-time-limit-enabled" class="text-success ${relativeTimeLimitEnabled ? '' : 'hidden'}">
							<fmt:message key="label.monitoring.summary.time.limit.enabled"/>
						</div>
						<div id="relative-time-limit-disabled" class="text-danger ${relativeTimeLimitEnabled ? 'hidden' : ''}">
							<fmt:message key="label.monitoring.summary.time.limit.disabled"/>
						</div>
					</td>
					<td>
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
					<td>
						<button class="btn btn-default btn-xs"
								onClick="updateTimeLimit('relative', null, 1)">
							<fmt:message key="label.monitoring.summary.time.limit.plus.minute.1"/>
						</button>
					</td>
					<td>
						<button class="btn btn-default btn-xs"
								onClick="updateTimeLimit('relative', null, 5)">
							<fmt:message key="label.monitoring.summary.time.limit.plus.minute.5"/>
						</button>
					</td>
					<td>
						<button class="btn btn-default btn-xs"
								onClick="updateTimeLimit('relative', null, -5)">
							<fmt:message key="label.monitoring.summary.time.limit.minus.minute.5"/>
						</button>
					</td>
					<td>
						<button class="btn btn-default btn-xs"
								onClick="updateTimeLimit('relative', null, -1)">
							<fmt:message key="label.monitoring.summary.time.limit.minus.minute.1"/>
						</button>
					</td>
				</tr>
				
				<tr>
					<th colspan="8"><fmt:message key="label.monitoring.summary.time.limit.absolute"/></th>
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
					<td>
						<button id="absolute-time-limit-start" class="btn btn-success btn-xs ${absoluteTimeLimitEnabled ? 'hidden' : ''}"
								onClick="updateTimeLimit('absolute', true)" disabled>
							<fmt:message key="label.monitoring.summary.time.limit.start"/>
						</button>
						<button id="absolute-time-limit-cancel" class="btn btn-danger btn-xs ${absoluteTimeLimitEnabled ? '' : 'hidden'}"
								onClick="updateTimeLimit('absolute', false)">
							<fmt:message key="label.monitoring.summary.time.limit.cancel"/>
						</button>
					</td>
					<td>
						<button id="absolute-time-limit-finish-now" class="btn btn-warning btn-xs"
								onClick="timeLimitFinishNow()">
							<fmt:message key="label.monitoring.summary.time.limit.finish.now"/>
						</button>
					</td>
					<td>
						<button class="btn btn-default btn-xs"
								onClick="updateTimeLimit('absolute', null, 1)">
							<fmt:message key="label.monitoring.summary.time.limit.plus.minute.1"/>
						</button>
					</td>
					<td>
						<button class="btn btn-default btn-xs"
								onClick="updateTimeLimit('absolute', null, 5)">
							<fmt:message key="label.monitoring.summary.time.limit.plus.minute.5"/>
						</button>
					</td>
					<td>
						<button class="btn btn-default btn-xs"
								onClick="updateTimeLimit('absolute', null, -5)">
							<fmt:message key="label.monitoring.summary.time.limit.minus.minute.5"/>
						</button>
					</td>
					<td>
						<button class="btn btn-default btn-xs"
								onClick="updateTimeLimit('absolute', null, -1)">
							<fmt:message key="label.monitoring.summary.time.limit.minus.minute.1"/>
						</button>
					</td>
				</tr>
								
				<tr>
					<th colspan="2">
						<fmt:message key="label.monitoring.summary.time.limit.individual"/>
					</th>
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
					<td>
						<span class="individual-time-limit-value"></span>
						<fmt:message key="label.monitoring.summary.time.limit.minutes"/>
						<!-- (<time class="timeago" />)  -->
					</td>
					<td>
						<button id="individual-time-limit-cancel" class="btn btn-danger btn-xs"
								onClick="updateTimeLimit.call(this, 'individual', false)">
							<fmt:message key="label.monitoring.summary.time.limit.cancel"/>
						</button>
					</td>
					<td>
						<!-- Finish now button at absolute time limit row -->
					</td>
					<td>
						<button class="btn btn-default btn-xs"
								onClick="updateTimeLimit.call(this, 'individual', null, 1)">
							<fmt:message key="label.monitoring.summary.time.limit.plus.minute.1"/>
						</button>
					</td>
					<td>
						<button class="btn btn-default btn-xs"
								onClick="updateTimeLimit.call(this, 'individual', null, 5)">
							<fmt:message key="label.monitoring.summary.time.limit.plus.minute.5"/>
						</button>
					</td>
					<td>
						<button class="btn btn-default btn-xs"
								onClick="updateTimeLimit.call(this, 'individual', null, -5)">
							<fmt:message key="label.monitoring.summary.time.limit.minus.minute.5"/>
						</button>
					</td>
					<td>
						<button class="btn btn-default btn-xs"
								onClick="updateTimeLimit.call(this, 'individual', null, -1)">
							<fmt:message key="label.monitoring.summary.time.limit.minus.minute.1"/>
						</button>
					</td>
				</tr>
				
			</table>
		</div>
	</div>
</div>
