<%@ include file="/common/taglibs.jsp"%>
		
<script type="text/javascript">
			var warning = '<fmt:message key="warn.minimum.number.characters" />';
			function validateForm() {
				//in case main characters restriction is ON check it's been fullfilled
				var isMinCharactersEnabled = $("#min-characters-enabled").val()  == "true";
				var charsMissing = $("#char-required-div").html();
							
				var isValid = !isMinCharactersEnabled || isMinCharactersEnabled && (charsMissing == "0");
				if (!isValid) {
					var warningMsg = warning.replace("{0}", charsMissing); 
					alert(warningMsg);
				}
							
				return isValid;
			}

			function getNumberOfCharacters(value, isRemoveHtmlTags) {

			    //HTML tags stripping 
				if (isRemoveHtmlTags) {
					value = value.replace(/&nbsp;/g, ' ').replace(/\n/gi, '').replace(/<\/?[a-z][^>]*>/gi, '');
				}
				
			    var wordCount = value ? (value).length : 0;
			    return wordCount;
			}
			
		//bind to pagebeforechange in order to prevent form submission (as per https://github.com/jquery/jquery-mobile/issues/729)
		$(document).bind('pageinit', function(){
			
			$('#topic-form').submit(function (e) {

			    //cache the form element for use in this function
			    var $this = $(this);

			    //prevent the default submission of the form when false is returned
			    // jquery mobile automatically triggers the submit.
			    e.preventDefault();

			    if (! validateForm()) {
			    	e.stopImmediatePropagation();
			    	return false;
			    }
			});
		});
</script>

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

	<%-- Does not user general tag because this field need keep compatible with CKEditor's content --%>
	<lams:STRUTS-textarea rows="10" cols="60" tabindex="2" property="message.body"/>
	<BR>
	
	<%-- If limitChars == 0, then we don't want to limit the characters at all. --%>
	<c:if test="${sessionMap.maxCharacters > 0}">
		<fmt:message key="lable.char.left" />: <span id="char-left-div"></span>
		<input type="hidden" name="limitCount" id="max-limit-count" />
		<br>
		
		<script type="text/javascript">
		$(document).bind('pageinit', function(){
			
			// Whether content has exceeded the maximum characters.
			var isNewlyInstantiated = true;
			var limit = <c:out value="${sessionMap.maxCharacters}"/>;
			
			//character count fuction
			var counter = function(evt) {
				
				var value = $('textarea[id="message.body__lamstextarea"]').val();
				var isCkeditor = false;
				var charactersCount = getNumberOfCharacters(value, isCkeditor);
				
				//limit is not exceeded
				if (charactersCount <= limit || isNewlyInstantiated) {
					
					isNewlyInstantiated = false;
					
					var count = (limit - charactersCount) > 0 ? limit - charactersCount : 0;
					$('#max-limit-count').val(count);
					$('#char-left-div').html(count);
					
				//limit exceeded 
				} else {
					this.value = this.value.substring(0, limit);
					//fix a bug: when change "this.value", onchange event won't be fired any more. So this will 
					//manually handle onchange event. It is a kind of crack coding!
					filterData(document.getElementById('message.body__lamstextarea'),
							document.getElementById('message.body__lamshidden'));
						
				}
			};
			
			//assign function
			$('textarea[id="message.body__lamstextarea"]').on('change keydown keypress keyup paste', counter);
			//count characters initially
			counter();

		});
		</script>
	</c:if>
	
	<c:if test="${sessionMap.minCharacters > 0}">
		<fmt:message key="label.char.required" />: <span id="char-required-div"></span>
			
		<script type="text/javascript">
		$(document).bind('pageinit', function(){
			
			//character count fuction
			var counter = function() {
				console.log("In counter"+$('textarea[id="message.body__lamstextarea"]').val());
				var value = $('textarea[id="message.body__lamstextarea"]').val();

				var isCkeditor = false;
				var charactersCount = getNumberOfCharacters(value, isCkeditor);
				
				var limit = <c:out value="${sessionMap.minCharacters}"/>;
				var count = (limit - charactersCount) > 0 ? limit - charactersCount : 0;
				$('#char-required-div').html(count);
			};
			
			//assign function
			$('textarea[id="message.body__lamstextarea"]').on('change keydown keypress keyup paste', counter);
			//count characters initially
			counter();

		});
		</script>			
	</c:if>
		
	<BR>
	<html:errors property="message.body" />
</div>
