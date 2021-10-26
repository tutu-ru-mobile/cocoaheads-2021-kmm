#!/bin/bash
./gradlew -t server:build &
./gradlew server:run
kill %1
