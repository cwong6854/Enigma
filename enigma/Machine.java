package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Curtis Wong
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _pawls = pawls;
        _numRotors = numRotors;
        _allRotors = new ArrayList<>(allRotors);
        _rotors = new Rotor[numRotors];
        _plugboard = new Permutation("", alpha);

    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        int count = 0;
        for (String name: rotors) {
            for (Rotor rotor: _allRotors) {
                String upper = rotor.name().toUpperCase();
                String upperlower = rotor.name().substring(0, 1).toUpperCase() + rotor.name().substring(1);
                if (count == _allRotors.size()) {
                    break;
                } else if ((rotor.name().equals(name)) || upper.equals(name) || upperlower.equals(name)) {
                    _rotors[count] = rotor;
                    count += 1;
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != _numRotors - 1) {
            throw new EnigmaException("Initial positions string wrong length");
        }
        if (numRotors() - 1 != setting.length()) {
            throw new EnigmaException("Setting must be string of numRotors-1");
        }
        for (int i = 1; i < 5; i += 1) {
            if (!(_alphabet.contains(setting.charAt(i - 1)))) {
                throw new EnigmaException("Position setting not in alphabet");
            }
            if (!_rotors[i].reflecting()) {
                _rotors[i].set(setting.charAt(i - 1));
            }
            _rotors[i].set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] move = new boolean[_rotors.length];
        int count = c;
        for (int i = _numRotors - numPawls(); i < _rotors.length - 1; i += 1) {
            if (_rotors[i + 1].atNotch()) {
                move[i] = true;
            } else if (_rotors[i - 1].rotates() && _rotors[i].atNotch()) {
                move[i] = true;
            }
        }
        move[move.length - 1] = true;
        for (int j = 0; j < move.length; j += 1) {
            if (move[j]) {
                _rotors[j].advance();
            }
        }

        if (_plugboard != null) {
            count = _plugboard.permute(count);
        }
        for (int k = _rotors.length - 1; k > 0; k -= 1) {
            count = _rotors[k].convertForward(count);
        }
        for (int l = 0; l < _rotors.length; l += 1) {
            count = _rotors[l].convertBackward(count);
        }
        if (_plugboard != null) {
            count = _plugboard.invert(count);
        }
        return count;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String s = "";
        for (int i = 0; i < msg.length(); i += 1) {
            s += _alphabet.toChar(this.convert(_alphabet.toInt(msg.charAt(i))));
        }
        return s;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Total number of rotors. */
    private int _numRotors;

    /** Total number of pawls. */
    private int _pawls;

    /** Array of rotors formatting the machine. */
    private Rotor[] _rotors;

    /** Initial plugboard. */
    private Permutation _plugboard;
    /** Arraylist containing all rotors. */
    private ArrayList<Rotor> _allRotors;

}
