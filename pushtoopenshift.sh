#!/bin/bash
cp pom.xml pom.xml.old
git rm pom.xml
git push openshift master
mv pom.xml.old pom.xml
git add pom.xml
