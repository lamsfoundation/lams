<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>

<!-- Used by TestHarness 
	 isUserLeader=${isUserLeader}
	 hideFinishButton=${hideFinishButton} -->

<form id="burning-questions" name="burning-questions" method="post" action="">
	<%@ include file="scratchies.jsp"%>
	
	<%-- show general burning question --%>
	<c:if test="${isUserLeader && scratchie.burningQuestionsEnabled || (mode == 'teacher')}">
		<div class="burning-question-container mb-3">
			<button type="button" data-bs-toggle="collapse" data-bs-target="#burning-question-general"
					class="btn btn-light btn-sm mb-2 <c:if test="${empty sessionMap.generalBurningQuestion}">collapsed</c:if>">
				<span class="if-collapsed">
					<i class="fa fa-xs fa-regular fa-square-plus" aria-hidden="true"></i>
				</span>
	  			<span class="if-not-collapsed">
	  				<i class="fa fa-xs fa-regular fa-square-minus" aria-hidden="true"></i>
	  			</span>
				<fmt:message key="label.general.burning.question" />
			</button>
	
			<div id="burning-question-general" class="collapse <c:if test="${not empty sessionMap.generalBurningQuestion}">show</c:if>" pt-2>
				<textarea rows="5" name="generalBurningQuestion" class="form-control"
					<c:if test="${mode == 'teacher'}">disabled="disabled"</c:if>
				>${sessionMap.generalBurningQuestion}</textarea>
			</div>
		</div>
	</c:if>
</form>

<c:if test="${mode != 'teacher'}">
	<div class="activity-bottom-buttons">
		<c:choose>
			<c:when test="${isUserLeader && sessionMap.reflectOn}">
				<input type="hidden" name="method" id="method" value="newReflection">
				<button name="finishButton" id="finishButton" onclick="return finish(false);" class="btn btn-primary na">
					<fmt:message key="label.continue" />
				</button>
			</c:when>
			<c:otherwise>
				<input type="hidden" name="method" id="method" value="showResults">
				<button name="finishButton" id="finishButton" onclick="return finish(false);" class="btn btn-primary na">
					<fmt:message key="label.submit" />
				</button>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>
