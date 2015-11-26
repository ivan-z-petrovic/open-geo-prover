# Integration of OGP with GeoGebra #

This development is done in Java 1.5.0\_22 version of Java SDK.

## Stages in development work on branch dedicated for integration of OGP to GeoGebra ##

Following table outlines main stages of development work.

| **Stage or sub-stage of work** | **Work Description** | **Work Status** | **Comment** |
|:-------------------------------|:---------------------|:----------------|:------------|
| **A**                          | _Initial development work_ | Documentation and testing |             |
| **A.1**                        | _Preparations for main work_ | **Finished**    | Making decisions about implementation |
| **A.2**                        | _Improvements on main development trunk_<br>required for integration of OGP with GeoGebra<table><thead><th> <b>Finished</b> </th><th>             </th></thead><tbody>
<tr><td> <b>A.3</b>                     </td><td> <i>XML parsing of geometry constructions</i> </td><td> <b>Finished</b> </td><td> Only transformations are not done </td></tr>
<tr><td> <b>A.4</b>                     </td><td> <i>Parsing of theorem statements</i> </td><td> <b>Finished</b> </td><td> Only a couple of basic statements <br>are supported in initial stage </td></tr>
<tr><td> <b>A.5</b>                     </td><td> <i>Replacement of logger</i> </td><td> <b>Finished</b> </td><td>             </td></tr>
<tr><td> <b>A.6</b>                     </td><td> <i>Implementation of output format and API</i> </td><td> <b>Finished</b> </td><td>             </td></tr>
<tr><td> <b>A.7</b>                     </td><td> <i>Preparing documentation and examples</i> </td><td> Ongoing         </td><td>             </td></tr>
<tr><td> <b>A.8</b>                     </td><td> <i>Testing and bug fixing</i> </td><td> Ongoing         </td><td>             </td></tr>
<tr><td> ...                            </td><td> ...                  </td><td> ...             </td><td>             </td></tr>
<tr><td>                                </td><td>                      </td><td>                 </td><td>             </td></tr>
<tr><td> <b>B</b>                       </td><td> <i>Improving and completing initial implementation</i> </td><td> Not Yet Started </td><td>             </td></tr>
<tr><td> <b>B.1</b>                     </td><td> <i>Completing tasks from initial implementation</i> </td><td> Not Yet Started </td><td>             </td></tr>
<tr><td> <b>C</b>                       </td><td> <i>Further development work</i> </td><td> Not Yet Started </td><td>             </td></tr>
<tr><td> ...                            </td><td> ...                  </td><td> ...             </td><td>             </td></tr></tbody></table>

The details about each specific stage of work:<br>
<br>
<ol><li><b>A-stage</b> is initial work which should provide basic OGP proving features available in GeoGebra software.<br>
<ul><li><b>A.1</b> - Preparations for main work are discussions and work about:<br>
<ul><li>desired functionality<br>
</li><li>how arguments and results will be transferred between OGP and GeoGebra<br>
</li><li>adjustments of OGP license to match GeoGebra's<br>
</li><li>removing of 3rd party libraries that can't be used in GeoGebra (in order to keep its <code>'.jar'</code> file as small as possible)<br>
</li><li>...<br>
</li></ul></li><li><b>A.2</b> - Sometimes it will be necessary to implement some new feature or functionality on this branch which is generally useful so it basically has to be implemented on main development trunk first. This sub-stage is also for fixing bugs on main development trunk found during work on GeoGebra branch of OGP.<br>
</li><li><b>A.3</b> - XML parsing of geometry constructions:<br>
<ul><li>agreed format of XML that will be sent from GeoGebra to OGP<br>
</li><li>using QDParser for parsing XML for constructions<br>
</li><li>adding support for various geometry constructions from GeoGebra even if they are currently not supported by OGP (which is currently oriented to algebraic provers), like rays, segments, polygons etc.<br>
</li></ul></li><li><b>A.4</b> - Parsing theorem statements:<br>
<ul><li>agreed about format of most important geometry theorem statements<br>
</li></ul></li><li><b>A.5</b> - Replacement of 3rd party <a href='http://logging.apache.org/log4j/1.2/'>log4j</a> logger with new one used in GeoGebra.<br>
</li><li><b>A.6</b> - Implementation of output format:<br>
<ul><li>what will be sent from OGP to GeoGebra and in what type of format<br>
</li></ul></li></ul></li><li><b>B-stage</b> - Improving and completing initial implementation<br>
<ul><li><b>B.1</b> - Completing tasks from initial implementation:<br>
<ul><li>defining disclaimer and putting notes in documentation about licenses of 3rd party libraries (which are compatible with GNU GPL v3 license) - <a href='https://code.google.com/p/open-geo-prover/issues/detail?id=#8'>issue #8</a>;<br>
</li><li>preparing build scripts (for Windows and Unix) - <a href='https://code.google.com/p/open-geo-prover/issues/detail?id=#18'>issue #18</a>;<br>
</li><li>new input parameters for GeoGebra - destination folder and name of log file, destination folder and name of output report - <a href='https://code.google.com/p/open-geo-prover/issues/detail?id=#20'>issue #20</a>;<br>
</li><li>implementing API to set custom destination and name of log file in OGP settings - <a href='https://code.google.com/p/open-geo-prover/issues/detail?id=#21'>issue #21</a>;<br>
</li><li>improving conversion of input constructions to rename intersection points if they are introduced another time with new label - <a href='https://code.google.com/p/open-geo-prover/issues/detail?id=#23'>issue #23</a>;<br>
</li><li>implementing conversion of constructions for missing geometry transformations (rotations, translations and dialtations) - <a href='https://code.google.com/p/open-geo-prover/issues/detail?id=#15'>issue #15</a>;<br>
</li></ul></li></ul></li><li><b>C-stage</b> - Further development work - To Be Decided</li></ol>

<b>Note:</b>
<blockquote>Every project issue created to track a single specific piece of work has to have a mark of at least stage of work it is related to, but better if more detailed sub-stage label is provided. For more information about issues of this project visit <a href='important_issues.md'>Issues</a> page.