package main.java.com.Filter.Data;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class FileSrcData {

    String ISO_3166_1_ALPHA_2;
    String ISO_3166_1_ALPHA_2_REGION_CODE;
    String REGION1;
    String REGION2;
    String REGION3;
    String REGION4;
    int POSTLEITZAHL;
    // Location? Bezirk? Ort? (Nicht klar beschrieben)
    String ORT;
    String AREA1;
    String AREA2;
    double LATITUDE;
    double LONGITUDE;
    String ZEITZONE;
    int CALC_LOCALE;
    boolean SOMMERZEIT;
    String ACTIVE;

    @Override
    public String toString() {
        return "FileSrcData{" +
                "ISO_3166_1_ALPHA_2='" + ISO_3166_1_ALPHA_2 + '\'' +
                ", ISO_3166_1_ALPHA_2_REGION_CODE='" + ISO_3166_1_ALPHA_2_REGION_CODE + '\'' +
                ", REGION1='" + REGION1 + '\'' +
                ", REGION2='" + REGION2 + '\'' +
                ", REGION3='" + REGION3 + '\'' +
                ", REGION4='" + REGION4 + '\'' +
                ", POSTLEITZAHL=" + POSTLEITZAHL +
                ", ORT='" + ORT + '\'' +
                ", AREA1='" + AREA1 + '\'' +
                ", AREA2='" + AREA2 + '\'' +
                ", LATITUDE=" + LATITUDE +
                ", LONGITUDE=" + LONGITUDE +
                ", ZEITZONE='" + ZEITZONE + '\'' +
                ", CALC_LOCALE=" + CALC_LOCALE +
                ", SOMMERZEIT=" + SOMMERZEIT +
                ", ACTIVE=" + ACTIVE +
                '}';
    }

    /**
     * Check most important fields (!= null)
     * @return true if checked fields have valid values, false otherwise
     */
    public boolean isValid(){
        return StringUtils.isNoneEmpty(
                this.getREGION1(),
                this.getREGION3(),
                this.getREGION4(),
                this.getORT()
        ) && this.getPOSTLEITZAHL() > 10000 && this.getPOSTLEITZAHL() < 100000;
    }
}
