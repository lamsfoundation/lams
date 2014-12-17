<%@ include file="/common/taglibs.jsp"%>

<div>
<c:choose>
	<c:when test="${sessionMap.allowRichEditor}">
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<lams:CKEditor id="message.body" value="${formBean.message.body}" contentFolderID="${sessionMap.learnerContentFolder}"
				toolbarSet="DefaultLearner">
		</lams:CKEditor>
	</c:when>
	
	<c:otherwise>
	
		<%-- Does not user general tag becuase this field need keep compatible with CKEditor's content --%>
		<lams:STRUTS-textarea rows="10" cols="60" tabindex="2" property="message.body" styleId="messageBody" />
		<BR>
		
		<%-- If limitChars == 0, then we don't want to limit the characters at all. --%>
		<c:if test="${sessionMap.maxCharacters > 0}">
			<fmt:message key="lable.char.left" />: <span id="char-left-div"></span>
			<input type="hidden" name="limitCount" id="max-limit-count" />
			<br>
				
			<script type="text/javascript">
				var limit = <c:out value="${sessionMap.maxCharacters}"/>;
				var bodyTxt = document.getElementById("messageBody");
				var limitCount = document.getElementById("max-limit-count");

				var showDiv = document.getElementById("char-left-div");
				var count = limit - bodyTxt.value.length;
				limitCount.value = count;
				showDiv.innerHTML = count;
				function calculateLeft() {
					if (this.value.length > limit) {
						this.value = this.value.substring(0, limit);
						//fix a bug: when change "this.value", onchange event won't be fired any more. So this will 
						//manually handle onchange event. It is a kind of crack coding!
						filterData(
								document.getElementById('messageBody'),
								document
										.getElementById('message.body__lamshidden'));
						
					} else {
						var count = limit - this.value.length;
						limitCount.value = count;
						showDiv.innerHTML = count;
					}
				}

				bodyTxt.onkeydown = calculateLeft;
				bodyTxt.onkeyup = calculateLeft;
			</script>
		</c:if>
			
		<c:if test="${sessionMap.minCharacters > 0}">
			<fmt:message key="label.char.required" />: <span id="char-required-div"></span>
			<input type="hidden" name="limitCount" id="min-limit-count" />
			
			<script type="text/javascript">
			document.observe("dom:loaded", function() {
				var limit = <c:out value="${sessionMap.minCharacters}"/>;
				var bodyTxt = document.getElementById("messageBody");
				var button = document.getElementById("submit-button");
				var limitCount = document.getElementById("min-limit-count");
				var showDiv = document.getElementById("char-required-div");
				
				function calculateRequired() {
					
					if (bodyTxt.value.length >= limit) {
						button.style.visibility="visible";
					} else {
						button.style.visibility="hidden";
					}
					
					var count = (limit - bodyTxt.value.length) > 0 ? limit - bodyTxt.value.length : 0;
					limitCount.value = count;
					showDiv.innerHTML = count;
				}
				calculateRequired();

				bodyTxt.onkeyup = calculateRequired;
			});
			</script>			
		</c:if>
	</c:otherwise>
</c:choose>
<BR>
<html:errors property="message.body" />

<div>
