import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.BiPredicate;


public class Driver{
	private static BiPredicate<String, String> nameEquality = (x,y) -> x.equals(y);

	private static void createTableArrangments(ListArrayBasedPlus tableAdt){

	}

	private static boolean nameCheck(String name, ListArrayBasedPlus nameList){
		int listSize = nameList.size();
		for(int i = 0; i<listSize; i++){
			if(nameEquality.test(name, (String)nameList.get(i))){
				return true;
			}
		}
		return false;
	}

	private static Table tableFinder(Party p, ListArrayBasedPlus sectionOne, ListArrayBasedPlus sectionTwo){
		ListArrayBasedPlus sectionList  = p.getSection()
				.equals(Location.KID_FRIENDLY_SECTION)
				?sectionOne
						:sectionTwo;
		int sectionSize = sectionList.size();
		for(int i = 0; i<sectionSize; i++){
			Table currentTable = (Table)sectionList.get(i);
			if(currentTable.getAvailableSeating()<=p.getSize() && 
					currentTable.getCurrentParty()==null){
				return currentTable;
			}
		}
		return null;
	}

	private static boolean findTableNumber(int num,ListArrayBasedPlus sec, Table t){
		int total = sec.size();
		for(int i = 0; i<total; i++){
			Table currentTable = (Table)sec.get(i);
			if(currentTable.getTableNumber()==num){
				System.out.println("Dupicate table number detected, try again");
				return false;
			}			
		}
		System.out.println("New table added");
		sec.add(sec.size(), t);
		return true;
	}

	private static boolean checkTableNumber(ListArrayBasedPlus t, int num){
		int total = t.size();
		for(int i = 0; i<total-1; i++){
			if(((Integer)t.get(i))==num){
				return true;
			}
		}
		return false;
	}

	private static void sectionBuilder(ListArrayBasedPlus sec, int size, BufferedReader read, ListArrayBasedPlus numList) throws NumberFormatException, IOException{
		for(int i = 0; i<size; i++){
			System.out.println(">>Enter table number: ");
			int tableNum = Integer.parseInt(read.readLine());
			boolean numCheck = checkTableNumber(numList, tableNum);
			if(numCheck){
				while(numCheck){
					System.out.println("This table already exists! Please enter another table number ");
				tableNum = Integer.parseInt(read.readLine());
				numCheck = checkTableNumber(numList, tableNum);
				}
			}
			System.out.println(">>Enter number of seats: ");
			int seatNumber = Integer.parseInt(read.readLine());
			numList.add(numList.size(), tableNum);
			sec.add(sec.size(), new Table(seatNumber,tableNum));
		}
	}

