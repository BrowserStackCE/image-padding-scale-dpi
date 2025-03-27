# Pad images for image injection feature

## Setup
* Clone the repo
* Install dependencies
* Update the input and output directories
* Run the code to generate the padded images. 

## Custom Configurations
* Based on the device set the DPI. Update the mmPixels variable. 522 is the DPI value for Samsung Galaxy S24 Ultra
```
  double mmToPixels = 522.0 / 25.4;
  ```
* Adjust the coordinates based on the needs
```
            int backgroundWidthMM = 105;
            int backgroundHeightMM = 148;
            int imageWidthMM = 115;
            int imageHeightMM = 66;
            int xCoordinateMM = -12;
            int yCoordinateMM = 40;
```