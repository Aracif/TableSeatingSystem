import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.function.BiPredicate;


public class Driver{
	private static BiPredicate<String, String> nameEquality = (x,y) -> x.equals(y);


	private static boolean nameCheck(String name, ListArrayBasedPlus nameList){
		int listSize = nameList.size();
		for(int i = 0; i<listSize; i++){
			if(nameEquality.test(name, (String)nameList.get(i))){
				return true;
			}
		}
		return false;
	}

	private static int checkTableNumber(ArrayList<Integer> t, int num){
		int total = t.size();
		for(int i = 0; i<total; i++){
			if((Integer)t.get(i)==num){
				return -1;
			}
		}
		return num;
	}

	private static int[] sectionBuilder(ListArrayBasedPlus sec,  BufferedReader read, ArrayList<Integer> numList) throws NumberFormatException, IOException{
		System.out.println(">>Enter table number: ");			
		int numCheck = checkTableNumber(numList, Integer.parseInt(read.readLine()));
		while(numCheck==-1){
			System.out.println("This table already exists! Please enter another table number ");
			numCheck = checkTableNumber(numList, Integer.parseInt(read.readLine()));
		}
		System.out.println(">>Enter number of seats: ");
		int seatNumber = Integer.parseInt(read.readLine());
		return new int[]{seatNumber, numCheck};
	}

	private static Table tableFinder(ListArrayBasedPlus section, Party p){		
		for(int i = 0; i < section.size(); i++){
			Table currentTable = (Table)section.get(i);
			if(currentTable.getAvailableSeating()>=p.getSize()){
				System.out.println("Serving customer " + p.getName() + 
						"'s party of " + p.getSize() + " at table number " + 
						currentTable.getTableNumber() + "(" + currentTable.getAvailableSeating() + 
						" chairs)\n");
				return currentTable;
			}
		}
		return null;
	}




