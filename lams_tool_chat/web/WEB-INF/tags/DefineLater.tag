
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
	 * DefineLater.tag
	 *	Author: Fiona Malikoff
	 *	Description: Layout for "Define Later" screens - to be used in learning.
	 *  A suggested layout - unless the tool has special requirements, this layout should be used.
	 *  Expects to be used inside <div id="content"></div>
	 */
%>

<%@ tag body-content="scriptless"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams" %>

<%@ attribute name="defineLaterMessageKey" required="false" rtexprvalue="true"%>
<%@ attribute name="buttonTryAgainKey" required="false" rtexprvalue="true"%>

<%-- Default value for I18N keys --%>
<c:if test="${empty defineLaterMessageKey}">
	<c:set var="defineLaterMessageKey" value="define.later.message" />
</c:if>
<c:if test="${empty buttonTryAgainKey}">
	<c:set var="buttonTryAgainKey" value="button.try.again" />
</c:if>

<div class="container-lg">
	<lams:Alert5 type="info" id="define-later-info" close="false">
		<fmt:message key="${defineLaterMessageKey}" />
	</lams:Alert5>

	<div class="activity-bottom-buttons">
		<a id="finishButton" href="javascript:location.reload(true);" class="btn btn-primary mt-3"> 
			<fmt:message key="${buttonTryAgainKey}" />
		</a>
	</div>
</div>