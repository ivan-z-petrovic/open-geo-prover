/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for output files</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CustomFileWriter extends CustomFile {
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
	
	public static final int BUFFER_SIZE = 4096; // 4K for size of output buffer
	public static final String OUTPUT_DIR_NAME = "output"; // default name of destination directory for all output files (from current working directory, whatever it is)
	
	/**
	 * Output file object
	 */
	private File outputFile = null;
	/**
	 * Stream for writing
	 */
	private OutputStream outputStream = null;
	/**
	 * Buffered output stream for writing
	 */
	private BufferedOutputStream buffOS = null;
	
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * @return the outputFile
	 */
	public File getOutputFile() {
		return outputFile;
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Method for initialization of output file.
	 * 
	 * @throws IOException
	 */
	private void initOutputFile() throws IOException {
		// Create file object for output folder - if folder doesn't exist, it will be created
		File outDir = new File(this.destinationDirectoryPath);
		if (outDir.exists() == false)
			outDir.mkdir(); // creates output directory
		else {
			if (outDir.isDirectory() == false)
				throw new IllegalArgumentException("File with path " + this.destinationDirectoryPath + " is not a directory.");
		}
		
		// Create file object for output file inside output directory
		this.outputFile = new File(outDir, CustomFile.buildBaseFileName(this.baseFileName, this.fileExtension));
		this.outputStream = new FileOutputStream(this.outputFile);
		this.buffOS = new BufferedOutputStream(this.outputStream);
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param dirName			Path or name of output directory
	 * @param fileName			Name of file to be created (with or without extension)
	 * @param fileExtension		Extension of file to be created
	 * @throws IOException
	 */
	public CustomFileWriter(String dirName, String fileName, String fileExtension) throws IOException {
		// Assumption: fileName argument is not null
		this.initFilePathElements(dirName, fileName, fileExtension);
		this.initOutputFile();
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param fileName			Name of file to be created (with or without extension)
	 * @param fileExtension		Extension of file to be created
	 * @throws IOException
	 */
	public CustomFileWriter(String fileName, String fileExtension) throws IOException {
		this(CustomFileWriter.OUTPUT_DIR_NAME, fileName, fileExtension); // Create file inside default output directory from current working directory
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param fileName		Absolute path or only base name of file to be created (extension is assumed)
	 * @throws IOException
	 */
	public CustomFileWriter(String fileName) throws IOException {
		if (CustomFile.isFilePathAbsolute(fileName))
			this.extractFilePathElementsFromAbsFilePath(fileName);
		else
			this.initFilePathElements(CustomFileWriter.OUTPUT_DIR_NAME, fileName, null);
		this.initOutputFile();
	}
	
	
	
	
	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Method for writing to file.
	 * 
	 * @param s	String object to be written to file
	 * @throws IOException
	 */
	public void write(String s) throws IOException {
		if (s == null)
			return;
		
		char[] charBuffer = s.toCharArray();
		byte[] byteBuffer = new byte[CustomFileWriter.BUFFER_SIZE];
		int bytesRemaining = 0; // number of bytes in array that are waiting to be written to stream
		
		for (int ii = 0, size = s.length(); ii < size; ii++) {
			if (bytesRemaining >= CustomFileWriter.BUFFER_SIZE) {
				this.outputStream.write(byteBuffer);
				this.buffOS.flush();
				bytesRemaining = 0;
			}
			
			byteBuffer[bytesRemaining] = (byte)charBuffer[ii];
			bytesRemaining++;
		}
		if (bytesRemaining > 0)
			this.outputStream.write(byteBuffer, 0, bytesRemaining);
		this.buffOS.flush();		
	}
	
	/**
	 * Method for safe closing of output stream.
	 */
	public void close() {
		if (this.outputStream != null) {
			try {
				this.outputStream.close();
			} catch (IOException e) {
				// this is called in finally block of exception handling when
				// working with CustomFileWriter; therefore ignore this exception
			}
		}
	}
	
}