	public static void main(String[] args) throws NumberFormatException, IOException{

		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		//Storage ADT's
		ListCDLSBased partyList = new ListCDLSBased();
		ListCDLSBased occupiedTables = new ListCDLSBased();
		ListArrayBasedPlus kidFriendlySeating = new ListArrayBasedPlus();
		ListArrayBasedPlus adultOnlySeating = new ListArrayBasedPlus();
		ListArrayBasedPlus nameTracking = new ListArrayBasedPlus();
		ListArrayBasedPlus tableNumbersKid = new ListArrayBasedPlus();
		ListArrayBasedPlus tableNumbersAdult = new ListArrayBasedPlus();

		//Get the initial input, this builds the seating in the restaurant.
		System.out.println("Enter your restaurant configuration: ");
		System.out.println(">>How many tables does your no-kids-allowed section have? ");
		int entries = Integer.parseInt(read.readLine());
		sectionBuilder(adultOnlySeating,entries,read,tableNumbersAdult);
		System.out.println(">>How many tables does your kid-friendly section have? ");
		entries = Integer.parseInt(read.readLine());
		sectionBuilder(kidFriendlySeating,entries,read,tableNumbersKid);



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
			System.out.println(">>Make a selection: ");

			try{
				switch(Integer.parseInt(read.readLine())){

				case 1: 			
					System.out.println("Enter the name of the party: ");
					String partyName = read.readLine();
					boolean nameFound = nameCheck(partyName,nameTracking);
					if(nameFound){
						while(nameFound){
							System.out.println("Name duplicate detected, please enter a new " +
									"name");
							partyName = read.readLine();
							nameFound = nameCheck(partyName,nameTracking);
						}
					}
					System.out.println("Enter the size of the party: ");
					Integer partySize = Integer.parseInt(read.readLine());
					System.out.println("Enter 'K' for kids section OR 'N' for adult only section");
					String section = read.readLine();
					partyList.add(partyList.size(),new Party(partyName, partySize, section.equals("K")
							?Location.KID_FRIENDLY_SECTION
									:Location.NO_KIDS_ALLOWED_SECTION ));
					nameTracking.add(nameTracking.size(), partyName);
					break;

				case 2:
					int linePosition = partyList.size()-1;
					Party currentParty = (Party) partyList.get(linePosition);
					Table currentTable = tableFinder(currentParty,kidFriendlySeating,adultOnlySeating);
					if(currentTable==null){
						for(int i = linePosition; i>=0 || currentTable!=null; i--){
							currentParty = (Party) partyList.get(linePosition);
							currentTable = tableFinder(currentParty,kidFriendlySeating,adultOnlySeating);
						}
					}
					if(currentTable!=null){
						currentTable.setCurrentParty(currentParty);
						partyList.remove(linePosition);
						occupiedTables.add(occupiedTables.size(), currentTable);
					}					
					System.out.println(currentTable==null
							?"There is currently no available seats for your party, sorry"
									:"Your party has been seated and removed from the line list");
					break;

				case 3: 
					System.out.println("Enter the name of the party to leave: ");
					String n = read.readLine();
					int totalOccupiedTables = occupiedTables.size();
					for(int i = 0; i<totalOccupiedTables; i++){
						Table cT = (Table)occupiedTables.get(i);
						String currentTablePartyName = cT.getCurrentParty().getName();
						if(currentTablePartyName.equals(n)){
							System.out.println("The party " + currentTablePartyName + 
									"has left the restaurant.");
							cT.setCurrentParty(null);
							occupiedTables.remove(i);
						}									
					}
					break;

				case 4:
					boolean dup = false;
					System.out.println("Enter the number of seats for your new table: " );
					int seatNumber = Integer.parseInt(read.readLine());
					System.out.println("Enter the section, 'N' for adult section OR " + 
							"'K' for the kids and adult section");
					String sectionFlag = read.readLine();
					Location loc = sectionFlag.equals("K")?Location.KID_FRIENDLY_SECTION:Location.NO_KIDS_ALLOWED_SECTION;					
					System.out.println("Enter the table number for your new table: ");
					int newTableNum = Integer.parseInt(read.readLine());
					Table newTable = new Table(seatNumber,newTableNum);
					if(loc.equals(Location.KID_FRIENDLY_SECTION)){
						boolean numCheck = checkTableNumber(tableNumbersKid, newTableNum);
						while(numCheck){
							System.out.println("Table number already exists, enter a new table number");
							newTableNum = Integer.parseInt(read.readLine());
							numCheck = checkTableNumber(tableNumbersKid, newTableNum);
						}
						tableNumbersKid.add(tableNumbersKid.size(), newTableNum);
						kidFriendlySeating.add(kidFriendlySeating.size(), new Table(seatNumber,newTableNum));
					}
					else{
						boolean numCheck = checkTableNumber(tableNumbersKid, newTableNum);
						while(numCheck){
							System.out.println("Table number already exists, enter a new table number");
							newTableNum = Integer.parseInt(read.readLine());
							numCheck = checkTableNumber(tableNumbersKid, newTableNum);
						}
						tableNumbersAdult.add(tableNumbersAdult.size(), newTableNum);
						adultOnlySeating.add(kidFriendlySeating.size(), new Table(seatNumber,newTableNum));
					}


					break;

				case 5:
					System.out.println("Enter the table number of the table you wish to remove: ");
					int tableNum = Integer.parseInt(read.readLine());
					break;
				}				
			}



			catch(IOException e){
				System.out.println("You enter some invalid input");
			}
		}
	}
}
