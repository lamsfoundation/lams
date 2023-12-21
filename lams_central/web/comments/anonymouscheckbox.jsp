<script type="text/javascript">
	$(document).ready(function() {
		$('[data-bs-toggle="popover"]').each((i, el) => {
			new bootstrap.Popover($(el), {
				content: '<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.anonymous.tooltip"/></spring:escapeBody>'
			})
		});
	});
</script>

<c:choose>
	<c:when test="${sessionMap.mode == 'teacher'}">
		<%-- include the value so it gets returned but do not allow it to be edited --%>
		<input type="hidden" id="${anonymousCheckboxName}" name="${anonymousCheckboxName}"
			value="${anonymousCheckboxChecked ? 'true' : 'false'}"
		/>
	</c:when>

	<c:otherwise>
		<div class="form-check form-check-reverse mt-2">
			<input type="checkbox" id="${anonymousCheckboxName}" class="form-check-input"
				name="${anonymousCheckboxName}" value="true" ${anonymousCheckboxChecked ? 'checked="checked"' : ''}
				aria-description='<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.anonymous.tooltip"/></spring:escapeBody>'
			>
				
			<label for="${anonymousCheckboxName}" class="form-check-label">
				<fmt:message key="label.post.anonomously" />
			</label>

			<span data-bs-toggle="popover" data-bs-placement="right" data-bs-trigger="hover focus" aria-hidden="true">
	  			<i class="fa fa-info-circle"></i>
			</span>
		</div>
	</c:otherwise>
</c:choose>
