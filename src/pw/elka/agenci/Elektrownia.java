package pw.elka.agenci;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Agent reprezentujacy jednostke wytwarzajaca energie
 *  TODO wywalic konstruktory - wszystko dzieje sie w setup
 * TODO jak wymusic, zeby odbiorcy komunikowali sie tylko z jednym dystrybutorem
 * TODO a dystrybutorzy tylko z jedna elektrownia ;/
 */
public class Elektrownia extends Agent {

	private static final long serialVersionUID = -461482933527302411L;
	static int liczbaElektrowni = 0;
	
	/**
	 * co ile Elektrownia wytwarza energie
	 */
	static final int CZAS_TIKA = 5000;
	
	/**
	 * numer Elektrowni
	 */
	private int nrElektrowni;
	/**
	 * Dystrybutor z ktorym polaczona jest Elektronia
	 */
	AID idDystrybutora;
	/**
	 * ilosc wyprodukowanej energii
	 */
	int wyprodukowanaEnergia;
	/**
	 * gorna granica produkowanej energii
	 */
	final int maxProdukcja = 500;
	
	@Override
	protected void setup() {
		super.setup();
		nrElektrowni = liczbaElektrowni++;
		Object[] args = getArguments();
		//idDystrybutora = new AID(args[0].toString(), AID.ISLOCALNAME);
		System.out.println("Elektrownia "+nrElektrowni+" jest gotowa do dzialania!");
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
		System.out.println("Elektrownia "+nrElektrowni+" konczy swoje dzialanie!");
	}

	/**
	 * TODO okomentowac
	 */
	private void produkujEnergie() {
		Random gen = new Random();
		wyprodukowanaEnergia=gen.nextInt(maxProdukcja);
	}
	
	/**
	 * TODO okomentowac
	 */
	private boolean sprawdzEnergie(int ile) {
		return ile<wyprodukowanaEnergia;
	}
	
	/**
	 * TODO okomentowac
	 */
	private void oddajEnergie(int ile) {
		wyprodukowanaEnergia-=ile;
	}
	
	/**
	 * Wewn klasa implementuj¹ca 
	 * wytwarzanie porcji energii co pewien czas.
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
	 * Wewn klasa odpowiedzialna za przyjmowanie zgloszen od dystrybutora
	 * i odpowiadanie na nie.
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
					oddajEnergie(ile);
					myAgent.send(odp);
				} else {
					ACLMessage odp = new ACLMessage(ACLMessage.REFUSE);
					odp.addReceiver(prosba.getSender());
					myAgent.send(odp);
				}
			} else {
				// jesli nie otrzymalem wiadomosci, to blokuje watek
				block();
			}
		}	
	}
}
