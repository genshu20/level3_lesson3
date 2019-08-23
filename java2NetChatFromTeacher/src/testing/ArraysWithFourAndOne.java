package testing;

public class ArraysWithFourAndOne {
    public int[]afterLastFour(int[]arr){
        for (int i = arr.length-1; i >=0 ; i--) {
            if (arr[i]==4){
                int[]newArr=new int[arr.length-i-1];
                System.arraycopy(arr,i+1,newArr,0,arr.length-i-1);
                return newArr;
            }
        }throw new RuntimeException("no four");
    }
   public boolean oneAndFour(int[]arr){
        boolean x=false;
        boolean y=false;
       for (int i = 0; i <arr.length ; i++) {
           if(arr[i]==1){
               x=true;
           }else if (arr[i] == 4){
               y = true;
           }else{
               return false;
           }
       }
       if(x&&y)return true;
       else return false;
   }
}
