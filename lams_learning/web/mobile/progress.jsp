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

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<lams:html>
	
	<lams:head>
		<lams:css/>
		<title><fmt:message key="learner.title"/></title>
	</lams:head>

	<script type="text/javascript">
		function loadFrame(url) {
			parent.frames['contentFrame'].location.href = url;
		}
		function openPopUp(args, title, h, w, resize, status, scrollbar, menubar, toolbar){
			window.open(args,title,"HEIGHT="+h+",WIDTH="+w+",resizable="+resize+",scrollbars=yes,status="+status+",menubar="+menubar+", toolbar="+toolbar);
		}
	</script>
<body>

<c:forEach var="activity" items="${progressList}" varStatus="status">
	<c:if test="${status.first}">
		<UL style="margin-right:0">
	</c:if>
	<c:if test="${activity.floating}">
		<div style="margin-top: 2px">&nbsp;</div>
	</c:if>
	<c:set var="includeActivity" value="${activity}" scope="request"/>
	<jsp:include page="common/progressOutput.jsp" />
	<c:if test="${status.last}">
		</UL>
	</c:if>
</c:forEach>

</body>
</lams:html>



