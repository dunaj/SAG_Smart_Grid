package pw.elka.agenci;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Vector;

/**
 * Agent odpowiadajacy za odbieranie prosb o energie od odbiorcow i
 * przekazywanie prosb do Elektrowni, a takze za poszukiwanie brakujacej energii
 * u innych Dystrybutorow, wreszcie przekazywanie energii do odbiorcow.
 */
public class Dystrybutor extends Agent implements OdbieraczEnergii {

	private static final long serialVersionUID = 8713713221778399107L;

	private Elektrownia elektrownia;
	private Vector<Dystrybutor> dytrybutorzy;

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
	}

	@Override
	protected void takeDown() {
		// TODO Auto-generated method stub
		super.takeDown();
	}

	/**
	 * Wewn klasa implementujaca zachowanie Dystrybutorow polegajace na
	 * zbieraniu prosb dotyczacych zapotrzebowania na energie od odbiorcow TODO
	 * czy ta klasa jest potrzebna?
	 */
	public class ZbieranieProsb extends CyclicBehaviour {

		private static final long serialVersionUID = -8694733170063523008L;

		@Override
		public void action() {
			// Dystrybutor oczekuje wiadomosci typu: prosba w tym zachowaniu
			MessageTemplate mt = MessageTemplate
					.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage prosba = myAgent.receive(mt);
			if (prosba != null) {
				// jesli jest
				AID odbiorca = prosba.getSender();
				int energia = Integer.parseInt((prosba.getContent()));
				myAgent.addBehaviour(new SzukajEnergii(odbiorca, energia));
			} else {
				// jesli nie otrzymalem wiadomosci, to blokuje watek
				block();
			}
		}

	}

	/**
	 * Klasa implementujaca zachowanie Dystrybutora polegajace na poszukiwaniu
	 * energii w sieci. Dystrybutor najpierw szuka energii u swojej Elektrowni,
	 * potem pyta Dystrybutorow
	 * 
	 * TODO czy na pewno takie dzialanie wyszukiwania
	 */
	public class SzukajEnergii extends Behaviour {

		private static final long serialVersionUID = -7619324663836444757L;

		private int szukanaEnergia;
		private int krok = 0;
		private AID odbiorca;

		public SzukajEnergii(AID odbiorca, int energia) {
			this.odbiorca = odbiorca;
			this.szukanaEnergia = energia;
		}

		@Override
		public void action() {
			MessageTemplate mtAgree = MessageTemplate
					.MatchPerformative(ACLMessage.AGREE);
			MessageTemplate mtRefuse = MessageTemplate
					.MatchPerformative(ACLMessage.REFUSE);
			// 1. pytaj swojej elektrowni (swoich elektrownii) czy ma
			// 2. jesli otrzymasz odpowiedz ze elektrownia ma energie ()
			// dodaj zachowanie dostarczenia z otrzymana energia
			// w pp:
			// szukaj u dystrybutorow polaczonych z Toba
			// 3. jak otrzymasz potwierdzenie od dystrybutora
			// wiadomosc do wszyskich, ze juz masz
			// 4. dodaj zachowanie dostarczenia z otrzymana energia
			switch (krok) {
			case 0: // pytaj swojej elektrowni
				ACLMessage prosba = new ACLMessage(ACLMessage.REQUEST);
				prosba.addReceiver(elektrownia.getAID());
				prosba.setContent(String.valueOf(szukanaEnergia));
				prosba.setPerformative(ACLMessage.REQUEST);
				myAgent.send(prosba);
				krok = 1;
				break;
			case 1:
				ACLMessage zgoda = myAgent.receive(mtAgree);
				ACLMessage odmowa = myAgent.receive(mtRefuse);
				if (zgoda != null) {
					// pozytywna odpowiedz - Elektrownia ma tyle energii
					myAgent.addBehaviour(new DostarczanieEnergii(odbiorca,
							szukanaEnergia));
					// koncz szukanie energii
					krok = 3;
				} else if (odmowa != null) {

					// negatywna odpowiedz
					// TODO poszukiwanie energii u innych dystrybutorow
					krok = 2;

				} else {
					block();
				}
				break;
			case 2:
				ACLMessage odp2 = myAgent.receive(mtAgree);
				if (odp2 != null) {
					// TODO wyslij broadcast ze juz masz
					// TODO stworzyc zadanie dostarczenia energii
				}
				krok = 3;
				break;
			}

		}

		@Override
		public boolean done() {
			return (krok == 3);
		}

	}

	/**
	 * Klasa implementujaca zachowania dostarczania energii przez dystrybutorow
	 */
	public class DostarczanieEnergii extends Behaviour {

		private static final long serialVersionUID = 2891929086673214889L;

		private int dostarczanaEnergia;
		private AID odbiorca;

		public DostarczanieEnergii(AID odbiorca, int energia) {
			this.odbiorca = odbiorca;
			dostarczanaEnergia = energia;
		}

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
}
