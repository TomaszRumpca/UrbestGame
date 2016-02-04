package pl.gda.pg.tomrumpc.urbestgame.navigation;

public class NavigationUtils {

    public static final double EQUATORIAL_EARTH_RADIUS = 6378137;
//    public static final double POLAR_EARTH_RADIUS = 6378137;
    public static final double E2 = EQUATORIAL_EARTH_RADIUS;
//            ((Math.pow(EQUATORIAL_EARTH_RADIUS,2d)) - (Math.pow(POLAR_EARTH_RADIUS,2d))) /
//                    (Math.pow(EQUATORIAL_EARTH_RADIUS,2d)) ;


    private NavigationUtils() {}

    public static double radToDegrees(double rad) {
        return ((180 * Math.PI) / rad);
    }

    public static double degreesToRad(double degrees) {
        return (degrees / 180 * Math.PI);
    }

    public static float[] fromMToB(float[] M, float[] rotFromBToM) {
        float[] B = new float[3];
        B[0] = M[0] * rotFromBToM[0] +
                M[1] * rotFromBToM[3] +
                M[2] * rotFromBToM[6];
        B[1] = M[0] * rotFromBToM[1] +
                M[1] * rotFromBToM[4] +
                M[2] * rotFromBToM[7];
        B[2] = M[0] * rotFromBToM[2] +
                M[1] * rotFromBToM[5] +
                M[2] * rotFromBToM[8];
        return B;
    }

    public static double[] fromMToB(double[] M, float[] rotFromBToM) {
        double[] B = new double[3];
        B[0] = M[0] * rotFromBToM[0] +
                M[1] * rotFromBToM[3] +
                M[2] * rotFromBToM[6];
        B[1] = M[0] * rotFromBToM[1] +
                M[1] * rotFromBToM[4] +
                M[2] * rotFromBToM[7];
        B[2] = M[0] * rotFromBToM[2] +
                M[1] * rotFromBToM[5] +
                M[2] * rotFromBToM[8];
        return B;
    }

    public static float[] fromBToM(float[] B, float[] rotFromBToM) {
        float[] M = new float[3];
        M[0] = B[0] * rotFromBToM[0] +
                B[1] * rotFromBToM[1] +
                B[2] * rotFromBToM[2];
        M[1] = B[0] * rotFromBToM[0 + 3] +
                B[1] * rotFromBToM[1 + 3] +
                B[2] * rotFromBToM[2 + 3];
        M[2] = B[0] * rotFromBToM[0 + 6] +
                B[1] * rotFromBToM[1 + 6] +
                B[2] * rotFromBToM[2 + 6];
        return M;
    }

    //doesn't work
    public static double[] geoCoordinatesToECEF(double latitude, double longitude, double height){
        double v = EQUATORIAL_EARTH_RADIUS / (Math.sqrt(1d - (E2 - Math.pow(Math.sin(latitude), 2d))));
        double[] ecef = new double[3];
        ecef[0] = (v + height) * Math.cos(latitude) * Math.cos(longitude);
        ecef[1] = (v + height) * Math.cos(latitude) * Math.sin(longitude);
        ecef[2] = (v*(1 - E2) + height)* Math.sin(latitude);
        return ecef;
    }

    public static double[] simpleGeoCoordinatesToECEF(double latitude, double longitude, double height){
        double earthRadius = E2;
        double[] ecef = new double[3];
        ecef[0] = (earthRadius + height) * Math.cos(latitude) * Math.cos(longitude);
        ecef[1] = (earthRadius + height) * Math.cos(latitude) * Math.sin(longitude);
        ecef[2] = (earthRadius + height) * Math.sin(latitude);
        return ecef;
    }

