#include <Wire.h>
//#include <LiquidCrystal_I2C.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <WiFi.h>
#define USE_ARDUINO_INTERRUPTS true    // Set-up low-level interrupts for most acurate BPM math
//#include <PulseSensorPlayground.h> 
#define SOS_LEDS 9
#define BUZZER 9
#define BUTTON1 3
#define BUTTON2 5
#define BUTTON3 6
#define BUTTON4 0
#define SOS_BUTTON 2
#define HEARTBEAT_SENSOR A0
#define VIBRATION_ENGINE 8
//OLED:

#define SCREEN_WIDTH 128 // OLED display width, in pixels
#define SCREEN_HEIGHT 64 // OLED display height, in pixels

#define I2C_SDA 5
#define I2C_SCL 4


#define OLED_RESET     -1 // Reset pin # (or -1 if sharing Arduino reset pin)
#define SCREEN_ADDRESS 0x3C ///< See datasheet for Address; 0x3D for 128x64, 0x3C for 128x32

typedef enum {
  IDLE,
  ASSESSMENT,
  SOS
} SYSTEM_STATUS;

typedef enum {
   SERVICE,
   Q1,
   Q2,
   Q3,
   Q4
} STAGE;

typedef enum {
  POLICE,
  MADA,
  FIRE
} SERV;

//PulseSensorPlayground pulseSensor;

/* OLED Instance */

Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RESET);
//LiquidCrystal_I2C lcd(0x3F, 20, 4);


/* Comm Params */
WiFiClient myClient;
char MsgBuffer[64];
boolean msgReceived = false;
boolean connected = false;
SYSTEM_STATUS state = IDLE;
STAGE stage = SERVICE;
SERV chosenService;
void SendMessage(char msg[]);
void AttemptConnection(char ssid[],IPAddress myIP);
void initAssessmentMode();
void WaitForResponse();
void UpdateAssessmentRoutine();
void ConfigGPIO();
void InitOLED();
int status;
IPAddress IPAddr(192,168,66,63);
char ssid[] = "Hack";
void setup() {
  ConfigGPIO();
  InitOLED();

//  pulseSensor.analogInput(HEARTBEAT_SENSOR);
// pulseSensor.setThreshold(563);
//  pulseSensor.begin();

  AttemptConnection(ssid,IPAddr);
  if(!connected /*&& false*/)
  {
    display.clearDisplay();

    display.setTextSize(2); // Draw 2X-scale text
    display.setTextColor(SSD1306_WHITE);
    display.setCursor(10, 0);
    display.println(F("scroll"));
    display.display();      // Show initial text
    delay(100);

    // lcd.clear();
    // lcd.setCursor(4,1);
    // lcd.print("System Error!");
    for(int i = 0;i<100;i++)
    {
      delay(15);
      digitalWrite(SOS_LEDS,HIGH);
      delay(15);
      digitalWrite(SOS_LEDS,LOW);
    }
    while(1);
  }
  else
  {
    // lcd.clear();
    // lcd.setCursor(4,1);
    // lcd.print("SoS System");
    // lcd.setCursor(5,2);
    // lcd.print("Online");
  }
  Serial.begin(9600);
}

