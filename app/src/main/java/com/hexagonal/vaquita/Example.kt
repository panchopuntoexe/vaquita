package com.hexagonal.vaquita

import org.parceler.Parcel

@Parcel
class Example {
    var name: String? = null
    var age = 0

    constructor() {}
    constructor(name: String?, age: Int) {
        this.age = age
        this.name = name
    }
}