#!/bin/bash

for f in *.${1}
do 


  filename=$(basename "$f")
  extension="${filename##*.}"
  filename="${filename%.*}" 

  renamefile="${filename}.swf"
  echo "Processing... $filename $renamefile"

  svg2swf "${f}" "${renamefile}"

done
