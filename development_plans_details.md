# OGP Details about past, current and future development plans #

## Stages in development on the main development trunk ##

Following table outlines all stages of development work on main development trunk. Stages are not chronological but stage A must be the first stage and also some stages cannot be started until some other work has been previously completed.

| **Stage or sub-stage of work** | **Work Description** | **Work Status** | **Comment** |
|:-------------------------------|:---------------------|:----------------|:------------|
| **A**                          | _Initial development work_ | Testing + Documenting | Must be the first |
| **A.1**                        | _Algebraic primitives_ | **Finished**    |             |
| **A.2**                        | _Simple Wu's method_ | **Finished**    |             |
| **A.3**                        | _Transformation of geometry theorem to algebraic form_ | **Finished**    |             |
| **A.4**                        | _NDG Conditions - initial processing_ | **Finished**    |             |
| **A.5**                        | _Preparing documentation and examples_ | Ongoing         |             |
| **A.6**                        | _Testing and bug fixing_ | Ongoing         |             |
|                                |                      |                 |             |
| **CON**                        | _Adding Support for conic sections_ | Ongoing         | Low priority work |
| **CON.1**                      | _Basic support for conic sections_ | **Finished**    |             |
| **CON.2**                      | _Specific conic sections_ | Not Yet Started |             |
| **CON.3**                      | _Preparing documentation and examples_ | Ongoing         |             |
| **CON.4**                      | _Testing and bug fixing_ | Ongoing         |             |
|                                |                      |                 |             |
| **GB**                         | _Groebner basis method implementation_ | Not Yet Started | High priority work |
| **GB.1**                       | _Refactoring code for algebraic primitives_ | Not Yet Started |             |
| **GB.2**                       | _Implementation of Groebner basis method_ | Not Yet Started | Must start after **GB.1** |
| **GB.3**                       | _Preparing documentation and examples_ | Not Yet Started |             |
| **GB.4**                       | _Testing and bug fixing_ | Not Yet Started |             |
|                                |                      |                 |             |
| **CWU**                        | _Complete Wu's method_ | Not Yet Started | Must start after **GB.1** |
| **CWU.1**                      | _Implementation of complete Wu's method_ | Not Yet Started | Will be later split in few sub-stages |
| **CWU.2**                      | _Preparing documentation and examples_ | Not Yet Started |             |
| **CWU.3**                      | _Testing and bug fixing_ | Not Yet Started |             |
|                                |                      |                 |             |
| **GUI**                        | _GUI development for OGP_ | Not Yet Started | Low priority work |
| **GUI.1**                      | _GUI design_         | Not Yet Started |             |
| **GUI.2**                      | _GUI implementation_ | Not Yet Started |             |
| **GUI.3**                      | _Preparing documentation and examples_ | Not Yet Started |             |
| **GUI.4**                      | _Testing and bug fixing_ | Not Yet Started |             |
|                                |                      |                 |             |
| **REP**                        | _Refactoring current solution for report generation_<br>and logging<table><thead><th> Not Yet Started </th><th> Medium priority work </th></thead><tbody>
<tr><td> <b>REP.1</b>                   </td><td> <i>Ideas for refactoring</i> </td><td> Not Yet Started </td><td>             </td></tr>
<tr><td> <b>REP.2</b>                   </td><td> <i>Implementation of new solution for reporting</i> </td><td> Not Yet Started </td><td>             </td></tr>
<tr><td> <b>REP.3</b>                   </td><td> <i>Refactoring solution for logging</i> </td><td> Not Yet Started </td><td>             </td></tr>
<tr><td> <b>REP.4</b>                   </td><td> <i>Preparing documentation and examples</i> </td><td> Not Yet Started </td><td>             </td></tr>
<tr><td> <b>REP.5</b>                   </td><td> <i>Testing and bug fixing</i> </td><td> Not Yet Started </td><td>             </td></tr>
<tr><td>                                </td><td>                      </td><td>                 </td><td>             </td></tr>
<tr><td> <b>No name<code>[</code>To Be Decided<code>]</code></b> </td><td> <i>N/A</i>           </td><td> Ideas consideration </td><td> Will be split in new stages later <br>Something could go to new branch first </td></tr>
<tr><td>                                </td><td> <i>Implementation of Generic XML format for</i><br>geometry theorems</td><td> Not Yet Started </td><td>             </td></tr>
<tr><td>                                </td><td> <i>Implementation of certificate for formal verification</i><br>of Wu's method</td><td> Not Yet Started </td><td> High priority work </td></tr>
<tr><td>                                </td><td> <i>Implementation of parallel algorithms for</i><br>polynomial multiplication</td><td> Work Started    </td><td> Low priority work </td></tr>
<tr><td>                                </td><td> <i>Refactoring code for transformation of</i><br>geometry theorems</td><td> Not Yet Started </td><td> Low priority work </td></tr>
<tr><td>                                </td><td> <i>Implementation of algebraic primitives that spend</i><br> less memory for Wu's method</td><td> Not Yet Started </td><td> Medium priority work;<br> Must start after <b>GB.1</b> </td></tr>
<tr><td>                                </td><td> <i>Other theoretical concepts</i> </td><td> Not Yet Started </td><td> Low priority work </td></tr>
<tr><td> ...                            </td><td> ...                  </td><td> ...             </td><td>             </td></tr>
<tr><td>                                </td><td>                      </td><td>                 </td><td>             </td></tr></tbody></table>

