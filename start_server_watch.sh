#!/bin/bash
./gradlew server:build
./gradlew -t server:assemble -q --offline &
./gradlew server:run -q --offline
kill %1
