package pw.elka.agenci;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Vector;

/**
 * Agent odpowiadajacy za odbieranie prosb o energie
 * od odbiorcow i przekazywanie prosb do Elektrowni,
 * a takze za poszukiwanie brakujacej energii u innych
 * Dystrybutorow, wreszcie przekazywanie energii
 * do odbiorcow.
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
	 * Wewn klasa implementujaca zachowanie Dystrybutorow polegajace na zbieraniu prosb
	 * dotyczacych zapotrzebowania na energie od odbiorcow
	 * TODO czy ta klasa jest potrzebna?
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
				// dodaj pojedyncze zachowanie Szukaj Energii (ilosc)
			} else {
				// jesli nie otrzymalem wiadomosci, to blokuje watek
				block();
			}
		}

	}
	
	/**
	 * Klasa implementujaca zachowanie Dystrybutora
	 * polegajace na poszukiwaniu energii w sieci.
	 * Dystrybutor najpierw szuka energii 
	 * u swojej Elektrowni, potem pyta Dystrybutorow
	 * 
	 * TODO czy na pewno takie dzialanie wyszukiwania
	 */
	public class SzukajEnergii extends Behaviour {

		private static final long serialVersionUID = -7619324663836444757L;
		
		private int szukanaEnergia;
		private int krok = 0;
		
		public SzukajEnergii (int energia) {
			szukanaEnergia = energia;
		}
		
		@Override
		public void action() {
			MessageTemplate mtAgree = MessageTemplate
					.MatchPerformative(ACLMessage.AGREE);
			MessageTemplate mtRefuse = MessageTemplate
					.MatchPerformative(ACLMessage.REFUSE);
			//1. pytaj swojej elektrowni (swoich elektrownii) czy ma
			//2. jesli otrzymasz odpowiedz ze elektrownia ma energie ()
			//			dodaj zachowanie dostarczenia z otrzymana energia
			//	 w pp:
			//			szukaj u dystrybutorow polaczonych z Toba
			//3. jak otrzymasz potwierdzenie od dystrybutora
			//		wiadomosc do wszyskich, ze juz masz
			//4. dodaj zachowanie dostarczenia z otrzymana energia
			switch (krok) {
			case 0: // pytaj swojej elektrowni
				ACLMessage prosba = new ACLMessage(ACLMessage.REQUEST);
				prosba.addReceiver(elektrownia.getAID());
				prosba.setContent("Energia");
				prosba.setPerformative(szukanaEnergia);
				myAgent.send(prosba);
				krok=1;
				break;
			case 1:
				ACLMessage odp = myAgent.receive(mtAgree); 
				if (odp != null) { 
					 // pozytywna odpowiedz odebrana
						 //Elektrownia ma tyle energii 
						 // TODO stworzyc zadanie dostarczenia energii
						 // TODO ustawic krok na wartosc koncowa 
				 }
				 else {
					odp = myAgent.receive(mtRefuse);
					if (odp != null) {
					//negatywna odpowiedz
					//TODO poszukiwanie energii u innych dystrybutorow
					}
				 } 
				break;
			case 2:
				break;
			case 3:
				break;
			}
			
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}

	}
	
	/**
	 * Klasa implementujaca zachowania dostarczania 
	 * energii przez dystrybutorow
	 */
	public class DostarczanieEnergii extends Behaviour {

		private static final long serialVersionUID = 2891929086673214889L;
		
		private int dostarczanaEnergia;
		
		public DostarczanieEnergii(int energia) {
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
