package com.spring.util;
import java.util.LinkedList;

public class AlogrithUtil {

	//找出该数组中满足其和 ≥ target 的长度最小的 连续 子数组
	public static void slowWindow() {
		int []  nums = {2,3,1,2,4,3};
		int target = 7;
		
		
		int left = 0;
		int right = 0;
		
		int sum  = 0; //滑动窗口内的和
		//int windowLength  = 0; //滑动窗口大小
		int minLength = Integer.MAX_VALUE;
		for (right = 0; right < nums.length; right++) {//滑动窗口右边增加
			sum = sum + nums[right];
			//windowLength ++;
			while (sum >= target) {//尝试缩小窗口大小
				// 先取长度
				int curLength = right - left + 1;
				if (curLength < minLength) {
					minLength = curLength;
				}
				
				
				sum = sum - nums[left];
				left++;
				//windowLength--;
			}
			
		}
		
		System.out.println(minLength == Integer.MAX_VALUE ? -1 : minLength);
	}

	/**
	 * 题目：
	 * 滑动窗口最大值 -- leetcode 239
	 * 
	 * 题目描述：
	 * 
	给定一个数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口 k 内的数字。滑动窗口每次只向右移动一位。

	返回滑动窗口最大值。

	示例:

	输入: nums = [1,3,-1,-3,5,3,6,7], 和 k = 3
	输出: [3,3,5,5,6,7] 
	解释: 

	  滑动窗口的位置                最大值
	---------------               -----
	[1  3  -1] -3  5  3  6  7       3
	 1 [3  -1  -3] 5  3  6  7       3
	 1  3 [-1  -3  5] 3  6  7       5
	 1  3  -1 [-3  5  3] 6  7       5
	 1  3  -1  -3 [5  3  6] 7       6
	 1  3  -1  -3  5 [3  6  7]      7
	注意：

	你可以假设 k 总是有效的，1 ≤ k ≤ 输入数组的大小，且输入数组不为空。

	进阶：

	你能在线性时间复杂度内解决此题吗？

	    /**
	     * 思路：
	     * 1、双端队列, 最大值放在队列头部
	     * 2、如果一个值比它前面的值要大，那么它前面的值就永远不可能成为最大值
	     * 2、因为当前元素比前面的元素大，则当前的窗口最大值为当前元素，否则为窗口中目前的最大值
	     */
	    public int[] maxSlidingWindow(int[] nums, int k) {
	        if (nums == null || nums.length <= 0 || k <= 0) {
	            return new int[0];
	        }
	        int len = nums.length;
	        // k有效不需要校验k
	        int[] res = new int[len - k + 1];
	        
	        // 双端队列
	        LinkedList<Integer> cache = new LinkedList<Integer>();
	        
	        for (int i=0; i<len; i++) {
	            // 添加元素到队列，保证队列递增,比当前元素小的都弹出
	            while (!cache.isEmpty() && nums[cache.peekLast()] < nums[i]) {
	                // 队列中加入元素索引
	                cache.removeLast();
	            }
	            // 队列中加入元素索引
	            cache.addLast(i);
	            // 需要移除队列中过期的元素
	            if (i - cache.peekFirst() >= k) {
	                cache.removeFirst();
	            }
	            // 如果队列中i>=k-1, 记录当前队列中的最大值
	            if (i >= k - 1) {
	                res[i-k+1] = nums[cache.peekFirst()];
	            }
	        }
	        
	        return res;
	    }
	    
	    public static void main(String[] args)
	    {
	    	//slowWindow();
	        int[] nums = {1,3,-1,-3,5,3,6,7}; 
	        int k = 3;
	        System.out.println(new AlogrithUtil().maxSlidingWindow(nums, k));
//	        int[] nums1 = {1,-1}; 
//	        int k1 = 1;
//	        System.out.println(new AlogrithUtil().maxSlidingWindow(nums1, k1));
	    }



}
