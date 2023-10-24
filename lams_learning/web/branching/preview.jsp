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
		function validate() {
			var validated = false, form = document.forms[0], elements = form.elements;
			for (var i = 0; i < elements.length; i++) {
				if (elements[i].name == "branchID") {
					if (elements[i].checked) {
						validated = true;
						break;
					}
				}
			}

			if (validated) {
				return true;
			}

			alert("<spring:escapeBody javaScriptEscape='true'><fmt:message key='message.activity.options.noActivitySelected' /></spring:escapeBody>");
			return false;
		}
	</script>
		
	<div id="container-main">
		<p>
			<em><fmt:message key="label.branching.preview.message" /> </em>
		</p>
		
		<form:form action="forceBranching.do?${formParameters}" modelAttribute="branchingForm" target="_self" onsubmit="return validate();">
		<fieldset>
			<legend class="visually-hidden">
				<fmt:message key="message.activity.options.noActivitySelected" />
			</legend>
		
			<div class="ltable table-striped no-header">
				<c:forEach items="${branchingForm.activityURLs}" var="activityURL" varStatus="loop">
					<div class="row">
						<div class="col">
							<div class="form-check">
								<c:choose>
									<c:when test="${activityURL.complete}">
										<i class="fa fa-check"></i>
									</c:when>
									<c:when test="${activityURL.defaultURL}">
										<input type="radio" class="form-check-input" name="branchID"
											id="activityID-${activityURL.activityId}"
											value="<c:out value="${activityURL.activityId}"/>"
											checked="checked">
									</c:when>
									<c:otherwise>
										<input type="radio" class="form-check-input" name="branchID"
											id="activityID-${activityURL.activityId}"
											value="<c:out value="${activityURL.activityId}"/>">
									</c:otherwise>
								</c:choose>

								<c:choose>
									<c:when test="${activityURL.complete}">
										<!-- No Label tags -->
										<c:choose>
											<c:when test="${activityURL.defaultURL}">
												<strong><c:out value="${activityURL.title}" /> </strong>
											</c:when>
											<c:otherwise>
												<c:out value="${activityURL.title}" />
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<!-- With Label tags -->
										<label class="form-check-label" for="activityID-${activityURL.activityId}">
											<c:choose>
												<c:when test="${activityURL.defaultURL}">
													<strong><c:out value="${activityURL.title}" /> </strong>
												</c:when>
												<c:otherwise>
													<c:out value="${activityURL.title}" />
												</c:otherwise>
											</c:choose>
										</label>
									</c:otherwise>
		
								</c:choose>
		
							</div>
						</div>
					</div>
				</c:forEach>
			</div>
			
			<button type="submit" class="btn btn-secondary float-end mb-2">
				<fmt:message key="label.activity.options.choose" />
			</button>
		</fieldset>
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
