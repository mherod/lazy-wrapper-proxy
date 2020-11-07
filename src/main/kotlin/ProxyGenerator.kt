import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.properties.Delegates

inline fun <reified T : Any> lazyProxyLazy(noinline delegate: () -> T): Lazy<T> {
    return lazy { proxyLazy(delegate) }
}

inline fun <reified T : Any> proxyLazy(noinline delegate: () -> T): T {
    require(T::class.java.isInterface)
    return runCatching {
        Proxy.newProxyInstance(
            T::class.java.classLoader,
            arrayOf(T::class.java),
            LazyInvocationHandler(delegate::invoke)
        )
    }.mapCatching {
        it as T
    }.recoverCatching {
        println("Couldn't create lazy proxy wrapper: $it")
        delegate.invoke()
    }.getOrThrow()
}

class LazyInvocationHandler<T : Any>(private val delegate: () -> T) : InvocationHandler {

    private val proxyInitTime: Long = System.currentTimeMillis()
    private var implInitTime: Long by Delegates.notNull()

    private val lazyDelegate: T by lazy {
        implInitTime = System.currentTimeMillis()
        delegate.invoke().also {
            println("Impl $it delayed by ${implInitTime - proxyInitTime}ms")
        }
    }

    override fun invoke(proxy: Any, method: Method?, args: Array<out Any>?): Any? {
        return method?.invoke(lazyDelegate, *args.orEmpty())
    }
}
