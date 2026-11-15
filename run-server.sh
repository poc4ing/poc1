#!/usr/bin/env bash
set -e

mvn clean install && java -Dspring.profiles.active=local -jar service/target/kyoe-ing-mortgage-service-1.0-SNAPSHOT.jar
