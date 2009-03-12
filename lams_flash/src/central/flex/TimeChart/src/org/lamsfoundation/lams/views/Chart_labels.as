/** include file for Chart labels **/
import flash.utils.Dictionary;

import org.lamsfoundation.lams.common.dictionary.XMLDictionary;

	[Bindable]
	private var chartTitle:String = "";
	
	[Bindable]
	private var activity_split_lbl:String = "";
	
	[Bindable]
	private var completion_rate_lbl:String = "";
	
	[Bindable]
	private var learner_linear_axis_lbl:String = "";
	
	[Bindable]
	private var learner_category_axis_lbl:String = "";
	
	[Bindable]
	private var completed_lbl:String = "";
	
	[Bindable]
	private var learner_datatip_average:String = "";
	
	[Bindable]
	private var learner_lbl:String = "";
	
	[Bindable]
	private var chart_time_hrs_lbl:String = "";
	
	[Bindable]
	private var chart_time_mins_lbl:String = "";
	
	[Bindable]
	private var chart_time_secs_lbl:String = "";
	
	[Bindable]
	private var chart_completed_time_lbl:String = "";
	
	[Bindable]
	private var chart_average_time_lbl:String = "";
	
	[Bindable]
	private var chart_legend_average_lbl:String = "";
	
	[Bindable]
	private var chart_duration_lbl:String = "";
	
	[Bindable]
	private var chart_average_cb_lbl:String = "";
	
	[Bindable]
	private var search_learner_txt_lbl:String = "";
	
	private function getLabel(key:String):String {
		var event:DictionaryEvent = new DictionaryEvent(DictionaryEvent.GET);
		event.labelKey = key;
		this.dispatchEvent(event);
		
		return event.result;
	}
	
	private function updateLabels():void {
			
		chartTitle = getLabel("time.chart.panel.title");
		activity_split_lbl = getLabel("chart.btn.activity.split");
		completion_rate_lbl = getLabel("chart.btn_completion.rate");
		learner_linear_axis_lbl = getLabel('chart.learner.linear.axis.title');
		learner_category_axis_lbl = getLabel('chart.learner.category.axis.title');
		
		completed_lbl = getLabel("label.completed");
		learner_lbl = getLabel("label.learner");
		learner_datatip_average = getLabel("chart.learner.datatip.average");
		chart_time_hrs_lbl = getLabel('chart.time.format.hours');
		chart_time_mins_lbl = getLabel('chart.time.format.minutes');
		chart_time_secs_lbl = getLabel('chart.time.format.secs');
		chart_completed_time_lbl = getLabel('chart.series.completed.time');
		chart_average_time_lbl = getLabel('chart.series.average.time');
		chart_legend_average_lbl = getLabel('chart.legend.average');
		chart_duration_lbl = getLabel('chart.series.duration');
		chart_average_cb_lbl = getLabel('show.average.checkbox');
		search_learner_txt_lbl = getLabel('search.learner.textbox');
		
		Button(chart_button_bar.getChildAt(ACTIVITY_SPLIT__BUTTON)).label = activity_split_lbl;
		Button(chart_button_bar.getChildAt(COMPLETION__BUTTON)).label = completion_rate_lbl;
	}