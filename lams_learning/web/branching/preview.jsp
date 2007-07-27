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

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>		
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

	<script language="JavaScript" type="text/JavaScript"><!--
		function selectActivity(branchId) {
			// find the activity in the form
			var form = document.forms[0];
			// use form.elements because we are guaranteed an array is returned
			var elements = form.elements;
			for (var i = 0; i < elements.length; i++) {
				// now check if we have the right element
				if (elements[i].name == "branchID") {
					var thisActivityBtn = elements[i];
					var thisBranchId = thisActivityBtn.value;
					if (branchId == thisBranchId) {
						thisActivityBtn.checked = true;
						break;
					}
				}
			}
		}
		function submitChoose() {
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
			}
			else {
				form.submit();
			}
		}
		function submitFinish() {
			var form = document.forms[1];
			form.submit();
		}
		//-->
	</script>
	
	<html:form action="/branching.do?method=forceBranching&type=${BranchingForm.map.type}&activityID=${BranchingForm.map.activityID}&progressID=${BranchingForm.map.progressID}" target="_self">

		<div id="content">
		
			<h1><c:out value="${BranchingForm.map.title}"/></h1>
			
			<p>&nbsp;</p>

			<p><em><fmt:message key="label.branching.preview.message"/></em></p>

			<table class="alternative-color" cellspacing="0">
				<c:forEach items="${BranchingForm.map.activityURLs}" var="activityURL" varStatus="loop">
					<tr onclick="selectActivity(<c:out value="${activityURL.activityId}" />)">
						<td width="2%">
							<c:choose>
								<c:when test="${activityURL.complete}">
									<%--html:img page="/images/tick.gif" /--%>
								</c:when>
								<c:when test="${activityURL.defaultURL}">
									<input type="radio" name="branchID" value="<c:out value="${activityURL.activityId}"/>" CHECKED />
								</c:when>
								<c:otherwise>
									<input type="radio" name="branchID" value="<c:out value="${activityURL.activityId}"/>" />
								</c:otherwise>
							</c:choose>
						</td>
						<td >
							<c:choose>
							<c:when test="${activityURL.defaultURL}">
								<strong><c:out value="${activityURL.title}"/></strong>
							</c:when>
							<c:otherwise>
								<c:out value="${activityURL.title}"/>
							</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:forEach> 
			</table>

			<table><tr><td>
			<c:if test="${BranchingForm.map.showNextButton}">
			<div class="left-buttons">
				<a href="#" class="button" id="chooseBtn" onClick="submitChoose()"><fmt:message key="label.activity.options.choose" /></a>
			</div>
			</c:if>
			<c:if test="${BranchingForm.map.showFinishButton}">
				<div class="right-buttons">
					<a href="#" class="button" id="finishBtn" onClick="submitFinish()"><fmt:message key="label.finish.button" /></a>
				</div>
			</c:if>
			</td></tr></table>
		</html:form>

		<html:form action="/CompleteActivity" method="POST">
			<input type="hidden" name="activityID" value="<c:out value='${BranchingForm.map.activityID}' />" />
			<input type="hidden" name="lessonID" value="<c:out value='${BranchingForm.map.lessonID}' />" /> 
			<input type="hidden" name="progressID" value="<c:out value='${BranchingForm.map.progressID}' />" /> 
			
		</html:form>

		</div>  <!--closes content-->

		<div id="footer">
		</div><!--closes footer-->

