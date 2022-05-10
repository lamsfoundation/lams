<% 
 /**
  * OutcomeAuthor.tag
  *	Author: Marcin Cieslak
  *	Description: Outcome selection in authoring
  */
 %>
<%@ tag body-content="scriptless" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-function" prefix="fn" %>
<%@ taglib uri="csrfguard" prefix="csrf" %>
<c:set var="lams"><lams:LAMSURL/></c:set>

<%-- Optional attributes. Must provide at either lessonId or toolContentId --%>
<%@ attribute name="lessonId" required="false" rtexprvalue="true" %>
<%@ attribute name="toolContentId" required="false" rtexprvalue="true" %>
<%@ attribute name="itemId" required="false" rtexprvalue="true" %>
<%@ attribute name="qbQuestionId" required="false" rtexprvalue="true" %>
<%@ attribute name="inPanel" required="false" rtexprvalue="true" %>

<%-- Support for multiple tags on one page --%>
<c:set var="outcomeTagId" value="${empty outcomeTagId ? 1 : outcomeTagId + 1}" />

<%-- ---------------------------------------%>
<style>
	#outcomeAuthor${outcomeTagId} .outcomeMappings div {
		display: inline-block;
	}
	
	#outcomeAuthor${outcomeTagId} input {
		margin-bottom: 0 !important;
	}
	
	.ui-autocomplete .ui-menu-item {
		cursor: pointer;
	}
</style>

<script type="text/javascript">
	// Adding jquery-ui.js if it hasn't been loaded already
	if (!jQuery.ui) {
		document.write('<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery-ui.js"><\/script>');
	}

	// create own set of IDs for Ajax calls
	var outcomeData${outcomeTagId} = {
			lessonId : '${lessonId}',
			toolContentId : '${toolContentId}',
			itemId : '${itemId}',
			qbQuestionId : '${qbQuestionId}'
		},
		// keep mapped outcome IDs for search result filtering
		outcomeMappingIds${outcomeTagId} = [],
		outcomeAddEnabled = true,
		
		outcomeExistingNoneLabel = '<fmt:message key="outcome.authoring.existing.none" />',
		outcomeCreateNewLabel = '<fmt:message key="outcome.authoring.create.new" />',
		outcomeMappingRemoveButton = '<i class="fa fa-remove loffset5"></i>',
		outcomeMappingRemoveConfirm = '<fmt:message key="outcome.authoring.remove.confirm" />';
	
	$(document).ready(function(){
		$('#outcomeSearchInput${outcomeTagId}').autocomplete({
			'source' : "<lams:LAMSURL/>outcome/outcomeSearch.do",
			'delay'  : 700,
			'minLength' : 2,
			'open'	: function(event, ui){
				$('.ui-autocomplete').addClass('dropdown-menu');
				$('.ui-menu-item').addClass('dropdown-item');
			},
			'classes' : {
				'ui-autocomplete' : 'dropdown-menu',
				'ui-menu-item'    : 'dropdown-item'
			},
			'response' : function(event, ui) {
				// filter out already mapped outcomes
				var index = ui.content.length - 1,
					// if result is empty, term and outcome add enabled come as objects (label, value)
					// if there are results, they come as simple values
					outcomeAddEnabled = ui.content[index] instanceof Object ? ui.content[index].label : ui.content[index],
					// convert to boolean
					outcomeAddEnabled = outcomeAddEnabled == 'true',
					term = ui.content[index - 1] instanceof Object ? ui.content[index - 1].label : ui.content[index - 1],
					sameNameFound = false;
				index--;
				ui.content.splice(index, 2);
				while(index--) {
					var label = ui.content[index].label;
					if (label.split('[')[0].trim() == term) {
						// do not offer to create an output which perfectly matches an existing one
						sameNameFound = true;
					}
					if (outcomeMappingIds${outcomeTagId}.includes(ui.content[index].value)){
						ui.content.splice(index, 1);
					}
				}
				if (outcomeAddEnabled && !sameNameFound) {
					ui.content.push({
						'label' : term + ' ' + outcomeCreateNewLabel
					});
				}
			},
			'select' : function(event, ui){
				var input = $(this);
				$.ajax({
					'url' : '<lams:LAMSURL/>outcome/outcomeMap.do?<csrf:token/>',
					'dataType' : 'text',
					'data': $.extend({
						'outcomeId' : ui.item.value,
						// if value is null, then it is a new outcome to create; remove ' [create new]' part before sending
						'name'		: ui.item.value ? null : ui.item.label.replace(outcomeCreateNewLabel, '')
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
		var outcomeData = outcomeData${outcomeTagId};
		
		$.ajax({
			'url' 	   : '<lams:LAMSURL/>outcome/outcomeGetMappings.do',
			'data'	   : outcomeData,
			'cache'    : false,
			'dataType' : 'json',
			'success'  : function(outcomeMappings) {
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
						var qbMapping = !outcomeData.qbQuestionId && outcomeData.toolContentId && this.qbMapping;
						$('<button type="button" class="mr-2 mt-2 btn btn-sm btn-info outcomeButton" />')
							.attr('mappingId', this.mappingId)
							.html(this.label + (qbMapping ? '' : outcomeMappingRemoveButton))
							.prop('disabled', qbMapping)
							.appendTo(mappingDiv)
							.click(function(){
								if (confirm(outcomeMappingRemoveConfirm)) {
									removeOutcomeMapping(this);
								}
							});
					});
				}
			}
		});
	}

	/**
	 * Removes an existing mapping
	 */
	function removeOutcomeMapping(button) {
		$.ajax({
			'url' : '<lams:LAMSURL/>outcome/outcomeRemoveMapping.do',
			'data': {
					'mappingId' :  $(button).attr('mappingId')
				}, 
			'type'  : 'post',
			'cache' : false,
			'success' : function() {
				refreshOutcomeMappings('${outcomeTagId}');
			}
		});
	}
</script>

<div id="outcomeAuthor${outcomeTagId}">
	<lams:Input id="outcomeSearchInput${outcomeTagId}" labelCellSize="3" labelKey="outcome.authoring.title" inputCellClass="justify-content-start flex-wrap">
		<div class="input-group">
			<div class="input-group-prepend">
		    	<span class="input-group-text"><i class="fa fa-search"></i></span>
		    </div>
		    <input type="text" id="outcomeSearchInput${outcomeTagId}" class="form-control" 
		    	   placeholder='<fmt:message key="outcome.authoring.input" />'></input>
		</div>
		<div class="outcomeMappings">
			<div class="mt-2 mr-2"><fmt:message key="outcome.authoring.existing" />: </div><div id="outcomeMappings${outcomeTagId}"></div>
		</div>
	</lams:Input>
</div>