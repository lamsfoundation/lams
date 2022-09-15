<%@ include file="/taglibs.jsp"%>


<!--waiting learner information table-->

<c:if test="${not gateForm.gate.gateOpen}">
	<div class="text-center">
		<div id="gateStatus" class="w-50 d-inline-block alert alert-danger">
			<i class="fa fa-lg fa-minus-circle"></i>
			<strong><fmt:message key="label.gate.closed"/></strong>
		</div>
	</div>
</c:if>		

<c:if test="${not empty gateForm.gate.description}">
	<!-- general information section-->
	<h5>
		<lams:out value="${gateForm.gate.description}" escapeHtml="true" />
	</h5>
</c:if>

<p id="gate-waiting-learners-p">
	<span><fmt:message key="label.grouping.status"/></span> 
	<strong> <fmt:message key="label.gate.waiting.learners">
			<fmt:param value="${gateForm.waitingLearners}" />
			<fmt:param value="${gateForm.totalLearners}" />
		</fmt:message>
	</strong>
</p>


<c:if test="${not gateForm.gate.gateOpen and gateForm.gate.gateStopAtPrecedingActivity}">
	<p>
		<fmt:message key="label.gate.stop.at.preceding"/>
	</p>
</c:if>