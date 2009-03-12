package org.lamsfoundation.lams.business
{
	import flash.events.EventDispatcher;
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	
	import org.lamsfoundation.lams.events.*;
	import org.lamsfoundation.lams.vos.Search;
	
	
	public class SearchManager extends EventDispatcher
	{
		/*-.........................................Properties..........................................*/
		private var learnerProgress:ArrayCollection;
		
		/*-.........................................Constructor..........................................*/
		public function SearchManager()
		{
		}
		
		[Bindable (event="resultsUpdated")]
		public function get results():ArrayCollection {
			return learnerProgress;
		}
		
		/*-.........................................Methods..........................................*/
		
		public function search(query:Search):ArrayCollection {
			// load results from Search query
			var results:ArrayCollection = new ArrayCollection();
			
			trace("search query...");
			if(query != null) {
				// call search service
				//for each( var completedAct:ComplexActivity in ) {
				// if completedAct datetime match query conditions 
				// add to results
				//}
				
				trace("received query...");
			}
			
			learnerProgress = results;
			dispatchEvent(new Event('resultsUpdated'));
			
			return this.results;
		}
		
		public function clearResults():void {
			if(learnerProgress.length > 0)
				learnerProgress.removeAll();
		}
	}
}