//-------------------------------------------------------------------------------------------
//  Project: SIMMAC OS Problem Set
//  Name: Karran Gowda
//  Professor: Gregory Simco
//  Date: 09/28/2020
// Analysis: I got this program to work with no compiler errors. This is the fourth version of
// this program. I ran into a problem with reading int values. The num array can only read two-
// digit numbers. I first read the text file associated with this project called simmacprog1.txt.
// Then I use an opcode method to find specific jobs/instructions. I get a job and find the CPU
// execution time of it. I get the largest CPU time of all the jobs and use that for the priority
// scheduler.This program has three priority schedulers with different percentages of the CPU time.
//-------------------------------------------------------------------------------------------
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;


public class simmac {
	//The instructions of the individual 32-bit words of memory  (psiar + 212)
	public static String[] text = new String[212];
			
	//512 32-bit words of memory (psiar + 212)
	public static char[] letter = new char[212];
	public static char[] letter2 = new char[212];
	public static char[] letter3 = new char[212];
			
	//public static String[] memory = new String[512];
			
	//The values of the individual 32-bit words of memory  (psiar + 212)
	public static String[] value = new String[212];
			
	//The int values of the individual 32-bit words of memory  (psiar + 212)
	public static int[] num = new int[212];
	
	//Stores the amount of time for each of the instructions to execute.
	public static int[] extime = new int[212];
	
	//Stores the amount of time for each of the instructions to execute.
	public static long[] executiontime = new long[212];
	
	//Total amount of cpu time.
	public static int cputime;
	
	//Counts the number of times an instruction is read from the text file.
	//Should have the total number of instructions.
	public static int programcounter;
			
	//The Control Storage Instruction Address Register that points to the location of the next micro-instruction (in control storage) to be executed.
	public static int csiar;
			
	//The Storage Address Register that points to the location in primary storage of the next machine language instruction to executed.
	public static int sar;
	
	//The Instruction Register that contains the current instruction being executed.
	public static int ir;
			
	//The Primary Storage Instruction Address Register that points to the location in primary storage of the next machine language instruction to be executed.
	public static int psiar;
			
	//The Storage Data Register that holds the data being written to or receives the data being read from primary storage at the location specified in the SAR.
	public static int sdr;
			
	//The Accumulator that contains one of the operands in each arithmetic operation while the others are in primary storage.
	public static int acc;
			
	//The Temporary Register that extracts the address portion of the machine instruction in the SDR so that it may be placed in the SAR.
	public static int temp;
			
		//Constructor where the main variables are instantiated and the methods are called.
		public static void simmac()
		{
				// Initializes the registers
				sar = 0;
				psiar = 300;
				sdr = 0;
				acc = 0;
				temp = 0;
				initsystem();
				loadsystemprograms();
				getCPU();
				sar = 0;
				psiar = 300;
				sdr = 0;
				acc = 0;
				temp = 0;
				
				initsystem();
				loadsystemprograms();
				runlevel0();
		}
			
		//initsystem initializes the memory addresses.
		public static void initsystem()
		{
				//All the memory addresses are instantiated.
				for (int i = 0; i < 212; i++)
				{
					letter[i] = ' ';
					letter2[i] = ' ';
					letter3[i] = ' ';
					value[i] = " ";
					num[i] = 0;
				}
		}
			
		//loadsystemprograms reads from the text file "simmacprog1.txt" and gets all the contents.
		//It then transfers them to strings and int values for the methods to use.
		public static void loadsystemprograms()
		{
				//The counter for the memory addresses.
				programcounter = 0;
				try {
					  //Reads from the text file.
				      File myObj = new File("simmacprog1.txt");
				      //Stores the file object to the variable myReader.
				      Scanner myReader = new Scanner(myObj);
				      //Goes through line by line in the variable.
				      while (myReader.hasNextLine()) {
				    	//Stores each line in the file to the variable data.
				        String data = myReader.nextLine();
				        //The entire line is stored in a String array called text.
				        text[programcounter] = data;
				        //The String array memory contains all the lines in the file.
				        //memory[psiar+programcounter] = text[programcounter].substring(0,4);
				        letter[programcounter] = text[programcounter].charAt(0);
				        letter2[programcounter] = text[programcounter].charAt(1);
				        letter3[programcounter] = text[programcounter].charAt(2);
				        //The number that is contained in the variable sdr.
				        value[programcounter] = text[programcounter].substring(4,6);
					    num[programcounter] = Integer.parseInt(value[programcounter]);
					    //System.out.println(num[programcounter]);
				        programcounter++;
				        //System.out.println(data);
				      }
				      myReader.close();
				    } catch (FileNotFoundException e) {
				      System.out.println("An error occurred.");
				      e.printStackTrace();
				    }
				
		}
			
