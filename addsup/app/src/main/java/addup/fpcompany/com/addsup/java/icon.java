package addup.fpcompany.com.addsup.java;

/**
 * Created by song02 on 2018-03-04.
 */

public class icon {
    String iconName;
    String iconId;


    public icon(String iconName, String iconId) {
        this.iconName = iconName;
        this.iconId = iconId;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    @Override
    public String toString() {
        return "icon{" +
                "iconName='" + iconName + '\'' +
                ", iconId='" + iconId + '\'' +
                '}';
    }

}
