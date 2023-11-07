# Lexing

The lexer's job is to take a string containing the program's human-readable source code and break 
it down into an array of computer-readable tokens.

## What's the point of a lexer?

The parser can't understand the source code itself as it's far too complex and has details which
are necessary for humans but which the computer doesn't care about. The job of the lexer is to output
a series of tokens which are simple enough to be parsed by the parser while still retaining the
key information like the value stored in them.

## Glossary

Token
: A string containing the smallest possible piece of data, such as a single literal, operator, or 
identifier.

