package org.lamsfoundation.lams.vos
{
	import mx.collections.ArrayCollection;
	
	public class UserCollection {
		
		public var _users:ArrayCollection;
		public var _groupName:String;
		
		public function UserCollection() {
			users = new ArrayCollection();
		}
		
		public function get toData():Object {
			
			return {users: getIdArray(), groupName: groupName};
		}
		
		private function getIdArray():Array {
			var idArray:Array = new Array();
			
			for each(var user:Object in users) {
				idArray.push(int(user.userID));
			}
			
			return idArray;
		}
		
		public function set groupName(value:String):void {
			if(value != null)
				_groupName = value;
			else
				_groupName = "";
		}
		
		public function get groupName():String {
			return _groupName;
		}
		
		public function set users(value:ArrayCollection):void {
			if(value != null)
				_users = value;
			else
				_users = new ArrayCollection();
		}
		
		public function get users():ArrayCollection {
			return _users;
		}
	}
}