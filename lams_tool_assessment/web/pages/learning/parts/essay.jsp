<%@ include file="/common/taglibs.jsp"%>
<c:set var="isWordsLimitEnabled" value="${hasEditRight && (question.maxWordsLimit != 0 || question.minWordsLimit != 0)}"/>

<script type="text/javascript">
<c:if test="${isWordsLimitEnabled}">
	$(document).ready(function() {
		var isCkeditor = ${question.allowRichEditor};
			
		if (isCkeditor) {
			var ckeditor = CKEDITOR.instances['question${questionIndex}'];
			// each event needs to be specified separately
			ckeditor.on('change', function(){
				$('#word-count${questionIndex}').html(this.wordCount.wordCount);
			});
			ckeditor.on('paste', function(){
				$('#word-count${questionIndex}').html(this.wordCount.wordCount);
			});
			ckeditor.on('blur', function(){
				$('#word-count${questionIndex}').html(this.wordCount.wordCount);
			});
			
			// trigger count after load
			ckeditor.on('instanceReady', function() {
				ckeditor.fire('change');
			});
		} else {
			$("#essay-question${questionIndex}").on('change keyup keydown paste', function(event){
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
				
				var value = $("#essay-question${questionIndex}").val() + newText,
			    	wordCount = getNumberOfWords(value),
			    	maxWordsLimit = ${question.maxWordsLimit};
			    	
				    if (newText && maxWordsLimit > 0){
					    var questionArea = $(this).closest('.question-area'),
					    	warning = $('.max-word-limit-exceeded', questionArea);
					    if (wordCount > maxWordsLimit){
						    // prevent new text from being entered
						    event.preventDefault();
						    if (!warning.is(':visible')) {
						    	questionArea.addClass('bg-warning');
								warning.slideDown(function(){
									setTimeout(function() {
										questionArea.removeClass('bg-warning');
										warning.slideUp();
									}, 5000);
								});
						    }
					    	return false;
						}
				    }
				
				$('#word-count${questionIndex}').html(wordCount);
			}).change();
		}
	});
</c:if>

	<c:if test="${not empty question.codeStyle}">
		// initialise syntax highlighter depending on programming language
		$(document).ready(function() {
			var codeArea = $('#essay-question${questionIndex}'),
				codeMirror = CodeMirror.fromTextArea(codeArea[0], {
					'mode' : '${question.codeStyleMime}'
				}),
				// on the back end we keep essay answer lines <BR>-separated, but CodeMirror uses \n
				content = codeMirror.getValue().replaceAll('<BR>', '\n');
			codeMirror.setValue(content);
		});
	</c:if>
</script>

<div class="card-subheader" id="instructions-${questionIndex}">
	<label for="essay-question${questionIndex}">
		<fmt:message key="label.learning.short.answer.answer" />
	</label>
</div>

<div>
	<c:if test="${isWordsLimitEnabled}">
		<c:choose>
			<c:when test="${question.maxWordsLimit != 0 && question.minWordsLimit != 0}">
				<div class="alert" role="alert">
					<span class="alert alert-info">
						<fmt:message key="label.info.max.and.min.number.words">
							<fmt:param>${question.minWordsLimit}</fmt:param>
							<fmt:param>${question.maxWordsLimit}</fmt:param>
						</fmt:message>
					</span>
				</div>
				
				<div class="alert max-word-limit-exceeded" role="alert">
					<span class="alert alert-danger">
						<fmt:message key="warn.maximum.number.words" />
					</span>
				</div>
			</c:when>
			
			<c:when test="${question.maxWordsLimit != 0}">
				<div class="alert" role="alert">
					<span class="alert alert-info">
						<fmt:message key="label.info.maximum.number.words">
							<fmt:param>${question.maxWordsLimit}</fmt:param>
						</fmt:message>
					</span>
				</div>
				
				<div class="alert max-word-limit-exceeded" role="alert">
					<span class="alert alert-danger">
						<fmt:message key="warn.maximum.number.words" />
					</span>
				</div>
			</c:when>
			
			<c:when test="${question.minWordsLimit != 0}">
				<div class="alert" role="alert">
					<span class="alert alert-info">
						<fmt:message key="label.info.minimum.number.words">
							<fmt:param>${question.minWordsLimit}</fmt:param>
						</fmt:message>
					</span>
				</div>
			</c:when>
		</c:choose>
	</c:if>

	<div>
		<c:choose>
			<c:when test="${question.allowRichEditor && hasEditRight}">
				<lams:CKEditor id="question${questionIndex}"
					value="${question.answer}"
					contentFolderID="${sessionMap.learnerContentFolder}"
					toolbarSet="DefaultLearner" height="174px"
					maxWords="${question.maxWordsLimit}"
					ariaLabelledby="question-title-${questionIndex} instructions-${questionIndex}"
					isRequired="${question.answerRequired}"
					></lams:CKEditor>
			</c:when>
			
			<c:when test="${not empty question.codeStyle}">
				<textarea id="essay-question${questionIndex}" name="question${questionIndex}"
					aria-labelledby="question-title-${questionIndex} instructions-${questionIndex}"
					${question.answerRequired? 'aria-required="true" required="true"' : ''}
				>${question.answer}</textarea>
			</c:when>
			
			<c:when test="${not hasEditRight}">
				${question.answer}
			</c:when>
			
			<c:otherwise>
				<lams:textarea id="essay-question${questionIndex}"
					name="question${questionIndex}" class="form-control" rows="8"
					aria-labelledby="question-title-${questionIndex} instructions-${questionIndex}"
					aria-describedby="question-description-${question.uid}"
					aria-required="${question.answerRequired}"
				>${question.answer}</lams:textarea>
			</c:otherwise>
		</c:choose>
	</div>

	<c:if test="${isWordsLimitEnabled}">
		<div class="mt-1">
			<fmt:message key="label.words" /> <span id="word-count${questionIndex}">0</span>
		</div>
	</c:if>
</div>
