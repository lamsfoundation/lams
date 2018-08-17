<%@ page import="org.lamsfoundation.lams.config.ConfigurationItem" %>
<%@ include file="/taglibs.jsp"%>

<c:forEach items="${config}" var="group">
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title"><fmt:message key="${group.key}"/></div>
		</div>
						
		<table class="table table-striped table-condensed" >
			<c:forEach items="${group}" path="value" var="row">
				<tr>
					<td>
						<fmt:message key="${row.descriptionKey}"/>
						<c:if test="${row.required}">&nbsp;&nbsp;*</c:if>
					</td>
					<td>
						<form:hidden path="key" name="key" value="${row.key}"/>
						<c:set var="BOOLEAN"><%= ConfigurationItem.BOOLEAN_FORMAT %></c:set>
						<c:choose>
						<c:when test="${row.format==BOOLEAN}">
							<form:select id="${row.key}" name="row" path="value" cssClass="form-control form-control-sm">
							<form:option value="true">true</form:option>
							<form:option value="false">false&nbsp;&nbsp;</form:option>
							</form:select>
						</c:when>
						<c:otherwise>
							<form:input id="${row.key}" path="value" name="row" value="${row.value}" size="50" maxlength="255" cssClass="form-control"/>
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</c:forEach>



