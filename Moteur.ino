#include <Servo.h>
#include <TinkerKit.h>

Servo motor1;
//Servo motor2;
//Servo motor3;
Servo motor4;
TKPotentiometer pot(I0);

int getSpeedToServo(unsigned int spd)
{
  //Serial.println("Wanted speed: ");
  //Serial.print(spd);
  //Serial.print("    ToServo : ");
  if(spd == 0)
  {
    // Serial.print("90");
    return 90;  
  }
  //Serial.print(119 + (int)(spd/151.22));
  return (119 + (int)(spd/151.22));
}

void receid(String key, int value)
{
  if(key=="vi")
  {
    Serial.print("On modifie la vitesse a ");
    Serial.print(value);
    motor1.write(getSpeedToServo(value));
    Serial.print(" soit ");
    Serial.println(getSpeedToServo(value));

  }
  else if(key=="or")
  {
    Serial.print("On modifie l'orientation a ");
    Serial.println(value); 
  }
  else if(key=="st")
  {
    if (value==1)
    {
      motor4.write(110);
      Serial.println("On  allume la sustentation");
    }
    else
    {
      motor4.write(90);
      Serial.println("On  eteint la sustentation");
    }
  }
  else
  { 
    Serial.println("error,the command was not understand");
  }
}

int getVariableBin(unsigned int bin)
{
  switch(bin)
  {
  case 48: 
    return 0;
  case 49: 
    return 1;
  case 50: 
    return 2;
  case 51: 
    return 3;
  case 52: 
    return 4;
  case 53: 
    return 5;
  case 54: 
    return 6;
  case 55: 
    return 7;
  case 56: 
    return 8;
  case 57: 
    return 9;    
  default: 
    return -1;
  }  
}
String getValue(String s)
{
  return s.substring(s.indexOf('#') + 1, s.indexOf('='));
}

String getTag(String s)
{
  return s.substring(s.indexOf('=') + 1);
}
char* decrypt(String inp)
{
  if(inp.indexOf("#or=") >= 0 || inp.indexOf("#vi=")  >= 0  || inp.indexOf("#st=") >= 0 )
    return {getValue(s), getTag(s)};
}

void setup()
{
  //motor3.attach(11); //Moteur 3 (Blanc) -> O0
  motor1.attach(10); //Moteur 1 (Rouge) -> O1
  motor4.attach(9);  //Moteur 4 (Marron) -> O2
  //motor2.attach(6); //Moteur 2 (Orange) -> O3 
  Serial.begin(9600);
  Serial.println("Debut du programme");
}

void loop()
{
  String inData= "";
  if (Serial.available() > 0) 
  {
    int h = Serial.available();
    for (int i=0;i<h;i++)
    {
      inData += (char)Serial.read();
    }
    // if you are getting escape -characters try Serial.read(); here
  }
  Serial.println(decrypt(inData));
  delay(1000);
}




