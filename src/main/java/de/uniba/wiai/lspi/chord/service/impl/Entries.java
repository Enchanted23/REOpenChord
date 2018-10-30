/***************************************************************************
 *                                                                         *
 *                               Entries.java                              *
 *                            -------------------                          *
 *   date                 : 28.02.2005                                     *
 *   copyright            : (C) 2004-2008 Distributed and                  *
 *                              Mobile Systems Group                       *
 *                              Lehrstuhl fuer Praktische Informatik       *
 *                              Universitaet Bamberg                       *
 *                              http://www.uni-bamberg.de/pi/              *
 *   email                : sven.kaffille@uni-bamberg.de                   *
 *   			    		karsten.loesing@uni-bamberg.de                 *
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

package de.uniba.wiai.lspi.chord.service.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uniba.wiai.lspi.chord.com.Entry;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.util.logging.Logger;
import org.mapdb.*;

/**
 * Stores entries for the local node in a local hash table and provides methods
 * for accessing them. It IS allowed, that multiple objects of type
 * {@link Entry} with same {@link ID} are stored!
 * 
 * @author Wallflower
 * @version 1.0.5
 * 
 */

final class Entries {

	/**
	 * Object logger.
	 */
	private final static Logger logger = Logger.getLogger(Entries.class);

	private final static boolean debugEnabled = logger
			.isEnabledFor(Logger.LogLevel.DEBUG);
	
	private DB db;
	private HTreeMap<ID, Set<Entry>> map;
	
	private static int counter=0;

	/**
	 * Creates an empty repository for entries.
	 */
	public Entries(){
		counter++;
		this.db = DBMaker.fileDB("entriesHMap"+counter+".db").closeOnJvmShutdown().make();
		this.map= (HTreeMap<ID, Set<Entry>>) db.hashMap("mapEntries").createOrOpen();
		System.out.println("a new node has been created! ");
	} 	


	/**
	 * Stores a set of entries to the local hash table.
	 * 
	 * @param entriesToAdd
	 *            Set of entries to add to the repository.
	 * @throws NullPointerException
	 *             If set reference is <code>null</code>.
	 */
	final void addAll(Set<Entry> entriesToAdd) {

		if (entriesToAdd == null) {
			NullPointerException e = new NullPointerException(
					"Set of entries to be added to the local hash table may "
							+ "not be null!");
			Entries.logger.error("Null pointer", e);
			throw e;
		}

		for (Entry nextEntry : entriesToAdd) {
			this.add(nextEntry);
		}

		if (debugEnabled) {
			Entries.logger.debug("Set of entries of length "
					+ entriesToAdd.size() + " was added.");
		}
	}

	/**
	 * Stores one entry to the local hash table.
	 * 
	 * @param entryToAdd
	 *            Entry to add to the repository.
	 * @throws NullPointerException
	 *             If entry to add is <code>null</code>.
	 */
	final void add(Entry entryToAdd) {
		
		if (entryToAdd == null) {
			NullPointerException e = new NullPointerException(
					"Entry to add may not be null!");
			Entries.logger.error("Null pointer", e);
			throw e;
		}

		ID key1=entryToAdd.getId();
		Set<Entry> values;
		
		synchronized(this.map){
			if(map.containsKey(key1))
			{

		        values=(Set<Entry>)map.get(key1);
			}else{
				values = new HashSet<Entry>();
			}
			
			values.add(entryToAdd);
			this.map.put(key1,values);	

		}

		if (debugEnabled) {
			Entries.logger.debug("Entry was added: " + entryToAdd);
		}
	}

	/**
	 * Removes the given entry from the local hash table.
	 * 
	 * @param entryToRemove
	 *            Entry to remove from the hash table.
	 * @throws NullPointerException
	 *             If entry to remove is <code>null</code>.
	 */
	final void remove(Entry entryToRemove) {
		
		if (entryToRemove == null) {
			NullPointerException e = new NullPointerException(
					"Entry to remove may not be null!");
			Entries.logger.error("Null pointer", e);
			throw e;
		}

		ID key1;
		Set<Entry> values;

        key1=entryToRemove.getId();
        
        synchronized(this.map){
        	if(map.containsKey(entryToRemove.getId())){

    	        values=(Set<Entry>)map.get(key1);
    	        values.remove(entryToRemove); 
    	        
    			map.remove(key1);
    			map.put(key1,values);
    				
    	    }
        }
		
		if (debugEnabled) {
			Entries.logger.debug("Entry was removed: " + entryToRemove);
		}
	}

