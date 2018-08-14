<% 
 /**
  * Outcome.tag
  *	Author: Marcin Cieslak
  *	Description: Outcome selection in authoring
  */
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<c:set var="lams"><lams:LAMSURL/></c:set>

<%-- Optional attributes. Must provide at either lessonId or toolContentId --%>
<%@ attribute name="lessonId" required="false" rtexprvalue="true" %>
<%@ attribute name="toolContentId" required="false" rtexprvalue="true" %>
<%@ attribute name="itemId" required="false" rtexprvalue="true" %>

<%-- Support for multiple tags on one page --%>
<c:set var="outcomeTagId" value="${empty outcomeTagId ? 1 : outcomeTagId + 1}" />

<%-- ---------------------------------------%>
<script type="text/javascript">
	// Adding jquery-ui.js if it hasn't been loaded already
	if (!jQuery.ui) {
		document.write('<link href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" rel="stylesheet" type="text/css" />');
		document.write('<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"><\/script>');
	}

	// create own set of IDs for Ajax calls
	var outcomeData${outcomeTagId} = {
			lessonId : '${lessonId}',
			toolContentId : '${toolContentId}',
			itemId : '${itemId}'
		},
		// keep mapped outcome IDs for search result filtering
		outcomeMappingIds${outcomeTagId} = [],
		
		outcomeExistingNoneLabel = '<fmt:message key="advanced.outcome.existing.none" />',
		outcomeMappingRemoveButton = '<i class="fa fa-remove loffset5" style="cursor: pointer" onClick="javascript:removeOutcomeMapping(this)"></i>';
	
	$(document).ready(function(){
		$('#outcomeSearchInput${outcomeTagId}').autocomplete({
			'source' : "<lams:LAMSURL/>outcome.do?method=outcomeSearch&organisationIds=${organisations}",
			'delay'  : 700,
			'minLength' : 2,
			'response' : function(event, ui) {
				// filter out already mapped outcomes
				var index = ui.content.length;
				while(index--) {
					if (outcomeMappingIds${outcomeTagId}.includes(ui.content[index].value)){
						ui.content.splice(index, 1);
					}
				}
			},
			'select' : function(event, ui){
				var input = $(this);
				$.ajax({
					'url' : '<lams:LAMSURL/>outcome.do',
					'data': $.extend({
						'method' : 'outcomeMap',
						'outcomeId' : ui.item.value
					}, outcomeData${outcomeTagId}),
					'method' : 'post',
					'cache' : false,
					'success' : function() {
						// if add was successful, refresh added mappings
						input.val("");
						refreshOutcomeMappings('${outcomeTagId}');
					}
				});
				return false;
			}
		});

		// on start load existing mappings
		refreshOutcomeMappings('${outcomeTagId}');
	});

	/**
	 * Loads existing mappings
	 */
	function refreshOutcomeMappings(outcomeTagId) {
		$.ajax({
			'url' : '<lams:LAMSURL/>outcome.do',
			'data': $.extend({
				'method' : 'outcomeGetMappings'
			}, outcomeData${outcomeTagId}),
			'cache' : false,
			'dataType' : 'json',
			'success' : function(outcomeMappings) {
				// clear mappings list
				var mappingDiv = $('#outcomeMappings' + outcomeTagId).empty();
				// clear cached outcome IDs
				outcomeMappingIds${outcomeTagId} = [];
				if (outcomeMappings.length == 0) {
					// display "none" label
					$(mappingDiv).append(outcomeExistingNoneLabel);
				} else {
					$.each(outcomeMappings, function(){
						// cache already mapped outcomes
						outcomeMappingIds${outcomeTagId}.push(this.outcomeId);
						// add a label with outcome information
						$('<span class="roffset10" />').attr('mappingId', this.mappingId).html(this.label + outcomeMappingRemoveButton).appendTo(mappingDiv);
					});
				}
			}
		});
	}

	/**
	 * Removes an existing mapping
	 */
	function removeOutcomeMapping(button) {
		var mappingId = $(button).parent().attr('mappingId');
		$.ajax({
			'url' : '<lams:LAMSURL/>outcome.do',
			'data': {
					'method' : 'outcomeRemoveMapping',
					'mappingId' : mappingId
				}, 
			'cache' : false,
			'success' : function() {
				refreshOutcomeMappings('${outcomeTagId}');
			}
		});
	}
</script>

<lams:SimplePanel titleKey="advanced.outcome.title">
	<label>
		<fmt:message key="advanced.outcome.input" />
		<input type="text" id="outcomeSearchInput${outcomeTagId}" class="ui-autocomplete-input loffset20"></input>
	</label>
	<br />
	<fmt:message key="advanced.outcome.existing" />: <span id="outcomeMappings${outcomeTagId}"></span>
</lams:SimplePanel>