			<style type="text/css">
				.gateLearners {
					padding: 5px 0 5px 5px;
					height: 300px;
					overflow: auto;
				}
				
				.allowButton {
					width: 100%;
				}
				
				.column-heading {
			    	margin-right: 10px;
				    margin-left: 7px;
				}
				
			</style>
								
			<script type="text/javascript">
			
				function selectUnselectAll(column){
					if ( $("#all-select-"+column).is(":checked") ) {
						$("[id^="+column+"-]:input[type='checkbox']").prop("checked", true);
					} else {
						$("[id^="+column+"-]:input[type='checkbox']").prop("checked", false);
					}
				}
				
				function updateSelectAll(column){
					$("#all-select-"+column).prop("checked", 
							$("[id^="+column+"-]:input[type='checkbox']").length == $("[id^="+column+"-]:input[type='checkbox']:checked").length );
				}
			</script>
			
<hr />
<p><fmt:message key="label.gate.open.single.learner"/></p>
<form:form action="openGateForSingleUser.do" id="gateForm" modelAttribute="gateForm" target="_self">
	<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
	<input type="hidden" id="activityId" name="activityId" value="${gateForm.activityId}" />
	<input type="hidden" id="userId" name="userId" />
			
	<div class="row">
		<div class="col-sm-4">				   
			<p class="text-danger text-center column-heading"> <input id="all-select-forbidden" onClick="javascript:selectUnselectAll('forbidden');" type="checkbox"  class="pull-left" />
			<strong><fmt:message key="label.gate.list.all.learners"/></strong></p>
			<div class="panel panel-default gateLearners" id="forbidden">
				<c:forEach var="learner" items="${gateForm.forbiddenLearnerList}">
					<input id="forbidden-${learner.userId}" type="checkbox" value="${learner.userId}" onClick="javascript:updateSelectAll('forbidden')" />
					<label for="forbidden-${learner.userId}"><c:out value="${learner.firstName} ${learner.lastName}" escapeXml="true"/></label>
					<br />
				</c:forEach>
			</div>
			<input type="submit" class="btn btn-default btn-xs allowButton" 
				   value="<fmt:message key="label.gate.allow"/>"
				   onclick="javascript:allowUsers('forbidden')"/>	
		</div>
		
		<div class="col-sm-4">
			<p class="text-center column-heading"> <input id="all-select-waiting" onClick="javascript:selectUnselectAll('waiting');" type="checkbox"  class="pull-left" />
			<strong><fmt:message key="label.gate.list.waiting.learners"/></strong></p>
			<div class="panel panel-default gateLearners" id="waiting">
				<c:forEach var="learner" items="${gateForm.waitingLearnerList}">
					<input id="waiting-${learner.userId}" type="checkbox" value="${learner.userId}" onClick="javascript:updateSelectAll('waiting')"/>
					<label for="waiting-${learner.userId}"><c:out value="${learner.firstName} ${learner.lastName}" escapeXml="true"/></label>
					<br />
				</c:forEach>
			</div>
			<input type="submit" class="btn btn-default btn-xs allowButton" 
				   value="<fmt:message key="label.gate.allow"/>"
				   onclick="javascript:allowUsers('waiting')"/>
		</div>
				
		<div class="col-sm-4">
			<p class="text-success text-center"><strong><fmt:message key="label.gate.list.allowed.learners"/></strong></p>
			<div class="panel panel-default gateLearners">
				<c:forEach var="learner" items="${gateForm.allowedToPassLearnerList}">
					<span><c:out value="${learner.firstName} ${learner.lastName}" escapeXml="true"/></span>
					<br />
				</c:forEach>
			</div>
		</div>
	</div>

</form:form>		
