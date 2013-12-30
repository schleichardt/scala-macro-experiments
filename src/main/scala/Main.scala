object Main extends App {
  println("running Scala macro experiments")
}

object MacroImpls {
  import reflect.macros.Context
  def hello_impl(c: Context)(): c.Expr[Unit] = {
    import c.universe._
    reify { println("Hello World!") }
  }
}