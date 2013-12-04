package com.numb3r3.common.db.berkeleydb;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryCursor;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

public class BerkeleyDBMatrix<K extends String, V extends String> {

	private Database db = null;

	private SecondaryDatabase firstKeyIndex = null;
	
	private SecondaryDatabase secondKeyIndex = null;

	private StoredClassCatalog classCatalog = null;

	private EntryBinding<K> keyBinding = null;

	private EntryBinding<V> valueBinding = null;

	private EntryBinding<Entry<K, V>> entryBinding = null;

	private EntryBinding<PairKey<K>> pairKeyBinding = null;

	public BerkeleyDBMatrix(final String dbName, final boolean resumable,
			Class<K> KClass, Class<V> VClass) {

		db = BerkeleyDBFactory.getDatabase(dbName, false, resumable);

		/*
		 * Open the database that you use to store your class information. The
		 * db used to store class information does not require duplicates
		 * support.
		 */
		Database catalogDB = BerkeleyDBFactory.getDatabase("classDB", false,
				false);

		/*
		 * Instantiate the class catalog
		 */
		classCatalog = new StoredClassCatalog(catalogDB);

		/*
		 * Create the binding
		 */
		keyBinding = new SerialBinding(classCatalog, KClass);
		valueBinding = new SerialBinding(classCatalog, VClass);
		entryBinding = new SerialBinding(classCatalog, Entry.class);
		pairKeyBinding = new SerialBinding(classCatalog, PairKey.class);

		SecondaryConfig mySecConfig = new SecondaryConfig();
		mySecConfig.setAllowCreate(true);
		/*
		 * Duplicates are frequently required for secondary databases.
		 */
		mySecConfig.setSortedDuplicates(true);

		/*
		 * Get a secondary object and set the key creator on it.
		 */
		SecondaryKeyCreator firstKeyCreator = new MyFirstKeyCreator();
		mySecConfig.setKeyCreator(firstKeyCreator);
		

		firstKeyIndex = BerkeleyDBFactory.getEnv().openSecondaryDatabase(null,
				"matrix_first_key", db, mySecConfig);
		
		/*
		 * Get a secondary object and set the key creator on it.
		 */
		SecondaryKeyCreator secondKeyCreator = new MySecondaryKeyCreator();
		mySecConfig.setKeyCreator(secondKeyCreator);
		
		secondKeyIndex = BerkeleyDBFactory.getEnv().openSecondaryDatabase(null,
				"matrix_second_key", db, mySecConfig);
		
	}

	public void put(final K firstKey, final K secondKey, final V value) {
		PairKey<K> pairKey = new PairKey(firstKey, secondKey);
		Entry<K, V> entry = new Entry(pairKey, value);
		
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		pairKeyBinding.objectToEntry(pairKey, key);
		entryBinding.objectToEntry(entry, data);
		
		db.put(null, key, data);
	}

	public V get(K first, K second) {
		DatabaseEntry primaryKey = new DatabaseEntry();
		// DatabaseEntry value = new DatabaseEntry();

		DatabaseEntry foundData = new DatabaseEntry();
		
		//Cursor cursor = db.openCursor(null, null);
		
		
		PairKey<K> pairKey = new PairKey(first, second);
		pairKeyBinding.objectToEntry(pairKey, primaryKey);

		OperationStatus retVal = db.get(null, primaryKey, foundData, LockMode.DEFAULT);
		//System.out.println("Firsts: " + this.size() + "\t" + second.toString() + "\t" + retVal);
		while (retVal == OperationStatus.SUCCESS) {

			Entry<K, V> entry = (Entry<K, V>) entryBinding
					.entryToObject(foundData);
			return entry.getValue();
		}
		return null;
	}

	public Map<K, Entry<K, V>> getByFirstKey(final K searchKey) {
		Map<K, Entry<K, V>> map = new HashMap<K, Entry<K, V>>();
		
		DatabaseEntry secondaryKey = new DatabaseEntry();
		keyBinding.objectToEntry(searchKey, secondaryKey);
		
		DatabaseEntry primaryKey = new DatabaseEntry();
		DatabaseEntry foundData = new DatabaseEntry();

		SecondaryCursor mySecCursor = firstKeyIndex.openCursor(null, null);
		

		OperationStatus retVal = mySecCursor.getSearchKey(secondaryKey,
				foundData, LockMode.DEFAULT);
		
		while (retVal == OperationStatus.SUCCESS) {
			Entry<K, V> entry = (Entry<K, V>) entryBinding.entryToObject(foundData);
			retVal = mySecCursor.getNextDup(secondaryKey, foundData, LockMode.DEFAULT);
			map.put(entry.getSecondKey(), entry);
		}
		mySecCursor.close();
		return map;
	}

