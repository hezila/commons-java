package com.numb3r3.common.db.berkeleydb;

import java.io.File;

import com.numb3r3.common.io.FileUtils;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

/**
 * A Factory class of Berkeley DB class
 * 
 * @author Feng Wang
 * 
 */
public class BerkeleyDBFactory {

	/**
	 * The default database storage foolder path
	 */
	private static String DEFAULT_STORAGE_FOLDER = "BDB_DATA";

	/**
	 * The environment for berkeley db
	 */
	private static Environment env;

	/**
	 * Init the environment
	 * 
	 * @param storageFolder
	 *            the storage folder path
	 * @param resumable
	 *            whether resumable
	 */
	public static void init(final String storageFolder, final boolean resumable) {
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		envConfig.setTransactional(resumable);
		envConfig.setLocking(resumable);

		File envHome = new File(storageFolder + "/data");
		if (!envHome.exists()) {
			envHome.mkdir();
		}
		if (!resumable) {
			FileUtils.deleteFolderContents(envHome);
		}

		env = new Environment(envHome, envConfig);
	}

	/**
	 * Get a database given the dbName
	 * 
	 * @param dbName
	 *            the database name
	 * @param dumplicate
	 *            If true, duplicate records are allowed in the database. If
	 *            this value is false, then putting a duplicate record into the
	 *            database results in an error return from the put call.
	 * @param resumable
	 *            wether resumable
	 * @return the berkeley db
	 */
	public static Database getDatabase(final String dbName,
			final boolean duplicate, final boolean resumable) {
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		dbConfig.setSortedDuplicates(duplicate);
		//dbConfig.setTransactional(resumable);

		/*
		 * support in-memory cache write
		 */
		dbConfig.setDeferredWrite(!resumable);

		Database db = env.openDatabase(null, dbName, dbConfig);
		return db;
	}

	/**
	 * Get the storage folder path
	 * 
	 * @return the storage folder path
	 */
	public String getStorageFolderPath() {
		return DEFAULT_STORAGE_FOLDER;
	}

	/**
	 * Get the environment
	 * 
	 * @return the environment
	 */
	public static Environment getEnv() {
		return env;
	}

	/**
	 * Close
	 */
	public static void close() {
		if (env != null) {
			env.close();
		}

	}
}
