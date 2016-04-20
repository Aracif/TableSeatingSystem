import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @author Sal Ficara
 * @version 2.0
 * 
 * 
 * This application constructs a simplified restaurant seating system, that
 * uses several list ADT's that were constructed during labs this semester for
 * its data structures.
 *
 */
public class Driver{

	/**
	 * Check for duplicate names
	 * 
	 * @param name A String object representing a party name
	 * @param nameList A ListArrayBasedPlus ADT structure
	 * @return boolean true if the name is found false otherwise
	 */
	private static boolean nameCheck(String name, ListArrayBasedPlus nameList){
		int listSize = nameList.size();
		
		for(int i = 0; i<listSize; i++){
			if(name.equals((String)nameList.get(i))){
				return true;
			}
		}
		return false;
	}

	/**
	 * Find the index where a specified table number is located
	 * 
	 * @param t A MyListReferenceBased ADT structure
	 * @param num An int representing a table number
	 * @return if a match is found return -1 otherwise return the num
	 */
	private static int checkTableNumber(MyListReferenceBased t, int num){
		int total = t.size();
		for(int i = 0; i<total; i++){
			if((Integer)t.get(i)==num){
				return -1;
			}
		}
		return num;
	}

	/**
	 * Uses a section list and a number list to determine the proper parameters
	 * needed to successfully construct a new Table object. These two values are 
	 * returned via an int array. This method also has built in error checking to 
	 * ensure that no duplicate table numbers are made.
	 * 
	 * @param sec A ListArrayBasedPlus ADT structure representing either kid or adult section
	 * @param read A buffered reader object
	 * @param numList A MyListReferenceBased ADT structure containing table numbers from one of the sections
	 * @return int[] return int array containing the number of seats in position 1 and the table number in position 2
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private static int[] sectionBuilder(ListArrayBasedPlus sec,  BufferedReader read, MyListReferenceBased numList) throws NumberFormatException, IOException{
		
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

	/**
	 * Used to locate the tables the are not currently occupied so a party can
	 * be seated.
	 * 
	 * 
	 * @param section A ListArrayBasedPlus ADT structure of either the kid or adult section
	 * @param p A party object, needed to get the size of current party
	 * @return boolean of value true in a proper table is found and null otherwise
	 */
	private static Table tableFinder(ListArrayBasedPlus section, Party p){		
		for(int i = 0; i < section.size(); i++){
			Table currentTable = (Table)section.get(i);
			if(currentTable.getAvailableSeating()>=p.getSize() && currentTable.getCurrentParty()==null){
				System.out.println("Serving customer " + p.getName() + 
						"'s party of " + p.getSize() + " at table number " + 
						currentTable.getTableNumber() + "(" + currentTable.getAvailableSeating() + 
						" chairs)\n");
				return currentTable;
			}
		}
		return null;
	}

	/**
	 * Used to locate a proper table to remove from a specified section.
	 * 
	 * @param sec A ListArrayBasedPlus ADT of either the kid or adult section
	 * @param tableNum A int representing the table we want to remove
	 * @return Integer value that returns a proper index of a table that can be removed, 
	 * 			or -2 if a table is found but is currently occupied, or -1 if it does not exits at all
	 * 			
	 */
	private static Integer tableRemover(ListArrayBasedPlus sec, int tableNum){
		int sectionSize = sec.size();
		Integer flag = -1;
		boolean found = false;
		for(int i = 0; i<sectionSize && found==false; i++){
			Table currentTable = (Table)sec.get(i);
			if(currentTable.getTableNumber()==tableNum && currentTable.getCurrentParty()==null){
				flag = i;		
				found = true;
			}
			else if(currentTable.getTableNumber()==tableNum && currentTable.getCurrentParty()!=null){
				flag = -2;
				found = true;
			}
		}
		return flag;
	}

	/**
	 * Used to gather information on what tables are currently unused or in terms
	 * that this program is written in it locates tables that have a currentParty field
	 * that is equal to null.
	 *  
	 * @param section ListArrayBasedPlus ADT structure that is either a kid or adult section list
	 * @param sectionName name of the section, used in the string thats printed to console
	 * @return String that tells you information on what seats are currently available
	 */
	private static String calculateAvailableTables(ListArrayBasedPlus section, String sectionName){
		String s ="";
		int finds = 0;
		if(section.numItems==0){
			return "No available seating";
		}
		else{
			for(int i = 0; i<section.numItems; i++){
				Party currentParty = ((Table)section.get(i)).getCurrentParty();
				if(currentParty==null){
					s += "\t" + ((Table)section.get(i)).toString()+"\n";									
				}
				else{
					finds++;
				}
			}
		}
		String tableGrammar = section.size()-finds>1?"tables are":"table is";
		return "The following " + (section.size()- finds) +" " + tableGrammar + " available in the " +
		sectionName + ":\n" + s;
	}

