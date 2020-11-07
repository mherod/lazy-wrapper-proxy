import org.junit.Test
import java.lang.Thread.sleep

class ProxyGeneratorKtTest {

    interface PretendInterface {
        fun helloWorld()
    }

    object PretendInterfaceImpl : PretendInterface {
        override fun helloWorld() {
            println("hello world")
        }
    }

    interface PretendDataInterface {
        var elephant: String
    }

    data class RealPretendData(override var elephant: String) : PretendDataInterface

    @Test
    fun doProxyWrapping() {
        val proxy by lazyProxyLazy<PretendInterface> {
            PretendInterfaceImpl
        }
        sleep(1_000)
        println(proxy.toString())
        proxy.helloWorld()
    }

    @Test
    fun doProxyWrapping_Data() {
        val proxy by lazyProxyLazy<PretendDataInterface> {
            RealPretendData("hello")
        }
        sleep(1_000)
        println(proxy.toString())
    }
}
