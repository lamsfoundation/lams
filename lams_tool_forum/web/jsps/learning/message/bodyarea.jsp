<%@ include file="/common/taglibs.jsp"%>

	<%-- for validateForm() method --%>
	<input type="hidden" name="minCharactersEnabled" id="min-characters-enabled" value="${sessionMap.minCharacters > 0}"/>

	<c:choose>
		<c:when test="${sessionMap.allowRichEditor}">
			<lams:CKEditor id="message.body" value="${messageForm.message.body}" 
					contentFolderID="${sessionMap.learnerContentFolder}" toolbarSet="DefaultLearner">
			</lams:CKEditor>
		</c:when>
		
		<c:otherwise>
			<%-- Does not use general tag because this field need keep compatible with CKEditor's content --%>
			<lams:textarea name="message.body" class="form-control" tabindex="2" rows="10">${messageForm.message.body}</lams:textarea>
		</c:otherwise>
	</c:choose>
 
	<c:if test="${sessionMap.allowAnonym || sessionMap.maxCharacters > 0 || sessionMap.minCharacters > 0}">
	<div class="row mt-2">

	<div class="col-12 col-sm-6">
	
	<%-- If limitChars == 0, then we don't want to limit the characters at all. --%>
	<c:if test="${sessionMap.maxCharacters > 0}">

		<fmt:message key="lable.char.left" />: <span id="char-left-div"></span>
		<input type="hidden" name="limitCount" id="max-limit-count" />
				
		<script type="text/javascript">
		$(document).ready(function() {
			
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

		});
		</script>
	</c:if>

	<c:if test="${sessionMap.minCharacters > 0}">
		<fmt:message key="label.char.required" />: <span id="char-required-div"></span>
			
		<script type="text/javascript">
		$(document).ready(function() {

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

		});
		</script>			
	</c:if>
	</div>
	
	<div class="col-12 col-sm-6 text-end">
		<c:if test="${sessionMap.allowAnonym}">
			<div class="checkbox form-control-inline form-check">
				<form:checkbox path="message.isAnonymous" id="isAnonymous" cssClass="form-check-input"/> 
				
				<label for="isAnonymous" class="form-check-label">	
					<fmt:message key="label.post.anonomously" />
				</label>
			</div>
			&nbsp;
			<a tabindex="0" role="button" data-bs-toggle="popover">
				<i class="fa fa-info-circle"></i>
			</a>
		
			<%-- Use c:out to escape any quotes in the I18N string. Then use html: true converts any escaped quotes back --%>
			<%-- into real quotes. Should be safe from XSS attack as the string is coming from a translation file. --%>	
			<fmt:message key="label.anonymous.tooltip" var="ANONYMOUS_TOOLTIP_VAR"></fmt:message>
					
	 		<script type="text/javascript">
			$(document).ready(function() {
				var ANONYMOUS_TOOLTIP = '<c:out value="${ANONYMOUS_TOOLTIP_VAR}" />';
		    		$('[data-bs-toggle="popover"]').popover({title: "", content: ANONYMOUS_TOOLTIP, placement:"auto left", delay: 50, trigger:"hover focus", html: true});
			});
			</script>
	 	</c:if>
	</div>

	</div> <!-- end row -->
	</c:if>
	
	<lams:errors path="message.body"/>