# Quick description of the area method's general idea #

The area method is another geometric theorem proving method, which is being implemented in OpenGeoProver this summer (2012) during the Google Summer of Code program by Damien Desfontaines (ddfontaines - at - gmail - dot - com).

Compared to Wu's method or the Gröbner basis method, it is a little less powerful: it can not deal with conics, for example. On the other hand, it has one big advantage - being not an algebraic method, it can produce human-readable proofs. This proofs are generally not the easiest, and they contain sometimes long lines of calculus, but they always remain close to the geometric construction.

The method is based on several observations :

  1. All classical geometrical assertions (AB is parallel/perpendicular to CD, AB has the same length as CD, angle <ABC has the same measure as <DEF, etc.) can be described as equalities between geometric quantities of two types :
    * oriented ratios AB/CD, where AB and CD are parallel,
    * S<sub>ABC</sub>, the (oriented) area of the triangle ABC,
    * and Pythagoras difference P<sub>ABC</sub>, defined as AB<sup>2</sup> + CB<sup>2</sup> - AC<sup>2</sup>. <p>For example, "A and B belong to the same circle arc CD" can be re-written "<b>S<sub>ACD</sub> <code>!=</code> 0, S<sub>BCD</sub> <code>!=</code> 0, and S<sub>CAD</sub><code>*</code>P<sub>CBD</sub> = S<sub>CBD</sub><code>*</code>P<sub>CAD</sub></b>".</p>
  1. A large subset of figures constructed by ruler and compass can also be made with a few (5) construction rules, on a formal system which deals only with points. A figure, to be used with the area method, has to be constructed using only these primitives:
    * Construction of an arbitrary free point,
    * Intersection of two lines
    * Foot from a given point to a line
    * Point Y on the line passing through a given point W, parallel to a given line AB, and verifying WY = rUV, where r is a rational expression in geometric quantities, or a variable.
    * Construction of a point Y on the line passing throug a point U and perpendicular to a line UV, such that 4S<sub>UVY</sub>/P<sub>UVY</sub> = r, where r is a rational expression in geometric quantities, or a variable. <p>When we say "a line", it is actually given in the form of two different points. So, each one of the primitive constructions above constructs a new point from a set of points.</p><p>The only "ruler and compass" construction which is impossible to describe with this system is the intersection between a circle and a line, or the intersection between two circles. To be more specific (and this last remark explains why this limitation is not really problematic), the only thing which is actually impossible to do is to distinguish the two points created this way. So, if one is already known (if we want to have the intersection between the line UV and the circle centered in O, passing through U, for example), it becomes possible.</p>
  1. This primitives can easily be translated into equalities between the geometric quantities exposed above.

So, the principle is simple: the algorithms process all the construction steps in reverse order, and in the formula it tries to show, every occurrence of intermediate points is replaced with previous points, thanks to "elimination lemmas" (allowing to do that for all types of quantities, and for all primitives). Finally, all we have to prove is the equality between two rational expressions, which can be made very easily.

That's for the general idea. All the details can be found here : http://hal.inria.fr/hal-00426563/PDF/areaMethodRecapV2.pdf . All my implementation follows the ideas and the results presented in this paper, so if you want to fully understand my code, this is a good idea to read it first.


# Actual implementation of the area method #

### Internal representation of the geometric constructions ###

The area method only deal with points. So, among the many different construction classes for lines and circles implemented in OpenGeoProver, I only use LineThroughTwoPoints and CircleWithCenterAndPoint.

The points can be constructed only in five different ways, as described above. The five corresponding classes are !PRatioPoint, !TRatioPoint, !AMFootPoint, !AMIntersectionPoint and FreePoint. They all inherit from the Point abstract class, itself inheriting from the GeoConstruction abstract class (all these classes are a part of the com.ogprover.pp.tp.geoconstruction package).

All the constructions of the figure are stored in the "Vector`<GeoConstruction`> constructions" field of AreaMethodProver.java class (package com.ogprover.thmprover), in the same order as they were actually constructed.

### Conversion from the GeoGebra constructions to the OGP intern representation ###

