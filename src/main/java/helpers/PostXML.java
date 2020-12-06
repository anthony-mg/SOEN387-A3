package helpers;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;

@XmlRootElement(name = "post")
public class PostXML {
    @XmlAttribute(name = "postId")
    private int postId;
    @XmlElement(name = "title")
    private String title;
    @XmlElement(name = "group")
    private String group;
    @XmlElement(name = "user")
    private String user;
    @XmlElement(name = "createdDate")
    private String createdDate;
    @XmlElement(name = "updatedDate")
    private String updatedDate;
    @XmlElement(name = "text")
    private String text;
    @XmlElementWrapper(name = "attachments")
    @XmlElement(name = "attachment")
    private ArrayList<XmlAttachment> attachments;


    public void setAttachments(ArrayList<XmlAttachment> attachments) {
        this.attachments = attachments;
    }
    public ArrayList<XmlAttachment> getAttachmentList() {
        return attachments;
    }

    public void addAttachment(XmlAttachment xmlAttachment) {
        try {
            if (attachments == null) {
                attachments = new ArrayList<XmlAttachment>();
            }
            attachments.add(xmlAttachment);
        } catch (Exception e) {
        }
    }

    public void setPostId(int postId){
        this.postId = postId;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setGroup(String group){
        this.group = group;
    }

    public void setUser(String user){
        this.user = user;
    }

    public void setCreatedDate(String createdDate){
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(String updatedDate){
        this.updatedDate = updatedDate;
    }

    public void setText(String text){
        this.text = text;
    }
}
