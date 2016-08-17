package com.tomorrow_p.common;


public class LbsUtil {
	private final static double EARTH_RADIUS = 6378137.0;

	/**
	 * @param 纬度a
	 * @param 经度a
	 * @param 纬度b
	 * @param 经度b
	 * @return 两次的距离
	 */
	public static double gps2m(double lat_a, double lon_a, double lat_b, double lon_b) {
		double radLat1 = (lat_a * Math.PI / 180.0);
		double radLat2 = (lat_b * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (lon_a - lon_b) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		return s;
	}

	/**
	 * 格式化距离
	 */
	public static String distanceReverse(double paramInt) {
		String str1 = null;
		float f = (float) (paramInt / 1000.0F);// 2004
		String str2 = String.format("%1.2f", f);
		str1 = str2 + "km";
		return str1;
	}

	//	/**
	//	 * 得到经纬度
	//	 */
	//	public static void getLatLng(Context context, final SPUtil sp) {
	//		mLocationClient = new LocationClient(context);
	//		LocationClientOption option = new LocationClientOption();
	//		option.setOpenGps(true); // 打开gps
	//		option.setCoorType("bd09ll"); // 设置坐标类型为bd09ll
	//		option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
	//		option.setProdName("maizuo"); // 设置产品线名称
	//		option.setScanSpan(1000 * 5); // 定时定位，每隔5秒钟定位一次。
	//		mLocationClient.setLocOption(option);
	//		mLocationClient.registerLocationListener(new BDLocationListener() {
	//			@Override
	//			public void onReceiveLocation(BDLocation location) {
	//				if (location == null)
	//					return;
	//				sp.putString("latitude", location.getLatitude() + "");
	//				sp.putString("longitude", location.getLongitude() + "");
	//				//System.out.println("=========================latitude:" + location.getLatitude());
	//				//System.out.println("===========================longitude:" + location.getLongitude());
	//				// mLocationClient.stop();
	//			}
	//
	//			@Override
	//			public void onReceivePoi(BDLocation arg0) {
	//				// TODO
	//			}
	//		});
	//		mLocationClient.start();
	//	}
	//
	//	/**
	//	 * 停止定位服务
	//	 */
	//	public static void stopLbsService() {
	//		if (mLocationClient != null && mLocationClient.isStarted()) {
	//			mLocationClient.stop();
	//			mLocationClient = null;
	//		}
	//	}
}
