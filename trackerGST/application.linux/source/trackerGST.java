import processing.core.*; 
import processing.xml.*; 

import processing.video.*; 
import blobDetection.*; 
import oscP5.*; 
import netP5.*; 
import codeanticode.gsvideo.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class trackerGST extends PApplet {


////////////////////////////////



BlobDetection theBlobDetection;
PImage img;
boolean newFrame=false;


////////////////////////////////////////////////////////////////////////////


OscP5 oscP5;

////////////////////////////////////////////////////////////////////////////

GSPipeline pipeline[] = new GSPipeline[4];


int w = 640;
int h = 480;


float TRESHOLD = 30;
float SMOOTHING  = 10.0f;

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

public void setup() {
  size(w, h,P2D);
  
  frameRate(30);
  
  textFont(loadFont("Dialog.plain-10.vlw"));

  OscProperties properties = new OscProperties();

  properties.setRemoteAddress("127.0.0.1", 3333);

  //properties.setListeningPort(3333);
  //properties.setSRSP(OscProperties.ON);
  //properties.setDatagramSize(1024);
  oscP5 = new OscP5(this, properties);


  for (int i = 0; i < pipeline.length; i++) {  
    pipeline[i] = new GSPipeline(this, "v4l2src device=/dev/video"+(i)+" ! ffmpegcolorspace ! videorate ! video/x-raw-rgb,width=320,framerate=60/1");  
    println(pipeline[i].getPipeline());
    pipeline[i].play();
  }
  
  
  img = new PImage(80,60); 
  theBlobDetection = new BlobDetection(img.width, img.height);
  theBlobDetection.setPosDiscrimination(true);
  theBlobDetection.setThreshold(0.05f);

  frame.setTitle("catgame tracker v0.5");
}


//////////////////////////////////////////////////////////////////////////////////////

public void draw() {
  
//  background(0);
  
  
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

public void exit() {
  for (int i = 0; i < 4; i++)
    cursorDelete(i);
  super.exit();
}


//////////////////////////////////////////////////////////////////////////////////////

// ==================================================
// drawBlobsAndEdges()
// ==================================================
public void drawBlobsAndEdges(boolean drawBlobs, boolean drawEdges)
{
  noFill();
  Blob b;
  EdgeVertex eA,eB;
  for (int n=0 ; n<theBlobDetection.getBlobNb() ; n++)
  {
    b=theBlobDetection.getBlob(n);
    if (b!=null)
    {
      // Edges
      if (drawEdges)
      {
        strokeWeight(3);
        stroke(0,255,0);
        for (int m=0;m<b.getEdgeNb();m++)
        {
          eA = b.getEdgeVertexA(m);
          eB = b.getEdgeVertexB(m);
          if (eA !=null && eB !=null)
            line(
              eA.x*width, eA.y*height, 
              eB.x*width, eB.y*height
              );
        }
      }

      // Blobs
      if (drawBlobs)
      {
        
        float XX = b.xMin*width + b.w*width / 2;
        float YY = b.yMin*height + b.h*height / 2;
        
        strokeWeight(1);
        stroke(255,0,0);
        fill(255,0,0);
        text(n,XX,YY);
        if(n<5) 
	SendData(XX, YY, n, 5);
 noFill();
        rect(
          b.xMin*width,b.yMin*height,
          b.w*width,b.h*height
          );
      }

    }

      }
}
// ==================================================
// Super Fast Blur v1.1
// by Mario Klingemann 
// <http://incubator.quasimondo.com>
// ==================================================
public void fastblur(PImage img,int radius)
{
 if (radius<1){
    return;
  }
  int w=img.width;
  int h=img.height;
  int wm=w-1;
  int hm=h-1;
  int wh=w*h;
  int div=radius+radius+1;
  int r[]=new int[wh];
  int g[]=new int[wh];
  int b[]=new int[wh];
  int rsum,gsum,bsum,x,y,i,p,p1,p2,yp,yi,yw;
  int vmin[] = new int[max(w,h)];
  int vmax[] = new int[max(w,h)];
  int[] pix=img.pixels;
  int dv[]=new int[256*div];
  for (i=0;i<256*div;i++){
    dv[i]=(i/div);
  }

  yw=yi=0;

  for (y=0;y<h;y++){
    rsum=gsum=bsum=0;
    for(i=-radius;i<=radius;i++){
      p=pix[yi+min(wm,max(i,0))];
      rsum+=(p & 0xff0000)>>16;
      gsum+=(p & 0x00ff00)>>8;
      bsum+= p & 0x0000ff;
    }
    for (x=0;x<w;x++){

      r[yi]=dv[rsum];
      g[yi]=dv[gsum];
      b[yi]=dv[bsum];

      if(y==0){
        vmin[x]=min(x+radius+1,wm);
        vmax[x]=max(x-radius,0);
      }
      p1=pix[yw+vmin[x]];
      p2=pix[yw+vmax[x]];

      rsum+=((p1 & 0xff0000)-(p2 & 0xff0000))>>16;
      gsum+=((p1 & 0x00ff00)-(p2 & 0x00ff00))>>8;
      bsum+= (p1 & 0x0000ff)-(p2 & 0x0000ff);
      yi++;
    }
    yw+=w;
  }

  for (x=0;x<w;x++){
    rsum=gsum=bsum=0;
    yp=-radius*w;
    for(i=-radius;i<=radius;i++){
      yi=max(0,yp)+x;
      rsum+=r[yi];
      gsum+=g[yi];
      bsum+=b[yi];
      yp+=w;
    }
    yi=x;
    for (y=0;y<h;y++){
      pix[yi]=0xff000000 | (dv[rsum]<<16) | (dv[gsum]<<8) | dv[bsum];
      if(x==0){
        vmin[y]=min(y+radius+1,hm)*w;
        vmax[y]=max(y-radius,0)*w;
      }
      p1=x+vmin[y];
      p2=x+vmax[y];

      rsum+=r[p1]-r[p2];
      gsum+=g[p1]-g[p2];
      bsum+=b[p1]-b[p2];

      yi+=w;
    }
  }

}

//////////////////////////////////////////////////////////////////////////////////////

public void SendData(float x, float y, int i, int totalId) {

  ///totalID is all Id\u2019s tat I have in my blob array
  xpos = map(x, 0, width, 0, 1);
  ypos = map(y, 0, height, 0, 1);
  session_id = i;
  s_id = totalId;

  OscBundle blobsBundle = new OscBundle();
  OscMessage aliveMessage = new OscMessage("/tuio/2Dcur");
  aliveMessage.add("alive");
  aliveMessage.add(s_id);

  OscMessage setMessage = new OscMessage("/tuio/2Dcur");
  setMessage.add("set");
  setMessage.add(session_id);
  setMessage.add(xpos);
  setMessage.add(ypos);
  setMessage.add(xspeed);
  setMessage.add(yspeed);
  setMessage.add(maccel);

  currentFrame++;
  OscMessage frameMessage = new OscMessage("/tuio/2Dcur");
  frameMessage.add("fseq");

  frameMessage.add(currentFrame);

  blobsBundle.add(aliveMessage);

  blobsBundle.add(frameMessage);
  blobsBundle.add(setMessage);


  oscP5.send(blobsBundle);

  //rect(10, 10, 10, 10);
}



//////////////////////////////////////////////////////////////////////////////////////


public void completeCursorMessage(int x, int y, int i, int totalId) {
  xpos = map(x, 0, width/2, 0, 1);
  ypos = map(y, 0, height/2, 0, 1);
  session_id = i;
  s_id = totalId;

  OscBundle oscBundle = new OscBundle();

  OscMessage frameMessage = new OscMessage("/tuio/2Dcur");
  frameMessage.add("fseq");
  frameMessage.add(-1);

  OscMessage aliveMessage = new OscMessage("/tuio/2Dcur");
  aliveMessage.add("alive");

  OscMessage setMessage = new OscMessage("/tuio/2Dcur");
  setMessage.add("set");
  setMessage.add(session_id);
  setMessage.add(xpos);
  setMessage.add(ypos);
  setMessage.add(xspeed);
  setMessage.add(yspeed);
  setMessage.add(maccel);

  currentFrame++;



  oscBundle.add(frameMessage);
  oscP5.send(oscBundle);
}


//////////////////////////////////////////////////////////////////////////////////////

public void cursorDelete( int totalId) {
  s_id = totalId;

  OscBundle cursorBundle = new OscBundle();
  OscMessage aliveMessage = new OscMessage("/tuio/2Dcur");
  aliveMessage.add("alive");
  aliveMessage.add(s_id);

  currentFrame++;
  OscMessage frameMessage = new OscMessage("/tuio/2Dcur");
  frameMessage.add("fseq");
  frameMessage.add(currentFrame);

  cursorBundle.add(aliveMessage);
  cursorBundle.add(frameMessage);

  oscP5.send(cursorBundle);
} 



//////////////////////////////////////////////////////////////////////////////////////



class Trackpoint{
 PVector pos;

 Trackpoint(float _x, float _y){
   pos = new PVector(_x,_y);
 }
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#DFDFDF", "trackerGST" });
  }
}
