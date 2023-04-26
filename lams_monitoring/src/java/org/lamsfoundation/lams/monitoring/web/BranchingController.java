package org.lamsfoundation.lams.monitoring.web;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchActivityEntry;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Controller
public class BranchingController {
    @Autowired
    private IMonitoringFullService monitoringService;

    @RequestMapping(path = "/groupedBranching", method = RequestMethod.GET)
    public String viewGroupeBranchingMappings(@RequestParam(name = AttributeNames.PARAM_ACTIVITY_ID) long activityId,
	    Model model) {

	Activity activity = monitoringService.getActivityById(activityId);
	if (!activity.isGroupBranchingActivity()) {
	    throw new IllegalArgumentException("Activity with ID " + activityId + " is not grouped branching activity");
	}
	BranchingActivity branchingActivity = (BranchingActivity) activity;
	Set<BranchActivityEntry> branchActiivtyEntries = new TreeSet<>(
		Comparator.comparing(e -> e.getGroup().getGroupName()));
	branchActiivtyEntries.addAll(branchingActivity.getBranchActivityEntries());
	model.addAttribute("branchActivityEntries", branchActiivtyEntries);

	return "branching/viewGroupedBranching";
    }
}