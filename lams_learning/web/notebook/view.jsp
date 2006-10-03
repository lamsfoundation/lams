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
<%@ page import="org.lamsfoundation.lams.notebook.service.CoreNotebookConstants" %>

<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<c:if test="${param.mode == 'edit'}">
<script type="text/javascript">
	function doSubmit(signature) {
		document.getElementById("notebookForm").signature.value = signature;
		document.getElementById("notebookForm").submit();
	}

</script>
</c:if>
<fmt:setBundle basename="org.lamsfoundation.lams.learning.ApplicationResources" />
	
	
			
	<div id="content">
		<html:form action="/notebook.do?method=updateEntry" styleId="notebookForm" method="post">
			<html:hidden property="uid" value="${entry.uid}"/>
			<html:hidden property="signature"/>
			<html:hidden property="lessonID" value="${entry.externalID}"/>
	
		<h1><fmt:message key="mynotes.title"/></h1>
		
		<!-- title -->
		<h2><c:choose>
				<c:when test="${param.mode == 'edit'}">
					<fmt:message key="mynotes.edit.heading">
						<fmt:param>
							<c:out value="${entry.title}" escapeXml="false"/>
						</fmt:param>
					</fmt:message>
				</c:when>
				<c:otherwise>
					<c:out value="${entry.title}"/>
				</c:otherwise>
			</c:choose>
		</h2>
		
		<table>
		<c:choose>
				<c:when test="${param.mode == 'edit'}">
							<tr>
								<td colspan="2">
									<div class="field-name" style="text-align: left;">
										<fmt:message key="mynotes.entry.title.label"></fmt:message>
									</div>
									<html:text property="title" style="width: 100%;" value="${entry.title}"></html:text>
								</td>
							</tr>
							<tr>
								<td>
									<div class="field-name" style="text-align: left;">
										<fmt:message key="mynotes.entry.entry.label"></fmt:message>
									</div>
									<html:textarea property="entry" styleId="entry" style="width: 100%" rows="10" value="${entry.entry}" />
								</td>
							</tr>
				</c:when>
				<c:otherwise>
					<tr>
						<td>
							<p><c:out value="${entry.entry}" escapeXml="false"/></p>
						</td>
					</tr>
				</c:otherwise>
			</c:choose>
			<tr>
				<td>
				<div class="right-buttons">
					<c:choose>
						<c:when test="${param.mode == 'edit'}">
							<a href="#" class="button" id="saveInNotebookBtn" onClick="doSubmit('<%= CoreNotebookConstants.SCRATCH_PAD_SIG %>')"><fmt:message key="mynotes.notebook.save.button"/></a>
							<a href="#" class="button" id="saveInJournalBtn" onClick="doSubmit('<%= CoreNotebookConstants.JOURNAL_SIG %>')"><fmt:message key="mynotes.journal.save.button"/></a>
							<a href="javascript: history.back();" class="button" id="cancelBtn"><fmt:message key="label.cancel.button"/></a>
						</c:when>
						<c:otherwise>
							<c:set var="editnote">
								<html:rewrite page="/notebook.do?method=viewEntry&mode=edit&uid=" /><c:out value="${entry.uid}"/>
							</c:set>
							<c:set var="journalSig"><%= CoreNotebookConstants.JOURNAL_SIG %></c:set>
							<c:if test="${entry.externalSignature != journalSig}"><a href="${editnote}" class="button" id="saveBtn"><fmt:message key="label.edit.button"/></a></c:if>
							<a href="javascript: history.back();" class="button" id="viewAllBtn"><fmt:message key="mynotes.view.all.button"/></a>
						</c:otherwise>
					</c:choose>
				</div>
				</td>
			</tr>
		</table>
		</html:form>
	</div>  <!--closes content-->


	<div id="footer">
	</div><!--closes footer-->

