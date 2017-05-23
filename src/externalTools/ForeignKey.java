package externalTools;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ForeignKey {
	private Map<Attribute, Attribute> references;

	public ForeignKey() {
		references = new HashMap<Attribute, Attribute>();
	}
	
	public void addReference(Attribute orig, Attribute ref) {
		references.put(orig, ref);
	}
	
	public boolean containsKey(Attribute attr){
		if (references.containsKey(attr)) {
			return true;
		}
		return false;
	}
	
	public Attribute getRef(Attribute attr) {
		return references.get(attr);
	}
	
	public String toString() {
		String buff = new String();
		buff = "Foreign Key: ";
		for (Map.Entry<Attribute, Attribute> entry : this.references.entrySet()) {
			Attribute or = entry.getKey();
			Attribute re = entry.getValue();
			buff += or.toString() + " -> " + re.toString() + "\n";
		}
		buff += "\n";
		return buff;
	}
	
	public Map<Attribute, Attribute> getRefs() {
		return references;
	}

	public boolean isEqual(ForeignKey fk) {
		if (this.references.size() == fk.references.size()) {
			// TODO check if keys are equal
			return false;
		} else {
			return false;
		}
	}
	
	public Set<Map.Entry<Attribute, Attribute>> getForeignKeys()
	{
		return references.entrySet();
	}
	
	/*public ArrayList<model.ForeignKey> getForeingKeys(){
		
		ArrayList<model.ForeignKey> foreingKeysOfVersion  = new ArrayList<model.ForeignKey>();
		
		for (Map.Entry<Attribute, Attribute> entry : this.references.entrySet()) {
			Attribute or = entry.getKey();
			Attribute re = entry.getValue();
			model.ForeignKey fk=new model.ForeignKey(or.getTable().getName(),re.getTable().getName());
			foreingKeysOfVersion.add(fk);
		}
		
		return foreingKeysOfVersion;
	}*/
}
