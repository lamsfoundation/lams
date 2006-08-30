<%@ include file="/common/taglibs.jsp"%>

<table>
	<tr>
		<td>
			<h1>
				${userDTO.fullName}
			</h1>
		</td>
	</tr>
	<tr>
		<td>
			<h1>
				${userDTO.reflectInstrctions}
			</h1>
		</td>
	</tr>
	<tr>
		<td>
			<c:choose>
				<c:when test="${userDTO.finishReflection}">
					<c:out value="${userDTO.reflect}" escapeXml="false"/>
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


