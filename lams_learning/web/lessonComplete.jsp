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

<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/bootstrap.min.js"></script>

<c:set var="displayPrintButton"><lams:Configuration key="DisplayPrintButton"/></c:set>

<script type="text/javascript">
	function restartLesson(){
		if (confirm('<fmt:message key="message.learner.progress.restart.confirm"/>')) {
			window.location.href = "<lams:WebAppURL/>learner.do?method=restartLesson&lessonID=${lessonID}";
		}
	}
</script>

<lams:Page type="learner">
	<c:set var="lastName">
		<lams:user property="lastName"/> 
	</c:set>
	<c:set var="firstName">
	 <lams:user property="firstName"/>
	</c:set>
	
	<div class="lead"><i class="fa fa-lg fa-check-square-o text-success"></i>&nbsp;
		<fmt:message key="message.lesson.finished">
			<fmt:param>
				<strong><c:out value="${firstName}" escapeXml="true"/>&nbsp;<c:out value="${lastName}" escapeXml="true"/></strong>
			</fmt:param>
		</fmt:message>
	</div>
	
	<div class="voffset10">
		<fmt:message key="message.lesson.finishedCont">
			<fmt:param>
				<strong><c:out value="${learnerprogress.lesson.lessonName}" escapeXml="true"/></strong>
			</fmt:param>
			<fmt:param>
				 <lams:Date value="${learnerprogress.finishDate}" style="short"/>
			</fmt:param>			
		</fmt:message>
	</div>
	
	<c:if test="${learnerprogress.lesson.allowLearnerRestart}">
		<div class="voffset10">
			<fmt:message key="message.lesson.restart" />
			<a class="btn btn-default loffset20" href="#" onClick="javascript:restartLesson()">
				<fmt:message key="message.lesson.restart.button" />
			</a>
		</div>
	</c:if>
	
	<c:if test="${not empty releasedLessons}">
		<div class="voffset10">
			<fmt:message key="message.released.lessons">
				<fmt:param>
					<c:out value="${releasedLessons}" escapeXml="true"/> 
				</fmt:param>		
			</fmt:message>
		</div>
	</c:if>
	
	<c:if test="${displayPrintButton}">
		<div class="pull-right voffset10">
			<a href="#" class="btn btn-default" onclick="JavaScript:window.print();">
				<fmt:message key="label.print" />
			</a>	
		</div>
	</c:if>
	
</lams:Page>

<c:if test="${not empty lessonFinishUrl}">
	<img width="0" height="0" style="border: none;" src="${lessonFinishUrl}" />
</c:if>
