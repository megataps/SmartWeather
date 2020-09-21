#!/bin/bash
sh gradlew "clean"
sh gradlew "assemble$1Release"
