@ECHO OFF
REM Extract OpenGeoProver.jar file in current directory
jar xf OpenGeoProver.jar

REM Move OpenGeoProver.jar under new lib directory
@ECHO ON
mkdir lib
move OpenGeoProver.jar lib
@ECHO OFF

REM Create input directory and copy sample files there
@ECHO ON
mkdir input
copy misc\documentation\testing_and_reports\geothm_xml_files_samples input
copy misc\documentation\testing_and_reports\ogp_xml_files_samples input
@ECHO OFF

REM Copy 3rd party libs from misc to current directory
@ECHO ON
mkdir 3rd_party_libs
copy misc\3rd_party_libs 3rd_party_libs
@ECHO OFF

REM Copy scripts directory from misc to current directory
@ECHO ON
mkdir scripts
copy misc\documentation\scripts\windows\runOGP.bat scripts
@ECHO OFF

REM To zip misc directory and all its contents use following command
REM 7z a misc.zip misc -r
REM 7z application must be installed on computer and exe file added to PATH

REM END
