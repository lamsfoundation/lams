ad_library {
    message formatting procs
}

namespace eval forum::format {

    ad_proc -public reply_subject { parent_subject } {
    
        Generates a subject string for a reply to an existing message.

    } {
        set subject "[_ forums.Re] $parent_subject"
        
        # trim multiple leading Re:
        regsub {^(\s*Re:\s*)*} $subject {Re: } subject

        return $subject
    }

    ad_proc emoticons {
        {-content:required}
    } {
        Convert smileys (emoticons) to img references. It constructs a relative 
        image reference to graphics/imagename.gif, so it will only work when
        used from pages that are directly under the forums/www/ directory.

        <p>

        If you change the images make sure to also adapt the image sizes in 
        the img tag that gets constructed here.
    } {
        set emoticons_map [list]

        # if you change this list, consider changing
        # www/doc/emoticons.adp as well
        foreach { emoticon image } {
            ":-)" "smile" 
            ";-)" "wink"
            ":-D" "bigsmile"
            "8-)" "smile8"
            ":-(" "sad"
            ";-(" "cry"
            ":-O" "scream"
            ":-/" "think"
        } {
            lappend emoticons_map $emoticon
            lappend emoticons_map "<img style=\"vertical-align:text-bottom\" src=\"/resources/forums/${image}.gif\" alt=\"$emoticon\" width=\"19\" height=\"19\">"
        }
        return [string map $emoticons_map $content]
    }

}
