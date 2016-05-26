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

<%@ include file="/common/taglibs.jsp"%>

<script language="JavaScript" type="text/javascript"><!--
	function validate() {
		var validated = false;
		
		var form = document.forms[0];
		var elements = form.elements;
		for (var i = 0; i < elements.length; i++) {
			if (elements[i].name == "branchID") {
				if (elements[i].checked) {
					validated = true;
					break;
				}
			}
		}
		if (!validated) {
			alert("<fmt:message key="message.activity.options.noActivitySelected" />");
			return false;
		}
		else {
			return true;
		}
	}
        function submitForm(methodName){
                var f = document.getElementById('messageForm');
                f.submit();
        }

	//-->
</script>

<c:set var="title"><c:out value="${BranchingForm.map.title}" /></c:set>
<lams:Page type="learner" title="${title}">

	<p>
		<em><fmt:message key="label.branching.preview.message" /> </em>
	</p>

	<div class="group-box">

		<html:form
			action="/branching.do?method=forceBranching&amp;type=${BranchingForm.map.type}&amp;activityID=${BranchingForm.map.activityID}&amp;progressID=${BranchingForm.map.progressID}"
			target="_self" onsubmit="return validate();">

			<table class="table table-condensed table-striped">
				<c:forEach items="${BranchingForm.map.activityURLs}"
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

			<c:if test="${BranchingForm.map.showNextButton}">
				<html:submit styleClass="btn btn-default pull-left">
					<fmt:message key="label.activity.options.choose" />
				</html:submit>
			</c:if>

		</html:form>
	</div>

	<c:if test="${BranchingForm.map.showFinishButton}">
		<html:form action="/CompleteActivity" method="post" styleId="messageForm">
			<input type="hidden" name="activityID"
				value="<c:out value='${BranchingForm.map.activityID}' />">
			<input type="hidden" name="lessonID"
				value="<c:out value='${BranchingForm.map.lessonID}' />">
			<input type="hidden" name="progressID"
				value="<c:out value='${BranchingForm.map.progressID}' />">

	        <html:link href="javascript:;" styleClass="btn btn-default pull-right" styleId="finishButton" onclick="submitForm('finish')">
				<span class="nextActivity"><fmt:message key="label.finish.button" /></span>
			</html:link>

		</html:form>
	</c:if>

<!--closes content-->

<div id="footer"></div>

</lams:Page>
