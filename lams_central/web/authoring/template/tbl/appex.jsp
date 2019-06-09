<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%-- Application Exercise. It should have one or more essay or multiple choice questions. If QTI Import is used then all the questions are put into one exercise. --%>

<!--  Start of panel Appex${appexNumber} -->
 <div class="panel panel-default" id="divappex${appexNumber}" >
     <div class="panel-heading" id="headingAppex${appexNumber}">
     	<span class="panel-title collapsable-icon-left">
     		<a class="${status.first ? '' : 'collapsed'}" role="button" data-toggle="collapse" href="#collapseAppex${appexNumber}" 
			aria-expanded="${status.first ? 'false' : 'true'}" aria-controls="collapseAppex${appexNumber}" >
					${appexNumber eq 1 ? "<label class=\"required\">" : ""}
						<fmt:message key="authoring.label.application.exercise.num"><fmt:param value="${appexNumber}"/></fmt:message>
					${appexNumber eq 1 ? "</label>" : ""}
			</a>
		</span>
     </div>
    
    <div id="collapseAppex${appexNumber}" class="panel-collapse collapse in" 
    		role="tabpanel" aria-labelledby="headingAppex${appexNumber}">	
    				
		 	<input type="hidden" name="numAssessments${appexNumber}" id="numAssessments${appexNumber}" value="0"/>
			<div id="divass${appexNumber}"></div>
			<div class="space-top space-sides">
				<a href="#" onclick="javascript:createAssessment('essay', 'numAssessments${appexNumber}', 'divass${appexNumber}');" class="btn btn-default"><fmt:message key="authoring.create.essay.question"/></a>
				<a href="#" onclick="javascript:createAssessment('mcq', 'numAssessments${appexNumber}', 'divass${appexNumber}');" class="btn btn-default"><fmt:message key="authoring.create.mc.question"/></a>
				<a href="#" onClick="javascript:importQTI('appex${appexNumber}')" class="btn btn-default pull-right"><fmt:message key="authoring.template.basic.import.qti" /></a>
			</div>
			<div class="space-top space-sides space-bottom">
				<div class="checkbox"><label for="divappex${appexNumber}NB">
					<input name="divappex${appexNumber}NB" id="divappex${appexNumber}NB" type="checkbox" value="true"/> <fmt:message key="authoring.tbl.use.notebook" />
				</label></div>
				<div class="form-group">
					<textarea id="divappex${appexNumber}NBIns" name="divappex${appexNumber}NBIns" class="form-control" rows="3"></textarea>
				</div>
			</div>
	</div>
	
	<script type="text/javascript">
	//automatically turn on refect option if there are text input in refect instruction area
	var notebook${appexNumber} = document.getElementById("divappex${appexNumber}NB");
	var instructions${appexNumber} = document.getElementById("divappex${appexNumber}NBIns");
	function turnOnIns${appexNumber}(){
		if(isEmpty(instructions${appexNumber}.value)){
			//turn off	
			notebook${appexNumber}.checked = false;
		}else{
			//turn on
			notebook${appexNumber}.checked = true;		
		}
	}
	
	instructions${appexNumber}.onkeyup=turnOnIns${appexNumber};
	</script>
	
</div> 
<!--  End of panel Appex${appexNumber} -->
