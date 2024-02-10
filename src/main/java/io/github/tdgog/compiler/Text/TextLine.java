package io.github.tdgog.compiler.Text;

public record TextLine(SourceText text, int start, int length, int lengthWithLineBreak) {

    public int end() {
        return start + length;
    }

    public TextSpan span() {
        return new TextSpan(start, length);
    }

    public TextSpan spanWithLineBreak() {
        return new TextSpan(start, lengthWithLineBreak);
    }

    @Override
    public String toString() {
        return text.substring(span());
    }

}
