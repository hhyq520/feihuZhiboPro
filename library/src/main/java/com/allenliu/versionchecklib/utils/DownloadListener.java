package com.allenliu.versionchecklib.utils;

public interface DownloadListener {
	
	/**
	 * Download progress callback
	 * @param totalSize File total size
	 * @param progress The current download size
	 */
	public void onProgress(long totalSize, long progress, String url, String downloadId, Object object, String targetPath, String tempPath, String ext);
	/**
	 * Download the back end
	 * @param status Download status
	 * @param url Download url address
	 * @param targetPath Download the file path
	 * @param tempPath Temporary path to download files
	 * @param error Error content
	 * @param throwable Exception information
	 */
	public void onFinish(int status, String url, String downloadId, Object object, String targetPath, String tempPath, String error, Throwable throwable, String ext);

}
