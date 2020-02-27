#include <stdio.h>
#include <stdlib.h>

void SortBinaryArrayInPlace(int A[], int N) {
	int trueCount = 0;
	for (int i = 0; i < N; i++) {
		if (A[i] > 0) {
			trueCount++;
		}
	}
	for (int i = N - 1; i > -1; i--) {
		A[i] = trueCount-- > 0;
	}
}

void printA(int a[], int n) {
	for (int i = 0; i < n; i++) {
		printf("%d ", a[i]);
	}
	printf("\n");
}

void main() {
	int a[9];
	a[0]=0;
	a[1]=1;
	a[2]=0;
	a[3]=0;
	a[4]=1;
	a[5]=1;
	a[6]=0;
	a[7]=0;
	a[8]=1;
	int n = (int) sizeof(a) / (int) sizeof(a[0]);
	printf("Length: %d\n", n);
	printf("Unsorted: ");
	printA(a, n);
	SortBinaryArrayInPlace(a, n);
	printf("Sorted:   ");
	printA(a, n);
}

