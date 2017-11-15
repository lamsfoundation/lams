<%@ page import="org.lamsfoundation.lams.config.ConfigurationItem" %>
<%@ include file="/taglibs.jsp"%>

<logic:iterate name="config" id="group">
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title"><fmt:message key="${group.key}"/></div>
		</div>
		
		<table class="table table-striped table-condensed" >
			<logic:iterate name="group" property="value" id="row">
				<tr>
					<td>
						<fmt:message key="${row.descriptionKey}"/>
						<c:if test="${row.required}">&nbsp;&nbsp;*</c:if>
					</td>
					<td>
						<html:hidden property="key" name="key" value="${row.key}"/>
						<c:set var="BOOLEAN"><%= ConfigurationItem.BOOLEAN_FORMAT %></c:set>
						<c:choose>
						<c:when test="${row.format==BOOLEAN}">
							<html:select styleId="${row.key}" name="row" property="value" styleClass="form-control form-control-sm">
							<html:option value="true">true</html:option>
							<html:option value="false">false&nbsp;&nbsp;</html:option>
							</html:select>
						</c:when>
						<c:otherwise>
							<html:text styleId="${row.key}" property="value" name="row" value="${row.value}" size="50" maxlength="255" styleClass="form-control"/>
						</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</div>
</logic:iterate>
