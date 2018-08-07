	<a id="toggleAdditionalOptionsAreaLink" href="javascript:toggleAdditionalOptionsArea()" class="btn btn-default btn-xs pull-right"><fmt:message key="label.authoring.basic.additionaloptions.show" /> </a>

 	<div id="additionalOptionsArea" class="form-inline" style="display: none;">
	
		${additionalOptions} 

		${summary}

 		<div class="checkbox">
		    <label>
	 	      <form:checkbox path="questionRequired" id="questionRequired"/>&nbsp;<fmt:message key="label.authoring.basic.required" />
		    </label>
	  	</div>
	</div>

 	<div id="additionalOptionsArea" class="form-inline" style="display: none;">
	
	
	