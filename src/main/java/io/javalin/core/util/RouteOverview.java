/*
 * Javalin - https://javalin.io
 * Copyright 2017 David Åse
 * Licensed under Apache 2.0: https://github.com/tipsy/javalin/blob/master/LICENSE
 */

package io.javalin.core.util;

import io.javalin.Handler;
import io.javalin.Javalin;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sun.reflect.ConstantPool;
import static java.util.Collections.singletonList;

public class RouteOverview {

    public static String createHtmlOverview(Javalin app) {
        String head = "<meta name='viewport' content='width=device-width, initial-scale=1'>" + "<style>b,thead{font-weight:700}body{font-family:monospace;padding:15px}table{border-collapse:collapse;font-size:14px;border:1px solid #d5d5d5;width:100%;white-space:pre}thead{background:#e9e9e9;border-bottom:1px solid #d5d5d5}tbody tr:hover{background:#f5f5f5}td{padding:6px 15px}b{color:#33D}em{color:#666}</style>";
        String rowTemplate = "<tr><td>%s</td><td>%s</td><td><b>%s</b></td><td>%s</td></tr>";
        List<String> tableContent = new ArrayList<>(singletonList("<thead><tr><td>Method</td><td>Path</td><td>Handler</td><td>Roles</td></tr></thead>"));
        app.routeOverviewEntries.forEach(entry -> tableContent.add(
            String.format(
                rowTemplate,
                entry.getHttpMethod(),
                entry.getPath(),
                getHandlerInfo(entry.getHandler()),
                entry.getRoles() != null ? entry.getRoles().toString() : "-"
            )
        ));
        return head + "<body><h1>All mapped routes</h1><table>" + String.join("", tableContent) + "</table><body>";
    }

    private static String getHandlerInfo(Handler handler) {
        String handlerString = handler.toString();
        String parentClass = handlerString.split("\\$")[0];
        if (handlerString.contains("$$Lambda$")) { // This is a field or method-reference
            Map<Handler, String> handlerNames = getFieldNameMap(parentClass);
            if (handlerNames.containsKey(handler)) {
                return parentClass + "." + handlerNames.get(handler);
            }
            if (getMethodName(handler) != null && !getMethodName(handler).contains("lambda$")) {
                return parentClass + "::" + getMethodName(handler);
            }
            return parentClass + "::??? (anonymous lambda)";
        }
        if (handlerString.contains("@")) { // This is a Class implementing Handler
            return handlerString.substring(0, handlerString.indexOf("@")) + ".class";
        }
        return "Mysterious handler";
    }

    private static Map<Handler, String> getFieldNameMap(String parentClass) {
        Map<Handler, String> fieldNameMap = new HashMap<>();
        try {
            for (Field field : Class.forName(parentClass).getDeclaredFields()) {
                field.setAccessible(true);
                fieldNameMap.put((Handler) field.get(field), field.getName());
            }
        } catch (Exception ignored) { // Nothing really matters.
        }
        return fieldNameMap;
    }

    private static String getMethodName(Handler handler) {
        try {
            Method getConstantPoolMethod = Class.class.getDeclaredMethod("getConstantPool");
            getConstantPoolMethod.setAccessible(true);
            ConstantPool constantPool = (ConstantPool) getConstantPoolMethod.invoke(handler.getClass());
            for (int i = constantPool.getSize() - 1; i > -1; i--) {
                try {
                    return constantPool.getMemberRefInfoAt(i)[1];
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

}
