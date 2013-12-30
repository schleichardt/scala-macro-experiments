package experiments

import org.scalatest._
import Matchers._
import java.io.ByteArrayOutputStream
import java.io.InputStream


class SetSuite extends FunSuite {
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

  object Other {
    def tree1 = macro MacroImpls.tree1Impl
    def parse = macro MacroImpls.parseImpl
  }
  
  object TypeCreator {
    def makeInstance: X = macro MacroImpls.makeInstanceImpl
  }
}