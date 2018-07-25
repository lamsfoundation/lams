	<%@ taglib uri="tags-html" prefix="html" %>
	<%@ taglib uri="tags-bean" prefix="bean" %>
	<%@ taglib uri="tags-logic" prefix="logic" %>
	<%@ taglib uri="tags-core" prefix="c" %>		
	<%@ taglib uri="tags-fmt" prefix="fmt" %>
	<%@ taglib uri="tags-lams" prefix="lams" %>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript">
		function allowUsers(who){
			var allowed = '';
			$('#' + who + ' input:checked').each(function(){
				allowed += $(this).val() + ',';
			});
			if (allowed == ''){
				return false;
			}
			$('#userId').val(allowed);
			return true;
		}
	</script>

	<c:set var="title"><fmt:message key="label.condition.gate.title"/></c:set>
	<lams:Page title="${title}" type="monitoring">
		
		<%@ include file="gateInfo.jsp" %>
		<c:if test="${not GateForm.map.gate.gateOpen}" >
			<p><fmt:message key="label.gate.you.open.message"/><br />
			<small><em><fmt:message key="message.gate.condition.force.pass" /></em></small></p>
		</c:if>
		
		<%@ include file="gateStatus.jsp" %>
		<c:if test="${not GateForm.map.gate.gateOpen}" >
				<%@ include file="openGateSingleUser.jsp" %>
		</c:if>

	</lams:Page>