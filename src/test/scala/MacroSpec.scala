package experiments

import experiments.info.{ParameterInfo, CalledMethodInfo}
import org.scalatest._
import Matchers._
import java.io.ByteArrayOutputStream
import java.io.InputStream


class MacroSpecs extends FunSuite {
  import PlayGround._

  test("hello world") {
    withFakeOut(new Hello().hello()) should be("Hello World!\n")
  }

  test("using parameters"){
    val message = "NaN"
    withFakeOut(new Hello().helloWithArgs(message, 3)) should be("NaN\nNaN\nNaN\n")
  }

  test("using list parameter"){
    val list = List("A", "B", "C")
    withFakeOut(new Hello().helloList(list)) should be("A\nB\nC\n")
  }

  test("creating class and instance"){
    val myInstance = TypeCreator.makeInstance
    myInstance.z should be (13)
  }

  //thats a very dynamic way but error prone
  test("using parse to create a tree"){
    Other.parse should be ("itparsed")
  }

  //less dynamic than parse, but easier to write and read
  test("using reify to create a tree"){
    Other.tree1 should be ("0")
  }

  test("inspect enclosing class, give the name of the class"){
    Other.enclosingClass should be (classOf[MacroSpecs].getName)
  }

  //TODO include the value, if possible
  test("inspect enclosing method"){
    anMethod("valueParam1", 5) should be (CalledMethodInfo("experiments.MacroSpecs.anMethod",List(ParameterInfo("param2","pending"), ParameterInfo("param1", "pending"))))
  }

  def anMethod(param1: String, param2: Int) = Other.enclosingMethod

  test("Context.mirror to find classes by name and inspect them"){
    //http://www.scala-lang.org/api/current/index.html#scala.reflect.api.Mirror
    ContextMacros.mirror1 should be (true)
  }

  test("get companion for a class"){
    Other.getCompanionFoo(new ClassWithCompanion) should be ("fooAsString")
  }

  test("access directly an object"){
    Other.accessSingletonObject should be ("barAsString")
  }

  test("type and parse to generate statement like http://stackoverflow.com/questions/16898101/scala-macros-generating-type-parameter-calls")(pending)
  test("implement generic macro for non generic methods (see other project ESMacros#applyEventImpl)")(pending)
  test("Context.universe")(pending)
  test("ExprUtils")(pending)
  test("Aliases")(pending)
  test("Attachments")(pending)
  test("Enclosures")(pending)
  test("Evals")(pending)
  test("FrontEnds")(pending)
  test("Infrastructure")(pending)
  test("Names")(pending)
  test("Parsers")(pending)
  test("Reifiers")(pending)
  test("TreeBuilder")(pending)
  test("Typers")(pending)
  test("Universe")(pending)
  test("show")(pending)//show(c.enclosingClass) prints the class as kind of source code
  test("TreeApi")(pending)

  def withFakeOut(block: => Unit): String = {
    val fakeOut = new ByteArrayOutputStream
    Console.withOut(fakeOut){
      block
    }
    new String(fakeOut.toByteArray)
  }
}


object PlayGround {
  import language.experimental.macros

  class Hello {
    def hello(): Unit = macro MacroImpls.helloImpl//qualified identifier macro implementation method in a static context
    def helloWithArgs(message: String, times: Int) = macro MacroImpls.helloWithArgsImpl
    def helloList(list: List[String]) = macro MacroImpls.helloListImpl
  }

  object ContextMacros {
    def mirror1: Boolean = macro MacroImpls.contextMirror1
  }


  object Other {
    def getCompanionFoo[T](param: T): String = macro  MacroImpls.getCompanionFooImpl[T]
    def accessSingletonObject: String = macro  MacroImpls.accessSingletonObjectImpl
    def tree1 = macro MacroImpls.tree1Impl
    def parse = macro MacroImpls.parseImpl
    def enclosingClass = macro MacroImpls.enclosingClassImpl
    def enclosingMethod = macro MacroImpls.enclosingMethodImpl
  }
  
  object TypeCreator {
    def makeInstance: X = macro MacroImpls.makeInstanceImpl
  }
}

class ClassWithCompanion
object ClassWithCompanion {
  def foo = "fooAsString"
}
object WithNoClassOrTrait {
  def bar = "barAsString"
}




