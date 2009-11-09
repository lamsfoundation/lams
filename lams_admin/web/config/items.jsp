<%@ page import="org.lamsfoundation.lams.config.ConfigurationItem" %>
<%@ include file="/taglibs.jsp"%>

	<logic:iterate name="config" id="group">
		<h2 align="center"><fmt:message key="${group.key}"/></h2>
		<table class="alternative-color" width=100% cellspacing="0">
		<logic:iterate name="group" property="value" id="row">
			<tr>
				<td align="right" width="50%">
					<fmt:message key="${row.descriptionKey}"/>
					<c:if test="${row.required}">&nbsp;&nbsp;*</c:if>
				</td>
				<td>
					<html:hidden property="key" name="key" value="${row.key}"/>
					<c:set var="BOOLEAN"><%= ConfigurationItem.BOOLEAN_FORMAT %></c:set>
					<c:choose>
					<c:when test="${row.format==BOOLEAN}">
						<html:select name="row" property="value">
						<html:option value="true">true</html:option>
						<html:option value="false">false&nbsp;&nbsp;</html:option>
						</html:select>
					</c:when>
					<c:otherwise>
						<html:text property="value" name="row" value="${row.value}" size="50" maxlength="255"/>
					</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</logic:iterate>
		</table>
	</logic:iterate>