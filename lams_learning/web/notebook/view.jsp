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
<%@ page import="org.lamsfoundation.lams.notebook.service.CoreNotebookConstants"%>

<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<c:if test="${param.mode == 'edit'}">
	<script type="text/javascript">
		function doSubmit(signature) {
			document.getElementById("notebookForm").signature.value = signature;
			document.getElementById("notebookForm").submit();
		}
	</script>
</c:if>
<fmt:setBundle basename="org.lamsfoundation.lams.learning.ApplicationResources" />

<c:set var="scratchPadSig">
	<%=CoreNotebookConstants.SCRATCH_PAD_SIG%>
</c:set>
<c:set var="scratchJournalSig">
	<%=CoreNotebookConstants.JOURNAL_SIG%>
</c:set>

<c:set var="title">
	<c:choose>
		<c:when test="${mode == 'teacher'}">
			<fmt:message key="mynotes.journals.title" />
		</c:when>
		<c:otherwise>
			<fmt:message key="mynotes.title" />
		</c:otherwise>
	</c:choose>
</c:set>

<lams:Page type="learner" title="${title}"  hideProgressBar="true">

	<html:form action="/notebook.do?method=updateEntry" styleId="notebookForm" method="post">
		<html:hidden property="uid" value="${entry.uid}" />
		<html:hidden property="signature" />
		<html:hidden property="lessonID" value="${entry.externalID}" />
		<html:hidden property="currentLessonID" value="${empty currentLessonID ? param.currentLessonID : currentLessonID}" />
		
		<!-- set title -->
		<c:set var="title">
			<c:choose>
				<c:when test="${empty entry.title}">
					<fmt:message key="mynotes.entry.no.title.label" />
				</c:when>
				<c:otherwise>
					<c:out value="${entry.title}" escapeXml="false" />
				</c:otherwise>
			</c:choose>
		</c:set>

		<!-- title -->
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<c:choose>
						<c:when test="${param.mode == 'edit'}">
							<fmt:message key="mynotes.edit.heading">
								<fmt:param>
									<c:out value="${title}" escapeXml="false" />
								</fmt:param>
							</fmt:message>
						</c:when>
						<c:otherwise>
							<c:out value="${title}" escapeXml="false" />
						</c:otherwise>
					</c:choose>
				</h4>

			</div>
			<div class="panel-body">
				<c:choose>
					<c:when test="${param.mode == 'edit'}">
						<div class="form-group" style="text-align: left;">
							<label for="${entry.title}"><fmt:message key="mynotes.entry.title.label"></fmt:message></label>
							<html:text property="title" styleClass="form-control" styleId="${entry.title}" value="${entry.title}"></html:text>
						</div>
						<div class="form-group" style="text-align: left;">
							<label><fmt:message key="mynotes.entry.entry.label"></fmt:message></label>
							<html:textarea property="entry" styleClass="form-control" styleId="entry" style="width: 100%" rows="10"
								value="${entry.entry}" />
						</div>
						</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:set var="entryTxt">
							<c:out value="${entry.entry}" escapeXml="false" />
						</c:set>
						<div class="panel">
							<lams:out value="${entryTxt}" />
						</div>
						<c:if test="${mode == 'teacher'}">
							<i>
								<fmt:message key="mynotes.entry.submitted.by">
									<fmt:param>
										<lams:Portrait userId="${entry.user.userId}"/><c:out value="${entry.user.fullName}" escapeXml="false" />
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
								<html:rewrite page="/notebook.do?method=viewEntry&mode=edit&currentLessonID=${param.currentLessonID}&uid=" />
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
										<html:rewrite page="/notebook.do?method=viewAllJournals&lessonID=${param.currentLessonID}" />
									</c:set>
								</c:when>
								<c:otherwise>
									<c:set var="viewAll">
										<html:rewrite page="/notebook.do?method=viewAll&currentLessonID=${param.currentLessonID}" />
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

	</html:form>
</lams:Page>
