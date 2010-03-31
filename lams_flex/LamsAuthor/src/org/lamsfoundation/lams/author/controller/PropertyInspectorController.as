package org.lamsfoundation.lams.author.controller
{
	import flash.events.Event;
	
	import mx.core.Application;
	
	import org.lamsfoundation.lams.author.components.activity.ToolActivityComponent;
	import org.lamsfoundation.lams.author.components.activity.group.GroupActivityComponent;
	import org.lamsfoundation.lams.author.util.Constants;
	
	public class PropertyInspectorController
	{
		private static var _instance:PropertyInspectorController;
		
		public function PropertyInspectorController() {
			if (_instance != null) {
				throw new Error("This is a singleton, do not instanciate, instead use TransitionController.instance");
			}
		}
		
		public static function get instance():PropertyInspectorController {
			if (_instance == null) {
				_instance = new PropertyInspectorController();
			}
			return _instance;
		}
		
		public function getGroupTypeName(typeID:Object):String {
			switch (typeID as int) {
				case Constants.GROUPING_TYPE_RANDOM:
					return Application.application.dictionary.getLabel("random_grp_lbl");
					break;
				case Constants.GROUPING_TYPE_TEACHER_CHOSEN:
					return Application.application.dictionary.getLabel("chosen_grp_lbl");
					break;
				case Constants.GROUPING_TYPE_LEARNER_CHOICE:
					return Application.application.dictionary.getLabel("learner_choice_grp_lbl");
					break;
				default:
					return "";
					break;
			}
		}
		
		/**
		* FUNCTIONS FOR TOOL ACTIVITIES
		*/ 
		
		
		public function setDefineLater(event:Event):void {
			if (AuthorController.instance.selectedActivityComponent is ToolActivityComponent) {
				(AuthorController.instance.selectedActivityComponent as ToolActivityComponent).activity.defineLater = event.currentTarget.selected;
			}
		}
		
		public function setRunOffline(event:Event):void {
			if (AuthorController.instance.selectedActivityComponent is ToolActivityComponent) {
				(AuthorController.instance.selectedActivityComponent as ToolActivityComponent).activity.runOffline = event.currentTarget.selected;
			}
		}
		
		
		/**
		* FUNCTIONS FOR GROUP ACTIVITIES
		*/ 
		
		public function setGroupType(event:Event):void {
			var groupTypeID:int = event.currentTarget.selectedItem as int;
			if (AuthorController.instance.selectedActivityComponent is GroupActivityComponent) {
				var groupActCmp:GroupActivityComponent = AuthorController.instance.selectedActivityComponent as GroupActivityComponent;
				groupActCmp.activity.groupingTypeID = groupTypeID;
			}
		}
		public function setGroupUseGroupNumber(event:Event):void {
			if (AuthorController.instance.selectedActivityComponent is GroupActivityComponent) {
				GroupActivityComponent(AuthorController.instance.selectedActivityComponent).activity.useGroupNumber = event.currentTarget.selected;
			}
		}
		public function setGroupNumber(event:Event):void {
			if (AuthorController.instance.selectedActivityComponent is GroupActivityComponent) {
				GroupActivityComponent(AuthorController.instance.selectedActivityComponent).activity.setGroupNum(event.currentTarget.value);
			}
		}
		public function setLearnerNumber(event:Event):void {
			if (AuthorController.instance.selectedActivityComponent is GroupActivityComponent) {
				GroupActivityComponent(AuthorController.instance.selectedActivityComponent).activity.learnersPerGroups = event.currentTarget.value;
			}
		}
		public function setEqualGroupSizes(event:Event):void {
			if (AuthorController.instance.selectedActivityComponent is GroupActivityComponent) {
				GroupActivityComponent(AuthorController.instance.selectedActivityComponent).activity.equalGroupSizes = event.currentTarget.selected;
			}
		}
		public function setViewStudentsBeforeSelection(event:Event):void {
			if (AuthorController.instance.selectedActivityComponent is GroupActivityComponent) {
				GroupActivityComponent(AuthorController.instance.selectedActivityComponent).activity.viewStudentsBeforeSelection = event.currentTarget.selected;
			}
		}
		
		
		
		

	}
}