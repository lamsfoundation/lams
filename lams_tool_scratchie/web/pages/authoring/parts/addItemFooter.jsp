<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
<c:set var="allowCreatingQbCollections"><%=Configuration.get(ConfigurationKeys.QB_COLLECTIONS_CREATE_ALLOW)%></c:set>

<script>
	function saveItem(isNewVersion){
		let form = $('#scratchieItemForm');
		if (isNewVersion) {
			action = form.attr('action');
			form.attr('action', action + '?newVersion=true');
		}
		form.submit();
	}

	$(document).ready(function (){
		$('#collection-uid-select').change(function(){
			let collectionSelect = $(this),
					newValue = collectionSelect.val(),
					previouslySelectedOption = $('option[selected]', collectionSelect);
			if (newValue == -1) {
				// create a new collection on the fly
				let newCollectionName = prompt('<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.questions.choice.collection.new.prompt" /></spring:escapeBody>'),
						newCollectionUid = -1;
				if (newCollectionName) {
					newCollectionName = newCollectionName.trim();
					$.ajax({
						'url' : '<lams:LAMSURL/>qb/collection/addCollection.do',
						'async' : false,
						'type' : 'post',
						'dataType' : 'text',
						'data' : {
							'name' : newCollectionName,
							'<csrf:tokenname/>' : '<csrf:tokenvalue/>'
						},
						success : function (response){
							if (!isNaN(response)) {
								newCollectionUid = +response;
							}
						},
						error : function (xhr) {
							alert(xhr.responseText);
						}
					});
				}

				if (newCollectionUid == -1 || newCollectionUid == 0) {
					// revert to previous selection
					previouslySelectedOption.prop('selected', true);
				} else {
					$('<option>').attr('value', newCollectionUid).attr('selected', 'selected')
							.prop('selected', true).text(newCollectionName).insertBefore($('option[value="-1"]', collectionSelect));
				}
			} else {
				previouslySelectedOption.removeAttr('selected');
				$('option[value="' + newValue + '"]', collectionSelect).attr('selected', 'selected');
			}
		});
	});
</script>

<footer class="footer fixed-bottom">
	<div class="panel-heading ">
		<div class="col-xs-12x col-md-6x form-groupx rowx form-inlinex btn-group-md voffset5">
			<%-- Hide if the question is not in users' collections  --%>
			<span <c:if test="${empty scratchieItemForm.userCollections}">style="visibility: hidden;"</c:if>>
	       		Collection
	        		
				<select class="btn btn-md btn-default" id="collection-uid-select">
					<c:forEach var="collection" items="${scratchieItemForm.userCollections}">
						<option value="${collection.uid}"
								<c:if test="${collection.uid == scratchieItemForm.oldCollectionUid}">selected="selected"</c:if>>
							<c:out value="${collection.name}" />
						</option>
					</c:forEach>
					<c:if test="${allowCreatingQbCollections}">
						<fmt:message key="label.questions.choice.collection.new.option" var="newOptionLabel"/>
						<option value="-1"><c:out escapeXml="true" value="${newOptionLabel}" /></option>
					</c:if>
				</select>
			</span>

			<div class="pull-right col-xs-12x col-md-6x" style="margin-top: -5px;">
				<a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.authoring.cancel.button" />
				</a>

				<div class="btn-group btn-group-sm dropup">
					<a id="saveButton" type="button" class="btn btn-sm btn-default button-add-item" onClick="javascript:saveItem(false)">
						<fmt:message key="label.authoring.save.button" />
					</a>
					<button id="saveDropButton" type="button" class="btn btn-default dropdown-toggle"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						<span class="caret"></span>
						<span class="sr-only">Toggle Dropdown</span>
					</button>
					<ul class="dropdown-menu">
						<li id="saveAsButton" onClick="javascript:saveItem(true)"><a href="#">
							<fmt:message key="label.authoring.save.new.version.button" />
						</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</footer>