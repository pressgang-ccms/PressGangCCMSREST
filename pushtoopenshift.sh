#!/bin/bash
git add pom.xml
git add deployments/ROOT.war
git commit -m "Saving any changes to pom.xml"
cp pom.xml pom.xml.old
git rm pom.xml
git add deployments/ROOT.war
git commit -m "Pushing to OpenShift"
git push openshift master
mv pom.xml.old pom.xml
git add pom.xml
git commit -m "Reverting from push to OpenShift"
