#include <Servo.h>
#include <TinkerKit.h>

Servo motor1;
//Servo motor2;
//Servo motor3;
Servo motor4;
TKPotentiometer pot(I0);

int getSpeedToServo(unsigned int spd)
{
  if(spd == 0)
  {
    return 90;  
  }
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

String getTag(String s)
{
  return s.substring(s.indexOf('#') + 1, s.indexOf('='));
}

int getValue(String s)
{
  return s.substring(s.indexOf('=') + 1).toInt();
}

void decrypt(String inp)
{
    receid(getTag(inp), getValue(inp)); 
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
  String inString = "";
  while (Serial.available() > 0) 
  {
    int inChar = Serial.read();
      inString += (char)inChar;
  }
  if(inString != "")
  {
    Serial.println(inString);
    decrypt(inString);
  }
  delay(1000);
}






