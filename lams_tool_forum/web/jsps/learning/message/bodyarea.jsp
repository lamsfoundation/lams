	<b class="required">*</b>
		<c:choose>
			<c:when test="${allowRichEditor}">
				<FCK:editor id="message.body"
					basePath="/lams/fckeditor/" height="150" width="85%">
					<c:out value="${formBean.message.body}" escapeXml="false" />
				</FCK:editor>
			</c:when>
			<c:otherwise>
				<lams:STRUTS-textarea rows="10" cols="60" tabindex="2" property="message.body" styleId="messageBody"/> 
				<BR>
				<fmt:message key="lable.char.left"/>
				<input type="text" name="limitCount" id="limitCount" disabled="true"/>
				<script type="text/javascript">
					<!--
					var limit = <c:out value="${limitedChars}"/>;
					var bodyTxt = document.getElementById("messageBody");
					var limitCount = document.getElementById("limitCount");
					limitCount.value=limit - bodyTxt.value.length;
					function calculateLeft(){
						if(this.value.length > limit){
							this.value = this.value.substring(0,limit);
							//fix a bug: when change "this.value", onchange event won't be fired any more. So this will 
							//manually handle onchange event. It is a kind of crack coding!
							filterData(document.getElementById('messageBody'),document.getElementById('message.body__lamshidden'));
						}else
							limitCount.value = limit - this.value.length;
					}
					
					bodyTxt.onkeydown = calculateLeft;
					bodyTxt.onkeyup = calculateLeft;
					-->
				</script>
			</c:otherwise>
		</c:choose>
		<BR><html:errors property="message.body" />