package io.eddumelendez.reactorkotlin

import io.eddumelendez.reactorkotlin.domain.User
import io.eddumelendez.reactorkotlin.repository.ReactiveUserRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import reactor.core.publisher.toMono
import reactor.test.test
import java.util.concurrent.CompletableFuture

class Part09Adapt {

    val repository = ReactiveUserRepository()

    @Test
    fun adaptToFlowable() {
        val flux = repository.findAll()
        val observable = fromFluxToFlowable(flux)
        fromFlowableToFlux(observable).test()
                .expectNext(User.SKYLER, User.JESSE, User.WALTER, User.SAUL)
                .verifyComplete()
    }

    // Adapt Flux to RxJava Flowable
    fun fromFluxToFlowable(flux: Flux<User>): Flowable<User> = Flowable.fromPublisher(flux)

    // Adapt RxJava Flowable to Flux
    fun fromFlowableToFlux(flowable: Flowable<User>): Flux<User> = flowable.toFlux()

    @Test
    fun adaptToObservable() {
        val flux = repository.findAll()
        val observable = fromFluxToObservable(flux)
        fromObservableToFlux(observable).test()
                .expectNext(User.SKYLER, User.JESSE, User.WALTER, User.SAUL)
                .verifyComplete()
    }

    // Adapt Flux to RxJava Observable
    fun fromFluxToObservable(flux: Flux<User>): Observable<User> {
        return Flowable.fromPublisher(flux).toObservable()
    }

    // Adapt RxJava Observable to Flux
    fun fromObservableToFlux(observable: Observable<User>): Flux<User> {
        return observable.toFlowable(BackpressureStrategy.LATEST).toFlux()
    }

    @Test
    fun adaptToSingle() {
        val mono = repository.findFirst()
        val single = fromMonoToSingle(mono)
        fromSingleToMono(single).test()
                .expectNext(User.SKYLER)
                .verifyComplete()
    }

    // Adapt Mono to RxJava Single
    fun fromMonoToSingle(mono: Mono<User>): Single<User> {
        return Single.fromPublisher(mono)
    }

    // Adapt RxJava Single to Mono
    fun fromSingleToMono(single: Single<User>): Mono<User> {
        return Mono.from(single.toFlowable())
    }

    @Test
    fun adaptToCompletableFuture() {
        val mono = repository.findFirst()
        val future = fromMonoToCompletableFuture(mono)
        fromCompletableFutureToMono(future).test()
                .expectNext(User.SKYLER)
                .verifyComplete()
    }

    // Adapt Mono to Java 8+ CompletableFuture
    fun fromMonoToCompletableFuture(mono: Mono<User>): CompletableFuture<User> {
        return mono.toFuture()
    }

    // Adapt Java 8+ CompletableFuture to Mono
    fun fromCompletableFutureToMono(future: CompletableFuture<User>): Mono<User> {
        return future.toMono()
    }

}
