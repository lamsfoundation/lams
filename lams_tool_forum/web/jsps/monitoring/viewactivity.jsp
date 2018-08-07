<%@ include file="/common/taglibs.jsp"%>

 <c:set var="errorKey" value="GLOBAL" />
 <c:if test="${not empty errorMap and not empty errorMap[errorKey]}">
	 <lams:Alert id="error" type="danger" close="false">
		 <c:forEach var="error" items="${errorMap[errorKey]}">
			  <c:out value="${error}" />
		 </c:forEach>
	 </lams:Alert>
</c:if>
					        
<table cellpadding="0">
	<!-- Title Row -->
	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="label.authoring.basic.title" />
			:
		</td>
		<td>
			<c:out value="${title}" escapeXml="true" />
		</td>
	</tr>
	<!-- Instructions Row -->
	<tr>
		<td class="field-name" width="30%">
			<fmt:message key="label.authoring.basic.instruction" />
			:
		</td>
		<td>
			<c:out value="${instruction}" escapeXml="false" />
		</td>
	</tr>

	<tr>
		<td colspan="2">
			<a href="/learning/viewTopic.do" class="button">
				<fmt:message key="label.monitoring.edit.activity.edit" />
			</a>
		</td>
	</tr>
</table>
