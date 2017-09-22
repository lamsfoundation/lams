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
<style type="text/css">
	.user-container {
		padding: 2px;
	}
</style>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<lams:Page type="learner" title="${title}">
	<div class="table-responsive">
		<table class="table table-condensed table-hover" cellspacing="0">
			<logic:iterate id="group" name="groups">
				<tr>
					<td width="15%"><strong><c:out value="${group.groupName}" /></strong></td>
					<td><c:forEach items="${group.users}" var="user">
							<div name="u-${user.userId}" class="user-container">
								<lams:Portrait userId="${user.userId}"/><c:out value="${user.firstName}" />&nbsp<c:out value="${user.lastName}" />
							</div>
						</c:forEach>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</div>

	<div id="footer"></div>
</lams:Page>