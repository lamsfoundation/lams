package org.lamsfoundation.lams.author.util
{
	public class Constants
	{
		
		public static const TOOL_CATEGORY_SYSTEM:int = 1;
	    public static const TOOL_CATEGORY_COLLABORATIVE:int = 2;
	    public static const TOOL_CATEGORY_ASSESSMENT:int = 3;
	    public static const TOOL_CATEGORY_INFORMATIVE:int = 4;
	    public static const TOOL_CATEGORY_SPLIT:int = 5;
	    public static const TOOL_CATEGORY_REFLECTIVE:int = 6;
	    
	    public static const CURSOR_STATE_NORMAL:int = 1;
	    public static const CURSOR_STATE_TRANSITION:int = 2;
	    public static const CURSOR_STATE_DATAFLOW:int = 3;
	    public static const CURSOR_STATE_HELP:int = 4;
	    
	    public static const UI_STATE_NORMAL:int = 1;
	    public static const UI_STATE_DRAWING:int = 2;
	    
	    public static const ACTIVITY_STATE_NORMAL:int = 1;
	    public static const ACTIVITY_STATE_DRAWING:int = 2;
	    public static const ACTIVITY_STATE_MOUSE_OVER:int = 3;
	    public static const ACTIVITY_STATE_SELECTED:int = 4;
	    
	    public static const TOOL_CATEGORY_COLOURS:Object =  {
	    	2:0xFFFDBE, 
	    	3:0xE9E2F5,
	    	6:0xDDFCB1,
	    	4:0xFFEEC8
	    };
	    
	    // Activity types
		public static const ACTIVITY_TYPE_TOOL:int = 1;
		public static const ACTIVITY_TYPE_GROUPING:int = 2;
		public static const ACTIVITY_TYPE_GATE_SYNCH:int = 3;
		public static const ACTIVITY_TYPE_GATE_SCHEDULE:int = 4;
		public static const ACTIVITY_TYPE_GATE_PERMISSION:int = 5;
		public static const ACTIVITY_TYPE_COMBINED:int = 6;
		public static const ACTIVITY_TYPE_OPTIONAL_ACTIVITY:int = 7;
		public static const ACTIVITY_TYPE_SEQUENCE:int = 8;
		public static const ACTIVITY_TYPE_GATE_SYSTEM:int = 9;
		public static const ACTIVITY_TYPE_BRANCHING_CHOSEN:int = 10;
		public static const ACTIVITY_TYPE_BRANCHING_GROUP:int = 11;
		public static const ACTIVITY_TYPE_BRANCHING_TOOL:int = 12;
		public static const ACTIVITY_TYPE_OPTIONAL_SEQUENCE:int = 13;
		public static const ACTIVITY_TYPE_GATE_CONDITION:int = 14;
		public static const ACTIVITY_TYPE_REFERENCE:int = 15;
		
		// Grouping type
		public static const GROUPING_TYPE_RANDOM:int = 1;
		public static const GROUPING_TYPE_TEACHER_CHOSEN:int = 2;
		public static const GROUPING_TYPE_LEARNER_CHOICE:int = 3;
		
		// Transition type
		public static const TRANSITION_TYPE_NORMAL:int = 1;
		public static const TRANSITION_TYPE_BRANCH:int = 2;
		public static const TRANSITION_TYPE_DATA_FLOW:int = 3;

	}
}