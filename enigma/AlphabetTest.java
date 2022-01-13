package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

public class AlphabetTest {
    @Test
    public void testSize() {
        Alphabet A = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals(26, A.size());
        Alphabet B = new Alphabet("");
        assertEquals(0, B.size());
        Alphabet C = new Alphabet("ABDC");
        assertEquals(4, C.size());
    }
    @Test
    public void testContains() {
        Alphabet A = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals(true, A.contains('B'));
        assertEquals(false, A.contains('<'));
        assertEquals(false, A.contains('a'));
        assertEquals(true, A.contains('A'));
    }
    @Test
    public void testToChar() {
        Alphabet A = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals('A', A.toChar(0));
        assertEquals('Z', A.toChar(25));
        assertEquals('N', A.toChar(13));
    }
    @Test
    public void testToInt() {
        Alphabet A = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals(0, A.toInt('A'));
        assertEquals(25, A.toInt('Z'));
        assertEquals(13, A.toInt('N'));
    }
}
