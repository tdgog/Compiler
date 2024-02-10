package io.github.tdgog.compiler.Text;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SourceText {

    @Getter
    private final List<TextLine> lines;
    private final String text;

    public SourceText(String text) {
        this.text = text;
        this.lines = parseLines(this, text);
    }

    public char charAt(int index) {
        return text.charAt(index);
    }

    public int length() {
        return text.length();
    }

    public int getLineIndex(int position) {
        int lower = 0;
        int upper = lines.size() - 1;

        while (lower <= upper) {
            int index = lower + (upper - lower) / 2;
            int start = lines.get(index).start();

            if (position == start)
                return index;

            if (start > position)
                upper = index - 1;
            else
                lower = index + 1;
        }

        return lower - 1;
    }

    private static List<TextLine> parseLines(SourceText sourceText, String text) {
        List<TextLine> result = new ArrayList<>();

        int position = 0;
        int lineStart = 0;
        while (position < text.length()) {
            int lineBreakWidth = getLineBreakWidth(text, position);

            if (lineBreakWidth == 0)
                position++;
            else {
                addLine(result, sourceText, position, lineStart, lineBreakWidth);
                position += lineBreakWidth;
                lineStart = position;
            }
        }

        if (position > lineStart)
            addLine(result, sourceText, position, lineStart, 0);

        return Collections.unmodifiableList(result);
    }

    private static void addLine(List<TextLine> result, SourceText sourceText, int position, int lineStart, int lineBreakWidth) {
        int lineLength = position - lineStart;
        int lengthWithLineBreak = lineLength + lineBreakWidth;
        result.add(new TextLine(sourceText, lineStart, lineLength, lengthWithLineBreak));
    }

    private static int getLineBreakWidth(String text, int position) {
        char current = text.charAt(position);
        char next = position + 1 >= text.length() ? '\0' : text.charAt(position + 1);

        if (current == '\r' && next == '\n')
            return 2;

        if (current == '\r' || current == '\n')
            return 1;

        return 0;
    }

    @Override
    public String toString() {
        return text;
    }

    public String substring(int start, int end) {
        return text.substring(start, end);
    }

    public String substring(TextSpan span) {
        return text.substring(span.start(), span.end());
    }

}
