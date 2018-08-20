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

<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-bean" prefix="bean" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>		
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

	<link type="text/css" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" rel="stylesheet">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui.timepicker.css" type="text/css" media="screen" />
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.timepicker.js"></script>

	<script type="text/javascript">
		var shown = true;
		
		$(document).ready(function(){
			toggleDateWidget();
			// sets up calendar for schedule date choice
			$('#scheduleDate').datetimepicker({
				'minDate' : 0
			});
		});
		
		function toggleDateWidget() {
			if ( shown ) {
				$('#scheduleDate').hide();	
				$('#scheduleDatetimeApply').hide();
				shown = false;
			} else {
				$('#scheduleDate').show();	
				$('#scheduleDatetimeApply').show();
				shown = true;
			}
		}
/* 		
		function applyDate() {
			var date = $('#scheduleDatetimeField').val();
			if (date) {
				$.ajax({
					url : LAMS_URL + 'monitoring/gate.do',
					cache : false,
					data : {
						'method'        : ??,
						'openDate' : date
					},
					success : function() {
						refreshMonitor('lesson');
					}
				});
			} else {
				alert('<fmt:message key="error.lesson.schedule.date"/>');
			}
		}
 */	</script>
	
	<c:set var="title"><fmt:message key="label.schedule.gate.title"/></c:set>
	<lams:Page title="${title}" type="monitoring">

		<%@ include file="gateInfo.jsp" %>
		<c:choose>
			<c:when test="${GateForm.map.activityCompletionBased}">
				<fmt:message key="label.schedule.gate.activity.completion.based"/>
			</c:when>
			<c:otherwise>
				<c:if test="${not GateForm.map.gate.gateOpen}" >
					<p><fmt:message key="label.schedule.gate.open.message"/>&nbsp;<lams:Date value="${GateForm.map.startingTime}"/></p>
					
					<html:form action="/gate?method=scheduleGate" target="_self" styleClass="form-inline">
					<input type="hidden" name="activityId" value="${GateForm.map.activityId}" />

					<div class="form-group">
						<a id="rescheduleShowButton" class="btn btn-default btn-sm" href="#" onClick="javascript:toggleDateWidget()">
							   <i class="fa fa-calendar"></i>
							   <fmt:message key="label.reschedule"/>
						</a>
						<%-- padding keeps the inputs the same height as the buttons. No flicking when shown/hidden --%>
						<html:text styleClass="input input-sm" property="scheduleDate" styleId="scheduleDate" style="vertical-align:bottom"/> 
						<html:submit styleId="scheduleDatetimeApply" styleClass="btn btn-primary btn-sm loffset2"><fmt:message key="button.apply"/></html:submit>
					</div>
					</html:form>
				</c:if>
				
				<c:if test="${GateForm.map.endingTime!=null}">
					<p><fmt:message key="label.schedule.gate.close.message"/> <lams:Date value="${GateForm.map.endingTime}"/></p>
				</c:if>
				
				
			</c:otherwise>
		</c:choose>

		<%@ include file="gateStatus.jsp" %>

	</lams:Page>
