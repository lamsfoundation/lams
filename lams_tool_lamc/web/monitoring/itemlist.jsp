<%@ include file="/common/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<div id="itemList">
<h2><fmt:message key="label.questions" />
<img src="${ctxPath}/includes/images/indicator.gif" style="display:none" id="resourceListArea_Busy" /></h2>

<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
<table id="itemTable" style="align:left;width:650px" >
    <c:set var="queIndex" scope="request" value="0"/>
    
    
    <c:forEach items="${listQuestionContentDTO}" var="currentDTO" varStatus="status">
	<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
	<c:set var="question" scope="request" value="${currentDTO.question}"/>
	<c:set var="feedback" scope="request" value="${currentDTO.feedback}"/>
	<c:set var="displayOrder" scope="request" value="${currentDTO.displayOrder}"/>	

	<tr>
		<td width="10%" class="align-right">
			<strong> 	<fmt:message key="label.question" />: </strong>
		</td>

		<td width="60%" class="align-left">
			<div style="overflow: auto;">						
				<c:out value="${question}" escapeXml="false"/> 
			</div>														
		</td>		


			<td width="10%" class="align-right">
		 		<c:if test="${totalQuestionCount != 1}"> 		
		 		
			 		<c:if test="${queIndex == 1}"> 		
						<a title="<fmt:message key='label.tip.moveQuestionDown'/>" href="javascript:;" onclick="javascript:submitModifyMonitoringQuestion('<c:out value="${queIndex}"/>','moveQuestionDown');">
		                            <img src="<c:out value="${tool}"/>images/down.gif" border="0">
						</a> 
					</c:if> 							
	
			 		<c:if test="${queIndex == totalQuestionCount}"> 		
						<a title="<fmt:message key='label.tip.moveQuestionUp'/>" href="javascript:;" onclick="javascript:submitModifyMonitoringQuestion('<c:out value="${queIndex}"/>','moveQuestionUp');">
		                            <img src="<c:out value="${tool}"/>images/up.gif" border="0">
						</a> 
					</c:if> 							
					
					<c:if test="${(queIndex != 1)  && (queIndex != totalQuestionCount)}"> 		
						<a title="<fmt:message key='label.tip.moveQuestionDown'/>" href="javascript:;" onclick="javascript:submitModifyMonitoringQuestion('<c:out value="${queIndex}"/>','moveQuestionDown');">
		                            <img src="<c:out value="${tool}"/>images/down.gif" border="0">
						</a> 
	
						<a title="<fmt:message key='label.tip.moveQuestionUp'/>" href="javascript:;" onclick="javascript:submitModifyMonitoringQuestion('<c:out value="${queIndex}"/>','moveQuestionUp');">
		                            <img src="<c:out value="${tool}"/>images/up.gif" border="0">
						</a> 
					</c:if> 							
				
				</c:if> 			
			</td>

		
		

		<td width="10%" class="align-right">
				<a title="<fmt:message key='label.tip.editQuestion'/>" href="javascript:;" onclick="javascript:showMessage('<html:rewrite page="/monitoring.do?dispatch=newEditableQuestionBox&questionIndex=${queIndex}&contentFolderID=${mcGeneralAuthoringDTO.contentFolderID}&httpSessionID=${mcGeneralAuthoringDTO.httpSessionID}&toolContentID=${mcGeneralAuthoringDTO.toolContentID}&activeModule=${mcGeneralAuthoringDTO.activeModule}&defaultContentIdStr=${mcGeneralAuthoringDTO.defaultContentIdStr}&sln=${mcGeneralAuthoringDTO.sln}&questionsSequenced=${mcGeneralAuthoringDTO.questionsSequenced}&retries=${mcGeneralAuthoringDTO.retries}&reflect=${mcGeneralAuthoringDTO.reflect}&reflectionSubject=${mcGeneralAuthoringDTO.reflectionSubject}"/>');">
	                    <img src="<c:out value="${tool}"/>images/edit.gif" border="0">
				</a> 
		</td>

		<td width="10%" class="align-right">
				<a title="<fmt:message key='label.tip.deleteQuestion'/>" href="javascript:;" onclick="removeMonitoringQuestion(${queIndex});">
	                    <img src="<c:out value="${tool}"/>images/delete.gif" border="0">
				</a> 				
		</td>
	</tr>
</c:forEach>

</table>
</div>
<%-- This script will works when a new resoruce item submit in order to refresh "Resource List" panel. --%>
<script lang="javascript">
	var win = null;
	if (window.parent && window.parent.hideMessage) {
		win = window.parent;
	} else if (window.top && window.top.hideMessage) {
		win = window.top;
	}
	if (win) {
		win.hideMessage();
		var obj = win.document.getElementById('resourceListArea');
		obj.innerHTML= document.getElementById("itemList").innerHTML;
	}
</script>