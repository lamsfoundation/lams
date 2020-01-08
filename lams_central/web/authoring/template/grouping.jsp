<%@ include file="/common/taglibs.jsp"%>

	<div class="panel panel-default">
	<div class="panel-heading"><div class="panel-title">
		<fmt:message key="authoring.label.grouping" />
	</div></div>
	<div class="panel-body">	

		<select name="grouping" id="grouping" title="<fmt:message key="authoring.label.grouping" />" class="form-control form-control-sm form-control-inline">
			<option value="1"><fmt:message key="authoring.label.grouping.random.allocation" /></option>
			<option value="2" selected><fmt:message key="authoring.label.grouping.teachers.choice" /></option>
			<option value="4"><fmt:message key="authoring.label.grouping.learners.choice" /></option>
		</select>
		&nbsp;
		<input type="radio" name="groupControl" value="numGroups" id="groupControlGroups" checked> 
		<label for="groupControlGroups"><fmt:message key="authoring.label.numgroups"/></label> 
		<input name="numGroups" id="numGroups" type="number" min="1" step="1" value="4" size="3"  class="inputclass form-control input-sm form-control-inline">
		&nbsp;
		<input type="radio" name="groupControl" value="numLearners" id="groupControlLearners" class="voffset5">
		<label for="groupControlLearners"><span id="numLearnersSpan"> <fmt:message key="authoring.label.numlearners"/></span></label>  
		<input name="numLearners" id="numLearners" type="number" step="1" min="1" value="1" size="3"  class="inputclass form-control input-sm form-control-inline voffset5" disabled>

	</div>
	</div>
	
	<script type="text/javascript">
	    
	    function groupingChanged() {
			var newValue = $('#grouping').val();
			if ( newValue == '2' ) {
				// teachers choice only has number of groups
				$('#numGroups').prop('disabled', false);
				$('#numLearners').prop('disabled', true);
				$('#groupControlGroups').prop('checked', true);
				$('#groupControlLearners').prop('disabled', true);
				$('#groupControlLearners').hide();
				$('#numLearnersSpan').hide();
				$('#numLearners').hide();
			} else {
				$('#groupControlLearners').prop('disabled', false);
				$('#groupControlLearners').show();
				$('#numLearnersSpan').show();
				$('#numLearners').show();
			}
		}
		
   		$('#grouping').change(function() {		
			groupingChanged();
	    });


   		$('#groupControlGroups').change(function() {		
   			$('#numGroups').prop('disabled', false);
			$('#numLearners').prop('disabled', true);
	    });

   		$('#groupControlLearners').change(function() {		
   			$('#numGroups').prop('disabled', true);
			$('#numLearners').prop('disabled', false);
	    });
	</script>
		
		
