package engine.utils

import assertk.Assert
import assertk.assertions.support.fail

fun Assert<Any?>.actual() = this.name ?: this.actual

fun Assert<String?>.isEqualTo(other: String?, ignoreCase: Boolean = false) =
    step("Check that ${actual()} is equal to $other") {
        if (actual.equals(other, ignoreCase)) return@step
        fail(other, actual)
    }