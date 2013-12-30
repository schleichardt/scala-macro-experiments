object Main extends App {
  println("running Scala macro experiments")
}

case class Person(firstName: String, lastName: String)

object MacroImpls {
  import reflect.macros.Context
  def hello_impl(c: Context)(): c.Expr[Unit] = {
    import c.universe._
    reify { println("Hello World!") }
  }

  //to create the correct parameter lists, use a wrong one and the compiler suggest a good one
  def helloWithArgs_impl(c: scala.reflect.macros.Context)(message: c.Expr[String], times: c.Expr[Int]): c.Expr[Unit] = {
    import c.universe._
    reify {
      for (i <- 0 until times.splice) {
        println(message.splice)
      }
    }
  }

  def helloList_impl(c: scala.reflect.macros.Context)(list: c.Expr[List[String]]): c.Expr[Unit] = {
    import c.universe._
    reify {
        println(list.splice.mkString("\n"))
    }
  }
}