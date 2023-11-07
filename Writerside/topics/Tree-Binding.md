# Tree Binding

The binder's job is to take a parse tree and attach type information to each node.
This type information can then be used by the type checker to ensure static typing is being followed.

## Why use a binder?

Many languages don't check typing at compile time and therefore the parse tree can be fed straight
into the emitter to produce the compiled language. Xylo, on the other hand, has no way of checking
types at runtime so must enforce these types at compile time. Enforcing types at compile time
requires a binder so the compiler knows what type each piece of data should be.

## How does the binder work?

The binder walks the parse tree. At each node on the tree, it will work out what type of expression
is represented by this node. It will then look at the node's children based on the type of expression
the node is, and finally it will bind the node with the types and values.
