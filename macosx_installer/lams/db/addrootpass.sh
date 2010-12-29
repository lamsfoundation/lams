unset -v input

input="$(/usr/bin/osascript 2>/dev/null <<-'__HEREDOC__'
   with timeout of 300 seconds
      tell application "Finder"
         activate
         set Input to display dialog "Please enter your MySQL Root password:" \
            with title "Password" \
            with icon caution \
            default answer "" \
            buttons {"Cancel", "OK"} \
            default button 2 \
            with hidden answer \
            giving up after 295
         return text returned of Input as string
      end tell
   end timeout
__HEREDOC__
)"

echo "db.root.password=${input}" >> db.properties

