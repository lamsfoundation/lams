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
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
      <meta http-equiv="content-type" content="text/html; charset=UTF-8">
	  <lams:css/>
	<title>Start Lesson</title>
	</head>
  <body>
	<H2>Start Lesson</H2>
	
	<P>Before you can start a lesson, you will need to create a learning design 
	using the authoring client. When you save the learning design, note down the id of 
	design ( or look it up in the lams_learning_design table in the database). 
	Once that is done, you can start a lesson using the learning design id.</p>

	<P>Authoring does not support the entry of a title or description for a design yet, so all we are able to display is the list
	of learning design ids.</p>
	
	<p>All lessons will be run with the users from the "Playpen:Everybody" class.</p>
	
	<html:form action="/dummy" target="_self" method="POST">
	
	<table width="100%" border="0" cellspacing="2" cellpadding="2" summary="This table is being used for layout purposes only">

		<tr><td class="formlabel">Learning Design</td>
		<td class="formcontrol">
			<select name="learningDesignId">
				<c:forEach items="${designs}" var="design">
					<option value="<c:out value="${design.learningDesignId}"/>"><c:out value="${design.title}"/>: <c:out value="${design.learningDesignId}"/>
					<c:if test="${design.title}">
						: <c:out value="${design.title}"/> (<c:out value="${design.description}"/>)</option>
					</c:if>
				</c:forEach>
			</select>
		</td></tr>

		<tr><td class="formlabel">Title</td>
		<td class="formcontrol"><html:text property="title" maxlength="80"  size="50"/></td></tr>

		<tr><td class="formlabel">Description (optional)</td>
		<td class="formcontrol"><html:text property="desc" maxlength="80"  size="50"/></td></tr>

		<tr><td class="formlabel">Start lesson at date/time (optional)<BR>
		Format: DD/MM/YYYY HH:MM (24hr time)</td>
		<td class="formcontrol">
			<html:text property="startDay" maxlength="2"  size="3"/>/
			<html:text property="startMonth" maxlength="2"  size="3"/>/
			<html:text property="startYear" maxlength="4"  size="5"/>&nbsp;&nbsp;
			<html:text property="startHour" maxlength="2"  size="3"/>:
			<html:text property="startMinute" maxlength="2"  size="3"/>			
		</td></tr>

		<tr><td class="formlabel" colspan="2">
			<html:submit property="method" value="startLesson" styleClass="button" onmouseover="pviiClassNew(this,'buttonover')" onmouseout="pviiClassNew(this,'button')" >
				Start Lesson 
			</html:submit>
		</td></tr>
	</table>
	
	</html:form>
	
	</body>
</html>
