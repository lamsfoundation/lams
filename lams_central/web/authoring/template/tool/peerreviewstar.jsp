<%@ include file="/common/taglibs.jsp"%>
<%-- Generic Peer Review Criteria page. Expects an input of criteriaNumber, and creates a field named peerreview${criteriaNumber} suitable for a star rating criteria entry --%>
<%-- Must include a global variable "var minimumWordsSpinnerArray  = new Array();" in the main javascript for the enclosing page. --%>

	<div class="voffset10">
		<div>
		<span class="field-name">
			<label for="peerreview"><fmt:message key="authoring.label.peerrevice.criteria.num"><fmt:param value="${criteriaNumber}"/></fmt:message></label>
		</span>
		<input name="peerreview${criteriaNumber}" id="peerreview${criteriaNumber}" class="form-control" type="text" maxlength="200"/>
		</div>
		
		<div>
		<label for="peerreview${criteriaNumber}EnableComments">
			<input type="checkbox" name="peerreview${criteriaNumber}EnableComments" value="true" class="form-control-inline" id="peerreview${criteriaNumber}EnableComments"/>&nbsp;
			<fmt:message key="authoring.label.peerrevice.min.words.in.comments"/>
		</label>
		<input name="peerreview${criteriaNumber}MinWordsLimit" id="peerreview${criteriaNumber}MinWordsLimit" type="number" step="1" min="1" value="1" size="3"  class="inputclass form-control input-sm form-control-inline voffset5" disabled/>
		</div>
	</div>
	
	<script type="text/javascript">
	 	$('#peerreview${criteriaNumber}EnableComments').click(function() {
			$('#peerreview${criteriaNumber}MinWordsLimit').prop('disabled', ! this.checked);
	     });
	</script>
	