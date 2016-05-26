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

	<c:set var="formAction">/branching.do?method=performBranching&type=${BranchingForm.map.type}&activityID=${BranchingForm.map.activityID}&progressID=${BranchingForm.map.progressID}</c:set>

	<html:form action="${formAction}" target="_self">

		<c:set var="title"><c:out value="${BranchingForm.map.title}" /></c:set>
		<lams:Page type="learner" title="${title}">

			<p><fmt:message key="label.branching.wait.message"/></p>
			<p><fmt:message key="label.branching.refresh.message"/></p>

			<html:submit styleClass="btn btn-default pull-right"><fmt:message key="label.next.button"/></html:submit>

			<div id="footer"></div>
		</lams:Page>

	</html:form>
