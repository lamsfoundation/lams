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
<c:set var="formAction">performBranching.do?type=${branchingForm.type}&activityID=${branchingForm.activityID}&progressID=${branchingForm.progressID}</c:set>

<lams:PageLearner title="${branchingForm.title}" toolSessionID="" lessonID="${lessonID}"
		refresh="60;URL=${WebAppURL}/branching/${formAction}${(branchingForm.previewLesson == true) ? '&force=true' : ''}">

	<div id="container-main">
		<form:form action="${formAction}" modelAttribute="branchingForm" target="_self">
			<lams:Alert5 type="info">
				<fmt:message key="label.branching.wait.message"/>
				<div class="mt-2"><fmt:message key="label.branching.refresh.message"/></div>
			</lams:Alert5>
	
			<div class="activity-bottom-buttons">
				<button type="submit" class="btn btn-primary na">
					<fmt:message key="label.next.button"/>
				</button>
			</div>
		</form:form>
	</div>
</lams:PageLearner>
