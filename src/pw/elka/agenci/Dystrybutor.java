package pw.elka.agenci;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Vector;

/**
 * Agent odpowiadajacy za odbieranie prosb o energie od odbiorcow i
 * przekazywanie prosb do Elektrowni, a takze za poszukiwanie brakujacej energii
 * u innych Dystrybutorow, wreszcie przekazywanie energii do odbiorcow.
 * TODO wywalic konstruktory - wszystko dzieje sie w setup
 * TODO jak wymusic, zeby odbiorcy komunikowali sie tylko z jednym dystrybutorem
 * TODO a dystrybutorzy tylko z jedna elektrownia ;/
 */
public class Dystrybutor extends Agent implements OdbieraczEnergii {

	private static final long serialVersionUID = 8713713221778399107L;
	static int liczbaDystrybutorow = 0;
	
	private int nrDystrybutora;
	private AID idElektrowni;
	private Vector<AID> dystrybutorzy;
	//private Vector<OdbieraczEnergii> odbieracze;

	
//	/**
//	 * dodanie Dystrybutora , ktory bedzie polaczony
//	 * z tym dystrybutorem
//	 * 
//	 * @param d
//	 *            - dodawany dystrybutor
//	 */
//	public void dodajDystrybutora(Dystrybutor d) {
//		dystrybutorzy.add(d);
//	}
	
//	/**
//	 * dodanie Dystrybutorow , ktorzy beda polaczeni
//	 * z tym dystrybutorem
//	 * 
//	 * @param dd
//	 *            - dodawani dystrybutorzy
//	 */
//	public void dodajDystrybutora(Vector<Dystrybutor> dd) {
//		dystrybutorzy.addAll(dd);
//	}
//	
//	/**
//	 * dodanie Dystrybutora lub Odbiorcy
//	 * 
//	 * @param o
//	 *            - dodawany odbieracz
//	 */
//	public void dodajOdbieracza(OdbieraczEnergii o) {
//		odbieracze.add(o);
//	}
//
//	/**
//	 * dodanie zbioru Dystrybutorów i Odbiorców, z którymi Dystrybutor ma byc
//	 * polaczony
//	 * 
//	 * @param o
//	 *            - dodawany odbieracz
//	 */
//	public void dodajOdbieraczy(Vector<OdbieraczEnergii> oo) {
//		odbieracze.addAll(oo);
//	}

	@Override
	protected void setup() {
		super.setup();
		nrDystrybutora = liczbaDystrybutorow++;
		Object[] args = getArguments();
        idElektrowni = new AID(args[0].toString(), AID.ISLOCALNAME);
        for (int i = 1; i<args.length; i++){
        	dystrybutorzy.add(new AID(args[i].toString(), AID.ISLOCALNAME));
        }
		addBehaviour(new ZbieranieProsb());
		System.out.println("Dystrybutor "+nrDystrybutora+" jest gotowy do dzialania!");
	}

	@Override
	protected void takeDown() {
		super.takeDown();
		System.out.println("Dystrybutor "+nrDystrybutora+" konczy swoje dzialanie!");
	}

	/**
	 * Wewn klasa implementujaca zachowanie Dystrybutorow polegajace na
	 * zbieraniu prosb dotyczacych zapotrzebowania na energie od odbiorcow
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

		/**
		 * 1. pytaj swojej elektrowni (swoich elektrowni) czy ma \
		 * 2. jesli otrzymasz odpowiedz ze elektrownia ma energie () \ 
		 * 		dodaj zachowanie  dostarczenia z otrzymana energia  \
		 * w pp: \
		 * 		szukaj u dystrybutorow polaczonych z Toba \
		 * 3. jak otrzymasz potwierdzenie od dystrybutora \
		 * 			wiadomosc do wszyskich, ze juz masz  \
		 * 4. dodaj zachowanie dostarczenia \
		 * 			z otrzymana energia \
		 * 
		 * TODO wymyslec i zaimplementowac jak reagowac na INFORM
		 * 
		 */
		@Override
		public void action() {
			MessageTemplate mtAgree = MessageTemplate
					.MatchPerformative(ACLMessage.AGREE);
			MessageTemplate mtRefuse = MessageTemplate
					.MatchPerformative(ACLMessage.REFUSE);

			switch (krok) {
			case 0: // pytaj swojej elektrowni
				ACLMessage prosba = new ACLMessage(ACLMessage.REQUEST);
				prosba.addReceiver(idElektrowni);
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
					// negatywna odpowiedz - poszukiwanie energii u innych
					// dystrybutorow
					ACLMessage prosba2 = new ACLMessage(ACLMessage.REQUEST);

					for (AID d: dystrybutorzy) {
						prosba2.addReceiver(d);
					}
					prosba2.setContent(String.valueOf(szukanaEnergia));
					prosba2.setPerformative(ACLMessage.REQUEST);
					myAgent.send(prosba2);
					krok = 2;

				} else {
					block();
				}
				break;
			case 2:
				ACLMessage odp2 = myAgent.receive(mtAgree);
				if (odp2 != null) {
					// broadcast ze juz mamy energie
					ACLMessage informacja = new ACLMessage(ACLMessage.INFORM);
					for (AID d : dystrybutorzy) {
						informacja.addReceiver(d);
					}
					informacja.setContent(String.valueOf(szukanaEnergia));
					informacja.setPerformative(ACLMessage.REQUEST);
					myAgent.send(informacja);
					// mamy energie wiec mozemy ja dostarczac
					myAgent.addBehaviour(new DostarczanieEnergii(odbiorca,
							szukanaEnergia));
					krok = 3;
				} else {
					block();
				}
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
	public class DostarczanieEnergii extends OneShotBehaviour {

		private static final long serialVersionUID = 2891929086673214889L;

		private int dostarczanaEnergia;
		private AID odbiorca;

		public DostarczanieEnergii(AID odbiorca, int energia) {
			this.odbiorca = odbiorca;
			dostarczanaEnergia = energia;
		}

		@Override
		public void action() {
			// natychmiast wyslij i skoncz zachowanie
			ACLMessage przesylka = new ACLMessage(ACLMessage.CONFIRM);
			przesylka.setContent(String.valueOf(dostarczanaEnergia));
			przesylka.addReceiver(odbiorca);
			myAgent.send(przesylka);
		}
	}
}
