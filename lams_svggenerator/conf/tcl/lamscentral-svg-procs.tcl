# 

ad_library {
    
    Manipulation/Creation SVG
    
    @author Ernie Ghiglione (Ernieg@mm.st)
    @creation-date 2010-07-08
    @cvs-id $Id$
}

#
#  Copyright (C) 2010 LAMS Foundation
#
#  This package is free software; you can redistribute it and/or modify it under the
#  terms of the GNU General Public License as published by the Free Software
#  Foundation; either version 2 of the License, or (at your option) any later
#  version.
#
#  It is distributed in the hope that it will be useful, but WITHOUT ANY
#  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
#  FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
#  details.
#

namespace eval lamscentral::svg {}

ad_proc -public lamscentral::svg::layout_activities {

} {

    Parses the activities and adds them to the svg with names and types

    @author Ernie Ghiglione (ErnieG@mm.st)

} {

}

ad_proc -public lamscentral::svg::layout_transitions {

} {

    Parses the transitions to display

    @author Ernie Ghiglione (ErnieG@mm.st)

} {

}


ad_proc -public lamscentral::svg::process_node {
    -node:required
    -appendto:required

} {
    Process an activity XML node and returns an SVG xml node
    @author Ernie Ghiglione (ErnieG@mm.st)
} {

    # get all the details of the activity

    # send the activity to its type to process

    # check if it has children, if it does, do this recursively 


    # return xml chunk?

}

