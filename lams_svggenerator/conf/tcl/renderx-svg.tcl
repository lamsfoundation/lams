# 

ad_page_contract {
    
    Generates SVG based on sequence layout
    
    @author Ernie Ghiglione (Ernieg@mm.st)
    @creation-date 2010-07-08
    @cvs-id $Id$
} {
    seq_id
    scale:optional
    width:optional
    height:optional
} -properties {
} -validate {
} -errors {
}

set file "/tmp/tt/${seq_id}.xml"


if {![info exists scale]} {
    set scale 1
}
if {![info exists width]} {
    set width "100%"
}
if {![info exists height]} {
    set height "100%"
}

set doc [dom parse [read [open $file]]]
#set doc [dom parse [read [open /tmp/learning_design.xml]]]
array set learningDesignInfo [lamscentral::xmlutil::GetLDData -node $doc]

# Get all activities data
set activities  [lamscentral::xmlutil::GetAllActivities -node $doc]

#set doco [dom setResultEncoding utf-8]
set act_data [dom createDocument activities]
set activities_xml [$act_data documentElement]
set acts [dom createDocument activity]

# Load all activities into an XML first
foreach activity $activities {
    set acts [dom createDocument activity]
    set act_x [$acts documentElement]

    set activityid [lindex $activity 0]
    set activity_title [lindex $activity 1]
    set xcoord [lindex $activity 3]
    set ycoord [lindex $activity 4]
    set category [lindex $activity 11]
    set type [lindex $activity 5]
    set grouping [lindex $activity 9]
    set parentActivityID [lindex $activity 12]

    $act_x setAttribute id $activityid \
	title $activity_title \
	xcoord $xcoord \
	ycoord $ycoord \
	category $category \
	type $type \
	grouping $grouping \
	parentActivityID $parentActivityID

	$activities_xml appendChild $act_x


}

# Now that they are on the XML, sort them out
foreach one [$activities_xml childNodes] {
    
    set parentID [$one getAttribute parentActivityID] 
    if {![string eq $parentID 0]} {

	set parentNode [$activities_xml selectNodes //*\[@id='${parentID}'\]]
	
	$activities_xml removeChild $one
	$parentNode appendChild $one

    }

}

#ns_write [$activities_xml asXML]
#ad_script_abort

set transitions [lamscentral::xmlutil::GetAllTransitions -node $doc]

# Create dom for svg

set doco [dom setResultEncoding utf-8]
set doco [dom createDocument svg]
set svg_xml [$doco documentElement]


# create svg node
$svg_xml setAttribute width $width height $height version 1.1 xmlns "http://www.w3.org/2000/svg"  xmlns:xlink "http://www.w3.org/1999/xlink"

# g element for the whole lot
set gdoco [dom createDocument g]
set xml [$gdoco documentElement]

$xml setAttribute transform "scale($scale,$scale)"

# Draw transitions
foreach transition $transitions {
    set act1 [lindex $transition 1]
    set act1_num [lsearch -index 0 $activities $act1]

    set act1_details [lindex $activities $act1_num]
    set act1_xcoord [lindex $act1_details 3]
    set act1_ycoord [lindex $act1_details 4]

    # according to the type of activity, we need to set up difference
    # width and height for the To and From activity to draw the
    # transition accordingly. 
    if {[string eq [lindex $act1_details 1] "Gate"] && \
	    [string eq [lindex $act1_details 11] 1] } {
	set act1_type "Gate"
	set act1_width 38
	set act1_height 38
	ns_log Notice "Gate: act1_xcoord $act1_xcoord act1_ycoord $act1_ycoord"
    } else {
	set act1_type "ToolActivity"
	set act1_width 125
	set act1_height 50
    }

    set act2 [lindex $transition 2]
    set act2_num [lsearch -index 0 $activities $act2]
    set act2_details [lindex $activities $act2_num]
    set act2_xcoord [lindex $act2_details 3]
    set act2_ycoord [lindex $act2_details 4]

    if {[string eq [lindex $act2_details 1] "Gate"] && \
	    [string eq [lindex $act2_details 11] 1] } {
	set act2_type "Gate"
	set act2_width 38
	set act2_height 38
    } else {
	set act2_type "ToolActivity"
	set act2_width 125
	set act2_height 50
    }
    
    set transition1 [lamscentral::svg::create_transition \
			 -id ${act1}_to_${act2} \
			 -start_x [expr $act1_xcoord + ($act1_width / 2)] \
			 -start_y [expr $act1_ycoord + ($act1_height / 2)] \
			 -end_x [expr $act2_xcoord + ($act2_width / 2)] \
			 -end_y [expr $act2_ycoord + ($act2_height / 2)] \
			 -draw_arrow 1 \
			 -type {$act1_type $act2_type} ]

    $xml appendChild $transition1
}

# Draw activities from activities xml

#foreach activity [$activities_xml childNodes] {
#    ns_write "$activity \n"
    
#}

#ns_return 200 image/svg+xml [$xml asXML]
#ad_script_abort

# Draw activities
foreach activity $activities {

    set activityid [lindex $activity 0]
    set activity_title [lindex $activity 1]
    set xcoord [lindex $activity 3]
    set ycoord [lindex $activity 4]
    set category [lindex $activity 11]
    set type [lindex $activity 5]
    set grouping [lindex $activity 9]
    set image [lamscentral::svg::getImage -image [lindex $activity 10]]
    set parentActivityID [lindex $activity 12]

    # we check if the activity belongs to a container activity
    # (ie: parallels, optionals, branching, etc)
    if {[string eq $parentActivityID  0]} {

 	$xml appendChild [lamscentral::svg::create_activity \
			      -id $activityid \
			      -x $xcoord \
			      -y $ycoord \
			      -text $activity_title \
			      -category $category \
			      -type $type \
			      -image $image \
			      -grouping $grouping \
			     -activities_xml $activities_xml]
    } else {
	# activities with parents (paralles, optionals, etc)


	set parentNode [$activities_xml selectNodes {//*[@id=$parentActivityID]}]
	set x [expr [$parentNode getAttribute xcoord] - 2]
	set y [expr [$parentNode getAttribute ycoord] - 20]

	$xml appendChild [lamscentral::svg::create_activity \
			      -id $activityid \
			      -x [expr $xcoord + $x] \
			      -y [expr $ycoord + $y] \
			      -text $activity_title \
			      -category $category \
			      -type $type \
			      -image $image \
			      -grouping $grouping \
			     -activities_xml $activities_xml]
	
    }

}

foreach one [$xml childNodes] {
    
    set parentID [$one getAttribute parentID] 
    if {![string eq $parentID 0]} {

	ns_log Notice "ONE: [$one asXML]"
	set parentNode [$xml selectNodes //*\[@id='${parentID}'\]]
	ns_log Notice "parentNode: $parentNode"
	$xml removeChild $one
	$parentNode appendChild $one

    }

}

$svg_xml appendChild $xml
ns_return 200 image/svg+xml  [$svg_xml asXML]    

