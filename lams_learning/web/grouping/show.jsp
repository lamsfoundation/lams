<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="userId" value="${user.userID}" />

<lams:PageLearner title="${title}" toolSessionID="" lessonID="${lessonID}">

	<style type="text/css">
		.user-container {
			padding: 2px;
		}
		
		.you {
			font-weight: bolder;
		}
	</style>

	<script type="text/javascript">
		checkNextGateActivity('finishButton', '', ${activityID}, submitForm);
		
		function submitForm() {
			var f = document.getElementById('messageForm');
			f.submit();
		}
	</script>

	<div id="container-main">	
		<form:form action="/lams/learning/grouping/completeActivity.do?userId=${userId}&lessonId=${lessonID}&activityID=${activityID}"
			target="_self" modelAttribute="messageForm" id="messageForm">
	
			<div class="card lcard">
				<div class="card-header">
					<i class="fa fa-sm fa-users"></i>&nbsp;
					<fmt:message key="label.view.groups.title" />
				</div>
				
				<div>
					<div class="ltable table-striped mb-0">
						<c:forEach var="group" items="${groups}">
							<div class="row">
								<div class="col-2">
									<strong><c:out value="${group.groupName}" /></strong>
								</div>
								<div class="col-10">
									<c:choose>
										<c:when test="${RestrictedGroupUserNames && !group.userBelongsToGroup}">
											<fmt:message key="label.learners">
												<fmt:param>
													${fn:length(group.userList)}
												</fmt:param>
											</fmt:message>
										</c:when>
											
										<c:otherwise>
											<c:forEach items="${group.userList}" var="groupUser">
												<div name="u-${groupUser.userID}" class="user-container ${groupUser.userID ==  userId ? 'alert alert-info you mb-0' : ''}">
													<lams:Portrait userId="${groupUser.userID}"/>&nbsp;
													<c:out value="${groupUser.firstName}" />&nbsp;<c:out value="${groupUser.lastName}" />
												</div>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
	
			<c:if test="${finishedButton}">
				<div class="activity-bottom-buttons">
					<button type="submit" class="btn btn-primary na" id="finishButton"> 
						<c:choose>
							<c:when test="${activityPosition.last}">
								<fmt:message key="label.submit.button" />
							</c:when>
							<c:otherwise>
								<fmt:message key="label.finish.button" />
							</c:otherwise>
						</c:choose>
					</button>
				</div>
			</c:if>
		</form:form>
	</div>
</lams:PageLearner>
