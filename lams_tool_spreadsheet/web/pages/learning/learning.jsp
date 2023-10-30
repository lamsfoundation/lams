<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />
<c:set var="mode" value="${sessionMap.mode}" />
<c:set var="toolSessionID" value="${sessionMap.toolSessionID}" />
<c:set var="spreadsheet" value="${sessionMap.spreadsheet}" />
<c:set var="finishedLock" value="${sessionMap.finishedLock}" />
<c:set var="language" value="${pageContext.response.locale.language}" />
<%@ page import="org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants"%>

<lams:PageLearner title="${spreadsheet.title}" toolSessionID="${toolSessionID}">
	<script type="text/javascript">
		checkNextGateActivity('finishButton', '${toolSessionID}', '', finishSession);
		
		function finishSession(){
        	saveUserSpreadsheet("finishSession");
		}
		
		function continueReflect(){
			saveUserSpreadsheet("continueReflect");
		}
		
		//save changes made in spreadsheet
		function saveUserSpreadsheet(typeOfAction){
			var code = window.frames["externalSpreadsheet"].cellsToJS();
			code = encodeURIComponent(code);
		    var url = '<c:url value="/learning/saveUserSpreadsheet.do"/>?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}&typeOfAction=' + typeOfAction + '&code=' + code;
		    location.href = url;
		}  
    </script>

	<div id="container-main">	
		<c:if test="${sessionMap.lockOnFinish and mode != 'teacher'}">
			<lams:Alert5 type="danger" id="lockWhenFinished" close="false">
				<c:choose>
					<c:when test="${sessionMap.userFinished}">
						<fmt:message key="message.activityLocked" />
					</c:when>
					<c:otherwise>
						<fmt:message key="message.warnLockOnFinish" />
					</c:otherwise>
				</c:choose>
			</lams:Alert5>
		</c:if>
		<c:if test="${userIsMarked && !(sessionMap.lockOnFinish and mode != 'teacher' && sessionMap.userFinished)}">
			<lams:Alert5 type="info" id="userIsMarked" close="false">
				<fmt:message key="message.spreadsheet.marked" />
			</lams:Alert5>
		</c:if>
		
		<lams:errors5/>
		
		<div id="instructions" class="instructions">
			<c:out value="${spreadsheet.instructions}" escapeXml="false"/>
		</div>

		<input type=hidden name="spreadsheetCode" id="spreadsheet-code" value="${sessionMap.code}"/>
		
		<c:if test="${spreadsheet.markingEnabled}">
			<div class="card lcard mb-3">
				<div class="card-body">
					<div class="row">
						<div class="col-2">
							<fmt:message key="label.learning.comments" />
						</div>
						<div class="col-10">
							<c:choose>
								<c:when test="${mark == null}">
									<fmt:message key="label.learning.not.available" />
								</c:when>
								<c:otherwise>
									<c:out value="${mark.comments}" escapeXml="false" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					
					<div class="row">
						<div class="col-2">
							<fmt:message key="label.learning.marks" />
						</div>
						<div class="col-10">
							<c:choose>
								<c:when test="${mark == null}">
									<fmt:message key="label.learning.not.available" />
								</c:when>
								<c:otherwise>
									<fmt:formatNumber type="number" maxFractionDigits="<%= SpreadsheetConstants.MARK_NUM_DEC_PLACES %>" value="${mark.marks}"/>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
		</c:if>		

		<div class="card lcard">
			<div class="card-body">
				<iframe
						id="externalSpreadsheet" name="externalSpreadsheet" src="<lams:WebAppURL/>includes/javascript/simple_spreadsheet/spreadsheet_offline.html?lang=${language}"
						style="width:99%;" frameborder="no" height="385px"
						scrolling="no">
				</iframe>
				
				<c:if test="${!userIsMarked && (mode != 'teacher') && (spreadsheet.learnerAllowedToSave) && !(sessionMap.lockOnFinish && sessionMap.userFinished)}">
					<div class="activity-bottom-buttons">
						<button type="button" name="SaveButton" onclick="return saveUserSpreadsheet('saveUserSpreadsheet')" class="btn btn-secondary">
							<i class="fa-regular fa-floppy-disk me-1"></i>
							<fmt:message key="label.save" />
						</button>
					</div>
				</c:if>		
			</div>
		</div>

		<c:if test="${sessionMap.userFinished and sessionMap.reflectOn}">
			<lams:NotebookReedit
				reflectInstructions="${sessionMap.reflectInstructions}"
				reflectEntry="${sessionMap.reflectEntry}"
				isEditButtonEnabled="${hasEditRight}"
				notebookHeaderLabelKey="title.reflection"/>
		</c:if>

		<c:if test="${mode != 'teacher'}">
			<div class="activity-bottom-buttons">
				<c:choose>
					<c:when	test="${sessionMap.reflectOn && (not sessionMap.userFinished)}">				
						<button type="button" name="FinishButton" onclick="return continueReflect()" class="btn btn-primary na">
							<fmt:message key="label.continue" />
						</button>
					</c:when>
					<c:otherwise>
						<button type="button" class="btn btn-primary na" id="finishButton">
							<c:choose>
			 					<c:when test="${sessionMap.isLastActivity}">
			 						<fmt:message key="label.submit" />
			 					</c:when>
			 					<c:otherwise>
			 		 				<fmt:message key="label.finished" />
			 					</c:otherwise>
			 				</c:choose>
						</button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>

	</div>
</lams:PageLearner>
