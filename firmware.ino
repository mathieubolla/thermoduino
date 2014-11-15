unsigned long lastTime;

void setup() {
  Serial.begin(9600);
  lastTime = millis();
  analogReference(INTERNAL);
}

void loop() {
  if (lastTime + 1000 < millis()) {
    int value = analogRead(A0);
    Serial.println(value);
    lastTime = millis();
  }
}
