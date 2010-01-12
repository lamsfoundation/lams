package org.lamsfoundation.lams.itemRenderers
{
	import mx.controls.treeClasses.TreeItemRenderer;
	import mx.core.Container;
	import mx.core.IFactory;
	import mx.events.FlexEvent;
	
	import org.lamsfoundation.lams.business.LearnerConstants;

	public class ComplexActivityTreeItemRenderer extends TreeItemRenderer implements IFactory
	{
		private var activityIconCanvas:ActivityIconCanvas;
		
		public function ComplexActivityTreeItemRenderer()
		{
			super();
			addListeners();
		    activityIconCanvas = new ActivityIconCanvas();
		    addChild(activityIconCanvas);
		}
        
        public function newInstance():* {
            return new ComplexActivityTreeItemRenderer();
        }

    	private function addListeners():void{
			addEventListener(FlexEvent.DATA_CHANGE, onDataChange);	
		}
			
        private function onDataChange(event:FlexEvent):void{
				setProgressImage();	
		}	
			
		private function setProgressImage(state:Container = null):void{
			if(activityIconCanvas.progressImage){
				if(state){
					activityIconCanvas.progressImage.selectedChild = state;
				}
				else{
					if(data){
						if(data.@activityProgress == LearnerConstants.UNATTEMPTED_PROGRESS){
							activityIconCanvas.progressImage.selectedChild = activityIconCanvas.unattemptedState;
						}else if(data.@activityProgress == LearnerConstants.ATTEMPTING_PROGRESS){
							activityIconCanvas.progressImage.selectedChild = activityIconCanvas.attemptedState;
						}else if(data.@activityProgress == LearnerConstants.ATTEMPTED_PROGRESS){
							activityIconCanvas.progressImage.selectedChild = activityIconCanvas.attemptedState;
						}else if(data.@activityProgress == LearnerConstants.COMPLETED_PROGRESS){
							activityIconCanvas.progressImage.selectedChild = activityIconCanvas.completedState;
						}
					}

				}
			}
		}
        
        override protected function updateDisplayList(w:Number, h:Number):void {
            super.updateDisplayList(w, h);
            
            // position the icon
            if (activityIconCanvas.parent == this) {
                if (icon) {
                    activityIconCanvas.x = icon.x - 1;
                } else if (label) {
                    activityIconCanvas.x = label.x - activityIconCanvas.width - 1;
                }
            }
        }

	}
}