		//Gets the overall CPU time of all the instructions.
		public static void getCPU()
		{
				//Goes through the memory address and runs the instructions.
				for (int i = 0; i < 212; i++)
				{
					//int code contains the numerical value of the instruction to get executed.
					int code = opcode(letter[i],letter2[i],letter3[i]);
					//The Instruction Register method is called so that sdr is set to the value of the instruction.
					instr(i);
					if (code == 1)
					{
						//Gets the time before executing the instruction.
						long startTime = System.nanoTime();
						loadi();
						//Gets the overall time.
						long endTime = System.nanoTime();
						//Gets the amount of time for the execution of the instruction by
						//subtracting the time before the method calling by the overall time.
						extime[i]=(int) (endTime-startTime);
						executiontime[i]= endTime-startTime;
					}
					else if (code == 2)
					{
						long startTime = System.nanoTime();
						store(i);
						long endTime = System.nanoTime();
						extime[i]=(int) (endTime-startTime);
						executiontime[i]= endTime-startTime;
					}
					else if (code == 3)
					{
						long startTime = System.nanoTime();
						add(i);
						long endTime = System.nanoTime();
						extime[i]=(int) (endTime-startTime);
						executiontime[i]= endTime-startTime;
					}
					else if (code == 4)
					{
						long startTime = System.nanoTime();
						sub(i);
						long endTime = System.nanoTime();
						extime[i]=(int) (endTime-startTime);
						executiontime[i]= endTime-startTime;
					}
					else if (code == 5)
					{
						long startTime = System.nanoTime();
						load(i);
						long endTime = System.nanoTime();
						extime[i]=(int) (endTime-startTime);
						executiontime[i]= endTime-startTime;
					}
					else if (code == 6)
					{
						long startTime = System.nanoTime();
						cond();
						long endTime = System.nanoTime();
						extime[i]=(int) (endTime-startTime);
						executiontime[i]= endTime-startTime;
					}
				}
				System.out.println();
				//Puts the largest value of time at the end of the array.
				Arrays.sort(extime);
				//Gets the largest value to be the CPU time and puts it in the variable cputime.
				int max = extime[extime.length-1];
				cputime = max;
				
		}
			
