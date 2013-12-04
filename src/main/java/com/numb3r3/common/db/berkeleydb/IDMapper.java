package com.numb3r3.common.db.berkeleydb;

import com.numb3r3.common.math.Maths;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.OperationStatus;

public class IDMapper {
	private static Database db = null;
	
	private static Object mutex = "ID_Mutex";
	
	private static int lastID;
	
	private static boolean resumable;
	
	public static void init(String dbName, boolean resumable)
	throws DatabaseException {
        if (dbName == null) dbName = "IDMap";
        db = BerkeleyDBFactory.getDatabase(dbName, false, resumable);
//		IDMapper.resumable = resumable;
//		DatabaseConfig dbConfig = new DatabaseConfig();
//		dbConfig.setAllowCreate(true);
//		dbConfig.setTransactional(resumable);
//		dbConfig.setDeferredWrite(!resumable);
//
//		db = env.openDatabase(null, "IDMap", dbConfig);
//		int count = (int) db.count();
//		if (resumable) {
//			if (count > 0) {
//				System.out.println("Loaded " + count
//						+ " ids that had been detected in previous.");
//				lastID = count -1;
//			}
//
//		} else {
//			lastID = -1;
//		}
	}
	
	/**
	 * Returns the id of an already seen key.
	 * If key is not seen before, it will return -1
	 */
	public static int getID(String keyID) {
		synchronized (mutex) {
			if (db == null) {
				return -1;
			}
			OperationStatus result = null;
			DatabaseEntry value = new DatabaseEntry();
			try {
				DatabaseEntry key = new DatabaseEntry(keyID.getBytes());
				result = db.get(null, key, value, null);

				if (result == OperationStatus.SUCCESS && value.getData().length > 0) {
					return Maths.byteArray2Int(value.getData());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1;
		}
	}
	
	public static int getNewID(String keyID) {
		synchronized (mutex) {
			try {
				// Make sure that we have not already assigned a docid for this URL
				int id = getID(keyID);
				if (id > 0) {
					return id;
				}

				lastID++;
				db.put(null, new DatabaseEntry(keyID.getBytes()), new DatabaseEntry(Maths.int2ByteArray(lastID)));
				return lastID;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1;
		}
	}
	
	public static void sync() {
		if (resumable) {
			return;
		}
		if (db == null) {
			return;
		}
		try {
			db.sync();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	public static void close() {
		db.close();
	}
}
