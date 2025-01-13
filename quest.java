import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;



public class Quest
{
	public static int maxX = 10;
	public static int maxY = 10;
	public static int maxItems = 25;
	public static int maxPlayers = 25;
	
	public static String mapFile = "map.csv";
	public static String playerFile = "players.csv";
	public static String itemFile = "items.csv";
	
	public static int itemCount = 0;
	public static int playerCount = 0;
	
	public static mapBlock[][] map = new mapBlock[maxX][maxY];
	public static Player p = new Player();  //the main player
	public static Item[] items = new Item[maxItems];
	public static Player[] npcs = new Player[maxPlayers];
	
	public static boolean playing = true;
	public static Scanner s = new Scanner(System.in);
	
	public static void main(String[] args)
	{
		initialize();
		int[] npcsHere = new int[maxPlayers];
		
		System.out.println("Welcome to The Jungle we got fun and games!");
		
		while(playing)
		{
			showMap();
			moveNpcs();
			npcsHere = showNpcs();
			if (npcsHere[0] > 0)
			{
				combat(npcsHere);
			}
			move(npcsHere);
			playing = !checkForWin();
				
		}
		if (p.isAlive())
		{
			System.out.println("Welcome back to the jungle.\nThe Game is your domain.");
		}
		else 
		{
			System.out.println("You f***ing lost.\nGet good loser");
		}
	}
	
	private static boolean checkForWin()
	{
		boolean win = false;
		//determine how to win from a win.csv file with xpos,ypos,itemIndex - item needed and reach certain point
		
		
		return win;
	}

	private static void combat(int[] npcsHere)
	{
		
		for(int i = 1; i <= npcsHere[0]; i++)
		{
			if(npcs[npcsHere[i]].combative > 0)
			{
				int npcRoll = rollForInitiative();
				int playerRoll = rollForInitiative();
				
				if (npcRoll > playerRoll)
				{
					//fight
					int hit = rollForHit();
					if (hit > (p.armor + armorTotal(p.items,p.itemCount)))
					{
						int damage = rollForDamage();
						damage += damageTotal(npcs[npcsHere[i]].items,npcs[npcsHere[i]].itemCount) + npcs[npcsHere[i]].strength;
						p.health = p.health - damage; 
						System.out.println(npcs[npcsHere[i]].title  + " hits with " + damage + " damage.  Your health now at " + p.health);
						if (!p.isAlive())
						{
							playing = false;
						}
					} else
					{
						System.out.println(" LOL YOU SUCK  " + npcs[npcsHere[i]].title + " try again");
					}
				}
			}
		}
	}
	
	private static void attack(int[] npcsHere)
	{
		
		for(int i = 1; i <= npcsHere[0]; i++)
		{
			if(npcs[npcsHere[i]].combative > 0)
			{

					int hit = rollForHit();
					if (hit > (npcs[npcsHere[i]].armor + armorTotal(npcs[npcsHere[i]].items,npcs[npcsHere[i]].itemCount)))
					{
						int damage = rollForDamage();
						damage += damageTotal(p.items,p.itemCount) + p.strength;
						npcs[npcsHere[i]].health = npcs[npcsHere[i]].health - damage; 
						System.out.println(p.title  + " hits " + npcs[npcsHere[i]].title + " with " + damage + " damage.  Health now at " + npcs[npcsHere[i]].health);
						if (!npcs[npcsHere[i]].isAlive())
						{
							//drop stuff
							drop(npcs[npcsHere[i]]);
							npcs[npcsHere[i]].title = "The body of " + npcs[npcsHere[i]].title;
						}
						
					} else
					{
						System.out.println(" LOL  " + p.title + " CAN'T FIGHT");
					}
			}
		}
	}


