package edu.hm.counterobfuscator.helper;

/**
 * @author Benjamin Moellerke <bemoelle@gmail.com>
 * @date 12.02.2015
 * 
 *       class is final static Validate.notNull(...); this class represent a
 *       validation class, to validate input and output values
 * 
 */
public final class Validate {

	/**
	 * construktor is empty
	 */
	private Validate() {

	}

	/**
	 * @param obj
	 */
	public static void notNull(Object obj) {

		if (obj == null)
			throw new IllegalArgumentException("Object must not be null");
	}

	/**
	 * @param obj
	 * @param msg
	 */
	public static void notNull(Object obj, String msg) {

		if (obj == null)
			throw new IllegalArgumentException(msg);
	}

	/**
	 * @param val
	 */
	public static void isTrue(boolean val) {

		if (!val)
			throw new IllegalArgumentException("Must be true");
	}

	/**
	 * @param val
	 * @param msg
	 */
	public static void isTrue(boolean val, String msg) {

		if (!val)
			throw new IllegalArgumentException(msg);
	}

	/**
	 * @param val
	 */
	public static void isFalse(boolean val) {

		if (val)
			throw new IllegalArgumentException("Must be false");
	}

	/**
	 * @param val
	 * @param msg
	 */
	public static void isFalse(boolean val, String msg) {

		if (val)
			throw new IllegalArgumentException(msg);
	}

	/**
	 * @param objects
	 */
	public static void noNullElements(Object[] objects) {

		noNullElements(objects, "Array must not contain any null objects");
	}

	/**
	 * @param objects
	 * @param msg
	 */
	public static void noNullElements(Object[] objects, String msg) {

		for (Object obj : objects)
			if (obj == null)
				throw new IllegalArgumentException(msg);
	}

	/**
	 * @param string
	 */
	public static void notEmpty(String string) {

		if (string == null || string.length() == 0)
			throw new IllegalArgumentException("String must not be empty");
	}

	/**
	 * @param string
	 * @param msg
	 */
	public static void notEmpty(String string, String msg) {

		if (string == null || string.length() == 0)
			throw new IllegalArgumentException(msg);
	}

	/**
	 * @param msg
	 */
	public static void fail(String msg) {

		throw new IllegalArgumentException(msg);
	}
}
