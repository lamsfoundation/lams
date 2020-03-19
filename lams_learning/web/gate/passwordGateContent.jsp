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

<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>

<!DOCTYPE html>
<lams:html xhtml="true">

<lams:head>
	<META HTTP-EQUIV="Refresh"
		CONTENT="60;URL=<lams:WebAppURL/>/gate/knockGate.do?activityID=${gateForm.activityID}&lessonID=${gateForm.lessonID }">

	<c:set var="title"><fmt:message key="label.password.gate.title"/></c:set>
	<title><c:out value="${title}" /></title>
	
	<lams:css />
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script>
		function customSubmitGateForm(){
			$('#key').val($('#passwordField').val());
			return true;
		}
	</script>
</lams:head>

<body class="stripes">
	<lams:Page type="learner" title="${title}">

		<%@ include file="gateDescription.jsp"%>
		
		<c:if test="${gateForm.incorrectKey}">
        <lams:Alert id="incorrectKey" type="danger" close="false">
            <fmt:message key="label.password.gate.incorrect.password">
            </fmt:message>
        </lams:Alert>		
			<p id="incorrectKey">
			</p>
		</c:if>
		<div class="panel">
			<div class="panel-body">
				<div class="form-group">
					<input type="text" class="form-control" id="passwordField" maxlength="32" autocomplete="off" placeholder="<fmt:message key="label.password.gate.message"/>"/>
  				</div>
			</div>
		</div>

		<%@ include file="../gate/gateNext.jsp"%>

	</lams:Page>
</body>

</lams:html>
