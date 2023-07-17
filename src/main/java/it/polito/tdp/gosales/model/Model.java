package it.polito.tdp.gosales.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.gosales.dao.GOsalesDAO;

public class Model {
	
	private GOsalesDAO dao;
	private Map<Integer, Retailers> mappaRetailers;
	private Graph<Retailers, DefaultWeightedEdge> grafo;
	
	public Model() {
		
		this.dao= new GOsalesDAO();
		this.mappaRetailers = new HashMap<Integer, Retailers>();
		
		//popoliamo la mappa
		List<Retailers> retailers = this.dao.getAllRetailers();
		for(Retailers r : retailers) {
			this.mappaRetailers.put(r.getCode(), r);
		}
	}
	
	
	public void creaGrafo(String nazione, Integer anno, Integer nMin) {
		
		this.grafo = new SimpleWeightedGraph<Retailers, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//Assegnazione Vertici
		List<Retailers> listaVertici = this.dao.getVertici(nazione);
		Graphs.addAllVertices(this.grafo, listaVertici);
		//Assegnazione Archi
		//1) calcoliamo archi da query
		//2) aggregriamo gli archi lato codice
		
		// Scelgo 1
		List<Arco> listaArchi = this.dao.getArchi(nazione, anno, nMin);
		for(Arco a : listaArchi) {
			Retailers r1 = this.mappaRetailers.get(a.getrCode1());
			Retailers r2 = this.mappaRetailers.get(a.getrCode2());
			int peso = a.getNComune();
			Graphs.addEdgeWithVertices(this.grafo, r1, r2, peso);
		}
	}
	
	public List<String> getNazioni() {
		List<String> listaNazioni = this.dao.getNazioni();
		return listaNazioni;
	}
	
	public List<Retailers> getVertici() {
		List<Retailers> lista = new ArrayList<Retailers>(this.grafo.vertexSet());
		return lista;
	}
	
	public List<ArcoExt> getArchi() {
		List<ArcoExt> lista = new ArrayList<ArcoExt>();
		
		for(DefaultWeightedEdge e :this.grafo.edgeSet()) {
			ArcoExt aExt = new ArcoExt(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), (int)(this.grafo.getEdgeWeight(e)));
			lista.add(aExt);
		}

		return lista;
	}
	
	public StatsConnessa analizzaComponenteConnessa(Retailers r) {
		
		ConnectivityInspector<Retailers, DefaultWeightedEdge> inspector = new ConnectivityInspector<Retailers, DefaultWeightedEdge>(this.grafo);
		Set<Retailers> connessi = inspector.connectedSetOf(r);
		
		//calcolare il peso totale degli archi nella componente connessa
		
		/*Possiamo prendere gli archi del grafo uno a uno e verificare se  
		 * i suoi vertici sono presenti nella componente connessa.
		 * In caso affermativo aggiungiamo il suo peso al totale.
		 */
		
		int peso=0;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(connessi.contains(this.grafo.getEdgeSource(e)) && connessi.contains(this.grafo.getEdgeTarget(e))) {
				peso= peso+(int)(this.grafo.getEdgeWeight(e));
			}
		}
		
		//restituiamo il risultato
		StatsConnessa result = new StatsConnessa(connessi, peso);
		return result;
	}
	
	public List<Products> getProductsRetailers(Retailers r, int anno) {
		
		List<Products> lista = this.dao.getProductsRetailers(r, anno);
		return lista;
	}

	
	public SimulationResult eseguiSimulazione (Products prodotto, int q, int n, Retailers r) {
		
	}
	
	
	
}
