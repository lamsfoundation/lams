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
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>	
<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/jquery.timeago.js"></script>

<fmt:setBundle basename="org.lamsfoundation.lams.learning.ApplicationResources" />
<c:set var="title">
	<fmt:message key="mynotes.title" />
</c:set>

<lams:Page type="learner" title="${title}" hideProgressBar="true">
	<c:if test="${empty entries}">
		<c:set var="addnote">
			<html:rewrite page="/notebook/add.do?lessonID=" />
			<c:out value="${lessonID}" />
		</c:set>
		<div class="pull-right">
			<a href="${addnote}" class="btn btn-default pull-right" id="addNewBtn"><fmt:message
					key="mynotes.add.new.current.lesson.button" /></a>
		</div>
	</c:if>
	
	<div class="lead">
		<fmt:message key="mynotes.view.all.button" />
	</div>

	<c:forEach var="entryList" items="${entries}" varStatus="list_status">
		<c:forEach var="entry" items="${entryList}" varStatus="entry_status">
			<html:link href="#" linkName="lesson${entry.externalID}" />

			<!-- set sig check flag -->
			<c:if test="${entry_status.first}">
				<c:set var="sigToCheck">
					<c:if test="${entry.externalSignature != ''}">
						<c:out value="${entry.externalSignature}" />
					</c:if>
				</c:set>

				<c:set var="addnote">
					<html:rewrite page="/notebook/add.do?lessonID=" />
					<c:out value="${entry.externalID}" />
				</c:set>

				<div class="panel panel-default">
					<div class="panel-heading">
						<h4 class="panel-title">
							<c:choose>
								<c:when test="${!empty sigToCheck}">
									<fmt:message key="mynotes.signature.${sigToCheck}.heading" /> - <c:out value="${entry.lessonName}"
										escapeXml="false" />
								</c:when>
								<c:otherwise>
									<fmt:message key="mynotes.signature.unknown.heading" /> - <c:out value="${entry.lessonName}" escapeXml="false" />
								</c:otherwise>
							</c:choose>
						</h4>

					</div>
					<div class="panel-body">

						<table class="table table-condensed table-hover" cellspacing="0">
							<tr>
								<th scope="col" width="30%" colspan="2"><fmt:message key="mynotes.entry.title.label" /></th>
								<th scope="col" width="25%" align="center"><fmt:message key="mynotes.entry.create.date.label" /></th>
								<th scope="col" width="25%" align="center"><fmt:message key="mynotes.entry.last.modified.label" /></th>
							</tr>
							</c:if>

							<c:if test="${!empty sigToCheck && sigToCheck != entry.externalSignature}">
								<!-- do segment separator -->
						</table>

						<c:set var="sigToCheck" value="${entry.externalSignature}" />

						<h4>
							<c:choose>
								<c:when test="${!empty sigToCheck}">
									<fmt:message key="mynotes.signature.${sigToCheck}.heading" /> - <c:out value="${entry.lessonName}"
										escapeXml="false" />
								</c:when>
								<c:otherwise>
									<fmt:message key="mynotes.signature.unknown.heading" /> - <c:out value="${entry.lessonName}" escapeXml="false" />
								</c:otherwise>
							</c:choose>
						</h4>

						<table class="table table-condensed table-hover" cellspacing="0">
							<tr>
								<th scope="col" width="30%" colspan="2"><fmt:message key="mynotes.entry.title.label" /></th>
								<th scope="col" width="25%" align="center"><fmt:message key="mynotes.entry.create.date.label" /></th>
								<th scope="col" width="25%" align="center"><fmt:message key="mynotes.entry.last.modified.label" /></th>
							</tr>
							</c:if>

							<tr>
								<td class="align-left" width="28%"><c:set var="viewnote">
										<html:rewrite page="/notebook.do?method=viewEntry&uid=" />
										<c:out value="${entry.uid}" />
									</c:set> <html:link href="${viewnote}">
										<c:choose>
											<c:when test="${empty entry.title}">
												<fmt:message key="mynotes.entry.no.title.label" />
											</c:when>
											<c:otherwise>
												<c:out value="${entry.title}" escapeXml="false" />
											</c:otherwise>
										</c:choose>
									</html:link></td>
								<td>&nbsp;</td>
								<td><lams:Date value="${entry.createDate}" timeago="true"/></td>
								<td><lams:Date value="${entry.lastModified}" timeago="true"/></td>
							</tr>
							</c:forEach>

						</table>

						<!--  new add button for lesson -->
						<div class="voffset10">
							<a href="${addnote}" class="btn btn-default pull-right" id="addNewBtn"><fmt:message
									key="mynotes.add.new.button" /></a>
						</div>
					</div>
				</div>
		</c:forEach>
</lams:Page>

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery("time.timeago").timeago();
	});
</script>