import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver{

	public static void main(String[] args){
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

		while(true){
			System.out.println("\t1.   Customer party enters the restaurant.");
			System.out.println("\t2.   Customer party is seated and served.");
			System.out.println("\t3.   Customer party leaves the restaurant.");
			System.out.println("\t4.   Add a table.");
			System.out.println("\t5.   Remove a table.");
			System.out.println("\t6.   Display available tables.");
			System.out.println("\t7.   Display info about waiting customer parties.");
			System.out.println("\t8.   Display info about customer parties being served.");
			System.out.println("\t9.   Close the restaurant.");
			System.out.println("Make a selection: ");

			try{
				switch(Integer.parseInt(read.readLine())){

				}
			}
			catch(IOException e){

			}
		}
	}
}
