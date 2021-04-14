#!/usr/bin/env bash

mvn clean package
sudo docker build -t daiming/testprogram .
