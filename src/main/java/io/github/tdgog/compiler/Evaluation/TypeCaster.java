package io.github.tdgog.compiler.Evaluation;

public final class TypeCaster {

    public static boolean toBoolean(Object object) {
        if (object instanceof Integer i)
            return i != 0;
        if (object instanceof Double d)
            return d != 0;
        if (object instanceof Boolean b)
            return b;
        throw new ClassCastException(object.getClass() + " cannot be cast to " + Boolean.class);
    }

    public static double toDouble(Object object) {
        if (object instanceof Integer i)
            return i + 0f;
        if (object instanceof Double d)
            return d;
        throw new ClassCastException(object.getClass() + " cannot be cast to " + Double.class);
    }

}
