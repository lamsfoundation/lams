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

<div id="page">	
	<div id="content">

		<html:form action="/grouping.do?method=completeActivity&userId=${user.userId}&lessonId=${lesson.lessonId}&activityID=${activityID}" target="_self">

		<h1><fmt:message key="label.view.groups.title"/></h1>
		<table class="alternative-color">
			<logic:iterate id="group" name="groups"> 
			<tr>
				<td width="25%">
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
			<p align="right">
				<html:submit styleClass="button" onmouseover="pviiClassNew(this,'buttonover')" onmouseout="pviiClassNew(this,'button')"><fmt:message key="label.finish.button"/></html:submit>
			</p>
		</c:if>

	</html:form>

	</div>
</div>