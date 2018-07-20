/**
 * Created by mycola on 10.05.2018.
 */
public class Run {
    private String build;
    private String area;
    private String part;
    private String browser;
    private String resolution;
    private String language;
    private String currency;
    private String status;
    private String description;
    private String pnr;
    private String card;
    private String documents;
    private String uid;
    private String message;
    private String lastStep;
    private String lastSubStep;
    private String periodicity;


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

    public String getCurrency() {
        return currency;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getPnr() {
        return pnr;
    }

    public String getCard() {
        return card;
    }

    public String getDocumens() {
        return documents;
    }

    public String getUid() {
        return uid;
    }

    public String getMessage() {
        return message;
    }

    public String getLastStep() {
        return lastStep;
    }

    public String getLastSubStep() {
        return lastSubStep;
    }

    public String getPeriodicity() {
        return periodicity;
    }

    public void setBuild(int build) {
        this.build = "" + build;
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

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public void setDocumens(String documents) {
        this.documents = documents;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLastStep(String lastStep) {
        this.lastStep = lastStep;
    }

    public void setLastSubStep(String lastSubStep) {
        this.lastSubStep = lastSubStep;
    }

    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }

    @Override
    public String toString() {
        return "\nRun{" +
                "build='" + build + '\'' +
                ", area='" + area + '\'' +
                ", part='" + part + '\'' +
                ", browser='" + browser + '\'' +
                ", resolution='" + resolution + '\'' +
                ", language='" + language + '\'' +
                ", currency='" + currency + '\'' +
                ", status='" + status + '\'' +
                ", pnr='" + pnr + '\'' +
                ", card='" + card + '\'' +
                ", uid='" + uid + '\'' +
                "}";
    }

}
