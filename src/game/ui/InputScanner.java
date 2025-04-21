package game.ui;

import java.io.InputStream;
import java.util.Scanner;

public class InputScanner {
    public Scanner scanner;

    public InputScanner(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
    };

    public void setScanner(InputStream inputStream) {
        this.scanner = new Scanner(inputStream);
    };

    public Scanner getScanner() {
        return scanner;
    }
}
