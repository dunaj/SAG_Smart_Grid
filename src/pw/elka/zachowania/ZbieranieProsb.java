package pw.elka.zachowania;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Klasa implementujaca zachowanie Dystrybutorow polegajace na zbieraniu prosb
 * dotyczacych zapotrzebowania na energie od odbiorcow
 */
public class ZbieranieProsb extends CyclicBehaviour {

	private static final long serialVersionUID = -8694733170063523008L;

	/**
	 * TODO skonczyc ta metode
	 */
	@Override
	public void action() {
		// Dystrybutor oczekuje wiadomosci typu: prosba w tym zachowaniu
		MessageTemplate mt = MessageTemplate
				.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			// Message received. Process it

		} else {
			// jesli nie otrzymalem wiadomosci, to blokuje watek
			block();
		}
	}

}
