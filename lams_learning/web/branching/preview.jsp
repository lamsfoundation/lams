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

<lams:html>

	<lams:head>
		<title><fmt:message key="label.branching.title"/></title>
		<lams:css/>
		<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
		<c:set var="formAction">/branching/performBranching.do?type=${branchingForm.type}&activityID=${branchingForm.activityID}&progressID=${branchingForm.progressID}</c:set>
		<c:if test="${branchingForm.previewLesson == true}">
			<c:set var="formAction"><c:out value="${formAction}"/>&amp;force=true</c:set>
		</c:if>
		<META HTTP-EQUIV="Refresh" CONTENT="60;URL=<lams:WebAppURL/>${formAction}">
	  </lams:head>

	<body class="stripes">
		<script type="text/javascript">
			function validate() {
				var validated = false,
					form = document.forms[0],
					elements = form.elements;
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
		
		<c:set var="title"><c:out value="${branchingForm.title}" /></c:set>
		<lams:Page type="learner" title="${title}">
		
			<p>
				<em><fmt:message key="label.branching.preview.message" /> </em>
			</p>
		
			<div class="group-box">
		
				<form:form
					action="forceBranching.do?type=${branchingForm.type}&activityID=${branchingForm.activityID}&progressID=${branchingForm.progressID}"
					modelAttribute="branchingForm" target="_self" onsubmit="return validate();">
		
					<table class="table table-condensed table-striped">
						<c:forEach items="${branchingForm.activityURLs}"
							var="activityURL" varStatus="loop">
							<tr>
								<td width="2%">
									<c:choose>
										<c:when test="${activityURL.complete}">
											<i class="fa fa-check"></i>
										</c:when>
										<c:when test="${activityURL.defaultURL}">
											<input type="radio" name="branchID"
												id="activityID-${activityURL.activityId}"
												value="<c:out value="${activityURL.activityId}"/>"
												checked="checked">
										</c:when>
										<c:otherwise>
											<input type="radio" name="branchID"
												id="activityID-${activityURL.activityId}"
												value="<c:out value="${activityURL.activityId}"/>">
										</c:otherwise>
									</c:choose>
								</td>
								<td>
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
											<label for="activityID-${activityURL.activityId}">
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
		
								</td>
							</tr>
						</c:forEach>
					</table>
			
					<button class="btn btn-primary pull-right">
						<fmt:message key="label.activity.options.choose" />
					</button>
		
				</form:form>
				
									
				<form action="/lams/learning/CompleteActivity.do" method="post">
					<input type="hidden" name="activityID" value="${branchingForm.activityID}">
					<input type="hidden" name="progressID" value="${branchingForm.progressID}">
			
					<button class="btn btn-primary pull-left na" onclick="finishActivity()">
						<fmt:message key="label.finish.button" />
					</button>
			
				</form>
			</div>
			

		<!--closes content-->
		
		<div id="footer"></div>
		
		</lams:Page>

	</body>

</lams:html>
