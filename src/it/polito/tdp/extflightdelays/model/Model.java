package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	SimpleWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	Map<Integer, Airport> aIdMap;
	Map<Airport, Airport> visita;
	
	public Model() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		aIdMap = new HashMap<Integer, Airport>();
		visita = new HashMap<>();
	}
	
	public void creaGrafo(int distanzaMedia) {
		ExtFlightDelaysDAO dao = new ExtFlightDelaysDAO();
		dao.loadAllAirports(aIdMap);
		//Aggiungere i vertici 
		Graphs.addAllVertices(grafo, aIdMap.values());
		
		for(Rotta rotta : dao.getRotte(aIdMap, distanzaMedia)) {
			//controllare se esiste già un arco 
			//se esiste, aggiorno il peso
			DefaultWeightedEdge edge = grafo.getEdge(rotta.getPartenza(), rotta.getDestinazione());
			if(edge == null) {
				Graphs.addEdge(grafo, rotta.getPartenza(), rotta.getDestinazione(), rotta.getDistanzaMedia());
			}else {
				//System.out.println("Aggiornare peso!");
				double peso = grafo.getEdgeWeight(edge);
				double newPeso = (peso + rotta.getDistanzaMedia()) / 2;
				grafo.setEdgeWeight(edge, newPeso);
			}
			
			
			
		}
		
		System.out.println("Grafo creato!");
		System.out.println("Vertici: "+grafo.vertexSet().size());
		System.out.println("Archi: "+grafo.edgeSet().size());
		
	}
	
	public boolean testConnessione(int a1, int a2) {
		Set<Airport> visitati = new HashSet<>();
		Airport partenza = aIdMap.get(a1);
		Airport destinazione = aIdMap.get(a2);
		
		BreadthFirstIterator<Airport, DefaultWeightedEdge> it = new BreadthFirstIterator<>(this.grafo, partenza);
		
		while(it.hasNext()) {
			visitati.add(it.next());
		}
		
		if(visitati.contains(destinazione))
			return true;
		else 
			return false;
		
	}
	
	public List<Airport> trovaPercorso(int a1, int a2){
		List<Airport> percorso = new ArrayList<>();
		Airport partenza = aIdMap.get(a1);
		Airport destinazione = aIdMap.get(a2);
		BreadthFirstIterator<Airport, DefaultWeightedEdge> it = new BreadthFirstIterator<>(this.grafo, partenza);
		
		visita.put(partenza, null);
		it.addTraversalListener(new TraversalListener<Airport, DefaultWeightedEdge>() {
			
			@Override
			public void vertexTraversed(VertexTraversalEvent<Airport> arg0) {
			}
			
			@Override
			public void vertexFinished(VertexTraversalEvent<Airport> arg0) {
			}
			
			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> ev) {

				Airport sorgente = grafo.getEdgeSource(ev.getEdge());
				Airport destinazione = grafo.getEdgeTarget(ev.getEdge());
				
				if(!visita.containsKey(destinazione) && visita.containsKey(sorgente)) {
					visita.put(destinazione, sorgente);
				}else if(!visita.containsKey(sorgente) && visita.containsKey(destinazione)) {
					visita.put(sorgente, destinazione);
				}
			}
			
			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent ev) {
				
			}
			
			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
			}
		});
		
		while(it.hasNext()) {
			it.next(); //non serve salvarmi niente perchè lo faccio sopra
		}
		
		if(!visita.containsKey(partenza) || !visita.containsKey(destinazione)) {
			return null;//aereoporti non collegati
		}
		
		Airport step = destinazione;
		while(!step.equals(partenza)) {
			percorso.add(step);
			step = visita.get(step);
		}
		
		percorso.add(step);
		
		return percorso;
	}

}
