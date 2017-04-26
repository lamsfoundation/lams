<%@ include file="/common/taglibs.jsp"%>
<c:set var="isWordsLimitEnabled" value="${hasEditRight && (question.maxWordsLimit != 0 || question.minWordsLimit != 0)}"/>

<c:if test="${isWordsLimitEnabled}">
<script type="text/javascript">
	
	$(document).ready(function() {
		
		//word count
		var counter${status.index} = function() {
			var isCkeditor = ${question.allowRichEditor};
			
			var value;
			if (isCkeditor) {
				//if (!CKEDITOR.instances.question${status.index}.checkDirty()) {
				//	return;
				//}
			    value = CKEDITOR.instances.question${status.index}.getData();
			    
			} else {
				value =  $("#question${status.index}__lamstextarea").val();
			}
		    
			var wordCount = getNumberOfWords(value, isCkeditor);
			$('#word-count${status.index}').html(wordCount);
			
		    var maxWordsLimit = ${question.maxWordsLimit};
		    if(wordCount > maxWordsLimit){
				//$('#text').val()  this.value = this.value.substring(0,limit);
				//fix a bug: when change "this.value", onchange event won't be fired any more. So this will 
				//manually handle onchange event. It is a kind of crack coding!
				//filterData(document.getElementById('messageBody'),document.getElementById('message.body__lamshidden'));
				//onchange="filterData(this,document.getElementById('question1__lamshidden'));"
			}
			//filterData(document.getElementById('question1__lamstextarea'),document.getElementById('question1__lamshidden'));
		};
		
		if (${question.allowRichEditor}) {
			CKEDITOR.instances["question${status.index}"].on("instanceReady", function(){    
			     this.on("change", counter${status.index});
			});
			//count words initially
		    CKEDITOR.instances["question${status.index}"].on('instanceReady', counter${status.index});
		      
		} else {
			$("#question${status.index}__lamstextarea").on('change keydown keypress keyup paste', counter${status.index});
			//count words initially
			counter${status.index}();
		}

	});
</script>
</c:if>

<div class="question-type">
	<fmt:message key="label.learning.short.answer.answer" />
</div>

<div class="table-responsive">
	<table class="table table-hover table-condensed">
		<c:if test="${isWordsLimitEnabled}">
			<tr>
				<td>
	
					<c:choose>
						<c:when test="${question.maxWordsLimit != 0 && question.minWordsLimit != 0}">
							<div class="reg-info">
								<fmt:message key="label.info.max.and.min.number.words" >
									<fmt:param>${question.minWordsLimit}</fmt:param>
									<fmt:param>${question.maxWordsLimit}</fmt:param>
								</fmt:message>
							</div>
						</c:when>
						<c:when test="${question.maxWordsLimit != 0}">
							<div class="reg-info">
								<fmt:message key="label.info.maximum.number.words" >
									<fmt:param>${question.maxWordsLimit}</fmt:param>
								</fmt:message>
							</div>
						</c:when>
						<c:when test="${question.minWordsLimit != 0}">
							<div class="reg-info">
								<fmt:message key="label.info.minimum.number.words" >
									<fmt:param>${question.minWordsLimit}</fmt:param>
								</fmt:message>
							</div>
						</c:when>
					</c:choose>
				</td>
			</tr>
		</c:if>
	
		<tr>
			<td>
				<c:choose>
					<c:when test="${question.allowRichEditor && hasEditRight}">
						<lams:CKEditor id="question${status.index}" value="${question.answerString}" contentFolderID="${sessionMap.learnerContentFolder}" toolbarSet="DefaultLearner"></lams:CKEditor>
					</c:when>
					<c:otherwise>
						<lams:STRUTS-textarea property="question${status.index}" styleClass="form-control" value="${question.answerString}" disabled="${!hasEditRight}" />
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		
		<c:if test="${isWordsLimitEnabled}">
			<tr>
				<td>
					<fmt:message key="label.words" /> <span id="word-count${status.index}">0</span>
				</td>
			</tr>
		</c:if>
	</table>
</div>
