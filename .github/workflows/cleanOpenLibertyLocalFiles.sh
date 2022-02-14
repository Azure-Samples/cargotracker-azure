#!/usr/bin/env bash

script="${BASH_SOURCE[0]}"
scriptDir="$(cd "$(dirname "${script}")" && pwd)"

repoRootDir="$(dirname "$(dirname ${scriptDir})")"

echo $repoRootDir

rm ${repoRootDir}/src/main/java/org/eclipse/cargotracker/infrastructure/messaging/JmsQueueNames.java
mv ${repoRootDir}/src/main/java/org/eclipse/cargotracker/infrastructure/messaging/JmsQueueNames.java.moved ${repoRootDir}/src/main/java/org/eclipse/cargotracker/infrastructure/messaging/JmsQueueNames.java

rm ${repoRootDir}/src/main/java/org/eclipse/cargotracker/infrastructure/messaging/jms/JmsApplicationEvents.java
mv ${repoRootDir}/src/main/java/org/eclipse/cargotracker/infrastructure/messaging/jms/JmsApplicationEvents.java.moved ${repoRootDir}/src/main/java/org/eclipse/cargotracker/infrastructure/messaging/jms/JmsApplicationEvents.java

rm ${repoRootDir}/src/main/webapp/WEB-INF/web.xml
mv ${repoRootDir}/src/main/webapp/WEB-INF/web.xml.moved ${repoRootDir}/src/main/webapp/WEB-INF/web.xml
