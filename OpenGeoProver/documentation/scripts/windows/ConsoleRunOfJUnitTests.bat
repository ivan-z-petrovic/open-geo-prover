@ECHO OFF
REM Call JUnit test

REM Plain TestRunner
@ECHO ON
java -cp ..\build\jutest;..\3rd_party_libs\junit-4.9b2.jar;..\3rd_party_libs\log4j-1.2.16.jar ^
 junit.textui.TestRunner com.ogprover.test.junit.JUTestPolynomialsSuite

@ECHO OFF
REM SwingGUI TestRunner
@ECHO ON
java -cp ..\build\jutest;..\3rd_party_libs\junit-3.8.2.jar;..\3rd_party_libs\log4j-1.2.16.jar ^
 junit.swingui.TestRunner com.ogprover.test.junit.JUTestPolynomialsSuite
@ECHO OFF


REM OTHER EXAMPLES


REM keep prompt window opened
cmd 
