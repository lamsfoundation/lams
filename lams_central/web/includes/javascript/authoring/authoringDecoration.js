/**
 * This file contains methods for Decoration manipulation on canvas.
 */

var DecorationLib = {
		
		/**
		 * Constructor for region annotation.
		 */
		Region : function(x, y, width, height, color, title) {
			this.title = title;
			
			this.draw = DecorationLib.methods.region.draw;
			this.fit = DecorationLib.methods.region.fit;
			this.loadPropertiesDialogContent = PropertyLib.regionProperties;
			
			this.draw(x, y, width, height, color);
		},
		
		
		/**
		 * Constructor for label annotation.
		 */
		Label : function(x, y, title){
			// set a default title, if none provided
			this.title = title || 'Label';
			
			this.draw = DecorationLib.methods.label.draw;
			this.loadPropertiesDialogContent = PropertyLib.labelProperties;
			
			this.draw(x, y);
		},
		
		
		methods : {
			
			/**
			 * Region methods
			 */
			region : {
				draw : function(x, y, x2, y2, color, title){
					// check for new coordinates or just take them from the existing shape
					var box = this.items && this.items.shape ? this.items.shape.getBBox() : null,
						x = x   ? x : box.x,
						y = y   ? y : box.y,
						// take into account minimal size of rectangle
						x2 = x2 ? (x2 < x + 20 ? x + 20 : x2) : x + box.width,
						y2 = y2 ? (y2 < y + 20 ? y + 20 : y2) : y + box.height,
						color =  color ? color : this.items.shape.attr('fill');
		
						if (box) {
							this.items.remove();
						}
						
						// the label
						this.items = paper.set();
						if (this.title) {
							var label = paper.text(x + 7, y + 10, this.title)
							 				 .attr('text-anchor', 'start')
							 				 .toBack();
							this.items.push(label);
						}
						
						// the rectangle
						this.items.shape = paper.path('M {0} {1} L {2} {1} L {2} {3} L {0} {3} z', x, y, x2, y2)
								 				.attr({
								 					'fill'    : color,
													'cursor'  : 'pointer'
												})
												.mousedown(HandlerLib.regionMousedownHandler)
												.click(HandlerLib.itemClickHandler)
												.toBack();
						this.items.push(this.items.shape);
						
						this.items.fitButton = paper.circle(x2 - 10, y + 10, 5)
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
																	 x2, y2 - 15, 15))
												.attr({
													'stroke' : null,
													'fill'   : 'blue',
													'cursor' : 'se-resize'
												})
												.mousedown(HandlerLib.resizeRegionStartHandler)
												.hide();
						this.items.push(this.items.resizeButton);
						
						this.items.data('parentObject', this);
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
		
		
		addRegion : function(x, y, x2, y2) {
			var region = new DecorationLib.Region(x, y, x2, y2, layout.colors.annotation);
			layout.regions.push(region);
			return region;
		},
		
		
		removeRegion : function(region) {
			layout.regions.splice(layout.regions.indexOf(region), 1);
			region.items.remove();
		},
		
		
		/**
		 * Get activities enveloped by given shape (usually annotation region)
		 */
		getChildActivities : function(shape){
			var result = [];
			$.each(layout.activities, function(){
				var activityBox = this.items.shape.getBBox();
				
				if (shape.isPointInside(activityBox.x, activityBox.y)
						&& shape.isPointInside(activityBox.x2, activityBox.y2)) {
					result.push(this);
				}
			});
			
			return result;
		},
		
		
		addLabel : function(x, y, title) {
			var label = new DecorationLib.Label(x, y, title);
			layout.labels.push(label);
			return label;
		},
		
		
		removeLabel : function(label) {
			layout.labels.splice(layout.labels.indexOf(label), 1);
			label.items.remove();
		}
};