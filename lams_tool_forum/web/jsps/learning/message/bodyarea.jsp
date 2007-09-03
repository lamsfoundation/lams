<%@ include file="/includes/taglibs.jsp"%>

<div>
<c:choose>
	<c:when test="${sessionMap.allowRichEditor}">
		<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
		<lams:FCKEditor id="message.body"
				value="${formBean.message.body}"
				toolbarSet="Default-Learner"></lams:FCKEditor>
	</c:when>
	<c:otherwise>
		<%-- Does not user general tag becuase this field need keep compatible with FCKEditor's content --%>
		<lams:STRUTS-textarea rows="10" cols="60" tabindex="2" property="message.body" styleId="messageBody" />
		<BR>
		<%-- If limitChars == 0, then we don't want to limit the characters at all. --%>
		<c:if test="${sessionMap.limitedChars > 0}">
			<fmt:message key="lable.char.left" />:  
			<span id="charleftDiv"></span>
			<input type="hidden" name="limitCount" id="limitCount" />
			<script type="text/javascript">
					<!--
					var limit = <c:out value="${sessionMap.limitedChars}"/>;
					var bodyTxt = document.getElementById("messageBody");
					var limitCount = document.getElementById("limitCount");
					
					var showDiv = document.getElementById("charleftDiv");
					limitCount.value=limit - bodyTxt.value.length;
					showDiv.innerHTML=limitCount.value;
					function calculateLeft(){
						if(this.value.length > limit){
							this.value = this.value.substring(0,limit);
							//fix a bug: when change "this.value", onchange event won't be fired any more. So this will 
							//manually handle onchange event. It is a kind of crack coding!
							filterData(document.getElementById('messageBody'),document.getElementById('message.body__lamshidden'));
						}else{
							limitCount.value = limit - this.value.length;
							showDiv.innerHTML=limitCount.value;							
						}
					}
					
					bodyTxt.onkeydown = calculateLeft;
					bodyTxt.onkeyup = calculateLeft;
					-->
			</script>
		</c:if>
	</c:otherwise>
</c:choose>
<BR>
<html:errors property="message.body" />

<div>
