package pw.elka.agenci;

import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

@SuppressWarnings("serial")
public class AgentStartowy {//extends Agent {
	
//	@Override
//	protected void setup() {
	public static void main(String args[]) {
		Runtime rt = Runtime.instance();
		Profile p = new ProfileImpl();
		System.out.println("Agent Startowy rusza!");
		/*
		 *  argumenty dla Odbiorcy 1
		 */
		Object[] o1Args = new Object[1];
		o1Args[0] = "d1";
		String o1Name = "o1";
		/* koniec argumentow dla Odbiorcy 1 */
		
		/*
		 *  argumenty dla Odbiorcy 2
		 */
		Object[] o2Args = new Object[1];
		o2Args[0] = "d1";
		String o2Name = "o2";
		/* koniec argumentow dla Odbiorcy 2*/
		
		/* argumenty dla Dystrybutora 1 */
		Object[] d1Args = new Object[1];
		String d1Name = "d1";
		d1Args[0] = "e1";
		/* Koniec argumentow dystrybutora 1*/		
		
		/* argumenty dla Elektrowni 1 */
		Object[] e1Args = new Object[1];
		String e1Name = "e1";
		e1Args[0] = "d1";
		/* Koniec argumentow Elektrowni 1*/		
		ContainerController cc = rt.createMainContainer(p);
		try {
			AgentController e1 = cc.createNewAgent(e1Name, "pw.elka.agenci.Elektrownia", e1Args);
			AgentController d1 = cc.createNewAgent(d1Name, "pw.elka.agenci.Dystrybutor", d1Args);
			AgentController o1 = cc.createNewAgent(o1Name, "pw.elka.agenci.Odbiorca", o1Args);
			AgentController o2 = cc.createNewAgent(o2Name, "pw.elka.agenci.Odbiorca", o2Args);
			// startowanie agentow
			e1.start();
			d1.start();
			o1.start();
			o2.start();
		} catch (Exception e) {
			System.out.println("Wyjatek!!!!!  "+e);
		}
	}
}
//	@Override
//	protected void takeDown() {
//		super.takeDown();
//		System.out.println("Agent Startowy konczy swoje dzialanie");
//	}
//}
