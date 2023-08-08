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

<c:set var="title"><fmt:message key="monitoring.gallery.walk.cluster.view"/></c:set>

<lams:html>
	<lams:head>
		<title><fmt:message key="monitoring.gallery.walk.cluster.view"/></title>

		<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
		<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
		<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
		<style>
			.group-item {
				min-width: 20rem;
			}
			body.component table.table {
				margin-bottom: 0;
			}
			body.component table.table tbody td {
				border-bottom: none !important;
			}
		</style>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	</lams:head>

	<body class="component">
	<lams:Page5 type="monitor" title="${title}">

		<div class="d-flex flex-wrap">
			<c:choose>
				<c:when test="${empty groups}">
					<fmt:message key="monitoring.gallery.walk.cluster.view.none"/>
				</c:when>
				<c:otherwise>
					<c:forEach items="${groups}" var="group" varStatus="groupCounter">
						<div class="group-item border border-secondary pt-1 mb-4 me-2">
							<h4 class="text-center">
								<c:out value="${group.key}" />
							</h4>
							<table class="cluster-table table table-condensed table-striped">
								<c:forEach items="${group.value}" var="linkedGroup">
									<tr>
										<td>
											<span class="loffset5">
												<c:out value="${linkedGroup}" />
											</span>
										</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
		<div id="footer"></div>
	</lams:Page5>

	<div id="footer">
	</div><!--closes footer-->

	</body>
</lams:html>