	/**
	 * Returns a set of entries matching the given ID. If no entries match the
	 * given ID, an empty set is returned.
	 * 
	 * @param id
	 *            ID of entries to be returned.
	 * @throws NullPointerException
	 *             If given ID is <code>null</code>.
	 * @return Set of matching entries. Empty Set if no matching entries are
	 *         available.
	 */
	final Set<Entry> getEntries(ID id) {

		if (id == null) {
			NullPointerException e = new NullPointerException(
					"ID to find entries for may not be null!");
			Entries.logger.error("Null pointer", e);
			throw e;
		}
		
		synchronized(this.map){
			if(map.containsKey(id))
			{
		        Set<Entry> values=(Set<Entry>) map.get(id);
		        return new HashSet<Entry>(values);
		
			}
		}
		
		if (debugEnabled) {
			Entries.logger.debug("No entries available for " + id
					+ ". Returning empty set.");
		}
		return new HashSet<Entry>();
	}

	/**
	 * Returns all entries in interval, excluding lower bound, but including
	 * upper bound
	 * 
	 * @param fromID
	 *            Lower bound of IDs; entries matching this ID are NOT included
	 *            in result.
	 * @param toID
	 *            Upper bound of IDs; entries matching this ID ARE included in
	 *            result.
	 * @throws NullPointerException
	 *             If either or both of the given ID references have value
	 *             <code>null</code>.
	 * @return Set of matching entries.
	 */
	final Set<Entry> getEntriesInInterval(ID fromID, ID toID) {

		if (fromID == null || toID == null) {
			NullPointerException e = new NullPointerException(
					"Neither of the given IDs may have value null!");
			Entries.logger.error("Null pointer", e);
			throw e;
		}

		Set<Entry> result = new HashSet<Entry>();
		synchronized(this.map){
				for (ID nextID : map.getKeys()) {
					if (nextID.isInInterval(fromID,toID)) {
						Set<Entry> entriesForID = map.get(nextID);
						for (Entry entryToAdd : entriesForID) {
							result.add(entryToAdd);
						}
					}
				}
		}
		
		// add entries matching upper bound
		result.addAll(this.getEntries(toID));

		return result;
	}

	/**
	 * Removes the given entries from the local hash table.
	 * 
	 * @param toRemove
	 *            Set of entries to remove from local hash table.
	 * @throws NullPointerException
	 *             If the given set of entries is <code>null</code>.
	 */
	final void removeAll(Set<Entry> toRemove) {

		if (toRemove == null) {
			NullPointerException e = new NullPointerException(
					"Set of entries may not have value null!");
			Entries.logger.error("Null pointer", e);
			throw e;
		}

		for (Entry nextEntry : toRemove) {
			this.remove(nextEntry);
		}

		if (debugEnabled) {
			Entries.logger.debug("Set of entries of length " + toRemove.size()
					+ " was removed.");
		}
	}

	/**
	 * Returns an unmodifiable map of all stored entries.
	 * 
	 * @return Unmodifiable map of all stored entries.
	 */
	final Map<ID, Set<Entry>> getEntries() {

		return Collections.unmodifiableMap(map);
	}

	/**
	 * Returns the number of stored entries.
	 * 
	 * @return Number of stored entries.
	 */
	final int getNumberOfStoredEntries() {
		;
		return(map.size());
	}

	/**
	 * Returns a formatted string of all entries stored in the local hash table.
	 * 
	 * @return String representation of all stored entries.
	 */
	public final String toString() {
		StringBuilder result = new StringBuilder("Entries:\n");
		
		for (Map.Entry<ID, Set<Entry>> entry : map.getEntries()) {
			result.append("  key = " + entry.getKey().toString()
					+ ", value = " + entry.getValue() + "\n");
		}
		return result.toString();
	}
}