import java.util.HashMap;


public class ElectionMap {

	private HashMap<String, ElectionResult> electionMap;
	public ElectionMap(){
		electionMap = new HashMap<String, ElectionResult>();
	}
	public ElectionResult get(String key){
		ElectionResult result = electionMap.get(key);
		if (result == null){
			String subStringName[] = key.split(" ");
			String tempKey = "";
			for (int j = 0; j< subStringName.length; j++){
				tempKey = tempKey + " " + subStringName[j];
				result = electionMap.get(tempKey.substring(1, tempKey.length()));
				if (result != null)
					return result;
			}
			String temp = key.toLowerCase();
			if (result == null){
				for(String k: electionMap.keySet()){
					if (temp.equals(k.toLowerCase())){
						result = electionMap.get(k);
						return result;
					}
				}
			}
		}
		return result;
	}
	public void put(String key, ElectionResult electionResult){
			String keys[] = key.split("-");
			for (int i = 0; i < keys.length; i++){
				electionMap.put(keys[i], electionResult);
			}
	}
}