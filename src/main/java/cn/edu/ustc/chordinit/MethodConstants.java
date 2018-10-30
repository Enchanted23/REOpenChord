/***************************************************************************
 *                                                                         *
 *                            MethodConstants.java                         *
 *                            -------------------                          *
 *   date                 : 02.09.2004, 15:23                              *
 *   copyright            : (C) 2004-2008 Distributed and                  *
 *                              Mobile Systems Group                       *
 *                              Lehrstuhl fuer Praktische Informatik       *
 *                              Universitaet Bamberg                       *
 *                              http://www.uni-bamberg.de/pi/              *
 *   email                : sven.kaffille@uni-bamberg.de                   *
 *                          karsten.loesing@uni-bamberg.de                 *
 *                                                                         *
 *                                                                         *
 ***************************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   A copy of the license can be found in the license.txt file supplied   *
 *   with this software or at: http://www.gnu.org/copyleft/gpl.html        *
 *                                                                         *
 ***************************************************************************/

package cn.edu.ustc.chordinit;

/**
 * This class contains constants regarding methods that can be invoked on
 * {@link de.uniba.wiai.lspi.chord.com.Node}. Do not forget that this
 * class has to be edited eventually if the methods in
 * {@link de.uniba.wiai.lspi.chord.com.Node} change.
 *
 * <p>
 * TODO: change to enum type !
 * </p>
 *
 * @author sven
 * @version 1.0.5
 */
public final class MethodConstants {

	/**
	 * Hide constructor as this has no instances.
	 */
	private MethodConstants() {
		// nothing to do here.
	}

	public static final int REQUEST = 0;

	public static final int RESPONSE = 1;

	public static final int CONNECT = -1;

	/**
	 * Integer constant used to identify method <code>findSuccessor</code>.
	 */
	public static final int FIND_SUCCESSOR = 0;

	/**
	 * Integer constant used to identify method <code>getNodeID</code>.
	 */
	public static final int GET_NODE_ID = 1;

	/**
	 * Integer constant used to identify method <code>insertEntry</code>.
	 */
	public static final int INSERT_ENTRY = 2;

	/**
	 * Integer constant used to identify method <code>insertReplicas</code>.
	 */
	public static final int INSERT_REPLICAS = 3;

	/**
	 * Integer constant used to identify method <code>leave</code>.
	 */
	public static final int LEAVES_NETWORK = 4;

	/**
	 * Integer constant used to identify method <code>notify</code>.
	 */
	public static final int NOTIFY = 5;

	/**
	 * Integer constant used to identify method <code>notifyAndCopeEntries</code>.
	 */
	public static final int NOTIFY_AND_COPY = 6;

	/**
	 * Integer constant used to identify method <code>ping</code>.
	 */
	public static final int PING = 7;

	/**
	 * Integer constant used to identify method <code>removeEntry</code>.
	 */
	public static final int REMOVE_ENTRY = 8;

	/**
	 * Integer constant used to identify method <code>removeReplicas</code>.
	 */
	public static final int REMOVE_REPLICAS = 9;

	/**
	 * Integer constant used to identify method <code>retrieveEntries</code>.
	 */
	public static final int RETRIEVE_ENTRIES = 10;

	/**
	 * Integer constant used to notify the endpoint that a proxy is shutting
	 * down .
	 */
	public static final int SHUTDOWN = 11;

	/**
	 * Integer constant used to get the uptime of the node.
	 */
	public static final int GET_UPTIME = 12;
	//new definition
	
	public static final int TRANSPORT_SHARD = 13;
	
	public static final int RETRIEVE_SHARD =14;
	
	public static final int TRANSPORT_RI = 15;
	
	public static final int TRANSPORT_RS = 16;
	
	public static final int TRANSPORT_RP = 17;

	/**
	 * Array containing names of methods of
	 * {@link de.uniba.wiai.lspi.chord.com.Node}. A name of a method
	 * can be accessed by using the constant identifying the method as an index
	 * into this array.
	 */
	static final String[] METHOD_NAMES = new String[] { "findSuccessor",
			"getNodeID", "insertEntry", "insertReplicas", "leavesNetwork",
			"notify", "notifyAndCopyEntries", "ping", "removeEntry",
			"removeReplicas", "retrieveEntries", "shutdown", "getUptime","transportShard","retrieveShard","transportRI","transportRS","transportRP" };

	/**
	 * Used to get the name of a method for a method identifier.
	 *
	 * @param methodIdentifier
	 *            The identifier of the method.
	 * @return The name of the method.
	 */
	static String getMethodName(int methodIdentifier) {
		return METHOD_NAMES[methodIdentifier];
	}

	static boolean isForChord(int methodIdentifier) {
		if (methodIdentifier >= FIND_SUCCESSOR && methodIdentifier <= GET_UPTIME)
			return true;
		else
			return false;
	}

}
