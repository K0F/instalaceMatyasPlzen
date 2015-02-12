#!/bin/bash
export DISPLAY=:0.0
xset -dpms
xset s off
(cd /tmp/ && wget -r --no-parent ftp://catgame:123456@192.168.0.150/catgame/ > /home/kof/sketchbook/instalaceMatyasPlzen/update.log 2>&1 && cd /tmp/192.168.0.150/catgame/ && chmod 775 ./catGame-3-linux.x86_64 && ./catGame-3-linux.x86_64 -popupwindow -screen-width 1600 -screen-height 1280 >> /home/kof/sketchbook/instalaceMatyasPlzen/update.log 2>&1)&
(sleep 60s && cd /home/kof/sketchbook/instalaceMatyasPlzen/ && git add . ; git commit -am "change @ production machine`date`"; git pull; git push)&
