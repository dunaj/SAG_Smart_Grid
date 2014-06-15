package pw.elka.agenci;

import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;


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
		
		/*
		 *  argumenty dla Odbiorcy 3
		 */
		Object[] o3Args = new Object[1];
		o3Args[0] = "d2";
		String o3Name = "o3";
		/* koniec argumentow dla Odbiorcy 3*/
		
		/*
		 *  argumenty dla Odbiorcy 4
		 */
		Object[] o4Args = new Object[1];
		o4Args[0] = "d2";
		String o4Name = "o4";
		/* koniec argumentow dla Odbiorcy 4*/
		
		/* argumenty dla Elektrowni 1 */
		Object[] e1Args = new Object[1];
		String e1Name = "e1";
		e1Args[0] = "d1";

		/* argumenty dla Dystrybutora 1 */
		Object[] d1Args = new Object[2];
		String d1Name = "d1";
		d1Args[0] = "e1";
		d1Args[1] = "d2";
		/* Koniec argumentow dystrybutora 1*/		
		
		
		/* argumenty dla Elektrowni 2 */
		Object[] e2Args = new Object[1];
		String e2Name = "e2";
		e2Args[0] = "d2";
		
		/* argumenty dla Dystrybutora 2 */
		Object[] d2Args = new Object[2];
		String d2Name = "d2";
		d2Args[0] = "e2";
		d2Args[1] = "d1";
		

		
		
		
		
		/* Koniec argumentow Elektrowni 1*/		
		ContainerController cc = rt.createMainContainer(p);
		try {
			AgentController e1 = cc.createNewAgent(e1Name, "pw.elka.agenci.Elektrownia", e1Args);
			AgentController e2 = cc.createNewAgent(e2Name, "pw.elka.agenci.Elektrownia", e2Args);
			AgentController d1 = cc.createNewAgent(d1Name, "pw.elka.agenci.Dystrybutor", d1Args);
			AgentController d2 = cc.createNewAgent(d2Name, "pw.elka.agenci.Dystrybutor", d2Args);
			AgentController o1 = cc.createNewAgent(o1Name, "pw.elka.agenci.Odbiorca", o1Args);
			AgentController o2 = cc.createNewAgent(o2Name, "pw.elka.agenci.Odbiorca", o2Args);
			AgentController o3 = cc.createNewAgent(o3Name, "pw.elka.agenci.Odbiorca", o3Args);
			AgentController o4 = cc.createNewAgent(o4Name, "pw.elka.agenci.Odbiorca", o4Args);
			// startowanie agentow
			e1.start();
			e2.start();
			d1.start();
			d2.start();
			o1.start();
			o2.start();
			o3.start();
			o4.start();
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
