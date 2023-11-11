# Compiler Internals

How the compiler works internally.

## Glossary

Lexer
: Breaks the human-readable source code down into a 
list of tokens.

Parser
: Converts the list of tokens into a computer-readable 
parse tree representing the structure of the code

Binder
: Assigns a data type to every node on the syntax tree.
