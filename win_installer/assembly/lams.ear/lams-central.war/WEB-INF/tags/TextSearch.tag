<% 
/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
 
 /**
  * TextSearch.tag
  *	Author: Marcin Cieslak
  *	Description: Displays form for creating text search conditions.
  */
 
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<%-- Required attributes --%>
<%@ attribute name="sessionMapID" required="true" rtexprvalue="true" %>
<%@ attribute name="wrapInFormTag" required="true" rtexprvalue="true" %>

<%-- Optional attributes --%>
<%@ attribute name="action" required="false" rtexprvalue="true" %>
<%@ attribute name="formID" required="false" rtexprvalue="true" %>
<%@ attribute name="headingLabelKey" required="false" rtexprvalue="true" %>
<%@ attribute name="allWordsLabelKey" required="false" rtexprvalue="true" %>
<%@ attribute name="phraseLabelKey" required="false" rtexprvalue="true" %>
<%@ attribute name="anyWordsLabelKey" required="false" rtexprvalue="true" %>
<%@ attribute name="excludedWordsLabelKey" required="false" rtexprvalue="true" %>
<%@ attribute name="saveButtonLabelKey" required="false" rtexprvalue="true" %>
<%@ attribute name="cancelButtonLabelKey" required="false" rtexprvalue="true" %>
<%@ attribute name="cancelAction" required="false" rtexprvalue="true" %>

<%-- Default value for message key --%>
<c:if test="${empty action}">
	<c:set var="action" value="authoring/textsearch.do" scope="request"/>
</c:if>
<c:if test="${empty cancelAction}">
	<c:set var="cancelAction" value="authoring/textsearchCancel.do" scope="request"/>
</c:if>
<c:if test="${empty formID}">
	<c:set var="formID" value="textSearchForm" scope="request"/>
</c:if>
<c:if test="${empty headingFindTextLabelKey}">
	<c:set var="headingLabelKey" value="textsearch.heading" scope="request"/>
</c:if>
<c:if test="${empty allWordsLabelKey}">
	<c:set var="allWordsLabelKey" value="textsearch.all.words" scope="request"/>
</c:if>
<c:if test="${empty phraseLabelKey}">
	<c:set var="phraseLabelKey" value="textsearch.phrase" scope="request"/>
</c:if>
<c:if test="${empty anyWordsLabelKey}">
	<c:set var="anyWordsLabelKey" value="textsearch.any.words" scope="request"/>
</c:if>
<c:if test="${empty excludedWordsLabelKey}">
	<c:set var="excludedWordsLabelKey" value="textsearch.excluded.words" scope="request"/>
</c:if>
<c:if test="${empty saveButtonLabelKey}">
	<c:set var="saveButtonLabelKey" value="textsearch.save" scope="request"/>
</c:if>
<c:if test="${empty cancelButtonLabelKey}">
	<c:set var="cancelButtonLabelKey" value="textsearch.cancel" scope="request"/>
</c:if>

<!-- begin text search form content -->
	
<c:if test="${wrapInFormTag}">
	<form action="${action}" id="${formID}" name="${formID}">
</c:if>
		<html:hidden property="sessionMapID" />
		<h4><fmt:message key="${headingLabelKey}" /></h4>
		<table>
			<tr>
				<td>
					<fmt:message key="${allWordsLabelKey}" />
				</td>
				<td>
					<html:text size="40" property="allWords" />
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="${phraseLabelKey}" />
				</td>
				<td>
					<html:text size="40" property="phrase" />
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="${anyWordsLabelKey}" />
				</td>
				<td>
					<html:text size="40" property="anyWords" /> 
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="${excludedWordsLabelKey}" />
				</td>
				<td>
					<html:text size="40" property="excludedWords" /> 
				</td>
			</tr>
		</table>
<c:if test="${wrapInFormTag}">
	<lams:ImgButtonWrapper>
		<a href="#" onclick="${formID}.submit();" class="button-add-item"><fmt:message key="${saveButtonLabelKey}" />
		</a>
		<a href="#" onclick="location.href='${cancelAction}'" class="button space-left"><fmt:message key="${cancelButtonLabelKey}" />
		</a>
	</lams:ImgButtonWrapper>
	</form>
</c:if>
<!-- end text search form content -->