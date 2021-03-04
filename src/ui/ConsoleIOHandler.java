package ui;

import java.io.*;
import java.util.logging.Logger;

public class ConsoleIOHandler implements IIOHandler {
    private static final Logger log = Logger.getLogger(ConsoleIOHandler.class.getName());
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    @Override
    public String read() {
        try {
            return br.readLine();
        } catch (final IOException e) {
            final String logWithException = "Could not read the entered value because of " + e.getMessage();
            this.write(logWithException);
            log.warning(logWithException);
            return "";
        }
    }

    @Override
    public void write(final String value) {
        try {
            bw.write(value, 0, value.length());
            bw.newLine();
            bw.flush();
        } catch (final IOException e) {
            log.warning("Could not write the entered value because of " + e.getMessage());
        }
    }
}
