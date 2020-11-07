# lazy-wrapper-proxy
Generate lazily generated wrapper interface proxies with lazily instantiated delegate implemations

## Why?
I made this when I figured out that each of the Dagger components in a different project could save construction time if they were constructed only when the exposed classes of the component were injected.

e.g.
```kotlin
val appComponent by lazyProxyLazy<AppComponent> {
    DaggerAppComponent.builder()
        .someModule()
        .build()
}
```

Now the DaggerAppComponent is only constructed for the first call to one of its methods. For the Dagger dependency graph of the project I had build in mind for there are many modules each with their own components and so this measure is able to really reduce class construction and the time to inject the component for the first time at runtime.
