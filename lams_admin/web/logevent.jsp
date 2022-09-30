<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<c:set var="lams"><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="label.event.log"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme5.css">
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.theme.bootstrap5.css"/>
	<link rel="stylesheet" href="${lams}css/jquery-ui.timepicker.css" />
	<link rel="stylesheet" href="${lams}css/jquery.tablesorter.pager.css" />
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery-ui.timepicker.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter.js"></script> 
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-widgets.js"></script> 
	<script type="text/javascript" src="${lams}includes/javascript/jquery.tablesorter-pager.js"></script> 
	<script type="text/javascript" src="${lams}includes/javascript/bootstrap5.bundle.min.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/portrait.js" ></script>
	<script type="text/javascript">
	
		<fmt:message key="label.lesson.with.name" var="LESSON_LABEL_VAR"><fmt:param value="{0}"/></fmt:message>
		const LESSON_LABEL = '<c:out value="${LESSON_LABEL_VAR}" />';
		<fmt:message key="label.activity.with.name" var="ACTIVITY_LABEL_VAR"><fmt:param value="{0}"/></fmt:message>
		const ACTIVITY_LABEL = '<c:out value="${ACTIVITY_LABEL_VAR}" />';
	
		var areaMenu, 
			typeMenu,
			areaTypeMenus = {},
			areaOptions = [],
			typeDescriptions = {};
		
		$(document).ready(function(){
			<c:forEach var="eventType" items="${eventLogTypes}">
				addEventType("${eventType.id}","${eventType.description}","${eventType.areaCode}","${eventType.areaDescription}");
			</c:forEach>		
			
			areaMenu = document.getElementById("areaMenu");
			areaMenu.options[0] = new Option('<fmt:message key="label.select.topic"/>','');
			for ( i=1; i <= areaOptions.length; i++) {
				areaMenu.options[i] = areaOptions[i-1];
			}
			typeMenu = document.getElementById("typeMenu");
			
			$("#endDatePicker").datepicker();
			var now = new Date();
			$("#endDatePicker").datepicker( "setDate", now );
			$("#startDatePicker").datepicker();
			var startDateParts = "${startDate}".split("-"); 	// YYYY-MM-DD
			if ( startDateParts.length == 3 ) {
				var startDate = new Date(startDateParts[0], startDateParts[1]-1, startDateParts[2]);
				$("#startDatePicker").datepicker( "setDate", startDate );
			} else {
				$("#startDatePicker").datepicker( "setDate",  now);
			}
			
			$(".tablesorter").tablesorter({
				theme: 'bootstrap',
				headerTemplate : '{content} {icon}',
			    sortInitialOrder: 'desc',
			    sortList: [[0]],
			    widgets: [ "uitheme", "resizable", "filter" ],
			    headers: { 0: { filter: false, sorter: true}, 1: { filter: false, sorter: false}, 2: { filter: true, sorter: true}, 3: { filter: true, sorter: true}, 4: { filter: true, sorter: false},  }, 
			    sortList : [[0,1]],
			    showProcessing: true,
			    widgetOptions: {
			    	resizable: true,
			    	// include column filters 
			        filter_columnFilters: true, 
			        filter_placeholder: { search : '<fmt:message key="label.search"/>' }, 
			        filter_searchDelay: 700 
			    }
			});
	
			$(".tablesorter").each(function() {
				$(this).tablesorterPager({
					processAjaxOnInit: false, 
					initialRows: {
				        total: 10,
				        filtered: 10
				      },
					savePages: false,
			        container: $(this).find(".ts-pager"),
			        output: '{startRow} to {endRow} ({totalRows})',
			        cssPageDisplay: '.pagedisplay',
			        cssPageSize: '.pagesize',
			        cssDisabled: 'disabled',
					ajaxUrl : "<c:url value='../logevent/getEventLog.do?sessionMapID=${sessionMapID}&page={page}&size={size}&{sortList:column}&{filterList:fcol}&s'/>",
					ajaxProcessing: function (data, table) {
				    	if (data && data.hasOwnProperty('rows')) {
				    		var rows = [],
				            json = {};
				    		
							for (i = 0; i < data.rows.length; i++){
								var logData = data.rows[i];
								
								rows += '<tr>';
	
								rows += '<td>';
								rows += 	logData['dateOccurred'];
								rows += '</td>';
	
								rows += '<td>';
								var typeId = logData['typeId'];
								var typeDescription;
								if ( typeId )
									typeDescription = typeDescriptions[typeId];
								if ( ! typeDescription ) {
									// this should never occur but just in case
									typeDescription = '[<fmt:message key="label.unknown"/>'+typeId+']';
								}
								rows += typeDescription;
								rows += '</td>';
	
								rows += '<td>';
								if ( logData['userId'] ) {
									rows += 	definePortraitPopover(logData['userPortraitId'], logData['userId'], logData['userName'], logData['userName']);
								} else {
									rows += '-';
								}
								rows += '</td>';
	
								rows += '<td>';
								if ( logData['targetUserId'] ) {
									rows += 	definePortraitPopover(logData['targetUserPortraitId'], logData['targetUserId'], logData['targetUserName'], logData['targetUserName']);
								} else {
									rows += '-';
								}
								rows += '</td>';
	
								rows += '<td>';
								var lesson = null, activity = null, description = null, lineCount = 0;
								if ( logData['lessonId'] ) {
									lesson = LESSON_LABEL.replace('{0}',logData['lessonName']).replace('{1}',logData['lessonId']);
									lineCount++;
								}
								if ( logData['activityId'] ) {
									activity = ACTIVITY_LABEL.replace('{0}',logData['activityName']).replace('{1}',logData['activityId']);
									lineCount++;
								}
								if ( logData['description'] ) {
									description = logData['description'];
									lineCount++;
								}
								if ( lesson != null ) {
									rows+=lesson; 
									if ( lineCount > 1 )
										rows+='<BR/>';
								}
								if ( activity != null ) {
									rows+=activity; 
									if ( lineCount > 1 )
										rows+='<BR/>';
								}
								if ( description != null ) {
									rows+=description; 
								}
								rows += '</td>';
	
								rows += '</tr>';
							}
				            
							json.total = data.total_rows;
							json.rows = $(rows);
							return json;
				            
				    		}
					},
					customAjaxUrl: function(table, url) {
						var startDate = $("#startDatePicker").datepicker("getDate");
						if ( startDate )
							url += "&startDate="+startDate.getTime();
						var endDate = $("#endDatePicker").datepicker("getDate");
						if ( endDate )
							url += "&endDate="+endDate.getTime();
						var area = $("#areaMenu").val();
						if ( area )
							url += "&area="+area;
						var typeId = $("#typeMenu").val();
						if ( typeId )
					    	  	url += "&typeId="+typeId;
				        return url;
				   	},
				  })
				  .bind('pagerInitialized pagerComplete', function(event, options){
					  if ( options.totalRows == 0 ) {
						  $.tablesorter.showError($(this), '<fmt:message key="label.no.data.found"/>');
					  } else {
						initializePortraitPopover('${lams}');
					  }
	 				})
				 
				});
	
		}); // end document.ready
		
		function addEventType(typeId, typeDescription, areaCode, areaDescription) {
			var areaTypes = areaTypeMenus[areaCode];
			if ( ! areaTypes ) {
				areaTypes = [];
				areaTypes.push(new Option('<fmt:message key="label.select.type"/>',''));
				areaTypeMenus[areaCode] = areaTypes;
				areaOptions.push(new Option(areaDescription, areaCode));
			}
			areaTypes.push(new Option(typeDescription, typeId));
			typeDescriptions[typeId] = typeDescription;
		}
		
		function configureTypeDropdown( areaCode ) {
	        typeMenu.length = 0;
	        var newTypeOptions = areaTypeMenus[areaCode];
	        if ( newTypeOptions ) {
		    		for ( i=0; i < newTypeOptions.length; i++) {
		    			typeMenu.options[i] = newTypeOptions[i];
		    		}
	        }
	        typeMenu[0].selected = true;
		}
		
		function getEvents() {
			$(".tablesorter").trigger('pagerUpdate', 1);
		}
	 </script>