	private static int damageTotal(int[] items2, int itemCount2)
	{
		int total = 0;
		for (int i = 0; i < itemCount2; i++)
		{
			total += items[items2[i]].damage;
		}
		
		return total;
	}

	
	private static int armorTotal(int[] items2, int itemCount2)
	{
		int total = 0;
		for (int i = 0; i < itemCount2; i++)
		{
			total += items[items2[i]].armor;
		}
		
		return total;
	}

	private static int rollForDamage()
	{
		int roll =  getRand(1,6);
		return roll;
	}	
	
	private static int rollForHit()
	{
		int roll =  getRand(1,20);
		return roll;
	}

	
	private static int rollForInitiative()
	{
		int roll = getRand(1,20);
		return roll;
	
	}

	private static void moveNpcs()
	{
		for(int i = 1; i <= playerCount; i++)
		{
			if(npcs[i].moveable == 1)
			{
				//move this guy
				switch (getRand(1,4))
				{
				case 1:  //go north
				//	System.out.println(npcs[i].title + " tried their damnest to go north");
					if (wall(map[npcs[i].xpos][npcs[i].ypos],'n'))
					{
					//	System.out.println("LOL NO\n");
					} 
					else 
					{
						npcs[i].ypos -= 1;
					}
					break;
				case 2:  //go south
					//System.out.println(npcs[i].title + " viscerally attempts to go south");
					if (wall(map[npcs[i].xpos][npcs[i].ypos],'s'))
					{
					//	System.out.println("FAIL\n");
					} 
					else 
					{
						npcs[i].ypos += 1;
					}					
					break;
				case 3:  //go east
					//System.out.println(npcs[i].title + " wants to go east");
					if (wall(map[npcs[i].xpos][npcs[i].ypos],'e'))
					{
						System.out.println("Wonder if it'll work a second time\n");
						System.out.println("I guess not.\n");
					} 
					else 
					{
						npcs[i].xpos += 1;
					}					
					break;
				case 4:  //go west
					//System.out.println(npcs[i].title + " fades west");
					if (wall(map[npcs[i].xpos][npcs[i].ypos],'w'))
					{
						//System.out.println("Rejected\n");
					} 
					else 
					{
						npcs[i].xpos -= 1;
					}						
					break;
				default:
					System.out.println("I should get the hell out of here");
					break;
				}
			}
		}
	}
	
	public static int getRand(int min, int max)
	{
		if (min >= max)
		{
			System.out.println("Range Error: min greater than max");
			return -1;
		}
		
		int r = (int)(Math.random() * ((max - min) + 1)) + min;    //.32445768764468876545
		return r;
	}		

	private static int[] showNpcs()
	{
		int[] people = new int[maxPlayers];
		int pcount = 0;
		
		for(int i = 1; i <= playerCount; i++)
		{
			if((npcs[i].xpos == p.xpos) && (npcs[i].ypos == p.ypos))
			{
				System.out.println(npcs[i].title + " is here.");
				pcount++;
				people[pcount] = i;
			}
		}
		people[0] = pcount;
		return people;
	}

	public static void showMap()
	{
		System.out.println("\n" + map[p.xpos][p.ypos].title + "\n" + map[p.xpos][p.ypos].desc);
		//x,y for map block, 
		for(int idx = 0; idx < map[p.xpos][p.ypos].itemCount; idx++)
		{
			int itemIdx = map[p.xpos][p.ypos].itemsHere[idx];
			System.out.printf("There might be a/an %s here\n",items[itemIdx].title);
		}
	}
	
	public static boolean wall(mapBlock m, char direction)
	{
		switch(direction)
		{
		case 'n':
			if (m.n == 1)
				return true;
			
			break;
		case 's':
			if (m.s == 1)
				return true;
			
			break;
		case 'e':
			if (m.e == 1)
				return true;
			
			break;
		case 'w':
			if (m.w == 1)
				return true;
			
			break;			
		}
			
		return false;
	}
	
	public static int find(String itemName)
	{
		int index = 0;
		
		for (int i = 1; i <= itemCount; i ++)
		{
			if(itemName.equalsIgnoreCase(items[i].title))
			{
				index = i;
			}
		}
		
		return index;
	}

