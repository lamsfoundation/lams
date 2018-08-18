	<c:choose>
	<c:when test="${sessionMap.mode == 'teacher'}">
		<%-- include the value so it gets returned but do not allow it to be edited --%>
		<input type="hidden" id="${anonymousCheckboxName}" name="${anonymousCheckboxName}" value="${anonymousCheckboxChecked ? 'true' : 'false'}"/>
	</c:when>

	<c:otherwise>
		<div class="checkbox form-control-inline">
		<label for="${anonymousCheckboxName}">
		<input type="checkbox" id="${anonymousCheckboxName}" name="${anonymousCheckboxName}" value="true" ${anonymousCheckboxChecked ? 'checked="checked"' : ''}><fmt:message key="label.post.anonomously" />
		</label>
		</div>&nbsp;<a tabindex="0" role="button" data-toggle="popover"><i class="fa fa-info-circle"></i></a>
 		<%-- Use c:out to escape any quotes in the I18N string. Then use html: true converts any escaped quotes back --%>
		<%-- into real quotes. Should be safe from XSS attack as the string is coming from a translation file. --%>	
		<fmt:message key="label.anonymous.tooltip" var="ANONYMOUS_TOOLTIP_VAR"></fmt:message>		
 		<script type="text/javascript">
		$(document).ready(function() {
			var ANONYMOUS_TOOLTIP = '<c:out value="${ANONYMOUS_TOOLTIP_VAR}" />';
	    		$('[data-toggle="popover"]').popover({title: "", content: ANONYMOUS_TOOLTIP, placement:"auto right", delay: 50, trigger:"hover focus", html: true});
		});
		</script>
	</c:otherwise>
	</c:choose>
