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
<%@ include file="/taglibs.jsp"%>

<lams:html>
    <lams:head>
		<lams:css/>
    </lams:head>
    
    <body class="stripes">
		<lams:Page type="monitor" title="${activityTitle}">

		<c:if test="${!hasSequenceActivity}">
		<table class="table table-bordered table-striped table-condensed">
		<c:forEach items="${subActivities}" var="subActivity">
			<c:set var="id" value="${subActivity.activityID}"/>
			<tr>
				<c:choose>
					<c:when test="${statusMap[id] == 1}">
						<td><a href="<lams:LAMSURL />/<c:out value="${urlMap[id]}" />"><c:out value="${subActivity.title}"/></a></td>
						<td><fmt:message key="label.completed" /></td>
					</c:when>
					<c:when test="${statusMap[id] == 2}">
						<td><a href="<lams:LAMSURL />/<c:out value="${urlMap[id]}" />"><c:out value="${subActivity.title}"/></a></td>
						<td><fmt:message key="label.started" /></td>
					</c:when>
					<c:otherwise>
						<td><c:out value="${subActivity.title}"/></td>
						<td><fmt:message key="label.not.started" /></td>
					</c:otherwise>
				</c:choose>
			</tr>
		</c:forEach>
		</table>
		</c:if>
		
		<c:if test="${hasSequenceActivity}">
		<c:forEach items="${subActivities}" var="subActivity">
			<c:set var="id" value="${subActivity.activityID}" />
			<h4><c:out value="${subActivity.title}" /> -
			<c:choose>
				<c:when test="${statusMap[id] == 1}"><fmt:message key="label.completed" /></c:when>
				<c:when test="${statusMap[id] == 2}"><fmt:message key="label.started" /></c:when>
				<c:otherwise><fmt:message key="label.not.started" /></c:otherwise>
			</c:choose>
			</h4>
			<table class="table table-bordered table-striped table-condensed">
				<c:forEach items="${subActivity.childActivities}" var="child">
					<c:set var="childId" value="${child.activityID}"/>
					<tr><td>
					<c:choose>
						<c:when test="${statusMap[childId] == 1 or statusMap[childId] == 2}">
						<a href="<lams:LAMSURL />/<c:out value="${urlMap[childId]}" />"><c:out value="${child.title}"/></a>
						</c:when>
						<c:otherwise>
						<c:out value="${child.title}"/>
						</c:otherwise>
					</c:choose>
					</td></tr>
				</c:forEach>
			</table>
		</c:forEach>
		</c:if>
		
		</lams:Page>

		<div id="footer">
		</div><!--closes footer-->

    </body>
</lams:html>