The conversion methods for constructions can all be found in the !GGConsConverterForAreaMethod.java class (package com.ogp.api.converter). All methods of this class (except the nextAvailableName() in the end) take the input arguments of each GeoGebra command, and add in the theorem protocol the corresponding GeoConstructions.

As you can see, a lot of conversion work is done here, because the area method has only five different ways to add new constructions to a figure. If we take, for example, the convertLineBisector method, we have to construct the bisector between two points A and B using only the features of the area method. This is done this way :
  * First, we take the midpoint between the two points, or in other terms, the point M such as AB/AM = 2. We can do this using the PRatio construction : M = PRatio(A,A,B,1/2).
  * Then, to draw the perpendicular line, we need a second point (because our lines have to be instances of LineThroughTwoPoints). We can construction this second point using the TRatio construction, with any coefficient.
  * Lastly, we can return the line between the two intermediary points just created.

You have probably noticed that it can lead to the creation of many intermediary points. Currently we give them names of the form iP1, iP2, iP3, etc, but it can change very easily by modifying the nextAvailableName() method. Work could (and should) be done to limit the number of this intermediary points : in our previous example, if the user had already created the midpoint between A and B, the first intermediary point should be replaced by the new one. Currently, this is not done, but it would improve the efficientness of the algorithm.

### Conversion from the GeoGebra statements to the area method representation ###

The statements must be converted too. OGP has a long list of classes, each one representing a different statement (they are in the com.ogprover.pp.tp.thmstatement package). The translation from GeoGebra command to this representation is pretty simple, and can be found in the !GGStatConverterForAreaMethod class, which is very similar to the !GGStatConverterForAlgebraicProvers (all being in com.ogp.api.converter). The main difference is on the convertEqual() command : the area method can deal with any expression equality, so here is a parsing step to create the EqualityOfExpressions instance.

Then, for the area method, a second conversion step is performed : one have to translate the statement in the form of an (or several) equality(ies) between rational expressions in geometrical quantities. These conversions are implemented in the getAreaMethodStatement() method, in each com.ogprover.pp.tp.thmstatement class. Sometimes it is pretty simple, for example three points are collinear iff the area of the corresponding triangle is zero. Sometimes it is much more complicated, see for example the getAreaMethodStatement() method of the ConcurrentLines.java class.

This last conversion step returns an instance of an AreaMethodStatement.java class, and this class contain a Vector`<AMExpression`> field. The statement is true iff all the !AMExpressions of the vector are equal to zero.

### Implementation of the area method expressions ###

Reading the Julien Narboux' paper, you have probably understood that the area method manipulated rational expressions in geometrical quantities. These expressions are implemented in the com.ogprover.pp.tp.expressions class. The base abstract class is !AMExpression.java, and then, it has a tree structure.

The internal nodes of the trees are :
  * Sum.java (class for representing the sum between two expressions)
  * Difference.java (same thing for difference)
  * Product.java (you got the idea)
  * Fraction.java
  * and AdditiveInverse.java.