			//This Level 0 scheduler gets 20% of the CPU time.
			//It is a first come, first serve scheduler.
			public static void runlevel0()
			{
				System.out.println("This virtual machine is called SIMMAC. It uses 512 32-bit words of memory.");
				System.out.println("The lines of code on the text file start at the 300th memory address.");
				System.out.println("Six SIMMAC machine language programs are used in this program(ADD, SUB, BRH, STR, LDI,and LDA).");
				System.out.println("Each line of code in the text file is a job. All of the jobs are run by a scheduler.");
				System.out.println("There are three schedulers to choose from in the Java file. They each use a certain percentage of the CPU time to run.");
				System.out.println("After a job is picked by the opcode, there is a job switch.");
				System.out.println("Five registers will be used in the job switches.");
				System.out.println("The run # indicates every iteration of the jobs.");
				System.out.println("The final values of these registers are printed after all the runs of the program are completed.");
			for (int j = 0; j < 212; j++)
			{
				System.out.println("Run #" + (j+1));
				//Goes through the memory address and runs the instructions.
				for (int i = 0; i < 212; i++)
				{
					//int code contains the numerical value of the instruction to get executed.
					int code = opcode(letter[i],letter2[i],letter3[i]);
					//The value of the instruction is stored in the Storage Data Register.
					instr(i);
					if (code == 1)
					{
						loadi();
						//Level 0 gets 20% of the cpu time.
						executiontime[i] = (int) (executiontime[i] - (cputime*0.20));
						System.out.println("Load Immediate");
					}
					else if (code == 2)
					{
						store(i);
						executiontime[i] = (int) (executiontime[i] - (cputime*0.20));
						System.out.println("Store");
					}
					else if (code == 3)
					{
						add(i);
						executiontime[i] = (int) (executiontime[i] - (cputime*0.20));
						System.out.println("Add");
					}
					else if (code == 4)
					{
						sub(i);
						executiontime[i] = (int) (executiontime[i] - (cputime*0.20));
						System.out.println("Subtract");
					}
					else if (code == 5)
					{
						load(i);
						executiontime[i] = (int) (executiontime[i] - (cputime*0.20));
						System.out.println("Load");
					}
					else if (code == 6)
					{
						cond();
						executiontime[i] = (int) (executiontime[i] - (cputime*0.20));
						System.out.println("Branch");
					}
					else if (code == 7)
					{
						//System.out.println("End of Job");
					}
				}
			}
				System.out.println("End of Job");
			}
			
			//This Level 1 Scheduler gets 30% of the CPU time.
			//This is a round robin scheduler.
			public static void runlevel1()
			{
				for (int j = 0; j < 212; j++)
				{
				//Goes through the memory addresses and runs the instructions.
				for (int i = 0; i < 212; i++)
				{
					if(executiontime[i]>0)
					{
					//int code contains the numerical value of the instruction to get executed.
					int code = opcode(letter[i],letter2[i],letter3[i]);
					//The Instruction Register method is called so that sdr is set to the value of the instruction.
					instr(i);
					if (code == 1)
					{
						loadi();
						//Level 1 gets 30% of the cpu time.
						executiontime[i] = (int) (executiontime[i] - (cputime*0.30));
						System.out.println("Load");
					}
					else if (code == 2)
					{
						store(i);
						executiontime[i] = (int) (executiontime[i] - (cputime*0.30));
						System.out.println("Store");
					}
					else if (code == 3)
					{
						add(i);
						executiontime[i] = (int) (executiontime[i] - (cputime*0.30));
						System.out.println("Add");
					}
					else if (code == 4)
					{
						sub(i);
						executiontime[i] = (int) (executiontime[i] - (cputime*0.30));
						System.out.println("Subtract");
					}
					else if (code == 5)
					{
						load(i);
						executiontime[i] = (int) (executiontime[i] - (cputime*0.30));
						System.out.println("Load");
					}
					else if (code == 6)
					{
						cond();
						executiontime[i] = (int) (executiontime[i] - (cputime*0.30));
						System.out.println("Branch");
					}
					else if (code == 7)
					{
						//System.out.println("End of Job");
					}
				}
			}
				
				}
				System.out.println("End of Job");
			}
			
			//This Level 2 scheduler gets 50% of the CPU time.
			//This is a round robin scheduler.
			public static void runlevel2()
			{
				for (int j = 0; j < 212; j++)
				{
				//Goes through the memory addresses and runs the instructions.
				for (int i = 0; i < 212; i++)
				{
					if(executiontime[i]>0)
					{
					//int code contains the numerical value of the instruction to get executed.
					int code = opcode(letter[i],letter2[i],letter3[i]);
					//The Instruction Register method is called so that sdr is set to the value of the instruction.
					instr(i);
					if (code == 1)
					{
						loadi();
						//Level 2 gets 50% of the cpu time.
						executiontime[i] = (int) (executiontime[i] - (cputime*0.50));
						System.out.println("Load");
					}
					else if (code == 2)
					{
						store(i);
						executiontime[i] = (int) (executiontime[i] - (cputime*0.50));
						System.out.println("Store");
					}
					else if (code == 3)
					{
						add(i);
						executiontime[i] = (int) (executiontime[i] - (cputime*0.50));
						System.out.println("Add");
					}
					else if (code == 4)
					{
						sub(i);
						executiontime[i] = (int) (executiontime[i] - (cputime*0.50));
						System.out.println("Subtract");
					}
					else if (code == 5)
					{
						load(i);
						executiontime[i] = (int) (executiontime[i] - (cputime*0.50));
						System.out.println("Load");
					}
					else if (code == 6)
					{
						cond();
						executiontime[i] = (int) (executiontime[i] - (cputime*0.50));
						System.out.println("Branch");
					}
					else if (code == 7)
					{
						//System.out.println("End of Job");
					}
				}
			}
				}
				System.out.println("End of Job");
			}
			
