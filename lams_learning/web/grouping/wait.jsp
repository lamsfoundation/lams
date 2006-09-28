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

	<c:set var="formAction">/grouping.do?method=performGrouping&activityID=${activity.activityId}</c:set>
	<c:if test="${GroupingForm.map.previewLesson == true}">
		<c:set var="formAction"><c:out value="${formAction}"/>&force=true</c:set>
	</c:if>

	<html:form action="${formAction}" target="_self">


		<div id="content">
		
			<h1><c:out value="${GroupingForm.map.title}"/></h1>
			
			<p>&nbsp;</p>

			<p><fmt:message key="label.view.view.groups.wait.message"/></p>

			<c:if test="${GroupingForm.map.previewLesson == true}">
				<c:set var="formAction"><c:out value="${formAction}"/>&force=true</c:set>
				<p><em><fmt:message key="label.grouping.preview.message"/></em></p>
			</c:if>

			<table><tr><td><div class="right-buttons">
					<html:submit styleClass="button"><fmt:message key="label.next.button"/></html:submit>
			</div></td></tr></table>

		</div>  <!--closes content-->

		<div id="footer">
		</div><!--closes footer-->

	</html:form>
