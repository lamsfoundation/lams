<!DOCTYPE html>

<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-core" prefix="c"%>

<lams:html>
<lams:head>
	<lams:css/>
	<style type="text/css">
		.rubric {
			margin-top: 10px;
		}
				
		.rubric .control {
			cursor: pointer;
		}
		
		.rubric:last-child .control {
			visibility: hidden;
		}
		
		.rubric:nth-last-child(2) .down, .rubric:first-child .up {
			visibility: hidden !important;
		}
	</style>
		
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.js"></script>
	<script type="text/javascript">
		var orgId = ${param.organisationID};
		function removeRubric(elem) {
			$(elem).closest('.rubric').remove();
		}
	
		function upRubric(elem){
			var rubric = $(elem).closest('.rubric');
			rubric.prev().before(rubric);
		}

		function downRubric(elem){
			var rubric = $(elem).closest('.rubric'),
				next = rubric.next();
			if (!next.is(':last-child')){
				next.after(rubric);
			}
		}

		function validateRubric(elem){
			var name = $(elem),
				rubric = name.closest('.rubric');
			if (name.val()) {
				if (rubric.is(':last-child')) {
					// set this rubric as a real one
					rubric.clone().appendTo(rubric.parent()).find('input').val(null);
				} else {
					// rubric has content now, so it is valid
					rubric.removeClass('has-error');
				}
			} else if (!rubric.is(':last-child')) {
				// mark empty rubric as invalid
				rubric.addClass('has-error');
			}
		}

		function submitRubrics() {
			var rubrics = [],	
				error = false;
			$('.rubric').each(function(){
				var rubric = $(this);
				if (rubric.hasClass('has-error')) {
					error = true;
					return false;
				} else if (rubric.is(':last-child')) {
					// skip last row as it is the rubric placeholder
					return true;
				}
				rubrics.push($('input', rubric).val());
			});
			
			if (error) {
				return;
			}
			
			var data = {
				'organisationID' : ${param.organisationID},
				'rubrics' : JSON.stringify(rubrics)
			};
			$.post('<lams:LAMSURL/>learning/kumalive/saveRubrics.do', data, function(){
				window.parent.closeDialog('dialogKumaliveRubrics${param.organisationID}');
			});
		}
		
		$(document).ready(function () {
			var rubrics = ${rubrics},
				rubric = $('.rubric');
			$.each(rubrics, function(){
				rubric.clone().insertBefore(rubric).find('input').val(this);
			});
		});
	</script>
	
</lams:head>
<body class="stripes">

<div class="container-fluid">
	<div class="panel panel-default panel-admin-page">
		<div class="panel-body panel-admin-body">
			<div>
				<div class="row rubric">
					<div class="col-9">
						<input type="text" class="form-control" placeholder="<fmt:message key='label.kumalive.rubric.name'/>" onkeyup="javascript:validateRubric(this)"/>
					</div>
					<div class="col-1 down control" title="<fmt:message key='label.kumalive.rubric.up'/>" onclick="javascript:downRubric(this)">
						<lams:Arrow state="down" />
		 			</div>
		 			<div class="col-1 up control" title="<fmt:message key='label.kumalive.rubric.up'/>" onclick="javascript:upRubric(this)">
		 				<lams:Arrow state="up" />
		 			</div> 
		 			<div class="col-1 control" title="<fmt:message key='label.kumalive.rubric.delete' />" onclick="javascript:removeRubric(this)">
						<i class="fa fa-times"></i>
		 			</div>
				</div>
			</div>
			<button class="btn btn-primary pull-right voffset20" onClick="javascript:submitRubrics()"><fmt:message key="button.save" /></button>
		</div>
	</div>
</div>

</body>
</lams:html>