#include <Servo.h>
#include <TinkerKit.h>

Servo motor1;
//Servo motor2;
Servo motor3;
//Servo motor4;
TKPotentiometer pot(I0);
int valvi;
int valor;
int valst;

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
  if(value < 0 || value > 10000)
  {
     Serial.println("La valeur voulue n'est pas dans l'encadrement possible!");
    return; 
  }
  if(key=="vi")
  {
    Serial.print("On modifie la vitesse a ");
    Serial.print(value);
    motor1.write(getSpeedToServo(value));
    Serial.print(" soit ");
    Serial.println(getSpeedToServo(value));
    valvi=value;
    if(valvi >= 10000)
    {
      valvi=0;
      motor1.write(90);
      pritnln("error in value vitesse,restart systeme");
    }
  }
  else if(key=="or")
  {
    Serial.print("On modifie l'orientation a ");
    Serial.println(value); 
    valor=value;
  }
  else if(key=="st")
  {
    if (value==1)
    {
      valst=1;
      motor3.write(100);
      Serial.println("On  allume la sustentation");
    }
    else
    {
      valst=0;
      motor3.write(90);
      Serial.println("On  eteint la sustentation");
    }
  }
  else if(key=="get")
  {
    if(value == 0)
    {
      Serial.println(getSpd());
    }
    else if(value == 1)
    {
      Serial.println(getOri());
    }
    else if(value == 2)
    {
      Serial.println(getSus());
    }
  }
  else
  { 
    Serial.println("Error, tag not reconized!");
  }
}

int getSpd()
{
  return valvi;
}
int getOri()
{
  return valor;
}
int getSus()
{
  return valst;
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
  motor3.attach(11); //Moteur 3 (Blanc) -> O0
  motor1.attach(10); //Moteur 4 (Marron) -> O1
  //motor4.attach(9);  //Moteur 3 (Rouge) -> O2
  //motor2.attach(6); //Moteur 2 (Orange) -> O3 
  motor1.write(90);
  motor3.write(90);
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
  delay(500);
}