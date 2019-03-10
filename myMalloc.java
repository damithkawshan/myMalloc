import java.nio.*;



public class myMalloc{
	
	static int size = 25;
	static byte[] memory = new byte[size];
	public static void main(String [] args){

		System.out.println("-- Note --");
		System.out.println("Header uses 4 bytes.");
		System.out.println("1st byte for flag :");
		System.out.println("-1 for free block , -2 for allocated block.");
		System.out.println("");

		//Initializing Memory
        

         for (int c = 0; c < size; c++) {
           memory[c] = 0;
        }
		

        memory[0] = -1;

        String bin = String.format("%21s", Integer.toBinaryString(size-4)).replace(' ', '0');
        //System.out.println(bin);
        String fs = bin.substring(0,7);
        String ss = bin.substring(7,14);
        String ts = bin.substring(14,21);
        int first_set = Integer.parseInt(bin.substring(0,7), 2);
        int second_set = Integer.parseInt(bin.substring(7,14), 2);
        int third_set = Integer.parseInt(bin.substring(14,21), 2);
        int whole = Integer.parseInt(fs+ss+ts, 2);
        
        memory[1] =(byte)first_set;
        memory[2] = (byte)second_set;
        memory[3] = (byte)third_set;
		
		
	//Memory Allocation Section


		allocate(5);
		allocate(2);
		allocate(8);
		free(2);
		allocate(8);
		allocate(1);


     for (int c = 0; c < size; c++) {
     		if(memory[c] == -1){
     			String mem = Integer.toString(getActualMem(c+1));
     			int range = c +Integer.parseInt(mem);
     			System.out.println("-- Free block : size: " + mem + " bytes ---");
     			System.out.println("Header index :" + (c-4));
     			System.out.println("Staring index :" + c);
     			System.out.println("Ending index :" + (range-1) +"\n");

     			
     		}
     		else{
     			if(memory[c] == -2){

     			String mem = Integer.toString(getActualMem(c+1));
     			int range = c +Integer.parseInt(mem);
     			System.out.println("-- Allocated block : size: " + mem + " bytes ---");
     			System.out.println("Header index :" + (c-4));
     			System.out.println("Staring index :" + c);
     			System.out.println("Ending index :" + (range-1)+ "\n");
     		}
     			
     		}
     		
            
        }
        //View Memory in bytes
        //Comment Out only to display starting & ending index
       showMemory();




	}


	public static void allocate(int amount){
		int most_suitable = size;
		int most_index = 1;
		if(amount>=size){
			System.out.println("Memory Exceeded!");
		}
		else{ 
		for(int i=0;i<size-4;i++){
			if(memory[i]== -1){
				if(getActualMem(i+1) == amount ){
					System.out.println("Free ideal block found!");
					most_suitable = getActualMem(i+1);
					most_index = i;
					break;
				}
				else{
					if((getActualMem(i+1) > amount) && (most_suitable>getActualMem(i+1)))
					{
						most_suitable = getActualMem(i+1);
						most_index = i;
					}
				}
				
			}
		}	
		if(most_suitable == size){
			System.out.println("No ideal block found !");
		}
		else{
				memory[most_index] = -2;
					setBinaryBytes(amount,most_index+1);
					alloc(most_index+4,amount);
		}
				


}
	}
	public static void alloc(int index,int amt){
		for(int o=index; o < index+amt ; o++){
			memory[o] = 1;
		}
		if(memory[index+amt] != -2){
			memory[index+amt] = -1;
			setHeader(index+amt+1);
		}

	}

	public static void free(int si){
		int cur = 0;
		for (int c = 0; c <= size-4; c++) {
     		if((memory[c] == -2) && (getActualMem(c+1)==si)){
     			memory[c] = -1;
     			cur = c;
     			setBinaryBytes(si+mergeDown(c+1),c+1);
     			for(int o=c+4; o < c+4+si ; o++){
					memory[o] = 0;
					}
				int y =mergeUp(cur-1);
			break;
     		}

     		
            
        }

        


	}

	public static void setHeader(int index){
		int si = 1;
		int in = index;
		while(memory[in]!=-2){
			if(in == size-1){
				break;
			}
			si +=1;
			in+=1;
			
			
		}
		setBinaryBytes(si-3,index);
	}

	public static int getActualMem(int index){

		String bin_1 = String.format("%7s", Integer.toBinaryString(memory[index])).replace(' ', '0');
		String bin_2 = String.format("%7s", Integer.toBinaryString(memory[index+1])).replace(' ', '0');
		String bin_3 = String.format("%7s", Integer.toBinaryString(memory[index+2])).replace(' ', '0');
		int mem = Integer.parseInt(bin_1+bin_2+bin_3, 2);
		return mem;

	}

	public static void setBinaryBytes(int si,int index){
		String bin = String.format("%21s", Integer.toBinaryString(si)).replace(' ', '0');
        //System.out.println(bin);
        String fs = bin.substring(0,7);
        String ss = bin.substring(7,14);
        String ts = bin.substring(14,21);
        int first_set = Integer.parseInt(bin.substring(0,7), 2);
        int second_set = Integer.parseInt(bin.substring(7,14), 2);
        int third_set = Integer.parseInt(bin.substring(14,21), 2);
        int whole = Integer.parseInt(fs+ss+ts, 2);
        
        memory[index] =(byte)first_set;
        memory[index+1] = (byte)second_set;
        memory[index+2] = (byte)third_set;
	}
	

	public static int mergeDown(int ind){
		//Merging down block
		int count = 0;
        for (int y = ind; y <= size-4; y++) {
        	if(memory[y]==-2){
return 0;
        	}
        	if(memory[y] == -1){
        		memory[y] = 0;
        		
        		for(int o=y+4; o < y+4+getActualMem(y+1) ; o++){
        			if(o==size){
        				break;
        			}
        			count+=1;
					memory[o] = 0;
					}
					setBinaryBytes(0,y+1);

        	}
        	
	}
	return count+4;
}


public static int mergeUp(int ind){
		//Merging down block
		int inde = ind;
		int act = getActualMem(ind+2);
		
        for (int y = ind; y >= 0; y--) {
        	if(memory[y]==-2){
				return 0;
        	}
        	if(memory[y] == -1){
        		memory[inde+1] = 0;
        		setBinaryBytes(0,inde+2);
        		setBinaryBytes(act+getActualMem(y+1)+4,y+1);
        	
	}
	
}
return 0;
}

public static void showMemory(){
	for (int c = 0; c < size; c++) {
if(memory[c] == -1 || memory[c] == -2)
	System.out.println();
     		System.out.print(" " + memory[c]);
            
        }
        System.out.println(" ");
}
	




}