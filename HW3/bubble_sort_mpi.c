#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <assert.h>

#define N 100000

void swap(int *xp, int *yp) {
  int temp = *xp;
  *xp = *yp;
  *yp = temp;
}

void merge_swap(int A[], int B[], int world_size){
	int i;
	for(i = 0; i < N/world_size; i++) {
		int temp = A[i];
		A[i] = B[i];
		B[i] = temp;
	}
}

// A function to implement bubble sort
void bubbleSort(int arr[], int n) {
  int i, j;
  for (i = 0; i < n-1; i++)      
    for (j = 0; j < n-i-1; j++)
      if (arr[j] > arr[j+1])
		swap(&arr[j], &arr[j+1]);
}

int isSorted(int *a, int size) {
  int i;
  for (i = 0; i < size - 1; i++) {
    if (a[i] > a[i + 1]) {
      return 0;
    }
  }
  return 1;
}

// Function to print an array
void printArray(int arr[], int size) {
  int i;
  for (i=0; i < size; i++){
    	printf("%d ", arr[i]);
   }
  printf("\n");
}


int main(int argc, char** argv) {
	int i, n;
	int* A;
	clock_t start, end;
	double elapsed_time, t1, t2;

	MPI_Init(NULL, NULL);
	int world_rank;
	MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);
	int world_size;
	MPI_Comm_size(MPI_COMM_WORLD, &world_size);

	t1 = MPI_Wtime();
	if(world_rank == 0) {
		A = (int *)malloc(sizeof(int)*N);
		if (A == NULL) {
			printf("Fail to malloc\n");
			exit(0);
		}
		for (i=N-1; i>=0; i--)
			A[N-1-i] = i;
		
		if (isSorted(A, N))
		  printf("Array is sorted\n");
		else
		  printf("Array is NOT sorted\n");
	}

	//Scatter
	int *sub_A = (int *)malloc(sizeof(int) * N/world_size);
	assert(sub_A != NULL);


	MPI_Scatter(A, N/world_size, MPI_INT, sub_A, N/world_size, MPI_INT, 0, MPI_COMM_WORLD);


 // 	if(world_rank == 0){
	//  	printf("Rank: %d \n", world_rank);
	// 	printArray(&sub_A[0], 10);	
	// }

	bubbleSort(sub_A, N/world_size);
	printf("Rank: %d isSorted: %d\n", world_rank, isSorted(sub_A, N/world_size));
	// printArray(sub_A, N/world_size);

	//Gather
	
	int *result = NULL;
	if(world_rank == 0){
		result = (int *)malloc(sizeof(int) * N);
	}

	MPI_Gather(sub_A, N/world_size, MPI_INT, result, N/world_size, MPI_INT, 0, MPI_COMM_WORLD);

	if(world_rank == 0){
		// printArray(&result[0], N);
		for(i = 0; i < world_size/2; i++){
			merge_swap(&result[N/world_size*i], &result[N/world_size*(world_size-i-1)], world_size);
		}
		// printArray(&result[0], N);
		t2 = MPI_Wtime();
		printf( "Elapsed time MPI_Wtime is %f\n", t2 - t1 ); 
	}

	
	
	// printArray(&result[0], N);

	
	// printArray(&result[N-10], 10);

	// printArray(&A[N-10], 10);
	
	if (isSorted(A, N))
	  printf("Array is sorted\n");
	else
	  printf("Array is NOT sorted\n");
	 
	MPI_Finalize();
	return 0;
}