/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for input files</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CustomFileReader extends CustomFile {
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

	public static final String INPUT_DIR_NAME = "input"; // default name of destination directory for all input files (from current working directory, whatever it is)
	
	/**
	 * Input file object
	 */
	private File inputFile = null;
	/**
	 * Stream for reading
	 */
	private InputStreamReader inputSR = null;
	/**
	 * Buffered input stream for reading
	 */
	private BufferedReader buffReader = null;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the inputFile
	 */
	public File getInputFile() {
		return inputFile;
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Method for initialization of input file.
	 * 
	 * @throws IOException
	 */
	private void initInputFile() throws IOException {
		// Create file object for input folder - if folder doesn't exist, exception is thrown
		File inDir = new File(this.destinationDirectoryPath);
		if (inDir.exists() == false || inDir.isDirectory() == false)
			throw new IllegalArgumentException("File with path " + this.destinationDirectoryPath + " doesn't exist or is not a directory.");
		
		// Create file object for input file inside input directory
		this.inputFile = new File(inDir, CustomFile.buildBaseFileName(this.baseFileName, this.fileExtension));
		this.inputSR = new InputStreamReader(new FileInputStream(this.inputFile), Charset.forName("UTF-8")); // Assume characters in input file are in UTF-8 format
		this.buffReader = new BufferedReader(this.inputSR);
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param dirName			Name or path of input directory
	 * @param fileName			Name of file to be created (with or without extension)
	 * @param fileExtension		Extension of file to be created
	 * @throws IOException
	 */
	public CustomFileReader(String dirName, String fileName, String fileExtension) throws IOException {
		// Assumption: fileName argument is not null
		this.initFilePathElements(dirName, fileName, fileExtension);
		this.initInputFile();
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param fileName			Name of file to be created (with or without extension)
	 * @param fileExtension		Extension of file to be created
	 * @throws IOException
	 */
	public CustomFileReader(String fileName, String fileExtension) throws IOException {
		this(CustomFileReader.INPUT_DIR_NAME, fileName, fileExtension); // Create file inside default input directory from current working directory
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param fileName		Absolute path or only base name of file to be created (extension is assumed)
	 * @throws IOException
	 */
	public CustomFileReader(String fileName) throws IOException {
		if (CustomFile.isFilePathAbsolute(fileName))
			this.extractFilePathElementsFromAbsFilePath(fileName);
		else
			this.initFilePathElements(CustomFileReader.INPUT_DIR_NAME, fileName, null);
		this.initInputFile();
	}
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method for reading a line from file.
	 * 
	 * @return		Line read from file.
	 * @throws IOException
	 */
	public String readLine() throws IOException {
		if (this.buffReader != null)
			return this.buffReader.readLine();
		return null;
	}
	
	/**
	 * Method for safe closing of input stream.
	 */
	public void close() {
		if (this.buffReader != null) {
			try {
				this.buffReader.close();
			} catch (IOException e) {
				// this is called in finally block of exception handling when
				// working with CustomFileReader; therefore ignore this exception
			}
		}
	}

}