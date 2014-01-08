<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	function finishActivity(){
		document.getElementById("finishButton").disabled = true;
		location.href = '<c:url value="/learning.do"/>?dispatch=finishActivity&toolSessionID=${toolSessionID}';
	}
</script>

<div data-role="header" data-theme="b" data-nobackbtn="true">
	<h1>
		${content.title}
	</h1>
</div>

<div data-role="content">
	<p>
		<fmt:message key="message.runOfflineSet" />
	</p>
</div>


<div data-role="footer" data-theme="b" class="ui-bar">
	<span class="ui-finishbtn-right">
	
		<c:if test="${mode == 'learner' || mode == 'author'}">
	
				<a href="#nogo" id="finishButton" onclick="finishActivity()" data-role="button" data-icon="arrow-r" data-theme="b">
					<span class="nextActivity">
						<c:choose>
							<c:when test="${activityPosition.last}">
								<fmt:message key="button.submit" />
							</c:when>
							<c:otherwise>
								<fmt:message key="button.finish" />
							</c:otherwise>
						</c:choose>
					</span>
				</a>
		</c:if>
		
	</span>
</div>


	



