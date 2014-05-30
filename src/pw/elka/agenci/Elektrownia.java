package pw.elka.agenci;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class Elektrownia extends Agent {

	private static final long serialVersionUID = -461482933527302411L;
	static int liczbaElektrowni = 0;
	
	
	/**
	 * Wewn klasa implementuj�ca cykliczne zachowanie Elektrownii
	 * polegaj�ce na sprawdzaniu pr�b o zapotrzebowanie na 
	 * energi� i odpowiadaniu na te pro�by.
	 * Zachowanie to b�dzie tak�e odpowiedzialne za cykliczne
	 * wytwarzanie porcji energii.
	 */
	public class ProdukcjaEnergii extends CyclicBehaviour {

		private static final long serialVersionUID = -860241246931824539L;

		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}

	}
	
}
