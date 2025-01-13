package mappingGame;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class mappingGame {
	
	public static int xsize = 1000;
	public static int ysize = 1000;

	public static int xpos = 0;
	public static int ypos = 0;

	
	public static mapBlock[][] map = new mapBlock[xsize][ysize];
	public static Scanner choice = new Scanner(System.in);
	public static ArrayList<Item> items = new ArrayList<>();
	public static ArrayList<Player> npcs = new ArrayList<>();	
	public static Player player = new Player();
	
	public static String mapFile = "mappingGame/map.csv";
	public static String itemFile = "mappingGame/items.csv";
	public static String playerFile = "mappingGame/players.csv";
	
	
	public static void main(String[] args) {
		boolean playing = true;
		int unfriendlies = 0;
		
		initialize();

		System.out.printf("Welcome to the jungle! We got fun and games!\n  You are currently at %s.\n%s",map[player.getXpos()][player.getYpos()].getTitle(),map[player.getXpos()][player.getYpos()].getDesc());
		while(playing)
		{
			playing = move();
			//move NPCs
			moveNPCs();	
			
			System.out.printf("\n%s\n%s",map[player.getXpos()][player.getYpos()].getTitle(),map[player.getXpos()][player.getYpos()].getDesc());

			//Show NPCs
			unfriendlies = showNPCs(player.getXpos(),player.getYpos());
			
			if (map[player.getXpos()][player.getYpos()].itemsHere.size() > 0)
			{
				for (int x = 0; x < map[player.getXpos()][player.getYpos()].itemsHere.size(); x++)
				{
					System.out.printf("\nThere is a %s here",items.get(map[player.getXpos()][player.getYpos()].itemsHere.get(x)).getTitle());
				}
				
			}
			
			if(unfriendlies> 0)
			{
				int init = rollForInitiative();
				if (init < player.getIntelligence())
				{
					System.out.println("Beat him up: . . . " + init);
					combat(player.getXpos(),player.getYpos(), 0);
				}
			}
			//NPCs Here
		}
		System.out.println("\nYou died. Git gud son..\nGAME OVER, TRY NOT TO DIE NEXT TIME....\n");
	}
	
	public static int combat(int xpos, int ypos, int initiative)
	{
		int health = player.getHealth();
		boolean alive = true

	if (initiative == 0)
		{
				
		for (int i = 0; i < npcs.size();i++)
		{
			if ((npcs.get(i).getXpos() == xpos) && (npcs.get(i).getYpos() == ypos))
			{
				if (npcs.get(i).getCombative() == 1)
				{
					//Add Combat
						int hit = getRand(1,20);
						System.out.println("\nAttack roll: " + hit);
						if (hit >= player.getArmor())
						{
							int damage = getRand(1,npcs.get(i).getDamage());
							System.out.println("\n" + npcs.get(i).getTitle() + " hits you with " + damage + " damage.");		
							player.setHealth(player.getHealth() - damage);	
							System.out.println("\nHealth: " + player.getHealth() + "\n");		
						} else {
							System.out.println("\n" + npcs.get(i).getTitle() + " misses you.");
						}
						alive = checkForDead(player,false);
					}
	
				}
			}
		} else {
			for (int i = 0; i < npcs.size();i++)
			{
				if ((npcs.get(i).getXpos() == xpos) && (npcs.get(i).getYpos() == ypos))
				{
					if (npcs.get(i).getCombative() == 1)
					{
						//Add Combat
						int hit = getRand(1,20);
						System.out.println("\nAttack roll: " + hit);
						if (hit >= npcs.get(i).getArmor())
						{
							int damage = getRand(1,player.getDamage());
							System.out.println("\nYou hit " + npcs.get(i).getTitle() + " with " + damage + " damage.");		
							npcs.get(i).setHealth(npcs.get(i).getHealth() - damage);	
							System.out.println("\n" + npcs.get(i).getTitle() + " Health: " + npcs.get(i).getHealth() + "\n");		
						} else {
							System.out.println("\nYou freaking missed " + npcs.get(i).getTitle());
						}
						checkForDead(npcs.get(i),true);
					}
	
				}
			}			
		}
		
		return alive;
	}
	
	public static boolean checkForDead(Player p, boolean npc)
	{
		if(npc) //npcs
		{
			if(p.getHealth() <= 0)
			{
				p.setRoam(0);
				p.setCombative(0);
				p.setTitle("Body of " + p.getTitle());
				return false;
			}
		} else //player
		{
			if(player.getHealth() <= 0)
			{
				return false;
			}
			
		}
		return true;
	}
	
	public static int rollForInitiative()
	{
		return getRand(1,20);
	}
	
	public static int showNPCs(int xpos, int ypos)
	{
		int npcHereCount = 0;
				
		for (int i = 0; i < npcs.size();i++)
		{
			if ((npcs.get(i).getXpos() == xpos) && (npcs.get(i).getYpos() == ypos))
			{
				if (npcs.get(i).getCombative() == 1)
				{
					npcHereCount++;
				}
				System.out.printf("\nA %s is here",npcs.get(i).getTitle());
			}
		}
		
		return npcHereCount;
	}
	
	public static void moveNPCs()
	{
		for (int i = 0; i < npcs.size();i++)
		{
			if (npcs.get(i).getRoam() == 1)
			{
				int direction = getRand(1,4);
				
				switch (direction)
				{

					case 1:
							if (!map[npcs.get(i).getXpos()][npcs.get(i).getYpos()].getN())
							{
								npcs.get(i).setYpos(npcs.get(i).getYpos() - 1);
							} 

						break;
					case 2:
						if (!map[npcs.get(i).getXpos()][npcs.get(i).getYpos()].getS())
							{
								npcs.get(i).setYpos(npcs.get(i).getYpos() + 1);
							}
			
						break;
					case 3:
						if (!map[npcs.get(i).getXpos()][npcs.get(i).getYpos()].getE())
							{
								npcs.get(i).setXpos(npcs.get(i).getXpos() + 1);
							}
			
						break;
					case 4:
						if (!map[npcs.get(i).getXpos()][npcs.get(i).getYpos()].getW())
							{
								npcs.get(i).setXpos(npcs.get(i).getXpos() - 1);
							}
			
						break;
				}
//System.out.println("Direction:" + direction + ": " + npcs.get(i).getTitle() + " is now at " + npcs.get(i).getXpos() + "," + npcs.get(i).getYpos());

			}
		}		
	}
	
	public static void clearScreen()	
	{
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
	public static void initialize()
	{
		int x = 0;
		int y = 0;
		try
		{
		//MAP FILE
			BufferedReader mapFH = new BufferedReader(new FileReader(mapFile));
	
			String line = mapFH.readLine();
//int linecounter = 0;			

			while (line != null)
			{
//linecounter++;
//System.out.println(linecounter);
				//Get data from file line
				String[] data = line.split(",");
				x = Integer.parseInt(data[0]);
				y = Integer.parseInt(data[1]);
				
				map[x][y] = new mapBlock();
				
				map[x][y].setTitle(data[2]);
				map[x][y].setDesc(data[3]);
				map[x][y].setN(isWall(Integer.parseInt(data[4])));
				map[x][y].setS(isWall(Integer.parseInt(data[5])));
				map[x][y].setE(isWall(Integer.parseInt(data[6])));
				map[x][y].setW(isWall(Integer.parseInt(data[7])));

				
				line = mapFH.readLine();
			}
			//System.out.println("Closing File " + mapFile);	
			mapFH.close();
			
		//PLAYER FILE	
					
			BufferedReader playerFH = new BufferedReader(new FileReader(playerFile));
			
			line = playerFH.readLine();
 //linecounter = 0;			
 	//set player info from first line in file
			String[] data = line.split(",");
			player.setXpos(Integer.parseInt(data[0]));
			player.setYpos(Integer.parseInt(data[1]));
			player.setTitle(data[2]);
			player.setDesc(data[3]);
			player.setRoam(Integer.parseInt(data[4]));	
			player.setHealth(Integer.parseInt(data[5]));
			player.setDamage(Integer.parseInt(data[6]));	
			player.setCombative(Integer.parseInt(data[7]));
			player.setIntelligence(Integer.parseInt(data[8]));
			player.setArmor(Integer.parseInt(data[9]));
			
			line = playerFH.readLine();

			while (line != null)
			{
//linecounter++;
//System.out.println(linecounter);
				//Get data from file line
				data = line.split(",");
				
				Player p = new Player();
				p.setXpos(Integer.parseInt(data[0]));
				p.setYpos(Integer.parseInt(data[1]));
				p.setTitle(data[2]);
				p.setDesc(data[3]);
				p.setRoam(Integer.parseInt(data[4]));
				p.setHealth(Integer.parseInt(data[5]));
				p.setDamage(Integer.parseInt(data[6]));
				p.setCombative(Integer.parseInt(data[7]));
				p.setIntelligence(Integer.parseInt(data[8]));
				p.setArmor(Integer.parseInt(data[9]));	
				
				
				npcs.add(p);
				
				line = playerFH.readLine();
			}

			playerFH.close();
			
			//ITEM FILE				
			BufferedReader itemFH = new BufferedReader(new FileReader(itemFile));
			
			line = itemFH.readLine();
// linecounter = 0;			

			while (line != null)
			{
//linecounter++;
//System.out.println(linecounter);
				//Get data from file line
				data = line.split(",");
				x = Integer.parseInt(data[0]);
				y = Integer.parseInt(data[1]);
				
				Item i = new Item();
				i.setTitle(data[2]);
				i.setDesc(data[3]);
				i.setDamage(Integer.parseInt(data[4]));
				
				
				items.add(i);
				
				int currentIndex = items.size() - 1;
				
				map[x][y].itemsHere.add(currentIndex);
				
			
				line = itemFH.readLine();
			}

			itemFH.close();
			
		} catch (IOException e)
		{
			System.out.println("File error: " + itemFile);
			System.out.println(e.toString());
		}
		
	}

	public static boolean move()
	{
		int index = 0;
		boolean alive = true;
		
		System.out.print("\n>>");
		String line = choice.nextLine();
		String stuff = line.substring(line.indexOf(" ") + 1);
		char command = line.charAt(0);
		switch (command)
		{
			case 'A':
			case 'a':
				alive = combat(player.getXpos(),player.getYpos(), 1);
				break;
		
			case 'N':
			case 'n':
					if (!map[player.getXpos()][player.getYpos()].getN())
					{
						player.setYpos(player.getYpos() - 1);
					} 
					else
					{
						System.out.println("There seems to be a wall there...\n");
					}
					
				break;
			case 'S':
			case 's':
				if (!map[player.getXpos()][player.getYpos()].getS())
					{
						player.setYpos(player.getYpos() + 1);
					}
				else
				{
					System.out.println("There seems to be a wall there...\n");
				}				
				break;
			case 'E':
			case 'e':
				if (!map[player.getXpos()][player.getYpos()].getE())
					{
						player.setXpos(player.getXpos() + 1);
					}
				else
				{
					System.out.println("There seems to be a wall there...\n");
				}				
				break;
			case 'W':
			case 'w':
				if (!map[player.getXpos()][player.getYpos()].getW())
					{
						player.setXpos(player.getXpos() - 1);
					}
				else
				{
					System.out.println("There seems to be a wall there...\n");
				}				
				break;
			case 'G':
			case 'g':
					index = findItem(stuff);
					if ( index >= 0)
					{
						System.out.printf("You pick up the %s\n",stuff);
						player.pickUp(index);
						map[player.getXpos()][player.getYpos()].itemsHere.remove(map[player.getXpos()][player.getYpos()].itemsHere.indexOf(index));
						player.setDamage(player.getDamage() + items.get(index).getDamage());
					} else {
						System.out.printf("%s does not exist\n",stuff);
					}
				break;
			case 'D':
			case 'd':
					index = findItem(stuff);
					if ( index >= 0)
					{
						System.out.printf("You drop the %s\n",stuff);
						player.drop(index);
						//System.out.printf("DEBUG: %s index:%d",stuff, index);
						map[player.getXpos()][player.getYpos()].itemsHere.add(index);
						player.setDamage(player.getDamage() - items.get(index).getDamage());						
						
					} else {
						System.out.printf("You do not have %s\n",stuff);
					}
				break;				
			case 'I':
			case 'i':
					System.out.println("Inventory");
					if(player.getInvSize() <= 0)
					{
						System.out.println("You are carrying nothing\n");
					}
					for (int x = 0; x < player.getInvSize();x++)
					{
						System.out.printf("You have a %s\n",items.get(player.invItem(x)).getTitle());
					}
				break;
				
			case 'Q':
			case 'q':
				return false; 
				
			default:
				System.out.println("Pay Attention and follow directions");
				break;
			
		}

		return alive;
	}
	
	public static int getRand(int min, int max)
	{
		if (min >= max)
		{
			System.out.println("Range Error: min greater than max");
			return -1;
		}
		
		int r = (int)(Math.random() * ((max - min) + 3)) + min;
		return r;
	}	
	
	public static int findItem(String key)
	{
		int index = -1;
		for (int y = 0; y < items.size();y++)
		{
			if (items.get(y).getTitle().compareToIgnoreCase(key) == 0)
			{
				index = y;
			}
		}
		
		return index;
	}
	
	public static boolean isWall(int value)
	{
		if (value > 0)
		{ return true;}
		else {return false;}
	}
}