    public  static double[] getENU(double latitude, double longitude, double height,
            double objectLatitude, double objectLongitude, double objectHeight){

        double[] userECEF = simpleGeoCoordinatesToECEF(latitude, longitude, height);
        double[] objectECEF = simpleGeoCoordinatesToECEF(objectLatitude, objectLongitude, objectHeight);
        double[] ECEF = new double[3];
        ECEF[0] = userECEF[0] - objectECEF[0];
        ECEF[1] = userECEF[1] - objectECEF[1];
        ECEF[2] = userECEF[2] - objectECEF[2];

        double[] matrix = new double[9];
        //row 1
        matrix[0] = -Math.sin(objectLongitude);
        matrix[1] = Math.cos(objectLongitude);
        matrix[2] = 0;
        //row 2
        matrix[3] = -Math.cos(objectLongitude) * Math.sin(objectLatitude);
        matrix[4] = -Math.sin(objectLongitude) * Math.sin(objectLatitude);
        matrix[5] = Math.cos(objectLatitude);
        //row 3
        matrix[6] = Math.cos(objectLongitude) * Math.cos(objectLatitude);
        matrix[7] = Math.sin(objectLongitude) * Math.cos(objectLatitude);
        matrix[8] = Math.sin(objectLatitude);

//        float[] matrix = new float[9];
//        matrix[0] =(float) -Math.sin(objectLongitude);
//        matrix[1] =(float) Math.cos(objectLongitude);
//        matrix[2] = 0;
//        //row 2
//        matrix[3] = (float)-Math.cos(objectLongitude) * (float)Math.sin(objectLatitude);
//        matrix[4] = (float)-Math.sin(objectLongitude) * (float)Math.sin(objectLatitude);
//        matrix[5] = (float)Math.cos(objectLatitude);
//        //row 3
//        matrix[6] = Math.cos(objectLongitude) * Math.cos(objectLatitude);
//        matrix[7] = Math.sin(objectLongitude) * Math.cos(objectLatitude);
//        matrix[8] = Math.sin(objectLatitude);

        double[] enu = new double[3];
        enu[0] = (matrix[0] * ECEF[0] + matrix[1] * ECEF[1]);
        enu[1] = (matrix[3] * ECEF[0] + matrix[4] * ECEF[1] + matrix[5] * ECEF[2]);
        enu[2] = (matrix[6] * ECEF[0] + matrix[7] * ECEF[1] + matrix[8] * ECEF[2]);

        return enu;
    }

    public  static double[] getENU(double latitude, double longitude, double height,
                                   double[] objectECEF){

        double[] userECEF = simpleGeoCoordinatesToECEF(latitude, longitude, height);
        double[] ECEF = new double[3];
        ECEF[0] = userECEF[0] - objectECEF[0];
        ECEF[1] = userECEF[1] - objectECEF[1];
        ECEF[2] = userECEF[2] - objectECEF[2];

        double[] matrix = new double[9];
        //row 1
        matrix[0] = -Math.sin(longitude);
        matrix[1] = Math.cos(longitude);
        matrix[2] = 0;
        //row 2
        matrix[3] = -Math.cos(longitude) * Math.sin(latitude);
        matrix[4] = -Math.sin(longitude) * Math.sin(latitude);
        matrix[5] = Math.cos(latitude);
        //row 3
        matrix[6] = Math.cos(longitude) * Math.cos(latitude);
        matrix[7] = Math.sin(longitude) + Math.cos(latitude);
        matrix[8] = Math.sin(latitude);

        double[] enu = new double[3];
        enu[0] = (matrix[0] * ECEF[0] + matrix[1] * ECEF[1] + matrix[2] * ECEF[2]);
        enu[1] = (matrix[3] * ECEF[0] + matrix[4] * ECEF[1] + matrix[5] * ECEF[2]);
        enu[2] = (matrix[6] * ECEF[0] + matrix[7] * ECEF[1] + matrix[8] * ECEF[2]);

        return enu;
    }

    public static double getAngle(float[] a, float[] b) {
        return Math.acos((scalarProduct(a,b)) / (vLength(a) * vLength(b)));
    }

    public static double vLength (float[] vector){
        return Math.sqrt(scalarProduct(vector,vector));
    }

    public static double scalarProduct(float[] a, float[] b){
        double sum = 0;
        if(a.length == b.length){
            for (int i = 0; i < a.length; i++) {
                sum += a[i]*b[i];
            }
        }else{
            throw new IllegalArgumentException("Parameters must have the same length");
        }
        return sum;
    }

}
