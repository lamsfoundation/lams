<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	$(document).ready(function(){
		CKEDITOR.on('instanceLoaded', function(e){
			//apply to only option title CKEditors
			var id = e.editor.element.$.id;
			if (id.startsWith("optionName")) {
				var tableId = id.replace("optionName", "option-table-");
				
				e.editor.on( 'focus', function() {
					//prevent expanding the already expanded table
					if (expandedTable == tableId) {
						return;
					}

					closeExpandedAreas();

					//hide toolbar until area is not expanded. otherwise it laggs during position change
					$("#cke_" + id).hide();

					$(".option-settings-hidden.delete-button", $("#" + tableId)).show();
					$(".option-settings-hidden:not(.delete-button)", $("#" + tableId)).slideDown()
					.promise().done(function() {
						//show toolbar after area got expanded
						$("#cke_" + id).show();
						//it's hack to make toolbar reposition in case it overlapped CKEditor after area expansion	
						CKEDITOR.instances[id].fire( 'change');
					});

					expandedTable = tableId;
			    } );
			}
		});

		//take care about short answer and numerical 
		$("[name^=optionName], [name^=numericalOption]").on("focus", function() {
			var tableId = $(this).attr("name").replace("optionName", "option-table-").replace("numericalOption", "option-table-");
			//prevent expanding the already expanded table
			if (expandedTable == tableId) {
				return;
			}

			closeExpandedAreas();

			$(".option-settings-hidden.delete-button", $("#" + tableId)).show();
			$(".option-settings-hidden:not(.delete-button)", $("#" + tableId)).slideDown();

			expandedTable = tableId;
		});

		// close all expanded areas on clicking anywhere outside of expanded table
		$(document).click(function(event) {
		    if (expandedTable
				    // allow clicking inside CKEditor area
				    && !$(event.target).closest("#" + expandedTable).length
				    // clicking on CKEditor toolbar
				    && !$(event.target).closest(".cke_top").length
				    // clicking on CKEditor toolbar
				    && !$(event.target).closest(".cke_dialog").length
				    ) {
		    	closeExpandedAreas();
		    }
		});

		//init all toogles (required in case of adding/removing options)
		if (${questionType == 8}) {
			$("input[type='checkbox'][name='optionCorrect']").bootstrapToggle();
		}
	});

	//store which table is expanded
	var expandedTable = "";
	function closeExpandedAreas() {
		//close all other expanded areas
		$(".option-settings-hidden").slideUp();
		expandedTable = "";
	}
</script>

<div id="optionArea">
	<input type="hidden" name="optionCount" id="optionCount" value="${fn:length(optionList)}">
	<input type="hidden" name="questionType" id="questionType" value="${questionType}" />
	
	<div id="option-table">
		<c:forEach var="option" items="${optionList}" varStatus="status">
			<table id="option-table-${status.index}" class="voffset10-bottom" data-id="${status.index}" tabindex="1">
				<tr>
					<td>
						<span>${status.index+1}</span>
					</td>
					
					<td>
						<c:choose>
							<c:when test="${questionType == 1}">
								<%@ include file="option.jsp"%>
							</c:when>
							<c:when test="${questionType == 2}">
								<%@ include file="matchingpairoption.jsp"%>
							</c:when>
							<c:when test="${questionType == 3}">
								<%@ include file="shortansweroption.jsp"%>
							</c:when>					
							<c:when test="${questionType == 4}">
								<%@ include file="numericaloption.jsp"%>
							</c:when>
							<c:when test="${questionType == 7}">
								<%@ include file="orderingoption.jsp"%>
							</c:when>
							<c:when test="${questionType == 8}">
								<%@ include file="optionhedgingmark.jsp"%>
							</c:when>
						</c:choose>	
					</td>
	
					<c:if test="${!isAuthoringRestricted}">
						<td width="30px">
							<i class="fa fa-trash delete-button option-settings-hidden" title="<fmt:message key="label.authoring.basic.delete" />"
								onclick="javascript:removeOption(${status.index})" style="display: none;"></i>
						</td>
					</c:if>
					
				</tr>
			</table>
		</c:forEach>
	</div>
</div>
