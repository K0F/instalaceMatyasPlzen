#!/bin/bash
cd /tmp/ && wget -r --no-parent ftp://catgame:123456@192.168.0.150/catgame/ > /home/kof/sketchbook/instalaceMatyasPlzen/update.log 2>&1 && cd /tmp/192.168.0.150/catgame/ && ./catGame-3-linux.x86_64 -popupwindow -screen-width 1600 -screen-height 1280 >> /home/kof/sketchbook/instalaceMatyasPlzen/update.log 2>&1
