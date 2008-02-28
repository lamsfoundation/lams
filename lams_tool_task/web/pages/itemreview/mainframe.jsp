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
<%@ include file="/common/taglibs.jsp" %>
<frameset rows="95,*" frameborder="no">
	<frame src="<c:url value="/pages/itemreview/initnav.jsp"/>?mode=${mode}&itemIndex=${itemIndex}&itemUid=${itemUid}&toolSessionID=${toolSessionID}&sessionMapID=${sessionMapID}" 
	name=headerFrame" marginheight="0" scrolling="YES">
</frameset>


<%--
To avoid use HttpSession (LDEV-199), try to 
use request.setAttribute() transfer value. But this page is embeded into a Frame html page, directly 
request can not block on frame page, so use this trick page redirect request, then server side could handle this 
request using this page as target 
--%>

<%@ include file="/common/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
            "http://www.w3.org/TR/html4/loose.dtd">
<lams:html>
	<lams:head>
	</lams:head>
	<body class="stripes" bgcolor="#9DC5EC" border="1" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
		<c:redirect url="/nextInstruction.do">
			<c:param name="mode" value="${mode}"/>
			<c:param name="itemIndex" value="${itemIndex}"/>
			<c:param name="itemUid" value="${itemUid}"/>
			<c:param name="toolSessionID" value="${toolSessionID}"/>
			<c:param name="sessionMapID" value="${sessionMapID}"/>
		</c:redirect>
	</body>
</lams:html>
