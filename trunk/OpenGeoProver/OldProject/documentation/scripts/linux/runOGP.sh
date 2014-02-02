#!/bin/bash

# First of all, enter the right directory of project
cd ..

# call project's jar file

# Butterfly theorem with default settings
java -Djava.ext.dirs=3rd_party_libs -jar lib/OpenGeoProver.jar -I ogp_butterfly.xml



# OTHER EXAMPLES

# Butterfly theorem with default settings
# java -Djava.ext.dirs=3rd_party_libs -jar lib/OpenGeoProver.jar -I ogp_butterfly.xml

# Butterfly theorem without creating output reports
# java -Djava.ext.dirs=3rd_party_libs -jar lib/OpenGeoProver.jar -I ogp_butterfly.xml -i O -o N
 
# Butterfly theorem in algebraic form
# java -Djava.ext.dirs=3rd_party_libs -jar lib/OpenGeoProver.jar -I geothm_butterfly.xml -i A -v

# no logging in file:
# java -Djava.ext.dirs=3rd_party_libs -jar lib/OpenGeoProver.jar -I geothm_butterfly.xml -i A -v -l N

# failure in final reminder calculation due to space limit exceeded error
# java -Djava.ext.dirs=3rd_party_libs -jar lib/OpenGeoProver.jar -I geothm_butterfly.xml -i A -v -s 150

# failure in triangulation due to space limit exceeded error
# java -Djava.ext.dirs=3rd_party_libs -jar lib/OpenGeoProver.jar -I geothm_butterfly.xml -i A -v -s 7

# example with two interior angles of circle
# java -Djava.ext.dirs=3rd_party_libs -jar lib/OpenGeoProver.jar -I geothm_interior_angles.xml -i A -v

# example with time in seconds expired
# java -Djava.ext.dirs=3rd_party_libs -jar lib/OpenGeoProver.jar -I geothm_butterfly.xml -i A -v -t 0.1

# timer set to greater value - didn't expire
# java -Djava.ext.dirs=3rd_party_libs -jar lib/OpenGeoProver.jar -I geothm_butterfly.xml -i A -v -t 2

