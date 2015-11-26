@ECHO OFF
REM First of all, enter the right directory of project
cd ..

REM call project's jar file
@ECHO ON
REM Butterfly theorem with default settings
 java -Djava.ext.dirs=3rd_party_libs ^
 -jar lib\OpenGeoProver.jar -I ogp_butterfly.xml
@ECHO OFF



REM OTHER EXAMPLES

REM Butterfly theorem with default settings
REM java -Djava.ext.dirs=3rd_party_libs ^
REM -jar lib\OpenGeoProver.jar -I ogp_butterfly.xml

REM Butterfly theorem without creating output reports
REM java -Djava.ext.dirs=3rd_party_libs ^
REM -jar lib\OpenGeoProver.jar -I ogp_butterfly.xml -i O -o N
 
REM Butterfly theorem in algebraic form
REM java -Djava.ext.dirs=3rd_party_libs ^
REM -jar lib\OpenGeoProver.jar -I geothm_butterfly.xml -i A -v

REM no logging in file:
REM java -Djava.ext.dirs=3rd_party_libs ^
REM -jar lib\OpenGeoProver.jar -I geothm_butterfly.xml -i A -v -l N

REM failure in final reminder calculation due to space limit exceeded error
REM java -Djava.ext.dirs=3rd_party_libs ^
REM -jar lib\OpenGeoProver.jar -I geothm_butterfly.xml -i A -v -s 150

REM failure in triangulation due to space limit exceeded error
REM java -Djava.ext.dirs=3rd_party_libs ^
REM -jar lib\OpenGeoProver.jar -I geothm_butterfly.xml -i A -v -s 7

REM example with two interior angles of circle
REM java -Djava.ext.dirs=3rd_party_libs ^
REM -jar lib\OpenGeoProver.jar -I geothm_interior_angles.xml -i A -v

REM example with time in seconds expired
REM java -Djava.ext.dirs=3rd_party_libs ^
REM -jar lib\OpenGeoProver.jar -I geothm_butterfly.xml -i A -v -t 0.1

REM timer set to greater value - didn't expire
REM java -Djava.ext.dirs=3rd_party_libs ^
REM -jar lib\OpenGeoProver.jar -I geothm_butterfly.xml -i A -v -t 2

REM keep prompt window opened
cmd 
