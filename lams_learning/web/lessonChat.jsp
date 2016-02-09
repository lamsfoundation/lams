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
<%@ page import="org.lamsfoundation.lams.util.Configuration" import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE html>

<lams:html>
	<lams:head>
		<lams:css/>
        <script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		
		<script type="text/javascript">
			function resizeFrame() {
			    var height = $(window).height();

			    <c:if test="${param.presenceEnabledPatch}">
			    	resizeChat();
			    </c:if>
			}
			
			window.onresize = resizeFrame;
		</script>
		
		<title><fmt:message key="learner.im.title"/></title>
	</lams:head>

	<body class="stripes" onload="resizeFrame()">
		<c:if test="${param.presenceEnabledPatch}">
		    <%@ include file="presenceChat.jsp" %>
		</c:if>
	 </body>

</lams:html>
