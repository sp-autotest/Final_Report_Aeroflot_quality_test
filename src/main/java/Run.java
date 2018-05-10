/**
 * Created by mycola on 10.05.2018.
 */
public class Run {
    private String build;
    private String browser;
    private String resolution;
    private String area;
    private String part;
    private String language;
    private String status;
    private String description;


    public String getBuild() {
        return build;
    }

    public String getBrowser() {
        return browser;
    }

    public String getResolution() {
        return resolution;
    }

    public String getArea() {
        return area;
    }

    public String getPart() {
        return part;
    }

    public String getLanguage() {
        return language;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "\nRun{" +
                "build='" + build + '\'' +
                ", browser='" + browser + '\'' +
                ", resolution='" + resolution + '\'' +
                ", area='" + area + '\'' +
                ", part='" + part + '\'' +
                ", language='" + language + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
