public class ElectionResult {
	private float democrats;
	private float republicans;
	private float others;
	private float total;

	public ElectionResult(String republicans, String democrats, String others){
		this.democrats = Float.parseFloat(democrats);
		this.republicans = Float.parseFloat(republicans);
		this.others = Float.parseFloat(others);
		this.total = this.democrats + this.republicans + this.others;
		
	}
	public float getDemocrats(){
		return democrats;
	}
	public float getRepublicans(){
		return republicans;
	}
	public float getOthers(){
		return others;
	}
	public float getTotal(){
		return total;
	}
}