package github.com.kotlin_news

/**
 * Created by guoshuaijie on 2017/8/15.
 */
class kotlinCodeTest {
    interface Base {
        fun print()
    }

    class BaseImpl(val x: Int) : Base {
        override fun print() {
            print(x)
        }
    }

    class Derived(b: Base) : Base by b

    fun main() {
        val b = BaseImpl(10)
        Derived(b).print() // prints 10
    }
}