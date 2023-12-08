file://<WORKSPACE>/src/test/scala/startup/unit_testing/StartUpSuite.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

action parameters:
offset: 1019
uri: file://<WORKSPACE>/src/test/scala/startup/unit_testing/StartUpSuite.scala
text:
```scala
package startup.unit_testing

import munit.FunSuite
import startup.ast.ArithmeticOperation
import startup.ast.ArithmeticOperation.given
import startup.ast.myStart
class StartUpSuite extends munit.FunSuite:

  test("add result in correct addition") {
    val obtained: Int = myStart(0).add(1, 1)
    val expected: Int = 2
    assertEquals(obtained, expected)
  }
  
  test("add operation results in correct addition") {
    val obtained: Int = ArithmeticOperation.add(1, 1)
    val expected: Int = 2
    assertEquals(obtained, expected)
  }

end StartUpSuite

startup.ast.Num(2) 

val IntExpressionNum: Expression[Int] = Num(3)
val IntExpressionMix: Expression[Int] = Mult(Plus(Num(3), Num(4)), Num(5))
val IntExpressionPlus: Expression[Int] = Plus(Num(3), Num(4))
val DoubleExpressionNum: Expression[Double] = Num(3.6)
val DoubleExpressionMix: Expression[Double] = Mult(Plus(Num(3.8), Num(4.5)), Num(5.9))
val DoubleExpressionPlus: Expression[Double] = Plus(Num(3.7), Num(4.6))
val DfExpressionNum: Expression[Dataset[]@@] = Num(3.6)
val DfExpressionMix: Expression[Double] = Mult(Plus(Num(3.8), Num(4.5)), Num(5.9))
val DfExpressionPlus: Expression[Double] = Plus(Num(3.7), Num(4.6))
```



#### Error stacktrace:

```
dotty.tools.dotc.core.SymDenotations$NoDenotation$.owner(SymDenotations.scala:2582)
	scala.meta.internal.pc.SignatureHelpProvider$.isValid(SignatureHelpProvider.scala:83)
	scala.meta.internal.pc.SignatureHelpProvider$.notCurrentApply(SignatureHelpProvider.scala:94)
	scala.meta.internal.pc.SignatureHelpProvider$.$anonfun$1(SignatureHelpProvider.scala:48)
	scala.collection.StrictOptimizedLinearSeqOps.loop$3(LinearSeq.scala:280)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile(LinearSeq.scala:282)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile$(LinearSeq.scala:278)
	scala.collection.immutable.List.dropWhile(List.scala:79)
	scala.meta.internal.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:48)
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:375)
```
#### Short summary: 

java.lang.AssertionError: NoDenotation.owner