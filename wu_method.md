(page is still incomplete)

# Operation of pseudo division of two polynomials #

Formulae for pseudo division:

Let **p** and **q** be multivariate polynomials and **p(x)** and **q(x)** are they as polynomials by single variable **x**.

![http://open-geo-prover.googlecode.com/svn/wiki/images/prem_formulae.png](http://open-geo-prover.googlecode.com/svn/wiki/images/prem_formulae.png)

# Steps in simple(partial) Wu's method #
  1. Geometry constructions are represented as (transformed into) system of polynomials and they are hypotheses of the theorem. Theorem statement is represented as (transformed into) a single polynomial;
  1. Triangulation of input system of polynomials for hypotheses (by using pseudo division of polynomials);
  1. Calculation of final reminder of statement polynomial by using successive pseudo divisions of that polynomial with each polynomial from triangular system;
  1. If pseudo reminder is equal to zero (polynomial) theorem has been proved, if it is not zero then if input system of hypotheses is linear, the theorem has been disproved, otherwise it is not known whether theorem statement is true or false.