	public static int findByIndex(int idx, Player source) //find in player item list
	{
		int i = 0;
		boolean found = false;
		
		
		while (!found && (i < source.itemCount))
		{
			if (source.items[i] != idx)
			{
				i++;
			}
			else
			{
				found = true;
				return i;
			}
		}
		if (!found)
		{
			return -1;
		}

		return i;
	}	
	
	public static int findByIndex(int idx) //find in player item list
	{
		int i = 0;
		boolean found = false;
		
		
		while (!found && (i < p.itemCount))
		{
			if (p.items[i] != idx)
			{
				i++;
			}
			else
			{
				found = true;
				return i;
			}
		}
		if (!found)
		{
			return -1;
		}

		return i;
	}
	
	public static int findByIndex(int idx, int xpos, int ypos)  //find in map location
	{
		int i = 0;
		boolean found = false;
		
		
		while (!found && (i < map[xpos][ypos].itemCount))
		{
			if (map[xpos][ypos].itemsHere[i] != idx)
			{
				i++;
			}
			else
			{
				found = true;
				return i;
			}
		}
		if (!found)
		{
			return -1;
		}

		return i;
	}

	public static void remove(int idx)  //from player inventory
	{
		int mapStuffIdx = findByIndex(idx);
		
		if (mapStuffIdx >= 0)
		{
			p.items[mapStuffIdx] = p.items[p.itemCount -1];
			p.items[p.itemCount -1] = 0;
			p.itemCount--;
		}
		
	}	
	
	public static void remove(int idx, Player source)  //from player inventory
	{
		int mapStuffIdx = findByIndex(idx,source);
		
		if (mapStuffIdx >= 0)
		{
			source.items[mapStuffIdx] = source.items[source.itemCount -1];
			source.items[source.itemCount -1] = 0;
			source.itemCount--;
		}
		
	}
	
	public static void remove(int idx, int xpos, int ypos)  //from map block
	{
		int mapStuffIdx = findByIndex(idx,xpos,ypos);
		
		if (mapStuffIdx >= 0)
		{
			map[xpos][ypos].itemsHere[mapStuffIdx] = map[xpos][ypos].itemsHere[map[xpos][ypos].itemCount -1];
			map[xpos][ypos].itemsHere[map[xpos][ypos].itemCount -1] = 0;
			map[xpos][ypos].itemCount--;
		}
		
	}
	
	public static void drop (Player source, String[] data)
	{
		if (source.itemCount == 0)
		{
			System.out.println("Nothing to Drop. Are you even paying attention?");
		} 
		else
		{
			String stuff = data[1];
			
			//finds that it is a legal item
			int itemIdx = find(stuff);  
			
			//verify in inventory
			int itemCheck = findByIndex(itemIdx);
			
			if ((itemCheck >= 0) && (itemIdx > 0))
			{
				map[p.xpos][p.ypos].itemsHere[map[p.xpos][p.ypos].itemCount++] = itemIdx;
				System.out.println(source.title + " drops the " + stuff + ".");
				remove(itemIdx, source);
				
			}
			else
			{
				System.out.println("LOL THERE IS NO " + stuff + " HERE");
			}
		}
	}
	
	public static void drop (Player source)
	{

		for(int i = 0; i < source.itemCount; i++)
		{
			int itemIdx = source.items[i];		
		
			String stuff = items[source.items[i]].title;
		
	
				map[p.xpos][p.ypos].itemsHere[map[p.xpos][p.ypos].itemCount++] = itemIdx;
				System.out.println(source.title + " drops the " + stuff + ".");
				remove(itemIdx, source);

		}
			
			
	}
		
