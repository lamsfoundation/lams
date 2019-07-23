$(document).ready(function(){
	$('.selectpicker').on('changed.bs.select', function (e, clickedIndex, isSelected, previousValue) {
		gridSearch();
	});
});

//auxiliary formatter for jqGrid's question column
function questionNameFormatter (cellvalue, options, rowObject) {
   	var questionDescription = rowObject[2] ? rowObject[2] : "";

   	var text = "<div class='question-title-grid'>" + cellvalue + "</div>";
   	text += "<div class='question-description-grid small'>";
   	if (questionDescription.length > 0) {
   		text += questionDescription;
   	}
	text += "</div>"
	        	
	return text;
}

	    //search field handler
	    var timeoutHnd;
	    function doSearch(ev){
			//	var elem = ev.target||ev.srcElement;
	       	if(timeoutHnd)
	       		clearTimeout(timeoutHnd)
	       	timeoutHnd = setTimeout(gridSearch,500)
	    }
		function gridSearch(){
			$("#questions-grid").jqGrid(
				'setGridParam', {
		           	postData: { 
		           		questionTypes: "" + $("#types-select").val(),
		           		collectionUids: "" + $("#collections-select").val(),
			           	searchString: $("#filter-questions").val() 
			        }
		       	}, 
				{ page: 1 }
			).trigger('reloadGrid');

		    $("#question-detail-area").hide("slow").html("");
	    }
		
		function gridSearchHighlight () {
	  	  	//highlight search results
			if ($("#filter-questions").val()) {
				$('>tbody>tr.jqgrow>td:nth-child(2)', this).highlight($("#filter-questions").val());
			}
		}