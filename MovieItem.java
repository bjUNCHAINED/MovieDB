// Byunghyun Jeon || byunghyun.jeon@gmail.com || July 2014
//
// MovieItem.java - A movie pending purchase, with ID and quantity
public class MovieItem {
	String mId;
	int quantity;
	
	public MovieItem(String mId, int quantity){
		this.mId = mId;
		this.quantity = quantity;
	}
	
	public String getId(){
		return mId;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}
}
