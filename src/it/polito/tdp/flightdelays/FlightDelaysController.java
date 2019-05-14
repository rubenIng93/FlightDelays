

/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.flightdelays;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;
import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
/**
 * Sample Skeleton for 'FlightDelays.fxml' Controller Class
 */



public class FlightDelaysController {
	private Model model;
	Map<Integer, Airport> aIdMap = new HashMap<>();

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="distanzaMinima"
    private TextField distanzaMinima; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoArrivo"
    private ComboBox<Airport> cmbBoxAeroportoArrivo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAeroportiConnessi"
    private Button btnAeroportiConnessi; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	
    	txtResult.clear();
    	    	
    	int distanzaMin = Integer.parseInt(distanzaMinima.getText());
    	model.creaGrafo(distanzaMin);
    	txtResult.appendText("Grafo creato!");

    }

    @FXML
    void doTestConnessione(ActionEvent event) {
    	
    	txtResult.clear();
    	Airport partenza = cmbBoxAeroportoPartenza.getValue();
    	Airport arrivo = cmbBoxAeroportoArrivo.getValue();
    	if(model.testConnessione(partenza.getId(), arrivo.getId())) {
    		txtResult.appendText("I due aereoporti sono connessi; \n"
    				+ "Ecco un possibile persorso: \n");
    		for(Airport air : model.trovaPercorso(partenza.getId(), arrivo.getId())) {
    			txtResult.appendText(air+"\n");
    		}
    	}
    	else {
    		txtResult.appendText("Aereoporti non connessi!");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert cmbBoxAeroportoArrivo != null : "fx:id=\"cmbBoxAeroportoArrivo\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'FlightDelays.fxml'.";

    }
    
    public void setModel(Model model) {
		this.model = model;
		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();
		for(Airport air : dao.loadAllAirports(aIdMap)) {
			cmbBoxAeroportoArrivo.getItems().addAll(air);
			cmbBoxAeroportoPartenza.getItems().addAll(air);
		}
		
		
	}
}




