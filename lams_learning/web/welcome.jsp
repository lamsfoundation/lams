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

<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams" %>

<c:if test="${empty lessonID}">
	<bean:parameter id="lessonID" name="lessonID" />
</c:if>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
<script language="JavaScript" type="text/JavaScript">
<!--
	var contentURL = '<lams:WebAppURL />learner/joinLesson.do?lessonID=<c:out value='${lessonID}' escapeXml='true' />';
	window.location.href = contentURL;
//-->
</script>


