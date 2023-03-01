import java.util.*;
import java.io.*;

import static java.lang.System.exit;

public class Interpreter extends Thread {
    public double ACC;
    public int PC;
    public ArrayList<Instruction> instructions;
    public double[] IO;

    public double[] registers;
    public boolean hasEnded;
    public boolean isFirst;
    public boolean isLast;
    public ArrayList<Buffer> buffer;
    public String file;
    public int ID;
    public double stdout;

    public Interpreter(){
        this.ACC=0;
        this.PC = 0;
        this.instructions = new ArrayList<Instruction>();
        this.buffer = new ArrayList<Buffer>();
        this.IO = new double[5];
        this.registers = new double[16];
        this.isFirst = false;
        this.isLast = false;
    }


    public void readFile(String file){
        try{
            Scanner scanner= new Scanner(new File(file));
            Instruction START= new Instruction();
            START.line_index= Integer.parseInt(scanner.next());
            START.Instruction_name = scanner.next();
            this.instructions.add(START);

            while(scanner.hasNextLine()){
                Instruction instruction= new Instruction();
                instruction.line_index=Integer.parseInt(scanner.next()); //get line
                instruction.Instruction_name=scanner.next();// get instruction name

                if(Objects.equals(instruction.Instruction_name, "STOP")){
                    this.instructions.add(instruction);
                    break;
                }

                if(scanner.hasNextInt()){
                    instruction.operand_int = Integer.parseInt(scanner.next());
                }
                else if(scanner.hasNext()){
                    instruction.operand_float = Double.parseDouble(scanner.next());
                }
                instruction.ParseNUM();
                this.instructions.add(instruction); //add instruction


                if(!scanner.hasNextLine()) break;
                scanner.nextLine();
            }
            scanner.close();

        } catch(FileNotFoundException e){
            System.out.println("Error occurred: "+ e);
            e.printStackTrace();
            exit(1);
        }
    }

    public void print_data(){
        int i=0;
        System.out.println("\n\n");
        for(double reg:this.registers){
            System.out.println("Register "+ i+": "+reg);
            i++;
        }
        System.out.println("\n\n");

        System.out.println("ACC: "+this.ACC);
        System.out.println("PC: "+this.PC);
        for (int j = 0; j< 5 ;j++){
            System.out.println("IO " + j +" : " + this.IO[j]);
        }
        System.out.println("stdout: " + this.stdout);

        System.out.println("\n\n");
    }
    public Instruction FETCH()
    {
        return this.instructions.get(this.PC);
    }

    /** build mode*/
    public void build()
    {
        System.out.println("-----Interpreter [" + ID + "]-----");
        while(!this.hasEnded)
        {
            this.exec(FETCH());
        }
        this.print_data();
    }

    /** debug mode*/
    public void debug()
    {
        while(!this.hasEnded)
        {
            System.out.println("Press s to step");
            Scanner s = new Scanner(System.in); // Step input
            if(s.next().equals("s")) //a step
            {
                this.exec(this.FETCH()); // execute instruction
                this.print_data(); // print all information
            }
            else
            {
                System.out.println("\nInvalid input. Press s to step!\n" );
            }
        }
    }
    public void run(){
        this.readFile(this.file);
        if(this.isFirst){
            this.build();
            Buffer firstBuf = this.buffer.get(0);
            firstBuf.put(this.IO[firstBuf.IO_to_Buf]);
        }
        else if(this.isLast){
            Buffer firstBuf = this.buffer.get(0);
            this.IO[firstBuf.Buf_to_IO] = firstBuf.get();
            this.build();
        }
        else{
            Buffer firstBuf = this.buffer.get(0);
            this.IO[firstBuf.Buf_to_IO] = firstBuf.get();
            this.build();
            Buffer secondBuf = this.buffer.get(1);
            secondBuf.put(this.IO[secondBuf.IO_to_Buf]);

        }

    }



    public void exec(Instruction instruction){
        this.PC++;
        if(instruction.operand_int != 0)
        {
            System.out.println( "CURRENT instruction: " + instruction.Instruction_name + " " + instruction.operand_int ); // print current instruction with int
        }
        else
        {
            System.out.println( "CURRENT instruction: " + instruction.Instruction_name + " " + instruction.operand_float ); //  print current instruction with float
        }

        switch(instruction.Instruction_name) // instruction set
        {
            case "START":
            {
                this.hasEnded = false;
                break;
            }
            case "STOP":
            {
                this.hasEnded = true;
                break;
            }
            case "OUT":
            {
                this.IO[instruction.operand_int] = this.ACC;
                break;
            }
            case "IN":
            {
                if(this.isFirst) {
                    System.out.print("Enter input number: ");
                    Scanner in = new Scanner(System.in);
                    System.out.print("\n");

                    this.IO[instruction.operand_int] = Double.parseDouble(in.next());
                }
                this.ACC = this.IO[instruction.operand_int];
                break;
            }
            case "LOAD":
            {
                this.ACC = this.registers[instruction.operand_int];
                break;
            }
            case "LOADNUM":
            {
                this.ACC = instruction.operand_float;
                break;
            }
            case "STORE":
            {
                this.registers[instruction.operand_int] = this.ACC;
                break;
            }
            case "JUMPNEG":
            {
                if(this.ACC < 0.0)
                {
                    this.PC = instruction.operand_int;
                }
                break;
            }
            case "JUMPPOS":
            {
                if(this.ACC >= 0.0)
                {
                    this.PC = instruction.operand_int;
                }
                break;
            }
            case "JUMPNULL":
            {
                if(this.ACC == 0.0)
                {
                    this.PC = instruction.operand_int;
                }
                break;
            }
            case "JUMP":
            {
                this.PC = instruction.operand_int;
                break;
            }
            case "ADD":
            {
                this.ACC += registers[instruction.operand_int];
                break;
            }
            case "ADDNUM":
            {
                this.ACC += instruction.operand_float;
                break;
            }
            case "SUB":
            {
                this.ACC -= registers[instruction.operand_int];
                break;
            }
            case "SUBNUM":
            {
                this.ACC -= instruction.operand_float;
                break;
            }
            case "MUL":
            {
                this.ACC *= registers[instruction.operand_int];
                break;
            }
            case "MULNUM":
            {
                this.ACC *= instruction.operand_float;
                break;
            }
            case "DIV":
            {
                this.ACC /= registers[instruction.operand_int];
                break;
            }
            case "DIVNUM":
            {
                this.ACC /= instruction.operand_float;
                break;
            }
            default:
            {
                break;
            }
        }
        this.stdout = this.IO[1];
    }
}