int samples = 0;
int timeMeasured = 0;
int startTime = 0;
void loop() {
    static unsigned long lastPressSOS = 0,lastPress1 = 0,lastPress2 = 0,lastPress3 = 0,lastPress4 = 0;
    if(millis() - lastPressSOS > 250)
      {
      if(state != SOS)
      {
        if(digitalRead(SOS_BUTTON) == HIGH)
        {
          lastPressSOS = millis();
          startTime = millis();
          state = SOS;
          SendMessage("0AA444SOS");
          initAssessmentMode();
          UpdateAssessmentRoutine();
        }
      }
    else
    {
      lastPressSOS = millis();
      while(digitalRead(SOS_BUTTON) == HIGH);
      if(millis() - lastPressSOS > 2000)
      {
        digitalWrite(SOS_LEDS,HIGH);
        state = IDLE;
        stage = SERVICE;
        // lcd.clear();
        // lcd.setCursor(5,2);
        // lcd.print("SOS Aborted!");
        delay(2000);
        digitalWrite(SOS_LEDS,LOW);
        SendMessage("0AA444ABORT");
        // lcd.clear();
        // lcd.setCursor(4,1);
        // lcd.print("SoS System");
        // lcd.setCursor(5,2);
        // lcd.print("Online");
      }
    }
      }
    
    if(millis() - lastPress1 > 250)
    {
      if(digitalRead(BUTTON1) == HIGH)
      {
        lastPress1 = millis();
        if(state == IDLE)
        {
          state = ASSESSMENT;
          initAssessmentMode();
          UpdateAssessmentRoutine();
        }
        else
        {
          if(stage == SERVICE)
          {
            SendMessage("0AA444ESH");
            chosenService = FIRE;
            stage = Q1;
            UpdateAssessmentRoutine();
          }
          else if(stage == Q1)
          {
            stage = SERVICE;
            switch(chosenService)
            {
              case POLICE:
                    SendMessage("0AA444POLICEQ1A1");
                    break;
              case MADA:
                    SendMessage("0AA444MADAQ1A1");
                    break;
              case FIRE:
                    SendMessage("0AA444FIREQ1A1");
                    break;
            }
            state = state == SOS? SOS : IDLE;
            // lcd.clear();
            // lcd.setCursor(5,2);
            // lcd.print("Data Sent!");
            // delay(2000);
            // lcd.clear();
            // lcd.clear();
            // lcd.setCursor(4,1);
            // lcd.print("SoS System");
            // lcd.setCursor(5,2);
            // lcd.print("Online");
          }
        }
      }
    }
    if(millis() - lastPress2 > 250)
    {
      if(digitalRead(BUTTON2) == HIGH)
      {
        lastPress2 = millis();
        if(state == IDLE)
        {
          state = ASSESSMENT;
          initAssessmentMode();
          UpdateAssessmentRoutine();
        }
        else
        {
          if(stage == SERVICE)
          {
            SendMessage("0AA444MADA");
            chosenService = MADA;
            stage = Q1;
            UpdateAssessmentRoutine();
          }
          else if(stage == Q1)
          {
            stage = SERVICE;
            switch(chosenService)
            {
              case POLICE:
                    SendMessage("0AA444POLICEQ1A2");
                    break;
              case MADA:
                    SendMessage("0AA444MADAQ1A2");
                    break;
              case FIRE:
                    SendMessage("0AA444FIREQ1A2");
                    break;
            }
            // state = state == SOS? SOS : IDLE;
            // lcd.clear();
            // lcd.setCursor(5,2);
            // lcd.print("Data Sent!");
            // delay(2000);
            // lcd.clear();
            // lcd.clear();
            // lcd.setCursor(4,1);
            // lcd.print("SoS System");
            // lcd.setCursor(5,2);
            // lcd.print("Online");
          }
        }
      }
    }
    if(millis() - lastPress3 > 250)
    {
      if(digitalRead(BUTTON3) == HIGH)
      {
        lastPress3 = millis();
        if(state == IDLE)
        {
          state = ASSESSMENT;
          initAssessmentMode();
          UpdateAssessmentRoutine();
        }
        else
        {
          if(stage == SERVICE)
          {
            SendMessage("0AA444POLICE");
            chosenService = POLICE;
            stage = Q1;
            UpdateAssessmentRoutine();
          }
          else if(stage == Q1)
          {
            stage = -1;
            switch(chosenService)
            {
              case POLICE:
                    SendMessage("0AA444POLICEQ1A3");
                    break;
              case MADA:
                    SendMessage("0AA444MADAQ1A3");
                    break;
              case FIRE:
                    SendMessage("0AA444FIREQ1A3");
                    break;
            }
            // state = state == SOS? SOS : IDLE;
            // lcd.clear();
            // lcd.setCursor(5,2);
            // lcd.print("Data Sent!");
            // delay(2000);
            // lcd.clear();
            // lcd.clear();
            // lcd.setCursor(4,1);
            // lcd.print("SoS System");
            // lcd.setCursor(5,2);
            // lcd.print("Online");
          }
        }
      }
    }

    if(state == SOS)
    {
       digitalWrite(SOS_LEDS,HIGH);
       digitalWrite(VIBRATION_ENGINE,HIGH);
       delay(80);
       digitalWrite(SOS_LEDS,LOW);
       digitalWrite(VIBRATION_ENGINE,LOW);
       delay(80);
       String header = "0AA444BPM";
      // String cmd = String(pulseSensor.getBeatsPerMinute());
       String f = String(header);// + cmd);
       if(millis() - startTime > 4000)
       {
          startTime = millis();
          SendMessage("0AA444BPM");
       }
     }
  }

