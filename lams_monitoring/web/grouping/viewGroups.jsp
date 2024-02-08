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
		<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
		<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
		
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
    </lams:head>

	<lams:PageAdmin title="${title}">
				<c:choose>
				<c:when test="${empty groups}">
					<fmt:message key="label.grouping.no.groups.created"/>
				</c:when>
				<c:otherwise>
				
					<!--  4 columns (or less if fewer groups), stacks on a smaller screen -->
					<c:set var="numGroups" value="${fn:length(groups)}"/> 
					<c:choose>
					<c:when test="${numGroups == 1}">
						<c:set var="colClass">col-12</c:set>
					</c:when>
					<c:when test="${numGroups == 2}">
						<c:set var="colClass">col-6</c:set>
					</c:when>
					<c:when test="${numGroups == 3}">
						<c:set var="colClass">col-4</c:set>
					</c:when>
					<c:otherwise>
						<c:set var="colClass">col-3</c:set>
					</c:otherwise>
					</c:choose>
					
					<c:forEach items="${groups}" var="group" varStatus="groupCounter">
					<c:if test="${(groupCounter.index mod 4) == 0}"><div class="row"></c:if>
			 			<div class="${colClass}">
							<h4><c:out value="${isCourseGrouping?group.name:group.groupName}" /></h4>
							<c:choose> 
								<c:when test="${empty group.users}">
									<span><fmt:message key="label.no.learners"/></span>
								</c:when>
								<c:otherwise>
									<table class="group-table table table-condensed table-striped">
									<c:forEach items="${group.users}" var="user">
										<tr>
											<td>
												<lams:Portrait userId="${user.userId}"/>
												<span class="loffset5">
													<c:out value="${user.firstName} ${user.lastName}" />
												</span>
											</td>
										</tr>
									</c:forEach>
									</table>
								</c:otherwise>
							</c:choose>
						</div>
					<c:if test="${(groupCounter.last) or (((groupCounter.index+1) mod 4) == 0)}"></div></c:if>
					</c:forEach> 
				</c:otherwise>
				</c:choose>	
	</lams:PageAdmin>
</lams:html>
