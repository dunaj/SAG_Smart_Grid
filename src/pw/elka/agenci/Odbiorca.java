package pw.elka.agenci;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

/**
 * Agent proszacy o energie dytrybutorow
 * i wykorzystujacy energie.
 */
public class Odbiorca extends Agent implements OdbieraczEnergii {

	private static final long serialVersionUID = -7698025950501425520L;

	/**
	 * Wewn klasa implementujaca zachowanie Odbiorcow
	 * Cykliczny pobor pradu.
	 * TODO zdecydowac czy to powinno byc rzeczywiscie Cykliczne czy moze
	 *  	Tick Behaviour
	 */
	public class PoborPradu extends CyclicBehaviour {

		private static final long serialVersionUID = -6705750061317380494L;

		@Override
		public void action() {
			// TODO Auto-generated method stub

		}

	}
}
