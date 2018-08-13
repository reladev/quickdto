package org.reladev.quickdto.processor;

import java.io.IOException;
import java.io.Writer;

public class IndentWriter {
    private Writer writer;
    private int indent;
    private int listIndent;
    private boolean listStarted = true;
    private String lineSuffix;
    private String indentValue;
    private String lineValue = "\n";

    public IndentWriter(Writer writer, String indentValue) {
        this.writer = writer;
        this.indentValue = indentValue;
    }

    /**
     * Sets the ident that should be prepended to each line.
     *
     * @param indent the number of indent values to prepend.
     * @return 'this' to allow for chaining.
     */
    public IndentWriter indent(int indent) {
        this.indent = indent;
        return this;
    }

    /**
     * Increase the indent amount by one.
     * @return 'this' to allow for chaining.
     */
    public IndentWriter indent() {
        indent++;
        return this;
    }

    /**
     * Decrease the indent amount by one.
     * @return 'this' to allow for chaining.
     */
    public IndentWriter unindent() {
        indent--;
        return this;
    }

    /**
     * Creates a blank line by adding '\n'
     * @return 'this' to allow for chaining.
     * @throws IOException If any I/O error occurs
     */
    public IndentWriter newLine() throws IOException {
        writer.append(lineValue);
        return this;
    }

    /**
     * Completes the previous line by adding '\n' and adds the number of indents specified.
     *
     * @param csq The text that should be added after the indents.
     * @return 'this' to allow for chaining.
     * @throws IOException If any I/O error occurs
     */
    public IndentWriter line(CharSequence csq) throws IOException {
        return line(0, csq);
    }

    /**
     * Same as {@link #line(CharSequence)}, but allows a one time addition of more indents.
     *
     * @param indent the number of indent values to prepend only for this line.
     * @param csq    The text that should be added after the indents.
     * @return 'this' to allow for chaining.
     * @throws IOException If any I/O error occurs
     */
    public IndentWriter line(int indent, CharSequence csq) throws IOException {
        if (listStarted && lineSuffix != null) {
            writer.append(lineSuffix);
        }
        listStarted = true;
        writer.append(lineValue);

        int totalIndent = this.indent + indent;
        for (int i = 0; i < totalIndent; i++) {
            writer.append(indentValue);
        }
        writer.append(csq);
        return this;
    }

    /**
     * Appends the text without adding any indent values.
     *
     * @param csq Text to append
     * @return 'this' to allow for chaining.
     * @throws IOException If any I/O error occurs
     */
    public IndentWriter append(CharSequence csq) throws IOException {
        writer.append(csq);
        return this;
    }

    public IndentWriter startLineList(String lineSuffix) {
        return startLineList(0, lineSuffix);
    }

    public IndentWriter startLineList(int indent, String lineSuffix) {
        listIndent = indent;
        this.indent += listIndent;
        this.lineSuffix = lineSuffix;
        listStarted = false;

        return this;
    }

    public IndentWriter endLineList() {
        lineSuffix = null;
        indent -= listIndent;
        listIndent = 0;
        return this;
    }

    /**
     * @see Writer#close()
     * @throws IOException If any I/O error occurs
     */
    public void flush() throws IOException {
        writer.flush();
    }

    /**
     * @see Writer#flush()
     * @throws IOException If any I/O error occurs
     */
    public void close() throws IOException {
        writer.close();
    }
}
