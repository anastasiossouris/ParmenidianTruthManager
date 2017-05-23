/**
 * 
 */
package externalTools;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author iskoulis
 *
 */
@XmlRootElement
public class Insersion extends Transition {

	public Insersion() {
		super();
	}
	
	public void table(Table newTable) {
		super.table(newTable);
		this.type = "NewTable";
	}
}
