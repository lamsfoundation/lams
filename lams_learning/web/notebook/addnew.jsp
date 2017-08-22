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
<script type="text/javascript">
	function doSubmit(signature) {
		document.getElementById("notebookForm").signature.value = signature;
		document.getElementById("notebookForm").submit();
	}
</script>
<fmt:setBundle basename="org.lamsfoundation.lams.learning.ApplicationResources" />

<c:set var="title">
	<fmt:message key="mynotes.title" />
</c:set>

<c:set var="scratchPadSig">
	<%=CoreNotebookConstants.SCRATCH_PAD_SIG%>
</c:set>
<c:set var="scratchJournalSig">
	<%=CoreNotebookConstants.JOURNAL_SIG%>
</c:set>


<lams:Page type="learner" title="${title}"  hideProgressBar="true">
	<html:form action="/notebook.do?method=processNewEntry" styleId="notebookForm" method="post">

		<html:hidden property="signature" />
		<html:hidden property="currentLessonID" value="${param.currentLessonID}" />
		<html:hidden property="lessonID" value="${param.lessonID}" />

		<div class="lead">
			<fmt:message key="mynotes.add.new.button" />
		</div>

					<div class="form-group">
						<label for="title"><fmt:message key="mynotes.entry.title.label"></fmt:message></label>
						<html:text property="title" styleId="title" styleClass="form-control"></html:text>
					</div> 
					<div class="form-group">
						<label for="entry"><fmt:message key="mynotes.entry.entry.label"></fmt:message></label>
						<html:textarea property="entry" styleId="entry" styleClass="form-control" rows="8" />
					</div> 
					<div class="voffset10 pull-right">
						<a href="#" class="btn btn-default" id="saveInNotebookBtn"
							onClick="doSubmit('${scratchPadSig}')"><fmt:message
								key="mynotes.notebook.save.button" /></a> <a href="#" class="btn btn-default" id="saveInJournalBtn"
							onClick="doSubmit('${scratchJournalSig}')"><fmt:message key="mynotes.journal.save.button" /></a>
						<a href="javascript: history.back();" class="btn btn-default" id="cancelBtn"><fmt:message key="label.cancel.button" /></a>
					</div>
				</td>
			</tr>
		</table>
	</html:form>
</lams:Page>
