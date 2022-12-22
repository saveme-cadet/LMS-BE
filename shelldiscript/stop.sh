#!/bin/bash

# ---------------------
# stop.sh
# stop process
# ---------------------

# /home/lfin/servers/jdk
# 동일서버에 멀티 App 세팅 시 -01, -02, -NN 으로 세팅
# 하나이면 APP_NO=""
APP_NO=""
JAR_NAME=pmas-app
APP_NAME="${JAR_NAME^^}${APP_NO}" #UPPERCASE

echo "Stopping ${APP_NAME} ..."

pid=$(pgrep -f ${APP_NAME})

if [ "${pid}" = "" ]
then
  echo " there is no running process ${APP_NAME}"
  exit 0
fi

echo " killing process pid:${pid}"

kill -9 "${pid}"