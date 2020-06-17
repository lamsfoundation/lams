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

		<c:set var="relativeTimeLimitEnabled" value="${assessment.timeLimit != 0}" />
        <div id="time-limit-collapse" class="panel-collapse collapse" role="tabpanel" aria-labelledby="time-limit-heading">
			<table class="table">
				<tr>
					<th colspan="7"><fmt:message key="label.monitoring.summary.time.limit.relative"/></th>
				</tr>
				<tr>
					<td>
						<span id="relative-time-limit-value">${assessment.timeLimit}</span>&nbsp;
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
						<button id="relative-time-limit-start" class="btn btn-success btn-xs disabled ${relativeTimeLimitEnabled ? 'hidden' : ''}"
								onClick="timeLimitControl('relative', true)">
							<fmt:message key="label.monitoring.summary.time.limit.start"/>
						</button>
						<button id="relative-time-limit-cancel" class="btn btn-danger btn-xs ${relativeTimeLimitEnabled ? '' : 'hidden'}"
								onClick="timeLimitControl('relative', false)">
							<fmt:message key="label.monitoring.summary.time.limit.cancel"/>
						</button>
					</td>
					<td>
						<button id="relative-time-limit-plus-1-minute" class="btn btn-default btn-xs"
								onClick="timeLimitControl('relative', null, 1)">
							<fmt:message key="label.monitoring.summary.time.limit.plus.minute.1"/>
						</button>
					</td>
					<td>
						<button id="relative-time-limit-plus-5-minute" class="btn btn-default btn-xs"
								onClick="timeLimitControl('relative', null, 5)">
							<fmt:message key="label.monitoring.summary.time.limit.plus.minute.5"/>
						</button>
					</td>
					<td>
						<button id="relative-time-limit-minus-5-minute" class="btn btn-default btn-xs"
								onClick="timeLimitControl('relative', null, -5)">
							<fmt:message key="label.monitoring.summary.time.limit.minus.minute.5"/>
						</button>
					</td>
					<td>
						<button id="relative-time-limit-minus-1-minute" class="btn btn-default btn-xs"
								onClick="timeLimitControl('relative', null, -1)">
							<fmt:message key="label.monitoring.summary.time.limit.minus.minute.1"/>
						</button>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
