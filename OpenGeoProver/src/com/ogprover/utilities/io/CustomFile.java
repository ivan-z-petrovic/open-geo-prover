/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.io;

import java.io.File;


/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>
 * 	Basic abstract class for all custom OGP files.
 * </dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */

public abstract class CustomFile {
	/*
	 * ======================================================================
	 * ========================== VARIABLES =================================
	 * ======================================================================
	 */
	/**
	 * <i><b>
	 * Version number of class in form xx.yy where
	 * xx is major version/release number and yy is minor
	 * release number.
	 * </b></i>
	 */
	public static final String VERSION_NUM = "1.00"; // this should match the version number from class comment
	
	/**
	 * Full (absolute) path of directory that contains this file
	 */
	protected String destinationDirectoryPath = null;
	/**
	 * Base file name without extension
	 */
	protected String baseFileName = null;
	/**
	 * File extension
	 */
	protected String fileExtension = null;
	
	
	
	
	/*
	 * ======================================================================
	 * ========================= ABSTRACT METHODS ===========================
	 * ======================================================================
	 */
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the destinationDirectoryPath
	 */
	public String getDestinationDirectoryPath() {
		return destinationDirectoryPath;
	}

	/**
	 * @param destinationDirectoryPath the destinationDirectoryPath to set
	 */
	public void setDestinationDirectoryPath(String destinationDirectoryPath) {
		this.destinationDirectoryPath = destinationDirectoryPath;
	}
	
	/**
	 * @return the baseFileName
	 */
	public String getBaseFileName() {
		return baseFileName;
	}

	/**
	 * @param baseFileName the baseFileName to set
	 */
	public void setBaseFileName(String baseFileName) {
		this.baseFileName = baseFileName;
	}

