
package generated;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * <p>Java class for Player complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Player"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="wins" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="age" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "",propOrder = {
    "name",
    "wins",
    "age"
})
@XmlRootElement(name = "Player")
public class Player {

    @XmlElement(namespace = "/jaxb/gen", required = true)
    protected String name;
    @XmlElement(namespace = "/jaxb/gen")
    protected int wins;
    @XmlElement(namespace = "/jaxb/gen")
    protected int age;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the wins property.
     * 
     */
    public int getWins() {
        return wins;
    }

    /**
     * Sets the value of the wins property.
     * 
     */
    public void setWins(int value) {
        this.wins = value;
    }

    /**
     * Gets the value of the age property.
     * 
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the value of the age property.
     * 
     */
    public void setAge(int value) {
        this.age = value;
    }

}
