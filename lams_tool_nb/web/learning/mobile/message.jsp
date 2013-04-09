<%@ include file="/includes/taglibs.jsp"%>



<script type="text/javascript">
	
	function disableFinishButton() {
		var elem = document.getElementById("finishButton");
		elem.disabled = true;
	}
	 function submitForm(methodName){
                var f = document.getElementById('messageForm');
                f.submit();
        }
</script>


<div data-role="header" data-theme="b" data-nobackbtn="true">
	<h1>
		<fmt:message key="activity.title" />
	</h1>
</div>
	
<div data-role="content">
	<logic:messagesPresent message="true">
		<html:messages id="message" message="true">
			<p>
				<bean:write name="message" />
			</p>
		</html:messages>
	</logic:messagesPresent>
</div>
	
<div data-role="footer" data-theme="b" class="ui-bar">
	<span class="ui-finishbtn-right">
		<html:form action="/learner" target="_self" onsubmit="disableFinishButton();" styleId="messageForm">
			<html:hidden property="toolSessionID" />
			<html:hidden property="mode" />
			<html:hidden property="method" value="finish"/>
			<c:choose>
				<c:when test="${reflectOnActivity}">
					<button type="submit" name="method" data-icon="arrow-r" data-theme="b">
						<fmt:message key="button.continue" />
					</button>
				</c:when>
				<c:otherwise>
					<a href="#nogo" id="finishButton" onclick="submitForm('finish')" data-role="button" data-icon="arrow-r" data-theme="b">
					<span class="nextActivity">
							<c:choose>
								<c:when test="${activityPosition.last}">
									<fmt:message key="button.submit" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.finish" />
								</c:otherwise>
							</c:choose>
					</span> </a>
				</c:otherwise>
			</c:choose>
		</html:form>
	</span>
</div>

</div>
