package org.lamsfoundation.lams.validators
{
	import com.visualempathy.display.controls.datetime.DateTimePicker;
	
	import mx.collections.ArrayCollection;
	import mx.containers.FormItem;
	import mx.controls.ComboBox;
	import mx.core.Application;
	import mx.validators.ValidationResult;
	import mx.validators.Validator;
	
	import org.lamsfoundation.lams.views.Advanced;
	import org.lamsfoundation.lams.vos.UserCollection;

	public class LessonValidator extends Validator
	{
		public var errorMessage:String;
		private var results:Array;
		
		public function LessonValidator()
		{
			super();
		}
		
		override protected function doValidation(value:Object):Array {
        
            // Clear results Array.
            results = [];
            
            if(value is String) {
            	validateLessonName(value as String);
            } else if(value is UserCollection) {
            	validateUserCollection(value as UserCollection);
            } else if(value is FormItem) {
            	var children:Array = (value as FormItem).getChildren();
            	if(children.length > 0) {
            		validateScheduleDate(children[0] as DateTimePicker, children[1] as ComboBox);
            	}
            } 
            
            // Return if there are errors.
            if(results.length > 0)
            	return results;
            	

            // Call base class doValidation().
            results = super.doValidation(value);     
            
            return results;
        }
        
        private function validateLessonName(lessonName:String):void {
        	 // Check lesson name field. 
            if (lessonName == "" || lessonName == null) {
                results.push(new ValidationResult(true, 
                    "", "noLessonName", this.errorMessage));
            }
        }
        
        private function validateUserCollection(collection:UserCollection):void {
        	if(collection.users.length <= 0)
        		results.push(new ValidationResult(true, "", "noLearners", this.errorMessage));
        }
        
        private function validateScheduleDate(timePicker:DateTimePicker, timeZonePicker:ComboBox):void {
        	var now:Date = new Date();
        	var tzList:ArrayCollection = (timeZonePicker.dataProvider != null) ? timeZonePicker.dataProvider as ArrayCollection : new ArrayCollection();
        	var usersTzIdx:uint = Advanced.getUserTimeZone(Application.application.param("tz") as String, tzList.toArray());
        	var usersTzOffset:Number = timeZonePicker.dataProvider[usersTzIdx].data;
 			
 			var selectedTzOffset:Number = timeZonePicker.selectedItem.data;
        	var rawOffset:Number = (usersTzOffset - selectedTzOffset);
        	
        	var valTime:Number = now.setTime(now.getTime() - rawOffset);
        	
        	if(timePicker.enabled) {
	        	if(timePicker.selectedDate.date > now.date)
	        		return;	
	        	else if(timePicker.selectedDate.date == now.date)
	        		if(timePicker.selectedDate.getHours() > now.getHours())
	        			return;
	        		else if(timePicker.selectedDate.getHours() == now.getHours())
	        			if(timePicker.selectedDate.getMinutes() >= now.getMinutes())
	        				return;
	        				
	        	results.push(new ValidationResult(true, "", "incorrectScheduleDate", this.errorMessage));
        	}
        }

	}
}