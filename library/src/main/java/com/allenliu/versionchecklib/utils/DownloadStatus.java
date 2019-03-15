package com.allenliu.versionchecklib.utils;

public class DownloadStatus {
	/**
	 * Download successful
	 */
	public static final int STATUS_SUCCESS = 0;
	/**
	 * Download failed
	 */
	public static final int STATUS_FAILED = 1;
	/**
	 * The file is downloading
	 */
	public static final int STATUS_DOWNLOADING = 2;
	/**
	 * File already exists
	 */
	public static final int STATUS_FILE_EXISTS =3;
	/**
	 * Abnormal file read and write
	 */
	public static final int STATUS_WRITE_ERROR = 4;
	/**
	 * Server access is abnormal
	 */
	public static final int STATUS_SERVER_ERROR = 5;
	/**
	 * There is no network
	 */
	public static final int STATUS_NOT_NETWORK = 6;
}
