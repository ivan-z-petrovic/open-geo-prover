# OGP Development Plans #

## Introduction ##

### Branches and Releases ###

**Main development trunk**

Project maintains main development trunk. All bugs/issues of general type (not branch specific) reported on some specific branch are fixed on development trunk first, then merged to the reported branch. Owners decide whether some fix has to be applied from development trunk to some other branches where bug/issue was not detected/reported.

Branch specific bugs (related to code only from some specific branch) are fixed only on that branch.

All general enhancements/improvements or new features of project are done on development trunk first, then are applied to desired branch. Owners decide whether to apply these improvements to some other branch.
General approach for other branches (that didn't request the improvement) should be to not change them, but to upgrade them with latest development trunk at certain point.

**New branches**

All specific enhancements/improvements (required for specific client i.e. geometry system) are managed this way: a new branch of project is created first, then improvements are implemented only on new branch.

Since development on main trunk will be done in stages according to predefined plan, all new non-planned features can be implemented only on a newly created branch first and then later merged up to the main development branch at some point, when it is safe. New branch will be created from main development trunk. Also, before merging a new feature from that new branch up to the main trunk, that branch has to be upgraded first with new revisions made on the main trunk. Same rules apply to all other already created branches - their development is done according to predefined plan so all new non-planned features are implemented to new branch created from the old one client specific branch.


## Current State of Project ##

### Current work ###
_The current active **development trunk** is in state of completing examples and tests for the initial work done, and is also for implementation of solution for support for conic sections._

**The current active development work is for integration of this prover into GeoGebra geometry system:**
  * [GeoGebra home page](http://www.geogebra.org/cms/)
  * [GeoGebra Wiki page](http://www.geogebra.org/en/wiki/index.php/English)
  * [GeoGebra Developer Wiki page](http://www.geogebra.org/trac/wiki)

As a part of this work, a new prover API suitable for GeoGebra system will be implemented. It will accept input from GeoGebra, convert it to internal prover representation and return the result in GeoGebra desired format.

On the other side GeoGebra will provide representation of geometry theorems enhancing its _Construction Protocol_ object to contain statement together with constructions. Also it will provide representation of Non-Degenerative Conditions (abbreviated NDG Conditions) and necessary GUI implementation for prover sub-screen. (See more details on [GeoGebra Developer Wiki page](http://www.geogebra.org/trac/wiki)).

For more info see [details section](development_plans#Details_about_development_plans.md).

### Other branches (beside main development trunk) ###

  1. Branch dedicated for Integration of OGP with GeoGebra.
  1. ...


## Plans for future work ##

Here are listed some ideas and plans for future work on this project.

  1. Generating Certificate for formal verification of Wu's method.
  1. Implementation of Gröbner basis method.
  1. Supporting new generic XML format for geometry theorems.
  1. Modifications in geometry constructions - deriving equations for geometry objects from given elements, and then producing polynomials by using these equations; Analysis and Work on conic sections (more details will be provided; this work will be done as new feature to existing development trunk).
  1. Analysis and Work on theorems from hyperbolic geometry (this will be tracked on separate branch).
  1. Implementation of parallel polynomial multiplying (new feature to head baseline).
  1. ...

## Details about development plans ##
Visit page [Details about all development plans](development_plans_details.md).