package enigma;

import java.util.NoSuchElementException;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Curtis Wong
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machy = readConfig();
        String forward = _input.nextLine();
        while (_input.hasNext()) {
            String setting = forward;
            if (!setting.contains("*")) {
                throw new EnigmaException("Error! Setting is incorrect.");
            }
            setUp(machy, setting);
            forward = (_input.nextLine()).toUpperCase();
            while (!(forward.contains("*"))) {
                String output = machy.convert(forward.replaceAll(" ", ""));
                if (forward.isEmpty()) {
                    _output.println();
                } else {
                    printMessageLine(output);
                }
                if (!_input.hasNext()) {
                    forward = "*";
                } else {
                    forward = (_input.nextLine()).toUpperCase();
                }
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            Alphabet alpha = new Alphabet(_config.next());
            int numRoters = _config.nextInt();
            int numPauls = _config.nextInt();
            Collection<Rotor> rotors = new ArrayList<Rotor>();
            _alphabet = alpha;
            while (_config.hasNext()) {
                rotors.add(readRotor());
            }
            return new Machine(alpha, numRoters, numPauls, rotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String rotorname = _config.next();
            String rotortype = _config.next();
            String notches;
            String rotorcycle = "";
            ArrayList<Integer> L = new ArrayList<Integer>();
            while (_config.hasNext("\\s*[(].+[)]\\s*")) {
                rotorcycle += " " + _config.next();
            }
            Permutation n = new Permutation(rotorcycle, _alphabet);
            if (rotortype.startsWith("M")) {
                notches = rotortype.substring(1);
                return new MovingRotor(rotorname, n, notches);
            } else if (rotortype.startsWith("N")) {
                return new FixedRotor(rotorname, n);
            } else if (rotortype.startsWith("R")) {
                return new Reflector(rotorname, n);
            } else {
                throw new EnigmaException("Invalid rotor type");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner s = new Scanner(settings);
        String[] name = new String[M.numRotors()];
        String spec = "";

        if (s.hasNext("[*]")) {
            s.next();
            for (int i = 0; i < M.numRotors(); i += 1) {
                name[i] = s.next();
            }
            M.insertRotors(name);
            if (s.hasNext("\\w{" + (M.numRotors() - 1) + "}")) {
                M.setRotors(s.next());
            }
            while (s.hasNext("[(]\\w+[)]")) {
                spec += s.next() + " ";
            }
            if (spec.length() > 0) {
                M.setPlugboard(new Permutation(spec, _alphabet));
            }
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String message = msg;
        if (message.length() == 0) {
            _output.println();
        } else {
            while (message.length() > 0) {
                if (message.length() <= 5) {
                    _output.println(message);
                    message = "";
                } else {
                    _output.print(message.substring(0, 5));
                    _output.print(" ");
                    message = message.substring(5);
                }
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
