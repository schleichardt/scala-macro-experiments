import java.lang.String
import org.scalatest._
import Matchers._
import java.io.ByteArrayOutputStream
import java.io.InputStream


class SetSuite extends FunSuite {
  import PlayGround._

  test("hello world") {
    val fakeOut = new ByteArrayOutputStream
    Console.withOut(fakeOut){
      new Hello().hello()
    }
    new String(fakeOut.toByteArray) should be("Hello World!\n")
  }

  test("using parameters"){
    val message = "NaN"
    val fakeOut = new ByteArrayOutputStream
    Console.withOut(fakeOut){
      new Hello().helloWithArgs(message, 3)
    }
    new String(fakeOut.toByteArray) should be("NaN\nNaN\nNaN\n")
  }
}

object PlayGround {
  import language.experimental.macros

  class Hello {
    def hello(): Unit = macro MacroImpls.hello_impl//qualified identifier macro implementation method in a static context
    def helloWithArgs(message: String, times: Int) = macro MacroImpls.helloWithArgs_impl
  }
}