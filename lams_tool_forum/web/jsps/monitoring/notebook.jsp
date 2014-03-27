<%@ include file="/common/taglibs.jsp"%>

<p>${userDTO.fullName}</p>

<table class="alternative-color" cellspacing="0">
	<tr>
		<th class="first">
			<lams:out value="${userDTO.reflectInstrctions}" escapeHtml="true"/>
		</th>
	</tr>
	<tr>
		<td>
			<c:choose>
				<c:when test="${userDTO.finishReflection}">
					<lams:out value="${userDTO.reflect}" escapeHtml="true"/>
				</c:when>
				<c:otherwise>
					<fmt:message key="message.not.avaliable" />
				</c:otherwise>
			</c:choose>
		</td>
	</tr>
</table>
<table cellpadding="0">
	<tr>
		<td>
			<a href="javascript:window.close();" class="button"><fmt:message key="button.close"/></a>
		</td>
	</tr>
</table>


