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
}

object PlayGround {
  import language.experimental.macros

  class Hello {
    def hello(): Unit = macro MacroImpls.hello_impl
  }
}