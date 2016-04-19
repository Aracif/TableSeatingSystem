

public class Queue<T> implements QueueInterface<T> {

	protected T[] items;
	protected int front;
	protected int end;
	protected int numItems;

	public Queue(){

		items = (T[])new Object[3];
		front = 0;
		end = 0;
		numItems = 0;
	}


	public boolean isEmpty(){
		return (numItems==0);
	}


	public void enqueue(T newItem) throws QueueException{
		if(numItems>0 && numItems<items.length){
			end = (end+1)% items.length;
			items[end] = newItem;	
			
			numItems++;
		}
		else if(numItems==items.length){			
				resize(items);
				enqueue(newItem);
				
		}
		else{
			items[0] = newItem;
			numItems++;
		}
	}

	
	public T dequeue() throws QueueException{

		T currentFront = items[front];

		if(numItems>=2){
			items[front] = null;
			front = (front+1)%items.length;
			numItems--;
			return currentFront;
		}
		else if(numItems==1){
			numItems--;
			
			items[front] = null;
			return currentFront;
		}
		else{
			return null;
		}
	}


	public void dequeueAll(){
		items = (T[])new Object[10];
	}

	public T peek() throws QueueException{	
		T item = null;
		if(numItems >0){
			item = items[front];
		}
		return item;
	}


	public String toString(){
		String arrayContents = "";
		int frontVal = front;
		for(int i = 0;i<numItems; i++){
			if(items[frontVal]!=null){
			arrayContents += items[frontVal] + " ";
			frontVal = (frontVal+1)%items.length;
			}
		}	
		return arrayContents + "numItems : " + numItems;
	}

	protected void resize(T[] arr){
		T[] temp =  (T[])new Object[items.length + (items.length/2)+1];
		for(int i = 0; i<numItems; i++){
			temp[i] = items[front];
			front = (front+1)%items.length;
	}
		front = 0;
		end = numItems-1;
		items=temp;
	}


}