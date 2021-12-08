<%@ include file="/common/taglibs.jsp"%>
<c:set var="isWordsLimitEnabled" value="${hasEditRight && (question.maxWordsLimit != 0 || question.minWordsLimit != 0)}"/>

<c:if test="${isWordsLimitEnabled and not question.allowRichEditor}">
<script type="text/javascript">
	
	$(document).ready(function() {
		$("#essay-question${status.index}").on('change keydown paste', function(event){
			var newText = '';
			// if it is a single key typed
			if (event.type == 'keydown') {
				// try to figure out if it is a printable, standard character
				if (!event.originalEvent.ctrlKey && event.originalEvent.key.length === 1){
					newText = String.fromCharCode(event.originalEvent.keyCode);
					if (!/^[a-zA-Z0-9\u0600-\u06FF\u0660-\u0669\u06F0-\u06F9 _.-]$/.test(newText)) {
						newText = '';
					}
				}
			} else if (event.type == 'paste') {
				// get pasted data
				newText = event.originalEvent.clipboardData || window.clipboardData;
				newText = newText.getData('Text');
			}
			
			var value = $("#essay-question${status.index}").val() + newText,
		    	wordCount = getNumberOfWords(value),
		    	maxWordsLimit = ${question.maxWordsLimit};
		    	
			    if (maxWordsLimit > 0){
				    var questionArea = $(this).closest('.question-area'),
				    	warning = $('.max-word-limit-exceeded', questionArea);
				    if (wordCount > maxWordsLimit){
					    // prevent new text from being entered
					    event.preventDefault();
					    questionArea.addClass('bg-warning');
						warning.slideDown();
				    	return false;
					} else {
						questionArea.removeClass('bg-warning');
						warning.slideUp();
					}
			    }
			
			$('#word-count${status.index}').html(wordCount);
		}).change();
	});
</script>
</c:if>

<c:if test="${not empty question.codeStyle}">
	<script type="text/javascript">
		// initialise syntax highlighter depending on programming language
		$(document).ready(function() {
			var codeArea = $('#essay-question${status.index}'),
				codeMirror = CodeMirror.fromTextArea(codeArea[0], {
					'mode' : '${question.codeStyleMime}'
				}),
				// on the back end we keep essay answer lines <BR>-separated, but CodeMirror uses \n
				content = codeMirror.getValue().replaceAll('<BR>', '\n');
			codeMirror.setValue(content);
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
							<div class="reg-info max-word-limit-exceeded text-danger">
								<fmt:message key="warn.maximum.number.words" />
							</div>
						</c:when>
						<c:when test="${question.maxWordsLimit != 0}">
							<div class="reg-info">
								<fmt:message key="label.info.maximum.number.words" >
									<fmt:param>${question.maxWordsLimit}</fmt:param>
								</fmt:message>
							</div>
							<div class="reg-info max-word-limit-exceeded text-danger">
								<fmt:message key="warn.maximum.number.words" />
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
						<lams:CKEditor id="question${status.index}" value="${question.answer}" contentFolderID="${sessionMap.learnerContentFolder}" toolbarSet="DefaultLearner" height="174px" maxWords="${question.maxWordsLimit}"></lams:CKEditor>
					</c:when>
					<c:when test="${not empty question.codeStyle}">
						<textarea id="essay-question${status.index}" name="question${status.index}">${question.answer}</textarea>
					</c:when>
					<c:when test="${not hasEditRight}">
						${question.answer}
					</c:when>
					<c:otherwise>
						<lams:textarea id="essay-question${status.index}" name="question${status.index}" class="form-control" rows="8">${question.answer}</lams:textarea>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		
		<c:if test="${isWordsLimitEnabled and not question.allowRichEditor}">
			<tr>
				<td>
					<fmt:message key="label.words" /> <span id="word-count${status.index}">0</span>
				</td>
			</tr>
		</c:if>
	</table>
</div>
