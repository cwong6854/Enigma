package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Curtis Wong
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testPermute() {
        Alphabet A = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        String B = "(ABCD) (EFG)";
        Permutation perm1 = new Permutation(B, A);
        assertEquals(2, perm.permute(1));

        String C = "(XYZ) (AFGH)";
        Permutation perm2 = new Permutation(C, A);
        assertEquals(23, perm2.permute(25));

        String D = "(S)";
        Permutation perm3 = new Permutation(D, A);
        assertEquals(18, perm3.permute(18));

        String E = new String("(AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)");
        Permutation perm4 = new Permutation(E, A);
        assertEquals(5, perm4.permute(0));
        assertEquals(0, perm4.permute(16));
        assertEquals(9, perm4.permute(10));
        assertEquals(24, perm4.permute(23));
    }

    @Test
    public void testInvert() {

        Alphabet A = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        String B = "(ABCD) (EFG)";
        Permutation perm1 = new Permutation(B, A);
        assertEquals(0, perm.invert(1));

        String C = "(XYZ) (AFGH)";
        Permutation perm2 = new Permutation(C, A);
        assertEquals(24, perm2.invert(25));

        String D = "(S)";
        Permutation perm3 = new Permutation(D, A);
        assertEquals(18, perm3.invert(18));

        String E = new String("(AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)");
        Permutation perm4 = new Permutation(E, A);
        assertEquals(16, perm4.invert(0));
        assertEquals(0, perm4.invert(5));
        assertEquals(23, perm4.invert(24));
    }

}
