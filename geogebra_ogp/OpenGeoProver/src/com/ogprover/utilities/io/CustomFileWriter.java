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
public class CustomFileWriter {
	public static final int BUFFER_SIZE = 4096; // 4K for size of output buffer
	public static final String OUTPUT_DIR_NAME = "output"; // default name of directory for all output files
	private String fileName = null;
	private String fileExtension = null;
	private File outputFile = null;
	private OutputStream outputStream = null;
	private BufferedOutputStream buffOS = null;
	
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
	 * @return the outputFile
	 */
	public File getOutputFile() {
		return outputFile;
	}

	/**
	 * 
	 * @return	full path of this file
	 */
	public String getPath() {
		return this.outputFile.getAbsolutePath();
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
	 * @param dirName		name of output directory
	 * @param fileName		name of file to be created
	 * @param fileExtension	extension of file to be created
	 * @throws IOException
	 */
	public CustomFileWriter(String dirName, String fileName, String fileExtension) throws IOException {
		// File outDir = new File(dirName);
		File outDir = new File(System.getProperty("user.dir") + "/" + dirName); // full path to output directory
		
		if (outDir.exists() == false)
			outDir.mkdir(); // creates output directory
		else {
			if (outDir.isDirectory() == false)
				throw new IllegalArgumentException("File with name " + CustomFileWriter.OUTPUT_DIR_NAME + " is not a directory.");
		}
		
		if (fileExtension != null && fileName.endsWith('.' + fileExtension) == true)
			this.fileName = fileName.substring(0, fileName.lastIndexOf('.'));
		else
			this.fileName = fileName;
		
		this.fileExtension = fileExtension;
		if (this.fileExtension != null)
			this.outputFile = new File(outDir, this.fileName + '.' + this.fileExtension);
		else
			this.outputFile = new File(outDir, this.fileName);
		this.outputStream = new FileOutputStream(this.outputFile);
		this.buffOS = new BufferedOutputStream(this.outputStream);
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param fileName			name of file to be created
	 * @param fileExtension		extension of file to be created
	 * @throws IOException
	 */
	public CustomFileWriter(String fileName, String fileExtension) throws IOException {
		this(CustomFileWriter.OUTPUT_DIR_NAME, fileName, fileExtension);
	}
	
	/**
	 * Constructor method.
	 * 
	 * @param fileName	name of file to be created
	 * @throws IOException
	 */
	public CustomFileWriter(String fileName) throws IOException {
		this(fileName, null);
	}
	
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