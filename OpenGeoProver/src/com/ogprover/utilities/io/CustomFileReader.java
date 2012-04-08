/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for input files</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CustomFileReader {
	public static final String INPUT_DIR_NAME = "input"; // default name of directory for all input files
	private String fileName = null;
	private String fileExtension = null;
	private File inputFile = null;
	private InputStreamReader inputSR = null;
	private BufferedReader buffReader = null;
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	
	/**
	 * @return the inputFile
	 */
	public File getInputFile() {
		return inputFile;
	}

	/**
	 * 
	 * @return	full path of this file
	 */
	public String getPath() {
		return this.inputFile.getAbsolutePath();
	}
	
	/**
	 * 
	 * @return	name of file with extension
	 */
	public String getFileNameWithExtension() {
		return this.fileName + '.' + this.fileExtension;
	}
	
	
	/**
	 * Constructor method.
	 * 
	 * @param dirName		name of input directory
	 * @param fileName		name of file to be created
	 * @param fileExtension	extension of file to be created
	 * @throws IOException
	 */
	public CustomFileReader(String dirName, String fileName, String fileExtension) throws IOException {
		//File inDir = new File(dirName);
		File inDir = new File(System.getProperty("user.dir") + "/" + dirName); // full path to input directory
		
		if (inDir.exists() == false || inDir.isDirectory() == false)
			throw new IllegalArgumentException("File with name " + CustomFileReader.INPUT_DIR_NAME + " is not a directory.");
		
		if (fileExtension != null && fileName.endsWith('.' + fileExtension) == true)
			this.fileName = fileName.substring(0, fileName.lastIndexOf('.'));
		else
			this.fileName = fileName;
		
		this.fileExtension = fileExtension;
		if (this.fileExtension != null)
			this.inputFile = new File(inDir, this.fileName + '.' + this.fileExtension);
		else
			this.inputFile = new File(inDir, this.fileName);
		this.inputSR = new InputStreamReader(new FileInputStream(this.inputFile));
		this.buffReader = new BufferedReader(this.inputSR);
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param fileName			name of file to be created
	 * @param fileExtension		extension of file to be created
	 * @throws IOException
	 */
	public CustomFileReader(String fileName, String fileExtension) throws IOException {
		this(CustomFileReader.INPUT_DIR_NAME, fileName, fileExtension);
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param fileName	name of file to be created
	 * @throws IOException
	 */
	public CustomFileReader(String fileName) throws IOException {
		this(fileName, null);
	}
	
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