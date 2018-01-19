/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.core.util

import io.javalin.Handler
import io.javalin.core.HandlerType
import io.javalin.security.Role

data class RouteOverviewEntry(val httpMethod: HandlerType, val path: String, val handler: Handler, val roles: List<Role>?)