	public static void move(int[] npcsHere)
	{
		if(p.isAlive())
		{
		
			String command = "";
			System.out.print("<N S E W> ");
			String line = s.nextLine();
			
			String[] data;
			data = line.split(" ",2);
			command = data[0];
			
			switch (command)
			{
				case "help":
				case "Help":
				case "h":
				case "H":
					System.out.println("Help/List Commands: H");
					System.out.println("North: N");
					System.out.println("South: S");
					System.out.println("East: E");
					System.out.println("West: W");
					System.out.println("Attack: A");
					System.out.println("Inventory: I");
					System.out.println("Get: G");
					System.out.println("Drop: D");
					System.out.println("Take: T");

					break;

				case "attack":
				case "Attack":
				case "a":
				case "A":
					attack(npcsHere);
					
					break;
				case "inventory":
				case "Inventory":
				case "i":
				case "I":
					System.out.println("You are carrying:");
					for (int a = 0; a < p.itemCount; a++)
					{
						System.out.println(items[p.items[a]].title);
					}
					
					break;
				case "Get":
				case "get":
				case "g":
				case "G":
					if (map[p.xpos][p.ypos].itemCount == 0)
					{
						System.out.println("PAY ATTENTION");
					} 
					else
					{
						String stuff = data[1];
						
						//finds that it is a legal item
						int itemIdx = find(stuff);  
						
						//verify on map
						int itemCheck = findByIndex(itemIdx,p.xpos,p.ypos);
						
						if ((itemCheck >= 0) && (itemIdx > 0))
						{
							p.items[p.itemCount++] = itemIdx;
							System.out.println("You pick up the " + stuff + ".");
							remove(itemIdx, p.xpos, p.ypos);  // start here
							
						}
						else
						{
							System.out.println("LOL NO " + stuff + " HERE.");
						}
					}
					break;

				case "Take":
				case "take":
				case "t":
				case "T":
					if (map[p.xpos][p.ypos].itemCount == 0)
					{
						System.out.println("LOL FAIL");
					} 
					else
					{
						String stuff = data[1];
						
						//finds that it is a legal item
						int itemIdx = find(stuff);  
						
						//verify on map
						int itemCheck = findByIndex(itemIdx,p.xpos,p.ypos);
						
						if ((itemCheck >= 0) && (itemIdx > 0))
						{
							p.items[p.itemCount++] = itemIdx;
							System.out.println("You stole the " + stuff + ".");
							remove(itemIdx, p.xpos, p.ypos);  // start here
							
						}
						else
						{
							System.out.println("LOL NO " + stuff + " HERE.");
						}
					}
					break;
			
				case "Drop":
				case "drop":
				case "d":
				case "D":
					drop(p, data);
					
					break;
			
				case "N":
				case "n":
					if (wall(map[p.xpos][p.ypos],'n'))
					{
						System.out.println("NO\n");
					} 
					else 
					{
						p.ypos -= 1;
					}
					break;
				case "S":
				case "s":
					if (wall(map[p.xpos][p.ypos],'s'))
					{
						System.out.println("TRY AGAIN\n");
					} 
					else 
					{
						p.ypos += 1;
					}				
					break;
				case "E":
				case "e":
					if (wall(map[p.xpos][p.ypos],'e'))
					{
						System.out.println("LOL\n");
					} 
					else 
					{
						p.xpos += 1;
					}				
					break;		
				case "W":
				case "w":
					if (wall(map[p.xpos][p.ypos],'w'))
					{
						System.out.println("NOT TODAY\n");
					} 
					else 
					{
						p.xpos -= 1;
					}				
					break;
				default:
					break;	
			}
		}
	}
	