	/**
	 * @return the fileExtension
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * @param fileExtension the fileExtension to set
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method (default).
	 */
	public CustomFile() {
		// ...
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method for initialization of file path elements from arguments.
	 * 
	 * @param dirName			Name or path of input directory
	 * @param fileName			Name of file to be created (with or without extension)
	 * @param fileExtension		Extension of file to be created
	 */
	protected void initFilePathElements(String dirName, String fileName, String fileExtension) {
		// Determine the destination folder path of file
		if (dirName == null)
			this.destinationDirectoryPath = System.getProperty("user.dir");
		else
			this.destinationDirectoryPath = CustomFile.buildAbsolutePath(dirName);
		
		// Separate file name and extension
		if (fileExtension != null && fileName.endsWith('.' + fileExtension) == true)
			this.baseFileName = fileName.substring(0, fileName.lastIndexOf('.'));
		else
			this.baseFileName = fileName;
		this.fileExtension = fileExtension;
	}
	
	/**
	 * Method that extracts destination directory absolute path, base file name without extension and extension from passed in
	 * absolute file path.
	 * 
	 * @param absoluteFilePath		Absolute file path.
	 */
	protected void extractFilePathElementsFromAbsFilePath(String absoluteFilePath) {
		// Note: if file path isn't absolute exit the method
		if (absoluteFilePath == null || absoluteFilePath.length() == 0 || CustomFile.isFilePathAbsolute(absoluteFilePath) == false)
			return;
		
		// Extract destination directory path, base file name without extension and extension
		
		int lastFileSeparatorIdx = absoluteFilePath.lastIndexOf(File.separatorChar); // must be different from -1 since path is absolute
		
		if (lastFileSeparatorIdx == absoluteFilePath.length() - 1)
			return; // This is the case when separator is last character in path which means that base file name is missing
		
		if (lastFileSeparatorIdx == 0) // file is in root directory
			this.destinationDirectoryPath = CustomFile.getRootDirPath();
		else
			this.destinationDirectoryPath = absoluteFilePath.substring(0, lastFileSeparatorIdx);
		String baseFileNameWithExt = absoluteFilePath.substring(lastFileSeparatorIdx + 1); // At this moment baseFileName must exist and is not an empty string
		
		int lastDotIdx = baseFileNameWithExt.lastIndexOf('.');
		
		if (lastDotIdx == -1) {
			this.baseFileName = baseFileNameWithExt;
			this.fileExtension = null;
		}
		else {
			this.baseFileName = baseFileNameWithExt.substring(0, lastDotIdx);
			this.fileExtension = (lastDotIdx == baseFileNameWithExt.length() - 1) ? null : baseFileNameWithExt.substring(lastDotIdx + 1);
		}
	}
	
	/**
	 * <i>
	 * Method which returns absolute path of root directory.
	 * </i>
	 * 
	 * @return	Absolute path of root directory.
	 */
	public static String getRootDirPath() {
		char fileSeparatorChar = File.separatorChar;
		
		if (fileSeparatorChar == '\\') // Windows type of OS
			return "C:" + File.separator;
		if (fileSeparatorChar == '/') // UNIX type of OS
			return "" + File.separator;
		return null;
	}
	
	/**
	 * <i>
	 * Method which examines whether passed in file path is absolute path or not.
	 * </i>
	 * 
	 * @param filePath		File path
	 * @return				TRUE if passed in file path is absolute, FALSE otherwise.
	 */
	public static boolean isFilePathAbsolute(String filePath) {
		if (filePath == null)
			return false;
		
		char fileSeparatorChar = File.separatorChar;
		
		if (fileSeparatorChar == '\\') { // Windows type of OS
			// Path is absolute if it starts with prefix in form "C:\"
			
			char ch1 = (filePath.length() > 0) ? filePath.charAt(0) : '\0';
			char ch2 = (filePath.length() > 1) ? filePath.charAt(1) : '\0';
			char ch3 = (filePath.length() > 2) ? filePath.charAt(2) : '\0';
			
			if (((ch1 >= 'A' && ch1 <= 'Z') || (ch1 >= 'a' && ch1 <= 'z')) && ch2 == ':' && ch3 == fileSeparatorChar)
				return true;
			return false;
		}
		else if (fileSeparatorChar == '/') { // UNIX type of OS
			// Path is absolute if it starts with separator
			
			char ch1 = (filePath.length() > 0) ? filePath.charAt(0) : '\0';
			
			if (ch1 == fileSeparatorChar)
				return true;
			return false;
		}
		
		return false; // default result
	}
	
	/**
	 * <i>
	 * Method which builds absolute file path from passed in relative or absolute path.
	 * If passed in path is relative, absolute path will consider that relative path as
	 * path relative to current working directory.
	 * </i>
	 * 
	 * @param path		File or directory path.
	 * @return			Absolute file path or null in case of error.
	 */
	public static String buildAbsolutePath(String path) {
		if (path == null)
			return null;
		
		if (CustomFile.isFilePathAbsolute(path) == true)
			return path;
		
		String currDirPath = System.getProperty("user.dir"); // current working directory
		if (!currDirPath.endsWith(File.separator))
			currDirPath = currDirPath + File.separator;
		return currDirPath + path;
	}
	
	/**
	 * <i>
	 * Method that returns absolute path of passed in file object.
	 * </i>
	 * 
	 * @param file		File object.
	 * @return			Absolute path of file that corresponds to passed in file object.
	 */
	public static String getFileAbsolutePath(File file) {
		return file.getAbsolutePath();
	}
	
	/**
	 * <i>
	 * Method which builds base file name with extension.
	 * </i>
	 * 
	 * @param fileNameWithoutExtension		Base file name without extension
	 * @param fileExtension					File extension
	 * @return								Base file name with extension
	 */
	public static String buildBaseFileName(String fileNameWithoutExtension, String fileExtension) {
		return (fileExtension != null && fileExtension.length() > 0) ? fileNameWithoutExtension + "." + fileExtension : fileNameWithoutExtension;
	}
	
	/**
	 * <i>
	 * Method which builds absolute file path from path of destination directory and base file name.
	 * </i>
	 * 
	 * @param destDirPath		Absolute or relative path of destination directory (relative is considered relative to current working directory) - optional
	 * @param baseFileName		Base file name with or without extension
	 * @return					Absolute file path
	 */
	public static String buildAbsoluteFilePath(String destDirPath, String baseFileName) {
		if (destDirPath == null || destDirPath.length() == 0)
			return CustomFile.buildAbsolutePath(baseFileName); // path is relative to current working directory
		if (destDirPath.endsWith(File.separator))
			return CustomFile.buildAbsolutePath(destDirPath + baseFileName);
		return CustomFile.buildAbsolutePath(destDirPath + File.separator + baseFileName);
	}
}