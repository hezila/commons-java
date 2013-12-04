package com.numb3r3.common.db.berkeleydb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class BerkeleyDBMap<K, V> implements
		Map<K, V> {

	private Database db = null;

	private StoredClassCatalog classCatalog = null;

	private EntryBinding<K> keyBinding = null;

	private EntryBinding<V> valueBinding = null;

	public BerkeleyDBMap(final String dbName, final boolean resumable,
			Class KClass, Class VClass) {
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

	}
	
	public void sync(){
		db.sync();
	}

	public void close() {
		classCatalog.close();
		db.close();
	}

	@Override
	public void clear() {
		BerkeleyDBFactory.getEnv().truncateDatabase(null, db.getDatabaseName(),
				false);
	}

	@Override
	public boolean containsKey(Object arg0) {
		K theKey = (K) arg0;

		// Open a cursor using a database handle
		Cursor cursor = db.openCursor(null, null);

		// Create the DatabaseEntry for the key
		DatabaseEntry key = new DatabaseEntry();
		keyBinding.objectToEntry(theKey, key);

		OperationStatus result = null;
		DatabaseEntry data = new DatabaseEntry();

		OperationStatus status = cursor.getSearchKey(key, data,
				LockMode.DEFAULT);

		cursor.close();

		if (status != OperationStatus.NOTFOUND) {
			return true;
		}

		return false;
	}

	@Override
	public boolean containsValue(Object arg0) {
		V data = (V) arg0;

		// Open a cursor using a database handle
		Cursor cursor = db.openCursor(null, null);

		/*
		 * Cursors need a pair of DatabaseEntry objects to operate. These hold
		 * the key and data found at any given position in the database.
		 */
		DatabaseEntry foundKey = new DatabaseEntry();
		DatabaseEntry foundData = new DatabaseEntry();
		while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			V found = (V) valueBinding.entryToObject(foundData);
			if (found.equals(data)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		Set<Entry<K, V>> entrySet = new HashSet<Entry<K, V>>();

		Cursor cursor = db.openCursor(null, null);
		DatabaseEntry foundKey = new DatabaseEntry();
		DatabaseEntry foundData = new DatabaseEntry();
		while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			K key = (K) keyBinding.entryToObject(foundKey);
			V data = (V) valueBinding.entryToObject(foundData);
			Entry<K, V> entry = new SimpleEntry(key, data);
			entrySet.add(entry);
		}
		return entrySet;
	}

	@Override
	public V get(Object arg0) {
		DatabaseEntry key = new DatabaseEntry();
		keyBinding.objectToEntry((K) arg0, key);

		DatabaseEntry value = new DatabaseEntry();

		OperationStatus result = db.get(null, key, value, LockMode.DEFAULT);
		if (result == OperationStatus.SUCCESS) {
			return (V) valueBinding.entryToObject(value);
		}

		return null;
	}

	@Override
	public boolean isEmpty() {
		if (db.count() > 0) {
			return true;
		}

		return false;
	}

	@Override
	public Set<K> keySet() {
		Set<K> set = new HashSet<K>();

		Cursor cursor = db.openCursor(null, null);
		DatabaseEntry foundKey = new DatabaseEntry();
		DatabaseEntry foundValue = new DatabaseEntry();
		while (cursor.getNext(foundKey, foundValue, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			K key = (K) keyBinding.entryToObject(foundKey);
			set.add(key);
		}

		return set;
	}

	@Override
	public V put(K arg0, V arg1) {
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();

		keyBinding.objectToEntry(arg0, key);
		valueBinding.objectToEntry(arg1, data);

		db.put(null, key, data);
		return arg1;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> arg0) {
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		for (K key_tmp : arg0.keySet()) {
			keyBinding.objectToEntry(key_tmp, key);
			valueBinding.objectToEntry(arg0.get(key_tmp), data);
			db.put(null, key, data);
		}

	}

	@Override
	public V remove(Object arg0) {
		Cursor cursor = db.openCursor(null, null);
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();
		keyBinding.objectToEntry((K) arg0, key);

		OperationStatus retVal = cursor.getSearchKey(key, data,
				LockMode.DEFAULT);

		if (cursor.count() == 1) {
			cursor.delete();
			return (V) valueBinding.entryToObject(data);
		}

		return null;
	}

	@Override
	public int size() {
		return (int) db.count();
	}

	@Override
	public Collection<V> values() {
		List<V> list = new ArrayList<V>();

		Cursor cursor = db.openCursor(null, null);
		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry data = new DatabaseEntry();

		while (cursor.getNext(key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			V v = (V) valueBinding.entryToObject(data);
			list.add(v);
		}
		return list;
	}

	private class SimpleEntry<K, V> implements Entry<K, V> {
		private K key = null;
		private V value = null;

		public SimpleEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			return this.value = value;
		}
	}

}
