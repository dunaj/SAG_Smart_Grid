package pw.elka.zachowania;

import jade.core.behaviours.Behaviour;

/**
 * Klasa implementujaca zachowanie Dystrybutora
 * polegajace na poszukiwaniu energii w sieci.
 * Dystrybutor najpierw szuka energii 
 * u innych Dystrybutorow, potem pyta Elektrownie
 * a na koncu kupuje energie od zewn dostawcy(?)
 * 
 * TODO czy na pewno takie dzialanie wyszukiwania
 */
public class SzukajEnergii extends Behaviour {

	private static final long serialVersionUID = -7619324663836444757L;

	@Override
	public void action() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
