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
}