void ConfigGPIO()
{
  /* OUTPUT GPIOS */
  pinMode(SOS_LEDS,OUTPUT);
  pinMode(VIBRATION_ENGINE,OUTPUT);
  /* INPUT GPIOS */  
  pinMode(BUTTON1,INPUT);
  pinMode(BUTTON2,INPUT);
  pinMode(BUTTON3,INPUT);
  pinMode(SOS_BUTTON,INPUT);

}

void AttemptConnection(char ssid[],IPAddress myIP)
 {
//   lcd.setCursor(3,0);
//   lcd.print("Connecting To");
//   lcd.setCursor(7,1);
//   lcd.print("Server...");
  status = WiFi.begin(ssid);
  if (status != WL_CONNECTED) {
    return;
  }
  if(myClient.connect(myIP,5555))
    connected = true;
}
void SendMessage(char msg[])
{
  myClient.println(msg);
}

void WaitForResponse()
{
  int byteCounter = 0;
  while(myClient.available() == 0)
  ;
  while(myClient.available() > 0)
    MsgBuffer[byteCounter++] = myClient.read();
  msgReceived = byteCounter == 0 ? false : true;
}

void InitOLED()
{
  
  Wire.begin(I2C_SDA, I2C_SCL);
  // SSD1306_SWITCHCAPVCC = generate display voltage from 3.3V internally
  if(!display.begin(SSD1306_SWITCHCAPVCC, SCREEN_ADDRESS,false,true)) {
    Serial.println(F("SSD1306 allocation failed"));
    for(;;); // Don't proceed, loop forever
  }
    Serial.println(F("\nSetup Started !"));
  // Show initial display buffer contents on the screen --
  // the library initializes this with an Adafruit splash screen.
  display.display();

}

void initAssessmentMode()
{
  // lcd.clear();
  for(int i = 0;i<4;i++)
  {
  //   lcd.setCursor(6,i);
  //   lcd.print("|");
  }
  for(int i = 0;i<4;i++)
   {
  //   lcd.setCursor(13,i);
  //   lcd.print("|");
  }
}

void UpdateAssessmentRoutine()
{
  if(state == IDLE) return;
  // lcd.clear();
  initAssessmentMode();
  switch(stage)
  {
     case SERVICE:
    //   lcd.setCursor(0,2);
    //   lcd.print("POLICE");
    //   lcd.setCursor(7,2);
    //   lcd.print("MADA");
    //   lcd.setCursor(14,2);
    //   lcd.print("FIRE");
    //   lcd.setCursor(14,3);
    //   lcd.print("DEPT");
      break;
    case Q1:
      if(chosenService == FIRE)
      {
        // lcd.setCursor(0,2);
        // lcd.print("STUCK");
        // lcd.setCursor(7,2);
        // lcd.print("FIRE");
        // lcd.setCursor(14,2);
        // lcd.print("RESCUE");
      }
      else if(chosenService == POLICE)
      {
        // lcd.setCursor(0,2);
        // lcd.print("BREAK");
        // lcd.setCursor(7,2);
        // lcd.print("CRIME");
        // lcd.setCursor(14,2);
        // lcd.print("OTHER");
      }
      else if(chosenService == MADA)
      {
        // lcd.setCursor(0,2);
        // lcd.print("HEART");
        // lcd.setCursor(7,2);
        // lcd.print("PAIN");
        // lcd.setCursor(14,2);
        // lcd.print("OTHER");
      }
      break;
    case Q2:
      break;
    case Q3:
      break;
    default:
      break;
  }
}
