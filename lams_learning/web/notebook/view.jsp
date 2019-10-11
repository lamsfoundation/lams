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
<%@ page import="org.lamsfoundation.lams.notebook.service.CoreNotebookConstants"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="scratchPadSig"><%=CoreNotebookConstants.SCRATCH_PAD_SIG%></c:set>
<c:set var="scratchJournalSig"><%=CoreNotebookConstants.JOURNAL_SIG%></c:set>
	
<lams:html>
<lams:head>
	<title><fmt:message key="mynotes.title" /></title>

	<lams:css />

	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
	<script type="text/javascript">
		function doSubmit(signature) {
			document.getElementById("notebookForm").signature.value = signature;
			document.getElementById("notebookForm").submit();
		}
	</script>
</lams:head>
<body class="stripes">
	<fmt:setBundle basename="org.lamsfoundation.lams.learning.ApplicationResources" />
	
	<c:set var="pageTitle">
		<c:choose>
			<c:when test="${mode == 'teacher'}">
				<fmt:message key="mynotes.journals.title" />
			</c:when>
			<c:otherwise>
				<fmt:message key="mynotes.title" />
			</c:otherwise>
		</c:choose>
	</c:set>
	<lams:Page type="learner" title="${pageTitle}"  hideProgressBar="true">
	
		<form:form action="updateEntry.do" modelAttribute="notebookForm" id="notebookForm" method="post">
			<form:hidden path="uid" value="${entry.uid}" />
			<form:hidden path="signature" />
			<form:hidden path="lessonID" value="${entry.externalID}" />
			<form:hidden path="currentLessonID" value="${empty currentLessonID ? param.currentLessonID : currentLessonID}" />
			
			<!-- title -->
			<c:set var="title">
				<c:choose>
					<c:when test="${empty entry.title}">
						<fmt:message key="mynotes.entry.no.title.label" />
					</c:when>
					<c:otherwise>
						<c:out value="${entry.title}"/>
					</c:otherwise>
				</c:choose>
			</c:set>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">
						<c:choose>
							<c:when test="${param.mode == 'edit'}">
								<fmt:message key="mynotes.edit.heading">
									<fmt:param>${title}</fmt:param>
								</fmt:message>
							</c:when>
							<c:otherwise>
								${title}
							</c:otherwise>
						</c:choose>
					</h4>
				</div>
				
				<div class="panel-body">
					<c:choose>
						<c:when test="${param.mode == 'edit'}">
							<div class="form-group" style="text-align: left;">
								<label for="${entry.title}"><fmt:message key="mynotes.entry.title.label"></fmt:message></label>
								<form:input path="title" cssClass="form-control" id="${entry.title}" value="${entry.title}"/>
							</div>
							<div class="form-group" style="text-align: left;">
								<label><fmt:message key="mynotes.entry.entry.label"></fmt:message></label>
								<form:textarea path="entry" cssClass="form-control" id="entry" style="width: 100%" rows="10"
									value="${entry.entry}" />
							</div>
						</c:when>
						<c:otherwise>
							<c:set var="entryTxt">
								<c:out value="${entry.entry}"/>
							</c:set>
							<div class="panel">
								<lams:out value="${entryTxt}" />
							</div>
							<c:if test="${mode == 'teacher'}">
								<i>
									<fmt:message key="mynotes.entry.submitted.by">
										<fmt:param>
											<lams:Portrait userId="${entry.user.userId}"/><c:out value="${entry.user.fullName}"/>
										</fmt:param>
									</fmt:message>
								</i>
							</c:if>
						</c:otherwise>
					</c:choose>
	
					<div class="voffset10">
						<c:choose>
							<c:when test="${param.mode == 'edit'}">
								<a href="#" class="btn btn-default" id="saveInNotebookBtn" onClick="doSubmit('${scratchPadSig}')">
									<fmt:message key="mynotes.notebook.save.button" />
								</a>
								<a href="#" class="btn btn-default" id="saveInJournalBtn" onClick="doSubmit('${scratchJournalSig}')">
									<fmt:message key="mynotes.journal.save.button" />
								</a>
								<a href="javascript: history.back();" class="btn btn-default" id="cancelBtn">
									<fmt:message key="label.cancel.button" />
								</a>
							</c:when>
							<c:otherwise>
								<c:set var="editnote">
									<c:url value="/notebook/viewEntry.do?&mode=edit&currentLessonID=${param.currentLessonID}&uid=" />
									<c:out value="${entry.uid}" />
								</c:set>
								<c:if test="${entry.externalSignature != scratchJournalSig}">
									<a href="${editnote}" class="btn btn-default" id="saveBtn">
										<fmt:message key="label.edit.button" />
									</a>
								</c:if>
								<c:choose>
									<c:when test="${mode == 'teacher'}">
										<c:set var="viewAll">
											<c:url value="/notebook/viewAllJournals.do?lessonID=${param.currentLessonID}" />
										</c:set>
									</c:when>
									<c:otherwise>
										<c:set var="viewAll">
											<c:url value="/notebook/viewAll.do?currentLessonID=${param.currentLessonID}" />
										</c:set>
									</c:otherwise>
								</c:choose>
								<a href="${viewAll}" class="btn btn-default" id="viewAllBtn">
									<fmt:message key="mynotes.view.all.button" />
								</a>
							</c:otherwise>
						</c:choose>
					</div>
	
				</div>
			</div>
	
		</form:form>
	</lams:Page>
</body>

</lams:html>
