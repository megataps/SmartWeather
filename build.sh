#!/bin/bash
sh gradlew "clean"
sh gradlew "test"
sh gradlew "connectedAndroidTest"
sh gradlew "assemble$1Release"
