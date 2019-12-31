<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="useInternalSMTPServer"><lams:Configuration key="InternalSMTPServer"/></c:set>
<c:set var="smtpServer"><lams:Configuration key="SMTPServer"/></c:set>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.management"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap.css">
	<link type="text/css" href="${lams}css/jquery.tablesorter.pager.css" rel="stylesheet">
	<style>
		#tablesorter thead .disabled {
			display: none
		}
	</style>
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap.min.js"></script>
	<script type="text/javascript" src="${lams}/includes/javascript/portrait.js" ></script>
	<script type="text/javascript">
	  	$(document).ready(function(){
	  	
	  		var $table = $(".tablesorter").tablesorter({
	  			theme: 'bootstrap',
	  			headerTemplate : '{content} {icon}',
				widgets: ["uitheme", "zebra", "filter"],
			    widgetOptions : {
			    	filter_external : '.search',
			        // include column filters
			        filter_columnFilters: false,
			        filter_placeholder: { search : 'Search...' },
			        filter_saveFilters : true,
			        filter_reset: '.reset'
			      },
			    widthFixed: true,
				headers : { 5 : { sorter: false } },
			    sortInitialOrder: 'desc',
	            sortList: [[1]] 
			});
			
			$(".tablesorter").each(function() {
				$(this).tablesorterPager({
					savePages: false,
	                container: $(this).find(".ts-pager"),
	                output: '{startRow} to {endRow} ({totalRows})',
	                cssPageDisplay: '.pagedisplay',
	                cssPageSize: '.pagesize',
	                cssDisabled: 'disabled',
	                ajaxUrl : "<c:url value='/usersearch/getPagedUsers.do'/>?page={page}&size={size}&{sortList:column}&{filterList:fcol}",
					ajaxProcessing: function (data, table) {
	
				    	if (data && data.hasOwnProperty('rows')) {
				    		var rows = [],
				            json = {};
				    		
							for (i = 0; i < data.rows.length; i++){
								var orgData = data.rows[i];
								
								rows += '<tr>';
								
								rows += '<td>';
								rows += 	definePortraitPopover(orgData["portraitId"], orgData["userId"], orgData["userId"], orgData["userId"]);
								rows += '</td>';
	
								rows += '<td>';
								rows += 	definePortraitPopover(orgData["portraitId"], orgData["userId"], orgData["login"], orgData["login"]);
								rows += '</td>';
	
								rows += '<td>';
								rows += 	orgData["firstName"];
								rows += '</td>';
	
								rows += '<td>';
								rows += 	orgData["lastName"];
								rows += '</td>';
	
								rows += '<td>';
								rows += 	orgData["email"];
								rows += '</td>';
								
								rows += '<td>';
								rows +=		'<form style="display: inline-block;" id="delete_' + orgData["userId"] +'" method="post" action="/lams/admin/user/remove.do"><input type="hidden" name="userId" value="' + orgData["userId"] + '"/><input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/><button class="btn btn-danger btn-xs" type="submit"><i class="fa fa-trash"/> <span class="hidden-xs hidden-sm"><fmt:message key="admin.user.delete"/></span></button></form>';
								rows += 	'&nbsp;';
								rows +=     '<form style="display: inline-block;" id="delete_' + orgData["userId"] +'" method="post" action="/lams/admin/user/edit.do"><input type="hidden" name="userId" value="' + orgData["userId"] + '"/><input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/><button class="btn btn-primary btn-xs" type="submit"><i class="fa fa-pencil"/> <span class="hidden-xs hidden-sm"><fmt:message key="admin.edit"/></span></button></form>';
								rows += 	'&nbsp;';
								rows += 	'<a title="<fmt:message key="label.login.as"/>" href="<lams:LAMSURL/>loginas.do?login=' + orgData["login"] + '">';
								rows += 		'<button type="button" class="btn btn-primary btn-xs"><i class="fa fa-sign-in"/><span class="hidden-xs hidden-sm"> <fmt:message key="label.login.as"/></span></button>';
								rows += 	'</a>';
								rows += 	'&nbsp;';
	
								if (${(useInternalSMTPServer || not empty smtpServer)} && orgData["email"] != null && orgData["email"] != "") {
								rows += 	'<a title="<fmt:message key="label.email"/>" href="<lams:LAMSURL/>emailUser/composeMail.do?returnUrl=/lams/admin/usersearch.do&userID=' + orgData["userId"] + '">';
								rows += 		'<button type="button" class="btn btn-primary btn-xs"><i class="fa fa-envelope"/> <span class="hidden-xs hidden-sm"><fmt:message key="label.email"/></span></button>';
								rows += 	'</a>';
								}
								
								rows += '</td>';
								
								rows += '</tr>';
							}
				            
							json.total = data.total_rows;
							json.rows = $(rows);
							return json;
				            
				    	}
					}
	            })
	            .bind('pagerInitialized pagerComplete', function(event, options){
					initializePortraitPopover('${lams}');
				})
	
			});
			
			// make demo search buttons work
			$('button[data-column]').on('click', function(){
				var $this = $(this),
			    	filter = [];
	
			    // text to add to filter
			    filter[ $this.data('1') ] = $this.text();
			    $table.trigger('search', [ filter ]);
			    return false;
			});
	  	})
	</script>
</lams:head>
    
<body class="stripes">

	<lams:Page type="admin" title="${title}">
		<p><a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
		<a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default loffset5"><fmt:message key="sysadmin.maintain" /></a></p>
		
		<p><input class="btn btn-default" type="button" value="<fmt:message key="admin.user.create"/>" onclick="javascript:document.location='user/edit.do'" />
		
		<span class="pull-right">
			<fmt:message key="admin.search" />:&nbsp;<input class="search form-control form-control-inline" type="search" name="term" data-column="1">
		</span>
		</p>
		
		<lams:TSTable numColumns="6"> 
			<th width="3%">
				<fmt:message key="admin.user.userid"/>
			</th>
			<th width="15%" align="center">
				<fmt:message key="admin.user.login"/>
			</th>
			<th width="15%" align="center">
				<fmt:message key="admin.user.first_name"/>
			</th>
			<th width="15%" align="center">
				<fmt:message key="admin.user.last_name"/>
			</th>
			<th width="15%" align="center">
				<fmt:message key="admin.user.email"/>
			</th>
			<th width="20%" align="center">
				<fmt:message key="admin.user.actions"/>
			</th>
		</lams:TSTable>			
				
	</lams:Page>

</body>
</lams:html>
