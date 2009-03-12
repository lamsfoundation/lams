package org.lamsfoundation.lams.business
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayCollection;
	
	import org.lamsfoundation.lams.common.dictionary.XMLDictionary;
	import org.lamsfoundation.lams.events.DictionaryEvent;
	import org.lamsfoundation.lams.vos.CompletedActivity;
	import org.lamsfoundation.lams.vos.LearnerProgress;
	
	public class ChartManager extends EventDispatcher
	{
		/*-.........................................Properties..........................................*/
		private var _progressList:ArrayCollection;
		
		private var _averageProgress:ArrayCollection;
		private var _targetFilterActivity:Number;
		
		private var _learnerProgress:LearnerProgress;
		
		private var _settings:Array;
		private var _loadType:String;
		private var _dictionary:XMLDictionary;
		
		/*-.........................................Constructor..........................................*/
		 public function ChartManager()
        {
        	_averageProgress = new ArrayCollection();
        }
        
		/*-.........................................Setters and Getters..........................................*/
		
		[Bindable (event="learnerProgressListChanged")]
		public function get progressList():ArrayCollection
		{
			return _progressList;
		}
		
		[Bindable (event="averageProgressListChanged")]
		public function get averageProgress():ArrayCollection
		{
			return _averageProgress;
		}
		
		[Bindable (event="learnerProgressChanged")]
		public function get learnerProgress():LearnerProgress
		{
			return _learnerProgress;
		}
		
		[Bindable (event="chartDictionaryChanged")]
		public function get dictionary():XMLDictionary
		{
			return _dictionary;
		}
		
		public function get loadType():String
		{
			return _loadType;
		}
		
		public function setDictionary(xml:XML):void {
			_dictionary = new XMLDictionary(xml);
		}
		
		public function setLabel(event:DictionaryEvent, key:String):void {
			event.result = _dictionary.getLabel(key);
		}

		/*-.........................................Methods..........................................*/
		
		// -----------------------------------------------------------
		public function updateProgressList(progressList:ArrayCollection):void {
			this._progressList = progressList;
			
			doAverage();
			
			dispatchEvent(new Event('learnerProgressListChanged'));
			
        	
		}
		
		public function doAverage():void {
			var average:ArrayCollection = new ArrayCollection();
        	averageFilter(average);
        	
        	
			// create data objects for completed activity and calculate average
			for each(var learnerProgress:LearnerProgress in this._progressList) {
				var c_count:int = 0;
				trace('progress user: ' + learnerProgress.learner_username);
				for each(var completedActivity:CompletedActivity in learnerProgress.completedActivities) {
					var _prev:CompletedActivity = (c_count > 0) ? learnerProgress.getCompletedActivity(c_count-1) : null;
					completedActivity.createDataObject(_prev, learnerProgress);
					
					updateAverage(completedActivity, average);
			
					c_count++;
				}
			}
			
			trace('updated progress list: ' + progressList.length);
			
			clearAverageFilter(average);
			
			trace('average list: ' + _averageProgress.length);
			_averageProgress = average;
			dispatchEvent(new Event('averageProgressListChanged'));
			
		}
		
		private function updateAverage(completedActivity:CompletedActivity, average:ArrayCollection):void {
			_targetFilterActivity = completedActivity.aid;
			average.refresh();
			
			if(average.length > 0) {
				var item:Object = average.getItemAt(0);
				
				item.Completed = (item.Completed + completedActivity.dataObject.Completed)/2;
				
				item.Duration = (item.Duration + completedActivity.dataObject.Duration)/2;
				
				trace('updating... avg ('  + item.Duration + ')');
			} else {
				var itemObj:Object = completedActivity.cloneDataObject("average");
				itemObj.Average = true;
				
				trace('adding... avg('  + itemObj.Duration + ')');
				
				average.addItem(itemObj);
			}
		}
		
		// ----------- filters -----------------------------
		private function averageFilter(average:ArrayCollection):void {
			average.filterFunction = calculateAverage;
			average.refresh();
		}
		
		private function clearAverageFilter(average:ArrayCollection):void {
			_targetFilterActivity = -1;
			average.filterFunction = null;
			average.refresh();
		}
		
		private function calculateAverage(item:Object):Boolean {
			if(_targetFilterActivity == -1)
				return true;
			else if(item.Activity == _targetFilterActivity)
				return true;
			else
				return false;
		}
		
		// -----------------------------------------------------------
		public function showLearnerProgress(learnerProgress:LearnerProgress):void {
			this._learnerProgress = learnerProgress;
			dispatchEvent(new Event('learnerProgressChanged'));
		}
		
		public function loadChartSettings(settings:Array):void {
			this._settings = settings;
			dispatchEvent(new Event('chartSettingsChanged'));
		}
		
		public function loadChartData(data:Object, type:String):void {
			var _newProgress:ArrayCollection = new ArrayCollection();
			
			_loadType = type;
			
			for each(var learnerProgress:Object in data)
				_newProgress.addItem(createNewProgress(learnerProgress));
				
			updateProgressList(_newProgress);
			
		}
		
		private function createNewProgress(newLearnerProgress:Object):LearnerProgress {
			var _newLearnerProgress:LearnerProgress = new LearnerProgress(newLearnerProgress);
			
			for each(var completedActivity:Object in newLearnerProgress.completedActivities) {
					var _completedActivity:CompletedActivity = new CompletedActivity(completedActivity);
					_newLearnerProgress.addCompletedActivity(_completedActivity);
			}
			
			return _newLearnerProgress;
				
		}
	}
}