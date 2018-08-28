<%@ include file="/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL /></c:set>

<c:set var="useInternalSMTPServer"><lams:Configuration key="InternalSMTPServer"/></c:set>
<c:set var="smtpServer"><lams:Configuration key="SMTPServer"/></c:set>

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
                ajaxUrl : "<c:url value='/usersearch.do'/>?dispatch=getPagedUsers&page={page}&size={size}&{sortList:column}&{filterList:fcol}",
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
							rows += 	'[<a href="user.do?method=edit&userId=' + orgData["userId"] + '">';
							rows += 		'<fmt:message key="admin.edit"/>';
							rows += 	'</a>]';
							rows += 	'&nbsp;';
							rows += 	'[<a href="user.do?method=remove&userId=' + orgData["userId"] + '">';
							rows += 		'<fmt:message key="admin.user.delete"/>';
							rows += 	'</a>]';
							rows += 	'&nbsp;';
							rows += 	'[<a href="<lams:LAMSURL/>/loginas.do?login=' + orgData["login"] + '">';
							rows += 		'<fmt:message key="label.login.as"/>';
							rows += 	'</a>]';
							rows += 	'&nbsp;';

							if (${(useInternalSMTPServer || not empty smtpServer)} && orgData["email"] != null && orgData["email"] != "") {
							rows += 	'[<a href="<lams:LAMSURL/>emailUser.do?method=composeMail&returnUrl=admin/usersearch.do&userID=' + orgData["userId"] + '">';
							rows += 		'<fmt:message key="label.email"/>';
							rows += 	'</a>]';
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

<p><a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
<a href="<lams:LAMSURL/>/admin/sysadminstart.do" class="btn btn-default loffset5"><fmt:message key="sysadmin.maintain" /></a></p>

<p><input class="btn btn-default" type="button" value="<fmt:message key="admin.user.create"/>" onclick=javascript:document.location='user.do?method=edit' />

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
		
