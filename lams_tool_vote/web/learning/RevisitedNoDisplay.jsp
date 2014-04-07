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

<%@ include file="/common/taglibs.jsp"%>

<c:choose>
	 <c:when test="${fn:length(requestScope.listGeneralCheckedOptionsContent) > 1}">
	
	    <h2><fmt:message key="label.learner.nominations" /></h2>
	
	 </c:when>
	 <c:otherwise>
	    <h2><fmt:message key="label.learner.nomination" /></h2>
	
	 </c:otherwise>
</c:choose>

<c:forEach var="entry" items="${requestScope.listGeneralCheckedOptionsContent}">
	<div>
		<c:out value="${entry}" escapeHtml="false" />
	</div>
</c:forEach>

<div>
	<lams:out value="${VoteLearningForm.userEntry}" escapeHtml="true" />
</div>
