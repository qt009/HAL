public class Instruction {
    public int line_index;
    public String Instruction_name;
    public int operand_int;
    public double operand_float;

    public Instruction(){
        this.line_index=0;
        this.Instruction_name="null";
        this.operand_int=0;
        this.operand_float=0;
    }

    public void ParseNUM(){
        switch(this.Instruction_name){
            case "LOADNUM":
            case "ADDNUM":
            case "SUBNUM":
            case "MULNUM":
            case "DIVNUM":
            {
                if(this.operand_int != 0) {
                    this.operand_float = this.operand_int;
                    break;
                }
            }
            default:
            {
                break;
            }
        }
    }


}

