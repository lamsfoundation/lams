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

<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<c:set var="displayPrintButton"><lams:Configuration key="DisplayPrintButton"/></c:set>

<div id="content">
	<c:set var="displayName">
		<lams:user property="firstName"/> <lams:user property="lastName"/>
	</c:set>
	
	
	<h2>
		<fmt:message key="message.lesson.finished">
			<fmt:param>
				<c:out value="${displayName}" escapeXml="true"/>
			</fmt:param>
		</fmt:message>
	</h2>
	
	<p>
		<fmt:message key="message.lesson.finishedCont">
			<fmt:param>
				<b><c:out value="${learnerprogress.lesson.lessonName}" escapeXml="true"/></b>
			</fmt:param>
			<fmt:param>
				 <lams:Date value="${learnerprogress.finishDate}" style="short"/>
			</fmt:param>			
		</fmt:message>
	</p>
	
	<c:if test="${not empty releasedLessons}">
		<p>
			<fmt:message key="message.released.lessons">
				<fmt:param>
					<c:out value="${releasedLessons}" escapeXml="true"/> 
				</fmt:param>		
			</fmt:message>
		</p>
	</c:if>
	
	<c:if test="${displayPrintButton}">
		<p class="align-right space-top">
			<a href="#" class="button" onclick="JavaScript:window.print();">
				<fmt:message key="label.print" />
			</a>	
		</p>
	</c:if>
	
</div>
<!--closes content-->


<div id="footer">
</div>
<!--closes footer-->

<c:if test="${not empty lessonFinishUrl}">
	<img width="0" height="0" style="border: none;" src="${lessonFinishUrl}" />
</c:if>
