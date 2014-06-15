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
 * Agent reprezentujacy jednostke wytwarzajaca energie 
 * TODO Elektrownia na prosbe wysyla tyle energii ile ma,
 * TODO jesli prosba jest wieksza niz ilosc posiadanej energii
 */
public class Elektrownia extends Agent {

	private static final long serialVersionUID = -461482933527302411L;
	static int liczbaElektrowni = 0;

	/**
	 * co ile Elektrownia wytwarza energie
	 */
	static final int CZAS_TIKA = 3000;

	/**
	 * numer Elektrowni
	 */
	private int nrElektrowni;
	/**
	 * Dystrybutor z ktorym polaczona jest Elektrownia
	 */
	AID idDystrybutora;
	/**
	 * ilosc wyprodukowanej energii
	 */
	int wyprodukowanaEnergia;
	/**
	 * gorna granica produkowanej energii
	 */
	final int maxProdukcja = 1000;

	@Override
	protected void setup() {
		super.setup();
		nrElektrowni = liczbaElektrowni++;
		Object[] args = getArguments();
		idDystrybutora = new AID(args[0].toString(), AID.ISLOCALNAME);
		System.out.println("Elektrownia " + nrElektrowni
				+ " jest gotowa do dzialania!");
		System.out.println(toJa()+"Mój Dystrybutor: "+idDystrybutora);
        System.out.println(toJa()+"Moje id: "+this.getAID());
		addBehaviour(new TickerBehaviour(this, CZAS_TIKA) {

			private static final long serialVersionUID = 11213112L;

			@Override
			protected void onTick() {
				addBehaviour(new ProdukcjaEnergii());
			}
		});
		addBehaviour(new PrzyjmowanieZgloszen());
	}

	@Override
	protected void takeDown() {
		super.takeDown();
		System.out.println("Elektrownia " + nrElektrowni
				+ " konczy swoje dzialanie!");
	}

	/**
	 * 
	 */
	public String toJa() {
		return "Elektrownia " + nrElektrowni + ": ";
	}

	/**
	 * TODO okomentowac
	 */
	private void produkujEnergie() {
		Random gen = new Random();
		wyprodukowanaEnergia = gen.nextInt(maxProdukcja);
		System.out.println(toJa() + "Wyprodukowalem " + wyprodukowanaEnergia
				+ "W energii");
	}

	/**
	 * TODO okomentowac
	 */
	private boolean sprawdzEnergie(int ile) {
		return ile < wyprodukowanaEnergia;
	}

	/**
	 * TODO okomentowac
	 */
	private void oddajEnergie(int ile) {
		wyprodukowanaEnergia -= ile;
	}

	/**
	 * Wewn klasa implementuj¹ca wytwarzanie porcji energii co pewien czas.
	 */
	public class ProdukcjaEnergii extends Behaviour {

		private static final long serialVersionUID = -860241246931824539L;

		@Override
		public void action() {
			produkujEnergie();
		}

		@Override
		public boolean done() {
			return true;
		}

	}

	/**
	 * Wewn klasa odpowiedzialna za przyjmowanie zgloszen od dystrybutora i
	 * odpowiadanie na nie.
	 */
	private class PrzyjmowanieZgloszen extends CyclicBehaviour {

		private static final long serialVersionUID = -8091563362530886850L;

		@Override
		public void action() {
			MessageTemplate mt = MessageTemplate
					.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage prosba = myAgent.receive(mt);
			if (prosba != null) {
				int ile = Integer.parseInt(prosba.getContent());
				if (sprawdzEnergie(ile)) {
					ACLMessage odp = new ACLMessage(ACLMessage.AGREE);
					odp.addReceiver(prosba.getSender());
					odp.setContent((new Integer(ile)).toString());
					System.out.println(toJa() + "Mam " + ile
							+ "W energii. Wysylam do "
							+ prosba.getSender().toString());
					oddajEnergie(ile);
					myAgent.send(odp);
				} else {
					// jesli nie mam tyle ile dystr chce, daje tyle ile mam
					if (wyprodukowanaEnergia > 0){
						ACLMessage odp = new ACLMessage(ACLMessage.INFORM);
						odp.addReceiver(prosba.getSender());
						odp.setContent((new Integer(wyprodukowanaEnergia)).toString());
						System.out.println(toJa() + "Mam " + wyprodukowanaEnergia
								+ "W energii. Wysylam do "
								+ prosba.getSender().toString());
						oddajEnergie(wyprodukowanaEnergia);
						myAgent.send(odp);
						
					} else {
						// jesli w ogole nic nie mam informuje o tym dystr
						ACLMessage odp = new ACLMessage(ACLMessage.REFUSE);
						odp.addReceiver(prosba.getSender());
						System.out.println(toJa()+"Nie mam w ogóle energii w tej chwili!");
						myAgent.send(odp);
					}
				}
			} else {
				// jesli nie otrzymalem wiadomosci, to blokuje watek
				block();
			}
		}
	}
}
