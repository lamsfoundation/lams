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

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>

<%@ include file="gateDescription.jsp"%>

<lams:Alert type="info" close="false" id="whenOpens">
<fmt:message key="label.schedule.gate.open.remaining" />&nbsp;<strong><lams:Date value="${GateForm.map.startingTime}" timeago="true"/></strong>
</lams:Alert>

<c:choose>
	<c:when test="${not empty GateForm.map.reachDate}">
		<p>
			<fmt:message key="label.schedule.gate.reach" />&nbsp;
			<strong><lams:Date value="${GateForm.map.reachDate}" /></strong>
		</p>
	</c:when>
	<c:otherwise>
		<c:if test="${GateForm.map.startingTime!=null}">
			<p>
				<fmt:message key="label.schedule.gate.open.message" />&nbsp;<strong><lams:Date value="${GateForm.map.startingTime}" /></strong>
			</p>
		</c:if>
		<c:if test="${GateForm.map.endingTime!=null}">
			<p>
				<fmt:message key="label.schedule.gate.close.message" />
				<lams:Date value="${GateForm.map.endingTime}" />
			</p>
		</c:if>
	</c:otherwise>
</c:choose>

  <script type="text/javascript">
    jQuery(document).ready(function() {
    	jQuery.timeago.settings.allowFuture = true;
    	jQuery("time.timeago").timeago();
    });
  </script>
