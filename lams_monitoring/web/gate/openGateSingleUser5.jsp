			<style type="text/css">
				.gateLearners {
					height: 300px;
					overflow: auto;
					padding: 5px 0 5px 5px;
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
	<div class="container-fluid">
		<div class="row">
			<div class="col-4">		
				<div class="form-check text-danger text-center mb-2">
					<input id="all-select-forbidden" class="form-check-input" 
						   type="checkbox" value="${learner.userId}" onClick="javascript:selectUnselectAll('forbidden')" />
					<label for="all-select-forbidden" class="form-check-label">
						<strong><fmt:message key="label.gate.list.all.learners"/></strong>
					</label>
				</div>		   
				<div class="card">
					<div class="card-body gateLearners"  id="forbidden">
						<c:forEach var="learner" items="${gateForm.forbiddenLearnerList}">
							<div class="form-check mb-1">
								<input id="forbidden-${learner.userId}" class="form-check-input" 
									   type="checkbox" value="${learner.userId}" onClick="javascript:updateSelectAll('forbidden')" />
								<label for="forbidden-${learner.userId}" class="form-check-label">
									<c:out value="${learner.firstName} ${learner.lastName}" escapeXml="true"/>
								</label>
							</div>
						</c:forEach>
					</div>
				</div>
				<input type="submit" class="btn btn-secondary w-100 mt-2" 
					   value="<fmt:message key="label.gate.allow"/>"
					   onclick="javascript:allowUsers('forbidden')"/>	
			</div>
			
			<div class="col-4">
				<div class="form-check text-center mb-2">
					<input id="all-select-waiting" class="form-check-input" 
						   type="checkbox" value="${learner.userId}" onClick="javascript:selectUnselectAll('waiting')" />
					<label for="all-select-waiting" class="form-check-label">
						<strong><fmt:message key="label.gate.list.waiting.learners"/></strong>
					</label>
				</div>	
				<div class="card">
					<div class="card-body gateLearners" id="waiting">
						<c:forEach var="learner" items="${gateForm.waitingLearnerList}">
							<div class="form-check mb-1">
								<input id="waiting-${learner.userId}" class="form-check-input" 
									   type="checkbox" value="${learner.userId}" onClick="javascript:updateSelectAll('waiting')" />
								<label for="waiting-${learner.userId}" class="form-check-label">
									<c:out value="${learner.firstName} ${learner.lastName}" escapeXml="true"/>
								</label>
							</div>
						</c:forEach>
					</div>
				</div>
				<input type="submit" class="btn btn-secondary w-100 mt-2" 
					   value="<fmt:message key="label.gate.allow"/>"
					   onclick="javascript:allowUsers('waiting')"/>
			</div>
					
			<div class="col-4">
					<div class="form-check text-center text-success mb-2">
					<label for="all-select-waiting" class="form-check-label">
						<strong><fmt:message key="label.gate.list.allowed.learners"/></strong>
					</label>
				</div>	
				<div class="card">
					<div class="card-body gateLearners">
						<c:forEach var="learner" items="${gateForm.allowedToPassLearnerList}">
							<label class="form-check-label"><c:out value="${learner.firstName} ${learner.lastName}" escapeXml="true"/></label>
							<br />
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
	</div>	

</form:form>		
