package functional_programming_in_scala.getting_started_with_FP

/**
  * Declares a singleton object, which
  * simultaneously declares a class and its only instance.
  */
object Introducing {

  def abs(n: Int): Int = {
    if(n < 0) -n
    else n
  }

  def factorial(x: Int): Int = {
    @annotation.tailrec
    def go(n: Int, acc: Int): Int = {
      if(n <= 0) acc
      else go(n - 1, n * acc)
    }
    go(x, 1)
  }

  private def formatAbs(x: Int) = {
    val msg = "The absoute value of %d is %d"
    msg.format(x, abs(x))
  }

  private def formatFactorial(n: Int) = {
    val msg = "The factorial of %d is %d."
    msg.format(n, factorial(n))
  }

  /**
    * formatResult is a higher-order-function (HOF)
    * formatAbs and formatFactorial, are almost identical.
    * We can generalize these to a single function, formatResult.
    * Which accepts as an argument a function to apply to its argument
    * @param name
    * @param n
    * @param f: a function from Int to Int
    */
  def formatResult(name: String, n: Int, f: Int => Int) = {
    val msg = "The %s of %d is %d."
    msg.format(name, n, f(n))
  }

  def fibonacci(n: Int): Int = {
    if(n == 0) 0
    else if(n == 1) 1
    else fibonacci(n - 1) + fibonacci(n - 2)
  }

  // 0 and 1 are the first two numbers in the sequence,
  // so we start the accumulators with those.
  // At every iteration, we add the two numbers to get the next one.
  def fib(n: Int): Int = {
    @annotation.tailrec
    def loop(n: Int, prev: Int, cur: Int): Int =
      if (n == 0) prev
      else loop(n - 1, cur, prev + cur)
    loop(n, 0, 1)
  }

  // Solution with a List as accumulator
  def fibonacciTailRec(n: Int): Int = {
    @annotation.tailrec
    def loop(x: Int,acc: List[Int]): Int = {
      if(x == 1) acc.sum
      else {
        loop(x - 1, List(acc.sum, acc.head))
      }
    }
    n match {
      case 0 => 0
      case 1 => 1
      case 2 => 1
      case _ => loop(n - 2,List(1,1))
    }
  }

  /**
    * Monomorphic function to find a String in an array
    */
  def findFirst(ss: Array[String], key: String): Int = {
    @annotation.tailrec
    def loop(n: Int): Int = {
      if(n >= ss.length) -1
      else if(ss(n) == key) n
      else loop(n + 1)
    }

    loop(0)
  }

  /**
    * An example of a polymorphic function
    * We can write findFirst more generally for any type A
    * by accepting a function to use for testing a particular A value
    * @param
    */
    def findFirst[A](as: Array[A], p: A => Boolean): Int = {
      def loop(n: Int): Int = {
        if(n >= as.length) -1
        else if(p(as(n))) n
        else loop(n + 1)
      }

      loop(0)
    }

  /**
    * Check whether an Array[A] is sorted according to a
    * given comparison function
    * @param
    */
    def isSorted[A](as: Array[A], ordered: (A, A) => Boolean): Boolean = {
      @annotation.tailrec
      def loop(arr: Array[A], ord: (A, A) => Boolean): Boolean = {
        val num = arr.length
        num match {
          case 1 => true
          case _  => {
            if(ord(arr.head, arr.tail.head) ) loop(arr.tail, ord)
            else false
            }
        }
      }
      loop(as, ordered)
    }

  /**
    * HOF that takes a function of two arguments and partially applies it.
    * @param a
    * @param f
    * @tparam A
    * @tparam B
    * @tparam C
    * @return
    */
  def partial1[A,B,C](a: A, f: (A,B) => C): B => C =
    (b: B) => f(a,b)

  /**
    * Currying, which converts a function f of two arguments into a function
    * of one argument that partially applies f. Here again there's only one
    * implementation that compiles
    */
    def curry[A,B,C](f: (A, B) => C): A => (B => C) =
      (a: A) => (b: B) => f(a,b)

  /**
    * Implement uncurrry, which reverses the transformation of curry. Note that
    * since => associates to the right, A => (B => C) can be written as A => B => C
    */
    def uncurry[A, B, C](f: A => B => C): (A, B) => C =
      (a, b) => f(a)(b)

  /**
    * Implement the higher-order function that composes two functions
    */
    def compose[A,B,C](f: B => C, g: A => B): A => C =
      (a) => f(g(a))

  val f = (x: Double) => math.Pi / 2 - x
  val cos = f.compose(math.sin)
  val cos_b = f.andThen(math.sin)

  println(cos(f(2.5)))
  println(cos_b(f(2.5)))

  def main(args: Array[String]): Unit = {
    println(factorial(5))
    println(fibonacci(10))
    println(fibonacciTailRec(10))
    println()
    println(formatAbs(-42))
    println(formatFactorial(5))
    println()

    println(formatResult("absolute value", -42, abs))
    println(formatResult("factorial", 7, factorial))
    println(formatResult("fibonacci", 8, fibonacciTailRec))
    println()

    println(findFirst(Array(1,2,3,4,5,6,7,8), (x: Int) => x == 7 ))

    println(isSorted(Array(9,1,2,3,4,5), (x: Int, y: Int) => x < y))
  }

}
