<%@ include file="/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("time.timeago").timeago();

		<c:if test="${param.closingGate eq 'true' and not gateForm.gate.gateOpen}">
			let opener = window.opener ? window.opener.document : null;
			// refresh gate tab in TBL monitoring
			if (window.opener && window.opener.document.URL.indexOf('#gates') > -1) {
				window.opener.location.reload();
			}
		</c:if>

		<c:if test="${gateJustToggled}">
			if (window.opener && typeof window.opener.updateSequenceTab === 'function') {
				window.opener.updateLessonTab();
				window.opener.updateSequenceTab();
			}
		</c:if>
	});
</script>
	        
<c:if test="${not gateForm.gate.gateOpen}">
	<c:if test="${not GateForm.readOnly}">
		<form:form action="openGate.do" id="gateForm" modelAttribute="gateForm" target="_self">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<input type="hidden" name="activityId" value="${gateForm.activityId}" />
			<p>
				<input type="submit"  id="openGateButton" class="btn btn-primary" value="<fmt:message key="label.gate.open"/>" />
			</p>
		</form:form>
	</c:if>    
</c:if>
<c:if test="${gateForm.gate.gateOpen}">
	<div id="gateStatus" class="alert alert-success p-3 mb-3">
		<i class="fa fa-lg fa-sign-in"></i>
		<strong>
			<fmt:message key="label.gate.gate.open"/>
			<c:if test="${not empty gateForm.gate.gateOpenTime}">
				&nbsp;<lams:Date value="${gateForm.gate.gateOpenTime}" timeago="true" />
			</c:if>
			<c:if test="${not empty gateForm.gate.gateOpenUser}">
				&nbsp;<fmt:message key="label.gate.gate.open.user">
					<fmt:param value="${gateForm.gate.gateOpenUser.getFullName()}" />
				</fmt:message>
			</c:if>
		</strong>
	</div>
	
			
	<form:form action="closeGate.do">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<input type="hidden" name="activityId" value="${gateForm.activityId}" />
		<input type="hidden" name="closingGate" value="true" />
		<p>
			<input type="submit" class="btn btn-danger"
				   title="<fmt:message key="button.task.gate.close.tooltip"/>"
				   value="<fmt:message key="button.task.gate.close"/>" />
		</p>
	</form:form>
</c:if>