package it.polito.tdp.gosales;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.gosales.model.ArcoExt;
import it.polito.tdp.gosales.model.Model;
import it.polito.tdp.gosales.model.Products;
import it.polito.tdp.gosales.model.Retailers;
import it.polito.tdp.gosales.model.StatsConnessa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnAnalizzaComponente;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnSimula;

    @FXML
    private ComboBox<Integer> cmbAnno;

    @FXML
    private ComboBox<String> cmbNazione;

    @FXML
    private ComboBox<Products> cmbProdotto;

    @FXML
    private ComboBox<Retailers> cmbRivenditore;

    @FXML
    private TextArea txtArchi;

    @FXML
    private TextField txtN;

    @FXML
    private TextField txtNProdotti;

    @FXML
    private TextField txtQ;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextArea txtVertici;

    @FXML
    void doAnalizzaComponente(ActionEvent event) {

    	Retailers r = this.cmbRivenditore.getValue();
    	Integer anno =  this.cmbAnno.getValue();
    	
    	
    	if(r==null) {
    		txtResult.setText("Per favore scegliere un Retailer.");
    		return;
    	}
    	
    	if(anno==null) {
    		txtResult.setText("Per favore scegliere un anno.");
    		return;
    	}
    	
    	//analizza componente connessa
    	StatsConnessa result = this.model.analizzaComponenteConnessa(r);
    	this.txtResult.appendText("La componente connessa di: "+r+" ha dimensione: "+result.getRetailers().size()+"\n");
    	this.txtResult.appendText("Il peso totale è: "+result.getPeso()+"\n");
    	
    	//abilitare controlli quiry
    	this.cmbProdotto.setDisable(false);
    	this.txtN.setDisable(false);
    	this.txtQ.setDisable(false);
    	this.btnSimula.setDisable(false);
    	this.txtN.clear();
    	this.txtQ.clear();
    	
    	//popolare box prodotti
    	this.cmbProdotto.getItems().clear();
    	this.cmbProdotto.getItems().addAll(this.model.getProductsRetailers(r, anno));
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	String nazione = this.cmbNazione.getValue();
    	if(nazione == null) {
    		txtResult.setText("Per favore inserire una nazione.");
    		return;
    	}

    	Integer anno = this.cmbAnno.getValue();
    	
    	if(anno == null) {
    		txtResult.setText("Per favore inserire un'anno.");
    		return;
    	}
    	
    	int nMin=0;
    	try {
    		nMin = Integer.parseInt(this.txtNProdotti.getText());
    	}catch(NumberFormatException e) {
    		txtResult.setText("Per favore inserire un numero intero positivo.");
    		return;
    	}
    	
    	if(nMin<0) {
    		txtResult.setText("Per favore inserire un numero intero positivo.");
    		return;
    	}
    	
    	//creare grafo e lanciarlo
    	this.model.creaGrafo(nazione, anno, nMin);
    	this.txtResult.setText("Grafo creato correttamente\n");
    	
    	//stampa vertici
    	List<Retailers> vertici = this.model.getVertici();
    	Collections.sort(vertici);
    	this.txtVertici.clear();
    	for(Retailers r : vertici) {
    		this.txtVertici.appendText(r+"\n");
    	}
    	
    	//stampa archi
    	List<ArcoExt> archi = this.model.getArchi();
    	Collections.sort(archi);
    	this.txtArchi.clear();
    	for(ArcoExt a : archi) {
    		this.txtArchi.appendText(a.getPeso()+": "+a.getR1()+" <--> "+a.getR2()+"\n");
    	}
    	
    	//popolare cmbBox Rivenditore una volta fatto grafo
    	this.cmbRivenditore.getItems().clear();
    	this.cmbRivenditore.getItems().addAll(vertici);
    	
    	//abilitare controlli query
    	this.cmbRivenditore.setDisable(false);
    	this.btnAnalizzaComponente.setDisable(false);
    	this.txtN.clear();
    	this.txtQ.clear();
    	this.cmbProdotto.setDisable(true);
    	this.txtN.setDisable(true);
    	this.txtQ.setDisable(true);
    	this.btnSimula.setDisable(true);
    	
    }

    @FXML
    void doSimulazione(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert btnAnalizzaComponente != null : "fx:id=\"btnAnalizzaComponente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbNazione != null : "fx:id=\"cmbNazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbProdotto != null : "fx:id=\"cmbProdotto\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbRivenditore != null : "fx:id=\"cmbRivenditore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtArchi != null : "fx:id=\"txtArchi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtNProdotti != null : "fx:id=\"txtNProdotti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtQ != null : "fx:id=\"txtQ\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtVertici != null : "fx:id=\"txtVertici\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	//popola nazioni
    	List<String> nazioni = this.model.getNazioni();
    	Collections.sort(nazioni);
    	this.cmbNazione.getItems().addAll(nazioni);
    	this.cmbAnno.getItems().add(2015);
    	this.cmbAnno.getItems().add(2016);
    	this.cmbAnno.getItems().add(2017);
    	this.cmbAnno.getItems().add(2018);
    }
    

}
