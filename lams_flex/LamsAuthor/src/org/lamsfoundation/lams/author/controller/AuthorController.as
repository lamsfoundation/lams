package org.lamsfoundation.lams.author.controller
{
	import flash.utils.Dictionary;
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	
	import org.lamsfoundation.lams.author.model.learninglibrary.LearningLibraryEntry;
	
	public class AuthorController
	{
		
		public function AuthorController()
		{
		}
		
		
		public function setLearningLibrary(learningLibraryIn:ArrayCollection):void {
			
			var learningLibrary:Dictionary = new Dictionary();
			
			for each(var learningLibraryEntryObj:Object in learningLibraryIn) {
	   			var learningLibraryEntry:LearningLibraryEntry = new LearningLibraryEntry(learningLibraryEntryObj);
	   			learningLibrary[learningLibraryEntry.learningLibraryID] = learningLibraryEntry;

	   		}
	   		
   			
   			// Set the application learning library
   			Application.application.learningLibrary = learningLibrary;
   			Application.application.canvasArea.compLearningLibrary.loadLearningLibrary();
   			Application.application.canvasArea.compLearningLibrary2.loadLearningLibrary();
			
		}
		
		
	}
}