package org.iesvdm.sudoku;

//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class SudokuTest {

    @Test
    void failTest() {
        Sudoku sudoku = new Sudoku ();
        sudoku.fillBoardBasedInCluesRandomlySolvable ();
        sudoku.fillBoardBasedInCluesRandomly ();
        sudoku.printBoard ();
    }

    @Test
    void fillBoradRandomlyTest() {
        Sudoku sudoku = new Sudoku ();
        sudoku.fillBoardRandomly ();

        assertThat ( sudoku.getBoard ().length ).isEqualTo ( sudoku.getGridSize () );
        for (int i = 0; i < sudoku.getGridSize (); i++) {
            assertThat ( sudoku.getBoard ()[i].length ).isEqualTo ( sudoku.getGridSize () );
            for (int j = 0; j < sudoku.getGridSize (); j++) {
                assertThat ( sudoku.getBoard ()[i][j] ).isLessThan ( 10 );
                assertThat ( sudoku.getBoard ()[i][j] ).isGreaterThan ( -1 );
            }
        }
    }

    @Test
    void fillBoardBasedInCluesRandomlyTest() {
        Sudoku sudoku = new Sudoku ();
        sudoku.setNumClues ( 80 );
        sudoku.fillBoardBasedInCluesRandomly ();
        int zero = 0;
        int clue = 0;
        for (int i = 0; i < sudoku.getGridSize (); i++) {
            for (int j = 0; j < sudoku.getGridSize (); j++) {
                if (sudoku.getBoard ()[i][j] == 0) {
                    zero++;
                } else {
                    clue++;
                }
            }
        }
        assertThat ( zero ).isOne ();
        assertThat ( clue ).isEqualTo ( 80 );
    }

    @Test
    void fillBoardBasedInCluesRandomlySolvableTest() {
        //El test falla; el método no funciona correctamente.
        Sudoku s = new Sudoku ();
        s.fillBoardBasedInCluesRandomlySolvable ();
        assertThat ( s.solveBoard () ).isTrue ();
    }

    @Test
    void fillBoardUnsolvableTest() {
        Sudoku s = new Sudoku ();
        s.fillBoardUnsolvable ();
        assertThat ( s.solveBoard () ).isFalse ();
    }

    @Test
    void copyBoardTest() {
        Sudoku s = new Sudoku ();
        s.fillBoardRandomly ();
        Sudoku su = new Sudoku ();
        su.copyBoard ( s.getBoard () );
        assertThat ( su.getBoard () ).isEqualTo ( s.getBoard () );
    }

    @Test
    void putNumberInBoardTest() {
        Sudoku s = new Sudoku ();
        s.fillBoardRandomly ();
        s.putNumberInBoard ( 4, 0, 0 );
        assertThat ( s.getBoard ()[0][0] ).isEqualTo ( 4 );
    }

    @Test
     void printBoardTest() {

    }
    @Test
    void isNumberInRowTest() {
       Sudoku s = new Sudoku ();
       s.fillBoardRandomly ();
       s.putNumberInBoard ( 4, 0, 0 );
       s.printBoard ();
       assertThat ( s.isNumberInRow (  4, 0) ).isTrue ();
    }
    @Test
    void isNumberInColumnTest() {
        Sudoku s = new Sudoku ();
        s.fillBoardRandomly ();
        s.putNumberInBoard ( 4, 0, 0 );
        s.printBoard ();
        assertThat ( s.isNumberInColumn (  4, 0) ).isTrue ();
    }
    @Test
    void isNumberInBoxTest() {
        //El test falla, ya que el método falla también.
        Sudoku s = new Sudoku ();
        s.setNumClues ( 80 );
        s.fillBoardBasedInCluesRandomlySolvable ();
        s.putNumberInBoard ( 0, 0, 0 );
        s.printBoard ();
        assertThat ( s.isNumberInBox (0,  0, 0) ).isTrue ();
    }
    @Test
    void isValidPlacementTest() {
        Sudoku s = new Sudoku ();
        s.fillBoardRandomly ();
        assertThat ( s.isValidPlacement ( 0, 0, 0 ) ).isTrue ();
    }
    @Test
    void solveBoardTest() {
        Sudoku s = new Sudoku ();
        s.fillBoardSolvable ();
        assertThat ( s.solveBoard () ).isTrue ();
        s.fillBoardUnsolvable ();
        assertThat ( s.solveBoard () ).isFalse ();
    }
}