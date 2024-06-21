package main.java.com.Filter.Data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
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

    String header_ISO_3166_1_ALPHA_2;
    String header_ISO_3166_1_ALPHA_2_REGION_CODE;
    String header_REGION1;
    String header_REGION2;
    String header_REGION3;
    String header_REGION4;
    String header_POSTLEITZAHL;
    String header_ORT;
    String header_AREA1;
    String header_AREA2;
    String header_LATITUDE;
    String header_LONGITUDE;
    String header_ZEITZONE;
    String header_CALC_LOCALE;
    String header_SOMMERZEIT;
    String header_ACTIVE;

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
                ", header_ISO_3166_1_ALPHA_2='" + header_ISO_3166_1_ALPHA_2 + '\'' +
                ", header_ISO_3166_1_ALPHA_2_REGION_CODE='" + header_ISO_3166_1_ALPHA_2_REGION_CODE + '\'' +
                ", header_REGION1='" + header_REGION1 + '\'' +
                ", header_REGION2='" + header_REGION2 + '\'' +
                ", header_REGION3='" + header_REGION3 + '\'' +
                ", header_REGION4='" + header_REGION4 + '\'' +
                ", header_POSTLEITZAHL='" + header_POSTLEITZAHL + '\'' +
                ", header_ORT='" + header_ORT + '\'' +
                ", header_AREA1='" + header_AREA1 + '\'' +
                ", header_AREA2='" + header_AREA2 + '\'' +
                ", header_LATITUDE='" + header_LATITUDE + '\'' +
                ", header_LONGITUDE='" + header_LONGITUDE + '\'' +
                ", header_ZEITZONE='" + header_ZEITZONE + '\'' +
                ", header_CALC_LOCALE='" + header_CALC_LOCALE + '\'' +
                ", header_SOMMERZEIT='" + header_SOMMERZEIT + '\'' +
                ", header_ACTIVE='" + header_ACTIVE + '\'' +
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
