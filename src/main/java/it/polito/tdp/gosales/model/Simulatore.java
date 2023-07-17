package it.polito.tdp.gosales.model;

import java.time.LocalDate;
import java.util.PriorityQueue;

import it.polito.tdp.gosales.dao.GOsalesDAO;
import it.polito.tdp.gosales.model.Event.EventType;

public class Simulatore {

	//parametri di ingresso
	private Retailers r;
	private int anno;
	private Products p;
	private int N; //quantità rifornimento
	private int nConnessi;
	
	//parametri(fissi)
	int avgD;
	int avgQ;
	private GOsalesDAO dao;
	double costoUnitario;
	double prezzoUnitario;
	double threshold;
	
	//variabili d'uscita
	private int clientiTot;
	private int clientiInsoddisfatti;
	private double costo;
	private double ricavo;
	
	//stato del mondo
	private int Q; //quantità scorta
	
	//coda degli eventi\
	PriorityQueue<Event> queue;

	public Simulatore(Retailers r, int anno, Products p, int n, int q) {
		this.r = r;
		this.anno = anno;
		this.p = p;
		N = n;
		Q = q;
		
		avgD=this.dao.getAvgD(r, p, anno);
		avgQ=this.dao.getAvgD(r, p, anno);
		this.costoUnitario= p.getUnit_cost();
		this.prezzoUnitario=p.getUnit_price();
		this.threshold=Math.min(0.2+0.1*nConnessi, 0.5);
		
		//possiamo chiamarlo qua o nel Model
		popolaCoda();
	}
	
	public void popolaCoda() {
		
		//eventi rifornimento (anno=12 mesi)
		for(int i=1; i<=12; i++) {
			Event evento = new Event(EventType.RIFORNIMENTO, LocalDate.of(anno, i, 1));
			this.queue.add(evento);
		}
		
		//eventi vendita (partono dal 15 gennaio)
		LocalDate data = LocalDate.of(anno, 1, 15);
		while(data.isBefore(LocalDate.of(anno, 12, 31))) {
			Event evento = new Event(EventType.VENDITA, data);
			this.queue.add(evento);
			//ci aggiungiamo ogni volta il numero di giorni
			data = data.plusDays(avgD);
		}
	}
	
	public void processaEventi() {
		while(queue.isEmpty()==false) {
			Event e = queue.poll();
			switch(e.getType()) {
			case RIFORNIMENTO:
				double probabilita=Math.random();
				if(probabilita<this.threshold) {
					Q=Q+ 0.8*N;
					this.costo= this.costo+this.costoUnitario*0.8*N;
				} else {
					Q=Q+N;
					this.costo= this.costo+this.costoUnitario*N;
				}
				break;
			
			case VENDITA:
				this.clientiTot++;
				if(avgQ>0.9*Q) {
					this.clientiInsoddisfatti++;
				}
				if(avgQ>=Q) {
					Q=Q-avgQ;
					this.ricavo= this.ricavo+this.costoUnitario*avgQ;
				} else {
					Q=0;
					this.ricavo= this.ricavo+this.costoUnitario*Q;
				}
				break;
				
			default:
				break;
				
			}
		}
	}
	
	
	
}
