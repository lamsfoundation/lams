<!DOCTYPE html>
        

<%@include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants"%>

<lams:html>
<lams:head>
	<title><fmt:message key="activity.title" /></title>
	<lams:css/>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/common.js"></script>
	<script type="text/javascript">
		function updateMark(userUid){
			window.opener.editMark(userUid);
			window.close();
		}
		function closeWindow() {
			window.close();
		}  				
	</script>
</lams:head>
<body class="stripes">

		<div id="content">
		<h1>
			<fmt:message key="label.monitoring.vieawallmarks.heading.marking"/>
		</h1>

			<table cellpadding="0">
				<c:forEach var="user" items="${userList}">
						
						
					<tr>
						<td class="field-name">
							<fmt:message key="label.monitoring.vieawallmarks.learner" />
						</td>
						<td>
							<c:out value="${user.loginName}" />
						</td>
					</tr>	
					
					<tr>
						<td class="field-name">
							<fmt:message key="label.monitoring.vieawallmarks.spreadsheet.submitted" />
						</td>
						<td>
							<c:choose>
								<c:when test="${user.userModifiedSpreadsheet != null}">
									<fmt:message key="label.monitoring.vieawallmarks.true" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.monitoring.vieawallmarks.false" />
								</c:otherwise>
							</c:choose>
						</td>
					</tr>					
					
					<tr>
						<td class="field-name">
							<fmt:message key="label.monitoring.vieawallmarks.marks" />
						</td>
						<td>
							<c:choose>
								<c:when test="${(user.userModifiedSpreadsheet != null) && (user.userModifiedSpreadsheet.mark != null)}">
									<fmt:formatNumber type="number" maxFractionDigits="<%= SpreadsheetConstants.MARK_NUM_DEC_PLACES %>" value="${user.userModifiedSpreadsheet.mark.marks}"/>
								</c:when>
								<c:otherwise>
									<fmt:message key="label.learning.not.available" />
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${user.userModifiedSpreadsheet != null}">
									<html:link href="javascript:updateMark(${user.uid});" 
										property="submit" styleClass="button">
										<fmt:message key="label.monitoring.vieawallmarks.update.marks" />
									</html:link>
								</c:when>
								<c:otherwise>
										<fmt:message key="label.monitoring.vieawallmarks.update.marks" />
								</c:otherwise>
							</c:choose>							

						</td>
					</tr>
					
					<tr>
						<td class="field-name">
							<fmt:message key="label.monitoring.vieawallmarks.comments" />
						</td>
						<td>
							<c:choose>
								<c:when test="${(user.userModifiedSpreadsheet != null) && (user.userModifiedSpreadsheet.mark != null)}">
									<c:out value="${user.userModifiedSpreadsheet.mark.comments}" escapeXml="false" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.learning.not.available" />
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					
					
					<tr>
						<td colspan="2">
							<hr size="1" style="width:500px"/>
						</td>
					</tr>
					<br><br>
						
				</c:forEach>
				<tr>
					<td colspan="2">
						<html:link href="javascript:closeWindow();" property="submit" styleClass="button">
							<fmt:message key="button.close" />
						</html:link>
					</td>
				</tr>
			</table>
		</div>
		<div id="footer"></div>

</body>
</lams:html>