	public static void main(String[] args) throws NumberFormatException, IOException{

		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		//Storage ADT's
		ListCDLSBased lineList = new ListCDLSBased();
		ListCDLSBased occupiedTables = new ListCDLSBased();
		ListArrayBasedPlus kidsSection = new ListArrayBasedPlus();
		ListArrayBasedPlus adultSection = new ListArrayBasedPlus();
		ListArrayBasedPlus nameTracking = new ListArrayBasedPlus();
		ArrayList<Integer> kidNumbers = new ArrayList<Integer>();
		ArrayList<Integer> adultNumbers = new ArrayList<Integer>();
		
		kidsSection.add(0, new Table(4,1));
		kidsSection.add(1, new Table(6,2));
		adultSection.add(0, new Table(4,1));
		adultSection.add(1, new Table(6,2));
		lineList.add(0, new Party("Ficara",8,Location.KID_FRIENDLY));
		lineList.add(1, new Party("Jackson",4,Location.KID_FRIENDLY));

		//Get the initial input, this builds the seating in the restaurant.
		System.out.println("Enter your restaurant configuration: ");
		System.out.println(">>How many tables does your no-kids-allowed section have? ");
		int entries = Integer.parseInt(read.readLine());
		for(int i = 0; i<entries; i++){
			int[] newTableStats = sectionBuilder(adultSection,read,adultNumbers);
			adultSection.add(adultSection.size(), new Table(newTableStats[0], newTableStats[1]));
			adultNumbers.add(newTableStats[1]);
		}

		System.out.println(">>How many tables does your kid-friendly section have? ");
		entries = Integer.parseInt(read.readLine());
		for(int i = 0; i<entries; i++){
			int[] newTableStats = sectionBuilder(kidsSection,read,kidNumbers);
			kidsSection.add(kidsSection.size(), new Table(newTableStats[0],newTableStats[1]));
			kidNumbers.add(newTableStats[1]);
		}


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
					System.out.println(">>Enter customer name : ");
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
					System.out.println(">>Enter number of seats for customer : ");
					Integer partySize = Integer.parseInt(read.readLine());
					System.out.println(">>no-kids-allowed (Y/N)?");
					String section = read.readLine();
					lineList.add(lineList.size(),new Party(partyName, partySize, section.equals("N")
							?Location.KID_FRIENDLY
									:Location.NO_KIDS_ALLOWED ));
					nameTracking.add(nameTracking.size(), partyName);
					break;

				case 2:					
					boolean tableSetFlag = false;				
					for(int linePosition = 0; linePosition<lineList.size() && tableSetFlag==false; linePosition++){
						Party currentParty = (Party) lineList.get(linePosition);
						if(lineList.size()==0){
							System.out.println("No customers to serve!");
						}
						else if(currentParty.getSection()==Location.KID_FRIENDLY){
							Table t = tableFinder(kidsSection, currentParty);
							if(t!=null){
								occupiedTables.add(occupiedTables.size(), t);
								t.setCurrentParty(currentParty);
								tableSetFlag = true;
								lineList.remove(linePosition);
							}							
						}
						else{						
							Table t = tableFinder(kidsSection, currentParty);
							if(t!=null){
								occupiedTables.add(occupiedTables.size(), t);
								t.setCurrentParty(currentParty);
								tableSetFlag = true;
								lineList.remove(linePosition);
							}
						}
					}
					break;

				case 3:
					if(occupiedTables.size()==0){
						System.out.println("No customer is being served!");
					}
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
							for(int k = 0; i<nameTracking.size(); k++){
								String str = (String)nameTracking.get(k);
								if(str.equals(currentTablePartyName)){
									nameTracking.remove(k);
								}
							}
						}									
					}
					break;

				case 4:
					System.out.println(">>You are now adding a table.");
					System.out.println("To which section would you like to add this table?(K/N): ");					
					Location desiredSection = read.readLine().equals("K")?Location.KID_FRIENDLY:Location.NO_KIDS_ALLOWED;
					System.out.println(">>Enter table number: ");
					int tableNum = checkTableNumber(kidNumbers,Integer.parseInt(read.readLine()));
					while(tableNum==-1){
						System.out.println("This table already exists in " + desiredSection.toString() + " section! Please enter another table number");
						tableNum = checkTableNumber(kidNumbers,Integer.parseInt(read.readLine()));
					}
					System.out.println(">>Enter number of seats: ");
					int seatNum = Integer.parseInt(read.readLine());
					if(desiredSection==Location.KID_FRIENDLY){
						kidsSection.add(kidsSection.size(), new Table(seatNum, tableNum));
						kidNumbers.add(tableNum);
					}
					else{
						adultSection.add(adultSection.size(), new Table(seatNum, tableNum));
						adultNumbers.add(tableNum);
					}							
					break;

				case 5:
					System.out.println("Enter the table number of the table you wish to remove: ");
					int tableNumber = Integer.parseInt(read.readLine());
					break;

				case 6:
					int adultSectionSize = adultSection.size();
					int kidSectionSize = kidsSection.size();
					int occupiedKidTables = 0;
					int occupiedAdultTables = 0;
					for(int i = 0; i<occupiedTables.size(); i++){
						Party currPart = ((Table)occupiedTables.get(i)).getCurrentParty();
						if(currPart.getSection()==Location.KID_FRIENDLY){
							occupiedKidTables++;
						}
						else{
							occupiedAdultTables++;
						}
					}
					System.out.println("\tThe following " + (adultSectionSize-occupiedAdultTables) + " tables are available in the no-kids-allowed section:");
					for(int i = 0; i<adultSectionSize; i++){
						if(((Table)adultSection.get(i)).getCurrentParty()==null){
							System.out.println("\t\t"+adultSection.get(i));
						}
					}					
					System.out.println("\tThe following " + (kidSectionSize-occupiedKidTables) + " tables are available in the kid-friendly section:");
					for(int i = 0; i<kidSectionSize; i++){
						if(((Table)kidsSection.get(i)).getCurrentParty()==null){
							System.out.println("\t\t"+ kidsSection.get(i));
						}
					}
					System.out.println("\n");
					break;

				case 7:
					int lineSize = lineList.size();
					if(lineSize==0){
						System.out.println("\nNo customers are waiting for tables!\n");
					}
					else{

						System.out.println("\n\t->The following " + lineSize + " customer parties are waiting for tables: ");
						for(int i = 0; i<lineSize; i++){
							System.out.println("\t\t"+lineList.get(i));
						}
						System.out.println("\n");
					}
					break;

				case 8:
					int occupiedSize = occupiedTables.size();
					String s1 = "";
					String s2 = "";
					if(occupiedSize==0){
						System.out.println("\nNo customers are being served!\n");
					}
					else{
						for(int i = 0; i<occupiedSize; i++){
							Table currentTab = (Table)occupiedTables.get(i);
							Party currentPar = currentTab.getCurrentParty();
							if(currentPar.getSection()==Location.KID_FRIENDLY){
								s1 += currentPar.getName() + "'s party is seated at table " +
										+ currentTab.getTableNumber();
							}
							else{
								s2 += currentPar.getName() + "'s party is seated at table " +
										+ currentTab.getTableNumber();
							}							
						}
						System.out.println("\n");
						if(!s2.equals("")){
							System.out.println("\tThe following customer is being served in the no-kids-allowed section:");
							System.out.println("\t\t"+s2);
						}
						if(!s1.equals("")){
							System.out.println("\tThe following customer is being served in the kid-friendly section:");
							System.out.println("\t\t"+s1);
							System.out.println("\n");
						}
					}
					break;

				case 9:
					System.out.println("Goodbye");
					System.exit(0);
				}



			}



			catch(IOException e){
				System.out.println("You enter some invalid input");
			}
		}
	}
}
