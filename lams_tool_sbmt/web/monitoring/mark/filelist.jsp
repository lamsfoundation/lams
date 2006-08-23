<%@include file="fileinfo.jsp"%>
<tr>
	<td class="field-name">
		<fmt:message key="label.learner.marks" />
		:
	</td>
	<td>
		<c:choose>
			<c:when test="${empty fileInfo.marks}">
				<fmt:message key="label.learner.notAvailable" />
			</c:when>
			<c:otherwise>
				<c:out value="${fileInfo.marks}" escapeXml="false" />
			</c:otherwise>
		</c:choose>
	</td>
</tr>
<tr>
	<td class="field-name">
		<fmt:message key="label.learner.comments" />
		:
	</td>
	<td>
		<c:choose>
			<c:when test="${empty fileInfo.comments}">
				<fmt:message key="label.learner.notAvailable" />
			</c:when>
			<c:otherwise>
				<c:out value="${fileInfo.comments}" escapeXml="false" />
			</c:otherwise>
		</c:choose>
	</td>
</tr>

<tr>
	<td colspan="2">
		<html:link href="javascript:updateMark(${fileInfo.submissionID},${fileInfo.reportID},${toolSessionID},${fileInfo.userDTO.userID});" 
			property="submit" styleClass="button">
			<bean:message key="label.monitoring.updateMarks.button" />
		</html:link>
		<hr size="1" style="width:500px"/>
	</td>
</tr>
