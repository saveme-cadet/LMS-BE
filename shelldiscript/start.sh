#! /bin/bash
# ---------------------
# start.sh
# start process
# ---------------------

export PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:${PATH}

USER=lfin
JAVA_HOME="/home/${USER}/servers/jdk"
echo "JAVA_HOME ${JAVA_HOME}"

# /home/lfin/servers/jdk
# 동일서버에 멀티 App 세팅 시 -01, -02, -NN 으로 세팅
# 하나이면 APP_NO=""
APP_NO=""
JAR_NAME=pmas-app
APP_NAME="${JAR_NAME^^}${APP_NO}" #UPPERCASE

#APP_PKGNAME=root
APP_HOME="/home/${USER}/servers/pmas-app${APP_NO}"
JAR_FILE="${APP_HOME}/libs/${JAR_NAME}-*.jar"

CONF_FILE=${APP_HOME}/conf/application.yml

# newrelic
# newrelic 모니터링 사용할때
JAR_NEWRELIC=${APP_HOME}/libs/newrelic.jar

# spring profile (local,dev,stage,prod)
SPRING_PROFILE=dev

# java option
JAVA_OPTIONS="-Xms1024m -Xmx2048m "
JAVA_OPTIONS=" ${JAVA_OPTIONS} -Dspring.profiles.active=${SPRING_PROFILE} "
JAVA_OPTIONS=" ${JAVA_OPTIONS} -Dspring.config.location=${CONF_FILE} "
JAVA_OPTIONS=" ${JAVA_OPTIONS} -javaagent:${JAR_NEWRELIC}  "

# java run cmd
RUN_CMD="${JAVA_HOME}/bin/java ${JAVA_OPTIONS} -jar ${JAR_FILE} ${APP_NAME}"

echo "starting ${APP_NAME}"

# check running process
pid=$(/usr/bin/pgrep -f ${APP_NAME})

if [ "${pid}" != "" ]; then
  echo " skip start process ${APP_NAME}"
  echo " other process pid:${pid}"
  exit 1
fi

/usr/bin/nohup ${RUN_CMD} > /dev/null 2>&1 &
#${RUN_CMD}

pid2=$(/usr/bin/pgrep -f ${APP_NAME})

echo " started process pid:${pid2}"