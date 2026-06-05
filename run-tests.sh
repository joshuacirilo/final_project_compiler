#!/usr/bin/env bash
set -u
rm -rf out
mkdir -p out
javac -encoding UTF-8 -d out src/*.java tests/*.java || exit 1
java -cp out TestRunner
