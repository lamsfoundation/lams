<%@ include file="/taglibs.jsp"%>

<c:if test="${not empty gateForm.gate.description}">
	<!-- general information section-->
	<p>
		<lams:out value="${gateForm.gate.description}" escapeHtml="true" />
	</p>
</c:if>

<!--waiting learner information table-->

<c:if test="${not gateForm.gate.gateOpen}">
	<div id="gateStatus" class="alert alert-danger" style="margin-bottom: 10px; padding: 8px">
				<div class="media">
					<div class="media-left">
						<i class="fa fa-lg fa-minus-circle"></i>
					</div>
					<div class="media-body">
						<strong><fmt:message key="label.gate.closed"/></strong>
					</div>
				</div>
	</div>
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