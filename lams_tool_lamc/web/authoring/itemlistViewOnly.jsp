<%@ include file="/common/taglibs.jsp" %>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request"/>
<div id="itemList">
<h2><fmt:message key="label.questions" />
<img src="${ctxPath}/includes/images/indicator.gif" style="display:none" id="resourceListArea_Busy" /></h2>

<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
<table id="itemTable" class="align-left" style="width:650px" >
    <c:set var="queIndex" scope="request" value="0"/>
    
    
    <c:forEach items="${listQuestionContentDTO}" var="currentDTO" varStatus="status">
	<c:set var="queIndex" scope="request" value="${queIndex +1}"/>
	<c:set var="question" scope="request" value="${currentDTO.question}"/>
	<c:set var="feedback" scope="request" value="${currentDTO.feedback}"/>
	<c:set var="displayOrder" scope="request" value="${currentDTO.displayOrder}"/>	

	<tr>
		<td width="10%" class="align-right">
				<strong> <fmt:message key="label.question" />: </strong>
		</td>

		<td width="60%" class="align-left">
			<div style="overflow: auto;">				
				<c:out value="${question}" escapeXml="false"/> 
			</div>											
		</td>		


		<td width="10%" class="align-right">
		</td>
       	
		<td width="10%" class="align-right">

		</td>

		<td width="10%" class="align-right">

		</td>
	</tr>
</c:forEach>

</table>
</div>
