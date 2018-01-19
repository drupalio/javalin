/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin

import io.javalin.core.util.RouteOverview
import io.javalin.util.HandlerImplementation


    private var app: Javalin? = null

    private class ImplementingClass : Handler {
        override fun handle(context: Context) {}
    }

    val lambdaField = Handler { ctx -> }

    fun main(args: Array<String>) {
        val app = Javalin.start(7070)
        app.get("/1", lambdaField)
        app.get("/2", ImplementingClass())
        app.get("/3", HandlerImplementation())
        app.get("/4") { methodReference(it) }
        app.get("/5") { ctx -> ctx.result("") }
        app.get("/") { ctx -> ctx.html(RouteOverview.createHtmlOverview(app)) }
    }

    private fun methodReference(context: Context) {
    }


