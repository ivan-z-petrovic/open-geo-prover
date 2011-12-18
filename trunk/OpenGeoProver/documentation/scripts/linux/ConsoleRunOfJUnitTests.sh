#!/bin/bash

# Call JUnit test

# Plain TestRunner
java -cp ../build/jutest;../3rd_party_libs/junit-4.9b2.jar;../3rd_party_libs/log4j-1.2.16.jar junit.textui.TestRunner com.ogprover.test.junit.JUTestPolynomialsSuite

# SwingGUI TestRunner
java -cp ../build/jutest;../3rd_party_libs/junit-3.8.2.jar;../3rd_party_libs/log4j-1.2.16.jar junit.swingui.TestRunner com.ogprover.test.junit.JUTestPolynomialsSuite

 # OTHER EXAMPLES ...
 