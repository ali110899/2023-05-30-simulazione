package it.polito.tdp.gosales.model;

public class ArcoExt implements Comparable<ArcoExt>{

	private Retailers r1;
	private Retailers r2;
	private int peso;
	
	public ArcoExt(Retailers r1, Retailers r2, int peso) {
		super();
		this.r1 = r1;
		this.r2 = r2;
		this.peso = peso;
	}

	public Retailers getR1() {
		return r1;
	}

	public Retailers getR2() {
		return r2;
	}

	public int getPeso() {
		return peso;
	}

	@Override
	public int compareTo(ArcoExt o) {
		// TODO Auto-generated method stub
		return this.peso-o.getPeso();
	}
	
	
	
	
}
