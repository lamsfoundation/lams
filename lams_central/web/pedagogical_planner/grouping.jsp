<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%-- Pedagogical Planner form for grouping. --%>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>

<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<lams:html>
<lams:head>
	<lams:css style="core" />
	<style type="text/css">
		table td {
			padding: 5px;
		}
	</style>
</lams:head>
<body>
<logic:messagesPresent message="true"> 
	<p class="warning">
		<html:messages message="true" id="errMsg" >
			<bean:write name="errMsg"/><br>
		</html:messages>
	</p>
</logic:messagesPresent>

<html:form action="/pedagogicalPlanner/grouping.do?method=saveOrUpdateGroupingForm" styleId="pedagogicalPlannerForm" method="post">
	<html:hidden property="toolContentID" />
	<html:hidden property="valid" styleId="valid" />
	<html:hidden property="callID" styleId="callID" />
	<html:hidden property="activityOrderNumber" styleId="activityOrderNumber" />
	
	<html:hidden property="groupingTypeId" />
	<c:set var="formBean" value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
	<table style="width: 570px; margin-top: 30px" cellpadding="0" cellspacing="0">
		<tr>
			<td style="width: 30%">
				<h2><fmt:message key="label.planner.grouping.type" /></h2>
			</td>
			<td>
				<c:choose>
					<c:when test="${formBean.groupingTypeId eq 1}">
						<fmt:message key="label.planner.grouping.type.random" />
					</c:when>
					<c:when test="${formBean.groupingTypeId eq 2}">
						<fmt:message key="label.planner.grouping.type.chosen" />
					</c:when>
					<c:when test="${formBean.groupingTypeId eq 4}">
						<fmt:message key="label.planner.grouping.type.learner.choice" />
					</c:when>
				</c:choose>
			</td>
		</tr>
		<c:choose>
			<c:when test="${not empty formBean.learnersPerGroup}">
				<tr>
					<td>
						<h2><fmt:message key="label.planner.grouping.number.of.learners" /></h2>
					</td>
					<td>
						<html:text property="learnersPerGroup" size="10" />
					</td>
				</tr>
			</c:when>
			<c:otherwise>
				<tr>
					<td>
						<h2><fmt:message key="label.planner.grouping.number.of.groups" /></h2>
					</td>
					<td>
						<html:text property="numberOfGroups" size="10" />
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
		<c:if test="${(formBean.groupingTypeId eq 4) and (empty formBean.learnersPerGroup)}">
			<tr>
				<td>
					<h2>
						<fmt:message key="label.planner.grouping.equal.group.size" />
					</h2>
				</td>
				<td>
					<html:checkbox property="equalGroupSizes"></html:checkbox>
				</td>
			</tr>
		</c:if>
	</table>
</html:form>
</body>
</lams:html>