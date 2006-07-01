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
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>		
<%@ taglib uri="tags-fmt" prefix="fmt" %>

	<html:form action="/grouping.do?method=completeActivity&userId=${user.userId}&lessonId=${lesson.lessonId}&activityID=${activityID}" target="_self">

	<h1 class="no-tabs-below"><c:out value="${title}"/></h1>
	<div id="header-no-tabs-learner">
	</div><!--closes header-->

	<div id="content-learner">

		<table class="alternative-color">
			<tr>
				<th width="25%" class="first">
					<fmt:message key="label.view.groups.title"/>
				</th>
				<th>
				</th>
			</tr>
			<logic:iterate id="group" name="groups"> 
			<tr>
				<td width="25%" class="first">
					<c:out value="${group.groupName}"/>
				</td>
				<td>
					<c:forEach items="${group.users}" var="user">
						<c:out value="${user.firstName}"/> <c:out value="${user.lastName}"/><BR>
					</c:forEach>
				</td>
			</tr>
			</logic:iterate>
		</table>

	<c:if test="${finishedButton}">
			<table><tr><td><div id="right" class="right-buttons">
					<html:submit styleClass="button"><fmt:message key="label.finish.button"/></html:submit>
			</div></td></tr></table>
	</c:if>

	</div>  <!--closes content-->

	<div id="footer-learner">
	</div><!--closes footer-->

	</html:form>

</div>