</lams:head>
    
<body class="component pb-4">
	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message key="label.event.log"/></c:set>

	<lams:Page5 type="admin" title="${title}" breadcrumbItems="${breadcrumbItems}">
	
		<form class="row row-cols-lg-auto g-3 align-items-center">
  			<div class="col-12">
				<select id="areaMenu" class="form-select form-select-sm d-inline" onchange="javascript:configureTypeDropdown(this.value)"></select>
			</div>
			<div class="col-12">
				<select id="typeMenu" class="form-select form-select-sm d-inline" aria-label="Type of event"></select>
			</div>
			<div class="col-12">
				<span class="d-inline px-4"><fmt:message key="label.between.dates"/></span>
			</div>
			<div class="col-12">
				<input type="text" class="form-control form-control-sm" name="startDatePicker" id="startDatePicker" aria-label="Start date" value=""/>
			</div>
			<div class="col-12">
				<input type="text" class="form-control form-control-sm" name="endDatePicker" id="endDatePicker" aria-label="End date" value=""/>
			</div>
			<div class="col-12">
				<a href="#" class="btn btn-primary btn-sm" onclick="javascript:getEvents()"><fmt:message key="admin.search"/></a>
			</div>
		</div>
		
		<div class="mt-3 bg-white">	
			<lams:TSTable5 numColumns="5" dataId="data-session-id='1'">
					<th style="width:20%"><fmt:message key="label.date"/></th>
					<th class="no-spinner"><fmt:message key="label.event.type"/></th>
					<th class="no-spinner" style="width:12%"><fmt:message key="audit.change.made.by"/></th>
					<th class="no-spinner" style="width:12%"><fmt:message key="audit.change.made.to"/></th>
					<th class="no-spinner"><fmt:message key="audit.remarks"/></th>
			</lams:TSTable5>
		</div>
		<div class="text-end">
			<a href="<lams:LAMSURL/>admin/appadminstart.do" class="btn btn-secondary">
				<fmt:message key="admin.cancel"/>
			</a>
		</div>		

	</lams:Page5>
</body>
</lams:html>
