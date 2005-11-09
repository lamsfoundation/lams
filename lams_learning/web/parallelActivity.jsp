<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

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
  
<%@ page language="java"%>
<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>


<%-- TODO: use type --%>
<frameset rows="*,*" bordercolor="1" id="lamsDynamicFrameSet">
	<c:forEach items="${activityForm.activityURLs}" var="activityURL" varStatus="loop">
		<frame src="<c:out value="${activityURL.url}" />" 
			name="TaskFrame<c:out value="${loop.index}" />"
			frameborder="" bordercolor="#E0E7EB"
			id="lamsDynamicFrame<c:out value="${loop.index}" />">
	</c:forEach>
</frameset>

<noframes>
	<body>
		<fmt:message key="message.activity.parallel.noFrames" />
	</body>
</noframes>

