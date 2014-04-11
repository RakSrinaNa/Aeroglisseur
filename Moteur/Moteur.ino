#include <Servo.h>
#include <TinkerKit.h>
#include <FileIO.h>
#include <Bridge.h>

const int rpm_sustent = 100;
const String speed_key = "vi";
const String orientation_key = "or";
const String sustentation_key = "st";
const String horizontal_cam_key = "ch";
const String vertical_cam_key = "cv";
const String values_file = "variables.txt";

Servo motorSustentation;
Servo motorDirection;
Servo servoMotor;
Servo servoVertCam;
Servo servoHorCam;
TKPotentiometer potentiometer(I0);
int speed_value;
int orientation_value;
int sustentation_value;
int vetical_cam_value;
int horizontal_cam_value;

void printMessage(String message)
{
    Serial.println(String(millis()/1000) + " -> " + message);
}

int oriToDegrees(unsigned int ori)
{
  return (15 + (int)(ori/70.0));
}

int RPMToServoSpeed(unsigned int rpm)
{
    if(rpm < 0)
        return 0;
    else if(rpm == 0)
        return 90;
    return (119 + (int)(rpm / 151.22));
}

void writeToMotors(String key, int value)
{
    if(value < 0 || value > 10000 || 
    (key == orientation_key && value == orientation_value) || 
    (key == speed_key && value == speed_value) || 
    (key == sustentation_key && value == sustentation_value) || 
    (key == horizontal_cam_key && value == horizontal_cam_value) || 
    (key == vertical_cam_key && value == vetical_cam_value))
        return;
    printMessage("Receiving key " + key + " with value " + value);
    if(key == speed_key)
    {
        printMessage("Setting speed to " + String(value) + " which is " + String(RPMToServoSpeed(value)));
        motorDirection.write(RPMToServoSpeed(value));
        speed_value = value;
    }
    else if(key == orientation_key)
    {
        printMessage("Setting orientation to " + String(value));
        orientation_value = value;
        servoMotor.write(oriToDegrees(value));
    }
    else if(key == sustentation_key)
    {
        if(value == 1)
        {
            printMessage("Switching sustentation ON");
            motorSustentation.write(RPMToServoSpeed(rpm_sustent));
            sustentation_value = 1;
        }
        else
        {
            printMessage("Switching sustentation OFF");
            sustentation_value = value;
            motorSustentation.write(RPMToServoSpeed(0));
        }
     }
     else if(key == horizontal_cam_key)
    {
        printMessage("Setting orientation CamHorizontal to " + String(value));
        horizontal_cam_value = value;
        servoHorCam.write(oriToDegrees(value));
   
    }
     else if(key == vertical_cam_key)
    {
        printMessage("Setting orientation CamVertival to " + String(value));
        vetical_cam_value = value;
        servoVertCam.write(map(100 - value, 0, 100, 20, 70));
    }
    else
        printMessage("Error, key not recognized! (" + key + ")");
}

void decrypt(String input)
{
    char charBuffer[input.length() + 1];
    input.toCharArray(charBuffer, input.length());
    char *lines = charBuffer;
    char *line;
    while((line = strtok_r(lines, "\n", &lines)) != NULL)
    {
        String temp = line;
        if (temp.indexOf('=') < 0)
            return;
        writeToMotors(temp.substring(0, temp.indexOf('=')), temp.substring(temp.indexOf('=') + 1).toInt());
    }
}

void initAero()
{
    printMessage("Copying default values to txt file...");
    Process process;
    process.begin("cp");
    process.addParameter("-f");
    process.addParameter("/mnt/sd/arduino/www/variables_start.txt");
    process.addParameter("/mnt/sd/arduino/www/" + values_file);
    process.run();
    speed_value = -1;
    sustentation_value = 0;
    orientation_value = 50;
    vetical_cam_value = 50;
    horizontal_cam_value = 70;
    writeToMotors(speed_key, speed_value);
    writeToMotors(sustentation_key, sustentation_value);
    writeToMotors(orientation_key, orientation_value);
    writeToMotors(vertical_cam_key, vetical_cam_value);
    writeToMotors(horizontal_cam_key, horizontal_cam_value);
}

String readValuesFile()
{
    Process process;
    process.begin("head");
    process.addParameter("/mnt/sd/arduino/www/" + values_file);
    process.run();
    String response = "";
    while(process.available() > 0)
    {
        char c = process.read();
        response += c;
    }
     return response;
}

void setup()
{
    Bridge.begin();
    Serial.begin(9600);
    printMessage("Starting arduino!");
    pinMode(13, OUTPUT);
    digitalWrite(13, LOW);
    digitalWrite(13, HIGH);
    motorDirection.attach(10); //Moteur 3 (Blanc) -> O0
    motorSustentation.attach(11); //Moteur 4 (Marron) -> O1
    servoMotor.attach(9); //O2
    servoVertCam.attach(6); //O3
    servoHorCam.attach(5); //O4
    initAero();
    printMessage("Done!");
}

void loop()
{
    decrypt(readValuesFile());
    delay(50);
}
