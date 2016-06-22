#!/bin/bash

mvn clean package -DskipTests

mv target/agreenserver.war target/agreenserver.jar

mv target/agreenserver.war.original target/agreenserver.war

rsync -azr target/agreenserver.* kojo@smartxms:/home/kojo/

#rsync -azr webapp/* kojo@smartxms:/home/kojo/webroot/agreen

