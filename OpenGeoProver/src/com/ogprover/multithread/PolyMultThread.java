/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.multithread;

import java.util.ArrayList;

import com.ogprover.polynomials.Polynomial;
import com.ogprover.polynomials.Term;


/**
 * <dl>
 * <dt><b>Class description:</b></dt>
 * <dd>Class for thread for multiplication of two polynomials</dd>
 * </dl>
 * 
 * @version 1.00
 * @author Ivan Petrovic
 */
public class PolyMultThread implements Runnable {
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
	 * Name for identification of thread
	 */
	private String name;
	/**
	 * Total number of cooperating threads
	 */
	private int numOfCoThreads;
	/**
	 * Index of this thread (between [0..(numOfCoThreads-1)])
	 */
	private int index;
	// two arrays of terms from two polynomials that are being multiplied
	/**
	 * Array of terms from first polynomial
	 */
	private ArrayList<Term> firstArray;
	/**
	 * Array of terms from second polynomial
	 */
	private ArrayList<Term> secondArray;
	/**
	 * Polynomial for storage of subsequent results of multiplying two terms
	 */
	private Polynomial localStorage;
	/**
	 * Resulting polynomial
	 */
	private SyncProduct sp;
	
	
	/*
	 * ======================================================================
	 * ========================== GETTERS/SETTERS ===========================
	 * ======================================================================
	 */
	/**
	 * Method to get thread name
	 * 
	 * @return	Name of this thread
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * ======================================================================
	 * ========================== CONSTRUCTORS ==============================
	 * ======================================================================
	 */
	/**
	 * Constructor method
	 * 
	 * @param index	Index of thread
	 * @param numOfCoThreads	Number of threads
	 * @param firstA	Terms of first polynomial
	 * @param secondA	Terms of second polynomial
	 * @param localStorage	Polynomial for local storage
	 * @param sp	Resulting polynomial
	 * @param name	Thread name
	 */
	public PolyMultThread(int index, int numOfCoThreads, 
						   ArrayList<Term> firstA, ArrayList<Term> secondA,
						   Polynomial localStorage, SyncProduct sp, String name){
		this.name = name;
		this.numOfCoThreads = numOfCoThreads;
		this.index = index;
		this.firstArray = firstA;
		this.secondArray = secondA;
		this.localStorage = localStorage;
		this.sp = sp;
	}

	

	/*
	 * ======================================================================
	 * ========================== SPECIFIC METHODS ==========================
	 * ======================================================================
	 */
	/**
	 * Main thread method - run()
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		int firstSize = this.firstArray.size();
		int secondSize = this.secondArray.size();
		
		while(this.index < firstSize){
			Term currFirst = this.firstArray.get(this.index);
			for (int ii = 0; ii < secondSize; ii++) {
				Term result = currFirst.clone().mul(this.secondArray.get(ii));
				this.localStorage.addTerm(result);
			}
			this.index += this.numOfCoThreads;
		}
		
		// accumulate result to main polynomial
		this.sp.mergePolynomial(this.localStorage);
	}
}