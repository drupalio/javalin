/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin;

import io.javalin.core.util.RouteOverview;
import io.javalin.util.HandlerImplementation;
import static io.javalin.TestAccessManager.MyRoles.ROLE_ONE;
import static io.javalin.TestAccessManager.MyRoles.ROLE_THREE;
import static io.javalin.TestAccessManager.MyRoles.ROLE_TWO;
import static io.javalin.security.Role.roles;

public class TestRouteOverview {

    public static void main(String[] args) {
        Javalin app = Javalin.start(7000);
        app.get("/1", TestRouteOverview.lambdaField);
        app.get("/2", new ImplementingClass());
        app.get("/3", new HandlerImplementation());
        app.get("/4", TestRouteOverview::methodReference);
        app.get("/5", ctx -> ctx.result(""));
        app.get("/6", ctx -> ctx.result(""), roles(ROLE_ONE));
        app.get("/7", TestRouteOverview.lambdaField, roles(ROLE_ONE, ROLE_THREE));
        app.get("/8", TestRouteOverview::methodReference, roles(ROLE_ONE, ROLE_TWO));
        app.get("/", ctx -> ctx.html(RouteOverview.createHtmlOverview(app)));
    }

    private static Javalin app;

    private static Handler lambdaField = ctx -> {
    };

    private static class ImplementingClass implements Handler {
        @Override
        public void handle(Context context) {
        }
    }

    private static void methodReference(Context context) {
    }

    public static void setup() {
        app = Javalin.start(0);
        app.get("/1", TestRouteOverview.lambdaField);
        app.get("/2", new ImplementingClass());
        app.get("/3", TestRouteOverview::methodReference);
        app.get("/4", ctx -> {
        });
    }

    public static void teardown() {
        System.out.println(RouteOverview.createHtmlOverview(app));
        app.stop();
    }

    public void assertThat_handler_field_works() {
        //assertThat(routeName(HANDLER_FIELD), containsString("TestRouteOverview.lambdaField"));
    }

    public void assertThat_handler_class_works() {
        //assertThat(routeName(HANDLER_CLASS), containsString("ImplementingClass.class"));
    }

    public void assertThat_handler_methodRef_works() {
        //assertThat(routeName(HANDLER_METHOD_REF), containsString("TestRouteOverview::methodReference"));
    }

    public void assertThat_handler_lambda_works() {
        //assertThat(routeName(HANDLER_LAMBDA), containsString("TestRouteOverview???"));
    }

}
