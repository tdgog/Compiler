package io.github.tdgog.compiler.Binder.Scope;

import io.github.tdgog.compiler.CodeAnalysis.VariableSymbol;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class BoundScope {

    private final HashMap<String, VariableSymbol> variables = new HashMap<>();
    @Getter
    private final BoundScope parent;

    public VariableSymbol tryLookup(String name) {
        if (variables.containsKey(name))
            return variables.get(name);

        if (parent == null)
            return null;

        return parent.tryLookup(name);
    }

    public boolean tryDeclare(VariableSymbol symbol) {
        if (variables.containsValue(symbol))
            return false;

        variables.put(symbol.name(), symbol);
        return true;
    }

    public List<VariableSymbol> getDeclaredVariables() {
        return variables.values().stream().toList();
    }

}
