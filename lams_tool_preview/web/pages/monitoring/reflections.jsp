<%@ include file="/common/taglibs.jsp"%>

<c:set var="lams"><lams:LAMSURL /></c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}"/>
<c:set var="summaryList" value="${sessionMap.summaryList}"/>
<c:set var="localeLanguage"><lams:user property="localeLanguage" /></c:set>

<lams:html>
<lams:head>
	<title><fmt:message key="label.monitoring.heading" />
	</title>
	<%@ include file="/common/header.jsp"%>
	
	<link type="text/css" href="${lams}css/jquery-ui-smoothness-theme.css" rel="stylesheet">
	<link type="text/css" href="${lams}css/jquery.jqGrid.css" rel="stylesheet" />
	<link rel="stylesheet" href="<html:rewrite page='/includes/css/learning.css'/>">
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.locale-en.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.jqGrid.js"></script>

	<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/jquery.timeago.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL />/includes/javascript/timeagoi18n/jquery.timeago.${fn:toLowerCase(localeLanguage)}.js"></script>

	<script type="text/javascript">
	
	$(document).ready(function(){

			debugger;
			
			jQuery("#group${toolSessionId}").jqGrid({
			   	url: "<c:url value='/monitoring/getReflections.do'/>?toolSessionId=${toolSessionId}&sessionMapID=${sessionMapID}",
				datatype: "json",
				height: 'auto',
				autowidth: true,
				shrinkToFit: false,
			   	colNames:[
						'itemId',
						'<fmt:message key="label.user.name" />',
						'<fmt:message key="title.reflection" />'
				],
			   	colModel:[
			   		{name:'itemId', index:'itemId', width:0, hidden: true},
			   		{name:'itemDescription', index:'itemDescription', width:100, searchoptions: { clearSearch: false }},
			   		{name:'notebook', index:'notebook', width:200, search:false}
			   	],
			   	rowNum:10,
			   	rowList:[10,20,30,40,50,100],
			   	pager: '#pager${toolSessionId}',
			   	viewrecords:true,
				loadComplete: function(){
					$("time.timeago").timeago();
				},
			   	// caption: "${groupSummary.sessionName}" use Bootstrap panels as the title bar
				subGrid: false
			}).jqGrid('filterToolbar', { 
				searchOnEnter: false
			})
			.navGrid('#pager${toolSessionId}',{add:false,del:false,edit:false,search:false});
        
		
        //jqgrid autowidth (http://stackoverflow.com/a/1610197)
        $(window).bind('resize', function() {
            resizeJqgrid(jQuery(".ui-jqgrid-btable:visible"));
        });

        //resize jqGrid on openning of bootstrap collapsible
        $('div[id^="collapse"]').on('shown.bs.collapse', function () {
            resizeJqgrid(jQuery(".ui-jqgrid-btable:visible", this));
        })

        function resizeJqgrid(jqgrids) {
            jqgrids.each(function(index) {
                var gridId = $(this).attr('id');
                var gridParentWidth = jQuery('#gbox_' + gridId).parent().width();
                jQuery('#' + gridId).setGridWidth(gridParentWidth, true);
            });
        };
        setTimeout(function(){ window.dispatchEvent(new Event('resize')); }, 300);
	});
	
    </script>
        
</lams:head>
<body class="stripes">

	<c:set var="title"><fmt:message key="title.reflection"/></c:set>

	<c:forEach var="groupSummary" items="${summaryList}" varStatus="status">
		<c:if test="${groupSummary.sessionId eq toolSessionId}" >
			<c:set var="title">${groupSummary.sessionName}</c:set>
		</c:if>
	</c:forEach>

	<lams:Page type="monitor" title="${title}">

		<table id="group${toolSessionId}" class="scroll" cellpadding="0" cellspacing="0"></table>
		<div id="pager${toolSessionId}"></div> 
	
		<span onclick="window.close()" class="btn btn-default voffset5 pull-right"><fmt:message key="label.close"/></span>
	
	</lams:Page>
	<!--closes content-->

	<div id="footer">
	</div>
	<!--closes footer-->

</body>

</lams:html>