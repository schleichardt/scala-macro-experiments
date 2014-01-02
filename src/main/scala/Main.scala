package experiments

import experiments.info.{CalledMethodInfo, ParameterInfo}
import scala.annotation.StaticAnnotation
import scala.language.existentials
import scala.reflect.macros.Universe

object Main extends App {
  println("running Scala macro experiments")
}

case class Person(firstName: String, lastName: String)
trait X {
  def z: Int
}

package info {

/**
 *
 * @param name the name of the called method
 * @param params a sequence containing the filled parameters
 */
  case class CalledMethodInfo(name: String, params: Seq[ParameterInfo])

  case class ParameterInfo(name: String, tpe: String)
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



  def enclosingMethodImpl(c: scala.reflect.macros.Context): c.Expr[CalledMethodInfo] = {
    import c.universe._

    //inspired from https://gist.github.com/akshaal/3388753
    def foldIntoListExpr[T](exprs : Iterable[c.Expr[T]])(implicit evidence: c.WeakTypeTag[T]) : c.Expr[List[T]] =
      exprs.foldLeft(reify { Nil : List[T] }) {
        (accumExpr, expr) =>
          reify { expr.splice :: accumExpr.splice }
      }

    val method: Tree = c.enclosingMethod
    val methodName = c.literal(c.enclosingMethod.symbol.fullName)


    /*
    c.enclosingMethod
    def anMethod(param1: String, param2: Int) = Other.enclosingMethod

    c.macroApplication
    PlayGround.Other.enclosingMethod //class scala.reflect.internal.Trees$Select
     */

    val parameters = foldIntoListExpr(method.children.collect {
      case v @ ValDef(modifiers, name, tpt, rhs) =>
        reify{
          ParameterInfo(c.literal(name.decoded).splice, "pending")
        }
    })
    reify {
      CalledMethodInfo(methodName.splice, parameters.splice)
    }
  }

  def contextMirror1(c: scala.reflect.macros.Context): c.Expr[Boolean] = {
    import c.universe._
    val clazz: ClassSymbol = c.mirror.staticClass("scala.collection.immutable.List")
    val scalaListIsClass: Boolean = clazz.isClass
    c.literal(scalaListIsClass)
  }

  def getCompanionFooImpl[T](c: scala.reflect.macros.Context)(param: c.Expr[T])(implicit evidence: c.WeakTypeTag[T]): c.Expr[String] = {
    import c.universe._
    val companion = c.mirror.staticClass(param.actualType.toString).companionSymbol
    val com = companion.fullName
    val tree = c.parse(s"$com.foo")
    c.Expr[String](tree)
  }

  def accessSingletonObjectImpl(c: scala.reflect.macros.Context): c.Expr[String] = {
    import c.universe._
    val singletonObject = c.mirror.staticModule("experiments.WithNoClassOrTrait")
    val tree = c.parse(s"${singletonObject.fullName}.bar")
    c.Expr[String](tree)
  }
}

