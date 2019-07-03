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

<%@ include file="/taglibs.jsp"%>

<lams:html>
    <lams:head>
		<lams:css/>
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
		</script>
    </lams:head>
    
    <body class="stripes">
		<c:set var="title"><fmt:message key="label.schedule.gate.title"/></c:set>
		<lams:Page title="${title}" type="monitoring" formID="gateForm">
	
			<%@ include file="gateInfo.jsp" %>
			<c:choose>
				<c:when test="${gateForm.activityCompletionBased}">
					<fmt:message key="label.schedule.gate.activity.completion.based"/>
				</c:when>
				<c:otherwise>
					<c:if test="${not gateForm.gate.gateOpen}" >
						<p><fmt:message key="label.schedule.gate.open.message"/>&nbsp;<lams:Date value="${gateForm.startingTime}"/></p>
						
						<form:form action="scheduleGate.do" modelAttribute="gateForm" id="gateForm" target="_self" cssClass="form-inline">
						<input type="hidden" name="activityId" value="${gateForm.activityId}" />
	
						<div class="form-group">
							<a id="rescheduleShowButton" class="btn btn-default btn-sm" href="#" onClick="javascript:toggleDateWidget()">
								   <i class="fa fa-calendar"></i>
								   <fmt:message key="label.reschedule"/>
							</a>
							<%-- padding keeps the inputs the same height as the buttons. No flicking when shown/hidden --%>
							<form:input cssClass="input input-sm" path="scheduleDate" id="scheduleDate" style="vertical-align:bottom"/> 
							<input type="submit" id="scheduleDatetimeApply" class="btn btn-primary btn-sm loffset2" value="<fmt:message key="button.apply"/>" />
						</div>
						</form:form>
					</c:if>
					
					<c:if test="${gateForm.endingTime!=null}">
						<p><fmt:message key="label.schedule.gate.close.message"/> <lams:Date value="${gateForm.endingTime}"/></p>
					</c:if>
					
					
				</c:otherwise>
			</c:choose>
	
			<%@ include file="gateStatus.jsp" %>
	
		</lams:Page>


		<div id="footer">
		</div><!--closes footer-->

    </body>
</lams:html>

	
