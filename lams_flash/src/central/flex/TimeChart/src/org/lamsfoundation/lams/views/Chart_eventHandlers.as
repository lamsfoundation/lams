/** Event handler functions for Chart.mxml **/
import flash.events.Event;
		
	private function chartControlHandler(event:ItemClickEvent):void {
		if(event.index == 0) {
			Button(event.currentTarget.getChildAt(1)).enabled = true;
			chartViews.selectedIndex = LEARNER;
		} else {
			chartViews.selectedIndex = CLASS;
			Button(event.currentTarget.getChildAt(0)).enabled = true;
		}
		
		Button(event.currentTarget.getChildAt(event.index)).enabled = false;
	}
	
	private function dataLearnerPlot(cat:mx.charts.CategoryAxis, item:Object):Object {
		return item.Activity;
	}
	
	private function labelLinAxis(item:Number, prevValue:Number, axis:IAxis):Object {
		var value:Number = item/60;
		return value.toFixed(1);
	}
	
	private function labelLearnerPlot(categoryValue:Object, previousCategoryValue:Object, axis:CategoryAxis, categoryItem:Object):String {
		return categoryItem.Title;
	} 
	
	private function learnerPlotDataTrip(hd:HitData):String {
		
		var formatStr:String = "<b>" + hd.item.Title + "</b>";
		
		formatStr += " " + completed_lbl + " <br>" + formatTime(hd.item.Completed);
		if(hd.item.Average) formatStr += "<br><i>" + learner_datatip_average + "</i>";
		
		return formatStr;
	}
	
	public function learnerPieDataTip(hd:HitData):String {
  		var formatStr:String = "<B>" + hd.item.Title + ": " 
  								+  Math.round(Number(PieSeriesItem(hd.chartItem).percentValue))
  								+ "</B>%<BR>" 
  								+ "(";
   		var duration:uint = hd.item.Duration;
		formatStr += formatTime(duration) + ")";
		
   		return formatStr;
	}
	
	public function clearSearch(event:Event):void {
		event.currentTarget.text = "";
		learnerProgressData.refresh();
	}
	
	public function applyFilter():void {
		
		if(learnerProgressData.length > 0)
			learnerProgressData.filterFunction = searchLearners;
		
		selectedLearner_cmb.selectedIndex = 0;
		selectedLearner_cmb.selectedItem = null;
		
		learnerProgressData.refresh();
		
		trace('applying filter: ' + learnerProgressData.length);
		
		var idx:int = learnerProgressData.getItemIndex(selectedLearnerProgress);
		
		if(idx >= 0)
			selectedLearner_cmb.selectedIndex = idx;
		else if(learnerProgressData.length > 0) {
			selectedLearner_cmb.selectedIndex = 1;
			selectLearner(selectedLearner_cmb.selectedItem);
		}
	}