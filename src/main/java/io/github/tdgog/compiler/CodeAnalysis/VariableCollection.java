package io.github.tdgog.compiler.CodeAnalysis;

import java.util.HashMap;
import java.util.Objects;

public class VariableCollection extends HashMap<VariableSymbol, Object> {

    public boolean containsKey(String key) {
        return get(key) != null;
    }

    public Object get(String key) {
        for (VariableSymbol variableSymbol : keySet())
            if (Objects.equals(variableSymbol.name(), key))
                return get(variableSymbol);
        return null;
    }

    public VariableSymbol getVariableSymbolFromName(String name) {
        for (VariableSymbol variableSymbol : keySet())
            if (Objects.equals(variableSymbol.name(), name))
                return variableSymbol;
        return null;
    }

}
