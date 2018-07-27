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

<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<lams:Page type="learner" title="${GroupingForm.map.title}">
	<html:form action="/grouping.do" target="_self">
		<input type="hidden" name="method" value="performGrouping" />
		<input type="hidden" name="activityID" value="${GroupingForm.map.activityID}" />
		<input type="hidden" name="force" value="${GroupingForm.map.previewLesson}" />
		

		<lams:Alert id="waitingGroups" close="false" type="info">
			<fmt:message key="label.view.view.groups.wait.message" />
		</lams:Alert>

		<c:if test="${GroupingForm.map.previewLesson == true}">
			<div class="voffset10">
				<em><fmt:message key="label.grouping.preview.message" /></em>
			</div>
		</c:if>

		<div class="right-buttons">
			<html:submit styleClass="btn btn-primary pull-right">
				<fmt:message key="label.next.button" />
			</html:submit>
		</div>

	</html:form>
</lams:Page>