package pw.elka;

import pw.elka.agenci.Dystrybutor;
import pw.elka.agenci.Elektrownia;
import pw.elka.agenci.Odbiorca;

public class Main {
	public static void main(String[] args) {
		Dystrybutor d1 = new Dystrybutor();
		Odbiorca o1 = new Odbiorca(d1);
		d1.dodajOdbieracza(o1);
		Elektrownia e1 = new Elektrownia(d1, 3000);
		
	}

}
