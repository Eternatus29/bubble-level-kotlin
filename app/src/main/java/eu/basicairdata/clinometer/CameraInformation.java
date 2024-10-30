package eu.basicairdata.clinometer;

public class CameraInformation {

    public static final int REAR_CAMERA = 0;
    public static final int FRONT_CAMERA = 1;
    public static final int EXTERNAL_CAMERA = 2;

    public int id = -1;
    public int type = -1;
    public String description = "";
    public int minExposureCompensation = 0;
    public int maxExposureCompensation = 0;
    public float horizontalViewAngle = 0;
}
