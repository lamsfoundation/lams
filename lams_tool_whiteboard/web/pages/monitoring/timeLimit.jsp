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

		<c:set var="absoluteTimeLimitEnabled" value="${not empty whiteboard.absoluteTimeLimit}" />
		<c:set var="relativeTimeLimitEnabled" value="${whiteboard.relativeTimeLimit != 0}" />
        <div id="time-limit-collapse" class="panel-body panel-collapse collapse" role="tabpanel" aria-labelledby="time-limit-heading">

			<table id="time-limit-table" class="table">
				<tr class="info">
					<td colspan="6"><h4><fmt:message key="label.monitoring.summary.time.limit.relative"/></h4>
						<p><fmt:message key="label.monitoring.summary.time.limit.relative.desc"/></p>
					</td>
				</tr>
				<tr>
					<td>
						<span id="relative-time-limit-value">${whiteboard.relativeTimeLimit}</span>&nbsp;
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
</div>