ad_proc -public lamscentral::svg::create_activity {
    -id:required
    -x:required
    -y:required
    -rx
    -ry
    -width
    -height
    -style
    -text
    -image
    -category:required
    -type:required
    -grouping:required
    -activities_xml:required
} {

    Returns an svg rect and text and image node

    @author Ernie Ghiglione (ErnieG@mm.st)

} {

    set doc [dom setResultEncoding utf-8]
    set doc [dom createDocument node]
    set node [$doc createElement g]

    set parentIDx [[$activities_xml selectNodes //*\[@id='${id}'\]] getAttribute parentActivityID]

    $node setAttribute id $id parentID $parentIDx

    # if this is a stop gate we need to draw a octogon instead
    if {[string equal $text "Gate"] && \
	    [string equal $type 5] } {
	set node [$doc createElement g]

#	$node setAttribute id "${id}" 

	set activity  [$doc createElement polygon]
	set textContent [$doc createTextNode "STOP"]
	set textNode [$doc createElement text]

	$textNode appendChild $textContent
	set point_x1 $x
	set point_y1 $y

	# These are the octogon proportions so accorting to one point
	# we calculate the new dimensions/proportions
	set proportions [list {-14.6 -35.4} {0 -35.4} {10.4 -25}  {10.4 -10.4} {0 0} {-14.6 0} {-25 -10.4} {-25 -25}]
	foreach proportion $proportions {
	    
	    append final_proportions "[expr $point_x1 - [lindex $proportion 0]],[expr $point_y1 - [lindex $proportion 1]] "

	}

	$activity setAttribute \
	    id $id \
	    style "fill:red;stroke:#000000;stroke-width:0.5px" \
	    points " $final_proportions"

	
	$node appendChild $activity

	# calculate midpoint for STOP text
	# ugly way to getting a point 
	set x1 [lindex [split [lindex $final_proportions 6] ,] 0]
	set x2 [lindex [split [lindex $final_proportions 2] ,] 0]

	set midpoint_x [expr ($x1 + $x2) /2]

	set y1 [lindex [split [lindex $final_proportions 6] ,] 1]
	set y2 [lindex [split [lindex $final_proportions 2] ,] 1]
	set midpoint_y [expr ($y1 + $y2) /2]

	$textNode setAttribute \
	    id "Gate_$id" \
	    x $midpoint_x \
	    y $midpoint_y \
	    dy 3 \
	    dx 0 \
	    text-anchor "middle" \
	    font-size "10" \
	    font-family "Verdana" \
	    style "stroke:#FFFFFF;stroke-width:1.3;"


	$node appendChild $textNode
	return $node

	
    } elseif {[string eq $type 6] } {
	# This is a parallel activity
	
	set node [create_parallel \
		      -id $id \
		      -x $x \
		      -y $y \
		      -text $text \
		      -grouping $grouping \
		      -activities_xml $activities_xml \
		     -parentID $parentIDx]

	    
	return $node

    } elseif {[string eq $type 13] } {
	# This is an optional sequence

	set optional_node [$activities_xml  selectNodes //*\[@id='${id}'\]]
	set optional_children [$optional_node childNodes]
	set optional_children_length [llength $optional_children]

	# now find out how many activities each of the subsequences
	# have so we can calculate the width

	set max_children 0
	foreach one $optional_children {
	    set get_children [llength [$one childNodes]]
	    if {$get_children > $max_children } {
		set max_children $get_children
	    }
	}

	$node setAttribute x $x y $y

        set rx 3
        set ry 3
        set width [expr (140/2 * $max_children)] 
        set height [expr (65 * $optional_children_length) + 20 ]
        set style "stroke:black;stroke-width:1;opacity:1;fill:#d0defd"

	set optionalContainer [$doc createElement rect]

	$optionalContainer setAttribute \
	    x $x \
	    y $y \
	    rx 3 \
	    ry 3 \
	    width $width \
	    height $height \
	    style $style

	$node appendChild $optionalContainer

	set optionalHeader  [$doc createElement rect]
	$optionalHeader setAttribute \
	    x [expr $x + 3] \
	    y [expr $y + 3] \
	    rx 0 \
	    ry 0 \
	    width [expr $width - 6] \
	    height 28 \
	    style "fill:#a2a5fd;stroke:#2b2a2d;stroke-width:.5;opacity:1" 
	
	$node appendChild $optionalHeader

	if {[info exists text]} {

	    set textNode [create_text \
			      -id "TextElement-$id" \
			      -x [expr $x + 8] \
			      -y [expr $y + 14]  \
			      -text_anchor "top" \
			      -font_size 10 \
			      -font_family "Verdana" \
			      -text "$text"]
	    
	    $node appendChild $textNode

	    set textNode [create_text \
			      -id "Children-$id" \
			      -x [expr $x + 8] \
			      -y [expr $y + (14 *2)]  \
			      -text_anchor "top" \
			      -font_size 8 \
			      -font_family "Verdana" \
			      -text "$optional_children_length Sequences"]

	    $node appendChild $textNode

	}

	return $node

    } elseif {[string eq $type 8] } {
	# This is a sequence within an optional

	set sequence_node [$activities_xml  selectNodes //*\[@id='${id}'\]]
	set sequence_children [$sequence_node childNodes]
	set sequence_children_length [llength $sequence_children]

	$node setAttribute x $x y $y

        set rx 3
        set ry 3
        set width [expr (140/2 * $sequence_children_length)]
        set height 55
        set style "stroke:black;stroke-width:1;opacity:1;fill:#d0defd"

        set sequenceContainer [$doc createElement rect]

	$sequenceContainer setAttribute \
	    x $x \
	    y $y \
	    rx 3 \
	    ry 3 \
	    width $width \
	    height $height \
	    style $style

	$node appendChild $sequenceContainer

	return $node


    } elseif {[string eq $type 7] } {
	# This is an optional activity

	# find how many children activities it has
	set optional_node [$activities_xml  selectNodes //*\[@id='${id}'\]]
	set optional_children [llength [$optional_node childNodes]]

	$node setAttribute x $x y $y parentID $parentIDx

	set rx 3
	set ry 3
	set width 140
	set height [expr (65 * $optional_children) + 20 ]
	set style "stroke:black;stroke-width:1;opacity:1;fill:#d0defd"

	set optionalContainer [$doc createElement rect]

	$optionalContainer setAttribute \
	    x $x \
	    y $y \
	    rx 3 \
	    ry 3 \
	    width $width \
	    height $height \
	    style $style

	$node appendChild $optionalContainer

	set optionalHeader  [$doc createElement rect]
	$optionalHeader setAttribute \
	    x [expr $x + 3] \
	    y [expr $y + 3] \
	    rx 0 \
	    ry 0 \
	    width [expr $width - 6] \
	    height 28 \
	    style "fill:#a2a5fd;stroke:#2b2a2d;stroke-width:.5;opacity:1" 
	
	$node appendChild $optionalHeader

	if {[info exists text]} {

	    set textNode [create_text \
			      -id "TextElement-$id" \
			      -x [expr $x + 8] \
			      -y [expr $y + 14]  \
			      -text_anchor "top" \
			      -font_size 10 \
			      -font_family "Verdana" \
			      -text "$text"]
	    
	    $node appendChild $textNode

	    set textNode [create_text \
			      -id "Children-$id" \
			      -x [expr $x + 8] \
			      -y [expr $y + (14 *2)]  \
			      -text_anchor "top" \
			      -font_size 8 \
			      -font_family "Verdana" \
			      -text "$optional_children Activities"]

	    $node appendChild $textNode

	}
	return $node

    } else {
	# This is a tool activity

	# Check first if this activity is a children of a sequence
	# activity, if it is, then we need to change its size

	set one_node [$activities_xml  selectNodes //*\[@id='${id}'\]]
	set parentId [$one_node getAttribute parentActivityID]
	if {![string eq $parentId 0]} {
	    set parent_node [$one_node parentNode]
	    if {[string eq [$parent_node getAttribute type] 8]} {
		set pp_node [$parent_node parentNode]
		set width 50
		set height 40
		set parent_x [$pp_node getAttribute xcoord]
		set parent_y [$pp_node getAttribute ycoord]
		set x [expr $parent_x + $x]
		set y [expr $parent_y + $y]
		unset text

	    }

	}

	if {![info exists rx]} {
	    set rx 3
	}
	if {![info exists ry]} {
	    set ry 3
	}
	if {![info exists width]} {
	    set width 125
	}
	if {![info exists height]} {
	    set height 50
	}
	if {![info exists style]} {
	    set style "stroke:black;stroke-width:1;opacity:1"
	}
	
	switch $category {
	    3 {
		append style ";fill:#ece9f7"
	    }
	    4 {
		append style ";fill:#fdf1d3"
	    }
	    2 {
		append style ";fill:#fffccb"
	    }
	    6 {
		append style ";fill:#e9f9c0"
	    }
	    1 {
		append style ";fill:#d0defd"
	    }
	    default {
		append style ";fill:#FFFFFF"
	    }
	}
    }

    # if activity uses a grouping we need to add a second rect layer
    # to show that it's grouped
    if {[string eq $grouping "true"]} {
	set act_grouping [create_grouping_effect \
			      -id "grouping-${id}" \
			      -x $x \
			      -y $y \
			      -width $width \
			      -height $height \
			      -style $style]
	$node appendChild $act_grouping
    }

    set activity  [$doc createElement rect]
    $activity setAttribute \
	id "act-${id}" \
	x $x \
	y $y \
	rx $rx \
	ry $ry \
	width $width \
	height $height \
	style $style


    $node appendChild $activity

    if {[info exists text]} {

	set textNode [create_text \
			  -id "TextElement-$id" \
			  -x [expr $x + ($width/2)] \
			  -y [expr $y + ($height/2) +18] \
			  -text_anchor "middle" \
			  -font_size 10 \
			  -font_family "Verdana" \
			  -text "$text"]

	$node appendChild $textNode
		      
    }

    set imageNode [create_image \
		       -id "image-${id}" \
		       -x [expr $x + ($width/2) -15] \
		       -y [expr $y + ($height/2) -22] \
		       -image $image ]

    $node appendChild $imageNode
    

    return $node

}


ad_proc -public lamscentral::svg::create_image {
    -id:required
    -x:required
    -y:required
    -image:required
} {

    Returns an svg text

    @author Ernie Ghiglione (ErnieG@mm.st)

} {

    set doc [dom setResultEncoding utf-8]
    set doc [dom createDocument node]
    set imageNode [$doc createElement image] 

    $imageNode setAttribute \
	id $id \
	x $x \
	y $y \
	height 30 \
	width 30 \
	xlink:href $image


    return $imageNode

}


ad_proc -public lamscentral::svg::create_text {
    -id:required
    -x:required
    -y:required
    -dy
    -dx
    -text_anchor
    -font_size
    -font_family
    -text:required

} {

    Returns an svg text

    @author Ernie Ghiglione (ErnieG@mm.st)

} {

    if {![info exists dy]} {
	set dy 0
    }
    if {![info exists dx]} {
	set dx 0
    }
    if {![info exists text_anchor]} {
	set text_anchor "top"
    }
    if {![info exists font_size]} {
	set font_size "10"
    }
    if {![info exists font_family]} {
	set font_family "Verdana"
    }
    if {![info exists style]} {
	set style "stroke: #000000"
    }

    set doc [dom setResultEncoding utf-8]
    set doc [dom createDocument node]
    set textContent [$doc createTextNode $text]
    set textNode [$doc createElement text] 

    $textNode appendChild $textContent
    $textNode setAttribute \
	id $id \
	x $x \
	y $y \
	dy $dy \
	text-anchor $text_anchor \
	font-size $font_size \
	font-family $font_family

    return $textNode

}

ad_proc -public lamscentral::svg::create_transition {
    -id:required
    -start_x:required
    -start_y:required
    -end_x:required
    -end_y:required
    -style
    -draw_arrow
    -type
} {

    Returns an svg line in the for of a transition

    @author Ernie Ghiglione (ErnieG@mm.st)

} {

    set doc [dom setResultEncoding utf-8]
    set doc [dom createDocument node]
    set line [$doc createElement line]

    $line setAttribute id $id x1 $start_x y1 $start_y x2 $end_x y2 $end_y style "stroke:rgb(99,99,99);stroke-width:2" parentID 0

    set arrow [$doc createElement arrow]

    return $line

}

ad_proc -public lamscentral::svg::create_parallel {
    -id:required
    -x:required
    -y:required
    -text
    -image
    -grouping:required
    -activities_xml:required
    -parentID:required
} {

    Returns an svg node with all the parallel details.

    @author Ernie Ghiglione (ErnieG@mm.st)

} {

    set doc [dom setResultEncoding utf-8]
    set doc [dom createDocument node]
    set node [$doc createElement g]

    $node setAttribute id "${id}" x $x y $y parentID $parentID

    # Given that for now all parallel activities are just two
    # activities, we can hard code the width and height
    set width 140
    set height 145
    set style "stroke:black;stroke-width:1;opacity:1;fill:#d0defd"

    # if the parallel is grouped, show it
    if {[string eq $grouping "true"]} {
	set act_grouping [create_grouping_effect \
			      -id "${id}" \
			      -x $x \
			      -y $y \
			      -width $width \
			      -height $height \
			      -style $style]
	$node appendChild $act_grouping
    }

    set parallelContainer [$doc createElement rect]
    
    $parallelContainer setAttribute \
	x $x \
	y $y \
	rx 3 \
	ry 3 \
	width $width \
	height $height \
	style $style

    $node appendChild  $parallelContainer

    set parallelHeader  [$doc createElement rect]
    $parallelHeader setAttribute \
	x [expr $x + 3] \
	y [expr $y + 3] \
	rx 0 \
	ry 0 \
	width [expr $width - 6] \
	height 15 \
	style "fill:#a2a5fd;stroke:#2b2a2d;stroke-width:.5;opacity:1" 

    $node appendChild  $parallelHeader

    if {[info exists text]} {

	set textNode [create_text \
			  -id "TextElement-$id" \
			  -x [expr $x + 8] \
			  -y [expr $y + 14]  \
			  -text_anchor "top" \
			  -font_size 10 \
			  -font_family "Verdana" \
			  -text "$text"]

	$node appendChild $textNode
    }

    return $node

}

ad_proc -public lamscentral::svg::create_grouping_effect {
    -id:required
    -x:required
    -y:required
    -width:required
    -height:required
    -style:required
} {

    Returns an svg node with all the grouping effect according to the size of the activity

    @author Ernie Ghiglione (ErnieG@mm.st)

} {

    set doc [dom setResultEncoding utf-8]
    set doc [dom createDocument node]
    set act_grouping [$doc createElement rect]

    $act_grouping setAttribute \
	id "${id}" \
	x [expr $x +4] \
	y [expr $y +4] \
	rx 3 \
	ry 3 \
	width $width \
	height $height \
	style "$style;stroke:#3b3b3b;stroke-width:3"

    return $act_grouping
    

}


ad_proc -public lamscentral::svg::create_optional {
    -id:required
    -x:required
    -y:required
    -text
    -grouping:required
} {

    Returns an svg node with all the optional details.

    @author Ernie Ghiglione (ErnieG@mm.st)

} {

    set doc [dom setResultEncoding utf-8]
    set doc [dom createDocument node]
    set node [$doc createElement g]

    $node setAttribute id "${id}" x $x y $y

    # Given that for now all parallel activities are just two
    # activities, we can hard code the width and height
    set width 135
    set height 135
    set style "stroke:black;stroke-width:1;opacity:1;fill:#d0defd"

    # if the parallel is grouped, show it
    if {[string eq $grouping "true"]} {
	set act_grouping [create_grouping_effect \
			      -id "${id}" \
			      -x $x \
			      -y $y \
			      -width $width \
			      -height $height \
			      -style $style]
	$node appendChild $act_grouping
    }

    set parallelContainer [$doc createElement rect]
    
    $parallelContainer setAttribute \
	x $x \
	y $y \
	rx 3 \
	ry 3 \
	width $width \
	height $height \
	style $style

    $node appendChild  $parallelContainer

    set parallelHeader  [$doc createElement rect]
    $parallelHeader setAttribute \
	x [expr $x + 3] \
	y [expr $y + 3] \
	rx 0 \
	ry 0 \
	width [expr $width - 6] \
	height 15 \
	style "fill:#a2a5fd;stroke:#2b2a2d;stroke-width:.5;opacity:1" 

    $node appendChild  $parallelHeader

    if {[info exists text]} {

	set textNode [create_text \
			  -id "TextElement-$id" \
			  -x [expr $x + 8] \
			  -y [expr $y + 14]  \
			  -text_anchor "top" \
			  -font_size 10 \
			  -font_family "Verdana" \
			  -text "$text"]

	$node appendChild $textNode
    }

    return $node

}


ad_proc -public lamscentral::svg::getImage {
    -image:required

} {

    Returns the path to an PNG image for the activity

    @author Ernie Ghiglione (ErnieG@mm.st)

} {

    set filex [file split $image]
    set count [expr [llength $filex] -1]

    set filename [lindex $filex $count]
    set png_filename [regsub .swf $filename .png]

    # if png_filename is empty then this is a grouping act:
    if {[empty_string_p $png_filename]} {
	set png_filename "icon_grouping.png"
    }

    return "http://lamscommunity.org/lamscentral/images/acts/$png_filename"

    

}