package org.lamsfoundation.lams.author.model
{
	public class ToolOutputDefinition
	{
		public var description:String;
		public var isDefaultGradebookMark:Boolean;
		public var name:String;
		public var showConditionNameOnly:String;
		public var startValue:Object;
		public var type:String;
		
		public function ToolOutputDefinition() {}
		
		public function loadFromDTO(dto:Object):void {
			description = dto.description;
			isDefaultGradebookMark = dto.isDefaultGradebookMark;
			name = dto.name;
			showConditionNameOnly = dto.showConditionNameOnly;
			startValue = dto.startValue;
			type = dto.type;
		}
		
		public function isEqual(compare:ToolOutputDefinition):Boolean {
			if (compare==null || compare.name == null) {
				return false;
			} else {
				return this.name == compare.name;
			}
		}
	}
}