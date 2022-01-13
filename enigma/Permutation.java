package enigma;
import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Curtis Wong
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char newmap = _alphabet.toChar(wrap(p));
        char initial = _alphabet.toChar(p);
        String[] arr = _cycles.split("\\)|\\(|\\ ",
                _cycles.length() * 2);
        for (String cycle: arr) {
            for (int i = 0; i < cycle.length(); i += 1) {
                if (_alphabet.toInt(newmap) + 1 > _alphabet.size()
                        && cycle.charAt(i) == newmap) {
                    newmap = cycle.charAt(0);
                } else if (cycle.charAt(i) == newmap
                        && cycle.length() > 1 && cycle.length() - 1 != i) {
                    newmap = cycle.charAt(i + 1);
                    break;
                } else if (cycle.charAt(i) == newmap
                        && cycle.length() > 1 && i != 0) {
                    newmap = cycle.charAt(0);
                    break;
                } else if (cycle.charAt(i) == initial
                        && i + 1 > cycle.length() - 1) {
                    newmap = cycle.charAt(0);
                }
            }
        }
        return _alphabet.toInt(newmap);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char newmap = _alphabet.toChar(wrap(c));
        char initial = _alphabet.toChar(c);
        String[] arr = _cycles.split("\\)|\\(|\\ ",
                _cycles.length() * 2);
        for (String cycle: arr) {
            for (int i = 0; i < cycle.length(); i += 1) {
                if (cycle.length() == 1) {
                    return _alphabet.toInt(newmap);
                } else {
                    if (_alphabet.toInt(newmap) + 1 > _alphabet.size()
                            && cycle.charAt(i) == newmap) {
                        newmap = cycle.charAt(i - 1);
                    } else if (cycle.charAt(i) == newmap
                            && cycle.length() > 1 && i != 0) {
                        newmap = cycle.charAt(i - 1);
                        break;
                    } else if (cycle.charAt(i) == newmap
                            && cycle.length() > 1 && i == 0) {
                        newmap = cycle.charAt(cycle.length() - 1);
                        break;
                    } else if (cycle.charAt(i) == initial
                            && i + 1 > cycle.length() - 1) {
                        newmap = cycle.charAt(i - 1);
                    }
                }
            }
        }
        return _alphabet.toInt(newmap);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int convertp = _alphabet.toInt(p);
        int permutep = this.permute(convertp);
        return _alphabet.toChar(permutep);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int convertc = _alphabet.toInt(c);
        int permutec = this.invert(convertc);
        return _alphabet.toChar(permutec);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < alphabet().size(); i += 1) {
            if (this.permute(i) == i) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** Cycles represented as string of permutation. */
    private String _cycles;
}
