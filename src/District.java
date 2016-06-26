public class District {
	private String state;
	private String district;
	private float totalVote;
	public District(String state, String district, float totalVote){
		this.state = state;
		this.district = district;
		this.totalVote = totalVote;
	}

	public String getState(){
		return state;
	}
	public String getDistrict(){
		return district;
	}

	public float getTotalVote(){
		return totalVote;
	}
}