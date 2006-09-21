	<script type="text/javascript">
	<!--
		
		function finishSession(){
			document.location.href ='<c:url value="/learning/finish.do?sessionMapID=${sessionMapID}&mode=${mode}&toolSessionID=${toolSessionID}"/>';
			return false;
		}
		function continueReflect(){
			document.location.href='<c:url value="/learning/newReflection.do?sessionMapID=${sessionMapID}"/>';
		}
		
	-->        
    </script>

		<c:if test="${mode != 'teacher'}">
			<div class="right-buttons">
				<c:choose>
					<c:when test="${sessionMap.reflectOn}">
						<html:button property="FinishButton"  onclick="return continueReflect()" disabled="${finishedLock}"  styleClass="button">
							<fmt:message key="label.continue" />
						</html:button>
					</c:when>
					<c:otherwise>
						<html:button property="FinishButton"  onclick="return finishSession()" disabled="${finishedLock}"  styleClass="button">
							<fmt:message key="label.finished" />
						</html:button>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>
