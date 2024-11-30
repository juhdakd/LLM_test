class Solution {
    public boolean canAliceWin(int[] nums) {
        // 判断是否是个位数
        int count1 = 0;
        int count2 = 0;
        for(int i=0;i<nums.length;i++){
            if(nums[i]<10){
                count1+=nums[i];
            }else{
                count2+=nums[i];
            }
        }
        if(count1==count2){
            return false;
        }
        return true;
    }
}