	/**
	 * Used to locate table numbers.
	 * 
	 * @param numList MyListReferenceBased ADT structure that hold either table numbers for the kid or adult section
	 * @param num The number we are searching for
	 * @return The index where the number was found if successful otherwise returns null
	 */
	private static Integer numberFind(MyListReferenceBased numList, Integer num){
		Integer foundInt = null;
		for(int i = 0; i<numList.size(); i++){
			Integer currentNum = (Integer)numList.get(i);
			if(currentNum.equals(num)){
				foundInt = i;
			}
		}
		return foundInt;
	}

	/**
	 * In its current state the main method uses 7 list ADT structures. 4 of them are 
	 * of type MyListReferenceBased and the other 3 are of type ListArrayBasedPlus
	 * below is a brief description of each list's purpose,
	 * 		lineList	  -> Holds Party objects, this ADT simulates a line, first come first serve.
	 * 		occupiedTables-> Holds Table objects, which all have a Party object assigned to 
	 * 						 the class field "currentParty" effectively linking a particular
	 * 						 party to the Table.
	 * 		kidsSection   -> Contains Table objects that are only in the kid section of the
	 * 						 restaurant. Note, when tables are assigned they still remain in this
	 * 						 array, the display method(case 6) just ignores tables that have a
	 * 						 non-null currentParty field. So these tables remain in this list
	 * 						 permanently UNLESS option 5 is used to remove a table.
	 * 		adultSection  -> Contains Table objects that are only in the adult section of the
	 * 						 restaurant. Everything else is identical to the kidsSection 
	 * 						 description.
	 * 		nameTracking  -> Holds String objects. The purpose of this is to easily access names
	 * 						 of all the current parties in the restaurant so that we can easily
	 * 						 check for duplicates.
	 * 		kidNumbers	  -> Holds Integer objects. The purpose of this is to easily access all
	 * 						 tables numbers in the kid section so that we can easily check for 
	 * 						 duplicate table numbers.
	 * 		adultNumbers  -> Serves the exact same purpose as about but for the adult tables 
	 * 						 instead.
	 * As for the cases themselves, it is self explanatory from the selection menu what they do.
	 * All code inside the cases is generally using the private static methods above to 
	 * accomplish the goal of whichever case you are looking at.
	 * @param args
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static void main(String[] args) throws NumberFormatException, IOException{

		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

		MyListReferenceBased  lineList = new MyListReferenceBased ();
		MyListReferenceBased  occupiedTables = new MyListReferenceBased ();
		ListArrayBasedPlus kidsSection = new ListArrayBasedPlus();
		ListArrayBasedPlus adultSection = new ListArrayBasedPlus();
		ListArrayBasedPlus nameTracking = new ListArrayBasedPlus();
		MyListReferenceBased kidNumbers = new MyListReferenceBased();
		MyListReferenceBased adultNumbers = new MyListReferenceBased();

		//		kidsSection.add(0, new Table(4,1));
		//		kidsSection.add(1, new Table(6,2));
		//		kidsSection.add(2, new Table(10,3));
		//		adultSection.add(0, new Table(4,1));
		//		adultSection.add(1, new Table(6,2));
		//		adultSection.add(2, new Table(8,3));
		//		adultNumbers.add(0,1);
		//		adultNumbers.add(1,2);
		//		adultNumbers.add(2,3);
		//		kidNumbers.add(0,1);
		//		kidNumbers.add(1,2);
		//		kidNumbers.add(2,3);
		//		lineList.add(0,new Party("Dexter",6,Location.NO_KIDS_ALLOWED) );
		//		lineList.add(1, new Party("Jackson",8,Location.KID_FRIENDLY));
		//		lineList.add(2, new Party("Smith",6,Location.KID_FRIENDLY));
		//		lineList.add(3, new Party("Ficara",4,Location.KID_FRIENDLY));
		//		lineList.add(4, new Party("Gates",2,Location.NO_KIDS_ALLOWED));
		//		lineList.add(5, new Party("Zuckerberg",6,Location.NO_KIDS_ALLOWED));
		//		nameTracking.add(0, "Ficara");
		//		nameTracking.add(1, "Jackson");
		//		nameTracking.add(2, "Smith");
		//		nameTracking.add(3, "Dexter");
		//		nameTracking.add(4, "Gates");
		//		nameTracking.add(5, "Zuckerberg");


		//Get the initial input, this builds the seating in the restaurant.
		System.out.println("Enter your restaurant configuration: ");
		System.out.println(">>How many tables does your no-kids-allowed section have? ");
		int entries = Integer.parseInt(read.readLine());
		for(int i = 0; i<entries; i++){
			int[] newTableStats = sectionBuilder(adultSection,read,adultNumbers);
			adultSection.add(adultSection.size(), new Table(newTableStats[0], newTableStats[1]));
			adultNumbers.add(adultNumbers.size(),newTableStats[1]);
		}

		System.out.println(">>How many tables does your kid-friendly section have? ");
		entries = Integer.parseInt(read.readLine());
		for(int i = 0; i<entries; i++){
			int[] newTableStats = sectionBuilder(kidsSection,read,kidNumbers);
			kidsSection.add(kidsSection.size(), new Table(newTableStats[0],newTableStats[1]));
			kidNumbers.add(kidNumbers.size(),newTableStats[1]);
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
					if(lineList.size()==0){
						System.out.println("There is no one in line right now");
					}
					else{
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
								Table t = tableFinder(adultSection, currentParty);
								if(t!=null){
									occupiedTables.add(occupiedTables.size(), t);
									t.setCurrentParty(currentParty);
									tableSetFlag = true;
									lineList.remove(linePosition);
								}
							}
						}
						if(tableSetFlag==false){
							System.out.println("There was no available seating for this party\n");
						}
					}
					break;

				case 3:
					boolean found = false;
					int totalOccupiedTables = occupiedTables.size();
					if(totalOccupiedTables==0){
						System.out.println("No customer is being served!");
					}
					else{
						System.out.println("Enter the name of the party to leave: ");
						String n = read.readLine();				
						for(int i = 0; i<totalOccupiedTables && found==false; i++){
							Table cT = (Table)occupiedTables.get(i);
							String currentTablePartyName = cT.getCurrentParty().getName();
							if(currentTablePartyName.equals(n)){
								System.out.println("The party " + currentTablePartyName + 
										" has left the restaurant.");
								cT.setCurrentParty(null);
								occupiedTables.remove(i);
								for(int k = 0; k<nameTracking.size(); k++){
									String str = (String)nameTracking.get(k);
									if(str.equals(currentTablePartyName)){
										nameTracking.remove(k);
										found=true;
									}
								}
							}									
						}
					}
					break;

				case 4:
					System.out.println(">>You are now adding a table.");
					System.out.println("To which section would you like to add this table?(K/N): ");					
					Location desiredSection = read.readLine().equals("K")?Location.KID_FRIENDLY:Location.NO_KIDS_ALLOWED;				
					if(desiredSection==Location.KID_FRIENDLY){
						int[] newTableStats = sectionBuilder(kidsSection,read,kidNumbers);
						kidsSection.add(kidsSection.size(), new Table(newTableStats[0],newTableStats[1]));
						kidNumbers.add(kidNumbers.size(),newTableStats[1]);
					}
					else{
						int[] newTableStats = sectionBuilder(adultSection,read,adultNumbers);
						adultSection.add(adultSection.size(), new Table(newTableStats[0], newTableStats[1]));
						adultNumbers.add(adultNumbers.size(),newTableStats[1]);
					}												
					break;

				case 5:
					Integer flag = null;
					System.out.println(">>You are now removing a table. ");
					System.out.println(" From which section would you like to remove this table?(K/N): ");
					String sec = read.readLine();
					Location selectedLocation = sec.equals("K")?Location.KID_FRIENDLY:Location.NO_KIDS_ALLOWED;
					System.out.println(">>Enter table number: ");
					Integer tableNum = Integer.parseInt(read.readLine());
					if(selectedLocation==Location.KID_FRIENDLY){					
						flag = tableRemover(kidsSection,tableNum);
						if(flag>=0){
							kidNumbers.remove(numberFind(kidNumbers, tableNum));
							kidsSection.remove(flag);
							System.out.println("Table " + tableNum + " has been removed from " + "kid-friendly section.");
						}
						else if(flag==-2){
							System.out.println("Can't remove a table that is currently in use!");
						}
						else{
							System.out.println("This table doesn't exist in the kid-friendly section! Please enter another table number");
						}
					}
					else{
						flag = tableRemover(adultSection, tableNum);
						if(flag>=0){
							adultNumbers.remove(numberFind(adultNumbers, tableNum));
							adultSection.remove(flag);
							System.out.println("Table " + tableNum + " has been removed from " + "adult-only section.");
						}
						else if(flag==-2){
							System.out.println("Can't remove a table that is currently in use!");
						}
						else{
							System.out.println("This table doesn't exist in the adult-only section! Please enter another table number");
						}
					}
					break;

				case 6:
					String kidsAvailableSeating = calculateAvailableTables(kidsSection, "kid-friendly section");
					String adultssAvailableSeating = calculateAvailableTables(adultSection, "no-kids-allowed section");
					System.out.println(adultssAvailableSeating);
					System.out.println(kidsAvailableSeating);
					break;

				case 7:
					int lineSize = lineList.size();
					if(lineSize==0){
						System.out.println("\nNo customers are waiting for tables!\n");
					}
					else{

						System.out.println("\n\tThe following " + lineSize + " customer parties are waiting for tables: ");
						for(int i = 0; i<lineSize; i++){
							System.out.println("\t -> "+lineList.get(i));
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
								s1 += "\t  ->" + currentPar.getName() + "'s party is seated at table " +
										+ currentTab.getTableNumber()+"\n";
							}
							else{
								s2 +="\t  ->" + currentPar.getName() + "'s party is seated at table " +
										+ currentTab.getTableNumber()+"\n";
							}							
						}
						System.out.println("\n");
						if(!s2.equals("")){
							System.out.println("\tThe following customers are being served in the no-kids-allowed section:");
							System.out.println(s2);
						}
						if(!s1.equals("")){
							System.out.println("\tThe following customers are being served in the kid-friendly section:");
							System.out.println(s1);
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
