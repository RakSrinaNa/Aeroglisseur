#include <Servo.h>
#include <TinkerKit.h>
#include <Time.h>
#include <FileIO.h>
#include <Bridge.h>

const String speed_key = "vi";
const String orientation_key = "or";
const String sustentation_key = "st";
const String values_file = "variables.txt";

Servo motor1;
//Servo motor2;
Servo motor3;
//Servo motor4;
TKPotentiometer potentiometer(I0);
int speed_value;
int orientation_value;
int sustentation_value;

void printMessage(String message)
{
    Serial.println(String(hour()) + ":" + String(minute()) + ":" + String(second()) + ":" + String(millis()) + " -> " + message);
}

int RPMToServoSpeed(unsigned int rpm)
{
    if(rpm <= 0)
        return 90;
    return (90 + (int)(rpm / 151.22));
}

void writeToMotors(String key, int value)
{
    if(value < 0 || value > 10000 || (key == orientation_key && value == orientation_value) || (key == speed_key && value == speed_value) || (key == sustentation_key && value == sustentation_value))
        return;
    printMessage("Receiving key " + key + " with value " + value);
    if(key == speed_key)
    {
        printMessage("Setting speed to " + String(value) + " which is " + String(RPMToServoSpeed(value)));
        motor1.write(RPMToServoSpeed(value));
        speed_value = value;
    }
    else if(key == orientation_key)
    {
        printMessage("Setting orientation to " + String(value));
        orientation_value = value;
    }
    else if(key == sustentation_key)
    {
        if(value == 1)
        {
            printMessage("Switching sustentation ON");
            motor3.write(100);
            sustentation_value = 1;
        }
        else
        {
            printMessage("Switching sustentation OFF");
            sustentation_value = value;
            motor3.write(90);
        }
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
    speed_value = 0;
    orientation_value = 50;
    sustentation_value = 0;
    printMessage("Done!");
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
    motor3.attach(11); //Moteur 3 (Blanc) -> O0
    motor1.attach(10); //Moteur 4 (Marron) -> O1
    //motor4.attach(9);  //Moteur 3 (Rouge) -> O2
    //motor2.attach(6); //Moteur 2 (Orange) -> O3
    motor1.write(90);
    motor3.write(90);
    initAero();
}

void loop()
{
    decrypt(readValuesFile());
    delay(50);
}