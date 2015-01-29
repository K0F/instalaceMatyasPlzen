#!/bin/bash
cd /tmp/ && wget -r --no-parent ftp://catgame:123456@192.168.0.150/catgame/ && cd /tmp/192.168.0.150/catgame/ && chmod 775 ./catGame-3-linux.x86_64 && ./catGame-3-linux.x86_64 -popupwindow -screen-width 1600 -screen-height 1280
