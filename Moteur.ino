  #include <Servo.h>
  #include <TinkerKit.h>
  #include <Time.h>
  #include <FileIO.h>
  #include <Bridge.h>
  
  Servo motor1;
  //Servo motor2;
  Servo motor3;
  //Servo motor4;
  TKPotentiometer pot(I0);
  int valvi;
  int valor;
  int valst;
  
  void afficherMessage(String message)
  {
     Serial.println(String(hour()) + ":" + String(minute()) + ":" + String(second()) + ":" + String(millis()) + " -> " + message); 
  }
  int getSpeedToServo(unsigned int spd)
  {
    if (spd == 0)
    {
      return 90;
    }
    return (119 + (int)(spd / 151.22));
  }
  
  void receid(String key, int value)
  {
    if(value < 0 || value > 10000 || (key == "or" && value == valor) || (key == "vi" && value == valvi) || (key == "st" && value == valst))
    {
      return;
    }
    afficherMessage("Recieving key " + key + " with value " + value);
    if(key == "vi")
    {
      afficherMessage("On modifie la vitesse a " + String(value) + "soit" + String(getSpeedToServo(value)));
      motor1.write(getSpeedToServo(value));
      valvi = value;
    }
    else if (key == "or")
    {
      afficherMessage("On modifie l'orientation a " + String(value));
      valor = value;
    }
    else if (key == "st")
    {
      if (value == 1)
      {
        afficherMessage("On  allume la sustentation");
        valst = 1;
        motor3.write(100);
      }
      else
      {
        afficherMessage("On  eteint la sustentation");
        valst = value;
        motor3.write(90);
      }
    }
    else
    {
      afficherMessage("Error, tag not reconized! (" + key + ")");
    }
  }
  
  void decrypt(String inp)
  {
    char charBuf[inp.length() + 1];
    inp.toCharArray(charBuf, inp.length());
    char *p = charBuf;
    char *str;
    while ((str = strtok_r(p, "\n", &p)) != NULL)
    {
      String temp = str;
      if (temp.indexOf('=') < 0)
      {
         return; 
      }
      receid(temp.substring(0, temp.indexOf('=')), temp.substring(temp.indexOf('=') + 1).toInt());
    }
  }
  
  void setup()
  {
    Bridge.begin();
    Serial.begin(9600);
    afficherMessage("Starting arduino!");
    pinMode(13, OUTPUT);
    digitalWrite(13, LOW);
    digitalWrite(13, HIGH);
    motor3.attach(11); //Moteur 3 (Blanc) -> O0
    motor1.attach(10); //Moteur 4 (Marron) -> O1
    //motor4.attach(9);  //Moteur 3 (Rouge) -> O2
    //motor2.attach(6); //Moteur 2 (Orange) -> O3
    motor1.write(90);
    motor3.write(90);
    initAero();
  }
  
  void initAero()
  {
     afficherMessage("Copying default values to txt file...");
     Process p;            
     p.begin("cp");      
     p.addParameter("-f"); 
     p.addParameter("/mnt/sd/arduino/www/variables_start.txt"); 
     p.addParameter("/mnt/sd/arduino/www/variables.txt"); 
     p.run();
     valvi = 0;
     valor = 50;
     valst = 0;
     afficherMessage("Done!");
  }
  String getTextString()
  {
     Process p;            
     p.begin("head");      
     p.addParameter("/mnt/sd/arduino/www/variables.txt"); 
     p.run();
     String getted = "";
     while(p.available() > 0) 
     {
       char c = p.read();
       getted += c;
     }
     return getted;
  }
  
  void loop()
  {
    delay(50);
    decrypt(getTextString());
  }
