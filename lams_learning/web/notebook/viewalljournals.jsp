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
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<fmt:setBundle basename="org.lamsfoundation.lams.learning.ApplicationResources" />
	<div id="content">
		<h1><fmt:message key="mynotes.journals.title"/></h1>
	
		<h2><fmt:message key="mynotes.view.all.button"/></h2>
		<p>&nbsp;</p>
		<table class="alternative-color">
					
			<c:forEach var="entry" items="${journals}" varStatus="status">
				
				<!-- set user check flag -->
				<c:if test="${status.first}">
					<c:set var="userToCheck" value="${entry.user.userId}"/>
					<h2><c:out value="${entry.user.fullName}" escapeXml="false"/></h2>
						<tr>
							<th scope="col" width="30%" colspan="2"><fmt:message key="mynotes.entry.title.label"/></th>
							<th scope="col" width="25%" align="center"><fmt:message key="mynotes.entry.create.date.label"/></th>
							<th scope="col" width="25%" align="center"><fmt:message key="mynotes.entry.last.modified.label"/></th>
						</tr>
				</c:if>
				
				<c:if test="${!empty userToCheck && userToCheck != entry.user.userId}">
					<!-- do segment separator -->
					</table>
					
					<c:set var="userToCheck" value="${entry.user.userId}"/>
					<h2><c:out value="${entry.user.fullName}" escapeXml="false"/></h2>
					<table class="alternative-color">
						<tr>
							<th scope="col" width="30%" colspan="2"><fmt:message key="mynotes.entry.title.label"/></th>
							<th scope="col" width="25%" align="center"><fmt:message key="mynotes.entry.create.date.label"/></th>
							<th scope="col" width="25%" align="center"><fmt:message key="mynotes.entry.last.modified.label"/></th>
						</tr>
				</c:if>
				
				<tr>
					<td align="left" width="28%">
						<c:set var="viewnote">
							<html:rewrite page="/notebook.do?method=viewEntry&mode=teacher&uid=" /><c:out value="${entry.uid}"/>
						</c:set> 
						<html:link href="${viewnote}">
							<c:out value="${entry.title}" escapeXml="false"/>
						</html:link>
					</td><td>&nbsp;</td>
					<td><lams:Date value="${entry.createDate}"/></td>
					<td><lams:Date value="${entry.lastModified}"/></td>
				</tr>
			</c:forEach>
		
		</table>
		<table>
			<tr>
				<td>
				<div class="right-buttons">
					<a href="#" class="button" id="addNewBtn" onClick="window.close();"><fmt:message key="label.close.button"/></a>
				</div>
				</td>
			</tr>
		</table>
	</div>  <!--closes content-->


	<div id="footer">
	</div><!--closes footer-->

