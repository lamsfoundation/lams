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

	<div id="content">

		<h1><fmt:message key="label.preview.definelater.title"/></h1>

		<c:choose>
		<c:when test="${isBranching}">
			<p><fmt:message key="label.preview.definelater.branching.message">
					<fmt:param><c:out value="${requestScope.title}"/></fmt:param>
				</fmt:message>
			</p>
		</c:when>
		<c:otherwise>
		<p><fmt:message key="label.preview.definelater.message">
				<fmt:param><c:out value="${requestScope.title}"/></fmt:param>
			</fmt:message>
		</p>
		</c:otherwise>
		</c:choose>

		<div class="right-buttons"><a href="<lams:LAMSURL/>${requestScope.activityURL}" class="button"><fmt:message key="label.next.button"/></a></div>

	</div>  <!--closes content-->


	<div id="footer">
	</div><!--closes footer-->


	