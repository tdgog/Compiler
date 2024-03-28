package io.github.tdgog.compiler.codeanalysis.logging;

public final class ClassNameConverter {

    private ClassNameConverter() {}

    public static String toFriendlyName(Class<?> clazz) {
        if (clazz == Boolean.class) return "boolean";
        else if (clazz == Integer.class) return "int";
        else if (clazz == Double.class) return "double";
        return clazz.getName();
    }

}