	public Map<K, Entry<K, V>> getBySecondKey(final K searchKey) {
		Map<K, Entry<K, V>> map = new HashMap<K, Entry<K, V>>();
		DatabaseEntry secondaryKey = new DatabaseEntry();
		keyBinding.objectToEntry(searchKey, secondaryKey);
		
		DatabaseEntry primaryKey = new DatabaseEntry();
		DatabaseEntry foundData = new DatabaseEntry();

		SecondaryCursor mySecCursor = secondKeyIndex.openCursor(null, null);

		OperationStatus retVal = mySecCursor.getSearchKey(secondaryKey,
				foundData, LockMode.DEFAULT);

		while (retVal == OperationStatus.SUCCESS) {			
			Entry<K, V> entry = (Entry<K, V>) entryBinding
					.entryToObject(foundData);
			retVal = mySecCursor.getNextDup(secondaryKey, foundData, LockMode.DEFAULT);
			map.put(entry.getFirstKey(), entry);
		}
		mySecCursor.close();
		return map;
	}
	
	public int size(){
		return (int) db.count();
	}

	public void close() {
		classCatalog.close();
		firstKeyIndex.close();
		secondKeyIndex.close();
		db.close();
	}

	public final static class Entry<K, V> implements Serializable {
		private PairKey<K> pairKey = null;
		private V value = null;

		public Entry(final K firstKey, final K secondKey, final V value) {
			this.pairKey = new PairKey(firstKey, secondKey);
			this.value = value;
		}
		
		public Entry(final PairKey pairKey, final V value){
			this.pairKey = pairKey;
			this.value = value;
		}

		public K getFirstKey() {
			return this.pairKey.getFirstKey();
		}

		public void setFirstKey(final K key) {
			this.pairKey.setFirstKey(key);
		}

		public K getSecondKey() {
			return this.pairKey.getSecondKey();
		}

		public void setSecondKey(final K key) {
			this.pairKey.setSecondKey(key);
		}

		public V getValue() {
			return this.value;
		}

		public void setValue(final V value) {
			this.value = value;
		}

		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("[" + this.getFirstKey() + "]");
			buffer.append("[" + this.getSecondKey() + "]");
			buffer.append("=" + this.getValue());
			
			return buffer.toString();
		}
		
	}

	private class MyFirstKeyCreator implements SecondaryKeyCreator {

		@Override
		public boolean createSecondaryKey(SecondaryDatabase secDB,
				DatabaseEntry keyEntry, DatabaseEntry dataEntry,
				DatabaseEntry resultEntry) {
			Entry<K, V> entry = (Entry<K, V>) entryBinding
					.entryToObject(dataEntry);
			K firstKey = entry.getFirstKey();
			keyBinding.objectToEntry(firstKey, resultEntry);
		
			return true;
		}

	}
	
	private class MySecondaryKeyCreator implements SecondaryKeyCreator {

		@Override
		public boolean createSecondaryKey(SecondaryDatabase secDB,
				DatabaseEntry keyEntry, DatabaseEntry dataEntry,
				DatabaseEntry resultEntry) {
			Entry<K, V> entry = (Entry<K, V>) entryBinding
					.entryToObject(dataEntry);
			K secondKey = entry.getSecondKey();
			keyBinding.objectToEntry(secondKey, resultEntry);
			return true;
		}

	}

	public static class PairKey<K> implements Serializable {
		private K firstKey = null;
		private K secondKey = null;

		public PairKey(final K firstKey, final K secondKey) {
			this.firstKey = firstKey;
			this.secondKey = secondKey;
		}

		public K getFirstKey() {
			return this.firstKey;
		}

		public void setFirstKey(final K firstKey) {
			this.firstKey = firstKey;
		}

		public K getSecondKey() {
			return this.secondKey;
		}

		public void setSecondKey(final K secondKey) {
			this.secondKey = secondKey;
		}
	}

	// private class MyEntryBinding extends TupleBinding {
	//
	// @Override
	// public Object entryToObject(TupleInput in) {
	//
	// return null;
	// }
	//
	// @Override
	// public void objectToEntry(Object object, TupleOutput to) {
	// Entry entry = (Entry) object;
	//
	// DatabaseEntry firstKey = new DatabaseEntry();
	// DatabaseEntry secondKey = new DatabaseEntry();
	// DatabaseEntry value = new DatabaseEntry();
	//
	// keyBinding.objectToEntry(entry.getFirstKey(), firstKey);
	// keyBinding.objectToEntry(entry.getSecondKey(), secondKey);
	// valueBinding.objectToEntry(entry.getValue(), value);
	//
	// to.write(firstKey.getData());
	// to.write(secondKey.getData());
	// to.write(value.getData());
	// }
	//
	// }

}
