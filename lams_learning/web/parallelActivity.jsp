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

<lams:html>

<lams:head>
	<title><fmt:message key="learner.title" />
	</title>

	<lams:css />
	<c:set var="lams">
		<lams:LAMSURL />
	</c:set>

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript"
		src="${lams}includes/javascript/common.js"></script>
</lams:head>

	<frameset rows="*,*" bordercolor="1" id="lamsDynamicFrameSet">
		<c:forEach items="${activityForm.activityURLs}" var="activityURL" varStatus="loop">
			<frame src="<c:out value="${activityURL.url}" />" 
				name="TaskFrame<c:out value="${loop.index}" />"
				frameborder="" bordercolor="#E0E7EB"
				id="lamsDynamicFrame<c:out value="${loop.index}" />">
		</c:forEach>
	</frameset>

</lams:html>
