package io.github.tdgog.compiler.Text;

public record TextSpan(int start, int length) {

    public static TextSpan fromBounds(int start, int end) {
        return new TextSpan(start, end - start);
    }

    public int end() {
        return start + length;
    }

}