The details about stages are given below:<br>
<br>
<ol><li><b>A-stage</b> or initial development, that started by reimplementation of simple Wu's method from <a href='http://poincare.matf.bg.ac.rs/~janicic//gclc/'>GCLC geometry system</a> in Java programming language<br>
<ul><li><b>A.1</b> - Implementation of main algebraic primitives (variables, powers, terms, polynomials and polynomial systems) and main algorithms for manipulation with these primitives.<br>
</li><li><b>A.2</b> - Implementation of simple Wu's method (algorithm for triangulation and calculation of final reminder).<br>
</li><li><b>A.3</b> - Transformation of geometry constructions and theorem statements to algebraic form.<br>
</li><li><b>A.4</b> - Processing of NDG conditions.<br>
</li></ul></li><li><b>CON-stage</b> is about adding support for conic sections to OGP<br>
<ul><li><b>CON.1</b> - Adding basic support which means parametric sets, general conic sections and conics through five points.<br>
</li><li><b>CON.2</b> - Adding support for specific conic sections like ellipses, hyperbolas and parabolas (all will be implemented by usage of parametric sets and special coordinates choice to fit desired equation of conic section).<br>
</li></ul></li><li><b>GB-stage</b> is for implementation of Groebner basis method<br>
<ul><li><b>GB.1</b> - First step which is about refactoring code for algebraic primitives. That way prover will become more flexible and will be able to work with different implementations of polynomials - some of them will be more efficient with memory, some will have faster algorithms for multiplication, some will be best for specific algebraic proving method.<br>
</li><li><b>GB.2</b> - Implementation of algorithms used by Groebner basis method.<br>
</li></ul></li><li><b>CWU-stage</b> is about implementation of Complete Wu's method for Automated Geometry Theorem Proving<br>
<ul><li><b>CWU.1</b> - Implementation of suitable algebraic primitives and algorithms for complete Wu's method - <a href='http://www.cs.wichita.edu/~ye/'>JGEX</a> can be used as a reference<br>
</li></ul></li><li><b>GUI-stage</b> is for implementation of GUI for OGP as a standalone application. It is not yet decided but probably <a href='http://qt-jambi.org/'>Qt Jambi</a> will be used for this purpose.<br>
</li><li><b>REP-stage</b> is for refactoring current solution for reporting and logging in OGP<br>
<ul><li><b>REP.1</b> - Current code is very saturated/polluted by pieces for low level logic related to report generation. Therefore it is necessary to consider idea for implementation of internal journal to track all actions taken by the prover at the moment of their realization and later to generate a report from that journal. Also current project uses only one type of logger. Project should provide usage of various loggers hence some kind of generalization is required.<br>
</li></ul></li><li><b>Other stages and work to be done</b>
<ul><li><b>Refactoring code for transformation of geometry theorems</b> - it is necessary to precisely define a construction in terms of Automated Geometry Theorem Proving and compare it with traditional mathematical concept of geometry construction. It is necessary to distinguish general geometry constructions from those that can be used in algebraic proving methods. Also, this stage covers questions about instantiation of points and usage of special coordinates - heuristics related to them. As a part of this stage it is also reasonable to consider the relationship between geometry objects and Analytic Geometry - to answer the questions like whether it is possible to represent geometry objects by their equations in Cartesian coordinates and obtain polynomial representation from these equations rather then deriving polynomials from geometry properties of constructed objects.<br>
</li><li><b>Implementation of algebraic primitives that spend less memory for Wu's method</b> - current implementation of algebraic primitives used in Wu's method has too much redundancy and unnecessary data. Instead of using large Java objects like <code>Vector&lt;...&gt;</code>, <code>ArrayList&lt;...&gt;</code> and <code>TreeMap&lt;...&gt;</code> it has to be reviewed how the memory can be saved. For example it is enough to keep only exponents at indices of variables that appear in one term of polynomial, instead of whole objects for variables and powers. Also, current implementation of primitives has fractions that are not required for simple Wu's method. This stage is about thinking of idea for better implementation that saves the memory and uses efficient algorithms and about implementation of new solution.<br>
</li><li><b>Other theoretical concepts</b> - question about:<br>
<ul><li>multivariate polynomial factorizations<br>
</li><li><a href='http://en.wikipedia.org/wiki/Resultant'>polynomial resultant</a> and its usage in algebraic proving<br>
</li><li>Rc-constructability - whether it is possible to construct the geometry object with specified properties from input elements - examination by usage of algebraic proving methods<br>
</li><li>application of algebraic proving methods in general problems of polynomial functional dependency (algebraic topics)<br>
</li><li>application of algebraic provers in other branches of geometry (stereometry, projective geometry, hyperbolic geometry, affine geometry ...)<br>
</li><li>overcoming limitations in classic algebraic proving methods related to axioms of order - variations of algebraic proving methods.</li></ul></li></ul></li></ol>


