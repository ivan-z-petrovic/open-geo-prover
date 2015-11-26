# Construction steps for proving Morley's Trisector Theorem #

[Morley's trisector theorem](http://en.wikipedia.org/wiki/Morley%27s_trisector_theorem) is statement about trisectors of triangle's inner angles.

The theorem states:
> _Let ABC be a triangle and u<sub>A</sub> and v<sub>A</sub> are two trisectors of angle <CAB (ordered in that angle such that v<sub>A</sub> is between u<sub>A</sub> and CA), u<sub>B</sub>, v<sub>B</sub> are trisectors of angle <ABC and u<sub>C</sub>,v<sub>C</sub> are trisectors of angle <BCA. Let P be the intersection of u<sub>A</sub> and v<sub>B</sub>, Q is the intersection of u<sub>B</sub> and v<sub>C</sub> and R is the intersection of u<sub>C</sub> and v<sub>A</sub>. Then triangle PQR is an equilateral triangle._

![http://open-geo-prover.googlecode.com/svn/wiki/images/morley.png](http://open-geo-prover.googlecode.com/svn/wiki/images/morley.png)

As known [trisector of angle cannot be constructed by usage of compass and ruler](http://en.wikipedia.org/wiki/Angle_trisection). Therefore, in order to construct all required objects for proving Morley's trisector theorem, the only way to do it by usage of compass and ruler is following:

  1. Select points A, B and P as free points. Let u<sub>A</sub> = AP and v<sub>B</sub> = BP.
  1. Construct line v<sub>A</sub> such that angle <(u<sub>A</sub>, v<sub>A</sub>) = <BAP and line b such that <(v<sub>A</sub>, b) = <BAP.
  1. Construct line u<sub>B</sub> such that angle <(u<sub>B</sub>, v<sub>B</sub>) = <ABP and line a such that <(u<sub>B</sub>, a) = <ABP.
  1. Triangle's vertex C is intersection of lines a and b.
  1. Since sum of inner triangle's angles is equal to 180 degrees, then sum of their thirds is 60 degrees. Angle of 60 degrees can be constructed by compass and ruler. Therefore it is possible to construct the third of inner angle <BCA as a difference of angle of 60 degrees and sum of angles <BAP and <ABP.
  1. Construct line u<sub>C</sub> such that angle <(u<sub>C</sub>, b) = <BCA/3 and line v<sub>C</sub> which is angle bisector of angle <(u<sub>C</sub>, a).
  1. P is intersection of u<sub>A</sub> and v<sub>B</sub>; Q is intersection of u<sub>B</sub> and v<sub>C</sub> and R is intersection of u<sub>C</sub> and v<sub>A</sub>.