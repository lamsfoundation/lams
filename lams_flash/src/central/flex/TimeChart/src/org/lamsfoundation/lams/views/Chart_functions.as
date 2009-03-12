/** General functions for Chart.mxml **/
import flash.events.Event;

import mx.collections.ArrayCollection;

import org.lamsfoundation.lams.vos.LearnerProgress;			
	
	public function set averageProgressList(averageProgressList:ArrayCollection):void {
		_averageProgressList = averageProgressList;
		
		if(_averageProgressList.length > 0)
		trace('avg first: ' + _averageProgressList.getItemAt(0).Completed);
		
		if(_averageProgressList.length > 0)
			showAverage.enabled = true;
	}
	
	private function getCompletedLabel(categoryValue:Object, previousCategoryValue:Object, axis:CategoryAxis, categoryItem:Object):String 
	{
		return CompletedActivity(categoryValue).title;
	}
	
	public function loadSelectedLearner():void {
		_learnerProgressList = new ArrayCollection();
		
		if(selectedLearnerProgress == null) {
			selectLearner(learnerProgressData.getItemAt(0));
			return;
		}
		
		chart_lbl.htmlText = "<b>" + learner_lbl + " :: " + selectedLearnerProgress.learner_fname + " " + selectedLearnerProgress.learner_lname + "</b>";
		
		for each(var cp:CompletedActivity in selectedLearnerProgress.completedActivities) {
			_learnerProgressList.addItem(cp.dataObject);
				
			trace('adding new c: ' + cp.dataObject.Completed);
			trace('adding new t: ' + cp.dataObject.Title);
			trace('adding new d: ' + cp.dataObject.Duration);
		} 
		
	}
	
	public function selectActivity(item:Object):void {
	}
	
	private function selectLearner(item:Object):void {
		// throw event to load selected learner
		var event:ChartEvent = new ChartEvent(ChartEvent.SELECT);
		event.learnerProgress = item as LearnerProgress;
		
		this.dispatchEvent(event);
	}
	
	private function selectLearnerEvent(evt:Event):void {
		selectLearner(evt.currentTarget.selectedItem);
	}
	
	private function learnerName(data:Object):String {
		return data.learner_fname + " " + data.learner_lname;
	}
	
	/**
	 * Format Time value
	 * @param value amount of seconds to be formats to string
	 * @return formatted time value
	 **/
	private function formatTime(value:Number):String {
		var formatStr:String = "";
		
		//Find The Seconds
	    var Seconds:Number = value%60;
	   
	    //Find The Minutes
	    var Minutes:Number = (value/60);
	   
	    //Find The Hours
	    var Hours:Number = (value/3600);
   		
   		if(Hours > 1) 
   			formatStr += Hours.toFixed(0) + " " + chart_time_hrs_lbl + " ";
   			
   		if(Minutes > 1) formatStr += Number(Minutes).toFixed(1) + " " + chart_time_mins_lbl + " ";
   		else formatStr += Seconds + " " + chart_time_secs_lbl;
   		
   		return formatStr;
	}
	
	// filter function for learner search
	public function searchLearners(item:Object):Boolean {
		var lp:LearnerProgress = item as LearnerProgress;
		var name:String = learnerName(lp);
		
		trace('searching... : ' + name);
		
		if(learnerName(lp).search(searchLearner_txt.text) >= 0)
			return true;
	
		return false;
	}

	
