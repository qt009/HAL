import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.exit;

public class HAL_OS {
    public static int noHALs;
    public ArrayList<Interpreter>HALs;
    public ArrayList<Buffer> buffers;
    public ArrayList<String> files;
    public ArrayList<Integer> list_IDs;
    public boolean hasEnded;


    public HAL_OS(){
        this.HALs = new ArrayList<Interpreter>();
        this.buffers = new ArrayList<Buffer>();
        this.files = new ArrayList<String>();
        this.list_IDs = new ArrayList<Integer>();
        noHALs = 0;
        hasEnded = false;
    }
    public void initHALs(int n){
        for (int i = 0; i < n ; i++){
            HALs.add(new Interpreter());
            HALs.get(i).file = this.files.get(i);
            HALs.get(i).ID = this.list_IDs.get(i);
        }
        HALs.get(0).isFirst = true;
        HALs.get(n-1).isLast = true;
    }

    public Interpreter get_with_ID(int ID){
        for (Interpreter i : HALs){
            if (i.ID == ID){
                return i;
            }
        }
        return null;
    }

    public void readFile(String file) {
        try {
            Scanner scanner = new Scanner(new File(file));
            if(scanner.nextLine().equals("HAL - Prozessoren :")){
                while(scanner.hasNextLine()){
                    if(!scanner.hasNextInt()){
                        break;
                    }
                    noHALs = Integer.parseInt(scanner.next());
                    this.list_IDs.add(noHALs);
                    this.files.add(scanner.next());
                }
                for( int ID : this.list_IDs){
                    if (ID == 0) noHALs +=1; // check if there's a '0' in indexes
                }
            }
            initHALs(noHALs);
            scanner.nextLine();
            if(scanner.nextLine().equals("HAL - Verbindungen :")){
                while(scanner.hasNextLine()){
                    //scanner.nextLine();
                    int[] nums = new int[4];
                    int count = 0;
                    while(scanner.hasNext()) {
                        if (scanner.hasNextInt()) {
                            nums[count] = Integer.parseInt(scanner.next());
                            count ++;
                        }
                        else scanner.next();
                        if (count == 4) break;

                    }

                    Buffer currentBuf = new Buffer();
                    currentBuf.IO_to_Buf=nums[1];
                    currentBuf.Buf_to_IO=nums[3];

                    this.get_with_ID(nums[0]).buffer.add(currentBuf);
                    this.get_with_ID(nums[2]).buffer.add(currentBuf);

                    currentBuf.setIptInterpreter(this.get_with_ID(nums[0]));
                    currentBuf.setOutInterpreter(this.get_with_ID(nums[2]));

                    buffers.add(currentBuf);


                }
            }

        }
        catch( FileNotFoundException e){
            System.out.println("Error: "+ e);
            e.printStackTrace();
            exit(1);
        }
    }


}

