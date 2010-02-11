package org.lamsfoundation.lams.author.model.learninglibrary
{
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	
	
	/**
	 * This class contains the information for each tool so it doesnt need to be stored
	 * inside the ToolActivity class for each activity. 
	 * 
	 * This information will be held in one global hashmap with the tool id as a key, 
	 * and each tool activity will have a reference to it via the id.
	 * 
	 */ 
	public class Tool
	{
		public var toolID:int;
		public var defaultToolContentID:int;
		public var toolName:String;
		public var toolDisplayName:String;
		public var toolSignature:String;
		public var activityCategoryID:int;
		public var description:String;
		public var authoringURL:String;
		public var monitoringURL:String;
		public var contributeURL:String;
		public var helpURL:String;
		public var supportsContribute:Boolean;
		public var supportsDefineLater:Boolean;
		public var supportsModeration:Boolean;
		public var supportsRunOffline:Boolean;
		public var supportsOutputs:Boolean;
		public var valid:Boolean;
		public var groupingSupportType:int;
		public var libraryActivityUIImage:String;
		public var toolOutputDefinitions:ArrayCollection;
		
		// For tool adapter tools
		public var mappedServers:ArrayCollection;		// List of allowable servers to show this tool for
		public var extLmsId:String;					// External LMS id for tool adapter tools
		
		// Convenience property
		public var learningLibraryID:int;
		
		public function Tool(dto:Object, learningLibraryIDIn:int)
		{
			this.toolID = parseInt(dto.toolID);
			this.defaultToolContentID = parseInt(dto.toolContentID);
			this.toolName = dto.activityTitle;
			this.toolDisplayName = dto.toolDisplayName;
			this.toolSignature = dto.toolSignature;
			this.activityCategoryID = parseInt(dto.activityCategoryID);
			this.description = dto.description;
			this.authoringURL = dto.authoringURL;
			this.monitoringURL = dto.monitoringURL;
			this.contributeURL = dto.contributeURL;
			this.helpURL = dto.helpURL;
			this.supportsContribute = Boolean(dto.supporstBoolean);
			this.supportsDefineLater = Boolean(dto.supportsDefineLater);
			this.supportsModeration = Boolean(dto.supportsModeration);
			this.supportsRunOffline = Boolean(dto.supportsRunOffline);
			this.supportsOutputs = Boolean(dto.supportsOutputs);
			this.valid = Boolean(dto.valid);
			this.groupingSupportType = parseInt(dto.groupingSupportType);
			this.libraryActivityUIImage = dto.libraryActivityUIImage;
			
			toolOutputDefinitions = new ArrayCollection();
			
			mappedServers = dto.mappedServers;
			this.extLmsId = dto.extLmsId;
			
			this.learningLibraryID = learningLibraryIDIn;
			
			if (Application.application.TESTING && Application.application.TESTING_LOCAL) {
				libraryActivityUIImage = "assets/test/toolimages" + libraryActivityUIImage.substring(libraryActivityUIImage.lastIndexOf("/"));
			} else {
				libraryActivityUIImage = Application.application.lamsURL + libraryActivityUIImage;
			}
		}
	}
}