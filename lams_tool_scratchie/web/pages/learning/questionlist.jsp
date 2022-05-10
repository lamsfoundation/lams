<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams">
	<lams:LAMSURL />
</c:set>

<!-- Used by TestHarness 
	 isUserLeader=${isUserLeader}
	 hideFinishButton=${hideFinishButton} -->

<form id="burning-questions" name="burning-questions" method="post" action="">
	<%@ include file="scratchies.jsp"%>
	
	<%-- show general burning question --%>
	<c:if test="${isUserLeader && scratchie.burningQuestionsEnabled || (mode == 'teacher')}">
		<div class="form-group burning-question-container">
			<a data-toggle="collapse" data-target="#burning-question-general" href="#bqg"
					<c:if test="${empty sessionMap.generalBurningQuestion}">class="collapsed"</c:if>>
				<span class="if-collapsed"><i class="fa fa-xs fa-plus-square-o roffset5" aria-hidden="true"></i></span>
	  			<span class="if-not-collapsed"><i class="fa fa-xs fa-minus-square-o roffset5" aria-hidden="true"></i></span>
				<fmt:message key="label.general.burning.question" />
			</a>
	
			<div id="burning-question-general" class="collapse <c:if test="${not empty sessionMap.generalBurningQuestion}">in</c:if>">
				<textarea rows="5" name="generalBurningQuestion" class="form-control"
					<c:if test="${mode == 'teacher'}">disabled="disabled"</c:if>
				>${sessionMap.generalBurningQuestion}</textarea>
			</div>
		</div>
	</c:if>
</form>

<c:if test="${mode != 'teacher'}">
	<div class="voffset10 pull-right">
		<c:choose>
			<c:when test="${isUserLeader && sessionMap.reflectOn}">
				<input type="hidden" name="method" id="method" value="newReflection">
				<button nae="finishButton" id="finishButton" onclick="return finish(false);" class="btn btn-default">
					<fmt:message key="label.continue" />
				</button>
			</c:when>
			<c:otherwise>
				<input type="hidden" name="method" id="method" value="showResults">
				<button name="finishButton" id="finishButton" onclick="return finish(false);" class="btn btn-default">
					<fmt:message key="label.submit" />
				</button>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>