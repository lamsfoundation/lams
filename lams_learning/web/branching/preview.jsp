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
<c:set var="WebAppURL"><lams:WebAppURL/></c:set>
<c:set var="formParameters" value="type=${branchingForm.type}&activityID=${branchingForm.activityID}&progressID=${branchingForm.progressID}"/>

<lams:PageLearner title="${branchingForm.title}" toolSessionID="" lessonID="${lessonID}"
		refresh="60;URL=${WebAppURL}/branching/performBranching.do?${formParameters}${(branchingForm.previewLesson == true) ? '&force=true' : ''}">

	<script type="text/javascript">
		function submitForm(activityId) {
			$("#branch-id").val(activityId);
			document.getElementById('branchingForm').submit();
		}
	</script>
		
	<div id="container-main">
		<p>
			<em><fmt:message key="label.branching.preview.message" /> </em>
		</p>
		
		<form:form action="forceBranching.do?${formParameters}" modelAttribute="branchingForm" target="_self">
			<input type="hidden" name="branchID" id="branch-id">
		
			<div class="ltable no-header table-hover">
				<c:forEach items="${branchingForm.activityURLs}" var="activityURL" varStatus="loop">
					<div class="row align-items-center">
						<div class="col-sm">
							<c:choose>
								<c:when test="${activityURL.complete}">
									<i class="fa fa-check"></i>
								</c:when>
								<c:otherwise>
									&nbsp;&nbsp;&nbsp;
								</c:otherwise>
							</c:choose>

							<c:choose>
								<c:when test="${activityURL.defaultURL}">
									<strong><c:out value="${activityURL.title}" /> </strong>
								</c:when>
								<c:otherwise>
									<c:out value="${activityURL.title}" />
								</c:otherwise>
							</c:choose>
						</div>
						
						<div class="col-sm-4">
							<button type="button" class="btn btn-secondary float-end"
									onclick="submitForm(${activityURL.activityId})">
								<i class="fa-regular fa-circle-check me-1"></i>
								<fmt:message key="label.activity.options.choose" />
							</button>	
						</div>
					</div>
				</c:forEach>
			</div>
		</form:form>

		<div class="activity-bottom-buttons">
			<form action="/lams/learning/CompleteActivity.do" method="post">
				<input type="hidden" name="activityID" value="${branchingForm.activityID}">
				<input type="hidden" name="progressID" value="${branchingForm.progressID}">
			
				<button type="submit" class="btn btn-primary float-start na">
					<fmt:message key="label.finish.button" />
				</button>
			</form>
		</div>
		
	</div>
</lams:PageLearner>
