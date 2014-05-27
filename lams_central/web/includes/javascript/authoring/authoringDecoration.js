/**
 * This file contains methods for Decoration manipulation on canvas.
 */

var DecorationLib = {
		
		/**
		 * Abstract class for Region, Optional and Floating Activities.
		 */
		Container : function(id, uiid, title) {
			this.id = +id || null;
			this.uiid = +uiid || (layout.ld ? ++layout.ld.maxUIID : null);
			this.title = title;
			this.childActivities = [];
			
			this.drawContainer = DecorationLib.methods.container.draw;
			this.fit = DecorationLib.methods.container.fit;
		},
		
		/**
		 * Constructor for region annotation.
		 */
		Region : function(id, uiid, x, y, x2, y2, title, color) {
			DecorationLib.Container.call(this, id, uiid, title);
			// we don't use it for region
			this.childActivities = null;
			
			this.draw = DecorationLib.methods.region.draw;
			this.loadPropertiesDialogContent = PropertyLib.regionProperties;
			
			this.draw(x, y, x2, y2, color);
		},
		
		
		/**
		 * Constructor for label annotation.
		 */
		Label : function(id, uiid, x, y, title){
			this.id = +id || null;
			this.uiid = +uiid || ++layout.ld.maxUIID;
			// set a default title, if none provided
			this.title = title || LABELS.DEFAULT_ANNOTATION_LABEL_TITLE;
			
			this.draw = DecorationLib.methods.label.draw;
			this.loadPropertiesDialogContent = PropertyLib.labelProperties;
			
			this.draw(x, y);
		},
		
		
		methods : {
			container : {
				
				draw : function(x, y, x2, y2, color){
					// check for new coordinates or just take them from the existing shape
					var box = this.items ? this.items.shape.getBBox() : null,
						x = x   ? x : box.x,
						y = y   ? y : box.y,
						// take into account minimal size of rectangle
						x2 = x2 ? Math.max(x2, x + 20) : x + box.width,
						y2 = y2 ? Math.max(y2, y + 20) : y + box.height,
						color =  color ? color : this.items.shape.attr('fill');
		
					if (box) {
						this.items.remove();
					}
					
					// the label
					this.items = paper.set();
					if (this.title) {
						var label = paper.text(x + 7, y + 10, this.title)
						 				 .attr({
						 					 'text-anchor' : 'start',
						 					 'cursor'  : 'pointer'
						 				 })
						 				 .toBack();
						this.items.push(label);
						
						// make sure title fits
						x2 = Math.max(x2, label.getBBox().x2 + 5);
					}
					
					// the rectangle
					this.items.shape = paper.path('M {0} {1} L {2} {1} L {2} {3} L {0} {3} z', x, y, x2, y2)
							 				.attr({
							 					'fill'    : color,
												'cursor'  : 'pointer'
											})
											.toBack();
					this.items.push(this.items.shape);
					
					this.items.mousedown(HandlerLib.containerMousedownHandler)
							  .click(HandlerLib.itemClickHandler);
				},	
		
				/**
				 * Adjust the annotation so it envelops its child activities and nothing more.
				 */
				fit : function() {
					var childActivities = DecorationLib.getChildActivities(this.items.shape);
					if (childActivities.length == 0) {
						return;
					}
		
					ActivityLib.removeSelectEffect(this);
					
					var allElements = paper.set();
					$.each(childActivities, function(){
						allElements.push(this.items.shape);
					});
					// big rectangle enveloping all child activities
					var box = allElements.getBBox();
					
					// add some padding
					this.draw(box.x - 20, box.y - 20, box.x2 + 20, box.y2 + 20);
				}
			},
			
			/**
			 * Region methods
			 */
			region : {
				draw : function(x, y, x2, y2, color){
					this.drawContainer(x, y, x2, y2, color);
					
					var box = this.items.shape.getBBox();
					
					this.items.fitButton = paper.circle(box.x2 - 10, box.y + 10, 5)
										 .attr({
											'stroke' : null,
											'fill'   : 'blue',
											'cursor' : 'pointer',
											'title'  : 'Fit'
										 })
										 .click(function(event){
											event.stopImmediatePropagation();
											var region = this.data('parentObject');
											region.fit();
											ActivityLib.addSelectEffect(region, true);
										 })
										 .hide();
					this.items.push(this.items.fitButton);
					
					this.items.resizeButton = paper.path(Raphael.format('M {0} {1} v {2} h -{2} z',
																 box.x2, box.y2 - 15, 15))
											.attr({
												'stroke' : null,
												'fill'   : 'blue',
												'cursor' : 'se-resize'
											})
											.mousedown(HandlerLib.resizeRegionStartHandler)
											.hide();
					this.items.push(this.items.resizeButton);
					
					this.items.data('parentObject', this);
				}
			},
			
			
			/**
			 * Label methods
			 */
			label : {
				draw : function(x, y) {
					var x = x ? x : this.items.shape.getBBox().x,
						// the Y coordinate has to be adjusted;
						// it is not perfect and not really cross-browser compliant...
						y = y ? y : this.items.shape.getBBox().y + 6;
					
					if (this.items) {
						this.items.remove();
					}
					
					this.items = paper.set();
					this.items.shape = paper.text(x, y, this.title)
											.attr({
												'text-anchor' : 'start',
												'cursor'	  : 'pointer'
											})
											.mousedown(HandlerLib.labelMousedownHandler)
											.click(HandlerLib.itemClickHandler);
					this.items.push(this.items.shape);
					
					this.items.data('parentObject', this);
				}
			}
		},
		
		
		addRegion : function(x, y, x2, y2, title, color) {
			var region = new DecorationLib.Region(null, null,
							 x, y, x2, y2, title, color ? color : layout.colors.annotation);
			layout.regions.push(region);
			setModified(true);
			return region;
		},
		
		
		removeRegion : function(region) {
			layout.regions.splice(layout.regions.indexOf(region), 1);
			region.items.remove();
			setModified(true);
		},
		
		
		/**
		 * Get activities enveloped by given container
		 */
		getChildActivities : function(shape){
			var result = [];
			$.each(layout.activities, function(){
				if (shape != this.items.shape) {
					var activityBox = this.items.shape.getBBox(),
						shapeBox = shape.getBBox();
					
					if (Raphael.isPointInsideBBox(shapeBox,activityBox.x, activityBox.y)
						&& Raphael.isPointInsideBBox(shapeBox, activityBox.x2, activityBox.y2)) {
						result.push(this);
					}
				}
			});
			
			var parentObject = shape.data('parentObject');
			if (parentObject && !(parentObject instanceof DecorationLib.Region)) {
				parentObject.childActivities = result;
			}
			return result;
		},
		
		
		addLabel : function(x, y, title) {
			var label = new DecorationLib.Label(null, null, x, y, title);
			layout.labels.push(label);
			setModified(true);
			return label;
		},
		
		
		removeLabel : function(label) {
			layout.labels.splice(layout.labels.indexOf(label), 1);
			label.items.remove();
			setModified(true);
		}
};

// set prototype hierarchy
DecorationLib.Region.prototype = new DecorationLib.Container;
ActivityLib.ParallelActivity.prototype = new DecorationLib.Container;
ActivityLib.OptionalActivity.prototype = new DecorationLib.Container;
ActivityLib.FloatingActivity.prototype = new DecorationLib.Container;