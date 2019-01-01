package io.eddumelendez.reactorkotlin

import org.junit.Test
import reactor.core.publisher.Flux
import reactor.test.test
import java.time.Duration

class Part01Flux {

    @Test
    fun empty() {
        val flux = emptyFlux()

        flux.test()
                .verifyComplete()
    }

    // Return an empty Flux
    fun emptyFlux(): Flux<String> = Flux.empty()

    @Test
    fun fromValues() {
        val flux = fooBarFluxFromValue()

        flux.test()
                .expectNext("foo", "bar")
                .verifyComplete()
    }

    // Return a Flux that contains 2 values "foo" and "bar" without using an array or a collection
    fun fooBarFluxFromValue(): Flux<String> = Flux.just("foo", "bar")

    @Test
    fun fromList() {
        val flux = fooBarFluxFromList()

        flux.test()
                .expectNext("foo", "bar")
                .verifyComplete()
    }

    // Create a Flux from a List that contains 2 values "foo" and "bar"
    fun fooBarFluxFromList(): Flux<String> = Flux.fromIterable(listOf("foo", "bar"))

    @Test
    fun error() {
        val flux = errorFlux()

        flux.test()
                .verifyError(IllegalStateException::class.java)
    }

    // Create a Flux that emits an IllegalStateException
    fun errorFlux(): Flux<String> = Flux.error(IllegalStateException())

    @Test
    fun countEach100ms() {
        val flux = counter()

        flux.test()
                .expectNext(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)
                .verifyComplete()
    }

    // Create a Flux that emits increasing values from 0 to 9 each 100ms
    fun counter(): Flux<Long> = Flux.interval(Duration.ofMillis(100)).take(10)

}
