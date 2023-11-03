<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	$(document).ready(function() {
		<c:if test="${sessionMap.maxCharacters > 0}">
	
			// Whether content has exceeded the maximum characters.
			var isNewlyInstantiated = true;
			var isCkeditor = ${sessionMap.allowRichEditor};
			var limit = <c:out value="${sessionMap.maxCharacters}"/>;
			var ckeditor = (isCkeditor) ? CKEDITOR.instances["message.body"] : null;
			
			//character count fuction
			var counter = function(evt) {
				
				var value = isCkeditor ? ckeditor.getSnapshot() 
						: $('textarea[id="message.body"]').val();
				var charactersCount = getNumberOfCharacters(value, isCkeditor);
				
				//limit is not exceeded
				if (charactersCount <= limit || isNewlyInstantiated) {
					
					isNewlyInstantiated = false;

					if (isCkeditor) {
						ckeditor.fire( 'updateSnapshot' );	
					}
					
					var count = (limit - charactersCount) > 0 ? limit - charactersCount : 0;
					$('#max-limit-count').val(count);
					$('#char-left-div').html(count);
				
				//limit exceeded in case of CKEditor
				} else if (isCkeditor) {
					//detect key pressed
					var key = ((evt.data === undefined) || (evt.data.$ === undefined)) ? null : evt.data.$.keyCode || evt.data.$.charCode;	
					
					//don't block backspace and del 
					if (key == 8 || key == 46) {
						return;
					}
					
					//evt.cancel();
					ckeditor.execCommand( 'undo' );
					
				//limit exceeded in case of textarea 
				} else {
					this.value = this.value.substring(0, limit);
					//fix a bug: when change "this.value", onchange event won't be fired any more. So this will 
					//manually handle onchange event. It is a kind of crack coding!
					filterData(document.getElementById('message.body'),document.getElementById('message.body__lamshidden'));						
				}
			};
			
			//assign function
			if (isCkeditor) {
			    // @todo Make this more elegant (.on('change') once we upgrade to Ckeditor 4
			    //ckeditor.on('key', counter);
			    ckeditor.on('paste', counter);
				ckeditor.on('afterCommandExec', counter);
				ckeditor.on("instanceReady", function(){                    
				     this.document.on("keyup", counter);
				});
				//count characters initially
			    ckeditor.on('instanceReady', counter);
			      
			} else {
				$('textarea[id="message.body"]').on('change keydown keypress keyup paste', counter);
				//count characters initially
				counter();
			}
		</c:if>

		<c:if test="${sessionMap.minCharacters > 0}">
			var isCkeditor = ${sessionMap.allowRichEditor};
			var ckeditor = isCkeditor ? CKEDITOR.instances["message.body"] : null;
			
			//character count fuction
			var counter = function() {
				var value = isCkeditor ? ckeditor.getSnapshot() 
						: $('textarea[id="message.body"]').val();
			    
				var charactersCount = getNumberOfCharacters(value, isCkeditor);
				
				var limit = <c:out value="${sessionMap.minCharacters}"/>;
				var count = (limit - charactersCount) > 0 ? limit - charactersCount : 0;
				$('#char-required-div').html(count);
			};
			
			//assign function
			if (isCkeditor) {
			    // @todo Make this more elegant (.on('change') once we upgrade to Ckeditor 4 
			    ckeditor.on('paste', counter);
				ckeditor.on('afterCommandExec', counter);
				ckeditor.on("instanceReady", function(){                    
				     this.document.on("keyup", counter);
				});
				//count characters initially
			    ckeditor.on('instanceReady', counter);
			      
			} else {
				$('textarea[id="message.body"]').on('change keydown keypress keyup paste', counter);
				//count characters initially
				counter();
			}	
		</c:if>

		<c:if test="${sessionMap.allowAnonym}">
			$('[data-bs-toggle="popover"]').each((i, el) => {
				new bootstrap.Popover($(el), {
					content: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.anonymous.tooltip"/></spring:escapeBody>',  
					delay: 50, 
					html: true
				})
			});
		</c:if>
	});
</script>

<%-- for validateForm() method --%>
<input type="hidden" name="minCharactersEnabled" id="min-characters-enabled" value="${sessionMap.minCharacters > 0}"/>

<c:choose>
	<c:when test="${sessionMap.allowRichEditor}">
		<lams:CKEditor id="message.body"
			value="${messageForm.message.body}"
			contentFolderID="${sessionMap.learnerContentFolder}"
			toolbarSet="DefaultLearner" 
			ariaLabelledby="label-body"
			isRequired="true"
			></lams:CKEditor>
	</c:when>
		
	<c:otherwise>
		<%-- Does not use general tag because this field need keep compatible with CKEditor's content --%>
		<lams:textarea id="message.body"
			name="message.body" class="form-control" rows="10"
			aria-labelledby="label-body"
			aria-required="true"
		>${messageForm.message.body}</lams:textarea>
	</c:otherwise>
</c:choose>
 
<c:if test="${sessionMap.maxCharacters > 0 || sessionMap.minCharacters > 0}">
	<div class="badge alert alert-info mt-1">
		<c:if test="${sessionMap.maxCharacters > 0}">
			<fmt:message key="lable.char.left" />: <span id="char-left-div"></span>
			<input type="hidden" name="limitCount" id="max-limit-count" />
		</c:if>
	
		<c:if test="${sessionMap.minCharacters > 0}">
			<fmt:message key="label.char.required" />: <span id="char-required-div"></span>		
		</c:if>
	</div>
</c:if>
	
<lams:errors5 path="message.body"/>