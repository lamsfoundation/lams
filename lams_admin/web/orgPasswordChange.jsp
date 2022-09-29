<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>
	
<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.org.password.change.title"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme5.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
	<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet" />
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<style type="text/css">
		.changeContainer .checkbox {
			display: inline-block;
		}
		
		.changeContainer .pass {
			display: inline-block;
			margin-left: 20px;
			margin-right: 10px;
			width: 260px;
		}
		
		.changeContainer .fa {
			cursor: pointer;
		}
		
		h3, h4 {
			text-align: center;
		}
		
		#changeTable > tbody > tr > td{
			padding-left: 50px;
			padding-top: 20px;
		}
		
		#changeTable > tbody > tr > td:first-child {
			border-right: thin solid black;
			padding-right: 50px;
		}
		
		.gridCell {
			vertical-align: top;
		}
		
		.ui-jqgrid-btable tr[role="row"] {
			cursor: pointer;	
		}
		
		.ui-jqgrid-btable tr.success > td {
			background-color: transparent !important;
		}
		
		#formValidationErrors {
			display: none;
			text-align: center;		
		}
	</style>
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
	<script type="text/javascript">
	     var mustHaveUppercase = ${mustHaveUppercase},
		     mustHaveNumerics  = ${mustHaveNumerics},
		     mustHaveLowercase  = ${mustHaveLowercase},
		     mustHaveSymbols   = ${mustHaveSymbols};
		 
	     $.validator.addMethod("pwcheck", function(value) {
		      return (!mustHaveUppercase || /[A-Z]/.test(value)) && // has uppercase letters 
				     (!mustHaveNumerics || /\d/.test(value)) && // has a digit
				     (!mustHaveLowercase || /[a-z]/.test(value)) && // has a lower case
				     (!mustHaveSymbols || /[`~!@#$%^&*\(\)_\-+={}\[\]\\|:\;\"\'\<\>,.?\/]/.test(value)); //has symbols
	     });
		 $.validator.addMethod("charactersAllowed", function(value) {
			  return /^[A-Za-z0-9\d`~!@#$%^&*\(\)_\-+={}\[\]\\|:\;\"\'\<\>,.?\/]*$/
				     .test(value)
		 });
	
		$(function() {
			// assign grid ID to each checkbox and define what happens when it gets (un)checked
			var changeCheckboxes = $('#staffChange').data('grid', 'staffGrid')
								  .add($('#learnerChange').data('grid', 'learnerGrid'))
								  .change(function(){
									  		var checkbox = $(this);
											// prevent both checkboxes from being unchecked
											if (!changeCheckboxes.is(':checked')) {
												checkbox.prop('checked', true);
												return;
											}
											
											var	enabled = checkbox.prop('checked'),
												grid = $('#' + checkbox.data('grid')).closest('.ui-jqgrid'),
												pass = checkbox.closest('.changeContainer').find('.pass');
											// disable/enable password input depending on checkbox state
											// hide/show users grid
											if (enabled) {
												grid.slideDown();
												pass.prop('disabled', false);
											} else {
												grid.slideUp();
												pass.val(null).prop('disabled', true);
											}
										});
	
			// generate new password on click
			$('.generatePassword').click(function(){
				var container = $(this).closest('.changeContainer');
				if (!container.find('input[type="checkbox"]').prop('checked')) {
					return;
				}
				$.ajax({
					'url' : '<lams:LAMSURL/>admin/orgPasswordChange/generatePassword.do'
				}).done(function(password){
					container.find('.pass').val(password);
				});
			});
			
			
			
			// Setup form validation 
			$("#orgPasswordChangeForm").validate({
				errorLabelContainer : '#formValidationErrors',
				errorClass : 'errorMessage',
				rules : {
					learnerPass : {
						required: false,
						minlength : <c:out value="${minNumChars}"/>,
						maxlength : 50,
						charactersAllowed : true,
						pwcheck : true
					},
					staffPass: {
						required: false,
						minlength : <c:out value="${minNumChars}"/>,
						maxlength : 50,
						charactersAllowed : true,
						pwcheck : true
					}
					
				},
	
				// Specify the validation error messages
				messages : {
					learnerPass : {
						required : "<fmt:message key='error.password.empty'/>",
						minlength : "<fmt:message key='admin.org.password.change.error.prefix.learner'/> <fmt:message key='label.password.min.length'><fmt:param value='${minNumChars}'/></fmt:message>",
						maxlength : "<fmt:message key='admin.org.password.change.error.prefix.learner'/> <fmt:message key='label.password.max.length'/>",
						charactersAllowed : "<fmt:message key='label.password.symbols.allowed'/> ` ~ ! @ # $ % ^ & * ( ) _ - + = { } [ ] \ | : ; \" ' < > , . ? /",
						pwcheck : "<fmt:message key='label.password.restrictions'/>"
					},
					staffPass: {
						required : "<fmt:message key='error.password.empty'/>",
						minlength : "<fmt:message key='admin.org.password.change.error.prefix.staff'/> <fmt:message key='label.password.min.length'><fmt:param value='${minNumChars}'/></fmt:message>",
						maxlength : "<fmt:message key='admin.org.password.change.error.prefix.staff'/> <fmt:message key='label.password.max.length'/>",
						charactersAllowed : "<fmt:message key='label.password.symbols.allowed'/> ` ~ ! @ # $ % ^ & * ( ) _ - + = { } [ ] \ | : ; \" ' < > , . ? /",
						pwcheck : "<fmt:message key='label.password.restrictions'/>"
					}
					
				},
	
				submitHandler : function(form) {
					// see which mode are we in (included/excluded) and serialize JSON arrays into strings
					var learnerGrid = $('#learnerGrid'),
						staffGrid = $('#staffGrid'),
						includedLearners = learnerGrid.data('included'),
						excludedLearners = learnerGrid.data('excluded'),
						includedStaff = staffGrid.data('included'),
						excludedStaff = staffGrid.data('excluded');
					$('#includedLearners').val(includedLearners === null ? '' : JSON.stringify(includedLearners));
					$('#excludedLearners').val(excludedLearners === null ? '' : JSON.stringify(excludedLearners));
					$('#includedStaff').val(includedStaff === null ? '' : JSON.stringify(includedStaff));
					$('#excludedStaff').val(excludedStaff === null ? '' : JSON.stringify(excludedStaff));
					form.submit();
				}
			});
	
			var jqGridURL = "<lams:LAMSURL/>admin/orgPasswordChange/getGridUsers.do?organisationID=<c:out value='${orgPasswordChangeForm.organisationID}' />&role=",
				jqGridSettings = {
					datatype		   : "json",
				    height			   : "100%",
				    // use new theme
				    guiStyle 		   : "bootstrap",
				    iconSet 		   : 'fontAwesome',
				    autowidth		   : true,
				    shrinkToFit 	   : true,
				    multiselect 	   : true,
				    multiPageSelection : true,
				    // it gets ordered by 
				    sortorder		   : "asc", 
				    sortname		   : "firstName", 
				    pager			   : true,
				    rowNum			   : 10,
					colNames : [
						'<fmt:message key="admin.org.password.change.grid.name"/>',
						'<fmt:message key="admin.org.password.change.grid.login"/>',
						'<fmt:message key="admin.org.password.change.grid.email"/>'
					],
				    colModel : [
				        {
						   'name'  : 'name', 
						   'index' : 'firstName',
						   'title' : false
						},
						{
						   'name'  : 'login', 
						   'index' : 'login',
						   'title' : false
						},
				        {
						   'name'  : 'email', 
						   'index' : 'email',
						   'title' : false
						}
				    ],
				    onSelectRow : function(id, status, event) {
					    var grid = $(this),
					   		included = grid.data('included'),
							excluded = grid.data('excluded'),
							selectAllChecked = grid.closest('.ui-jqgrid-view').find('.jqgh_cbox .cbox').prop('checked');
						if (selectAllChecked) {
							var index = excluded.indexOf(+id);
							// if row is deselected, add it to excluded array
							if (index < 0) {
								if (!status) {
									excluded.push(+id);
								}
							} else if (status) {
								excluded.splice(index, 1);
							}
						} else {
							var index = included.indexOf(+id);
							// if row is selected, add it to included array
							if (index < 0) {
								if (status) {
									included.push(+id);
								}
							} else if (!status) {
								included.splice(index, 1);
							}
						}
					},
					gridComplete : function(){
						var grid = $(this),
							included = grid.data('included'),
							// cell containing "(de)select all" button
							selectAllCell = grid.closest('.ui-jqgrid-view').find('.jqgh_cbox > div');
						// remove the default button provided by jqGrid
						$('.cbox', selectAllCell).remove();
						// create own button which follows own rules
						var selectAllCheckbox = $('<input type="checkbox" class="cbox" />')
												.prop('checked', included === null)
												.prependTo(selectAllCell)
												.change(function(){
													// start with deselecting everyone on current page
													grid.resetSelection();
													if ($(this).prop('checked')){
														// on select all change mode and select all on current page
														grid.data('included', null);
														grid.data('excluded', []);
														$('[role="row"]', grid).each(function(){
															grid.jqGrid('setSelection', +$(this).attr('id'), false);
														});
													} else {
														// on deselect all just change mode
														grid.data('excluded', null);
														grid.data('included', []);
													}
												});
						
						grid.resetSelection();
						if (selectAllCheckbox.prop('checked')) {
							var excluded = grid.data('excluded');
							// go through each loaded row
							$('[role="row"]', grid).each(function(){
								var id = +$(this).attr('id'),
									selected = $(this).hasClass('success');
								// if row is not selected and is not excluded, select it
								if (!selected && (!excluded || !excluded.includes(id))) {
									// select without triggering onSelectRow
									grid.jqGrid('setSelection', id, false);
								}
							}); 
						} else {
							// go through each loaded row
							$('[role="row"]', grid).each(function(){
								var id = +$(this).attr('id'),
									selected = $(this).hasClass('success');
								// if row is not selected and is included, select it
								if (!selected && included.includes(id)) {
									// select without triggering onSelectRow
									grid.jqGrid('setSelection', id, false);
								}
							});
						}
	
						changeCheckboxes.trigger('change');
					},
				    loadError : function(xhr,st,err) {
				    	$.jgrid.info_dialog('<fmt:message key="admin.error"/>', 
		    					'<fmt:message key="admin.org.password.change.grid.error.load"/>',
		    					'<fmt:message key="label.ok"/>');
				    }
			};
	
	
		    var includedLearners = "<c:out value='${orgPasswordChangeForm.includedLearners}' />",
		    	excludedLearners = "<c:out value='${orgPasswordChangeForm.excludedLearners}' />",
		    	includedStaff = "<c:out value='${orgPasswordChangeForm.includedStaff}' />",
		    	excludedStaff = "<c:out value='${orgPasswordChangeForm.excludedStaff}' />";
			jqGridSettings.url = jqGridURL + 'learner'
			$("#learnerGrid").data('excluded', excludedLearners ? JSON.parse(excludedLearners) : null)
							 .data('included', includedLearners ? JSON.parse(includedLearners) : null)
							 .jqGrid(jqGridSettings);
			jqGridSettings.url = jqGridURL + 'staff'
			$("#staffGrid").data('excluded', excludedStaff ? JSON.parse(excludedStaff) : null)
						   .data('included', includedStaff ? JSON.parse(includedStaff) : null)
						   .jqGrid(jqGridSettings);
	
		});
	</script>
	
</lams:head>
    
<body class="stripes">
	<lams:Page5 type="admin" title="${title}">
				<p>
					<a href="<lams:LAMSURL/>admin/usermanage.do?org=<c:out value='${orgPasswordChangeForm.organisationID}' />" class="btn btn-default">
					<fmt:message key="admin.user.manage" /></a>
				</p>
				
				<div class="panel panel-default panel-body">
					<h3><c:out value='${orgPasswordChangeForm.orgName}' /></h3>
					
					<lams:Alert5 type="info" id="passwordConditions" close="false">
						<fmt:message key='label.password.must.contain' />:
						<ul class="list-unstyled" style="line-height: 1.2">
							<li><span class="fa fa-check"></span> <fmt:message
									key='label.password.min.length'>
									<fmt:param value='${minNumChars}' />
								</fmt:message></li>
				
							<c:if test="${mustHaveUppercase}">
								<li><span class="fa fa-check"></span> <fmt:message
										key='label.password.must.ucase' /></li>
							</c:if>
							<c:if test="${mustHaveLowercase}">
								<li><span class="fa fa-check"></span> <fmt:message
										key='label.password.must.lcase' /></li>
							</c:if>
				
							<c:if test="${mustHaveNumerics}">
								<li><span class="fa fa-check"></span> <fmt:message
										key='label.password.must.number' /></li>
							</c:if>
				
				
							<c:if test="${mustHaveSymbols}">
								<li><span class="fa fa-check"></span> <fmt:message
										key='label.password.must.symbol' /></li>
							</c:if>
							
							<li><span class="fa fa-check"></span>
								<fmt:message key='label.password.user.details' />
							</li>
							<li><span class="fa fa-check"></span>
								<fmt:message key='label.password.common' />
							</li>
						</ul>
						
					</lams:Alert5>
					
					<form:form action="changePassword.do" modelAttribute="orgPasswordChangeForm" id="orgPasswordChangeForm" method="post">
						<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
						<form:hidden path="organisationID" />
						<form:hidden path="orgName" />
						<form:hidden path="includedLearners" id="includedLearners" />
						<form:hidden path="excludedLearners" id="excludedLearners" />
						<form:hidden path="includedStaff" id="includedStaff" />
						<form:hidden path="excludedStaff" id="excludedStaff" />
						
						<div class="form-group">
							<div class="checkbox">
								<label>
									<form:checkbox path="email"/><fmt:message key="admin.org.password.change.email" />
								</label>
							</div>
							<div class="checkbox">
								<label>
									<form:checkbox path="force"/><fmt:message key="admin.org.password.change.force" />
								</label>
							</div>
						</div>
						
						<c:if test="${success}">
							<lams:Alert5 type="info" id="passwordConditions" close="false">
								<h4><fmt:message key="admin.org.password.change.success" /></h4>
							</lams:Alert5>
						</c:if>
						
						<div id="formValidationErrors">
							<ul></ul>
						</div>
						<h4><fmt:message key="admin.org.password.change.choose" /></h4>
						<table id="changeTable">
							<tbody>
								<tr>
									<td class="changeContainer">
										<div class="checkbox">
											<label>
												<form:checkbox path="staffChange" id="staffChange" />
													<fmt:message key="admin.org.password.change.is.staff" />
											</label>
										</div>
										<form:input path="staffPass" id="staffPass" cssClass="pass form-control" maxlength="50"
												   title="<fmt:message key='admin.org.password.change.custom' />" />
										<i class="fa fa-refresh generatePassword" title="<fmt:message key='admin.org.password.change.generate' />"></i>
									</td>
									<td class="changeContainer">
										<div class="checkbox">
											<label>
												<form:checkbox path="learnerChange" id="learnerChange" />
													<fmt:message key="admin.org.password.change.is.learner" />
											</label>
										</div>
										<form:input path="learnerPass" id="learnersPass" cssClass="pass form-control" maxlength="50"
											   title="<fmt:message key='admin.org.password.change.custom' />" />
										<i class="fa fa-refresh generatePassword" title="<fmt:message key='admin.org.password.change.generate' />"></i>
									</td>
								</tr>
								<tr>
									<td class="gridCell">
										<table id="staffGrid"></table>
									</td>
									<td class="gridCell">
										<table id="learnerGrid"></table>
									</td>
								</tr>
							</tbody>
						</table>
						<div class="pull-right mt-3">
							<input type="submit" class="btn btn-primary" value="<fmt:message key='admin.org.password.change.submit' />" />
						</div>
					</form:form>
				</div>
	</lams:Page5>
</body>
</lams:html>




