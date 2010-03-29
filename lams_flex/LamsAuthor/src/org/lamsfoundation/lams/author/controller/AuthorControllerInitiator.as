package org.lamsfoundation.lams.author.controller
{
	import mx.collections.ArrayCollection;
	
	
	/**
	 * @author lfoxton
	 * 
	 * Helper class for author controller so that mate
	 * can initiate values without calling the constructor
	 * for AuthorController (which ruins its singleton status)
	 * 
	 */
	public class AuthorControllerInitiator
	{
		public function AuthorControllerInitiator(){}
		
		public function setLearningLibrary(learningLibraryIn:ArrayCollection):void {
			AuthorController.instance.setLearningLibrary(learningLibraryIn);
		}
		
		public function setDictionaryFallback(xml:XML):void {
			AuthorController.instance.setDictionaryFallback(xml);
		}
		
		public function setDictionary(xml:XML):void {
			AuthorController.instance.setDictionary(xml);
		}
	}
}