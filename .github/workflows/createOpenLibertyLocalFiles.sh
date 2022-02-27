#!/usr/bin/env bash

script="${BASH_SOURCE[0]}"
scriptDir="$(cd "$(dirname "${script}")" && pwd)"

repoRootDir="$(dirname "$(dirname ${scriptDir})")"/cargotracker-liberty

echo $repoRootDir

mv ${repoRootDir}/src/main/java/org/eclipse/cargotracker/infrastructure/messaging/JmsQueueNames.java ${repoRootDir}/src/main/java/org/eclipse/cargotracker/infrastructure/messaging/JmsQueueNames.java.moved
cp ${repoRootDir}/src/main/liberty/jms/JmsQueueNames.java ${repoRootDir}/src/main/java/org/eclipse/cargotracker/infrastructure/messaging/

mv ${repoRootDir}/src/main/java/org/eclipse/cargotracker/infrastructure/messaging/jms/JmsApplicationEvents.java ${repoRootDir}/src/main/java/org/eclipse/cargotracker/infrastructure/messaging/jms/JmsApplicationEvents.java.moved
cp ${repoRootDir}/src/main/liberty/jms/JmsApplicationEvents.java ${repoRootDir}/src/main/java/org/eclipse/cargotracker/infrastructure/messaging/jms/

mv ${repoRootDir}/src/main/webapp/WEB-INF/web.xml ${repoRootDir}/src/main/webapp/WEB-INF/web.xml.moved
cp ${repoRootDir}/src/main/liberty/webapp/WEB-INF/web.xml ${repoRootDir}/src/main/webapp/WEB-INF/
