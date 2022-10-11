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

	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme5.css" type="text/css" media="screen">	
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery.tablesorter.theme.bootstrap5.css">
	<link type="text/css"  href="<lams:LAMSURL/>css/jquery.tablesorter.pager5.css" rel="stylesheet">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<style>
		#tablesorter thead .disabled {
			display: none
		}
	</style>
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap5.bundle.min.js"></script>
	<lams:JSImport src="includes/javascript/portrait5.js" />
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
	
								rows += '<td class="d-none d-lg-table-cell">';
								rows += 	orgData["email"];
								rows += '</td>';
								
								rows += '<td>';
								rows +=		'<form style="display: inline-block;" id="delete_' + orgData["userId"] +'" method="post" action="/lams/admin/user/remove.do"><input type="hidden" name="userId" value="' + orgData["userId"] + '"/><input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/><button class="btn btn-danger" type="submit" title="<fmt:message key="admin.user.delete"/>"><i class="fa fa-trash"></i></button></form>';
								rows += 	'&nbsp;';
								rows +=     '<form style="display: inline-block;" id="edit_' + orgData["userId"] +'" method="post" action="/lams/admin/user/edit.do"><input type="hidden" name="userId" value="' + orgData["userId"] + '"/><input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/><button class="btn btn-primary" type="submit" title="<fmt:message key="admin.edit"/>"><i class="fa fa-pencil"></i></button></form>';
								rows += 	'&nbsp;';
								<c:if test="${loginAsEnable}">
								rows += 	'<a title="<fmt:message key="label.login.as"/>" href="<lams:LAMSURL/>loginas.do?login=' + orgData["login"] + '">';
								rows += 		'<button type="button" class="btn btn-primary" title="<fmt:message key="label.login.as"/>"><i class="fa fa-sign-in"></i></button>';
								rows += 	'</a>';
								rows += 	'&nbsp;';
								</c:if>
	
								if (${(useInternalSMTPServer || not empty smtpServer)} && orgData["email"] != null && orgData["email"] != "") {
								rows += 	'<a title="<fmt:message key="label.email"/>" href="<lams:LAMSURL/>emailUser/composeMail.do?returnUrl=/lams/admin/usersearch.do&userID=' + orgData["userId"] + '">';
								rows += 		'<button type="button" class="btn btn-primary"><i class="fa fa-envelope"></i> <span class="hidden-xs hidden-sm"><fmt:message key="label.email"/></span></button>';
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
    
<body class="component pb-4">

	<%-- Build the breadcrumb --%>
	<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, <lams:LAMSURL/>admin/orgmanage.do?org=1 | <fmt:message key="admin.course.manage" /></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems},. | ${title}</c:set>


	<lams:Page5 type="admin" title="${title}" breadcrumbItems="${breadcrumbItems}">
		<div class="row mb-3">
			<div class="col-2">
				<input class="btn btn-primary" type="button" value="<fmt:message key="admin.user.create"/>" onclick="javascript:document.location='user/edit.do'" />
			</div>
				<label for="search" class="col offset-6 col-form-label text-end"><fmt:message key="admin.search" />:</label>
			<div class="col-2">
				<input class="search form-control" type="search" id="search" name="term" data-column="1">
			</div>
		</div>
		
		<lams:TSTable5 numColumns="6"> 
			<th width="100px">
				<fmt:message key="admin.user.userid"/>
			</th>
			<th>
				<fmt:message key="admin.user.login"/>
			</th>
			<th>
				<fmt:message key="admin.user.first_name"/>
			</th>
			<th>
				<fmt:message key="admin.user.last_name"/>
			</th>
			<th class="d-none d-lg-table-cell">
				<fmt:message key="admin.user.email"/>
			</th>
			<th>
				<fmt:message key="admin.user.actions"/>
			</th>
		</lams:TSTable5>		
			
		<div class="text-end">
			<a href="javascript:history.go(-1)" class="btn btn-secondary">
				<fmt:message key="admin.cancel"/>
			</a>
		</div>		
				
	</lams:Page5>

</body>
</lams:html>
