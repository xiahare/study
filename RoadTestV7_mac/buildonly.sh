#pushd src/com/roadtest/
#downlaod chormedriver from here
# entry: https://sites.google.com/a/chromium.org/chromedriver/downloads
# version 91: https://chromedriver.storage.googleapis.com/index.html?path=91.0.4472.101/
# version 92: 
javac -cp "lib/*" src/com/roadtest/*.java
#jar cfm MyJar.jar manifest.txt  src/com/roadtest/*.class
[ -f RoadTestMac.jar ] && rm -rf RoadTestMac.jar
pushd src
#jar cfm ../RoadTestMac.jar ../manifest.txt com/roadtest/*.class
popd
#java -jar RoadTestMac.jar info.txt
#popd
