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

<!DOCTYPE html>
<lams:html xhtml="true">

	<lams:head>
		<title><fmt:message key="${label.view.groups.title}"/></title>
		<lams:css/>
		<META HTTP-EQUIV="Refresh" CONTENT="300;URL=<lams:WebAppURL/>/grouping.do?method=performGrouping&activityID=${activity.activityId}">
		<script src="<lams:LAMSURL/>includes/javascript/AC_RunActiveContent.js" type="text/javascript"></script>
	  </lams:head>

	<body class="stripes">
	      <tiles:insert attribute="body" />
	</body>

</lams:html>