The external nodes are :
  * AreaOfTriangle (represents what is called S<sub>ABC</sub> in the Narboux' paper)
  * PythagorasDifference (represents P<sub>ABC</sub>)
  * Ratio (represents the ratio of two collinear segments)
  * Number (represents simple integers)

By modifying the Number class, we could in the future deal with a larger base field than the rational numbers.

Several methods have been written to manipulate this formal expressions :

#### Uniformization ####

The uniformisation process is described in the 2.5.2 section of the Narboux' paper. Its goal is to uniformize the geometric quantities, for being able to simplify more things. For example, for any points A, B and C, S<sub>ABC</sub> - S<sub>BCA = 0. But the simplification process will not be able to find out that S</sub>ABC<sub> = S</sub>BCA, so the uniformization process will transform the second term into S<sub>ABC</sub> , to make this simplification possible.

The order used on points is the alphabetical order : S<sub>ACB</sub> will be transformed to -S<sub>ABC</sub> , for example.

#### Simplification ####

The simplification step is very important. The more an expression is simplified, the faster the future computations steps will be.

Currently, the simplifications implemented are exactly those explained in the Narboux' paper, in the section 2.5.3. In the code, they are implemented in the simplifyInOneStep() method. Then, to fully simplify an expression, we apply several times simplifyInOneStep() on the expression, until simplifyInOneStep() no longer changes anything.

#### Elimination of a point ####

All the area method is based on the elimination of all construction points in a formula. The eliminate() method eliminates a given point from every geometric quantity in a formula, replacing it with the corresponding expression. All the eliminations lemmas are described in the 2.4.1 section of Narboux' paper.

The eliminate() method takes as an argument a point, but also a link to the current prover - this is because in certain cases, a auxiliary proof is needed to determine whether three points are collinear or not. You can see in particular the lemmas 2.9 to 2.12 in the Narboux' paper, which are implemented in the Ratio.java#eliminate() method.

#### Reducing to a single fraction ####

The reduceToSingleFraction() method explains pretty weel what it does : it transforms a/b + c/d into (ac+bd)/bd, etc. This is useful to transform an equality of the form expr=0, where expr contains +, -, × and /, into an equality of the form expr'=0, where expr' contains +, - and ×. In more mathematical terms, it transforms an equation in a field into an equation in a ring.

There is no loss of information during this process : all the denominators generated are non-zero if the non-degeneracy-conditions of the figure (generated during the conversion process) are verified.

#### Reduct to right associative form ####

The expressions are implemented with a tree structure, but the tree does not have a particular form. Considering the simplification step only looks the two child nodes of each node to see if there are some possible simplifications, the expressions are usually not fully simplified, for example (a×(b×c))-((a×b)×c).

So, the reductToRightAssociativeForm() method transforms a rational expression on a ring to an expression into a "right associative form". As described in the souce code comments, an expression is said to be in right associative form if it is a sum of products of "basic" terms, if every product is represented as a tree where all the left childs are basic terms ( (a×(b×(c×(d×e)))), for example, but not (a×b)×(c×(d×e)) ), and if the sum is also such a tree.

Furthermore, every product starts with a constant number, and this is the only place where there are constant numbers.

Again, one can not do this in once : we apply several times the reductToRightAssociativeFormInOneStep() method, until nothing changes.

This method can take very long if the expressions are big : in worst cases, its complexity is exponential. Maybe there is a way to make this process faster (by doing on-the-fly simplifications ?).

#### Grouping of the producs ####

Once the expression is in a right associative form, we have to group all the products in the sum : (-1)×a×b + 4×b×b + 2×b×a becomes 1×a×b + 4×b×b, for example. This is done with the groupSumOfProducts() method, using the auxiliary methods areSameProducts and addProductToSum().

#### Transformation into an independant-variables expression ####

In many cases, the elimination of all points, the reducing into a single fraction, the reduction to right associative form and the simplification of the resulting expression is enough to get zero, and so prove the desired statement. But sometimes it is not the case. For detailed explanation, see the section 2.5.4 of the Narboux' paper.

The transformations detailed in this sections are implemented in the toIndependantVariables() method. Similarly to the elimination process, we need to have a copy of the current prover, to verify whether points are collinear or not - see Lemma 2.22.


### Area method algorithm ###

The source code of the AreaMethodProver.java#prove() method is, in my opinion, pretty clear :
  1. First, we verify if the desired statement has been already proved.
  1. If not, for each statement of the theorem :
    * We eliminate all the construction points appearing in the corresponding expression, and uniformize, reduce to a single fraction and simplify the resulting expression.
    * When it's done, the resulting expression - which contains only free points - is transformed to a right associative form, the products are grouped and simplified.
    * If this gives us zero, the statement is proved.
    * Else, the formula is transformed to independant variables, uniformized, reduced to a single fraction, reduced again into a right associative form, the products are grouped, and simplified. If the result is zero, the statement is proved. Else, the statement is proved to be false.
  1. If all the statements of the theorem are true, then the theorem is true.

> For now, there are several debug messages. In the future, they will be replaced by the storage of each step, for future pretty-printing of the proof.