package org.lamsfoundation.lams.vos
{
	
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	
	import org.lamsfoundation.lams.common.util.WDDXUtil;
	
	public class WorkspaceItem extends EventDispatcher
	{
		public static var ROOT_VFOLDER:Number = -1;
		public static var ORG_VFOLDER:Number = -2;
		
		public static var RT_FOLDER:String = "Folder";
		public static var RT_LD:String = "LearningDesign";
		public static var RT_LESSON:String = "Lesson";
		public static var RT_FILE:String = "File";
		
		// properties
		private var _name:String;
		private var _description:String;
		
		private var _workspaceFolderID:Number;
		private var _parentWorkspaceFolderID:Number;
		
		private var _resourceID:Number;
		private var _resourceType:String;
		
		private var _children:ArrayCollection  = new ArrayCollection();
		private var _sort:Sort;
		
		//private var creationDateTime:Date;
		//private var lastModifiedDateTime:Date;
	
		public function WorkspaceItem(dispatcher:IEventDispatcher=null)
		{
			super(dispatcher);
			
			_sort = new Sort();
			_sort.fields = [new SortField("name")];
		}

		public function get name():String {
			return _name;
		}

		public function get description():String {
			return _description;
		}
		
		public function get resourceID():Number {
			return _resourceID;
		}
		
		public function get resourceType():String {
			return _resourceType;
		}
		
		public function set workspaceFolderID(value:Number):void {
			_workspaceFolderID = value;
		}
		
		public function get workspaceFolderID():Number {
			return _workspaceFolderID;
		}
		
		public function set parentWorkspaceFolderID(value:Number):void {
			_parentWorkspaceFolderID = value;
		}
		
		public function get parentWorkspaceFolderID():Number {
			return _parentWorkspaceFolderID;
		}
		
		public function populate(dto:Object):void {
			_name = WDDXUtil.cleanNull(dto.name) as String;
			_description = WDDXUtil.cleanNull(dto.description) as String;
			_resourceID = dto.resourceID;
			_resourceType = WDDXUtil.cleanNull(dto.resourceType) as String;
			
			if(_resourceType == RT_FOLDER)
				_workspaceFolderID = _resourceID;
				
			if(_resourceID != -1) _children.sort = _sort;
			_children.refresh();
			
		}
		
		public function set children(value:ArrayCollection):void {
			_children = value;
		}
		
		public function get children():ArrayCollection {
			_children.refresh();
			
			return _children;
		}
		
		public function isValidDesign():Boolean {
			return (_resourceType == RT_LD || _resourceType == RT_LESSON);
		}

	}
}