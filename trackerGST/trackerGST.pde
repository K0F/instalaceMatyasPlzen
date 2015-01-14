
////////////////////////////////
import processing.video.*;
import blobDetection.*;

BlobDetection theBlobDetection;
PImage img;
boolean newFrame=false;


////////////////////////////////////////////////////////////////////////////
import oscP5.*;
import netP5.*;
OscP5 oscP5;
import codeanticode.gsvideo.*;
////////////////////////////////////////////////////////////////////////////

GSPipeline pipeline[] = new GSPipeline[4];


int w = 640;
int h = 480;


float TRESHOLD = 120;
float SMOOTHING  = 10.0;

int MAXDETI = 5;

////////////////////////////////////////////////////////////////////////////

float brightestX = 0; // X-coordinate of the brightest video pixel
float brightestY = 0; // Y-coordinate of the brightest video pixel



float xpos, ypos, xspeed, yspeed, maccel;
int session_id, s_id, total_id;
int currentFrame = 0;

int offset = 11;
int cams[] = {
  offset, offset+60, offset+120, offset+160
};
int xs[] = {
  0, w/2, 0, w/2
};
int ys []= {
  0, 0, h/2, h/2
};



////////////////////////////////////////////////////////////////////////////

void setup() {
  size(w, h);
  
  frameRate(15);
  
  textFont(loadFont("Dialog.plain-10.vlw"));

  OscProperties properties = new OscProperties();

  properties.setRemoteAddress("127.0.0.1", 3333);

  //properties.setListeningPort(3333);
  //properties.setSRSP(OscProperties.ON);
  //properties.setDatagramSize(1024);
  oscP5 = new OscP5(this, properties);


  for (int i = 0; i < pipeline.length; i++) {  
    pipeline[i] = new GSPipeline(this, "v4l2src device=/dev/video"+(i)+" ! ffmpegcolorspace ! videorate ! video/x-raw-rgb, width=320,framerate=15/1");  
    println(pipeline[i].getPipeline());
    pipeline[i].play();
  }
  
  
  img = new PImage(80,60); 
  theBlobDetection = new BlobDetection(img.width, img.height);
  theBlobDetection.setPosDiscrimination(true);
  theBlobDetection.setThreshold(0.05);

  frame.setTitle("catgame tracker v0.5");
}


//////////////////////////////////////////////////////////////////////////////////////

void draw() {
  
  background(0);
  
  
  for (int i = 0; i < pipeline.length; i++) {
    if (pipeline[i].available()) {
      pipeline[i].read();
      image(pipeline[i], xs[i], ys[i], width/2, height/2);
      
    }
  }

  loadPixels();

  float brightestValue = TRESHOLD; // Brightness of the brightest video pixel


/*
  int index = 0;
  int objcnt = 0;
  
  for (int y = 0; y < height; y++) {
    for (int x = 0; x < width; x++) {
      int pixelValue = pixels[index];
      float pixelBrightness = brightness(pixelValue);
      
      float d = dist(brightestX,brightestY,x,y);
       
      //if (d > 100 && objcnt < MAXDETI && 
      
      if(pixelBrightness > brightestValue && pixelBrightness > TRESHOLD) {
        
        brightestValue = pixelBrightness;
        brightestY += (y - brightestY) / SMOOTHING;
        brightestX += (x - brightestX) / SMOOTHING;
       }
      index++;
    }
  }
  */
  
  
      img.copy(g, 0, 0, width, height, 
        0, 0, img.width, img.height);
      fastblur(img, 2);
    theBlobDetection.computeBlobs(img.pixels);
    drawBlobsAndEdges(true,true);

  
}


//////////////////////////////////////////////////////////////////////////////////////

void exit() {
  for (int i = 0; i < 4; i++)
    cursorDelete(i);
  super.exit();
}


//////////////////////////////////////////////////////////////////////////////////////

