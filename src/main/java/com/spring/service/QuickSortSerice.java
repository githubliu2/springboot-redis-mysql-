package com.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuickSortSerice {

	public <T> void quickSort(T[] t, int left, int right) {
		if (left >= right) {
			return ;
		}
		
		T temp = t[left];
		int i = left;
		int j = right;
		
		
		while (i < j) {
			if (temp.equals(t[j]) && i < j) j--;
		}
	}
	
	public <T> void quickSort(int[] t, int left, int right) {
		if (left >= right) {
			return ;
		}
		
		int temp = t[left];
		int i = left;
		int j = right;
		
		
		while (i < j) {
			while (t[j] >= temp && i < j) j--;
			while (t[i] <= temp && i < j) i++;
			if (i < j) {
				int num = t[i];
				t[i] = t[j];
				t[j] = num;
			}
		}
		
		t[left] = t[i];
		t[i] = temp;
		
		quickSort(t, left, i - 1);
		quickSort(t, i + 1, right);
	}
	
//	public static void main(String[] args) {
//		QuickSortSerice quickSortSerice = new QuickSortSerice();
//		 int[] arr = {9,2,7,9,5,1,6,5,3,1};
//		quickSortSerice.quickSort(arr, 0, arr.length-1);
//        for (int i = 0; i < arr.length; i++) {
//            System.out.println(arr[i]);
//        }
//	}
}
