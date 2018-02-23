<%@ include file="/taglibs.jsp"%>

<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>

<lams:css />
<link type="text/css" href="<lams:LAMSURL/>css/free.ui.jqgrid.min.css" rel="stylesheet" />
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
	
	.jqgh_cbox {
		visibility: hidden;
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
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/free.jquery.jqgrid.min.js"></script>
<script type="text/javascript">
     var mustHaveUppercase = ${mustHaveUppercase},
	     mustHaveNumerics  = ${mustHaveNumerics},
	     mustHaveLowercase  = ${mustHaveLowercase},
	     mustHaveSymbols   = ${mustHaveSymbols},
	     excludedLearners = JSON.parse("<bean:write name='OrgPasswordChangeForm' property='excludedLearners' />"),
	     excludedStaff = JSON.parse("<bean:write name='OrgPasswordChangeForm' property='excludedStaff' />");

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
		var changeCheckboxes = $('#isStaffChange').data('grid', 'staffGrid')
							  .add($('#isLearnerChange').data('grid', 'learnerGrid'))
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
				'url' : 'orgPasswordChange.do',
				'data': {
					'method' : 'generatePassword'
				}
			}).done(function(password){
				container.find('.pass').val(password);
			});
		});
		
		
		
		// Setup form validation 
		$("#OrgPasswordChangeForm").validate({
			errorLabelContainer : '#formValidationErrors',
			errorClass : 'errorMessage',
			rules : {
				learnerPass : {
					required: false,
					minlength : <c:out value="${minNumChars}"/>,
					maxlength : 25,
					charactersAllowed : true,
					pwcheck : true
				},
				staffPass: {
					required: false,
					minlength : <c:out value="${minNumChars}"/>,
					maxlength : 25,
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
				$('#excludedLearners').val(JSON.stringify(excludedLearners));
				$('#excludedStaff').val(JSON.stringify(excludedStaff));
				form.submit();
			}
		});

		var jqGridURL = "orgPasswordChange.do?method=getGridUsers&organisationID=<bean:write name='OrgPasswordChangeForm' property='organisationID' />&role=",
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
						excluded = grid.data('excluded'),
						index = excluded.indexOf(+id);
					// if row is deselected, add it to excluded array
					if (index < 0) {
						if (!status) {
							excluded.push(+id);
						}
					} else if (status) {
						excluded.splice(index, 1);
					}
				},
				gridComplete : function(){
					var grid = $(this),
						// get excludedLearners or excludedStaff
						excluded = grid.data('excluded');
					// go through each loaded row
					$('[role="row"]', grid).each(function(){
						var id = +$(this).attr('id'),
							selected = $(this).hasClass('success');
						// if row is not selected and is not excluded, select it
						if (!selected && !excluded.includes(id)) {
							// select without triggering onSelectRow
							grid.jqGrid('setSelection', id, false);
						}
					});

					changeCheckboxes.trigger('change');
				},
			    loadError : function(xhr,st,err) {
			    	$.jgrid.info_dialog('<fmt:message key="admin.error"/>', 
	    					'<fmt:message key="admin.org.password.change.grid.error.load"/>',
	    					'<fmt:message key="label.ok"/>');
			    }
		};
		
		jqGridSettings.url = jqGridURL + 'learner'
		$("#learnerGrid").data('excluded', excludedLearners).jqGrid(jqGridSettings);
		jqGridSettings.url = jqGridURL + 'staff'
		$("#staffGrid").data('excluded', excludedStaff).jqGrid(jqGridSettings);

	});
</script>

<p>
	<a href="<lams:LAMSURL/>admin/usermanage.do?org=<bean:write name='OrgPasswordChangeForm' property='organisationID' />" class="btn btn-default">
	<fmt:message key="admin.user.manage" /></a>
</p>

<div class="panel panel-default panel-body">
	<h3><bean:write name='OrgPasswordChangeForm' property='orgName' /></h3>
	
	<lams:Alert type="info" id="passwordConditions" close="false">
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
		</ul>
		
	</lams:Alert>
	
	<html:form action="orgPasswordChange.do" styleId="OrgPasswordChangeForm">
		<input type="hidden" name="method" value="changePassword" />
		<html:hidden property="organisationID" />
		<html:hidden property="orgName" />
		<html:hidden property="excludedLearners" styleId="excludedLearners" />
		<html:hidden property="excludedStaff" styleId="excludedStaff" />
		
		<div class="form-group">
			<div class="checkbox">
				<label>
					<html:checkbox property="email"><fmt:message key="admin.org.password.change.email" /></html:checkbox>
				</label>
			</div>
			<div class="checkbox">
				<label>
					<html:checkbox property="force"><fmt:message key="admin.org.password.change.force" /></html:checkbox>
				</label>
			</div>
		</div>
		
		<c:if test="${success}">
			<lams:Alert type="info" id="passwordConditions" close="false">
				<h4><fmt:message key="admin.org.password.change.success" /></h4>
			</lams:Alert>
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
								<html:checkbox property="isStaffChange" styleId="isStaffChange">
									<fmt:message key="admin.org.password.change.is.staff" />
								</html:checkbox>
							</label>
						</div>
						<html:text property="staffPass" styleId="staffPass" styleClass="pass form-control" maxlength="25"
								   title="<fmt:message key='admin.org.password.change.custom' />" />
						<i class="fa fa-refresh generatePassword" title="<fmt:message key='admin.org.password.change.generate' />"></i>
					</td>
					<td class="changeContainer">
						<div class="checkbox">
							<label>
								<html:checkbox property="isLearnerChange" styleId="isLearnerChange">
									<fmt:message key="admin.org.password.change.is.learner" />
								</html:checkbox>
							</label>
						</div>
						<html:text property="learnerPass" styleId="learnersPass" styleClass="pass form-control" maxlength="25"
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
		<div class="pull-right voffset20">
			<input type="submit" class="btn btn-primary" value="<fmt:message key='admin.org.password.change.submit' />" />
		</div>
	</html:form>
</div>