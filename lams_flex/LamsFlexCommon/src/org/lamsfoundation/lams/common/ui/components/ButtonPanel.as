package org.lamsfoundation.lams.common.ui.components
{
    import flash.events.MouseEvent;
    
    import mx.binding.utils.BindingUtils;
    import mx.containers.Panel;
    import mx.controls.Button;

    [Event(name="buttonClick", type="flash.events.Event")]

    /**
     * This is a panel with a button inserted into the header 
     * @author lfoxton
     * 
     */
    public class ButtonPanel extends Panel
    {
        [Bindable] public var buttonLabel:String = " ";
        [Bindable] public var buttonPadding:Number = 10;
        [Bindable] public var buttonWidth:Number = 20;
        [Bindable] public var buttonHeight:Number = 20;
        [Bindable] public var buttonIcon:Class;

        public var mybtn:Button;

        public function ButtonPanel()
        {
            super();
        }
                
        protected override function createChildren():void
        {
            super.createChildren();
            
            if( ! buttonLabel ) return;
            
            mybtn = new Button();
            mybtn.label = buttonLabel;
            mybtn.width = buttonWidth;
            mybtn.height = buttonHeight;
            mybtn.visible = true;
            mybtn.includeInLayout = true;
            mybtn.setStyle("icon", buttonIcon);
            mybtn.addEventListener( MouseEvent.CLICK, buttonClickHandler );
            rawChildren.addChild( mybtn );
            
        }
        
        protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
        {
            super.updateDisplayList(unscaledWidth,unscaledHeight);
            var x:int = width - ( mybtn.width + buttonPadding );
            //mybtn.width = mybtn.measuredWidth;
            //mybtn.height = mybtn.measuredHeight;
            var headerHeight:int = this.getStyle("headerHeight");
            mybtn.width = headerHeight - 2;
            mybtn.height = headerHeight - 2;          
            mybtn.move( x, 1 );
        }
        
        private function buttonClickHandler(event:MouseEvent):void
        {
            this.dispatchEvent( new Event( 'buttonClick', false ) );
        }
        
    }
}