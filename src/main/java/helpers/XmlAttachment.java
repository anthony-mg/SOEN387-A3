package helpers;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "attachment")
public class XmlAttachment {
    @XmlAttribute(name = "id")
    private int id;
    @XmlElement(name = "name")
    private String name;

    public void setAttachmentId(int id){
        this.id = id;
    }

    public void setAttachmentName(String name){
        this.name = name;
    }
}
