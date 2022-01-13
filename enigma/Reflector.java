package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Curtis Wong
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
    }
    @Override
    boolean reflecting() {
        return true;
    }

    @Override
    void set(int posn) {
        throw error("reflector has only one position");
    }

}
