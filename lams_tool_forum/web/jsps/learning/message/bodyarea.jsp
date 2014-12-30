<%@ include file="/common/taglibs.jsp"%>

<div>
	
	<%-- for validateForm() method --%>
	<input type="hidden" name="minCharactersEnabled" id="min-characters-enabled" value="${sessionMap.minCharacters > 0}"/>
	
	<c:if test="${sessionMap.minCharacters > 0}">
		<div class="info">
			<fmt:message key="info.minimum.number.characters" >
				<fmt:param>${sessionMap.minCharacters}</fmt:param>
			</fmt:message>
		</div>
	</c:if>

	<c:choose>
		<c:when test="${sessionMap.allowRichEditor}">
			<c:set var="formBean" value="<%= request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY) %>" />
			<lams:CKEditor id="message.body" value="${formBean.message.body}" 
					contentFolderID="${sessionMap.learnerContentFolder}" toolbarSet="DefaultLearner">
			</lams:CKEditor>
		</c:when>
		
		<c:otherwise>
			<%-- Does not user general tag becuase this field need keep compatible with CKEditor's content --%>
			<lams:STRUTS-textarea rows="10" cols="60" tabindex="2" property="message.body"/>
		</c:otherwise>
	</c:choose>
	<BR>
		
	<%-- If limitChars == 0, then we don't want to limit the characters at all. --%>
	<c:if test="${sessionMap.maxCharacters > 0}">
		<fmt:message key="lable.char.left" />: <span id="char-left-div"></span>
		<input type="hidden" name="limitCount" id="max-limit-count" />
		<br>
				
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
						: $('textarea[id="message.body__lamstextarea"]').val();
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
					filterData(document.getElementById('message.body__lamstextarea'),
							document.getElementById('message.body__lamshidden'));
						
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
				$('textarea[id="message.body__lamstextarea"]').on('change keydown keypress keyup paste', counter);
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
						: $('textarea[id="message.body__lamstextarea"]').val();
			    
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
				$('textarea[id="message.body__lamstextarea"]').on('change keydown keypress keyup paste', counter);
				//count characters initially
				counter();
			}

		});
		</script>			
	</c:if>
		
	<BR>
	<html:errors property="message.body" />

<div>
