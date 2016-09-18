	<%@ taglib uri="tags-html" prefix="html" %>
	<%@ taglib uri="tags-bean" prefix="bean" %>
	<%@ taglib uri="tags-logic" prefix="logic" %>
	<%@ taglib uri="tags-core" prefix="c" %>		
	<%@ taglib uri="tags-fmt" prefix="fmt" %>
	<%@ taglib uri="tags-lams" prefix="lams" %>
	<script type="text/javascript">
		function onSubmitForm(){
			var select = document.getElementById(document.pressed);
			if (select.selectedIndex==-1){
				return false;
			}
			else {
				document.getElementById("userId").value=select.options[select.selectedIndex].value;
				return true;
			}
		}
	</script>
	<c:set var="title"><fmt:message key="label.permission.gate.title"/></c:set>
	<lams:Page title="${title}" type="monitoring">
		
		<%@ include file="gateInfo.jsp" %>
		
		<c:if test="${not GateForm.map.gate.gateOpen}" >
			<p><fmt:message key="label.gate.you.open.message"/></p>
		</c:if>

		<%@ include file="gateStatus.jsp" %>
		<c:if test="${not GateForm.map.gate.gateOpen}" >
					<%@ include file="openGateSingleUser.jsp" %>
		</c:if>

	</lams:Page>