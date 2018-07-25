<%@ taglib uri="tags-lams" prefix="lams"%>

<c:if test="${not empty GateForm.map.gate.description}">
	<!-- general information section-->
	<p>
		<lams:out value="${GateForm.map.gate.description}" escapeHtml="true" />
	</p>
</c:if>

<!--waiting learner information table-->

<c:if test="${not GateForm.map.gate.gateOpen}">
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
<p>
<span><fmt:message key="label.grouping.status"/></span> 
	<strong> <fmt:message key="label.gate.waiting.learners">
			<fmt:param value="${GateForm.map.waitingLearners}" />
			<fmt:param value="${GateForm.map.totalLearners}" />
		</fmt:message>
	</strong>
</p>


