			<hr />
			<p><fmt:message key="label.gate.open.single.learner"/></p>
			<html:form action="/gate?method=openGateForSingleUser" onsubmit="return onSubmitForm();" target="_self">
			<input type="hidden" id="activityId" name="activityId" value="${GateForm.map.activityId}" />
			<input type="hidden" id="userId" name="userId" />
			
			<div class="row">
				<div class="col-sm-4">
					  <p class="text-danger text-center"><strong><fmt:message key="label.gate.list.all.learners"/></strong></p>
						<select class="form-control"  id="forbidden" name="forbidden" size="10">
							<c:forEach var="learner" items="${GateForm.map.forbiddenLearnerList}">
								<option value="${learner.userId}"><c:out value="${learner.firstName} ${learner.lastName}" escapeXml="true"/></option>
							</c:forEach>
						</select>
						<br/>
						<input style="width: 160px" type="submit"  class="btn btn-default btn-xs"  value="<fmt:message key="label.gate.allow"/>" onclick="document.pressed='forbidden'"/>	
				</div>
				<div class="col-sm-4">
						<p class="text-center"><strong><fmt:message key="label.gate.list.waiting.learners"/></strong></p>
						<select class="form-control"   id="waiting" name="waiting" size="10">
							<c:forEach var="learner" items="${GateForm.map.waitingLearnerList}">
								<option value="${learner.userId}"><c:out value="${learner.firstName} ${learner.lastName}" escapeXml="true"/></option>
							</c:forEach>
						</select>
						<br/>
						<input style="width: 160px" type="submit" class="btn btn-default btn-xs"  value="<fmt:message key="label.gate.allow"/>" onclick="document.pressed='waiting'"/>
				</div>
				<div class="col-sm-4">
							<p class="text-success text-center"><strong><fmt:message key="label.gate.list.allowed.learners"/></strong></p>
							<select class="form-control"  id="allowed" name="allowed" size="10" disabled="disabled">
							<c:forEach var="learner" items="${GateForm.map.allowedToPassLearnerList}">
								<option><c:out value="${learner.firstName} ${learner.lastName}" escapeXml="true"/></option>
							</c:forEach>
						</select>
				</div>
			</div>

			</html:form>		
