package io.eddumelendez.reactorkotlin

import io.eddumelendez.reactorkotlin.domain.User
import io.eddumelendez.reactorkotlin.repository.ReactiveUserRepository
import org.junit.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import reactor.test.test

class Part04Transform {

    var repository = ReactiveUserRepository()

    @Test
    fun transformMono() {
        val mono = repository.findFirst()

        capitalizeOne(mono).test()
                .expectNext(User("SWHITE", "SKYLER", "WHITE"))
                .verifyComplete()
    }

    fun getSyncCapitalizeUser(u: User)= User(u.username.toUpperCase(), u.firstname?.toUpperCase(), u.lastname?.toUpperCase())

    // Capitalize the user username, firstname and lastname
    fun capitalizeOne(mono: Mono<User>): Mono<User> = mono.map(::getSyncCapitalizeUser)

    @Test
    fun transformFlux() {
        val flux = repository.findAll()

        capitalizeMany(flux).test()
                .expectNext(
                        User("SWHITE", "SKYLER", "WHITE"),
                        User("JPINKMAN", "JESSE", "PINKMAN"),
                        User("WWHITE", "WALTER", "WHITE"),
                        User("SGOODMAN", "SAUL", "GOODMAN"))
                .verifyComplete()
    }

    // Capitalize the users username, firstName and lastName
    fun capitalizeMany(flux: Flux<User>): Flux<User> = flux.map(::getSyncCapitalizeUser)

    @Test
    fun asyncTransformFlux() {
        val flux = repository.findAll()

        asyncCapitalizeMany(flux).test()
                .expectNext(
                        User("SWHITE", "SKYLER", "WHITE"),
                        User("JPINKMAN", "JESSE", "PINKMAN"),
                        User("WWHITE", "WALTER", "WHITE"),
                        User("SGOODMAN", "SAUL", "GOODMAN"))
                .verifyComplete()
    }

    // Capitalize the users username, firstName and lastName using asyncCapitalizeUser()
    fun asyncCapitalizeMany(flux: Flux<User>): Flux<User> = flux.flatMap(::asyncCapitalizeUser)

    fun asyncCapitalizeUser(user: User): Mono<User> {
        return User(user.username.toUpperCase(), user.firstname?.toUpperCase(), user.lastname?.toUpperCase()).toMono()
    }

}
