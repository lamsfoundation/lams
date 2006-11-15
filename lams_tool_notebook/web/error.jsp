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
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA

  http://www.gnu.org/licenses/gpl.txt
--%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" 
	"http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" isErrorPage="true" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<lams:html>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<%-- Catch JSP Servlet Exception --%>
<%
if (exception != null) {
%>
<c:set var="errorMessage">
	<%=exception.getMessage()%>
</c:set>
<c:set var="errorName">
	<%=exception.getClass().getName()%>
</c:set>
<%
		java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
		java.io.PrintStream os = new java.io.PrintStream(bos);
		exception.printStackTrace(os);
		String stack = new String(bos.toByteArray());
%>
<c:set var="errorStack">
	<%=stack%>
</c:set>
<%
} else if ((Exception) request.getAttribute("javax.servlet.error.exception") != null) {
%>

<c:set var="errorMessage">
	<%=((Exception) request.getAttribute("javax.servlet.error.exception")).getMessage()%>
</c:set>
<c:set var="errorName">
	<%=((Exception) request.getAttribute("javax.servlet.error.exception")).getMessage()
									.getClass().getName()%>
</c:set>
<%
		java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
		java.io.PrintStream os = new java.io.PrintStream(bos);
		((Exception) request.getAttribute("javax.servlet.error.exception")).printStackTrace(os);
		String stack = new String(bos.toByteArray());
%>
<c:set var="errorStack">
	<%=stack%>
</c:set>
<%
}
%>
<body class="stripes">
<form action="${lams}errorpages/error.jsp" method="post" id="errorForm">
	<input type="hidden" name="errorName" value="${errorName}"/>
	<input type="hidden" name="errorMessage" value="${errorMessage}"/>
	<input type="hidden" name="errorStack" value="${errorStack}"/>
</form>

<script type="text/javascript">

if(window.top != null)
	document.getElementById("errorForm").target = "_parent";
document.getElementById("errorForm").submit();

</script>
</body>
</lams:html>
