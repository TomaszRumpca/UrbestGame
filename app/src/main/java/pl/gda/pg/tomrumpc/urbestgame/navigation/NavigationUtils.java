package pl.gda.pg.tomrumpc.urbestgame.navigation;

public class NavigationUtils {

    public static final float EARTH_RADIUS = 6378137;

    private NavigationUtils() {

    }

    public static double radToDegrees(double rad) {
        return (rad / 180 * Math.PI);
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

    public static float[] LatLonToECEF(float lat, float lon, float h) {
        float[] ecef = new float[3];
        ecef[0] = (float) ((EARTH_RADIUS + h) * Math.cos(lat) * Math.cos(lon));
        ecef[1] = (float) ((EARTH_RADIUS + h) * Math.cos(lat) * Math.sin(lon));
        ecef[2] = (float) ((EARTH_RADIUS + h) * Math.sin(lat));
        return ecef;
    }


    public static double[] LatLonToECEF(double lat, double lon, float h) {
        double[] ecef = new double[3];
        ecef[0] = (EARTH_RADIUS + h) * Math.cos(lat) * Math.cos(lon);
        ecef[1] = (EARTH_RADIUS + h) * Math.cos(lat) * Math.sin(lon);
        ecef[2] = (EARTH_RADIUS + h) * Math.sin(lat);
        return ecef;
    }

    //namiar na objects ze wspolrzednych geograficznych
    public static float[] getENU(float lat, float lon, float h, float[] objectEcef) {

        float[] userEcef = LatLonToECEF(lat, lon, h);
        float[] ecef = new float[3];
        ecef[0] = userEcef[0] - objectEcef[0];
        ecef[1] = userEcef[1] - objectEcef[1];
        ecef[2] = userEcef[2] - objectEcef[2];

        double[] matrix = new double[9];
        //row 1
        matrix[0] = -Math.sin(lon);
        matrix[1] = Math.cos(lon);
        matrix[2] = 0;
        //row 2
        matrix[3] = -Math.cos(lon) * Math.sin(lat);
        matrix[4] = -Math.sin(lat) * Math.sin(lon);
        matrix[5] = Math.cos(lat);
        //row 3
        matrix[6] = Math.cos(lat) * Math.cos(lon);
        matrix[7] = Math.sin(lon) + Math.cos(lat);
        matrix[8] = Math.sin(lat);


        float[] enu = new float[3];
        enu[0] = (float) (matrix[0] * ecef[0] + matrix[1] * ecef[1] + matrix[2] * ecef[2]);
        enu[1] = (float) (matrix[3] * ecef[0] + matrix[4] * ecef[1] + matrix[5] * ecef[2]);
        enu[2] = (float) (matrix[6] * ecef[0] + matrix[7] * ecef[1] + matrix[8] * ecef[2]);

        return enu;
    }

    public static double getAngle(float[] a, float[] b) {

        double temp = (a[0] * b[0] + a[1] * b[1] + a[2] * b[2]) /
                (Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]) * Math.sqrt(b[0] * b[0] + b[1] * b[1] + b[2] * b[2]));

        return Math.acos(temp);
    }


}
