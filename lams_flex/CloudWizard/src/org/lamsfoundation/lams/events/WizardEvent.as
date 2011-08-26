package org.lamsfoundation.lams.events
{
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	
	import org.lamsfoundation.lams.vos.Lesson;
	import org.lamsfoundation.lams.vos.WorkspaceItem;

	public class WizardEvent extends Event
	{
		/*-.........................................Constants..........................................*/
		
		public static const INIT_WORKSPACE:String 				= "initWorkspaceEvent";
		
		public static const GET_FOLDER_CONTENTS:String 			= "getFolderContentsEvent";
		public static const OPEN_FOLDER:String 					= "openFolderContentsEvent";
		
		public static const LOAD_ORGANISATION_USERS:String 		= "loadOrganisationUsers";
		public static const LOAD_ORGANISATION_LEARNERS:String 	= "loadOrganisationStaff";
		public static const LOAD_ORGANISATION_STAFF:String		= "loadOrganisationLearners";
		
		public static const ADD_LESSON:String					= "addLessonEvent";
		public static const SCHEDULE_LESSON:String				= "scheduleLessonEvent";
		public static const SCHEDULE_LESSON_FINISH:String		= "scheduleLessonFinishEvent";
		public static const CHECK_SCHEDULE_LESSON_FINISH:String	= "checkScheduleLessonFinishEvent";
		public static const CREATE_LESSON_CLASS:String 			= "createLessonClassEvent";
		public static const SPLIT_LESSON_CLASS:String			= "splitLessonClassEvent";
		public static const START:String						= "startEvent";
		public static const START_LESSON:String 				= "startLessonEvent";
		public static const CREATE_PRESENCE_ROOM:String			= "createPresenceRoomEvent";
		
		public static const CLOSE_NOSTART:String				= "closeNoStartEvent";
		public static const CHANGE_START_BUTTON_LABEL:String 	= "changeStartButtonLabelEvent";
		
		/*-.........................................Properties..........................................*/

		public var mode:uint = 2;
		
		public var folderID:int = -1;
		public var folder:WorkspaceItem;
		public var folders:Array;
		
		public var organisationID:uint = Application.application.param("organisationID");
		
		public var contents:ArrayCollection;
		
		public var lesson:Lesson;
		
		public var startEvent_type:String;
		public var schedule:String;
		public var timeZone:uint;
		
		public var scheduledNumberDaysToLessonFinish:int;
		
		public var last:Boolean = true;
		
		public var label:String; 				// changing start button label
		
		/*-.........................................Constructor..........................................*/
		
		public function WizardEvent(type:String, bubbles:Boolean=true, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			
			startEvent_type = (schedule != null) ? START_LESSON : SCHEDULE_LESSON;
		}
		
	}
}