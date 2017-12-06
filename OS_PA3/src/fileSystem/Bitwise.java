package fileSystem;

/**
 * Manage bitwise operations in a byte or array of bytes.
 *
 * Unit tests are in {@see TestBitwise}. See TestBitwise.java.
 */
public class Bitwise {
	
    private static final int bitmasks[] = {1, 2, 4, 8, 16, 32, 64, 128};

    /**
     * Check to see if bit i is set in byte. Returns true if it is
     * set, false otherwise.
     */
    public static boolean isset(int i, byte b) {
    	//just check if it is the rightmost bit 0
    	//not set means this byte 
    	if ((b & bitmasks[i]) == 0) return false;
    	return true;
    }

    /**
     * Check to see if bit i is set in array of bytes. Returns true if
     * it is set, false otherwise.
     */
    public static boolean isset(int i, byte bytes[]) {//FIXME!!!
    	//check the array
    	int positionInBitmasks = i%8;
    	int positionInBytes = (bytes.length - 1) - i/8;
    	return isset(positionInBitmasks, bytes[positionInBytes]);
    }

    /**
     * Set bit i in byte and return the new byte.
     */
    public static byte set(int i, byte b) {//FIXME!!!
    	return (byte) (b | bitmasks[i]);
    }

    /**
     * Set bit i in array of bytes.
     */
    public static void set(int i, byte bytes[]) {//FIXME!!!
    	//find position in bitmasks first
    	int positionInBitmasks = i%8;
    	int positionInBytes = (bytes.length - 1) - i/8;
    	
    	//change the value in the bytes array
    	//using the old byte
    	bytes[positionInBytes] = set(positionInBitmasks, bytes[positionInBytes]);
    }

    /**
     * Clear bit i in byte and return the new byte.
     */
    public static byte clear(int i, byte b) {//FIXME!!!
    	//set and return if it is set or not
    	
    	//find the byte in the bitmasks, find its 2's compliment
    	//bitwise and with the input and return the result
    	return (byte) (b & (~bitmasks[i]));
    }

    /**
     * Clear bit i in array of bytes and return true if the bit was 1
     * before clearing, false otherwise.
     */
    public static boolean clear(int i, byte bytes[]) {//FIXME!!!
    	//first retrieve the result, then clear
    	boolean result = false;
    	if (isset(i, bytes)) result = true;
    	
    	//then clear
    	int positionInBitmasks = i%8;
    	int positionInBytes = (bytes.length - 1) - i/8;
    	//then clear
    	bytes[positionInBytes] = clear(positionInBitmasks, bytes[positionInBytes]);
    	
    	//then return result
    	return result;
    }

    /**
     * Clear every bit in array of bytes.
     *
     * There is no clearAll for a single byte, you can just get a new
     * byte for that.
     */
    public static void clearAll(byte bytes[]) {
        for(int i = 0; i < bytes.length; ++i) {
            bytes[i] = 0;
        }
    }

    /**
     * Convert byte to a string of bits. Each bit is represented as
     * "0" if it is clear, "1" if it is set.
     */
    public static String toString(byte b) {//FIXME!!!
    	//use stringbuilder
    	StringBuilder result = new StringBuilder();
    	//iterate through all spots in the bitmasks
    	//then check against byte b and all contents in the bitmasks
    	for (int i = bitmasks.length - 1; i >= 0; i--) {
    		//if 
    		if (isset(i, b)) result.append(1);
    		else result.append(0);
    	}
    	return result.toString();
    }

    /**
     * Convert array of bytes to string of bits (each byte converted
     * to a string by calling {@link #byteToString(byte b)}, every
     * byte separated by sep, every "every" bytes separated by lsep.
     */
    public static String toString(byte bytes[], String sep,
                                  String lsep, int every) {
        String s = "";
        for(int i = bytes.length * 8 - 1; i >= 0; --i) {
        	s += isset(i, bytes) ? "1" : "0";
        	if(i > 0)
                if(every > 0 && i % (8 * every) == 0)
                    s += lsep;
                else if(i % 8 == 0)
                    s += sep;
        }
        return s;
    }

    /**
     * Convert array of bytes to string of bits, each byte separated
     * by sep. See {@link #byteToString(byte bytes[], String sep)}.
     */
    public static String toString(byte bytes[], String sep) {
        return toString(bytes, sep, null, 0);
    }

    /**
     * Convert array of bytes to string of bits, each byte separated
     * by a comma, and every 8 bytes separated by a newline. See
     * {@link #byteToString(byte bytes[], String sep)}.
     */
    public static String toString(byte bytes[]) {
        return toString(bytes, ",", "\n", 8);
    }
}