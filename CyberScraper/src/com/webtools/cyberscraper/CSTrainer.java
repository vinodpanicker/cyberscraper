package com.webtools.cyberscraper;

public class CSTrainer {

	private AFConstants afConstants;

	/**
	 * The function returns the AFConstants used for training
	 * @return
	 */
	public AFConstants prepareForTraining() {
		this.afConstants = new AFConstants();
		return afConstants;
		
	}
	
	/**
	 * The function returns the AFConstants used for training
	 * @return
	 */
	public AFConstants prepareForTraining(AFConstants afConstants) {
		this.afConstants=afConstants;
		return afConstants;
		
	}

}
