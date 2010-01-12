package org.lamsfoundation.lams.vos
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	public class Search
	{
		
		static public var QUERY_DATE_ACTIVITIES:uint = 0;
		static public var QUERY_DATE_LEARNERS:uint = 1;
		static public var QUERY_PERIOD_ACTIVITIES:uint = 2;
		static public var QUERY_PERIOD_LEARNERS:uint = 3;
		
		public var query_type:uint;
		public var start_date:Date;
		public var end_date:Date;
		public var duration:Number;
		
		private var learners:ArrayCollection;
		private var activities:ArrayCollection;
		
		// constructor
		public function Search(start_date:Date, end_date:Date, query_type:uint)
		{
			this.start_date = start_date;
			this.end_date = end_date;
			this.query_type = query_type;
		}

	}
}