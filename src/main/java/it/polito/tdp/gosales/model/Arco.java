package it.polito.tdp.gosales.model;

public class Arco {

	private int rCode1;
	private int rCode2;
	private int NComune;
	
	public Arco(int rCode1, int rCode2, int nComune) {
		super();
		this.rCode1 = rCode1;
		this.rCode2 = rCode2;
		NComune = nComune;
	}

	public int getrCode1() {
		return rCode1;
	}

	public int getrCode2() {
		return rCode2;
	}

	public int getNComune() {
		return NComune;
	}

	
	
	
}
