package org.code.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class BLEDevice {
	
	private String writeUUID="12345";
	
	private String notifyUUID="asdsd";
	
	
	private BluetoothDevice bluetoothDevice;
	
	private BluetoothGatt  bluetoothGatt;
	
	
	private BluetoothGattCharacteristic writeChar;
	private BluetoothGattCharacteristic notifyChar;
	
	
	private DeviceListener deviceListener;
	
	public void setDeviceListener(DeviceListener deviceListener) {
		this.deviceListener = deviceListener;
	}
	
	
	int tag;
	public void setTag(int tag) {
		this.tag = tag;
	}
	
	private Context context;
	
	
	public BLEDevice(Context  context) {
		
		this.context=context;
		 
		// TODO Auto-generated constructor stub
	}

	public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
		this.bluetoothDevice = bluetoothDevice;
	}
	
	public void connect(){
		
	     bluetoothGatt= 	bluetoothDevice.connectGatt(context, false, bluetoothGattCallback);
	     
	}
	
	
	
	private BluetoothGattCallback bluetoothGattCallback=new BluetoothGattCallback() {

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			// TODO Auto-generated method stub
			super.onCharacteristicChanged(gatt, characteristic);
			
			deviceListener.onGetValue(tag,characteristic.getValue());
			
			
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			super.onCharacteristicRead(gatt, characteristic, status);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// TODO Auto-generated method stub
			super.onCharacteristicWrite(gatt, characteristic, status);
		}

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			// TODO Auto-generated method stub
			super.onConnectionStateChange(gatt, status, newState);
			
			if(newState==BluetoothGatt.STATE_CONNECTED){
				Log.e("", " 连接成功");
				
				
				deviceListener.onConnected(tag);
				
				gatt.discoverServices();
				
			}else  if(newState==BluetoothGatt.STATE_DISCONNECTED){
				Log.e("", " 连接  断开");
				deviceListener.onDisconnected(tag);
			}
			
		}

		@Override
		public void onDescriptorRead(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			// TODO Auto-generated method stub
			super.onDescriptorRead(gatt, descriptor, status);
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			// TODO Auto-generated method stub
			super.onDescriptorWrite(gatt, descriptor, status);
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			// TODO Auto-generated method stub
			super.onReadRemoteRssi(gatt, rssi, status);
		}

		@Override
		public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
			// TODO Auto-generated method stub
			super.onReliableWriteCompleted(gatt, status);
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			// TODO Auto-generated method stub
			super.onServicesDiscovered(gatt, status);
			
			Log.e("", "发现到服务");
			
			 List<BluetoothGattService>  services= bluetoothGatt.getServices();
			 
			 for (BluetoothGattService bluetoothGattService : services) {
			      List<BluetoothGattCharacteristic>	bluetoothGattCharacteristics= bluetoothGattService.getCharacteristics();
			      
				for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattCharacteristics) {
					Log.e("", " bluetoothGattCharacteristic:"+bluetoothGattCharacteristic.getUuid().toString());
					
					
					if(bluetoothGattCharacteristic.getUuid().toString().equals(writeUUID)){
						writeChar=bluetoothGattCharacteristic;
					}else if(bluetoothGattCharacteristic.getUuid().toString().equals(notifyUUID)){
						notifyChar=bluetoothGattCharacteristic;
						
						enableNotify(notifyChar);
					}
				}
			}
			
		}
	};
	
	
	public void sendValue(byte [] values){
		
		writeChar.setValue(values);
		
		bluetoothGatt.writeCharacteristic(writeChar);
		
	}

	protected void enableNotify(BluetoothGattCharacteristic notifyChar) {
		
	}

	public interface DeviceListener {

		public void onConnected(int deviceTag);

		public void onDisconnected(int deviceTag);

		public void onGetValue(int deviceTag,byte [] values);
	}

}
