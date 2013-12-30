package experiments

import scala.annotation.StaticAnnotation

object Main extends App {
  println("running Scala macro experiments")
}

case class Person(firstName: String, lastName: String)
trait X {
  def z: Int
}

object MacroImpls {
  import reflect.macros.Context
  def helloImpl(c: Context)(): c.Expr[Unit] = {
    import c.universe._
    reify { println("Hello World!") }
  }

  //to create the correct parameter lists, use a wrong one and the compiler suggest a good one
  def helloWithArgsImpl(c: scala.reflect.macros.Context)(message: c.Expr[String], times: c.Expr[Int]): c.Expr[Unit] = {
    import c.universe._
    reify {
      for (i <- 0 until times.splice) {
        println(message.splice)
      }
    }
  }

  def helloListImpl(c: scala.reflect.macros.Context)(list: c.Expr[List[String]]): c.Expr[Unit] = {
    import c.universe._
    reify {
        println(list.splice.mkString("\n"))
    }
  }

  //inspired by http://meta.plasm.us/posts/2013/07/12/vampire-methods-for-structural-types/
  def makeInstanceImpl(c: scala.reflect.macros.Context): c.Expr[X] = c.universe.reify[X] {
    class Workaround extends X {
      def z: Int = 13
    }
    new Workaround {}
  }

  def tree1Impl(c: scala.reflect.macros.Context): c.Expr[String] = {
    import c.universe._
    val tree = reify{
      val i = 0
      i.toString
    }.tree
    c.Expr(tree)
  }

  def parseImpl(c: scala.reflect.macros.Context): c.Expr[String] = {
    import c.universe._
    val tree:Tree = c.parse("""val p = "itparsed"; p""")
    c.Expr(tree)
  }

  def enclosingClassImpl(c: scala.reflect.macros.Context): c.Expr[String] = {
    import c.universe._
    val className = c.enclosingClass.symbol.fullName
    c.literal(className)
  }
}
