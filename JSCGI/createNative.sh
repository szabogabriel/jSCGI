#!/bin/sh

mvn clean install

TARGET_FILE="target/$(ls target | grep jar)"

native-image -jar ${TARGET_FILE}

