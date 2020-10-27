<%@ include file="/taglibs.jsp"%>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.timeago.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>
<script type="text/javascript">
	$(document).ready(function() {$("time.timeago").timeago();});
</script>
	        
<c:if test="${not gateForm.gate.gateOpen}">
	<c:if test="${not GateForm.readOnly}">
		<form:form action="openGate.do" id="gateForm" modelAttribute="gateForm" target="_self">
			<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
			<input type="hidden" name="activityId" value="${gateForm.activityId}" />
			<p>
				<input type="submit"  id="openGateButton" class="btn btn-primary btn-sm voffset10" value="<fmt:message key="label.gate.open"/>" />
			</p>
		</form:form>
	</c:if>    
</c:if>
<c:if test="${gateForm.gate.gateOpen}">
	<div id="gateStatus" class="alert alert-success" style="margin-bottom: 10px; padding: 8px">
		<div class="media">
			<div class="media-left">
				<i class="fa fa-lg fa-sign-in"></i>
			</div>
			<div class="media-body">
				<strong><fmt:message key="label.gate.gate.open"/>
				<c:if test="${not empty gateForm.gate.gateOpenTime}">
					&nbsp;<lams:Date value="${gateForm.gate.gateOpenTime}" timeago="true" />
				</c:if>
				<c:if test="${not empty gateForm.gate.gateOpenUser}">
					&nbsp;<fmt:message key="label.gate.gate.open.user">
						<fmt:param value="${gateForm.gate.gateOpenUser.firstName} ${gateForm.gate.gateOpenUser.lastName}" />
					</fmt:message>
				</c:if>
				</strong>
			</div>
		</div>
	</div>
	
			
	<form:form action="closeGate.do">
		<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
		<input type="hidden" name="activityId" value="${gateForm.activityId}" />
		<p>
			<input type="submit" class="btn btn-danger btn-sm voffset10"
				   title="<fmt:message key="button.task.gate.close.tooltip"/>"
				   value="<fmt:message key="button.task.gate.close"/>" />
		</p>
	</form:form>
</c:if>
