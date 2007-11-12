				
				function initLoadGroup(element, stateId, display, orgId) {
					jQuery(element).load(
						"displayGroup.do",
						{
							display: display,
							stateId: stateId, 
							orgId: orgId
						},
						function() {
							toggleGroupContents(element, stateId);
							registerToolTip(element);
						}
					);
				}
			
				function toggleGroupContents(element, stateId) {
					jQuery("a.j-group-header", element).click(function() {
						var row = jQuery(this).parent("h2").parent("div.left-buttons").parent("div.row");
						var orgId = jQuery(row).parent("div.course-bg").attr("id");
						var course = jQuery(row).next("div.j-course-contents");
						if (jQuery(course).html() == null) {
							loadGroupContents(orgId, stateId);
							saveCollapsed(orgId, "false");
						} else {
							var display = course.css("display");
							if (jQuery.browser.msie && jQuery.browser.version == '6.0') {
								course.slideToggle("fast");
							} else {
								course.toggle("fast");
							}
							if (display == "none") {
								saveCollapsed(orgId, "false");
							} else if (display == "block") {
								saveCollapsed(orgId, "true");
							}
						}
					});
				}
				
				function saveCollapsed(orgId, collapsed) {
					jQuery.ajax({
						url: "servlet/updateCollapsedGroup",
						data: {
							orgId: orgId, 
							collapsed: collapsed
						}
					});
				}
				
				function loadGroupContents(orgId, stateId) {
					jQuery.ajax({
						url: "displayGroup.do",
						data: {
							orgId: orgId, 
							stateId: stateId,
							display: 'contents'
						},
						success: function(html) {
							jQuery("#"+orgId).append(html);
							registerToolTip(this);
						}
					});
				}
				
				function registerToolTip(element) {
					jQuery("a.disabled-sequence-name-link, a.sequence-name-link", element).ToolTip({
						className: 'sequence-description-tooltip',
						position: 'mouse',
						delay: 300
					});
					jQuery("a.disabled-sequence-name-link, a.sequence-name-link", element).each(function(i, element) {
						var title = jQuery(element).attr("title");
						if (title!=null) {
							var newTitle = title.replace(/\r\n/g,"<BR>").replace(/\n/g,"<BR>")
							jQuery(element).attr("title", newTitle);
						}
					});
				}
				
				function makeSortable(element, acceptClass) {
					jQuery(element).Sortable({
						accept: acceptClass,
						axis: "vertically",
						containment: [jQuery(element).offset().left,
						jQuery(element).offset().top,
						jQuery(element).width(),
						jQuery(element).height()],
						onStop: function() {
							var ids = [];
							jQuery(this).parent().children("p").each(function(i, element) {
								ids.push(element.id);
							});
							var jLessonsId = jQuery(this).parent().attr("id");
							var dashIndex = jLessonsId.indexOf("-");
							var orgId = (dashIndex>0 ? jLessonsId.substring(0, dashIndex) : jLessonsId);
							jQuery.ajax({
								url: "servlet/saveLessonOrder",
								data: {
									orgId: orgId, 
									ids: ids.join(",")
								},
								error: function(a,b) {
									refresh();
								}
							});
						}
					});
				}
				