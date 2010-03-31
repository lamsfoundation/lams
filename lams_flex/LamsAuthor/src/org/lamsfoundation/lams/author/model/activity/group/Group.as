package org.lamsfoundation.lams.author.model.activity.group
{
	import mx.core.Application;
	
	public class Group
	{
		public var groupUIID:int;
		public var groupActivityUIID:int;
		public var groupName:String;
		public var orderID:int;
		
		public function Group(groupUIID:int, groupActivityUIID:int, groupName:String, orderID:int){
			this.groupUIID = groupUIID;
			this.groupActivityUIID = groupActivityUIID;
			this.groupName = groupName;
			this.orderID = orderID;
		}
	}
}