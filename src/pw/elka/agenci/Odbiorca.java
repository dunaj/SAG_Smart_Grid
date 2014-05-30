package pw.elka.agenci;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Random;

/**
 * Agent proszacy o energie dytrybutorow i wykorzystujacy energie.
 */
public class Odbiorca extends Agent implements OdbieraczEnergii {

	private static final long serialVersionUID = -7698025950501425520L;
	private static final int CZAS_TIKA = 2000;
	static int liczbaOdbiorcow = 0;
	
	/**
	 * numer odbiorcy w systemie
	 */
	int nrOdbiorcy;
	
	private Dystrybutor dystrybutor;
	/**
	 * pole pokazujace ile odbiorca potrzebuje pradu
	 */
	private int zapotrzebowanie = 0;

	/**
	 * Konstruktor przyjmujacy Dystrybutora z ktorym ma byc polaczony Odbiorca
	 * 
	 * @param d
	 *            - dystrybutor, z ktorym ma byc polaczony Odbiorca
	 */
	public Odbiorca(Dystrybutor d) {
		this.dystrybutor = d;
		setup();
	}

	@Override
	protected void setup() {
		super.setup();
		addBehaviour(new TickerBehaviour(this, CZAS_TIKA) {
			
			private static final long serialVersionUID = 6022385825163721857L;

			protected void onTick() {
				addBehaviour(new PoborPradu());
			}
		});
		addBehaviour(new OdbiorPradu());
		System.out.println("Odbiorca "+nrOdbiorcy+" jest gotowy do dzialania!");
	}

	/**
	 * Generator poboru pradu w danej chwili.
	 * 
	 * @return losowa ilosc energii
	 */
	private int ileEnergii() {
		Random gen = new Random();
		return gen.nextInt(1000);
	}

	/**
	 * funkcja zwiekszajaca zapotrzebowanie na prad, wywolywana po wyslaniu
	 * prosby o energie do Dystrybutora
	 */
	private void zwiekszZapotrzebowanie(int ile) {
		zapotrzebowanie += ile;
	}

	/**
	 * funkcja zmniejszajaca zapotrzebowanie na prad, wywolywana po odebraniu
	 * energii od Dystrybutora
	 */
	private void zmniejszZapotrzebowanie(int ile) {
		zapotrzebowanie -= ile;
	}

	/**
	 * funkcja wyswietlajaca aktualne zapotrzebowanie na energie
	 */
	public String jakieZapotrzebowanie() {
		return "Zapotrzebowanie Odbiorcy: " + zapotrzebowanie;
	}

	/**
	 * Wewn klasa implementujaca zachowanie Odbiorcow Cykliczny pobor pradu.
	 */
	public class PoborPradu extends Behaviour {

		private static final long serialVersionUID = -6705750061317380494L;

		@Override
		public void action() {

			ACLMessage prosba = new ACLMessage(ACLMessage.REQUEST);
			int ile = ileEnergii();
			prosba.setContent(String.valueOf(ile));
			prosba.addReceiver(dystrybutor.getAID());
			((Odbiorca) myAgent).zwiekszZapotrzebowanie(ile);
			myAgent.send(prosba);
		}

		@Override
		public boolean done() {
			return true;
		}

	}

	/**
	 * Zachowanie odpowiedzialne za odbior pradu od Dystrybutora
	 */
	public class OdbiorPradu extends CyclicBehaviour {

		private static final long serialVersionUID = 6753410205530977115L;

		@Override
		public void action() {
			MessageTemplate mtAgree = MessageTemplate
					.MatchPerformative(ACLMessage.CONFIRM);
			ACLMessage odp = myAgent.receive(mtAgree);
			if (odp != null) {
				zmniejszZapotrzebowanie(Integer.parseInt(odp.getContent()));
			} else {
				block();
			}
		}
	}
}
