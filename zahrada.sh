#!/bin/bash
#xrandr --output HDMI-0 --mode 1280x800 --rotate left
#xrandr --output VGA-0 --mode 1280x800 --right-of HDMI-0 --rotate right
#xrandr --output DVI-D-0  --rotate left
#xrandr --output VGA-0  --right-of DVI-D-0 --rotate right

export DISPLAY=:0.0
xset -dpms
xset s off

xrandr --output HDMI-0 --mode 1280x800 --rotate right
xrandr --output VGA-0 --mode 1280x800 --rotate left --right-of HDMI-0

#(sleep 1s && cd /home/kof/processing-2.2.1 && ./processing-java --sketch=/home/kof/sketchbook/instalaceMatyasPlzen/trackerGST/ --output=/tmp/trash --force --run) &
(sleep 1s && cd /home/kof/sketchbook/instalaceMatyasPlzen/trackerGST/application.linux/ && ./trackerGST) &

#(sleep 15s && cd /home/kof/sketchbook/instalaceMatyasPlzen/trackerGST/application.linux && ./catGame-3-linux.x86_64 -popupwindow -screen-width 1600 -screen-height 1280) &

#(sleep 15s && cd /home/kof/Downloads/catGame_linuBuild_ver1_0 && ./catGame-3-linux.x86_64 -popupwindow -screen-width 1600 -screen-height 1280) &

(sleep 1s && (cd /home/kof/sketchBook/instalaceMatyasPlzen/ && ./updateDebug.sh >> update.log 2>&1)&
# || (sleep 15s && cd /home/kof/Downloads/catGame_linuBuild_ver1_0 && ./catGame-3-linux.x86_64 -popupwindow -screen-width 1600 -screen-height 1280) )) &

#
#(sleep 15s && (cd /home/kof && ./updateGame.sh && (sleep 60s && xdotool key --repeat 5 --repeat-delay 100 Tab key Return))&
(sleep 60s && xdotool key --repeat 5 --repeat-delay 100 Tab key Return) &
