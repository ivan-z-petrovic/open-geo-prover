#!/bin/bash

# Extract OpenGeoProver.jar file in current directory
jar xf OpenGeoProver.jar

# Move OpenGeoProver.jar under new lib directory
mkdir lib
mv OpenGeoProver.jar lib

# Create input directory and copy sample files there
mkdir input
cp misc/documentation/testing_and_reports/geothm_xml_files_samples/* input
cp misc/documentation/testing_and_reports/ogp_xml_files_samples/* input

# Copy 3rd party libs from misc to current directory
mkdir 3rd_party_libs
cp misc/3rd_party_libs/* 3rd_party_libs

# Copy scripts directory from misc to current directory
mkdir scripts
cp misc/documentation/scripts/linux/runOGP.sh scripts
cd scripts
chmod a+x runOGP.sh
cd ..

# END
