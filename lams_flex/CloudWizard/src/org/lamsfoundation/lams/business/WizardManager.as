package org.lamsfoundation.lams.business
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.external.*;
	import flash.utils.clearInterval;
	import flash.utils.setInterval;
	
	import mx.collections.ArrayCollection;
	
	import org.lamsfoundation.lams.common.dictionary.XMLDictionaryRegistry;
	import org.lamsfoundation.lams.vos.Lesson;
	import org.lamsfoundation.lams.vos.WorkspaceItem;
	
	public class WizardManager extends EventDispatcher
	{
		public var READ_ACCESS:Number = 1;
		public var MEMBERSHIP_ACCESS:Number = 2;
		public var OWNER_ACCESS:Number = 3;
		public var NO_ACCESS:Number = 4;
		
		/*-.........................................Properties..........................................*/
		private var _dictionaryRegistry:XMLDictionaryRegistry;
		
		// lesson tab
		private var _selectedResource:WorkspaceItem;
		private var _workspaceRoot:WorkspaceItem;
		
		// learners tab
		private var _learners:ArrayCollection;
		private var _staff:ArrayCollection;
		private var _usersLoaded:Boolean;
		
		private var _lessonID:uint;
		private var _initializedLesson:Lesson;
		
		private var _lessonInstances:Array;
		private var _xmppRoomCount:uint = 0;
		
		private var closeInterval:uint = 0;
		
		/*-.........................................Constructor..........................................*/
		 public function WizardManager()
        {
        	_dictionaryRegistry = new XMLDictionaryRegistry(new XML());
        	
        	setLearners(new ArrayCollection());
        	setStaff(new ArrayCollection(), 0);
        	
        	_lessonInstances = new Array();
        	
        	_usersLoaded = false;
        }
        
		/*-.........................................Setters and Getters..........................................*/
		
		[Bindable (event="workspaceRootChanged")]
		public function get workspaceRoot():WorkspaceItem
		{
			return _workspaceRoot;
		}
		
		[Bindable (event="learnersChanged")]
		public function get learners():ArrayCollection
		{
			return _learners;
		}
		
		[Bindable (event="staffChanged")]
		public function get staff():ArrayCollection
		{
			return _staff;
		}
		
		[Bindable (event="usersLoadedChanged")]
		public function get usersLoaded():Boolean
		{
			return _usersLoaded;
		}
		
		[Bindable (event="lessonIDChanged")]
		public function get lessonID():uint
		{
			return _lessonID;
		}
		
		[Bindable (event="lessonInstancesChanged")]
		public function get lessonInstances():Array
		{
			return _lessonInstances;
		}
		
		[Bindable (event="xxmpRoomCreated")]
		public function get xxmpRoomCount():uint
		{
			return _xmppRoomCount;
		}
		
		[Bindable (event="dictionaryUpdated")]
		public function get dictionaryRegistry():XMLDictionaryRegistry
		{
			return _dictionaryRegistry;
		}
		
		/*-.........................................Methods..........................................*/
		
		public function initWorkspace():WorkspaceItem {
			var item:WorkspaceItem = new WorkspaceItem();
			item.populate({name: "root", description: "root node", resourceID: -1, resourceType:WorkspaceItem.RT_FOLDER});
			
			_workspaceRoot = item;
			
			dispatchEvent(new Event("workspaceRootChanged"));
			
			return workspaceRoot;
		}
		
		public function setFolderContents(contents:Object, folder:WorkspaceItem):Array {
			var foldersToOpen:Array = new Array();
			
			for each(var content:Object in contents.contents as ArrayCollection) {
   				var newItem:WorkspaceItem = new WorkspaceItem();
   				newItem.populate(content);	
   				
   				newItem.parentWorkspaceFolderID = folder.workspaceFolderID;
  				
  				folder.children.addItem(newItem);
   			}
   			
   			if(folder.resourceID == -1)
  				foldersToOpen.push(folder.children.getItemAt(0));
   			
   			return foldersToOpen;
		}
		
		public function setUsersLoaded(value:Boolean):void {
			_usersLoaded = value;
			
			dispatchEvent(new Event("usersLoadedChanged"));
		}
		
		public function setLesson(newlessonID:uint, newLesson:Lesson):void {
			_lessonID = newlessonID;
			_initializedLesson = newLesson;
			
			if(_lessonInstances.length <= 0)
				setLessonInstances(new ArrayCollection([newlessonID]), newLesson);
			
			dispatchEvent(new Event("lessonIDChanged"));
		}
		
		public function setLessonInstances(instances:ArrayCollection, newLesson:Lesson):void {
			_lessonInstances = instances.toArray();
			
			if(_lessonInstances.length == 1)
				setLesson(_lessonInstances[0], newLesson);
			
			dispatchEvent(new Event("lessonInstancesChanged"));
		}
		
		public function setLearners(newLearners:ArrayCollection):void {
			_learners = newLearners;
			
			dispatchEvent(new Event("learnersChanged"));
		}
		
		public function setStaff(newStaff:ArrayCollection, userId:uint):void {
			_staff = newStaff;
			
			for each(var staff:Object in _staff) {
				if (staff.userID == userId) {
					staff.isCurrentUser = true;
				}
			}
			
			dispatchEvent(new Event("staffChanged"));
		}
		
		public function setDictionary(xml:XML):void {
			_dictionaryRegistry.xml = xml;
			
			dispatchEvent(new Event("dictionaryUpdated"));
		}
		
		public function closeOnSuccess(success:Boolean, last:Boolean):void {
			//trace('success close: ' + success);
			//trace('success last: ' + last);
			//trace('success room count: ' + xxmpRoomCount);
			//trace('success li length: ' + lessonInstances.length);
			
			if(success && last && xxmpRoomCount >= lessonInstances.length) {
				clearInterval(closeInterval);
				ExternalInterface.call("close");
			} else if(closeInterval == 0 && last)
				closeInterval = setInterval(closeOnSuccess, 500, success, last);
		}
		
		public function closeNoStart():void {
			ExternalInterface.call("close");
		}
		
		public function updateXmppRoomCount():void {
			_xmppRoomCount++;
			
			dispatchEvent(new Event("xxmpRoomCreated"));
		}

		// -----------------------------------------------------------

	}
}