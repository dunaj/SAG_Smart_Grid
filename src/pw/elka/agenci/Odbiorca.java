package pw.elka.agenci;

import jade.core.AID;
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
	private static final int CZAS_TIKA = 20000;
	static int liczbaOdbiorcow = 0;
	private final int ILE_POTRZEBUJE_MAKS = 1000;
	
	/**
	 * numer odbiorcy w systemie
	 */
	int nrOdbiorcy;
	
	/**
	 * dystrybutor do ktorego dany odbiorca jest podlaczony
	 */
	private AID idDystrybutora;
	/**
	 * pole pokazujace ile odbiorca potrzebuje pradu
	 */
	private int zapotrzebowanie = 0;

	@Override
	protected void setup() {
		super.setup();
		Object[] args = getArguments();
        idDystrybutora = new AID(args[0].toString(), AID.ISLOCALNAME);
        System.out.println(toJa()+"Mój Dystrybutor: "+idDystrybutora);
        System.out.println(toJa()+"Moje id: "+this.getAID());
        nrOdbiorcy = liczbaOdbiorcow++;
        Random gen = new Random();        
		addBehaviour(new TickerBehaviour(this, gen.nextInt(CZAS_TIKA)) {
			
			private static final long serialVersionUID = 6022385825163721857L;

			protected void onTick() {
				System.out.println(jakieZapotrzebowanie());
				addBehaviour(new PoborPradu());
			}
		});
		addBehaviour(new OdbiorPradu());
		System.out.println("Odbiorca "+nrOdbiorcy+" jest gotowy do dzialania!");
	}
	
	@Override
	protected void takeDown() {
		super.takeDown();
		System.out.println("Odbiorca "+nrOdbiorcy+" konczy swoje dzialanie!");
	}
	
	/**
	 * Generator poboru pradu w danej chwili.
	 * 
	 * @return losowa ilosc energii
	 */
	private int ileEnergii() {
		Random gen = new Random();
		return gen.nextInt(ILE_POTRZEBUJE_MAKS);
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
		return toJa()+"Zapotrzebowanie: " + zapotrzebowanie+"W.";
	}
	
	/**
	 * Funkcja przedstawiajaca odbiorce
	 */
	public String toJa() {
		return "Odbiorca "+nrOdbiorcy+": ";
	}
	
	/**
	 * Wewn klasa implementujaca zachowanie Odbiorcow Cykliczny pobor pradu.
	 */
	public class PoborPradu extends Behaviour {

		private static final long serialVersionUID = -6705750061317380494L;

		@Override
		public void action() {

			ACLMessage prosba = new ACLMessage(ACLMessage.REQUEST);
			int nowaEnergia = ((Odbiorca) myAgent).ileEnergii();
			((Odbiorca) myAgent).zwiekszZapotrzebowanie(nowaEnergia);
			prosba.setContent(String.valueOf(zapotrzebowanie));
			prosba.addReceiver(idDystrybutora);
			System.out.println(toJa()+"Zapotrzebowanie zwiêkszone o "+nowaEnergia +"W energii!!");
			System.out.println(toJa()+"Proszê o "+zapotrzebowanie+"W energii!!");
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
				System.out.println(toJa()+"Dosta³em "+Integer.parseInt(odp.getContent())+"W energii!!");
			} else {
				block();
			}
		}
	}
}
