#include <RN487x_BLE.h>

#define bleSerial Serial1

const char* myDeviceName = "Compteur" ;  // Custom Device name
const char* ServiceUUID = "AD11CF40063F11E5BE3E0002A5D5C51B" ; // Custom private service UUID
const char* CharacteristicUUID = "BF3FBD80063F11E59E690002A5D5C501" ;  // custom characteristic GATT
const uint8_t CharacteristicLen = 2 ;  // data length (in bytes)
const uint16_t Handle = 0x72 ;
char Payload[CharacteristicLen*2] ;
uint8_t newValue_u8 = 0 ;
uint8_t prevValue_u8 = 0 ;
int i = 0;

void initLed()
{
  pinMode(LED_RED, OUTPUT) ;
  pinMode(LED_GREEN, OUTPUT) ;
  pinMode(LED_BLUE, OUTPUT) ;  
}

void setRgbColor(uint8_t red, uint8_t green, uint8_t blue)
{
  red = 255 - red ;
  green = 255 - green ;
  blue = 255 - blue ;

  analogWrite(LED_RED, red) ;
  analogWrite(LED_GREEN, green) ;
  analogWrite(LED_BLUE, blue) ;
}

void updateValue()
{
  prevValue_u8 = newValue_u8 ;
  newValue_u8 = i++ ;
  // Update the local characteristic only if value has changed
  if (newValue_u8 != prevValue_u8)
  {
    uint8_t data = newValue_u8 ;
    Payload[3] = '0' + (data % 10) ;
    data /= 10 ;
    Payload[2] = '0' + (data % 10) ;
    data /= 10 ;
    Payload[1] = '0' + (data % 10) ;
    Payload[0] = '0' ; // MSB = 0, positive temp.
  }
  rn487xBle.writeLocalCharacteristic(Handle, Payload);
}

void setup()
{
  initLed() ;

  // Initialize the BLE hardware
  rn487xBle.hwInit() ;
  // Open the communication pipe with the BLE module
  bleSerial.begin(rn487xBle.getDefaultBaudRate()) ;
  // Assign the BLE serial port to the BLE library
  rn487xBle.initBleStream(&bleSerial) ;
  // Finalize the init. process
  if (rn487xBle.swInit())
  {
    setRgbColor(0, 0, 255) ;
  }
  else
  {
    setRgbColor(255, 0, 255) ;
    while(1) ;
  }

  // First, enter into command mode
  rn487xBle.enterCommandMode() ;
  // Stop advertising before starting the demo
  rn487xBle.stopAdvertising() ;
  // Set the advertising output power (range: min = 5, max = 0)
  rn487xBle.setAdvPower(3) ;
  // Set the serialized device name, i.e. device n,ame + 2 last bytes from MAC address.
  rn487xBle.setSerializedName(myDeviceName) ;
  rn487xBle.clearAllServices() ;
  rn487xBle.reboot() ;
  rn487xBle.enterCommandMode() ;  
  // Set a private service
  rn487xBle.setServiceUUID(ServiceUUID) ;
  // characteristic; readable and can perform notification, 2-octets size
  rn487xBle.setCharactUUID(CharacteristicUUID, READ_PROPERTY | NOTIFY_PROPERTY, CharacteristicLen) ;     
  // take into account the settings by issuing a reboot
  rn487xBle.reboot() ;
  rn487xBle.enterCommandMode() ;
  // Clear adv. packet
  rn487xBle.clearImmediateAdvertising() ;
  // Start adv.
  // The line below is required, to let an Android device discover the board. 
  rn487xBle.startImmediateAdvertising(AD_TYPE_FLAGS, "06");
  rn487xBle.startImmediateAdvertising(AD_TYPE_MANUFACTURE_SPECIFIC_DATA, "CD00FE14AD11CF40063F11E5BE3E0002A5D5C51B") ;
}

void loop()
{
  // Check the connection status
  if (rn487xBle.getConnectionStatus())
  {
    setRgbColor(0, 255, 0) ;
    updateValue();
  }
  else
  {
    setRgbColor(255, 0, 0) ;
  }
  // Delay inter connection polling
  delay(10000) ;
}