	public static void initialize()
	{
		String cfile = "";
		try 
		{
			//Map File
			cfile = mapFile;
			FileReader fr = new FileReader(mapFile);
			BufferedReader br = new BufferedReader(fr);
			
			String splitBy = ",";
			String line = br.readLine();
			
			while(line != null)
			{
				String[] data = line.split(splitBy);
				
				int xpos = Integer.parseInt(data[0]);
				int ypos = Integer.parseInt(data[1]);
				
				String t = data[2];
				String d = data[3];
				
				int n = Integer.parseInt(data[4]);
				int s = Integer.parseInt(data[5]);
				int e = Integer.parseInt(data[6]);
				int w = Integer.parseInt(data[7]);
				
				//System.out.println("Adding map block " + xpos + "," + ypos + " - " +t );
				
				map[xpos][ypos] = new mapBlock(t,d,n,s,e,w);
				
				
				line = br.readLine();
			}
			
			br.close();

			//Player File
			cfile = playerFile;
			fr = new FileReader(playerFile);
			br = new BufferedReader(fr);
			
			splitBy = ",";
			line = br.readLine();
			

				String[] data = line.split(splitBy);
				
				p.xpos = Integer.parseInt(data[0]);
				p.ypos = Integer.parseInt(data[1]);
				
				p.title = data[2];
				p.desc = data[3];
				//skip [4], [5]
				p.health = Integer.parseInt(data[6]);
				p.armor = Integer.parseInt(data[7]);	
				p.strength = Integer.parseInt(data[8]);
				p.dexterity = Integer.parseInt(data[9]);
				p.intelligence = Integer.parseInt(data[10]);
								
				line = br.readLine();
				
				while(line != null)  //npcs numbered 1-24 (maxPlayers -1)
				{
					playerCount++;
					npcs[playerCount] = new Player();
					
					data = line.split(splitBy);
					
					npcs[playerCount].xpos = Integer.parseInt(data[0]);
					npcs[playerCount].ypos = Integer.parseInt(data[1]);
					
					npcs[playerCount].title = data[2];
					npcs[playerCount].desc = data[3];
					npcs[playerCount].moveable = Integer.parseInt(data[4]);
					npcs[playerCount].combative = Integer.parseInt(data[5]);
					npcs[playerCount].health = Integer.parseInt(data[6]);
					npcs[playerCount].armor = Integer.parseInt(data[7]);	
					npcs[playerCount].strength = Integer.parseInt(data[8]);
					npcs[playerCount].dexterity = Integer.parseInt(data[9]);
					npcs[playerCount].intelligence = Integer.parseInt(data[10]);
					
					npcs[playerCount].items = new int[maxItems];
									
					
					line = br.readLine();

				//	System.out.println("Loaded npc " + playerCount + ": " + npcs[playerCount].title);
				}
			
			br.close();
			
			//Items File
			cfile = itemFile;
			fr = new FileReader(itemFile);
			br = new BufferedReader(fr);
			
			splitBy = ",";
			line = br.readLine();
			
			while(line != null)
			{
				data = line.split(splitBy);
				
				int xpos = Integer.parseInt(data[0]);
				int ypos = Integer.parseInt(data[1]);
				int heldFlag =  Integer.parseInt(data[0]);
				int npcID = Integer.parseInt(data[1]);
				
				String t = data[2];
				String d = data[3];
				
				itemCount++;//remember index 0 is blank, item 1 is index 1, etc
				items[itemCount] = new Item();
				items[itemCount].title = t;
				items[itemCount].description = d;
				items[itemCount].damage = Integer.parseInt(data[4]);
				items[itemCount].armor = Integer.parseInt(data[5]);
				
				//System.out.println("Adding " + t + " to map position " + xpos + "," + ypos); 
				
				if (heldFlag >= 0)
				{
					map[xpos][ypos].itemsHere[map[xpos][ypos].itemCount++] = itemCount;
				}
				else
				{
					npcs[npcID].items[npcs[npcID].itemCount] = itemCount;
					npcs[npcID].itemCount++;
				}
				//map[xpos][ypos] = new mapBlock(t,d,n,s,e,w);
				
				
				line = br.readLine();
			}
			
			br.close();			
			
			
		} 
		catch (IOException e)
		{
			System.out.println("I CAN'T READ " + cfile);
		}
	}
	
	
	
}