			//The add method adds a value to the accumulator.
			public static void add(int loc)
			{
				temp = acc;
				acc = psiar + 1;
				psiar = acc;
				acc = temp;
				temp = sdr;
				sar = temp;
				sdr = num[loc];
				temp = sdr;
				acc = acc + temp;
				csiar = 0;
			}
			
			//The instruction fetch called at the beginning of the run methods.
			public static void instr(int loc)
			{
				sar = psiar;
				sdr = num[loc];
				ir = sdr;
			}
			
			
			//The sub method subtracts a value from the accumulator.
			public static void sub(int loc)
			{
				temp = acc;
				acc = psiar + 1;
				psiar = acc;
				acc = temp;
				temp = sdr;
				sar = temp;
				sdr = num[loc];
				temp = sdr;
				acc = acc - temp;
				csiar = 0;
			}
			//The load method loads a value from the accumulator.
			public static void load(int loc)
			{
				temp = acc;
				acc = psiar + 1;
				psiar = acc;
				acc = temp;
				temp = sdr;
				sar = temp;
				sdr = num[loc];
				acc = sdr;
				csiar = 0;
			}
			//The store method stores a value from the accumulator.
			public static void store(int loc)
			{
				temp = acc;
				acc = psiar + 1;
				psiar = acc;
				acc = temp;
				temp = sdr;
				sar = temp;
				sdr = acc;
				num[loc] = sdr;
				csiar = 0;
			}
			//The cond method sets the accumulator to be zero.
			public static void cond()
			{
				csiar = 64;
				psiar = sdr;
				csiar = 0;
				temp = acc;
				acc = psiar + 1;
				psiar = acc;
				acc = temp;
				csiar = 0;
			}
			//The loadi method uses a number to be the value to load in to the accumulator.
			public static void loadi()
			{
				acc = psiar + 1;
				psiar = acc;
				acc = sdr;
				csiar = 0;
			}
			//Determines what instruction to be executed next according to the first three letters.
			public static int opcode(char letter, char letter2, char letter3) {
				
				if (letter == 'L' && letter2 == 'D' && letter3 == 'I')
				{
				return 1;
				}
				else if (letter == 'S' && letter2 == 'T' && letter3 == 'R')
				{
				return 2;
				}
				else if (letter == 'A' && letter2 == 'D' && letter3 == 'D')
				{
				return 3;
				}
				else if (letter == 'S' && letter2 == 'U' && letter3 == 'B')
				{
				return 4;
				}
				else if (letter == 'L' && letter2 == 'D' && letter3 == 'A')
				{
				return 5;
				}
				else if (letter == 'B' && letter2 == 'R' && letter3 == 'H')
				{
				return 6;
				}
				else if (letter == 'H' && letter2 == 'A' && letter3 == 'L')
				{
				return 7;
				}
				else
				{
				return -1;
				}
			}
			
			//The main method that executes the constructor and
			//prints the final values for the accumulator, 
			public static void main (String[] args)
			{
				simmac();
				System.out.println("(Accumulator)ACC:" + acc);
				System.out.println("(Primary Storage Instruction Address Register)PSIAR:" + psiar);
				System.out.println("(Storage Address Register)SAR:" + sar);
				System.out.println("(Storage Data Register)SDR:" + sdr);
				System.out.println("(Temporary Register)TMPR:" + temp);
				
			}
}

