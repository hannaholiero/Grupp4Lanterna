package org.example;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.Random;

public class Main {



    public static void main(String[] args) throws Exception {
        // Setup
        TerminalSize terminalSize = new TerminalSize(40, 15);
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory().setInitialTerminalSize(terminalSize);
        Terminal terminal = terminalFactory.createTerminal();

        terminal.setCursorVisible(false);
        TextGraphics textGraphics = terminal.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        terminal.flush();

        KeyStroke keyStroke = null;

        String title = "APPLE GAME";
        String toStart = "PRESS E TO START";
        String score = "Score:";
        textGraphics.putString(1, 1, title, SGR.BORDERED);
        textGraphics.putString(1, 5, toStart, SGR.BLINK, SGR.BOLD);

        terminal.flush();

        while (true){
            do {
                keyStroke = terminal.pollInput();

            }
            while (keyStroke == null);

            Character c = keyStroke.getCharacter(); // used Character instead of char because it might be null
            if (c == Character.valueOf('e')) {
                terminal.clearScreen();
                terminal.flush();
                break;
            }
        }

        // "Static" things

        final char apple = '❦';
//        int x = 10;
//        int y = 10;

        // Lets go

        Random r = new Random();
//        Random randomchar = new Random();
        Position applePosition = new Position(r.nextInt(50), r.nextInt(5));
        terminal.setCursorPosition(applePosition.col, applePosition.row);
        terminal.putCharacter(apple);

        terminal.flush();

        String playerStart = "►";

        Position playerposition = new Position(10, 10);
        textGraphics.putString(10, 10, playerStart, SGR.BOLD);
        textGraphics.putString(1,14,score, SGR.ITALIC);




        KeyStroke latestKeyStroke = null;

        int points = 1;
        int sum = 0;
        boolean continueReadingInput = true;
        while (continueReadingInput) {

            int index = 0;
            keyStroke = null;

            do {
                index++;
                if (index % 40 == 0) {
                    if (latestKeyStroke != null) {

                        if (applePosition.col == playerposition.col && applePosition.row == playerposition.row) {
//                            terminal.close();
//                            applePosition = new Position(r.nextInt(35), r.nextInt(12)); // Apple changes position
                            applePosition = new Position(r.nextInt(35),13);
                            terminal.setCursorPosition(applePosition.col, applePosition.row);
                            terminal.putCharacter(apple);
                            textGraphics.putString(1, 14, score, SGR.ITALIC);
                            textGraphics.putString(8, 14, String.valueOf(points++), SGR.UNDERLINE);
//                          continueReadingInput = false;

//                            textGraphics.putString(5, 5, String.valueOf(sum), SGR.BOLD);

                        }
                        handlePlayer(playerposition, latestKeyStroke, terminal);
                    }
                }

                Thread.sleep(1); // might throw InterruptedException
                keyStroke = terminal.pollInput();


            } while (keyStroke == null);
            latestKeyStroke = keyStroke;

        }
    }
    private static void handlePlayer (Position playerPosition, KeyStroke keyStroke, Terminal terminal) throws Exception {
        // Handle player
        String delete = " ";

        String playerhead = "▣";

        Position oldPlayerPosition = new Position(playerPosition.col, playerPosition.row);

        TextGraphics textGraphics = terminal.newTextGraphics();
        textGraphics.setForegroundColor(TextColor.ANSI.MAGENTA);
        textGraphics.setBackgroundColor(TextColor.ANSI.BLACK);

        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;

        switch(keyStroke.getKeyType()){
            case ArrowUp :
                playerPosition.row--;
                up = true;
                playerhead = "▲";
                break;
            case ArrowDown :
                playerhead = "▼";
                playerPosition.row++;
                break;
            case ArrowLeft :
                playerPosition.col--;
                playerhead = "◄";
                break;
            case ArrowRight :
                playerPosition.col++;
                playerhead = "►";
                break;

        }

        textGraphics.putString(oldPlayerPosition.col, oldPlayerPosition.row, delete, SGR.BOLD);

        textGraphics.putString(playerPosition.col, playerPosition.row, playerhead, SGR.BOLD);

        terminal.flush();
    }
}