<b>Note:</b>
<blockquote>Every project issue created to track a single specific piece of work has to have a mark of at least stage of work it is related to, but better if more detailed sub-stage label is provided. For more information about issues of this project visit <a href='important_issues.md'>Issues</a> page.</blockquote>


<h2>All Branches</h2>

Following table outlines all development baselines of OGP project.<br>
<br>
<table><thead><th> <b>Destination of development branch</b> </th><th> <b>Description</b> </th><th> <b>Status</b> </th><th> <b>Comment</b> </th></thead><tbody>
<tr><td> trunk                                    </td><td> MAIN Development baseline </td><td> <b>Open; under development</b> </td></tr>
<tr><td> branches/geogebra_ogp                    </td><td> Integration of OGP with GeoGebra </td><td> <b>Open; under development</b> </td><td> cut from trunk </td></tr>
<tr><td> branches/geogebra_ogp_for_area_method    </td><td> Implementation of Area Method </td><td> <b>Open; under development</b> </td><td> cut from branches/geogebra_ogp; <br>temporary baseline </td></tr>
<tr><td> branches/ogp_full_angle_method           </td><td> Implementation of Full Angle Method </td><td> <b>Open; under development</b> </td><td> cut from trunk; <br>temporary baseline </td></tr>
<tr><td> <code>[TBD]</code>                       </td><td> Implementation of algebraic provers <br>for hyperbolic geometry </td><td>               </td><td>                </td></tr>
<tr><td> ...                                      </td><td> ...                </td><td>               </td><td>                </td></tr></tbody></table>


<h2>Details about other branches</h2>

<ul><li><a href='ogp_for_geogebra_plan_details.md'>Integration of OGP with GeoGebra</a>
</li><li><a href='ogp_with_area_plan_details.md'>Implementation of Area Method</a>
</li><li><a href='ogp_with_fam_plan_details.md'>Implementation of Full Angle Method</a>
</li><li>...</li></ul>

<br>
<br>
<h2>Plans for making branches</h2>
<h4>Integration with GeoGebra</h4>
Following diagram shows the development plan for integration of OGP into GeoGebra with implementation of Area method.<br>
<img src='http://open-geo-prover.googlecode.com/svn/wiki/images/baselines_diagram01.png' />

<ol><li>New baseline will be cut from branch dedicated to integration of OGP with GeoGebra, and implementation of Area method will start on that baseline. The idea is to separate two parallel independent development processes while it is not certain how they will affect each other and while ideas are in initial form.<br>
</li><li>Every change made on basic branch for GeoGebra will be merged up to branch for Area method to keep them as much consistent as possible.<br>
</li><li>Every bug found on Area method branch or every desired improvement that are not specific to Area method must be merged down to basic branch for integration with GeoGebra and even up to main development trunk (if it is some general thing).<br>
</li><li>Every Area method specific changes will be done only on the Area method baseline.<br>
</li><li>This situation should last not longer than 2-3 months. In summer we expect to have basic prover functionality implemented in GeoGebra and also main development ideas and concepts developed for Area method implementation. At that moment we will merge all Area method specific code from its baseline down to basic development baseline of OGP for GeoGebra and we will then delete Area specific branch. All further development work for integration of OGP with GeoGebra will continue on the single branch (the basic one) for all types of provers.<br>
</li><li>Since Area method is needed in OGP not only for GeoGebra, at some point in the future that code will be merged from GeoGebra's branch up to main development trunk. But it is still not planned and that is very low priority work at this moment (as per plans for development on main trunk).</li></ol>

<h4>Implementation of Full Angle Method of GATP</h4>
<ol><li>New temporary baseline has been created from main DEV trunk.<br>
</li><li>After implementation is over and well tested on new branch it will be merged up to main DEV trunk and to GeoGebra trunk and then the temporary baseline will be turned off.