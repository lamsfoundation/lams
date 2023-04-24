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
		<title><fmt:message key="label.branching.branch.heading"/></title>

		<lams:css/>
		<lams:css webapp="monitoring" suffix="monitorLesson"/>
		<style>
			table {
				margin: auto;
				width: 50% !important;
			}
		</style>

		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	</lams:head>

	<body class="stripes">
	<lams:Page type="monitor" title="${title}">

		<div class="container">
			<c:choose>
				<c:when test="${empty branchActivityEntries}">
					<fmt:message key="label.branching.no.mappings.created"/>
				</c:when>
				<c:otherwise>
					<table class="table table-striped table-bordered">
						<tr>
							<th>
								<b><fmt:message key="label.grouping.group.heading"/></b>
							</th>
							<th>
								<b><fmt:message key="label.branching.branch.heading"/></b>
							</th>
						</tr>

						<c:forEach var="branchActivityEntry" items="${branchActivityEntries}" varStatus="status">
							<tr>
								<td>
									<c:out value="${branchActivityEntry.group.groupName}" />
								</td>
								<td>
									<c:out value="${branchActivityEntry.branchSequenceActivity.title}" />
								</td>
							</tr>
						</c:forEach>
					</table>
				</c:otherwise>
			</c:choose>
		</div>
		<div id="footer"></div>
	</lams:Page>

	<div id="footer">
	</div><!--closes footer-->

	</body>
</lams:html>