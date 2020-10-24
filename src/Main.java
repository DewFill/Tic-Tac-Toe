package tictactoe;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    static char[][] createInputArray(){
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter cells: ");
        String string = sc.nextLine();
        char[][] ch = new char[3][3];
        int charStringAt = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ch[i][j] = string.charAt(charStringAt);
                charStringAt++;
            }
        }
        return ch;
    }

    static void printArrayField(char[][] threeByThreeCharArray){
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(threeByThreeCharArray[i][j] + " ");
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("---------");
    }

    /*
        +++TODO "Impossible" - when the field has three X in a row as well as three O in a row.                                  3
        +++TODO "X wins" - when the field has three X in a row;
        +++TODO "O wins" - when the field has three O in a row;
        +++TODO "Draw" - when no side has a three in a row and the field has no empty cells;                                     2
        +++TODO "Game not finished" - when no side has a three in a row but the field has empty cells;
        +++TODO Or the field has a lot more X's than O's or vice versa (if the difference is 2 or more, should be 1 or 0).       1
     */

    public static void main(String[] args) {
        char[][] array = {
                {' ', ' ', ' '},
                {' ', ' ', ' '},
                {' ', ' ', ' '}
        };
        printArrayField(array);

        Scanner sc = new Scanner(System.in);

        while(CheckGameState.start(array)){
            CheckCoordinates.start(sc, array);
        }
    }

    public static class CheckCoordinates{
        static int numberOfMovesCheck = 0;
        static int[] coords = new int[0];

        public static void start(Scanner sc, char[][] array){
            System.out.print("Enter the coordinates: ");
            String coordString = sc.nextLine();
            int[] arr = new int[0];
            try {
                arr = Arrays.stream(coordString.split(" "))
                        .map(String::trim).mapToInt(Integer::parseInt).toArray();
            } catch (NumberFormatException e) {
                System.out.println("You should enter numbers!");
                start(sc, array);
            }
            if (Arrays.equals(arr, new int[]{1, 1})) {
                coords = new int[]{2, 0};
            } else if (Arrays.equals(arr, new int[]{1, 2})) {
                coords = new int[]{1, 0};
            } else if (Arrays.equals(arr, new int[]{1, 3})) {
                coords = new int[]{0, 0};
            } else if (Arrays.equals(arr, new int[]{2, 1})) {
                coords = new int[]{2, 1};
            } else if (Arrays.equals(arr, new int[]{2, 2})) {
                coords = new int[]{1, 1};
            } else if (Arrays.equals(arr, new int[]{2, 3})) {
                coords = new int[]{0, 1};
            } else if (Arrays.equals(arr, new int[]{3, 1})) {
                coords = new int[]{2, 2};
            } else if (Arrays.equals(arr, new int[]{3, 2})) {
                coords = new int[]{1, 2};
            } else if (Arrays.equals(arr, new int[]{3, 3})) {
                coords = new int[]{0, 2};
            } else {
                System.out.println("Coordinates should be from 1 to 3!");
                start(sc, array);
            }

            switch (array[coords[0]][coords[1]]) {
                case ' ':
                case '_':
                    if (numberOfMovesCheck % 2 == 0){
                        array[coords[0]][coords[1]] = 'X';
                    } else {
                        array[coords[0]][coords[1]] = 'O';
                    }
                    printArrayField(array);
                    numberOfMovesCheck++;
                    break;
                case 'X':
                case 'O':
                    System.out.println("This cell is occupied! Choose another one!");
                    start(sc, array);
                    break;
            }
        }
    }
    public static class CheckGameState {
        public static boolean start(char[][] array){
            boolean impossibleMove = CheckGameState.impossibleMoveCheck(array);
            boolean xWins = CheckGameState.winXHorizontalCheck(array) || CheckGameState.winXVerticalCheck(array) || CheckGameState.winXDiagonalToTheLeftCheck(array) || CheckGameState.winXDiagonalToTheRightCheck(array);
            boolean oWins = CheckGameState.winOHorizontalCheck(array) || CheckGameState.winOVerticalCheck(array) || CheckGameState.winODiagonalToTheLeftCheck(array) || CheckGameState.winODiagonalToTheRightCheck(array);
            boolean draw = CheckGameState.DrawCheck(array) && !oWins && !xWins; //использовать только после проверки всех выигрышей
            boolean impossibleMoreThanOneWin = CheckGameState.moreThanOneWinOnTheField > 1; //использовать только после проверки всех выигрышей
            if (impossibleMove || impossibleMoreThanOneWin){
                System.out.println("Draw");
                return false;
            } else if (!xWins && !oWins && draw){
                System.out.println("Draw");
                return false;
            } else if(xWins){
                System.out.println("X wins");
                return false;
            } else if(oWins){
                System.out.println("O wins");
                return false;
            } else if(!xWins && !oWins && CheckGameState.gameNotFinishedCheck(array)){
                //System.out.println("Game not finished");
                return true;
            }
            return true;
        }
        public static int moreThanOneWinOnTheField = 0;
        static boolean impossibleMoveCheck(char[][] threeByThreeCharArray){ //если количество X от O или O от X отличается больше чем на один
            int x = 0;
            int o = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    switch (threeByThreeCharArray[i][j]){
                        case ('X'):
                            x++;
                            break;
                        case ('O'):
                            o++;
                            break;
                        default:
                            break;
                    }
                }
            }
            if(x - o > 1 || o - x > 1){
                //System.out.println("Невозможная раскладка поля");
                return true;
            }
            return false;
        }
        static boolean DrawCheck(char[][] threeByThreeCharArray){ // если нет свободного места на поле
            int x = 0;
            int o = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    switch (threeByThreeCharArray[i][j]){
                        case ('X'):
                            x++;
                            break;
                        case ('O'):
                            o++;
                            break;
                        default:
                            break;
                    }
                }
            }
            if(x + o == 9){
                //System.out.println("Ничья");
                return true;
            }
            return false;
        }
        static boolean gameNotFinishedCheck(char[][] threeByThreeCharArray){ // если нет свободного места на поле
            int x = 0;
            int o = 0;
            int space = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    switch (threeByThreeCharArray[i][j]){
                        case ('X'):
                            x++;
                            break;
                        case ('O'):
                            o++;
                            break;
                        case (' '):
                        case ('_'):
                            space++;
                            break;
                    }
                }
            }
            if(x + o < 9 && space > 0){
                //System.out.println("Ничья");
                return true;
            }
            return false;
        }
        static boolean winXHorizontalCheck(char[][] threeByThreeCharArray){ //проверяет на выигрыш X по горизонтали
            for (int i = 0; i < 3; i++) {
                char a = threeByThreeCharArray[i][0];
                char b = threeByThreeCharArray[i][1];
                char c = threeByThreeCharArray[i][2];

                if(a == 'X' && a == b && b == c){
                    //System.out.println("X выиграл по горизонтали");
                    moreThanOneWinOnTheField++;
                    return true;
                }
            }
            return false;
        }
        static boolean winXVerticalCheck(char[][] threeByThreeCharArray){ //проверяет на выигрыш X по вертикали
            for (int i = 0; i < 3; i++) {
                char a = threeByThreeCharArray[0][i];
                char b = threeByThreeCharArray[1][i];
                char c = threeByThreeCharArray[2][i];

                if(a == 'X' && a == b && b == c){
                    //System.out.println("X выиграл по вертикали");
                    moreThanOneWinOnTheField++;
                    return true;
                }
            }
            return false;
        }
        static boolean winXDiagonalToTheRightCheck(char[][] threeByThreeCharArray){ //проверяет на выигрыш X по диагонали вправо
            char a = threeByThreeCharArray[2][0];
            char b = threeByThreeCharArray[1][1];
            char c = threeByThreeCharArray[0][2];

            if(a == 'X' && a == b && b == c){
                //System.out.println("X выиграл по диагонали вправо");
                moreThanOneWinOnTheField++;
                return true;
            }
            return false;
        }
        static boolean winXDiagonalToTheLeftCheck(char[][] threeByThreeCharArray){ //проверяет на выигрыш X по диагонали влево
            char a = threeByThreeCharArray[0][0];
            char b = threeByThreeCharArray[1][1];
            char c = threeByThreeCharArray[2][2];

            if(a == 'X' && a == b && b == c){
                //System.out.println("X выиграл по диагонали влево");
                moreThanOneWinOnTheField++;
                return true;
            }
            return false;
        }

        static boolean winOHorizontalCheck(char[][] threeByThreeCharArray){ //проверяет на выигрыш O по горизонтали
            for (int i = 0; i < 3; i++) {
                char a = threeByThreeCharArray[i][0];
                char b = threeByThreeCharArray[i][1];
                char c = threeByThreeCharArray[i][2];

                if(a == 'O' && a == b && b == c){
                    //System.out.println("O выиграл по горизонтали");
                    moreThanOneWinOnTheField++;
                    return true;
                }
            }
            return false;
        }
        static boolean winOVerticalCheck(char[][] threeByThreeCharArray){ //проверяет на выигрыш O по горизонтали
            for (int i = 0; i < 3; i++) {
                char a = threeByThreeCharArray[0][i];
                char b = threeByThreeCharArray[1][i];
                char c = threeByThreeCharArray[2][i];

                if(a == 'O' && a == b && b == c){
                    //System.out.println("O выиграл по вертикали");
                    moreThanOneWinOnTheField++;
                    return true;
                }
            }
            return false;
        }
        static boolean winODiagonalToTheRightCheck(char[][] threeByThreeCharArray){ //проверяет на выигрыш O по диагонали вправо
            char a = threeByThreeCharArray[2][0];
            char b = threeByThreeCharArray[1][1];
            char c = threeByThreeCharArray[0][2];

            if(a == 'O' && a == b && b == c){
                //System.out.println("O выиграл по диагонали вправо");
                moreThanOneWinOnTheField++;
                return true;
            }
            return false;
        }

        static boolean winODiagonalToTheLeftCheck(char[][] threeByThreeCharArray){ //проверяет на выигрыш O по диагонали влево
            char a = threeByThreeCharArray[0][0];
            char b = threeByThreeCharArray[1][1];
            char c = threeByThreeCharArray[2][2];

            if(a == 'O' && a == b && b == c){
                //System.out.println("O выиграл по диагонали влево");
                moreThanOneWinOnTheField++;
                return true;
            }
            return false;
        }
    }
}

